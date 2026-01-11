package addsynth.core.gameplay.blocks.music_box.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public final class Track {

  public boolean mute;
  public byte instrument;
  public final Note[] note = new Note[MusicGrid.frames];

  public Track(){
    byte i;
    for(i = 0; i < MusicGrid.frames; i++){
      note[i] = new Note();
    }
  }
  
  public final CompoundTag getNBT(){
    final CompoundTag track_tag = new CompoundTag();
    track_tag.putBoolean("Mute", mute);
    track_tag.putByte("Instrument", instrument);
    final ListTag note_list = new ListTag();
    byte i;
    for(i = 0; i < MusicGrid.frames; i++){
      note_list.add(note[i].getNBT());
    }
    track_tag.put("Notes", note_list);
    return track_tag;
  }

  public final void load_from_nbt(final CompoundTag track_tag){
    mute       = track_tag.getBoolean("Mute");
    instrument = track_tag.getByte("Instrument");
    final ListTag note_list = track_tag.getList("Notes", Tag.TAG_COMPOUND);
    byte i;
    for(i = 0; i < MusicGrid.frames; i++){
      note[i].load_from_nbt(note_list.getCompound(i));
    }
  }

  public final void setFrom(final Track track){
    mute = track.mute;
    instrument = track.instrument;
    byte i;
    for(i = 0; i < MusicGrid.frames; i++){
      note[i].setFrom(track.note[i]); // I don't want to think about assigning reference variables right now.
    }
  }

}
