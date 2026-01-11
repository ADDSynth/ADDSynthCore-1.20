package addsynth.core.util.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/** This gets the BlockStates in a 3x3x3 space centered around a BlockPosition. */
public final class BlockStateGroup {

  private final ImmutableMap<BlockPos, BlockState> map;

  public BlockStateGroup(LevelAccessor level, BlockPos start_position){
    final ImmutableMap.Builder<BlockPos, BlockState> builder = ImmutableMap.builder();
    BlockPos position;
    int x, y, z;
    for(y = -1; y <= 1; y++){
      for(z = -1; z <= 1; z++){
        for(x = -1; x <= 1; x++){
          position = start_position.offset(x, y, z);
          builder.put(position, level.getBlockState(position));
        }
      }
    }
    this.map = builder.build();
  }

  public final BlockState get(BlockPos position){
    return map.get(position);
  }

  public final BlockState get(BlockPos position, Direction direction){
    return map.get(position.relative(direction));
  }

  public final BlockState get(BlockPos position, Direction directionA, Direction directionB){
    return map.get(position.relative(directionA).relative(directionB));
  }

}
