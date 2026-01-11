package addsynth.core.util.math.number;

import addsynth.core.ADDSynthCore;

public final class BinaryEncoder {

  /* I was going to do this version, but it needs extra logic to prevent the user from inputting duplicates.
  public static final int encode(int ... values){
    if(values.length == 0) return 0;
    if(values.length == 1) return values[0];
    int number = values[0];
    int i;
    int length = values.length;
    for(i = 0; i < length; i++){
      number += 1 << values[i];
    }
    return number;
  }
  */

  public static final byte encodeByte(boolean ... values){
    return (byte)encodeInternal(values, 8);
  }

  /** Pass in true/false values or a boolean array to encode the values as bits inside an integer. */
  public static final int encode(boolean ... values){
    return encodeInternal(values, 32);
  }
    
  private static final int encodeInternal(boolean[] values, int max){
    if(values.length == 0) return 0;
    if(values.length == 1) return values[0] ? 1 : 0;
    int number = 0;
    int i;
    int length = values.length;
    if(length > max){
      ADDSynthCore.log.error("Input boolean array for "+BinaryEncoder.class.getName()+".encode() method exceeds the maximum length of "+max+". Not all values will be encoded!", new IllegalArgumentException());
      length = max;
    }
    for(i = 0; i < length; i++){
      if(values[i]){
        number |= (1 << i);
      }
    }
    return number;
  }

  /** Returns the bit value at the specified index in the number. */
  public static final boolean decode(int number, int index){
    if(index < 32){
      return (number & (1 << index)) > 0;
    }
    return false;
  }

  /** Returns a boolean array of the given size decoded from the specified number. */
  public static final boolean[] getArray(int number, int size){
    final int length = Math.min(size, 32);
    final boolean[] array = new boolean[length];
    for(int i = 0; i < length; i++){
      array[i] = (number & (1 << i)) > 0;
    }
    return array;
  }

}
