package addsynth.core.gameplay.blocks.music_box.data;

import net.minecraft.nbt.CompoundTag;

public final class Note {

  public boolean on;
  public byte pitch;
  public float volume = 1.0f;

  public Note(){}
  
  public Note(final Note note){
    setFrom(note);
  }

  public final CompoundTag getNBT(){
    final CompoundTag note_tag = new CompoundTag();
    note_tag.putBoolean("On", on);
    note_tag.putByte("Pitch", pitch);
    note_tag.putFloat("Volume", volume);
    return note_tag;
  }

  public final void load_from_nbt(final CompoundTag note_tag){
    on     = note_tag.getBoolean("On");
    pitch  = note_tag.getByte("Pitch");
    volume = note_tag.getFloat("Volume");
  }

  public final void set(final byte pitch){
    on     = true;
    this.pitch  = pitch;
    volume = 1.0f;
  }

  public final void setFrom(final Note note){
    on     = note.on;
    pitch  = note.pitch;
    volume = note.volume;
  }

}
