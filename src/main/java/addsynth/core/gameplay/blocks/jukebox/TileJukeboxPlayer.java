package addsynth.core.gameplay.blocks.jukebox;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import addsynth.core.ADDSynthCore;
import addsynth.core.game.inventory.filter.TypeFilter;
import addsynth.core.game.item.ItemUtil;
import addsynth.core.game.tiles.TileStorageMachine;
import addsynth.core.gameplay.registers.Tiles;
import addsynth.core.util.game.tileentity.ITickingTileEntity;
import addsynth.core.util.math.random.RandomUtil;
import addsynth.core.util.time.MinecraftTime;
import addsynth.core.util.time.TimeConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;

public class TileJukeboxPlayer extends TileStorageMachine implements MenuProvider, ITickingTileEntity {

  public static final TypeFilter filter = new TypeFilter(RecordItem.class);
  private static final int max_slots = 27;
  private static final Random random = new Random();

  private int index;
  private Item current_item;
  private ItemStack current_disc = ItemStack.EMPTY;
  private boolean changed;

  private int slot;
  private int track_index;
  private int previous_tracks;
  private int tracks;
  /** Translation layer to convert tracks to slot indexes, because there could be gaps in the inventory. */
  private final Map<Integer, Integer> trackToSlots = new HashMap<Integer, Integer>(max_slots);
  private final Map<Integer, Integer> temp_map = new HashMap<Integer, Integer>(max_slots);
  @Nonnull
  private int[] track_list = new int[0];

  private boolean repeat_single;
  private int play_time;
  private int song_time;
  private boolean shuffle;
  private boolean is_playing;
  private boolean is_in_delay;
  private int redstone_signal;
  private boolean output_redstone = true;
  private int delay_time;

  /** Cannot start and stop in the same tick! So we must play on the next tick! */
  private boolean reloaded;

  public TileJukeboxPlayer(BlockPos position, BlockState blockstate){
    super(Tiles.AUTO_JUKEBOX.get(), position, blockstate, 27, filter);
  }

  public final boolean hasMusicDiscs(){
    return tracks > 0;
  }

  // ==============================================================================================

  @Override
  public void onLoad(){
    // recreate trackToSlots Map
    trackToSlots.clear();
    tracks = 0;
    for(slot = 0; slot < max_slots; slot++){
      if(!input_inventory.getStackInSlot(slot).isEmpty()){
        trackToSlots.put(tracks, slot);
        tracks++;
      }
    }
  }

  @Override
  public final void onInventoryChanged(){
    final Level level = this.level;
    if(level != null){
      if(!level.isClientSide){ // after finishing this, I realize this might not be needed
        update();
        changed = true;
      }
    }
  }

  // S1: Added first item
  // S2: Removed all items
  // S3: Removed current item (decrement tracks and reload)
  // S4: Swapped out current item
  // S5: Removed item before current (decrement index & tracks)
  // S6: Removed item after  current (decrement tracks)
  // S7: Added item before current (increment index & tracks)
  // S8: Added item after  current (increment tracks)
  private final void update(){
    // TODO: set an inventory_changed boolean when inventory changes, then run update script in tick function. May fix Music Disc not playing when removing the one playing.
    // Re-Evaluate Inventory
    previous_tracks = track_list.length;
    tracks = 0;
    temp_map.clear();
    for(slot = 0; slot < max_slots; slot++){
      if(!input_inventory.getStackInSlot(slot).isEmpty()){
        temp_map.put(tracks, slot);
        tracks++;
      }
    }
    if(tracks > 0){
      // Create track list if necessary
      if(previous_tracks == 0){
        index = 0;
        if(tracks == 1){
          track_list = new int[]{0};
        }
        else{
          createTrackList();
        }
        // trackToSlots = Map.copyOf(temp_map);
        trackToSlots.clear();
        trackToSlots.putAll(temp_map);
      }
      else{
        int i;
        switch(Integer.signum(tracks - previous_tracks)){
        case -1: // Discs were removed
          // loop through manually, so as not to produce a Concurrent Modification error
          // check if key exists in new map (could be null)
          // check if keys map to same values
          // remove entry in current map
          for(track_index = 0; track_index < previous_tracks; track_index++){
            if(temp_map.containsKey(track_index)){
              if(trackToSlots.get(track_index) == temp_map.get(track_index)){
                continue;
              }
            }
            // find removed track
            final int removed_index = ArrayUtils.indexOf(track_list, track_index);
            if(removed_index != -1){
              // remove track
              track_list = ArrayUtils.remove(track_list, removed_index);
              // decrement all tracks above the one removed
              final int length = track_list.length;
              for(i = 0; i < length; i++){
                if(track_list[i] > track_index){
                  track_list[i] -= 1;
                }
              }
              // decrement index if removed index < index
              if(track_index < index){
                index--;
              }
            }
            trackToSlots.clear();
            trackToSlots.putAll(temp_map);
            break;
          }
          break;
        case 1: // New Discs were added (Only 1 Disc at a time at the moment)
          for(track_index = 0; track_index < tracks; track_index++){
            if(trackToSlots.containsKey(track_index)){
              if(trackToSlots.get(track_index) == temp_map.get(track_index)){
                continue;
              }
            }
            if(shuffle){
              final int random_index = random.nextInt(track_list.length+1);
              track_list = ArrayUtils.insert(random_index, track_list, track_index);
              // increment all tracks that are higher than the new one
              final int length = track_list.length;
              for(i = 0; i < length; i++){
                if(i != random_index){
                  if(track_list[i] >= track_index){
                    track_list[i] += 1;
                  }
                }
              }
              if(random_index < index){
                index++;
              }
            }
            else{
              track_list = ArrayUtils.add(track_list, track_list.length);
              if(track_index <= index){
                index++;
              }
            }
            // to be proper, yeah, we would insert the element then increment all existing elements.
            trackToSlots.putAll(temp_map);
            break;
          }
          break;
        }
      }
    }
    else{
      // no more tracks
      track_list = new int[0];
      trackToSlots.clear();
    }
    loadMusicDisc();
  }

  @Override
  public final void serverTick(final ServerLevel level, BlockState blockstate){
    if(reloaded){
      if(hasMusicDiscs()){
        if(is_playing){
          startPlaying();
        }
      }
      else{
        setEmpty();
        is_playing = false;
      }
      reloaded = false;
      changed = true;
    }
    if(is_playing){
      play_time++;
      if(is_in_delay){
        if(play_time > delay_time){
          // load next track
          incrementTrackIndex(!repeat_single);
          is_in_delay = false;
        }
      }
      else{
        if(play_time % 20 == 0){
          // spawn note particle
          level.gameEvent(GameEvent.JUKEBOX_PLAY, worldPosition, GameEvent.Context.of(getBlockState()));
          final Vec3 vec3 = Vec3.atBottomCenterOf(worldPosition).add(0.0D, (double)1.2F, 0.0D);
          final float f = (float)level.getRandom().nextInt(4) / 24.0F;
          ((ServerLevel)level).sendParticles(ParticleTypes.NOTE, vec3.x(), vec3.y(), vec3.z(), 0, (double)f, 0.0D, 0.0D, 1.0D);
        }
        if(play_time > song_time){
          stopPlaying();
          if(delay_time > 0){
            play_time = 0;
            is_in_delay = true;
          }
          else{
            incrementTrackIndex(!repeat_single);
          }
        }
      }
      update_data(); // since we save play_time, which increments every tick, we save every tick.
    }
    else{
      if(changed){
        update_data();
        changed = false;
      }
    }
  }

  private void incrementTrackIndex(boolean predicate){
    if(predicate){
      index++;
      if(index >= tracks){
        reset();
      }
      loadMusicDisc(true); // even if next disc is the same, we still need to stop and restart.
      changed = true;
    }
    else{
      reloaded = true;
    }
  }

  public final boolean addMusicDisc(final ItemStack music_disc){
    final ItemStack remainder = input_inventory.add(music_disc);
    return remainder.isEmpty(); // empty means it was successfully added to inventory.
  }

  /** Loads Music Disc Data. */
  private final void loadMusicDisc(){
    loadMusicDisc(false);
  }

  // Make sure this only runs once per tick. 2nd time it'll see current_disc matches current_item and set reloaded to FALSE!
  private final void loadMusicDisc(boolean reload){
    current_item = current_disc.getItem();
    if(hasMusicDiscs()){
      tracks = Math.min(tracks, track_list.length); // simple safety check never hurt anybody
      if(index >= tracks){
        index = tracks - 1;
      }
      current_disc = input_inventory.getStackInSlot(trackToSlots.get(track_list[index]));
    }
    else{
      setEmpty();
    }
    reloaded = current_item != current_disc.getItem() || reload;
    if(reloaded){
      stopPlaying();
      current_item = current_disc.getItem();
      if(current_item instanceof RecordItem record){
        redstone_signal = record.getAnalogOutput();
        song_time = record.getLengthInTicks();
      }
      else if(current_item != Items.AIR){
        ADDSynthCore.log.error("Invalid item in Auto JukeBox: "+current_disc.getDisplayName().getString());
        input_inventory.ejectInvalidItems(level, worldPosition);
        setEmpty();
      }
      changed = true;
    }
  }

  private final void setEmpty(){
    current_disc = ItemStack.EMPTY;
    redstone_signal = 0;
    song_time = 0;
  }

  // ==============================================================================================

  private void startPlaying(){
    play_time = 0;
    final Level level = this.level;
    if(level != null){
      level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
      level.levelEvent((Player)null, 1010, this.getBlockPos(), Item.getId(current_disc.getItem()));
    }
  }

  private void stopPlaying(){
    final Level level = this.level;
    if(level != null){
      level.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.getBlockPos(), GameEvent.Context.of(this.getBlockState()));
      level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
      level.levelEvent(1011, this.getBlockPos(), 0);
    }
    play_time = 0;
  }

  public final void playStop(){
    if(is_playing){
      stopPlaying();
      is_playing = false;
    }
    else{ // not playing
      if(hasMusicDiscs()){
        startPlaying();
        is_playing = true;
      }
    }
    changed = true;
  }

  public final void skipLeft(){
    stopPlaying();
    index--;
    loadMusicDisc();
    changed = true;
  }

  public final void skipRight(){
    stopPlaying();
    incrementTrackIndex(true);
    changed = true;
  }

  public final void toggleShuffle(){
    shuffle = !shuffle;
    changed = true;
  }

  public final void toggleRepeat(){
    repeat_single = !repeat_single;
    changed = true;
  }

  public final void toggleRedstoneOutput(){
    output_redstone = !output_redstone;
    changed = true;
  }

  public final void reset(){
    stopPlaying();
    index = 0;
    is_in_delay = false;
    createTrackList();
    loadMusicDisc();
    changed = true;
  }

  private final void createTrackList(){
    onLoad(); // more safety features
    if(shuffle){
      track_list = RandomUtil.get_random_list(random, tracks);
      return;
    }
    track_list = new int[tracks];
    for(int i = 0; i < tracks; i++){
      track_list[i] = i;
    }
  }


  public final void setDelayTime(int seconds){
    delay_time = seconds * TimeConstants.ticks_per_second;
    changed = true;
  }

  public final void changeVolume(){
  }

  // ==============================================================================================

  @Override
  public final void load(CompoundTag nbt){
    super.load(nbt);
    track_list = nbt.getIntArray("Tracklist");
    tracks = nbt.getInt("Tracks");
    index = nbt.getInt("Index");
    shuffle = nbt.getBoolean("Shuffle");
    current_disc = ItemUtil.loadItem(nbt, "CurrentDisc");
    play_time = nbt.getInt("Time");
    song_time = nbt.getInt("Song Time");
    repeat_single = nbt.getBoolean("Repeat");
    output_redstone = nbt.getBoolean("Output Redstone");
    delay_time = nbt.getInt("Track Delay");
    is_playing = nbt.getBoolean("IsPlaying");
    is_in_delay = nbt.getBoolean("IsDelaying");
    // reloaded = nbt.getBoolean("Reload");
  }

  @Override
  protected final void saveAdditional(CompoundTag nbt){
    super.saveAdditional(nbt);
    nbt.putIntArray("Tracklist", track_list);
    nbt.putInt("Tracks", tracks);
    nbt.putInt("Index", index);
    nbt.putBoolean("Shuffle", shuffle);
    ItemUtil.saveItem(nbt, "CurrentDisc", current_disc);
    nbt.putInt("Time", play_time);
    nbt.putInt("Song Time", song_time);
    nbt.putBoolean("Repeat", repeat_single);
    nbt.putBoolean("Output Redstone", output_redstone);
    nbt.putInt("Track Delay", delay_time);
    nbt.putBoolean("IsPlaying", is_playing);
    nbt.putBoolean("IsDelaying", is_in_delay);
    // nbt.putBoolean("Reload", reloaded);
  }

  // Client functions
  public final boolean isPlaying(){return is_playing;}
  public final int getIndex(){return hasMusicDiscs() ? index+1 : 0;}
  public final int getNumberOfTracks(){return tracks;}
  public final ItemStack getItemStack(){return current_disc;}
  public final String getTime(){return MinecraftTime.print2(play_time);}
  public final String getTotalTime(){return MinecraftTime.print2(is_in_delay ? delay_time : song_time);}
  public final float getSongPercentage(){
    if(song_time == 0 || (is_in_delay && delay_time == 0)){
      return 0;
    }
    return (float)play_time / (is_in_delay ? delay_time : song_time);
  }
  public final boolean getShuffle(){return shuffle;}
  public final boolean getRepeat(){return repeat_single;}
  public final boolean getOutputRedstone(){ return output_redstone;}
  public final boolean canSkipLeft(){return index > 0;}
  public final boolean canSkipRight(){return tracks > 0;}
  public final String getStatus(){return is_playing ? is_in_delay ? "Track Delay" : "Playing" : "Stopped";}
  public final int getRedstoneSignal(){return output_redstone ? redstone_signal : 0;}

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int containerID, Inventory playerInventory, Player player){
    return new JukeboxContainer(containerID, playerInventory, this);
  }

  @Override
  public Component getDisplayName(){
    return getBlockState().getBlock().getName();
  }

}
