package addsynth.core.block_network.node;

import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/** A Node neatly stores a Block Position, Block Type, and a TileEntity in the same object.
 *  The TileEntity can be null if no TileEntity exists at that location. */
public class Node extends AbstractNode<BlockEntity> {

  public Node(@Nonnull final BlockEntity tile){
    super(tile);
  }

  public Node(final AbstractNode node){
    super(node.position, node.block, node.tile);
  }

  public Node(final BlockPos position, final Level world){
    super(position, world.getBlockState(position).getBlock(), world.getBlockEntity(position));
  }

}
