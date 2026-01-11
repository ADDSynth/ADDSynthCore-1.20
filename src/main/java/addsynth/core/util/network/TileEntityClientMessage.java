package addsynth.core.util.network;

import addsynth.core.util.game.MinecraftUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TileEntityClientMessage<T extends BlockEntity> extends IClientMessage {

  private final Class<T> tile_class;
  protected final BlockPos position;

  public TileEntityClientMessage(final BlockPos position, final Class<T> tile_class){
    this.tile_class = tile_class;
    this.position = position;
  }

  @Override
  protected final void handle(){
    @SuppressWarnings("resource")
    final Minecraft minecraft = Minecraft.getInstance();
    final LocalPlayer player = minecraft.player;
    if(player != null){
      final ClientLevel level = player.clientLevel;
      if(level.isLoaded(position)){
        final T tile = MinecraftUtility.getTileEntity(position, level, tile_class);
        if(tile != null){
          handle(level, player, tile);
        }
      }
    }
  }

  /** We already get and check the TileEntity for you. */
  protected abstract void handle(ClientLevel level, LocalPlayer player, T tile);

}
