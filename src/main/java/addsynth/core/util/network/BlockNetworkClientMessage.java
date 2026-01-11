package addsynth.core.util.network;

import addsynth.core.block_network.BlockNetwork;
import addsynth.core.util.game.MinecraftUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

/** <p>For when you need to send data to a group of BlockEntities on the client,
 *  most likely used to sync BlockEntities in a {@link BlockNetwork}.
 *  <p>Use {@link NetworkUtil} to read/write the {@link #positions}. */
public abstract class BlockNetworkClientMessage<T extends BlockEntity> extends IClientMessage {

  private final Class<T> tile_class;
  protected final BlockPos[] positions;

  public BlockNetworkClientMessage(final BlockPos[] position, final Class<T> tile_class){
    this.tile_class = tile_class;
    this.positions = position;
  }

  @Override
  protected final void handle(){
    @SuppressWarnings("resource")
    final Minecraft minecraft = Minecraft.getInstance();
    final LocalPlayer player = minecraft.player;
    if(player != null){
      final ClientLevel level = player.clientLevel;
      T tile;
      for(final BlockPos position : positions){
        if(level.isLoaded(position)){
          tile = MinecraftUtility.getTileEntity(position, level, tile_class);
          if(tile != null){
            handle(level, player, tile);
          }
        }
      }
    }
  }

  /** We already get and check the TileEntities for you. */
  protected abstract void handle(ClientLevel level, LocalPlayer player, T tile);

}
