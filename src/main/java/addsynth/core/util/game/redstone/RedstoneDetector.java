package addsynth.core.util.game.redstone;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class RedstoneDetector {

  private final boolean direct_detection;
  private boolean previous_state;
  private boolean powered;
  private int power_level;
  private RedstoneState state;

  public RedstoneDetector(){
    direct_detection = false;
  }

  public RedstoneDetector(boolean use_direct_redstone_detection){
    direct_detection = use_direct_redstone_detection;
  }

  /** Call to update the Redstone State. */
  public void update(final Level world, final BlockPos worldPosition){
    update(world, worldPosition, false);
  }

  /** Call to update the Redstone State. Returns true if state changed. */
  public boolean update(final Level world, final BlockPos worldPosition, final boolean changed){
    previous_state = powered;
    power_level = direct_detection ? world.getDirectSignalTo(worldPosition) : world.getBestNeighborSignal(worldPosition);
    powered = power_level > 0;
    state = RedstoneState.get(powered, previous_state);
    return (powered != previous_state) || changed;
  }

  /** Call to update the Redstone State. */
  public void update(final Level world, final Iterable<BlockPos> positions){
    update(world, positions, false);
  }

  /** Call to update the Redstone State. Returns true if state changed. */
  public boolean update(final Level world, final Iterable<BlockPos> positions, final boolean changed){
    previous_state = powered;
    power_level = 0;
    for(final BlockPos position : positions){
      if(direct_detection){
        power_level = Math.max(world.getDirectSignalTo(position), power_level);
      }
      else{
        power_level = Math.max(world.getBestNeighborSignal(position), power_level);
      }
    }
    powered = power_level > 0;
    state = RedstoneState.get(powered, previous_state);
    return (powered != previous_state) || changed;
  }

  public boolean onRisingEdge(){return state == RedstoneState.RISING_EDGE;}
  public boolean onFallingEdge(){return state == RedstoneState.FALLING_EDGE;}
  public boolean changed(){return powered != previous_state;}

  public boolean isPowered(){return powered;}
  public boolean isOff(){return !powered;}
  public int getPowerLevel(){return power_level;} // It's over 9,000!!!!

  public void load(final CompoundTag tag){
    final CompoundTag data = tag.getCompound("Redstone");
    power_level = data.getInt("Power Level");
    powered = power_level > 0;
    previous_state = data.getBoolean("Previous");
    state = RedstoneState.get(powered, previous_state);
  }

  public void save(final CompoundTag tag){
    final CompoundTag data = new CompoundTag();
    data.putInt("Power Level", power_level);
    data.putBoolean("Previous", previous_state);
    tag.put("Redstone", data);
  }

  public void setFrom(final RedstoneDetector redstone){
    previous_state = redstone.previous_state;
    powered = redstone.powered;
    power_level = redstone.power_level;
    state = redstone.state;
  }

}
