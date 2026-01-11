package addsynth.core.util.math.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import addsynth.core.util.math.common.MathUtility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

/** @see BlockArea
 */
public final class BlockMath {

  /**
   * In retrospect, BlockPos and Vec3i have their own getDistance() method, but I still think
   * mine runs faster.
   * @param position_1
   * @param position_2
   */
  public static final double get_distance(final Vec3i position_1, final Vec3i position_2){
    final int x_value = (position_2.getX() - position_1.getX()) * (position_2.getX() - position_1.getX());
    final int y_value = (position_2.getY() - position_1.getY()) * (position_2.getY() - position_1.getY());
    final int z_value = (position_2.getZ() - position_1.getZ()) * (position_2.getZ() - position_1.getZ());
    return Math.sqrt((double)(x_value + y_value + z_value));
  }

  /** Returns the distance between a block and an entity.
   *  Adds 0.5 to the BlockPos to set the point in the center of the block.
   * @param block_position
   * @param x
   * @param y
   * @param z
   */
  public static final double get_distance(BlockPos block_position, double x, double y, double z){
    return MathUtility.get_distance(block_position.getX() + 0.5, block_position.getY() + 0.5, block_position.getZ() + 0.5, x, y, z);
  }

  /** Gets the horizontal distance between 2 Block Positions, ignoring their y levels. */
  public static final double getHorizontalDistance(final BlockPos position_1, final BlockPos position_2){
    final int x_value = (position_2.getX() - position_1.getX()) * (position_2.getX() - position_1.getX());
    final int z_value = (position_2.getZ() - position_1.getZ()) * (position_2.getZ() - position_1.getZ());
    return Math.sqrt((double)(x_value + z_value));
  }

  /** Gets horizontal distance between a block and an entity.
   *  Adds 0.5 to the BlockPos to set the point in the center of the block.
   * @param block_position
   * @param x
   * @param z
   */
  public static final double getHorizontalDistance(BlockPos block_position, double x, double z){
    return MathUtility.get_distance(block_position.getX() + 0.5, block_position.getZ() + 0.5, x, z);
  }

  /** Returns whether an Entity's position is within the radius of the BlockPos.
   *  Adds 0.5 to the BlockPos to set the point in the center of the block.
   * @param position
   * @param x
   * @param y
   * @param z
   * @param distance
   */
  public static final boolean isWithin(BlockPos position, double x, double y, double z, double distance){
    return get_distance(position, x, y, z) <= distance;
  }

  /** Returns whether an Entity's horizontal position is within range of the BlockPos.
   *  Adds 0.5 to the BlockPos to set the point in the center of the block.
   * @param position
   * @param x
   * @param z
   * @param distance
   */
  public static final boolean isWithinHorizontal(BlockPos position, double x, double z, double distance){
    return getHorizontalDistance(position, x, z) <= distance;
  }

  /** @deprecated Look at what this function does. Bypass this and use the vanilla method instead. */
  @Deprecated
  public static final boolean isWithin(final BlockPos position1, final BlockPos position2, final double distance){
    return position1.closerThan(position2, distance);
  }

  /** Returns whether the two Block Positions are horizontally in range of each other, ignoring their y levels. */
  public static final boolean isWithinHorizontal(final BlockPos position1, final BlockPos position2, final double distance){
    return getHorizontalDistance(position1, position2) <= distance;
  }

  /**
   * This method is very similar to is_inside_sphere() except this runs much faster because we
   * just need to check the distance of each component, instead of the ACTUAL distance together.
   */
  public static final boolean is_inside_cube(final BlockPos center, final int radius, final BlockPos position){
    final int distance_x = Math.abs( center.getX() - position.getX() );
    final int distance_y = Math.abs( center.getY() - position.getY() );
    final int distance_z = Math.abs( center.getZ() - position.getZ() );
    return distance_x <= radius && distance_y <= radius && distance_z <= radius;
  }

  /**
   * 
   * @param center
   * @param radius
   * @param position
   */
  public static final boolean is_inside_sphere(final BlockPos center, final int radius, final BlockPos position){
    return (int)Math.round(get_distance(center,position)) <= radius;
  }

  public static final boolean is_inside_sphere(final BlockPos center, final float radius, final BlockPos position){
    return get_distance(center,position) <= radius;
  }

  /** Returns the chunk id. Coordinates 512x512 is chunk 32x32. */
  public static final int get_chunk_index(final int coordinate){
    return coordinate >> 4;
  }

  /** Returns the chunk id. Coordinates 512x512 is chunk 32x32. */
  public static final int get_chunk_index(final double coordinate){
    return (int)Math.floor(coordinate) >> 4;
  }

  /** Returns the coordinate in the chunk, 0-15. */
  public static final int get_coordinate_in_chunk(final int coordinate){
    return coordinate & 15;
  }
  
  /** Returns the coordinate in the chunk, 0-15. */
  public static final int get_coordinate_in_chunk(final float coordinate){
    return (int)Math.floor(coordinate) & 15;
  }
  
  /** Returns the coordinate in the chunk, 0-15. */
  public static final int get_coordinate_in_chunk(final double coordinate){
    return (int)Math.floor(coordinate) & 15;
  }

  public static final int get_first_block_in_chunk(final int coordinate){
    return coordinate & 0xFFFFFFF0;
  }

  public static final class BlockDistanceComparator implements Comparator<BlockPos> {
    private final BlockPos origin;
    public BlockDistanceComparator(final BlockPos origin){
      this.origin = origin;
    }
    @Override
    public int compare(BlockPos pos1, BlockPos pos2){
      final double length1 = get_distance(origin, pos1);
      final double length2 = get_distance(origin, pos2);
      if(length1 < length2){ return -1; }
      if(length1 > length2){ return 1; }
      return 0;
    }
  }
  // OPTIMIZE: by using a class or MapEntry that pairs the BlockPos with the distance, then sort the pairs by distance.

  /*
  public static final class BlockHorizontalDistanceComparator implements Comparator<BlockPos> {
    private final BlockPos origin;
    public BlockHorizontalDistanceComparator(final BlockPos origin){
      this.origin = origin;
    }
    @Override
    public int compare(BlockPos pos1, BlockPos pos2){
      final double length1 = getHorizontalDistance(origin, pos1);
      final double length2 = getHorizontalDistance(origin, pos2);
      if(length1 < length2){ return -1; }
      if(length1 > length2){ return 1; }
      return 0;
    }
  }
  */

  /** This returns a list of Block Positions arranged in a cyllinder, given the origin and radius.
   *  The returned list is sorted so positions near the center are towards the beginning.
   * @param origin
   * @param radius
   */
  public static final Collection<BlockPos> getBlockPositionsAroundPillar(BlockPos origin, int radius){
    return getBlockPositionsAroundPillar(origin, radius, radius);
  }

  /** This returns a list of Block Positions arranged in a cyllinder, given the origin and dimensions.
   *  The returned list is sorted so positions near the center are towards the beginning.
   * @param origin
   * @param horizontal_radius
   * @param vertical_radius
   */
  public static final Collection<BlockPos> getBlockPositionsAroundPillar(BlockPos origin, int horizontal_radius, int vertical_radius){
    final ArrayList<BlockPos> list = new ArrayList<>();
    final int final_x = origin.getX() + horizontal_radius;
    final int final_y = origin.getY() + vertical_radius;
    final int final_z = origin.getZ() + horizontal_radius;
    int x;
    int y;
    int z;
    for(y = origin.getY() - vertical_radius; y < final_y; y++){
      for(x = origin.getX() - horizontal_radius; x < final_x; x++){
        for(z = origin.getZ() - horizontal_radius; z < final_z; z++){
          final BlockPos pos = new BlockPos(x, y, z);
          if(isWithinHorizontal(origin, pos, horizontal_radius)){
            list.add(pos);
          }
        }
      }
    }
    list.sort(new BlockDistanceComparator(origin));
    return list;
  }

  /** This returns a list of Block Positions in a spherical shape, given the origin and radius.
   *  The returned list is sorted so positions near the center are towards the beginning.
   * @param origin
   * @param radius
   */
  public static final Collection<BlockPos> getBlockPositionsAroundPoint(BlockPos origin, int radius){
    final ArrayList<BlockPos> list = new ArrayList<>();
    final int final_x = origin.getX() + radius;
    final int final_y = origin.getY() + radius;
    final int final_z = origin.getZ() + radius;
    int x;
    int y;
    int z;
    for(y = origin.getY() - radius; y < final_y; y++){
      for(x = origin.getX() - radius; x < final_x; x++){
        for(z = origin.getZ() - radius; z < final_z; z++){
          final BlockPos pos = new BlockPos(x, y, z);
          if(origin.closerThan(pos, radius)){
            list.add(pos);
          }
        }
      }
    }
    list.sort(new BlockDistanceComparator(origin));
    return list;
  }

  /** This returns a list of Block Positions arranged in a custom shaped sphere, given the arguments.
   *  The returned list is sorted so positions near the center are towards the beginning.
   * @param origin
   * @param horizontal_radius
   * @param vertical_radius
   * @deprecated You'll need to fix the problem with this function before you can use it.
   *             Not correctly calculating the distance of a point to its origin given a non-uniform radius.
   */
  @Deprecated
  public static final Collection<BlockPos> getBlockPositionsAroundPoint(BlockPos origin, int horizontal_radius, int vertical_radius){
    final ArrayList<BlockPos> list = new ArrayList<>();
    final int final_x = origin.getX() + horizontal_radius;
    final int final_y = origin.getY() + vertical_radius;
    final int final_z = origin.getZ() + horizontal_radius;
    int x;
    int y;
    int z;
    for(y = origin.getY() - vertical_radius; y < final_y; y++){
      for(x = origin.getX() - horizontal_radius; x < final_x; x++){
        for(z = origin.getZ() - horizontal_radius; z < final_z; z++){
          final BlockPos pos = new BlockPos(x, y, z);
          if(origin.closerThan(pos, (horizontal_radius + vertical_radius) / 2)){
            list.add(pos);
          }
        }
      }
    }
    list.sort(new BlockDistanceComparator(origin));
    return list;
  }
  
}
