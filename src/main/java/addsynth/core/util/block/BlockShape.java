package addsynth.core.util.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockShape {

  public static final double MINIMUM_THICKNESS = 0.03125; // 1/32 of a block

  // !!! This MUST follow the same order as the Direction enum!
  public static final int DOWN  =  1;
  public static final int UP    =  2;
  public static final int NORTH =  4;
  public static final int SOUTH =  8;
  public static final int WEST  = 16;
  public static final int EAST  = 32;

  public static final VoxelShape[] createWireShapes(double thickness){
    if(thickness < MINIMUM_THICKNESS){
      thickness = MINIMUM_THICKNESS;
    }
    final double half_width = thickness / 2;
    return createWireShapes(0.5 - half_width, 0.5 + half_width);
  }

  public static final VoxelShape[] createWireShapes(final double min_size, final double max_size){
    final int max = 64;
    final VoxelShape[] shapes = new VoxelShape[max];
    final VoxelShape center = Shapes.box(min_size, min_size, min_size, max_size, max_size, max_size);
    final VoxelShape west   = Shapes.box(0, min_size, min_size, max_size, max_size, max_size);
    final VoxelShape down   = Shapes.box(min_size, 0, min_size, max_size, max_size, max_size);
    final VoxelShape north  = Shapes.box(min_size, min_size, 0, max_size, max_size, max_size);
    final VoxelShape east   = Shapes.box(min_size, min_size, min_size, 1, max_size, max_size);
    final VoxelShape up     = Shapes.box(min_size, min_size, min_size, max_size, 1, max_size);
    final VoxelShape south  = Shapes.box(min_size, min_size, min_size, max_size, max_size, 1);
    int i;
    for(i = 0; i < max; i++){
      
      shapes[i] = center;
      if((i & DOWN)  == DOWN ){ shapes[i] = Shapes.or(shapes[i], down);  }
      if((i & UP)    == UP   ){ shapes[i] = Shapes.or(shapes[i], up);    }
      if((i & NORTH) == NORTH){ shapes[i] = Shapes.or(shapes[i], north); }
      if((i & SOUTH) == SOUTH){ shapes[i] = Shapes.or(shapes[i], south); }
      if((i & WEST)  == WEST ){ shapes[i] = Shapes.or(shapes[i], west);  }
      if((i & EAST)  == EAST ){ shapes[i] = Shapes.or(shapes[i], east);  }
    }
    return shapes;
  }

  public static final VoxelShape[] createPanelShapes(final double thickness){
    final double min = Math.max(thickness, MINIMUM_THICKNESS);
    final double max = 1 - min;
    return new VoxelShape[]{
      Shapes.box(0, 0, 0, 1, min, 1),
      Shapes.box(0, max, 0, 1, 1, 1),
      Shapes.box(0, 0, 0, 1, 1, min),
      Shapes.box(0, 0, max, 1, 1, 1),
      Shapes.box(0, 0, 0, min, 1, 1),
      Shapes.box(max, 0, 0, 1, 1, 1)
    };
  }

  public static final VoxelShape[] createSixSidedPanelShapes(final double thickness){
    final int max_number = 64;
    final double min = Math.max(thickness, MINIMUM_THICKNESS);
    final double max = 1 - min;
    final VoxelShape[] shapes = new VoxelShape[max_number];
    final VoxelShape west  = Shapes.box(0, 0, 0, min, 1, 1);
    final VoxelShape down  = Shapes.box(0, 0, 0, 1, min, 1);
    final VoxelShape north = Shapes.box(0, 0, 0, 1, 1, min);
    final VoxelShape east  = Shapes.box(max, 0, 0, 1, 1, 1);
    final VoxelShape up    = Shapes.box(0, max, 0, 1, 1, 1);
    final VoxelShape south = Shapes.box(0, 0, max, 1, 1, 1);
    int i;
    shapes[0] = Shapes.empty();
    for(i = 1; i < max_number; i++){
      shapes[i] = Shapes.empty();
      if((i & DOWN)  == DOWN ){ shapes[i] = Shapes.or(shapes[i], down);  }
      if((i & UP)    == UP   ){ shapes[i] = Shapes.or(shapes[i], up);    }
      if((i & NORTH) == NORTH){ shapes[i] = Shapes.or(shapes[i], north); }
      if((i & SOUTH) == SOUTH){ shapes[i] = Shapes.or(shapes[i], south); }
      if((i & WEST)  == WEST ){ shapes[i] = Shapes.or(shapes[i], west);  }
      if((i & EAST)  == EAST ){ shapes[i] = Shapes.or(shapes[i], east);  }
    }
    return shapes;
  }

  public static final int getIndex(final BlockState state){
    final int down  = state.getValue(BlockStateProperties.DOWN)  ? DOWN  : 0;
    final int up    = state.getValue(BlockStateProperties.UP)    ? UP    : 0;
    final int north = state.getValue(BlockStateProperties.NORTH) ? NORTH : 0;
    final int south = state.getValue(BlockStateProperties.SOUTH) ? SOUTH : 0;
    final int west  = state.getValue(BlockStateProperties.WEST)  ? WEST  : 0;
    final int east  = state.getValue(BlockStateProperties.EAST)  ? EAST  : 0;
    return down | up | north | south | east | west;
  }

  public static final VoxelShape combine(final VoxelShape ... shapes){
    if(shapes.length == 0){ return Shapes.empty(); }
    if(shapes.length == 1){ return shapes[0]; }
    VoxelShape final_shape = shapes[0];
    int i;
    for(i = 1; i < shapes.length; i++){
      final_shape = Shapes.or(final_shape, shapes[i]);
    }
    return final_shape;
  }

}
