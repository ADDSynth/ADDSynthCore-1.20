package addsynth.core.gui.widgets.scrollbar;

import net.minecraft.util.Mth;

public final class ScrollbarSystem {

  /** Max Position scrollbar will be at if at last index position. */
  private double max_y;
  /** Current scrollbar index. */
  private int index;
  /** Maximum scrollbar positions. */
  private int max_positions;
  /** The size of the scrollbar positions. */
  private double step;
  private int[] index_positions;

  /** Generates an array of stop positions for the scrollbar.
   * @param min_y Y Position of Scrollbar
   * @param scrollbar_height Height of inner scrollbar
   * @param total_height Total Height of Scrollbar
   * @param visible_items Number of Items Visible in menu
   * @param list_length Total Number of Items in Scrollbar
   */
  public final void calculatePositions(double min_y, int scrollbar_height, int total_height, int visible_items, int list_length){
    max_positions = Math.max(0, list_length - visible_items);

    // handle a position of 1 to prevent divide by zero
    if(max_positions < 1){
      index_positions = new int[]{(int)min_y};
      return;
    }
    max_y = min_y + total_height - scrollbar_height;
    index_positions = new int[max_positions+1];
    step = (max_y - min_y) / max_positions;
    int i;
    for(i = 0; i <= max_positions; i++){
      index_positions[i] = (int)Math.round(min_y + (step*i));
    }
    
    index = clampIndex(index);
  }

  private final int clampIndex(int index){
    return Mth.clamp(index, 0, max_positions);
  }

  public final int getIndex(){
    return index;
  }

  /** Sets the Y position of the Scrollbar. */
  public final int getPosition(){
    return index_positions[index];
  }

  /** Sets the Scrollbar Index. Returns true if index changed. */
  public final boolean setIndex(int new_index){
    new_index = clampIndex(new_index);
    if(new_index != index){
      index = new_index;
      return true;
    }
    return false;
  }

  public final boolean setIndexFromPosition(double position){
    if(max_positions > 0){
      return setIndex((int)Math.round((position-index_positions[0]) / step)); // only works because the very first thing we do is clamp the value.
    }
    return false;
  }

}
