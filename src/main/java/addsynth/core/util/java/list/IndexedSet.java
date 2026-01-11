package addsynth.core.util.java.list;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;

/** An IndexedSet is an object that acts as both a Set and a List, so you can be assured
 *  that the items contained in the IndexedSet are both unique (elements can only be added
 *  if they are not already in the IndexedSet), and they can be accessed by index. The
 *  {@link #contains} method is O(1) constant time because we call the Set's contain method.
 *  The add operations are also constant time, because they check if the item can be added
 *  to the Set first, however, many remove and index functions must navigate through the
 *  whole list, and that means they are O(n) linear time.
 * @param <T>
 */
public class IndexedSet<T> implements Iterable<T> /*, Collection<T>*/ {

  private final ArrayList<T> list;
  private final HashSet<T> set;

  // ------------------------------------------------------------
  // Constructors
  // ------------------------------------------------------------

  public IndexedSet(){
    list = new ArrayList<>();
    set = new HashSet<>();
  }

  public IndexedSet(final int initial_capacity){
    list = new ArrayList<>(initial_capacity);
    set = new HashSet<>(initial_capacity);
  }

  public IndexedSet(final Collection<? extends T> collection){
    final int size = collection.size();
    list = new ArrayList<>(size);
    set = new HashSet<>(size);
    addAll(collection);
  }

  // ------------------------------------------------------------
  // Basic operations
  // ------------------------------------------------------------

  public boolean contains(T value){
    return set.contains(value);
  }

  public int getIndexOf(T value){
    return list.indexOf(value);
  }

  public T get(int index){
    return list.get(index);
  }

  public void clear(){
    list.clear();
    set.clear();
  }

  public int size() {
      return list.size();
  }

  public boolean isEmpty(){
    return list.isEmpty();
  }

  public boolean validIndex(int index){
    return index >= 0 && index < list.size();
  }

  // ------------------------------------------------------------
  // Add operations
  // ------------------------------------------------------------

  public boolean add(T value){
    if(set.add(value)){
      list.add(value);
      return true;
    }
    return false;
  }

  public boolean set(int index, T value){
    if(index >= size()){
      return add(value);
    }
    final T existing = list.get(index);
    if(!Objects.equals(value, existing)){
      if(set.add(value)){
        set.remove(existing);
        list.set(index, value);
        return true;
      }
    }
    return false;
  }

  public boolean insert(T value, int index){
    if(set.add(value)){
      list.add(index, value);
      return true;
    }
    return false;
  }

  public boolean addAll(Collection<? extends T> values){
    boolean changed = false;
    for(T value : values){
      if(set.add(value)){
        list.add(value);
        changed = true;
      }
    }
    return changed;
  }

  // ------------------------------------------------------------
  // Remove operations
  // ------------------------------------------------------------

  public boolean remove(int index){
    if(validIndex(index)){
      final T removed = list.remove(index);
      set.remove(removed);
      return true;
    }
    return false;
  }

  @Nullable
  public T extract(int index){
    if(validIndex(index)){
      final T removed = list.remove(index);
      set.remove(removed);
      return removed;
    }
    return null;
  }

  public boolean remove(T value){
    if(set.remove(value)){
      list.remove(value);
      return true;
    }
    return false;
  }

  public boolean removeIf(Predicate<T> predicate){
    boolean changed = false;
    T value;
    // Iterate backwards so index removal is safe
    for(int i = list.size() - 1; i >= 0; i--){
      value = list.get(i);
      if(predicate.test(value)){
        list.remove(i);
        set.remove(value);
        changed = true;
      }
    }
    return changed;
  }

  public boolean removeAll(Collection<? extends T> collection){
    boolean changed = false;
    for(T o : collection){
      if(remove(o)){
        changed = true;
      }
    }
    return changed;
  }

  // ------------------------------------------------------------
  // Conversions
  // ------------------------------------------------------------

  public <U> U[] toArray(U[] a){
    return list.toArray(a);
  }

  public List<T> toList(){
    return new ArrayList<>(list);
  }

  public Set<T> toSet(){
    return new HashSet<>(set);
  }

  // ------------------------------------------------------------
  // Utility operations
  // ------------------------------------------------------------

  @Override
  public void forEach(Consumer<? super T> action){
    list.forEach(action);
  }

  @Override
  public Iterator<T> iterator(){
    return list.iterator();
  }

  public void sort(){
    list.sort(null);
  }

  public void sort(Comparator<? super T> comparator){
    list.sort(comparator);
  }

  @Override
  public int hashCode(){
    // Order matters
    return list.hashCode();
  }

  @Override
  public String toString(){
    return list.toString();
  }

}
