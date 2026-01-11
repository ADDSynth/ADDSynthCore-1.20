package addsynth.core.gameplay.blocks.jukebox;

import addsynth.core.util.network.TileEntityNetworkMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class JukeboxMessage extends TileEntityNetworkMessage<TileJukeboxPlayer> {

  public enum Command {
    PLAYSTOP, SKIP_LEFT, SKIP_RIGHT, TOGGLE_SHUFFLE, TOGGLE_REPEAT, TOGGLE_OUTPUT_REDSTONE, ADJUST_BUFFER_TIME,
    RESET, CHANGE_VOLUME
  }

  private final Command command;
  private final int time;

  public JukeboxMessage(final BlockPos position, final Command command){
    super(position, TileJukeboxPlayer.class);
    this.command = command;
    time = 0;
  }

  public JukeboxMessage(final BlockPos position, final Command command, final int time){
    super(position, TileJukeboxPlayer.class);
    this.command = command;
    this.time = time;
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeEnum(command);
    buf.writeInt(time);
  }

  public static final JukeboxMessage decode(final FriendlyByteBuf buf){
    return new JukeboxMessage(buf.readBlockPos(), buf.readEnum(Command.class), buf.readInt());
  }

  @Override
  protected final void handle(final ServerLevel level, final ServerPlayer player, final TileJukeboxPlayer jukebox){
    switch(command){
    case PLAYSTOP:               jukebox.playStop(); break;
    case SKIP_LEFT:              jukebox.skipLeft(); break;
    case SKIP_RIGHT:             jukebox.skipRight(); break;
    case TOGGLE_SHUFFLE:         jukebox.toggleShuffle(); break;
    case TOGGLE_REPEAT:          jukebox.toggleRepeat(); break;
    case TOGGLE_OUTPUT_REDSTONE: jukebox.toggleRedstoneOutput(); break;
    case ADJUST_BUFFER_TIME:     jukebox.setDelayTime(time); break;
    case RESET:                  jukebox.reset(); break;
    case CHANGE_VOLUME: break;
    }
  }

}
