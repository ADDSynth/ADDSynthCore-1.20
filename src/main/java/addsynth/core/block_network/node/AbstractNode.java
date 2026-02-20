package addsynth.core.block_network.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import addsynth.core.util.java.StringUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractNode<T extends BlockEntity> {

  public final BlockPos position;
  public final Block block;
  protected final T tile;

  public AbstractNode(@Nonnull final T tile){
    this.position = tile.getBlockPos();
    this.block = tile.getBlockState().getBlock();
    this.tile = tile;
  }

  public AbstractNode(final BlockPos position, final Block block, final T tile){
    this.position = position;
    this.block = block;
    this.tile = tile;
  }

  /** <p>Returns whether this node is invalid.<p>An invalid node will either have a null Block
   *  or null Position. If this node contains a BlockEntity, then we also check if the BlockEntity
   *  has been removed and if the Block and Position matches that of the BlockEntity.
   */
  public boolean isInvalid(){
    if(block == null || position == null){
      return true;
    }
    if(tile != null){
      return tile.isRemoved() || !tile.getBlockPos().equals(position) || tile.getBlockState().getBlock() != block;
    }
    return false;
  }

  public boolean hasTileEntity(){
    return tile != null;
  }

  @Nullable
  public T getTile(){
    return tile;
  }

  @Override
  public String toString(){
    if(tile == null){
      return StringUtil.build("Node{Position: ", StringUtil.print(position), ", Block: ", StringUtil.getName(block), "}");
    }
    return StringUtil.build("Node{TileEntity: ", tile.getClass().getSimpleName(), ", Position: ", StringUtil.print(position), "}");
  }

  @Override
  public int hashCode(){
    return position.hashCode();
  }

  @Override
  public boolean equals(final Object obj){
    return obj instanceof AbstractNode ? position.equals(((AbstractNode)obj).position) : false;
  }

}
