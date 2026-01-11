package addsynth.core.util.math.random;

import java.util.Random;

public final class Weight {

  // 2 =  1/3   2/3
  // 3 =  1/10  3/10  6/10
  // 4 =  1/20  3/20  6/20  10/20
  // 5 =  1/35  3/35  6/35  10/35  15/35
  
  /** Builds a custom weighted array, then returns a random integer from that
   *  array. For instance, if you pass the numbers 3, 5, 0, and 2 as arguments, then
   *  this will construct the array <code>[0, 0, 0, 1, 1, 1, 1, 1, 3, 3]</code>.
   *  There are 3 0's, 5 1's, 0 2's, and 2 3's.
   * @param random
   * @param weights
   */
  public static final int getWeightedValue(final Random random, int ... weights){
    final int[] custom_weight_array = getCustomWeightArray(weights);
    return custom_weight_array[random.nextInt(custom_weight_array.length)];
  }
  
  /** Allows you to build a custom weighted array.
   *  For instance, if you pass the numbers 3, 5, 0, and 2 as arguments, then
   *  this will construct the array <code>[0, 0, 0, 1, 1, 1, 1, 1, 3, 3]</code>.
   *  There are 3 0's, 5 1's, 0 2's, and 2 3's.
   * @param weights
   */
  public static final int[] getCustomWeightArray(final int ... weights){
    if(weights == null){ throw new NullPointerException("Input for "+Weight.class.getSimpleName()+".getCustomWeightArray(int ... weights) is null.");}

    final int length = weights.length;
    if(length == 0){ throw new IllegalArgumentException("Number of weight values in "+Weight.class.getSimpleName()+".getCustomWeightArray() is 0.");}
    
    int i;
    int total_length = 0;
    for(i = 0; i < length; i++){
      total_length += weights[i];
    }
    final int[] final_array = new int[total_length];
    int k;
    int z = 0;
    int limit;
    
    for(k = 0; k < length; k++){
      limit = weights[k];
      for(i = 0; i < limit; i++){
        final_array[z] = k;
        z += 1;
      }
    }
    
    return final_array;
  }
  
  /** This creates a linearly distributed bucket list, so that you have a higher chance of
   *  getting a low value rather than a high value. For example, say you pass the number 5
   *  as the argument, this function will then create the bucket list:<br />
   *  <code>[0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 4]</code><br />
   *  The function will then randomly pick one of these numbers and return it. This means
   *  you have a 33% chance of getting 0, a 27% chance of getting 1, a 20% of getting 2,
   *  a 13% chance of getting 3, and a 7% chance of getting 4.
   * @param random
   * @param highest_value
   */
  public static final int getWeightedValue(final Random random, final int highest_value){
    final int[] linear_weight_array = getLinearWeightArray(highest_value);
    return linear_weight_array[random.nextInt(linear_weight_array.length)];
  }
  
  /** This creates a linearly distributed bucket list, so that you have a higher chance of
   *  getting a low value rather than a high value. For example, say you pass the number 5
   *  as the argument, this function will then create the bucket list:<br />
   *  <code>[0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 4]</code><br />
   *  This means you have a 33% chance of getting 0, a 27% chance of getting 1, a 20% of
   *  getting 2, a 13% chance of getting 3, and a 7% chance of getting 4.
   * @param highest_value
   */
  public static final int[] getLinearWeightArray(final int highest_value){
    if(highest_value <= 0){
      throw new IllegalArgumentException("Only values > 0 are allowed.");
    }
    int j;
    int total_buckets = 0;
    for(j = highest_value; j > 0; j--){
      total_buckets += j;
    }
    final int[] bucket = new int[total_buckets];
    
    int i = 0;
    int limit = highest_value;
    int z;
    for(; j < highest_value; j++){
      for(z = 0; z < limit; z++){
        bucket[i] = j;
        i += 1;
      }
      limit -= 1;
    }
    return bucket;
  }
  
}
