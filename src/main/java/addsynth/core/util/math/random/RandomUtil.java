package addsynth.core.util.math.random;

import java.util.Random;

public final class RandomUtil {

  /** Returns a random integer between <code>minimum</code> (inclusive) and <code>maximum</code>
   *  (inclusive). This function won't error if you get the minimum and maximum mixed up,
   *  or if they're the same value.
   * @param minimum
   * @param maximum
   */
  public static final int RandomRange(final int minimum, final int maximum){
    final int min = Math.min(minimum, maximum);
    final int max = Math.max(minimum, maximum);
    final int diff = max - min;
    if(diff == 0){ return min; }
    final Random random = new Random();
    return min + random.nextInt(diff + 1);
  }

  public static final int choose(final Random random, final int ... list){
    if(list.length == 0){
      throw new IllegalArgumentException(RandomUtil.class.getSimpleName()+".choose() requires a list of at least 1 integer!");
    }
    return list[random.nextInt(list.length)];
  }

  public static final <T> T choose(final Random random, final T[] list){
    if(list.length == 0){
      throw new IllegalArgumentException(RandomUtil.class.getSimpleName()+".choose() requires a list of 1 or more objects!");
    }
    return list[random.nextInt(list.length)];
  }

  public static final int[] get_random_list(int max_number){
    return get_random_list(new Random(), max_number, max_number);
  }

  public static final int[] get_random_list(int max_number, int max_needed){
    return get_random_list(new Random(), max_number, max_needed);
  }

  public static final int[] get_random_list(Random random, int max_number){
    return get_random_list(random, max_number, max_number);
  }

  public static final int[] get_random_list(Random random, int max_number, int max_needed){
    final int[] list = new int[max_needed];
    final boolean[] used = new boolean[max_number];
    int i;
    int j;
    int count;
    int random_number;
    int max_count = max_number;
    for(i = 0; i < max_needed; i++){
      random_number = random.nextInt(max_count);
      count = 0;
      for(j = 0; j < max_number; j++){
        if(!used[j]){
          if(count == random_number){
            break;
          }
          count++;
        }
      }
      list[i] = j;
      used[j] = true;
      max_count--;
    }
    return list;
  }

  /** Returns a random value between -1.0 and 1.0 (so the total range is 2), with most values being picked
   *  closer to 0. The probability graph is in a shape of a triangle. It's not quite a normal distribution
   *  graph, but it's pretty similar. I would normally get this by adding 2 random values then dividing by 2,
   *  which would give a value between 0 and 1, but subtracting 2 values is computationally faster. Divide
   *  or Multiply the result to expand or shrink, and add or subtract to shift the center.
   * @param random
   * @return
   */
  public static final float getCentralDistribution(final Random random){
    return random.nextFloat() - random.nextFloat();
  }

  /** Returns a random value between -1.0 and 1.0 (so the total range is 2), with most values being picked
   *  closer to 0. The probability graph is in a shape of a triangle. It's not quite a normal distribution
   *  graph, but it's pretty similar. I would normally get this by adding 2 random values then dividing by 2,
   *  which would give a value between 0 and 1, but subtracting 2 values is computationally faster. Divide
   *  or Multiply the result to expand or shrink, and add or subtract to shift the center.
   * @param random
   * @param scale Use default value of 1 for no scaling, Values between 0 and 1 to shrink, and higher values
   *        to scale up. Remember the default range is between -1.0 and 1.0, so a total range of 2.
   * @param shift Positive values to shift right, and negative values to shift left.
   * @return
   */
  public static final float getCentralDistribution(final Random random, final float scale, final float shift){
    return ((random.nextFloat() - random.nextFloat()) * scale) + shift;
  }

}
