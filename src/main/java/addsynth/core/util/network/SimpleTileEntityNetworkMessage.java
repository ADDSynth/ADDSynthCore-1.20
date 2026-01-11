package addsynth.core.util.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

/** A much simplified version of {@link TileEntityNetworkMessage} that doesn't
 *  cast to a specialized BlockEntity. It simply finds a BlockEntity at the
 *  position and passes it to you. Use this when checking if a BlockEntity
 *  implements an interface.
 */
public abstract class SimpleTileEntityNetworkMessage extends INetworkMessage {

  protected final BlockPos position;

  public SimpleTileEntityNetworkMessage(final BlockPos position){
    this.position = position;
  }

  @Override
  protected final void handle(final ServerPlayer sender){
    @SuppressWarnings("resource")
    final ServerLevel level = sender.serverLevel();
    if(level.isLoaded(position)){
      final BlockEntity tile = level.getBlockEntity(position);
      if(tile != null){
        handle(level, tile);
      }
    }
  }

  /** We already get the TileEntity for you. */
  protected abstract void handle(ServerLevel level, BlockEntity tile);

}
