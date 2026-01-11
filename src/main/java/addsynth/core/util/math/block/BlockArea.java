package addsynth.core.util.math.block;

import addsynth.core.util.constants.DirectionConstant;
import addsynth.core.util.java.ArrayUtil;
import addsynth.core.util.java.StringUtil;
import addsynth.core.util.math.common.CommonMath;
import addsynth.core.util.world.WorldConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/** <p>The BlockArea defines a volume of blocks from a Minimum position (inclusive)
 *  and a Maximum position (inclusive). You can change the values directly, but
 *  we also hold several functions that work with BlockAreas, such as Moving,
 *  Expanding, Shrinking, and Aligning. A BlockArea can be used in an enhanced
 *  for loop to perform an action on all the BlockPositions defined in this area.
 *  
 *  <p>There are also several functions for getting information about this BlockArea,
 *  such as the Center Position, Relative Offsets, Whether a Block is Inside this
 *  Area, Intersects with another BlockArea, Distance, or Whether two BlockAreas are
 *  equal, and several alignment functions.
 *  
 *  <p>There are also some static functions that perform actions on a list of
 *  Block Positions. You can get a BlockArea from a list of BlockPositions. You can
 *  check if the BlockPositions in the list come together to form a complete
 *  rectangular shape. You can find the BlockPosition closest to an Entity.
 *  You can also save and load a BlockArea to/from a {@link CompoundTag}.
 *  
 *  <p>The smallest possible volume is when the Minimum and Maximum positions are
 *  the same, and thus the defined space is a single block space. It is possible to
 *  move the Minimum and Maximum positions so they define a negative area of space.
 *  When this happens the result of these functions may be upredictable. You can
 *  check if this BlockArea is {@link #valid} or {@link #isInvalid invalid}, and
 *  can {@link #correct} it if it is invalid.
 *  
 *  <p>Once again, after hindsight, this seems similar to vanilla Minecraft's
 *  {@link AABB} class, but I, once again, prefer to use my own.
 *  @author ADDSynth
 */
public final class BlockArea implements Iterable<BlockPos>, Cloneable {

  public int min_x;
  public int min_y;
  public int min_z;
  public int max_x;
  public int max_y;
  public int max_z;

  // Constructors

  public BlockArea(){
    min_x = 0;
    min_y = 0;
    min_z = 0;
    max_x = 0;
    max_y = 0;
    max_z = 0;
  }

  public BlockArea(final Vec3i first_position){
    this(first_position.getX(), first_position.getY(), first_position.getZ());
  }
  
  public BlockArea(final int x, final int y, final int z){
    min_x = x;
    min_y = y;
    min_z = z;
    max_x = x;
    max_y = y;
    max_z = z;
  }

  public BlockArea(final Vec3i min, final Vec3i max){
    min_x = min.getX();
    min_y = min.getY();
    min_z = min.getZ();
    max_x = max.getX();
    max_y = max.getY();
    max_z = max.getZ();
  }

  public BlockArea(int min_x, int min_y, int min_z, int max_x, int max_y, int max_z){
    this.min_x = min_x;
    this.min_y = min_y;
    this.min_z = min_z;
    this.max_x = max_x;
    this.max_y = max_y;
    this.max_z = max_z;
  }

  // Reset

  public final void reset(){
    min_x = 0;
    min_y = 0;
    min_z = 0;
    max_x = 0;
    max_y = 0;
    max_z = 0;
  }

  // Standard Getters

  /** Returns the Minimum position. */
  public final Vec3i getMinimum(){return new Vec3i(min_x, min_y, min_z); }
  
  /** Returns the Maximum position. */
  public final Vec3i getMaximum(){return new Vec3i(max_x, max_y, max_z); }
  
  /** Returns the Width, or size of the X component. */
  public final int getWidth() {return max_x + 1 - min_x; }
  
  /** Returns the Height, or size of the Y component. */
  public final int getHeight(){return max_y + 1 - min_y; }
  
  /** Returns the Length, or size of the Z component. */
  public final int getLength(){return max_z + 1 - min_z; }
  
  /** <p>Returns the center position between the minimum and maximum.
   *  <p>If any dimension has an even length, this will not return
   *  the exact center position, but the first whole position less
   *  than the true center. For example, given 4 (inclusive) and
   *  7 (inclusive) the center is 5.5, but this function will return 5.
   *  @see #hasTrueCenter()
   */
  public final Vec3i getCenter(){
    return new Vec3i((min_x + max_x)/2, (min_y + max_y)/2, (min_z + max_z)/2);
  }
  
  /** Gets the center position of the X dimension. */
  public final int getCenterX(){return (min_x + max_x)/2; }
  
  /** Gets the center position of the Y dimension. */
  public final int getCenterY(){return (min_y + max_y)/2; }
  
  /** Gets the center position of the Z dimension. */
  public final int getCenterZ(){return (min_z + max_z)/2; }
  
  private final long getAreaInternal(){
    return (long)getWidth() * (long)getHeight() * (long)getLength();
  }
  
  /** <p>Returns the total number of blocks represented by this BlockArea.
   *  <p>This is often used as the index limit in a loop. If the total
   *  number of blocks in this area is greater than the maximum value an
   *  Integer can hold, then {@link Integer#MAX_VALUE} is returned.
   *  <p>If the Minimum is the same as the Maximum, then this area defines a
   *  single block position, and the area will be 1.<br>
   *  A BlockArea with invalid ranges could have a negative area.
   *  @see #hasOverflowIndex()
   *  @see #isSinglePoint()
   *  @see #isInvalid()
   */
  public final int getArea(){
    return (int)Math.min(getAreaInternal(), Integer.MAX_VALUE);
  }
  
  // Special Get functions

  /** This gets the BlockPos represented by the index value.<br>
   *  If you supplied an index that is out-of-range, then a
   *  null BlockPos will be returned. */
  // @Nullable
  public final BlockPos getPositionFromIndex(final int index){
    if(valid()){
      if(ArrayUtil.isInsideBounds(index, getArea())){
        final int width  = getWidth();
        final int height = getHeight();
        final int x = index % width;
        final int y = (index / width) % height;
        final int z = index / (width * height);
        return new BlockPos(x, y, z);
      }
    }
    return null;
  }

  /** <p>Returns an index value for the specified position inside this BlockArea.
   *  Returns {@code -1} if the position is not part of this BlockArea.
   *  <p>Counts along the X axis first, then the Y axis, then Z axis.
   *  For very large BlockAreas, the returned index may have overflowed and
   *  wrapped around to a negative value. You can check if this BlockArea
   *  would return an overflowed index by calling {@link #hasOverflowIndex()}.
   *  @see #contains(Vec3i)
   */
  public final int getIndex(final Vec3i pos){
    if(contains(pos)){
      final int x = pos.getX() - min_x;
      final int y = pos.getY() - min_y;
      final int z = pos.getZ() - min_z;
      final int width  = getWidth();
      final int height = getHeight();
      return (z * width * height) + (y * width) + x;
    }
    return -1;
  }

  /** Gets the distance from this BlockArea's center to the position. */
  public final double getDistance(final Vec3i position){
    return BlockMath.get_distance(getCenter(), position);
  }
  
  /** Gets the distance between these two BlockArea's centers. */
  public final double getDistance(final BlockArea area){
    return BlockMath.get_distance(getCenter(), area.getCenter());
  }

  /** Creates a new BlockArea of the same size and
   *  placed directly West of this BlockArea. */
  public final BlockArea getNewVolumeWest(){
    return getNewVolumeWest(getWidth());
  }
  
  /** Creates a new BlockArea of the same size and
   *  placed directly East of this BlockArea. */
  public final BlockArea getNewVolumeEast(){
    return getNewVolumeEast(getWidth());
  }
  
  /** Creates a new BlockArea of the same size and
   *  placed directly Below this BlockArea. */
  public final BlockArea getNewVolumeDown(){
    return getNewVolumeDown(getHeight());
  }
  
  /** Creates a new BlockArea of the same size and
   *  placed directly Above this BlockArea. */
  public final BlockArea getNewVolumeUp(){
    return getNewVolumeUp(getHeight());
  }
  
  /** Creates a new BlockArea of the same size and
   *  placed directly North of this BlockArea. */
  public final BlockArea getNewVolumeNorth(){
    return getNewVolumeNorth(getLength());
  }
  
  /** Creates a new BlockArea of the same size and
   *  placed directly South this BlockArea. */
  public final BlockArea getNewVolumeSouth(){
    return getNewVolumeSouth(getLength());
  }

  public final BlockArea getNewAdjacentVolume(final int direction, final int size){
    return switch(direction){
      case DirectionConstant.WEST  -> getNewVolumeWest(size);
      case DirectionConstant.EAST  -> getNewVolumeEast(size);
      case DirectionConstant.DOWN  -> getNewVolumeDown(size);
      case DirectionConstant.UP    -> getNewVolumeUp(size);
      case DirectionConstant.NORTH -> getNewVolumeNorth(size);
      case DirectionConstant.SOUTH -> getNewVolumeSouth(size);
      default -> null;
    };
  }

  /** Returns a new BlockArea place directly West of this BlockArea that
   *  has the same Height and Length, but extended in the -X direction
   *  with the specified Width. */
  public final BlockArea getNewVolumeWest(final int size){
    return new BlockArea(min_x - size, min_y, min_z, min_x - 1, max_y, max_z);
  }
  
  /** Returns a new BlockArea placed directly East of this BlockArea that
   *  has the same Height and Length, but extended in the +X direction
   *  with the specified Width. */
  public final BlockArea getNewVolumeEast(final int size){
    return new BlockArea(max_x + 1, min_y, min_z, max_x + size, max_y, max_z);
  }
  
  /** Returns a new BlockArea placed directly Down from this BlockArea that
   *  has the same Width and Length, but extended in the -Y direction
   *  with the specified Height. */
  public final BlockArea getNewVolumeDown(final int size){
    return new BlockArea(min_x, min_y - size, min_z, max_x, min_y - 1, max_z);
  }
  
  /** Returns a new BlockArea placed directly Up from this BlockArea that
   *  has the same Width and Length, but extended in the +Y direction
   *  with the specified Height. */
  public final BlockArea getNewVolumeUp(final int size){
    return new BlockArea(min_x, max_y + 1, min_z, max_x, max_y + size, max_z);
  }
  
  /** Returns a new BlockArea placed directly North of this BlockArea that
   *  has the same Width and Height, but extended in the -Z direction
   *  with the specified Length. */
  public final BlockArea getNewVolumeNorth(final int size){
    return new BlockArea(min_x, min_y, min_z - size, max_x, max_y, min_z - 1);
  }
  
  /** Returns a new BlockArea placed directly South of this BlockArea that
   *  has the same Width and Height, but extended in the +Z direction
   *  with the specified Length. */
  public final BlockArea getNewVolumeSouth(final int size){
    return new BlockArea(min_x, min_y, max_z + 1, max_x, max_y, max_z + size);
  }

  // Setters

  /** I think normally it wouldn't matter if you set a BlockArea = to another, but
   *  it would matter if you have two references that are meant to describe the same
   *  area but are separate variables because they're used for different purposes.
   *  If they're the same reference, then changes to one will affect the other. */
  public final void set(final BlockArea other){
    min_x = other.min_x;
    min_y = other.min_y;
    min_z = other.min_z;
    max_x = other.max_x;
    max_y = other.max_y;
    max_z = other.max_z;
  }

  public final void set(final Vec3i min, final Vec3i max){
    min_x = min.getX();
    min_y = min.getY();
    min_z = min.getZ();
    max_x = max.getX();
    max_y = max.getY();
    max_z = max.getZ();
  }
  
  public final void set(final int min_x, final int min_y, final int min_z, final int max_x, final int max_y, final int max_z){
    this.min_x = min_x;
    this.min_y = min_y;
    this.min_z = min_z;
    this.max_x = max_x;
    this.max_y = max_y;
    this.max_z = max_z;
  }
  
  public final void setMinimum(final Vec3i min){
    this.min_x = min.getX();
    this.min_y = min.getY();
    this.min_z = min.getZ();
  }
  
  public final void setMinimum(final int x, final int y, final int z){
    this.min_x = x;
    this.min_y = y;
    this.min_z = z;
  }
  
  public final void setMaximum(final Vec3i max){
    this.max_x = max.getX();
    this.max_y = max.getY();
    this.max_z = max.getZ();
  }
  
  public final void setMaximum(final int x, final int y, final int z){
    this.max_x = x;
    this.max_y = y;
    this.max_z = z;
  }

  /** Sets the total height by extending the Maximum X from the Minimum X.
   *  A value of 1 will set the Maximum equal to the Minimum.<br>
   *  Does not change the Minimum position, so you may want to offset the
   *  BlockArea to your preferred position. */
  public final void setWidth(final int width){
    max_x = min_x - 1 + width;
  }
  
  /** Sets the total height by extending the Maximum Y from the Minimum Y.
   *  A value of 1 will set the Maximum equal to the Minimum.<br>
   *  Does not change the Minimum position, so you may want to offset the
   *  BlockArea to your preferred position. */
  public final void setHeight(final int height){
    max_y = min_y - 1 + height;
  }
  
  /** Sets the total height by extending the Maximum Z from the Minimum Z.
   *  A value of 1 will set the Maximum equal to the Minimum.<br>
   *  Does not change the Minimum position, so you may want to offset the
   *  BlockArea to your preferred position. */
  public final void setLength(final int length){
    max_z = min_z - 1 + length;
  }

  /** Sets this Minimum X and Maximum X to match that of the passed-in BlockArea. */
  public final void setWidth(final BlockArea area){
    min_x = area.min_x;
    max_x = area.max_x;
  }
  
  /** Sets this Minimum Y and Maximum Y to match that of the passed-in BlockArea. */
  public final void setHeight(final BlockArea area){
    min_y = area.min_y;
    max_y = area.max_y;
  }
  
  /** Sets this Minimum Z and Maximum Z to match that of the passed-in BlockArea. */
  public final void setLength(final BlockArea area){
    min_z = area.min_z;
    max_z = area.max_z;
  }

  // Special Set functions
  
  /** Sets this BlockArea's dimensions to be directly West of the
   *  passed-in area, and sets it to be the same size. */
  public final void setWest(final BlockArea area){
    setWest(area, area.getWidth());
  }
  
  /** Sets this BlockArea's dimensions to be directly East of the
   *  passed-in area, and sets it to be the same size. */
  public final void setEast(final BlockArea area){
    setEast(area, area.getWidth());
  }
  
  /** Sets this BlockArea's dimensions to be directly Below the
   *  passed-in area, and sets it to be the same size. */
  public final void setBelow(final BlockArea area){
    setBelow(area, area.getHeight());
  }
  
  /** Sets this BlockArea's dimensions to be directly Above the
   *  passed-in area, and sets it to be the same size. */
  public final void setAbove(final BlockArea area){
    setAbove(area, area.getHeight());
  }
  
  /** Sets this BlockArea's dimensions to be directly North of the
   *  passed-in area, and sets it to be the same size. */
  public final void setNorth(final BlockArea area){
    setNorth(area, area.getLength());
  }
  
  /** Sets this BlockArea's dimensions to be directly South of the
   *  passed-in area, and sets it to be the same size. */
  public final void setSouth(final BlockArea area){
    setSouth(area, area.getLength());
  }
  
  /** Sets this BlockArea's dimensions to be directly West of the
   *  passed-in area, extended by the specified size. */
  public final void setWest(final BlockArea area, final int size){
    min_x = area.min_x - size;
    min_y = area.min_y;
    min_z = area.min_z;
    max_x = area.min_x - 1;
    max_y = area.max_y;
    max_z = area.max_z;
  }
  
  /** Sets this BlockArea's dimensions to be directly East of the
   *  passed-in area, extended by the specified size. */
  public final void setEast(final BlockArea area, final int size){
    min_x = area.max_x + 1;
    min_y = area.min_y;
    min_z = area.min_z;
    max_x = area.max_x + size;
    max_y = area.max_y;
    max_z = area.max_z;
  }
  
  /** Sets this BlockArea's dimensions to be directly Below the
   *  passed-in area, extended by the specified size. */
  public final void setBelow(final BlockArea area, final int size){
    min_x = area.min_x;
    min_y = area.min_y - size;
    min_z = area.min_z;
    max_x = area.max_x;
    max_y = area.min_y - 1;
    max_z = area.max_z;
  }
  
  /** Sets this BlockArea's dimensions to be directly Above of the
   *  passed-in area, extended by the specified size. */
  public final void setAbove(final BlockArea area, final int size){
    min_x = area.min_x;
    min_y = area.max_y + 1;
    min_z = area.min_z;
    max_x = area.max_x;
    max_y = area.max_y + size;
    max_z = area.max_z;
  }
  
  /** Sets this BlockArea's dimensions to be directly North of the
   *  passed-in area, extended West by the specified size. */
  public final void setNorth(final BlockArea area, final int size){
    min_x = area.min_x;
    min_y = area.min_y;
    min_z = area.min_z - size;
    max_x = area.max_x;
    max_y = area.max_y;
    max_z = area.min_z - 1;
  }
  
  /** Sets this BlockArea's dimensions to be directly South of the
   *  passed-in area, extended West by the specified size. */
  public final void setSouth(final BlockArea area, final int size){
    min_x = area.min_x;
    min_y = area.min_y;
    min_z = area.max_z + 1;
    max_x = area.max_x;
    max_y = area.max_y;
    max_z = area.max_z + size;
  }

  /** Sets this BlockArea to be just West of the supplied BlockArea,
   *  but doesn't align the Y or Z dimensions. */
  public final void setWestOf(final BlockArea area){
    final int width = getWidth();
    min_x = area.min_x - width;
    max_x = area.min_x - 1;
  }
  
  /** Sets this BlockArea to be just East of the supplied BlockArea,
   *  but doesn't align the Y or Z dimensions. */
  public final void setEastOf(final BlockArea area){
    final int width = getWidth();
    min_x = area.max_x + 1;
    max_x = area.max_x + width;
  }
  
  /** Sets this BlockArea to be just Below the supplied BlockArea,
   *  but doesn't align the X or Z dimensions. */
  public final void setBelowOf(final BlockArea area){
    final int height = getHeight();
    min_y = area.min_y - height;
    max_y = area.min_y - 1;
  }
  
  /** Sets this BlockArea to be just Above the supplied BlockArea,
   *  but doesn't align the X or Z dimensions. */
  public final void setAboveOf(final BlockArea area){
    final int height = getHeight();
    min_y = area.max_y + 1;
    max_y = area.max_y + height;
  }
  
  /** Sets this BlockArea to be just North of the supplied BlockArea,
   *  but doesn't align the X or Y dimensions. */
  public final void setNorthOf(final BlockArea area){
    final int length = getLength();
    min_z = area.min_z - length;
    max_z = area.min_z - 1;
  }
  
  /** Sets this BlockArea to be just South of the supplied BlockArea,
   *  but doesn't align the X or Y dimensions. */
  public final void setSouthOf(final BlockArea area){
    final int length = getLength();
    min_z = area.max_z + 1;
    max_z = area.max_z + length;
  }

  /** Places this BlockArea directly West of the specified BlockArea
   *  and also aligns along the Y and Z axiis. Does not change size. */
  public final void setDirectlyWestOf(final BlockArea area){
    setWestOf(area);
    alignCenterY(area);
    alignCenterZ(area);
  }
  
  /** Places this BlockArea directly East of the specified BlockArea
   *  and also aligns along the Y and Z axiis. Does not change size. */
  public final void setDirectlyEastOf(final BlockArea area){
    setEastOf(area);
    alignCenterY(area);
    alignCenterZ(area);
  }
  
  /** Places this BlockArea directly Below the specified BlockArea
   *  and also aligns along the X and Z axiis. Does not change size. */
  public final void setDirectlyBelowOf(final BlockArea area){
    setBelowOf(area);
    alignCenterX(area);
    alignCenterZ(area);
  }
  
  /** Places this BlockArea directly Above the specified BlockArea
   *  and also aligns along the X and Z axiis. Does not change size. */
  public final void setDirectlyAboveOf(final BlockArea area){
    setAboveOf(area);
    alignCenterX(area);
    alignCenterZ(area);
  }
  
  /** Places this BlockArea directly North of the specified BlockArea
   *  and also aligns along the X and Y axiis. Does not change size. */
  public final void setDirectlyNorthOf(final BlockArea area){
    setNorthOf(area);
    alignCenterX(area);
    alignCenterY(area);
  }
  
  /** Places this BlockArea directly South of the specified BlockArea
   *  and also aligns along the X and Y axiis. Does not change size. */
  public final void setDirectlySouthOf(final BlockArea area){
    setSouthOf(area);
    alignCenterX(area);
    alignCenterY(area);
  }

  /** Checks if any part of this BlockArea lies outside
   *  the World boundary, and resizes itself to fit inside. */
  public final void setWithinWorldBoundary(final Level world){
    final int min_build_height = world.getMinBuildHeight();
    if(min_y < min_build_height){
      min_y = min_build_height;
    }
    final int max_build_height = world.getMaxBuildHeight() - 1;
    if(max_y > max_build_height){
      max_y = max_build_height;
    }
    // X and Z dimension have the same limit as an Integer which is
    // +/- 2.7 Billion, but world boundary is capped at +/- 3 Million.
    // World border is configurable and prevents entities from moving beyond
    // or placing or interacting with blocks beyond.
    // Can't figure out how to restrict along the X or Z axiis at this moment.
  }

  // Standard BlockArea functions

  /** If this position lies outside of this BlockArea, then this
   *  BlockArea will be expanded to include the position. */
  public final void add(final Vec3i pos){
    add(pos.getX(), pos.getY(), pos.getZ());
  }
  
  /** If this position lies outside of this BlockArea, then this
   *  BlockArea will be expanded to include the position. */
  public final void add(final int x, final int y, final int z){
    if(x < min_x){min_x = x;}
    if(y < min_y){min_y = y;}
    if(z < min_z){min_z = z;}
    if(x > max_x){max_x = x;}
    if(y > max_y){max_y = y;}
    if(z > max_z){max_z = z;}
  }

  /** This will expand the dimensions of the BlockArea if the passed-in
   *  BlockArea has a smaller Minimum or larger Maximum. For example,
   *  given one area of 2 to 8, and another area of 5 to 13, the result
   *  area will have a size of 2 to 13.
   */
  public final void merge(final BlockArea other){
    if(other.min_x < min_x){ min_x = other.min_x; }
    if(other.min_y < min_y){ min_y = other.min_y; }
    if(other.min_z < min_z){ min_z = other.min_z; }
    if(other.max_x > max_x){ max_x = other.max_x; }
    if(other.max_y > max_y){ max_y = other.max_y; }
    if(other.max_z > max_z){ max_z = other.max_z; }
  }

  /*
  public final void keepLowest(final BlockArea other){
    if(other.min_x < min_x){ min_x = other.min_x; }
    if(other.min_y < min_y){ min_y = other.min_y; }
    if(other.min_z < min_z){ min_z = other.min_z; }
  }
  
  public final void keepHighest(final BlockArea other){
    if(other.max_x > max_x){ max_x = other.max_x; }
    if(other.max_y > max_y){ max_y = other.max_y; }
    if(other.max_z > max_z){ max_z = other.max_z; }
  }
  */
  
  /** This will change the dimensions of this BlockArea to only the
   *  space that occupies both BlockAreas. For example, given one
   *  BlockArea of 2 to 8, and another BlockArea of 5 to 13, the
   *  resulting BlockArea will be 5 to 8.
   */
  public final void keepOverlap(final BlockArea other){
    if(other.min_x > min_x){ min_x = other.min_x; }
    if(other.min_y > min_y){ min_y = other.min_y; }
    if(other.min_z > min_z){ min_z = other.min_z; }
    if(other.max_x < max_x){ max_x = other.max_x; }
    if(other.max_y < max_y){ max_y = other.max_y; }
    if(other.max_z < max_z){ max_z = other.max_z; }
  }

  /** If this position lies inside the BlockArea, then the BlockArea
   *  will shrink until it does not include the position. */
  public final void remove(final Vec3i pos){
    remove(pos.getX(), pos.getY(), pos.getZ());
  }
  
  /** If this position lies inside the BlockArea, then the BlockArea
   *  will shrink until it does not include the position. */
  public final void remove(final int x, final int y, final int z){
    if(contains(x, y, z)){
      final int x_offset = getOffsetRelativeToCenterX(x);
      final int y_offset = getOffsetRelativeToCenterY(y);
      final int z_offset = getOffsetRelativeToCenterZ(z);
      if(x_offset <= 0){
        min_x = x + 1;
      }
      else{
        max_x = x - 1;
      }
      if(y_offset <= 0){
        min_y = y + 1;
      }
      else{
        max_y = y - 1;
      }
      if(z_offset <= 0){
        min_z = z + 1;
      }
      else{
        max_z = z - 1;
      }
    }
  }

  /** This will shrink the dimensions of the BlockArea if any part of
   *  the passed-in BlockArea overlaps with our space. For example, given
   *  one BlockArea of 2 to 8, and another BlockArea of 5 to 13, the
   *  first BlockArea will be changed to 2 to 4. It shrunk from the
   *  right side because the other BlockArea was closer to that side. */
  public final void remove(final BlockArea other){
    if(intersects(other)){
      final int x_offset = other.min_x - min_x;
      final int y_offset = other.min_y - min_y;
      final int z_offset = other.min_z - min_z;
      if(x_offset <= 0){
        min_x = other.max_x + 1;
      }
      else{
        max_x = other.min_x - 1;
      }
      if(y_offset <= 0){
        min_y = other.max_y + 1;
      }
      else{
        max_y = other.min_y - 1;
      }
      if(z_offset <= 0){
        min_z = other.max_z + 1;
      }
      else{
        max_z = other.min_z - 1;
      }
    }
  }

  /** This will check which side of the BlockArea this position is closest to and then
   *  move away in the opposite direction until the position is NOT in this BlockArea.
   *  For example, for a BlockArea that goes from 3 to 6 and a position of 4, the
   *  position is closest to the left value, so the BlockArea will move right 2 spaces
   *  and end up with an area of 5 to 8, thus no longer containing 4. If the position
   *  is equal distance away from two or more sides, we first check the X axis, then
   *  the Y axis, then the Z axis, and move towards the positive direction.
   *  This will not have any effect if the position is NOT inside this BlockArea. */
  public final void moveAwayFrom(final Vec3i position){
    moveAwayFrom(position.getX(), position.getY(), position.getZ());
  }
  
  /** This will check which side of the BlockArea this position is closest to and then
   *  move away in the opposite direction until the position is NOT in this BlockArea.
   *  For example, for a BlockArea that goes from 3 to 6 and a position of 4, the
   *  position is closest to the left value, so the BlockArea will move right 2 spaces
   *  and end up with an area of 5 to 8, thus no longer containing 4. If the position
   *  is equal distance away from two or more sides, we first check the X axis, then
   *  the Y axis, then the Z axis, and move towards the positive direction.
   *  This will not have any effect if the position is NOT inside this BlockArea. */
  public final void moveAwayFrom(final int x, final int y, final int z){
    if(contains(x, y, z)){
      final int  west_offset = x - min_x;
      final int  east_offset = max_x - x;
      final int  down_offset = y - min_y;
      final int    up_offset = max_y - y;
      final int north_offset = z - min_z;
      final int south_offset = max_z - z;
      final int   min_offset = CommonMath.getMin(west_offset, east_offset, down_offset, up_offset, north_offset, south_offset);
      if(min_offset ==  west_offset){ moveEast ( west_offset); return;}
      if(min_offset ==  east_offset){ moveWest ( east_offset); return;}
      if(min_offset ==  down_offset){ moveUp   ( down_offset); return;}
      if(min_offset ==    up_offset){ moveDown (   up_offset); return;}
      if(min_offset == north_offset){ moveSouth(north_offset); return;}
      if(min_offset == south_offset){ moveNorth(south_offset); return;}
    }
  }

  /** This will move this BlockArea away from the other BlockArea in the direction
   *  that has the shortest distance. If there is more than one direction that has
   *  the shortest diatance, we first move East, then Up, then South. If these
   *  BlockAreas do not intersect with one another, then nothing happens. */
  public final void moveAwayFrom(final BlockArea other){
    if(intersects(other)){
      final int  west_offset = other.max_x - min_x;
      final int  east_offset = max_x - other.min_x;
      final int  down_offset = other.max_y - min_y;
      final int    up_offset = max_y - other.min_y;
      final int north_offset = other.max_z - min_z;
      final int south_offset = max_z - other.min_z;
      final int   min_offset = CommonMath.getMin(west_offset, east_offset, down_offset, up_offset, north_offset, south_offset);
      if(min_offset ==  west_offset){ moveEast ( west_offset); return;}
      if(min_offset ==  east_offset){ moveWest ( east_offset); return;}
      if(min_offset ==  down_offset){ moveUp   ( down_offset); return;}
      if(min_offset ==    up_offset){ moveDown (   up_offset); return;}
      if(min_offset == north_offset){ moveSouth(north_offset); return;}
      if(min_offset == south_offset){ moveNorth(south_offset); return;}
    }
  }

  /** Rotates along the X-axis, or Pitch. Y and Z dimensions are swapped. */
  public final void RotateXAxis(){
    final int temp_min_y = min_y;
    final int temp_max_y = max_y;
    final int center_y = getCenterY();
    final int center_z = getCenterZ();
    min_y = center_y + (min_z - center_z);
    max_y = center_y + (max_z - center_z);
    min_z = center_z + (temp_min_y - center_y);
    max_z = center_z + (temp_max_y - center_y);
  }
  
  /** Rotates along the Y-axis, or Yaw. X and Z dimensions are swapped. */
  public final void RotateYAxis(){
    final int temp_min_x = min_x;
    final int temp_max_x = max_x;
    final int center_x = getCenterX();
    final int center_z = getCenterZ();
    min_x = center_x + (min_z - center_z);
    max_x = center_x + (max_z - center_z);
    min_z = center_z + (temp_min_x - center_x);
    max_z = center_z + (temp_max_x - center_x);
  }
  
  /** Rotates along the Z-axis, or Roll. X and Y dimensions are swapped. */
  public final void RotateZAxis(){
    final int temp_min_x = min_x;
    final int temp_max_x = max_x;
    final int center_x = getCenterX();
    final int center_y = getCenterY();
    min_x = center_x + (min_y - center_y);
    max_x = center_x + (max_y - center_y);
    min_y = center_y + (temp_min_x - center_x);
    max_y = center_y + (temp_max_x - center_x);
  }

  /** This will move the BlockArea so that the Minimum is at (0, 0, 0)
   *  preserving the size of the dimensions. For example, given a BlockArea
   *  with a Minimum of (2, 6, -3) and Maximum of (9, 18, 4), after normalize
   *  the Minimum will be (0, 0, 0) and the Maximum will be (7, 12, 7). */
  public final void normalize(){
    offset(max_x - min_x, max_y - min_y, max_z - min_z);
  }

  // Area Meta functions

  /** Returns true if any part of the BlockAreas intersect with each other. */
  public final boolean intersects(final BlockArea other){
    return (other.min_x <= max_x || other.max_x >= min_x) &&
           (other.min_y <= max_y || other.max_y >= min_y) &&
           (other.min_z <= max_x || other.max_z >= min_z);
  }

  /** Returns true if the position exists within this BlockArea. */
  public final boolean contains(final Vec3i position){
    return contains(position.getX(), position.getY(), position.getZ());
  }

  /** Returns true if the position exists within this BlockArea. */
  public final boolean contains(final int x, final int y, final int z){
    return x >= min_x && x <= max_x && y >= min_y && y <= max_y && z >= min_z && z <= max_z;
  }

  /** Returns true if this BlockArea is completely inside the specified BlockArea. */
  public final boolean isInside(final BlockArea area){
    return min_x >= area.min_x && max_x <= area.max_x &&
           min_y >= area.min_y && max_y <= area.max_y &&
           min_z >= area.min_z && max_x <= area.max_z;
  }

  /** Returns true if these BlockAreas DO NOT intersect with each other. */
  public final boolean isOutside(final BlockArea area){
    return !intersects(area);
  }

  /** Returns true if this BlockArea has the specified BlockArea completely inside of it. */
  public final boolean encompasses(final BlockArea other){
    return min_x <= other.min_x && max_x >= other.max_x &&
           min_y <= other.min_y && max_y >= other.max_y &&
           min_z <= other.min_z && max_z >= other.max_z;
  }

  // Expanding

  /** Expands in all directions by 1 space. */
  public final void expand(){
    expand(1);
  }
  
  /** Expands in all directions by the amount that you specify. */
  public final void expand(final int amount){
    min_x -= amount;
    min_y -= amount;
    min_z -= amount;
    max_x += amount;
    max_y += amount;
    max_z += amount;
  }
  
  /** Expands each dimension by the amounts that you specify. */
  public final void expand(final int width, final int height, final int length){
    min_x -= width;
    max_x += width;
    min_y -= height;
    max_y += height;
    min_z -= length;
    max_z += length;
  }
  
  /** Expand the given direction by 1 space. */
  public final void expandDirection(final Direction direction){
    expandDirection(direction.get3DDataValue(), 1);
  }
  
  /** Expand the given direction by 1 space. */
  public final void expandDirection(final int direction){
    expandDirection(direction, 1);
  }
  
  /** Expand the given direction by the amount you specify. */
  public final void expandDirection(final Direction direction, final int amount){
    expandDirection(direction.get3DDataValue(), amount);
  }
  
  /** Expand the given direction by the amount you specify. */
  public final void expandDirection(final int direction, final int amount){
    switch(direction){
    case DirectionConstant.WEST:  min_x -= amount; break;
    case DirectionConstant.EAST:  max_x += amount; break;
    case DirectionConstant.DOWN:  min_y -= amount; break;
    case DirectionConstant.UP:    max_x += amount; break;
    case DirectionConstant.NORTH: min_z -= amount; break;
    case DirectionConstant.SOUTH: max_z += amount; break;
    }
  }
  
  // Shrinking
  
  /** Shrinks the entire area by 1 space in each dimension. */
  public final void shrink(){
    shrink(1);
  }
  
  /** Shrinks the entire area by the amount that you specify. */
  public final void shrink(final int amount){
    min_x += amount;
    min_y += amount;
    min_z += amount;
    max_x -= amount;
    max_y -= amount;
    max_z -= amount;
  }
  
  /** Shrinks each dimension by the amounts that you specify. */
  public final void shrink(final int width, final int height, final int length){
    min_x += width;
    max_x -= width;
    min_y += height;
    max_y -= height;
    min_z += length;
    max_z -= length;
  }
  
  /** Shrinks in the direction you specify by 1 space. */
  public final void shrinkDirection(final Direction direction){
    shrinkDirection(direction.get3DDataValue(), 1);
  }
  
  /** Shrinks in the direction you specify by 1 space. */
  public final void shrinkDirection(final int direction){
    shrinkDirection(direction, 1);
  }
  
  /** Shrinks in the direction you specify by the amount. */
  public final void shrinkDirection(final Direction direction, final int amount){
    shrinkDirection(direction.get3DDataValue(), amount);
  }
  
  /** Shrinks in the direction you specify by the amount. */
  public final void shrinkDirection(final int direction, final int amount){
    switch(direction){
    case DirectionConstant.WEST:  min_x += amount; break;
    case DirectionConstant.EAST:  max_x -= amount; break;
    case DirectionConstant.DOWN:  min_y += amount; break;
    case DirectionConstant.UP:    max_y -= amount; break;
    case DirectionConstant.NORTH: min_z += amount; break;
    case DirectionConstant.SOUTH: max_z -= amount; break;
    }
  }

  /** Shrinks this BlockArea so the dimensions are less than the supplied 3D vector. */
  public final void reduce(final Vec3i pos){
    reduce(pos.getX(), pos.getY(), pos.getZ());
  }
  
  /** Shrinks this BlockArea so the dimensions are less than the supplied 3D vector. */
  public final void reduce(final int x, final int y, final int z){
    if(withinX(x)){
      final int x_offset = getOffsetRelativeToCenterX(x);
      if(x_offset <= 0){
        min_x = x + 1;
      }
      else{
        max_x = x - 1;
      }
    }
    if(withinY(y)){
      final int y_offset = getOffsetRelativeToCenterY(y);
      if(y_offset <= 0){
        min_y = y + 1;
      }
      else{
        max_y = y - 1;
      }
    }
    if(withinZ(z)){
      final int z_offset = getOffsetRelativeToCenterZ(z);
      if(z_offset <= 0){
        min_z = z + 1;
      }
      else{
        max_z = z - 1;
      }
    }
  }

  // Move functions

  /** Moves the entire area in the direction you specify by 1 space. */
  public final void move(final Direction direction){
    move(direction.get3DDataValue(), 1);
  }
  
  /** Moves the entire area in the direction you specify by 1 space. */
  public final void move(final int direction){
    move(direction, 1);
  }
  
  /** Moves the entire area in the direction and amount that you specify. */
  public final void move(final Direction direction, final int amount){
    move(direction.get3DDataValue(), amount);
  }
  
  /** Moves the entire area in the direction and amount that you specify. */
  public final void move(final int direction, final int amount){
    switch(direction){
    case DirectionConstant.DOWN:  moveDown (amount); break;
    case DirectionConstant.UP:    moveUp   (amount); break;
    case DirectionConstant.WEST:  moveWest (amount); break;
    case DirectionConstant.EAST:  moveEast (amount); break;
    case DirectionConstant.NORTH: moveNorth(amount); break;
    case DirectionConstant.SOUTH: moveSouth(amount); break;
    }
  }

  /** Moves area by Axis. Here you can specify a negative amount to move in the opposite direction. */
  public final void Move(final Direction.Axis axis, final int amount){
    switch(axis){
    case X: MoveX(amount); break;
    case Y: MoveY(amount); break;
    case Z: MoveZ(amount); break;
    }
  }
  
  /** Moves along the X axis. West if positive, East if negative. */
  public final void MoveX(final int amount){
    min_x += amount;
    max_x += amount;
  }
  
  /** Moves along the Y axis. Up if positive, Down if negative. */
  public final void MoveY(final int amount){
    min_y += amount;
    max_y += amount;
  }
  
  /** Moves along the Z axis. South if positive, North if negative. */
  public final void MoveZ(final int amount){
    min_z += amount;
    max_z += amount;
  }
  
  public final void moveWest(){
    min_x--;
    max_x--;
  }
  
  public final void moveWest(final int amount){
    min_x -= amount;
    max_x -= amount;
  }
  
  public final void moveEast(){
    min_x++;
    max_x++;
  }
  
  public final void moveEast(final int amount){
    min_x += amount;
    max_x += amount;
  }
  
  public final void moveDown(){
    min_y--;
    max_y--;
  }
  
  public final void moveDown(final int amount){
    min_y -= amount;
    max_y -= amount;
  }
  
  public final void moveUp(){
    min_y++;
    max_y++;
  }
  
  public final void moveUp(final int amount){
    min_y += amount;
    max_y += amount;
  }
  
  public final void moveNorth(){
    min_z--;
    max_z--;
  }
  
  public final void moveNorth(final int amount){
    min_z -= amount;
    max_z -= amount;
  }
  
  public final void moveSouth(){
    min_z++;
    max_z++;
  }
  
  public final void moveSouth(final int amount){
    min_z += amount;
    max_z += amount;
  }

  /** Moves the area so that the Minimum matches the position. */
  public final void moveTo(final Vec3i position){
    offset(position.getX() - min_x, position.getY() - min_y, position.getZ() - min_z);
  }
  
  /** Moves this BlockArea so that the Minimum is in the same position
   *  as the Minimum of the supplied BlockArea. */
  public final void moveTo(final BlockArea area){
    moveTo(area.getMinimum());
  }
  
  /** <p>Moves this BlockArea so it is center-aligned with the passed-in BlockArea.
   *  <p>For example, given a BlockArea of -5 to 3 (length of 9) and a BlockArea of
   *     5 to 7 (length of 3), the first BlockArea will move to 2 and 10, because
   *     at that position, both BlockAreas will have the same center (6).
   *  <p>We try to center-align them as best we can. We can achieve this if both
   *     BlockAreas have all odd length dimensions. You can check this by calling
   *     {@link #hasTrueCenter()}.
   * @param area
   */
  public final void moveToCenter(final BlockArea area){
    final Vec3i center = getCenter();
    final Vec3i other_center = area.getCenter();
    offset(other_center.getX() - center.getX(), other_center.getY() - center.getY(), other_center.getZ() - center.getZ());
  }
  
  /** <p>Moves this BlockArea so the center is at the specified position. 
   *  <p>You can get the offset coordinates without moving by calling
   *  {@link #getOffsetRelativeToCenter(Vec3i)}. */
  public final void moveCenterTo(final Vec3i pos){
    final Vec3i center = getCenter();
    offset(pos.getX() - center.getX(), pos.getY() - center.getY(), pos.getZ() - center.getZ());
  }
  
  /** This will move the BlockArea by the amount specified in the 3D vector. */
  public final void offset(final Vec3i offset){
    offset(offset.getX(), offset.getY(), offset.getZ());
  }
  
  /** This will move this BlockArea by the amounts that you specify. */
  public final void offset(final int x_offset, final int y_offset, final int z_offset){
    min_x += x_offset;
    max_x += x_offset;
    min_y += y_offset;
    max_y += y_offset;
    min_z += z_offset;
    max_z += z_offset;
  }

  // Adjacent functions
  
  /** Returns whether the position is touching this BlockArea. */
  public final boolean isAdjacent(final Vec3i position){
    final int x = position.getX();
    final int y = position.getY();
    final int z = position.getZ();
    return min_x - 1 == x || max_x + 1 == x ||
           min_y - 1 == y || max_y + 1 == y ||
           min_z - 1 == z || max_z + 1 == z;
  }
  
  /** Returns whether these two BlockAreas have at least 1 block adjacent to each other. */
  public final boolean isAdjacent(final BlockArea other){
    // TEST: BlockArea.isAdjacent(BlockArea) probably doesn't work right.
    return min_x - 1 == other.max_x || max_x + 1 == other.min_x ||
           min_y - 1 == other.max_y || max_y + 1 == other.min_y ||
           min_z - 1 == other.max_z || max_z + 1 == other.min_z;
  }
  
  /** Returns whether the position is exactly 1 block West of this
   *  area, but doesn't account for the Y position or Z position. */
  public final boolean isAdjacentWest(final Vec3i position){
    return min_x - 1 == position.getX();
  }
  
  /** Returns whether the position is exactly 1 block East of this
   *  area, but doesn't account for the Y position or Z position. */
  public final boolean isAdjacentEast(final Vec3i position){
    return max_x + 1 == position.getX();
  }
  
  /** Returns whether the position is exactly 1 block Down from this
   *  area, but doesn't account for the X position or Z position. */
  public final boolean isAdjacentDown(final Vec3i position){
    return min_y - 1 == position.getY();
  }
  
  /** Returns whether the position is exactly 1 block Up from this
   *  area, but doesn't account for the X position or Z position. */
  public final boolean isAdjacentUp(final Vec3i position){
    return max_y + 1 == position.getY();
  }
  
  /** Returns whether the position is exactly 1 block North of this
   *  area, but doesn't account for the X position or Y position. */
  public final boolean isAdjacentNorth(final Vec3i position){
    return min_z - 1 == position.getZ();
  }
  
  /** Returns whether the position is exactly 1 block South of this
   *  area, but doesn't account for the X position or Y position. */
  public final boolean isAdjacentSouth(final Vec3i position){
    return max_z + 1 == position.getZ();
  }
  
  /** Returns whether the position is directly against the West side of this BlockArea. */
  public final boolean isDirectlyAdjacentWest(final Vec3i position){
    return withinY(position.getY()) && withinZ(position.getZ()) && min_x - 1 == position.getX();
  }
  
  /** Returns whether the position is directly against the East side of this BlockArea. */
  public final boolean isDirectlyAdjacentEast(final Vec3i position){
    return withinY(position.getY()) && withinZ(position.getZ()) && max_x + 1 == position.getX();
  }
  
  /** Returns whether the position is directly against the Bottom side of this BlockArea. */
  public final boolean isDirectlyAdjacentDown(final Vec3i position){
    return withinX(position.getX()) && withinZ(position.getZ()) && min_y - 1 == position.getY();
  }
  
  /** Returns whether the position is directly against the Top side of this BlockArea. */
  public final boolean isDirectlyAdjacentUp(final Vec3i position){
    return withinX(position.getX()) && withinZ(position.getZ()) && max_y + 1 == position.getY();
  }
  
  /** Returns whether the position is directly against the North side of this BlockArea. */
  public final boolean isDirectlyAdjacentNorth(final Vec3i position){
    return withinX(position.getX()) && withinY(position.getY()) && min_z - 1 == position.getZ();
  }
  
  /** Returns whether the position is directly against the South side of this BlockArea. */
  public final boolean isDirectlyAdjacentSouth(final Vec3i position){
    return withinX(position.getX()) && withinY(position.getY()) && max_z + 1 == position.getZ();
  }

  // Offset functions

  /** Returns whether the position is West of this BlockArea. */
  public final boolean isPositionWest(final Vec3i position){ return getRelativePositionX(position.getX()) < 0; }
  
  /** Returns whether the position is East of this BlockArea. */
  public final boolean isPositionEast(final Vec3i position){ return getRelativePositionX(position.getX()) > 0; }
  
  /** Returns whether the position is Below this BlockArea. */
  public final boolean isPositionBelow(final Vec3i position){ return getRelativePositionY(position.getY()) < 0; }
  
  /** Returns whether the position is Above this BlockArea. */
  public final boolean isPositionAbove(final Vec3i position){ return getRelativePositionY(position.getY()) > 0; }
  
  /** Returns whether the position is North of this BlockArea. */
  public final boolean isPositionNorth(final Vec3i position){ return getRelativePositionZ(position.getZ()) < 0; }
  
  /** Returns whether the position is South of this BlockArea. */
  public final boolean isPositionSouth(final Vec3i position){ return getRelativePositionZ(position.getZ()) > 0; }
  
  /** Returns whether this BlockArea is West of the other BlockArea. */
  public final boolean isWest(final BlockArea other){ return other.min_x - min_x > 0; }    

  /** Returns whether this BlockArea is East of the other BlockArea. */
  public final boolean isEast(final BlockArea other){ return other.min_x - min_x < 0; }
  
  /** Returns whether this BlockArea is Below of the other BlockArea. */
  public final boolean isBelow(final BlockArea other){ return other.min_y - min_y > 0; }
  
  /** Returns whether this BlockArea is Above of the other BlockArea. */
  public final boolean isAbove(final BlockArea other){ return other.min_y - min_y < 0; }
  
  /** Returns whether this BlockArea is North of the other BlockArea. */
  public final boolean isNorth(final BlockArea other){ return other.min_z - min_z > 0; }
  
  /** Returns whether this BlockArea is South of the other BlockArea. */
  public final boolean isSouth(final BlockArea other){ return other.min_z - min_z < 0; }

  /** Returns an offset vector of the position in relation to
   *  this BlockArea. To get an offset relative to the BlockArea's
   *  center, use {@link #getOffsetRelativeToCenter(Vec3i)}. */
  public final Vec3i getOffset(final Vec3i position){
    return new Vec3i(position.getX() - min_x, position.getY() - min_y, position.getZ() - min_z);
  }
  
  /** Returns an offset vector of the BlockArea in relation to this BlockArea. */
  public final Vec3i getOffset(final BlockArea area){
    return getOffset(area.getMinimum());
  }

  /** Returns how many spaces away from the X dimension the
   *  value is, or 0 if the position is inside the BlockArea. */
  public final int getRelativePositionX(final int x){
    if(x < min_x){ return x - min_x; } // 4 - 5 = -1
    if(x > max_x){ return x - max_x; } // 6 - 5 = 1
    return 0;
  }
  
  /** Returns how many spaces away from the Y dimension the
   *  value is, or 0 if the position is inside the BlockArea. */
  public final int getRelativePositionY(final int y){
    if(y < min_y){ return y - min_y; }
    if(y > max_y){ return y - max_y; }
    return 0;
  }
  
  /** Returns how many spaces away from the Z dimension the
   *  value is, or 0 if the position is inside the BlockArea. */
  public final int getRelativePositionZ(final int z){
    if(z < min_z){ return z - min_z; }
    if(z > max_z){ return z - max_z; }
    return 0;
  }
  
  /** Returns a 3D vector describing how far away the specified
   *  position is. If one of the vectors is 0, then that means
   *  that coordinate is aligned within this BlockArea. */
  public final Vec3i getRelativePosition(final Vec3i position){
    return new Vec3i(getRelativePositionX(position.getX()),
                     getRelativePositionY(position.getY()),
                     getRelativePositionZ(position.getZ()));
  }

  /** Gets an offset position relative to this BlockArea's center position along the X axis.
   *  If this BlockArea's center is at 5, and the passed in value is 8, then this will return 3. */
  public final int getOffsetRelativeToCenterX(final int x){
    return x - getCenterX();
  }
  
  /** Gets an offset position relative to this BlockArea's center position along the Y axis.
   *  If this BlockArea's center is at 5, and the passed in value is 8, then this will return 3. */
  public final int getOffsetRelativeToCenterY(final int y){
    return y - getCenterY();
  }
  
  /** Gets an offset position relative to this BlockArea's center position along the Z axis.
   *  If this BlockArea's center is at 5, and the passed in value is 8, then this will return 3. */
  public final int getOffsetRelativeToCenterZ(final int z){
    return z - getCenterZ();
  }
  
  /** <p>Gets offset coordinates relative to this BlockArea's center position.
   *  <p>This is exactly what {@link #moveCenterTo(Vec3i)} does to calculate
   *  the offset coordinates needed to move the BlockArea's center to the position. */
  public final Vec3i getOffsetRelativeToCenter(final Vec3i position){
    final Vec3i center = getCenter();
    return new Vec3i(position.getX() - center.getX(), position.getY() - center.getY(), position.getZ() - center.getZ());
  }

  // Alignment Test functions

  /** Returns whether the value is within the X range of this BlockArea. */
  public final boolean withinX(final int x){ return x >= min_x && x <= max_x; }
  
  /** Returns whether the value is within the Y range of this BlockArea. */
  public final boolean withinY(final int y){ return y >= min_y && y <= max_y; }
  
  /** Returns whether the value is within the Z range of this BlockArea. */
  public final boolean withinZ(final int z){ return z >= min_z && z <= max_z; }

  /** Returns whether the two BlockAreas are center-aligned along the axis you specify. */
  public final boolean isAxisAligned(final Direction.Axis axis, final BlockArea area){
    return isAxisAligned(axis, area.getCenter());
  }

  /** Returns whether the position is aligned with the
   *  center of this BlockArea along the axis you specify. */
  public final boolean isAxisAligned(final Direction.Axis axis, final Vec3i position){
    return switch(axis){
    case X -> getCenterX() == position.getX();
    case Y -> getCenterY() == position.getY();
    case Z -> getCenterZ() == position.getZ();
    };
  }
  
  /** Returns whether the two BlockAreas center positions are the same position. */
  public final boolean isCenterAligned(final BlockArea area){
    return isCenterAligned(area.getCenter());
  }
  
  /** Returns whether the position is the same as this BlockArea's center position. */
  public final boolean isCenterAligned(final Vec3i position){
    return getCenter().equals(position);
  }
  
  /** Returns whether the Minimum position is aligned to a grid
   *  with grid cells of the specified size. */
  public final boolean isAlignedToGrid(final int grid_size){
    return min_x % grid_size == 0 && min_y % grid_size == 0 && min_z % grid_size == 0;
  }
  
  /** Returns whether the Minimum position is aligned to a grid
   *  with grid cells of the specified size. */
  public final boolean isAxisAlignedToGrid(final Direction.Axis axis, final int grid_size){
    return switch(axis){
    case X -> min_x % grid_size == 0;
    case Y -> min_y % grid_size == 0;
    case Z -> min_z % grid_size == 0;
    };
  }

  // Alignment functions

  /** Moves this BlockArea so that it's Center X position
   *  is aligned with the provided position. */
  public final void alignCenterX(final Vec3i position){
    MoveX(position.getX() - getCenterX());
  }
  
  /** Moves this BlockArea so that it's Center Y position
   *  is aligned with the provided position. */
  public final void alignCenterY(final Vec3i position){
    MoveY(position.getY() - getCenterY());
  }
  
  /** Moves this BlockArea so that it's Center Z position
   *  is aligned with the provided position. */
  public final void alignCenterZ(final Vec3i position){
    MoveZ(position.getZ() - getCenterZ());
  }

  /** Moves this BlockArea to be centered with the passed-in
   *  BlockArea along the X dimension. */
  public final void alignCenterX(final BlockArea area){
    MoveX(area.getCenterX() - getCenterX());
  }
  
  /** Moves this BlockArea to be centered with the passed-in
   *  BlockArea along the Y dimension. */
  public final void alignCenterY(final BlockArea area){
    MoveY(area.getCenterY() - getCenterY());
  }
  
  /** Moves this BlockArea to be centered with the passed-in
   *  BlockArea along the Z dimension. */
  public final void alignCenterZ(final BlockArea area){
    MoveZ(area.getCenterZ() - getCenterZ());
  }

  /** Aligns the BlockArea to a grid with cells of the specified size.
  /*  For example, 13 rounded to the nearst 5 would be 15. */
  public final void alignToGrid(final int grid_size){
    final int round_x = CommonMath.RoundNearest(min_x, grid_size);
    final int round_y = CommonMath.RoundNearest(min_y, grid_size);
    final int round_z = CommonMath.RoundNearest(min_z, grid_size);
    offset(round_x - min_x, round_y - min_y, round_z - min_z);
  }

  /** Aligns the BlockArea to a grid of the specified sizes. */
  public final void alignToGrid(final int x_size, final int y_size, final int z_size){
    final int round_x = CommonMath.RoundNearest(min_x, x_size);
    final int round_y = CommonMath.RoundNearest(min_y, y_size);
    final int round_z = CommonMath.RoundNearest(min_z, z_size);
    offset(round_x - min_x, round_y - min_y, round_z - min_z);
  }
  
  /** Aligns the BlockArea to a grid with cells of the specified size along the specified axis. */
  public final void alignAxisToGrid(final Direction.Axis axis, final int grid_size){
    switch(axis){
    case X: MoveX(CommonMath.RoundNearest(min_x, grid_size) - min_x); break;
    case Y: MoveY(CommonMath.RoundNearest(min_y, grid_size) - min_y); break;
    case Z: MoveZ(CommonMath.RoundNearest(min_z, grid_size) - min_z); break;
    }
  }
  
  /** Aligns the BlockArea to the closest 16x16x16 grid cell. */
  public final void alignToNearestChunk(){
    alignToGrid(WorldConstants.chunk_size);
  }

  public final void AlignAndResizeToNearestChunk(){
    alignToNearestChunk();
    max_x = min_x + WorldConstants.chunk_size;
    max_y = min_y + WorldConstants.chunk_size;
    max_z = min_z + WorldConstants.chunk_size;
  }

  // Save & Load

  public final CompoundTag save(){
    final CompoundTag tag = new CompoundTag();
    tag.putInt("Min X", min_x);
    tag.putInt("Min Y", min_y);
    tag.putInt("Min Z", min_z);
    tag.putInt("Max X", max_x);
    tag.putInt("Max Y", max_y);
    tag.putInt("Max Z", max_z);
    return tag;
  }

  public static final BlockArea load(final CompoundTag nbt, final String name){
    final CompoundTag tag = nbt.getCompound(name);
    return new BlockArea(
      // CompoundTag.getInt() returns the default 0 if it doesn't exist.
      tag.getInt("Min X"), tag.getInt("Min Y"), tag.getInt("Min Z"),
      tag.getInt("Max X"), tag.getInt("Max Y"), tag.getInt("Max Z")
    );
  }

  // Special functions

  /** Returns the BlockPos in this BlockArea that is closest to the passed in Entity. */
  public final BlockPos getClosest(final Entity player){
    final BlockPos player_position = new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());
    double length = Double.MAX_VALUE;
    BlockPos closest = null;
    double result;
    for(final BlockPos pos : this){
      result = BlockMath.get_distance(player_position, pos);
      if(result < length){
        length = result;
        closest = pos;
      }
    }
    return closest;
  }

  /** Static function. Returns the BlockPos that is closest to the passed in Entity. */
  public static final BlockPos getClosest(final Collection<BlockPos> positions, final Entity player){
    return getClosest(positions, new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ()));
  }
  
  /** Static function. Returns the BlockPos that is closest to the passed in Entity. */
  public static final BlockPos getClosest(final BlockPos[] positions, final Entity player){
    return getClosest(positions, new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ()));
  }

  public static final BlockPos getClosest(final Collection<BlockPos> positions, final BlockPos position){
    double length = Double.MAX_VALUE;
    BlockPos closest = null;
    double result;
    for(final BlockPos pos : positions){
      result = BlockMath.get_distance(position, pos);
      if(result < length){
        length = result;
        closest = pos;
      }
    }
    return closest;
  }
  
  public static final BlockPos getClosest(final BlockPos[] positions, final BlockPos position){
    double length = Double.MAX_VALUE;
    BlockPos closest = null;
    double result;
    for(final BlockPos pos : positions){
      result = BlockMath.get_distance(position, pos);
      if(result < length){
        length = result;
        closest = pos;
      }
    }
    return closest;
  }

  /** Returns a new BlockArea that is big enough to contain all the passed-in blocks. */
  public static final BlockArea get(final Collection<BlockPos> blocks){
    return get(blocks.toArray(new BlockPos[blocks.size()]));
  }
  
  /** Returns a new BlockArea that is big enough to contain all the passed-in blocks. */
  public static final BlockArea get(final BlockPos[] blocks){
    final int length = blocks.length;
    if(length > 0){
      final BlockArea area = new BlockArea(blocks[0]);
      int i;
      for(i = 1; i < length; i++){
        area.add(blocks[i]);
      }
      return area;
    }
    return null;
  }

  /** Returns the minimum position in the list of BlockPositions.<br>
   *  This is often used to get a common origin point which is then
   *  used to check certain blocks at certain positions.
   *  <p>This does not return a BlockPos in the list! It returns the lowest possible position!<br />
   *  For instance, in the following situation where {@code X} represents a block:<br />
   *  <code><pre>
   *     X 
   *    X  
   *    O X</pre></code>
   *  This would return the bottom-left space, represented by an {@code O}.
   */
  public static final BlockPos getMinimumPosition(final Collection<BlockPos> list){
    return getMinimumPosition(list.toArray(new BlockPos[list.size()]));
  }

  /** Returns the minimum position in the list of BlockPositions.<br>
   *  This is often used to get a common origin point which is then
   *  used to check certain blocks at certain positions.
   *  <p>This does not return a BlockPos in the list! It returns the lowest possible position!<br>
   *  For instance, in the following situation where {@code X} represents a block:<br>
   *  <code><pre>
   *     X 
   *    X  
   *    O X</pre></code>
   *  This would return the bottom-left space, represented by an {@code O}.
   */
  public static final BlockPos getMinimumPosition(final BlockPos[] list){
    final int length = list.length;
    if(length > 0){
      int i;
      int x;
      int y;
      int z;
      int min_x = list[0].getX();
      int min_y = list[0].getY();
      int min_z = list[0].getZ();
      for(i = 1; i < length; i++){
        x = list[i].getX();
        y = list[i].getY();
        z = list[i].getZ();
        if(x < min_x){ min_x = x; }
        if(y < min_y){ min_y = y; }
        if(z < min_z){ min_z = z; }
      }
      return new BlockPos(min_x, min_y, min_z);
    }
    throw new IllegalArgumentException("Function "+BlockArea.class.getName()+".getMinimumPosition() requires a list that has at least 1 BlockPosition.");
  }

  /** Returns the maximum position in the list of BlockPositions.
   *  <p>This does not return a BlockPos in the list! It returns the highest possible position!<br>
   *  For instance, in the following situation where {@code X} represents a block:<br>
   *  <code><pre>
   *     XO
   *    X  
   *      X</pre></code>
   *  This would return the top-right space, represented by an {@code O}.
   */
  public static final BlockPos getMaximumPosition(final Collection<BlockPos> list){
    return getMaximumPosition(list.toArray(new BlockPos[list.size()]));
  }

  /** Returns the maximum position in the list of BlockPositions.
   *  <p>This does not return a BlockPos in the list! It returns the highest possible position!<br>
   *  For instance, in the following situation where {@code X} represents a block:<br>
   *  <code><pre>
   *     XO
   *    X  
   *      X</pre></code>
   *  This would return the top-right space, represented by an {@code O}.
   */
  public static final BlockPos getMaximumPosition(final BlockPos[] list){
    final int length = list.length;
    if(length > 0){
      int i;
      int x;
      int y;
      int z;
      int min_x = list[0].getX();
      int min_y = list[0].getY();
      int min_z = list[0].getZ();
      for(i = 1; i < length; i++){
        x = list[i].getX();
        y = list[i].getY();
        z = list[i].getZ();
        if(x > min_x){ min_x = x; }
        if(y > min_y){ min_y = y; }
        if(z > min_z){ min_z = z; }
      }
      return new BlockPos(min_x, min_y, min_z);
    }
    throw new IllegalArgumentException("Function "+BlockArea.class.getName()+".getMaximumPosition() requires a list that has at least 1 BlockPosition.");
  }

  /** Returns true if all the block positions in this list can be grouped
   *  together to form a full rectangular shape with no holes or extrusions.
   *  You must call {@link #get(Collection)} first. */
  public final boolean isFullRectangle(final Collection<BlockPos> list){
    return isFullRectangle(list.toArray(new BlockPos[list.size()]));
  }

  /** Returns true if all the block positions in this list can be grouped
   *  together to form a full rectangular shape with no holes or extrusions.
   *  You must call {@link #get(BlockPos[])} first. */
  public final boolean isFullRectangle(final BlockPos[] list){
    final int width  = getWidth();
    final int height = getHeight();
    final int length = getLength();
    final boolean[][][] check = new boolean[width][height][length];
    int x;
    int y;
    int z;
    try{
      for(final BlockPos pos : list){
        x = pos.getX() - min_x;
        y = pos.getY() - min_y;
        z = pos.getZ() - min_z;
        check[x][y][z] = true;
      }
    }
    catch(ArrayIndexOutOfBoundsException e){
      return false;
    }
    for(z = 0; z < length; z++){
      for(y = 0; y < height; y++){
        for(x = 0; x < width; x++){
          if(!check[x][y][z]){
            return false;
          }
        }
      }
    }
    return true;
  }

  /** Returns a random BlockPosition in this BlockArea. */
  public final BlockPos getRandomPosition(){
    return getRandomPosition(new Random());
  }
  
  /** Returns a random BlockPosition in this BlockArea
   *  using the {@link Random} object of your choice. */
  public final BlockPos getRandomPosition(final Random random){
    return getPositionFromIndex(random.nextInt(getArea()));
  }

  /** Gets a random position along the axis that you specify. May return
   *  a value anywhere between Minimum and Maximum (inclusive). */
  public final int getRandomSlice(final Direction.Axis axis){
    return getRandomSlice(axis, new Random());
  }
  
  /** Gets a random position along the axis that you specify. May return
   *  a value anywhere between Minimum and Maximum (inclusive). */
  public final int getRandomSlice(final Direction.Axis axis, final Random random){
    return switch(axis){
    case X -> min_x + random.nextInt(getWidth());
    case Y -> min_y + random.nextInt(getHeight());
    case Z -> min_z + random.nextInt(getLength());
    };
  }

  // Basic Geometric Test functions

  /** Returns true if the two BlockAreas have the same width. */
  public final boolean sameWidth(final BlockArea area){
    return min_x == area.min_x && max_x == area.max_x;
  }
  
  /** Returns true if the two BlockAreas have the same height. */
  public final boolean sameHeight(final BlockArea area){
    return min_y == area.min_y && max_y == area.max_y;
  }
  
  /** Returns true if the two BlockAreas have the same length. */
  public final boolean sameLength(final BlockArea area){
    return min_z == area.min_z && max_z == area.max_z;
  }

  /** Returns whether at least one of the dimensions is flat. */
  public final boolean isFlatPlane(){
    return (min_x == max_x) || (min_y == max_y) || (min_z == max_z);
  }

  /** Returns whether the BlockArea is flat along the X axis. */  
  public final boolean isFlatPlaneX(){ return min_x == max_x; }
  
  /** Returns whether the BlockArea is flat along the Y axis. */  
  public final boolean isFlatPlaneY(){ return min_y == max_y; }
  
  /** Returns whether the BlockArea is flat along the Z axis. */  
  public final boolean isFlatPlaneZ(){ return min_z == max_z; }

  /** Returns whether this BlockArea is only extending along a single axis. */
  public final boolean isPillar(){
    return isPillarX() || isPillarY() || isPillarZ();
  }
  
  /** Returns whether this BlockArea is only extending along the X axis. */
  public final boolean isPillarX(){
    return min_y == max_y && min_z == max_z;
  }
  
  /** Returns whether this BlockArea is only extending along the Y axis. */
  public final boolean isPillarY(){
    return min_x == max_x && min_z == max_z;
  }
  
  /** Returns whether this BlockArea is only extending along the Z axis. */
  public final boolean isPillarZ(){
    return min_x == max_x && min_y == max_y;
  }

  /** Returns true if this BlockArea has all values set to 0. */
  public final boolean isOrigin(){
    return min_x == 0 && min_y == 0 && min_z == 0 && max_x == 0 && max_y == 0 && max_z == 0;
  }
  
  /** Returns true if this BlockArea is at position (0, 0, 0). */
  public final boolean isAtOrigin(){
    return min_x == 0 && min_y == 0 && min_z == 0;
  }
  
  /** Returns true if the center position is at (0, 0, 0). */
  public final boolean isCenterAtOrigin(){
    final Vec3i center = getCenter();
    return center.getX() == 0 && center.getY() == 0 && center.getZ() == 0;
  }
  
  /** Returns whether the Minimum position is the same as
   *  the Maximum position, thus only defining an area of
   *  a single position and having an area of 1. */
  public final boolean isSinglePoint(){
    return min_x == max_x && min_y == max_y && min_z == max_z;
  }

  /** This will return true if all dimensions have an odd length,
   *  thus meaning you'll get the exact center position if you call
   *  {@link #getCenter()}. */
  public final boolean hasTrueCenter(){
    return (getWidth() % 2 == 1) && (getHeight() % 2 == 1) && (getLength() % 2 == 1);
  }
  
  /** Returns whether that dimension has an odd length. */
  public final boolean isOdd(final Direction.Axis axis){
    final int size = switch(axis){
      case X -> getWidth();
      case Y -> getHeight();
      case Z -> getLength();
    };
    return size % 2 == 1;
  }
  
  /** Returns whether that dimension has an even length. */
  public final boolean isEven(final Direction.Axis axis){
    final int size = switch(axis){
    case X -> getWidth();
    case Y -> getHeight();
    case Z -> getLength();
    };
    return size % 2 == 0;
  }

  /** Returns whether this BlockArea has an area of 0 or less, so this is
   *  just an alias for {@link #isInvalid()}. */
  public final boolean isEmpty(){
    return isInvalid();
  }

  // Meta functions

  /** We allow you to move the Minimum and Maximum positions so that the
   *  Minimum is greater than the Maximum, thus creating a negative area.
   *  In such a case, the result of some of these functions may be
   *  unpredictable. Use this function to check if the BlockArea correctly
   *  has its Minimum position lower or equal to the Maximum position.
   */
  public final boolean valid(){
    return min_x <= max_x && min_y <= max_y && min_z <= max_z;
  }
  
  /** We allow you to move the Minimum and Maximum positions so that the
   *  Minimum is greater than the Maximum, thus creating a negative area. In
   *  such a case, the result of some of these functions may be unpredictable.
   *  Use this function to check if this BlockArea is invalid. If this BlockArea
   *  is invalid, you cannot {@linkplain #getIndex(Vec3i) get an index} value of
   *  a BlockPosition in this area or iterate through any positions in this area.
   */
  public final boolean isInvalid(){
    return min_x > max_x || min_y > max_y || min_z > max_x;
  }
  
  /** If this BlockArea {@link #isInvalid()}, meaning it has at least 1 dimension
   *  whose Maximum is less than the Minimum, this will correct the BlockArea by
   *  swapping any invalid Minimum and Maximum values. */
  public final void correct(){
    int temp;
    if(max_x < min_x){
      temp = min_x;
      min_x = max_x;
      max_x = temp;
    }
    if(max_y < min_y){
      temp = min_y;
      min_y = max_y;
      max_y = temp;
    }
    if(max_z < min_z){
      temp = min_z;
      min_z = max_z;
      max_z = temp;
    }
  }
  
  /** Returns whether this BlockArea is so big that you won't actually
   *  get the total number of blocks if you call {@link #getArea()}. */
  public final boolean hasOverflowIndex(){
    return getAreaInternal() > Integer.MAX_VALUE;
  }

  // Java Overrides

  /** Two BlockAreas will be equivelant if they have the same Minimum and Maximum. */
  @Override
  public final boolean equals(final Object object){
    if(object != null){
      if(object instanceof BlockArea){
        final BlockArea other = (BlockArea)object;
        return min_x == other.min_x &&
               min_y == other.min_y &&
               min_z == other.min_z &&
               max_x == other.max_x &&
               max_y == other.max_y &&
               max_z == other.max_z;
      }
    }
    return false;
  }

  /** Two BlockAreas will have the same hash code if they have the same Minimum and Maximum values. */
  @Override
  public final int hashCode(){
    return Arrays.hashCode(new int[]{min_x, min_y, min_z, max_x, max_y, max_z});
  }

  public final class BlockAreaIterator implements Iterator<BlockPos> {
    private final BlockArea internal;
    private final BlockArea area;
    private final DirectionalLoop loop;
    
    private BlockAreaIterator(final BlockArea area){
      this.internal = area.clone();
      this.area = area;
      this.loop = new DirectionalLoop.South(area);
    }
    
    private BlockAreaIterator(final BlockArea area, final Direction direction){
      this.internal = area.clone();
      this.area = area;
      this.loop = switch(direction){
        case WEST  -> new DirectionalLoop.West(area);
        case EAST  -> new DirectionalLoop.East(area);
        case UP    -> new DirectionalLoop.Up(area);
        case DOWN  -> new DirectionalLoop.Down(area);
        case NORTH -> new DirectionalLoop.North(area);
        case SOUTH -> new DirectionalLoop.South(area);
      };
    }
    
    @Override
    public final boolean hasNext(){
      return !loop.hasReachedEnd();
    }
    
    @Override
    public final BlockPos next(){
      if(!internal.equals(area)){
        throw new ConcurrentModificationException("Cannot modify BlockArea while you are iterating through it.");
      }
      // get BlockPos first, then increment
      final BlockPos pos = loop.getPosition();
      loop.increment();
      return pos;
    }
  
  }

  /** <p>This will allow you to use the BlockArea in enhanced for loops
   *  to easily loop through all BlockPositions defined in this area.
   *  <p>This does not suffer from the Position index Integer overflow
   *  problem that {@link #getArea()} does, and will always loop through
   *  the entire BlockArea. */
  @Override
  public final Iterator<BlockPos> iterator(){
    return new BlockAreaIterator(this);
  }

  public final BlockAreaIterator getDirectionalIterator(final Direction direction){
    return new BlockAreaIterator(this, direction);
  }

  /** Creates a new BlockArea instance with the same Minimum and Maximum. */
  @Override
  public final BlockArea clone(){
    return new BlockArea(min_x, min_y, min_z, max_x, max_y, max_z);
  }

  public final BlockPos[] toArray(){
    final int length = getArea();
    final BlockPos[] array = new BlockPos[length];
    int i = 0;
    for(final BlockPos pos : this){
      array[i] = pos;
      i++;
    }
    return array;
  }

  public final List<BlockPos> toList(){
    final ArrayList<BlockPos> list = new ArrayList<>(getArea());
    for(final BlockPos pos : this){
      list.add(pos);
    }
    return list;
  }

  @Override
  public final String toString(){
    final StringBuilder s = new StringBuilder();
    s.append(this.getClass().getSimpleName());
    s.append("(Minimum");
    s.append(StringUtil.printPosition(min_x, min_y, min_z));
    s.append(", Maximum");
    s.append(StringUtil.printPosition(max_x, max_y, max_z));
    s.append(")");
    return s.toString();
  }

}
