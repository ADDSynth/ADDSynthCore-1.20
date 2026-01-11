package addsynth.core.block_network;

import java.util.function.Function;
import javax.annotation.Nullable;
import addsynth.core.block_network.search.StandardBlockSearch;
import addsynth.core.util.game.MinecraftUtility;
import addsynth.core.util.game.tileentity.ITickingTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** The BlockNetworkHandler uses your TileEntity class and BlockNetwork constructor to
 *  correctly find valid TileEntities and initialize BlockNetworks. It is highly
 *  recommended that you store a static reference to a BlockNetworkHandler inside your
 *  BlockNetwork, so any code that wish to interact with your BlockNetwork can call the
 *  static handler's functions.
 * @param <T> Your TileEntity that implements the {@link IBlockNetworkUser} interface.
 * @param <B> Your BlockNetwork.
 */
public final class BlockNetworkHandler <T extends BlockEntity & IBlockNetworkUser<B>, B extends BlockNetwork<T>> {

  final Class<T> class_type;
  final Function<BlockPos, B> constructor;

  public BlockNetworkHandler(final Class<T> class_type, final Function<BlockPos, B> constructor){
    this.class_type = class_type;
    this.constructor = constructor;
  }

  public final StandardBlockSearch standardBlockSearch(){
    return StandardBlockSearch.create(class_type);
  }

  /** This is a helper function that is used to initialize your BlockNetwork
   *  in the {@link ITickingTileEntity#serverTick(ServerLevel, BlockState)} function.
   *  Use this if your BlockNetwork does not need to be ticked. */
  public final B check(final B network, final ServerLevel world, final T tile){
    if(network == null){
      if(tile.isRemoved()){
        return null;
      }
      return create_or_join(world, tile);
    }
    return network;
  }

  /** Static helper function that automatically initializes your BlockNetwork if needed
   *  and ticks it. This must be called in the {@link ITickingTileEntity#serverTick(ServerLevel, BlockState)}
   *  method and your BlockNetwork should override the {@link BlockNetwork#tick(ServerLevel)} method. */
  public final void tick(final B network, final ServerLevel world, final T tile){
    final B good_network = check(network, world, tile);
    good_network.baseTick(world, tile);
  }

  /**
   * This is called by {@link #check(BlockNetwork, ServerLevel, BlockEntity)}.
   * @param world
   * @param tile The TileEntity attempting to creat a BlockNetwork.
   */
  final B create_or_join(final ServerLevel world, final T tile){
    if(world == null){
      throw new NullPointerException("Can't create BlockNetwork because the world isn't loaded yet.");
    }
    final B network = find_existing_network(world, tile.getBlockPos());
    if(network == null){
      // new BlockNetwork
      return createBlockNetwork(world, tile);
    }
    // first existing Network that we find becomes the current Network, and overwrites all other networks.
    network.updateBlockNetwork(world, tile.getBlockPos());
    return network;
  }

  /** Only call this if a BlockNetwork requires data from another BlockNetwork during their tick event,
   *  but calling {@link IBlockNetworkUser#getBlockNetwork()} returned {@code null}. Normal BlockNetwork
   *  initialization is achieved by calling {@link #check(BlockNetwork, ServerLevel, BlockEntity)}.
   * @param world
   * @param tile
   * @return A new BlockNetwork that has already been updated and had it's data loaded from the TileEntity.
   */
  public final B createBlockNetwork(final ServerLevel world, final T tile){
    final BlockPos pos = tile.getBlockPos();
    final B network = constructor.apply(pos); // The BlockNetwork MUST be allowed to fully construct before we update!
    
    // Network data must be loaded BEFORE update, because BlockNetworks might perform certain actions after an update.
    tile.setBlockNetwork(network);
    tile.load_block_network_data();
    DebugBlockNetwork.DATA_LOADED(network, pos);
    
    network.updateBlockNetwork(world, pos);
    return network;
  }

  private final B find_existing_network(final ServerLevel world, final BlockPos position){
    BlockPos offset;
    T check_tile;
    B network = null;
    for(final Direction direction : Direction.values()){
      offset = position.relative(direction);
      check_tile = MinecraftUtility.getTileEntity(offset, world, class_type);
      if(check_tile != null){
        network = check_tile.getBlockNetwork();
        if(network != null){
          DebugBlockNetwork.JOINED(position, network, offset);
          break;
        }
      }
    }
    return network;
  }

  /** This is a static helper function that must be called in your Block's {@link Block#onRemove}
   *  function, for any blocks that may belong to your BlockNetwork. This properly removes the
   *  TileEntity from your BlockNetwork's list of TileEntities. Here's an example:<br>
   *  <pre><code>  @Override
   *  public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving){
   *    MyBlockNetwork.handler.onRemove(super::onRemove, state, world, pos, newState, isMoving);
   *  }</code></pre> */
  public final void onRemove(BlockRemoveFunction remove_method, BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving){
    final T tile = MinecraftUtility.getTileEntity(pos, world, class_type);
    remove_method.onRemove(state, world, pos, newState, isMoving);
    if(tile != null){
      if(tile.isRemoved()){ // has been removed by super.onRemove()
        // evidently, the onRemove function gets called every time the BlockState changes.
        removeTile((ServerLevel)world, tile);
      }
    }
  }

  /** This must be called in your Block's {@link Block#onRemove} function to remove the TileEntity from your
   *  BlockNetwork's list of TileEntities. If this is the only thing you need to do, then we actually prefer
   *  you call {@link #onRemove}. This is only separated if you need to do other things.
   *  First get a reference to the TileEntity by calling {@link MinecraftUtility#getTileEntity}, then the
   *  {@code super.onRemove()} method to remove the TileEntity from the world (just marks it for removal), then
   *  call this function to remove the TileEntity from the BlockNetwork, but only if the TileEntity exists,
   *  and has actually been removed by {@code super.onRemove()}. Check by calling {@link BlockEntity#isRemoved()}.
   * @param world
   * @param destroyed_tile
   */
  public final void removeTile(final ServerLevel world, final T destroyed_tile){
    if(destroyed_tile != null){
      final B first_network = destroyed_tile.getBlockNetwork();
      if(first_network != null){
        final BlockPos tile_position = destroyed_tile.getBlockPos();
        DebugBlockNetwork.TILE_REMOVED(first_network, tile_position);
        if(first_network.removeTile(world, destroyed_tile)){
          // Connect to next network and check all adjacent networks
          boolean first = true;
          BlockPos position;
          T tile;
          B network;
          for(Direction side : Direction.values()){
            position = tile_position.relative(side);
            tile = MinecraftUtility.getTileEntity(position, world, class_type);
            if(tile != null){
              if(first){
                // Update current network with the next valid position
                first_network.updateBlockNetwork(world, position);
                // Now all adjacent tiles, even though they hold a reference to the original network,
                // the network won't contain their position AFTER UPDATE if they were disconnected.
                first = false;
              }
              else{
                // if it's an original block, then the network SHOULD NOT contain that position, after update.
                // if it's part of another network we already updated, then that position SHOULD be in it's block list.
                network = tile.getBlockNetwork();
                if(network == null ? true : !network.blocks.contains(tile)){
                  DebugBlockNetwork.SPLIT(position, network);
                  final B new_network = constructor.apply(position);
                  new_network.updateBlockNetwork(world, position);
                }
              }
            }
          }
        }
        destroyed_tile.setBlockNetwork(null);
      }
    }
  }

  /** Helper function. Call in block's {@link Block#neighborChanged} function.
   *  Used to cause the BlockNetwork to respond to an adjacent block being added or removed.
   *  @see BlockNetwork#neighbor_was_changed(ServerLevel, BlockPos, BlockPos)
   **/
  public final void neighbor_changed(final Level world, final BlockPos pos, final BlockPos position_of_neighbor){
    if(!world.isClientSide){
      final T tile = MinecraftUtility.getTileEntity(pos, world, class_type);
      if(tile != null){
        final B block_network = tile.getBlockNetwork();
        if(block_network != null){
          block_network.neighbor_was_changed((ServerLevel)world, pos, position_of_neighbor);
        }
      }
    }
  }

  /** Whenever you want to save a list of BlockEntities, and you expect some of those BlockEntities
   *  to be part of a BlockNetwork, you most likely will only want to save the FIRST BlockEntity
   *  that belongs to that BlockNetwork, to act as a representative of the entire BlockNetwork.
   */
  @Nullable
  public static final BlockEntity getTileEntity(Level level, BlockPos position){
    final BlockEntity tile = level.getBlockEntity(position);
    if(tile instanceof IBlockNetworkUser network_tile){
      final BlockNetwork network = network_tile.getBlockNetwork();
      if(network != null){
        return network.getFirstTile();
      }
    }
    return tile;
  }

}
