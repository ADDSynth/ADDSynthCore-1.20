package addsynth.core.block_network.node;

import java.util.Objects;
import javax.annotation.Nonnull;
import addsynth.core.util.java.StringUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

/** This is a Node that MUST have a TileEntity at that location.
 *  You can be assured that the TileEntity you get is not null.
 */
public class BlockEntityNode<T extends BlockEntity> extends AbstractNode<T> {

  public BlockEntityNode(@Nonnull final T tile){
    super(Objects.requireNonNull(tile, "BlockEntity used for BlockEntityNode must not be null!"));
  }

  BlockEntityNode(BlockPos position, Block block, T tile){
    super(position, block, Objects.requireNonNull(tile, "BlockEntity used for BlockEntityNode must not be null!"));
  }

  @Override
  public boolean isInvalid(){
    if(block == null || position == null || tile == null){
      return true;
    }
    return tile.isRemoved() || !tile.getBlockPos().equals(position) || tile.getBlockState().getBlock() != block;
  }

  @Override
  public boolean hasTileEntity(){
    return true;
  }

  @Override
  @Nonnull
  public T getTile(){
    return tile;
  }

  @Override
  public String toString(){
    return StringUtil.build("Node{TileEntity: ", tile.getClass().getSimpleName(), ", Position: ", StringUtil.print(position), "}");
  }

}
