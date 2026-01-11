package addsynth.core.block_network.search;

import java.util.HashSet;
import java.util.function.Predicate;
import addsynth.core.ADDSynthCore;
import addsynth.core.block_network.CustomSearch;
import addsynth.core.block_network.IBlockNetworkUser;
import addsynth.core.block_network.node.BlockEntityNode;
import addsynth.core.block_network.node.Node;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

/** The standard search algorithm only tests a single BlockEntity against a predicate
 *  you specify in order to determine if the BlockEntity should be a part of the
 *  BlockNetwork. You can optionally specify a Consumer to run additional code on
 *  all positions that are searched by the search algorithm.
 */
public final class StandardBlockSearch implements IBlockSearchAlgorithm {

  public final Predicate<BlockEntityNode> isValid;
  private final HashSet<BlockPos> searched = new HashSet<BlockPos>(1000);

  public StandardBlockSearch(Predicate<BlockEntityNode> is_valid){
    this.isValid = is_valid;
  }

  /** Creates a StandardBlockSearch object for use in BlockNetworks. */
  public static final <T extends BlockEntity & IBlockNetworkUser> StandardBlockSearch create(final Class<T> tile_class){
    return new StandardBlockSearch(
      (BlockEntityNode node) -> {
        final BlockEntity tile = node.getTile();
        return tile.isRemoved() ? false : tile_class.isInstance(tile);
      }
    );
  }

  /** Standard search algorithm. Checks current block and adjacent blocks against the predicate you specify.
   *  If the predicate returns true, the {@link BlockEntityNode} is added to a list and then the list is returned.
   * @param from Starting Position. Predicate must return true otherwise an empty list is returned.
   * @param world
   */
  public final HashSet<BlockEntityNode> find_blocks(final BlockPos from, final ServerLevel world){
    return find_blocks(from, world, null);
  }

  /** Standard search algorithm. Checks current block and adjacent blocks against the predicate you specify.
   *  If the predicate returns true, the {@link BlockEntityNode} is added to a list and then the list is returned. This
   *  version has an additional Consumer argument which allows you to run additional code on all blocks searched.
   * @param from Starting Position. Predicate must return true otherwise an empty list is returned.
   * @param world
   * @param consumer Supply a function that takes a Node as an argument. Allows you to run additional code on all Nodes searched.
   */
  @Override
  public final HashSet<BlockEntityNode> find_blocks(final BlockPos from, final ServerLevel world, final CustomSearch consumer){
    final HashSet<BlockEntityNode> list = new HashSet<>(100);
    try{
      searched.clear();
      searched.add(from);
      if(check(from, list, world, consumer)){
        search(from, list, world, consumer);
      }
    }
    catch(StackOverflowError e){
      ADDSynthCore.log.fatal("Search algorithm in "+StandardBlockSearch.class.getName()+" looped forever! We're sorry about that! "+
        "(someone's code is doing something they're not supposed to.)", e);
    }
    catch(Exception e){
      ADDSynthCore.log.error("Error in "+StandardBlockSearch.class.getSimpleName()+".search() algorithm.", e);
    }
    return list;
  }

  private final void search(BlockPos from, HashSet<BlockEntityNode> list, ServerLevel world, CustomSearch consumer){
    BlockPos position;
    for(final Direction side : Direction.values()){
      position = from.relative(side);
      if(searched.contains(position) == false){
        searched.add(position);
        if(check(position, list, world, consumer)){
          search(position, list, world, consumer);
        }
      }
    }
  }

  private final boolean check(BlockPos position, HashSet<BlockEntityNode> list, ServerLevel world, CustomSearch consumer){
    if(consumer != null){
      consumer.accept(null, new Node(position, world), world);
    }
    final BlockEntity tile = world.getBlockEntity(position);
    if(tile != null){
      final BlockEntityNode node = new BlockEntityNode<>(tile);
      if(isValid.test(node)){
        list.add(node);
        return true;
      }
    }
    return false;
  }

}
