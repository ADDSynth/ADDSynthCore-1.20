package addsynth.core.block_network.search;

import java.util.HashSet;
import java.util.function.BiPredicate;
import javax.annotation.Nullable;
import addsynth.core.ADDSynthCore;
import addsynth.core.block_network.CustomSearch;
import addsynth.core.block_network.IBlockNetworkUser;
import addsynth.core.block_network.node.BlockEntityNode;
import addsynth.core.block_network.node.Node;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

/** The advanced search algorithm goes a step further in determining which BlockEntity
 *  gets added to the BlockNetwork, by also providing the Node of the previous position,
 *  allowing you to run extra checks based on the previous BlockEntity. For instance,
 *  the next position may be a valid BlockEntity for the BlockNetwork, but you may not
 *  want to add it because it really should not be 'connecting' to the BlockEntity we're
 *  coming from.
 */
public final class AdvancedSearchAlgorithm implements IBlockSearchAlgorithm {

  public final BiPredicate<BlockEntityNode, BlockEntityNode> isValid;
  private final HashSet<BlockPos> searched = new HashSet<BlockPos>(1000);

  public AdvancedSearchAlgorithm(BiPredicate<BlockEntityNode, BlockEntityNode> is_valid){
    this.isValid = is_valid;
  }

  /** Creates a AdvancedSearchAlgorithm object for use in BlockNetworks. */
  public static final <T extends BlockEntity & IBlockNetworkUser> AdvancedSearchAlgorithm create(final Class<T> tile_class){
    return new AdvancedSearchAlgorithm(
      (BlockEntityNode from, BlockEntityNode to) -> {
        final BlockEntity tile = to.getTile();
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
      final BlockEntityNode node = check(null, from, list, world, consumer);
      if(node != null){
        search(node, list, world, consumer);
      }
    }
    catch(StackOverflowError e){
      ADDSynthCore.log.fatal("Search algorithm in "+AdvancedSearchAlgorithm.class.getName()+" looped forever! We're sorry about that! "+
        "(someone's code is doing something they're not supposed to.)", e);
    }
    catch(Exception e){
      ADDSynthCore.log.error("Error in "+AdvancedSearchAlgorithm.class.getSimpleName()+".search() algorithm.", e);
    }
    return list;
  }

  private final void search(BlockEntityNode from, HashSet<BlockEntityNode> list, ServerLevel world, CustomSearch consumer){
    final BlockPos previous_position = from.position;
    BlockPos position;
    BlockEntityNode node;
    for(final Direction side : Direction.values()){
      position = previous_position.relative(side);
      if(searched.contains(position) == false){
        searched.add(position);
        node = check(from, position, list, world, consumer);
        if(node != null){
          search(node, list, world, consumer);
        }
      }
    }
  }

  @Nullable
  private final BlockEntityNode check(@Nullable BlockEntityNode from, BlockPos position, HashSet<BlockEntityNode> list, ServerLevel world, CustomSearch consumer){
    if(consumer != null){
      consumer.accept(from != null ? new Node(from) : null, new Node(position, world), world);
    }
    final BlockEntity tile = world.getBlockEntity(position);
    if(tile != null){
      final BlockEntityNode node = new BlockEntityNode<>(tile);
      if(isValid.test(from, node)){
        list.add(node);
        return node;
      }
    }
    return null;
  }

}
