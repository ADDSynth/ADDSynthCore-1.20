package addsynth.core.util.network;

import addsynth.core.util.game.MinecraftUtility;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TileEntityNetworkMessage<T extends BlockEntity> extends INetworkMessage {

  private final Class<T> tile_class;
  protected final BlockPos position; // I could make this private, and have a method encode it, but I thought that was going too far

  public TileEntityNetworkMessage(final BlockPos position, final Class<T> tile_class){
    this.tile_class = tile_class;
    this.position = position;
  }

  @Override
  protected final void handle(final ServerPlayer sender){
    @SuppressWarnings("resource")
    final ServerLevel level = sender.serverLevel();
    if(level.isLoaded(position)){
      final T tile = MinecraftUtility.getTileEntity(position, level, tile_class);
      if(tile != null){
        handle(level, sender, tile);
      }
    }
  }

  /** We already get and check the TileEntity for you. */
  protected abstract void handle(ServerLevel level, ServerPlayer player, T tile);

}
