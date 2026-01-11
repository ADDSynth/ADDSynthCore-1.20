package addsynth.core.game.blocks;

import addsynth.core.util.game.tileentity.ITickingTileEntity;
import addsynth.core.util.game.tileentity.TileEntityUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileEntityBlock extends BaseEntityBlock {

  protected TileEntityBlock(Properties properties){
    super(properties);
  }

  @Override
  @SuppressWarnings("deprecation")
  public RenderShape getRenderShape(BlockState blockstate){
    return RenderShape.MODEL;
  }

  /** Most ticking BlockEntities only need to tick on the server side.
   *  Use this if your BlockEntity ONLY ticks on the server side.
   * @param <E>
   * @param <A>
   * @param world
   * @param type_a BlockEntityType supplied by the {@link #getTicker} function.
   * @param type_b Your BlockEntityType, with a TileEntity that implements {@link ITickingTileEntity}.
   */
  public static final <E extends BlockEntity, A extends BlockEntity & ITickingTileEntity> BlockEntityTicker<E>
  standardTicker(Level world, BlockEntityType<E> type_a, BlockEntityType<A> type_b){
    if(!world.isClientSide){
      return createTickerHelper(type_a, type_b, TileEntityUtil::tick);
    }
    return null;
  }

  protected static final boolean isServer(final Level world){
    return !world.isClientSide;
  }

  protected static final boolean isClient(final Level world){
    return world.isClientSide;
  }
  
}
