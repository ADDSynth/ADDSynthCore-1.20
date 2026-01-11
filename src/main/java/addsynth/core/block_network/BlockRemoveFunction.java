package addsynth.core.block_network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockRemoveFunction {

  public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving);

}
