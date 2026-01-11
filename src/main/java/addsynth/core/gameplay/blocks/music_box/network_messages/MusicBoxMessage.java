package addsynth.core.gameplay.blocks.music_box.network_messages;

import addsynth.core.gameplay.blocks.music_box.TileMusicBox;
import addsynth.core.util.network.TileEntityNetworkMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class MusicBoxMessage extends TileEntityNetworkMessage<TileMusicBox> {

  private final TileMusicBox.Command command;
  private final byte info;

  public MusicBoxMessage(final BlockPos position, final TileMusicBox.Command command){
    this(position, command, 0);
  }

  public MusicBoxMessage(final BlockPos position, final TileMusicBox.Command command, final int data){
    super(position, TileMusicBox.class);
    this.command = command;
    this.info = (byte)data;
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeEnum(command);
    buf.writeByte(info);
  }

  public static final MusicBoxMessage decode(final FriendlyByteBuf buf){
    return new MusicBoxMessage(buf.readBlockPos(), buf.readEnum(TileMusicBox.Command.class), buf.readByte());
  }

  @Override
  protected final void handle(final ServerLevel level, final ServerPlayer player, final TileMusicBox music_box){
    switch(command){
    case PLAY:                 music_box.play(false);                break;
    case CHANGE_TEMPO:         music_box.change_tempo(info > 0);     break;
    case CYCLE_NEXT_DIRECTION: music_box.increment_next_direction(); break;
    case TOGGLE_MUTE:          music_box.toggle_mute(info);          break;
    case SWAP_TRACK:           music_box.swap_track(info, info + 1); break;
    }
  }

}
