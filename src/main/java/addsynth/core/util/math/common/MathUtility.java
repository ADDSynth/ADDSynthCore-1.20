package addsynth.core.util.math.common;

import javax.annotation.Nonnegative;
import addsynth.core.ADDSynthCore;

/** For math functions that use {@link net.minecraft.core.BlockPos BlockPos},
 *  use {@link addsynth.core.util.math.block.BlockMath BlockMath} instead.
 *  @author ADDSynth
 */
public final class MathUtility {

  /** This divides a whole number as evenly as possible, with higher numbers closer towards the beginning.
   *  For example, dividing 18 into 5 equal parts will produce [4, 4, 4, 3, 3].
   * @param number
   * @param count
   */
  public static final int[] divide_evenly(final int number, @Nonnegative final int count){
    if(count == 0){
      ADDSynthCore.log.error("Error in "+MathUtility.class.getSimpleName()+".divide_evenly(). Cannot divide by 0!", new ArithmeticException());
      return new int[count];
    }
    if(number == 0){
      return new int[count];
    }
    if(count == 1){
      return new int[] {number};
    }
    final int[] list = new int[count];
    final int equal = number / count; // integer division drops the decimal remainder, which is what we want.
    final int sign = number >= 0 ? 1 : -1;
    int remainder = Math.abs(number % count);
    int i;
    for(i = 0; i < count; i++){
      if(remainder > 0){
        list[i] = equal + sign;
        remainder -= 1;
      }
      else{
        list[i] = equal;
      }
    }
    return list;
  }

  /** If you want to extract a number as evenly as possible from a random list of numbers, this function produces
   *  another list of numbers which you can use to extract from the original list. And it uses a Round-Robin
   *  method, so bigger numbers will end up towards the beginning. It's kind of hard to explain, so I'll give
   *  you an example: If you're trying to extract 30 from the list [8, 15, 4, 1, 15], this function will produce
   *  the list [8, 9, 4, 1, 8], which, when extracted from the original list would produce [0, 6, 0, 0, 7].
   * @param number
   * @param list
   */
  public static final int[] divide_evenly(@Nonnegative int number, int[] list){
    final int length = list.length;
    final int[] final_list = new int[length]; // already initialized to all 0

    if(length == 0){
      ADDSynthCore.log.error(MathUtility.class.getSimpleName()+".divide_evenly() cannot use an empty integer array.", new IllegalArgumentException());
      return final_list;
    }
    if(number < 0){
      ADDSynthCore.log.error("Cannot use "+MathUtility.class.getSimpleName()+".divide_evenly() to divide a negative number!", new IllegalArgumentException());
      return final_list;
    }
    if(number == 0){
      return final_list;
    }
    if(length == 1){
      return new int[]{Math.min(number, Math.max(list[0], 0))};
    }

    int i;
    int count;
    int min_number;

    do{
      // reset
      count = 0;
      
      // get first index, count, and min number
      min_number = CommonMath.getMax(list);
      for(i = 0; i < length; i++){
        if(list[i] > 0){
          count += 1;
          if(list[i] < min_number){
            min_number = list[i];
          }
        }
      }
      
      if(count > 0){
        // transfer
        min_number = Math.min(min_number, number / count);
        if(min_number == 0){
          break;
        }
        for(i = 0; i < length; i++){
          if(list[i] > 0){
            list[i] -= min_number;
            final_list[i] += min_number;
            number -= min_number;
          }
        }
      }
    }
    while(number >= count && count > 0);
    
    if(count == 0){
      return final_list;
    }

    // transfer remaining
    for(i = 0; i < length && number > 0; i++){
      if(list[i] > 0){
        final_list[i] += 1;
        number -= 1;
      }
    }
    return final_list;
  }

  /** If you want to extract a number as evenly as possible from a random list of numbers, this function produces
   *  another list of numbers which you can use to extract from the original list. And it uses a Round-Robin
   *  method, so bigger numbers will end up towards the beginning. It's kind of hard to explain, so I'll give
   *  you an example: If you're trying to extract 30 from the list [8, 15, 4, 1, 15], this function will produce
   *  the list [8, 9, 4, 1, 8], which, when extracted from the original list would produce [0, 6, 0, 0, 7].
   * @param number
   * @param list
   */
  public static final long[] divide_evenly(@Nonnegative long number, long[] list){
    final int length = list.length;
    final long[] final_list = new long[length]; // already initialized to all 0

    if(length == 0){
      ADDSynthCore.log.error(MathUtility.class.getSimpleName()+".divide_evenly() cannot use an empty integer array.", new IllegalArgumentException());
      return final_list;
    }
    if(number < 0){
      ADDSynthCore.log.error("Cannot use "+MathUtility.class.getSimpleName()+".divide_evenly() to divide a negative number!", new IllegalArgumentException());
      return final_list;
    }
    if(number == 0){
      return final_list;
    }
    if(length == 1){
      return new long[]{Math.min(number, Math.max(list[0], 0))};
    }

    int i;
    int count;
    long min_number;

    do{
      // reset
      count = 0;
      
      // get first index, count, and min number
      min_number = CommonMath.getMax(list);
      for(i = 0; i < length; i++){
        if(list[i] > 0){
          count += 1;
          if(list[i] < min_number){
            min_number = list[i];
          }
        }
      }
      
      if(count > 0){
        // transfer
        min_number = Math.min(min_number, number / count);
        if(min_number == 0){
          break;
        }
        for(i = 0; i < length; i++){
          if(list[i] > 0){
            list[i] -= min_number;
            final_list[i] += min_number;
            number -= min_number;
          }
        }
      }
    }
    while(number >= count && count > 0);
    
    if(count == 0){
      return final_list;
    }

    // transfer remaining
    for(i = 0; i < length && number > 0; i++){
      if(list[i] > 0){
        final_list[i] += 1;
        number -= 1;
      }
    }
    return final_list;
  }

  public static final double get_distance(double x1, double z1, double x2, double z2){
    return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((z2 - z1) * (z2 - z1)));
  }

  public static final double get_distance(double x1, double y1, double z1, double x2, double y2, double z2){
    return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)) + ((z2 - z1) * (z2 - z1)));
  }

  public static final boolean isWithin(double x1, double z1, double x2, double z2, double distance){
    return get_distance(x1, z1, x2, z2) <= distance;
  }

  public static final boolean isWithin(double x1, double y1, double z1, double x2, double y2, double z2, double distance){
    return get_distance(x1, y1, z1, x2, y2, z2) <= distance;
  }

}
