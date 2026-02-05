package addsynth.core.block_network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import addsynth.core.block_network.node.BlockEntityNode;
import addsynth.core.block_network.search.IBlockSearchAlgorithm;
import addsynth.core.util.java.list.IndexedSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

/** This is the collection of BlockEntities that the BlockNetwork
 *  consists of. It is only meant to be used with blockNetworks. */
public final class BlockList<T extends BlockEntity & IBlockNetworkUser> {

  private final IndexedSet<T> list;
  /** <p>Although the only practical thing we need to do is record the size of the list
   *  before update, so we can print a debug message to log if it changed. The quickest
   *  and easiest way to keep the tile list synchronized is to clear it and then add
   *  each tile one by one, however this will change the {@code first_tile} if the update
   *  occurred somewhere else. The only known consequence of this is that there's a small
   *  chance the Block Network won't update for that tick (becuase the new {@code first_tile}
   *  was already ticked before update.) It is unclear if there are any more consequences of
   *  NOT keeping the tile list in order each time it is updated.
   *  <p>To avoid potential future issues, we have decided to preserve the block list order
   *  between updates. But this means we must use two lists, and iterate through the list
   *  twice instead of once, however we use HashSets so this should keep time complexity
   *  to O(n). It also makes intuitive sense. If a Block Network updates, you just want
   *  to remove tiles that don't exist anymore, and add new tiles to the back of the list.
   *  You also DO NOT want {@code first_tile} to be reassigned if it still exists!
   */
  private final HashSet<T> temp;

  public BlockList(){
    list = new IndexedSet<>(100);
    temp = new HashSet<>(100);
  }

  public BlockList(final int size){
    list = new IndexedSet<>(size);
    temp = new HashSet<>(size);
  }

  @Nullable
  public final T getFirstTile(){
    if(list.size() == 0){
      return null;
    }
    return list.getFirst();
  }

  public final boolean isFirstTile(final BlockEntity tile){
    if(list.size() > 0){
      return list.getFirst() == tile;
    }
    return false;
  }

  /** This is the main function that finds all blocks belonging to this BlockNetwork.
   *  This is called by {@link BlockNetwork#updateBlockNetwork(ServerLevel, BlockPos)}. */
  @SuppressWarnings("unchecked")
  public final void update(IBlockSearchAlgorithm search_algorithm, final ServerLevel world, final BlockPos from, final BlockNetwork network, final CustomSearch custom_search){
    // get tiles
    final HashSet<BlockEntityNode> found = search_algorithm.find_blocks(from, world, custom_search);
  
    // extract tiles
    final int oldSize = list.size();
    temp.clear(); // we still need to extract all tiles in the update so we can remove tiles if they don't exist.
    T tile;
    for(final BlockEntityNode node : found){
      tile = (T)node.getTile();
      // set BlockNetwork
      tile.setBlockNetwork(network);
      list.add(tile); // go ahead and add new tiles in the same loop. This will fail if they already exist.
      temp.add(tile);
    }

    // sync list
    list.removeIf((T tile2) -> !temp.contains(tile2));
    DebugBlockNetwork.BLOCKS_CHANGED(network, oldSize, list.size());
  }

  public final void remove(final T tile){
    list.remove(tile);
  }

  public final ArrayList<BlockPos> getBlockPositions(){
    final ArrayList<BlockPos> positions = new ArrayList<>(list.size());
    for(final BlockEntity tile : list){
      if(tile.isRemoved() == false){
        positions.add(tile.getBlockPos());
      }
    }
    return positions;
  }

  public final ArrayList<T> getTileEntities(){
    return list.toList();
  }

  public final boolean contains(final T tile){
    return list.contains(tile);
  }

  public final boolean contains(final BlockPos position){
    for(final BlockEntity tile : list){
      if(tile.getBlockPos().equals(position)){
        return true;
      }
    }
    return false;
  }

  public final void remove_invalid(){
    list.removeIf((T tile) -> tile.isRemoved());
  }

  public final void forAllTileEntities(final Consumer<T> action){
    list.forEach(action);
  }

  public final int size(){
    return list.size();
  }

  public final BlockEntity[] toArray(){
    return list.toArray(new BlockEntity[list.size()]);
  }

}
