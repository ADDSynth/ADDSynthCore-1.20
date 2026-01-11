package addsynth.core.gameplay.blocks.music_box.network_messages;

import addsynth.core.gameplay.blocks.music_box.TileMusicBox;
import addsynth.core.util.network.TileEntityNetworkMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class ChangeInstrumentMessage extends TileEntityNetworkMessage<TileMusicBox> {

  private final byte track;
  private final byte instrument;

  public ChangeInstrumentMessage(final BlockPos position, final byte track, final byte instrument){
    super(position, TileMusicBox.class);
    this.track = track;
    this.instrument = instrument;
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeByte(track);
    buf.writeByte(instrument);
  }

  public static final ChangeInstrumentMessage decode(final FriendlyByteBuf buf){
    return new ChangeInstrumentMessage(buf.readBlockPos(), buf.readByte(), buf.readByte());
  }

  @Override
  protected final void handle(final ServerLevel level, final ServerPlayer player, final TileMusicBox music_box){
    music_box.change_track_instrument(track, instrument);
  }

}
