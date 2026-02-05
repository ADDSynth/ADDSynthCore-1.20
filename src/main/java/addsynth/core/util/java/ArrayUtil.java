package addsynth.core.util.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import addsynth.core.ADDSynthCore;

public final class ArrayUtil {

  public static final boolean isInsideBounds(final int index, final int max_length){
    return index >= 0 && index < max_length;
  }

  public static final boolean isInsideBounds(final int index, @Nonnull final Object[] array){
    return index >= 0 && index < array.length;
  }

  /** Used to check if the array contains the value. */
  public static final <T> boolean valueExists(final T[] array, final T value){
    for(final T i : array){
      if(Objects.equals(i, value)){
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the value at index in the array. Returns the default value if any error occured.
   * @param array
   * @param index
   * @param default_value
   */
  // Energy Suspension Bridge in Overpowered Technology uses this, when loading enum index values
  public static final <T> T getArrayValue(final T[] array, final int index, final T default_value){
    if(array == null){
      ADDSynthCore.log.error("Input array for "+ArrayUtil.class.getName()+".getArrayValue() is null!", new NullPointerException());
      return default_value;
    }
    if(isInsideBounds(index, array.length)){
      return array[index];
    }
    print_array_index_out_of_bounds_error(array, index);
    return default_value;
  }

  /** Prints extremely detailed information to the log, and also prints the stacktrace. */
  public static final void print_array_index_out_of_bounds_error(@Nonnull final Object[] array, final int index){
    final int length = array.length;
    final StringBuilder s = new StringBuilder();
    s.append("Invalid index ");
    s.append(index);
    s.append(" for array ");
    s.append(array.getClass().getComponentType().getSimpleName());
    s.append('[');
    s.append(length);
    s.append("]. ");
    if(length == 0){
      s.append("The array is empty.");
    }
    else{
      if(length == 1){
        s.append("Only index 0 is valid.");
      }
      else{
        s.append("Only indexes 0 to ");
        s.append(length - 1);
        s.append(" are valid.");
      }
    }
    ADDSynthCore.log.error(s.toString(), new ArrayIndexOutOfBoundsException());
  }

  /** Gets total length of all arrays combined. */
  public static final int get_length_of_arrays(final Object[] ... arrays){
    int total_length = 0;
    for(Object[] array : arrays){
      if(array != null){
        total_length += array.length;
      }
    }
    return total_length;
  }

  public static final long[] combine_arrays(@Nonnull final long[] a, @Nonnull final long[] b){
    final long[] result = new long[a.length + b.length];
    System.arraycopy(a, 0, result, 0, a.length);
    System.arraycopy(b, 0, result, a.length, b.length);
    return result;
  }

  // https://stackoverflow.com/questions/12462079/potential-heap-pollution-via-varargs-parameter
  // https://softwareengineering.stackexchange.com/questions/155994/java-heap-pollution#
  // https://docs.oracle.com/javase/tutorial/java/generics/nonReifiableVarargsType.html
  // https://docs.oracle.com/javase/8/docs/api/java/lang/SafeVarargs.html
  // THIS WORKS PERFECTLY!!! NEVER CHANGE!!!
  @SafeVarargs
  public static final <T> T[] combine_arrays(@Nonnull final T[] first_array, final T[] ... additional_arrays){
    int i = first_array.length;
    final T[] final_array = Arrays.copyOf(first_array, i + get_length_of_arrays(additional_arrays)); // creates a new array with the total size.
    for(T[] array : additional_arrays){
      if(array == null){
        ADDSynthCore.log.error("Encountered a null array in "+ArrayUtil.class.getSimpleName()+".combine_arrays() function.", new NullPointerException());
        continue;
      }
      for(T object : array){
        final_array[i] = object;
        i++;
      }
    }
    if(i != final_array.length){
      // in case we encountered a null array, only return an array containing the elements we actually copied.
      return Arrays.copyOfRange(final_array, 0, i);
    }
    return final_array;
  }

  @SafeVarargs
  public static final <T> T[] combine_collections(@Nonnull final IntFunction<T[]> new_array_supplier, final Collection<T> ... lists){
    // check for null
    if(lists == null){
      return new_array_supplier.apply(0);
    }
    // get length of all lists
    int final_length = 0;
    for(final Collection<T> list : lists){
      if(list != null){
        final_length += list.size();
      }
    }
    // create final array
    final T[] final_array = new_array_supplier.apply(final_length);
    int i = 0;
    for(final Collection<T> list : lists){
      if(list != null){
        for(T a : list){
          final_array[i] = a;
          i++;
        }
      }
    }
    return final_array;
  }

  /** This checks the two arrays and returns true if they are different.
   *  @deprecated Use !Arrays.equals(Object[], Object[]) instead. */
  // REMOVE in 2031
  @Deprecated
  public static final <T> boolean arrayChanged(final T[] cached_array, final T[] new_array){
    return !Arrays.equals(cached_array, new_array);
  }

  /** This checks the array against the supplied {@link ArrayList} to see if the array needs to be updated. */
  @Deprecated
  public static final <T> boolean arrayChanged(final T[] cached_array, final ArrayList<T> list){
    final int length = cached_array.length;
    if(list.size() != length){
      return true;
    }
    for(int i = 0; i < length; i++){
      if(!Objects.equals(cached_array[i], list.get(i))){
        return true;
      }
    }
    return false;
  }

  /** This checks the data in the new_array against the data in the cached array and updates the
   *  cached_array accordingly. Returns true if an update occured.
   *  @deprecated Use Arrays.equals(a[], b[]) ? a : b instead. */
  // REMOVE in 2031
  @Deprecated
  public static final <T> T[] updateArray(final T[] cached_array, final T[] new_array){
    return Arrays.equals(cached_array, new_array) ? cached_array : new_array;
  }

  /** This checks the cached_array against the data stored in the supplied list, and updates
   *  it if there were any changes. Returns true if an update occured.
   */
  // REMOVE in 2031
  @Deprecated
  @SuppressWarnings("unchecked")
  public static final <T> T[] updateArray(final T[] cached_array, final ArrayList<T> list){
    return updateArray(cached_array, (T[])list.toArray());
  }

  /** Syncs the data in the cached_list with that in the supplied list. Items are
   *  first removed from the cached list if they do not exist in the new List. Any
   *  new items are appended to the cached list. Returns true if an update occured. */
  public static final <T> boolean syncList(final Collection<T> cached_list, final Collection<T> new_list){
    boolean changed = cached_list.removeIf((T item) -> {return !new_list.contains(item);});
    for(final T item : new_list){
      if(cached_list.contains(item) == false){
        cached_list.add(item);
        changed = true;
      }
    }
    return changed;
  }

  /** Syncs the data in the cached_list with that in the supplied list. The cached list is
   *  resized, and each element is copied over one by one. This ensures the order matches
   *  that of the new list. Returns true if an update occured. */
  public static final <T> boolean syncListExactly(final List<T> cached_list, final List<T> new_list){
    final int length = new_list.size();
    if(cached_list.size() != length){
      cached_list.clear();
      cached_list.addAll(new_list);
      return true;
    }
    boolean changed = false;
    T a;
    for(int i = 0; i < length; i++){
      a = new_list.get(i);
      if(!Objects.equals(cached_list.get(i), a)){
        cached_list.set(i, a);
        changed = true;
      }
    }
    return changed;
  }

}
