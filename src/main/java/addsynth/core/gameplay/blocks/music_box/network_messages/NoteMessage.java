package addsynth.core.gameplay.blocks.music_box.network_messages;

import addsynth.core.gameplay.blocks.music_box.TileMusicBox;
import addsynth.core.util.network.TileEntityNetworkMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class NoteMessage extends TileEntityNetworkMessage<TileMusicBox> {

  private final byte frame;
  private final byte track;
  private final boolean on;
  private final byte note;
  private final float volume;

  public NoteMessage(BlockPos position, byte frame, byte track, byte note, float volume){
    super(position, TileMusicBox.class);
    this.frame = frame;
    this.track = track;
    this.on = true;
    this.note = note;
    this.volume = volume;
  }

  public NoteMessage(BlockPos position, byte frame, byte track){
    super(position, TileMusicBox.class);
    this.frame = frame;
    this.track = track;
    this.on = false;
    note = 0;
    volume = 0;
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeBoolean(on);
    buf.writeByte(frame);
    buf.writeByte(track);
    buf.writeByte(note);
    buf.writeFloat(volume);
  }

  public static final NoteMessage decode(final FriendlyByteBuf buf){
    final BlockPos position = buf.readBlockPos();
    if(buf.readBoolean()){
      return new NoteMessage(position, buf.readByte(), buf.readByte(), buf.readByte(), buf.readFloat());
    }
    return new NoteMessage(position, buf.readByte(), buf.readByte());
  }

  @Override
  protected final void handle(final ServerLevel level, final ServerPlayer player, final TileMusicBox tile){
    if(on){
      tile.set_note(track, frame, note);
    }
    else{
      tile.disable_note(track, frame);
    }
  }

}
