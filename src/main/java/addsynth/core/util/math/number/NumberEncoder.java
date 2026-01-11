package addsynth.core.util.math.number;

/** When an object has a finite range of values, and you want to give each valid arrangement a specific
 *  ID number, for example, if ANY wire could exist in ANY 4 directions, then each wire could be assigned
 *  their own ID in powers of 2 (1 = UP, 2 = RIGHT, 4 = DOWN, 8 = LEFT), then we can add those numbers
 *  to get any combination of wires. Using this as an example, the value of 3 gives an arrangement that
 *  includes the UP and RIGHT wire, a value of 11 gives the LEFT, RIGHT, and UP wires, and a value of 14
 *  has the LEFT, DOWN, and RIGHT wires.
 */
public class NumberEncoder {

  private final int states;
  private final int value;

  /**
   * @param number_of_states The number of possible states in each node. For on/off this would be 2.
   * @param index The index of the node. Start at 0.
   */
  public NumberEncoder(int number_of_states, int index){
    this.states = number_of_states;
    value = (int)Math.pow(number_of_states, index);
  }

  /** After encoding the node's value, add it to the other nodes to get the final ID. */
  public final int encode(int node_value){
    return node_value * value;
  }

  /** After encoding the node's value, add it to the other nodes to get the final ID. */
  public final int encode(boolean node_exists){
    return node_exists ? value : 0;
  }

  /** Returns the value of this node. */
  public final int decode(int number){
    return (number / value) % states;
  }

  public final boolean exists(int number){
    return decode(number) > 0;
  }

}
