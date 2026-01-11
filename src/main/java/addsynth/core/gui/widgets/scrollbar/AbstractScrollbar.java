package addsynth.core.gui.widgets.scrollbar;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import addsynth.core.ADDSynthCore;
import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.widgets.WidgetUtil;
import addsynth.core.gui.widgets.rect.Box;
import addsynth.core.gui.widgets.rect.Dimensions;
import addsynth.core.util.java.ArrayUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

/** <p>A Scrollbar is a widget that goes beside a list of values which the player can
 *     move up or down to scroll a list of values. It automatically adjusts its position
 *     and size based on how many list items there are.
 *  <p>First define an array of {@link AbstractListEntry}s and place them on your gui, and add them as buttons.
 *     Then create the Scrollbar. Place it in the gui where you want, give it your full array of values,
 *     and your array of {@link AbstractListEntry}s.
 *  <p>When any {@link AbstractListEntry} is clicked, that value will be selected in the list. Only one value
 *     can be selected at any time.
 *  <p>You can assign a {@link BiConsumer} as a responder, which will be called whenever a list item
 *     is selected. The first argument is the value. The second is the index in the array of values.
 *  <p>You can manually get the selected value by called {@link #getSelected}, or the index by calling
 *     {@link #getSelectedIndex}.
 *  <p>To manually set which entry is selected call {@link #setSelected(int)} or {@link #init(int)}.
 *     Absolutely be careful you don't have Scrollbar responders call each other otherwise that will
 *     create an infinite loop!
 *  <p>Call {@link #unSelect} or {@link #setSelected(int)} with any negative value to unselect.
 * @author ADDSynth
 * @param <E> The type of item in this list, which is usually {@link Component} or {@link ItemStack}.
 * @param <L> The associated ListEntry type for the item type in this list.
 */
public abstract class AbstractScrollbar<E, L extends AbstractListEntry<E>> extends AbstractWidget {

  // texture constants
  private static final int texture_scale_width  = 72;
  private static final int texture_scale_height = 512;
  private static final int max_scrollbar_height = 464; // 512 - (24*2)
  private static final Dimensions scrollbar_texture            = new Dimensions(GuiReference.scrollbar,  0, 24, 24, max_scrollbar_height, 2, texture_scale_width, texture_scale_height);
  private static final Dimensions scrollbar_background_texture = new Dimensions(GuiReference.scrollbar, 24, 24, 24, max_scrollbar_height, 2, texture_scale_width, texture_scale_height);
  private static final Dimensions scrollbar_center_texture     = new Dimensions(GuiReference.scrollbar, 48, 24, 24, max_scrollbar_height, 2, texture_scale_width, texture_scale_height);

  // gui dimensions
  /** Scrollbar width on the gui. */
  private static final int scrollbar_gui_width = 12;
  private static final int center_gui_height = 2;

  // other constants
  /** For every scrollbar height that is a multiple of this, we add a center section. */
  private static final int center_ratio = 16;

  /** Main scrollbar box. */
  private final Box scrollbar = new Box();
  
  // calculations
  private int number_of_center_sections;
  private int scrollbar_center_height;
  
  private final ScrollbarSystem scrollbar_system = new ScrollbarSystem();
  private int mouse_distance;

  /** The List Entries connected to this Scrollbar. */
  private final L[] list_items;
  /** Number of visible List Entries. */
  private final int visible_elements;
  /** Full list of values. NEVER CHANGE DIRECTLY. Call {@link #updateScrollbar} to set. */
  protected E[] values;
  /** Number of values in the full list. Use this instead of <code>values.length</code>. */
  protected int list_length;
  /** The selected index. */
  private int selected = -1;

  /** This is passed the new selected Index every time it is changed,
   *  even if it is changed to an invalid value. */
  private BiConsumer<E, Integer> onSelected;
  /** This value is returned if the scrollbar currently has an invalid selected index. */
  private final E invalid_value;

  public AbstractScrollbar(final int x, final int y, final int height, final L[] list_items, final E[] values, final E default_value){
    super(x, y, scrollbar_gui_width, height, Component.empty());
    scrollbar.setWidth(scrollbar_gui_width);
    if(height > max_scrollbar_height - 8){
      ADDSynthCore.log.error("Requested Scrollbar height is bigger than Max Scrollbar height!");
    }
    for(final L entry : list_items){
      entry.setScrollbar(this);
    }
    this.list_items = list_items;
    visible_elements = list_items.length;
    this.invalid_value = default_value;
    updateScrollbar(values);
  }
  
  /** Call this to assign the full list of values. The scrollbar will automatically update its displayed list and size. */
  public final void updateScrollbar(final E[] values){
    this.values = values != null ? values : createEmptyValueArray();
    list_length = this.values.length;
    
    recalculateScrollbar();
    updateList();
  }
  
  @Nonnull
  protected abstract E[] createEmptyValueArray();
  
  private final void recalculateScrollbar(){
    try{
      // recalculate everything for now. it doesn't hurt.
  
      // scrollbar
      if(list_length > visible_elements){
        final double ratio = (double)visible_elements / list_length;
        scrollbar.setHeight(Math.max((int)Math.round(height * ratio), 6));
      }
      else{
        scrollbar.setHeight(this.height);
      }
      // update position_y based on new index value
      scrollbar_system.calculatePositions(getY(), scrollbar.getHeight(), height, visible_elements, list_length);
      scrollbar.setY(scrollbar_system.getPosition());

      // calculate center
      number_of_center_sections = (int)Math.round((double)scrollbar.getHeight() / center_ratio);
      if(number_of_center_sections == 1){
        number_of_center_sections = 0;
      }
      scrollbar_center_height = number_of_center_sections * center_gui_height;
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  @Override
  public void renderWidget(GuiGraphics graphics, int p_render_1_, int p_render_2_, float p_render_3_){
    WidgetUtil.common_button_render_setup(GuiReference.scrollbar);
    final int x = getX();

    // Background
    scrollbar_background_texture.drawVerticalSplit(graphics, x, getY(), scrollbar_gui_width, height);

    // Scrollbar
    scrollbar_texture.drawVerticalSplit(graphics, x, scrollbar.getY(), scrollbar_gui_width, scrollbar.getHeight());
    scrollbar_center_texture.drawSized(graphics, x, scrollbar.getCenterY() - scrollbar_center_height/2, scrollbar_gui_width, scrollbar_center_height);
  }

  @Override
  public final void onClick(double mouse_x, double mouse_y){
    if(mouse_y < scrollbar.getTop() || mouse_y >= scrollbar.getBottom()){
      scrollbar_system.setIndexFromPosition(mouse_y - scrollbar.getHalfHeight());
      scrollbar.setY(scrollbar_system.getPosition());
      updateList();
    }
    mouse_distance = (int)mouse_y - scrollbar.getY();
  }

  @Override
  protected final void onDrag(double mouse_x, double mouse_y, double screen_x, double screen_y){
    scrollbar.setY(Mth.clamp((int)mouse_y - mouse_distance, getY(), getY() + height - scrollbar.getHeight()));
    if(scrollbar_system.setIndexFromPosition(scrollbar.getY())){
      updateList();
    }
  }

  /** Adjusts the scrollbar's position based on the provided index position,
   *  and also updates the displayed list. */
  private final void setScrollbarPosition(final int scrollbar_position_index){
    if(scrollbar_system.setIndex(scrollbar_position_index)){
      scrollbar.setY(scrollbar_system.getPosition());
      updateList();
    }
  }

  /** Used to update the displayed list entries. Must be called every time the scrollbar's position changes. */
  protected final void updateList(){
    int i;
    int index = scrollbar_system.getIndex();
    int id;
    for(i = 0; i < visible_elements; i++){
      id = index + i;
      if(id < list_length){
        updateListItem(list_items[i], id, values[id]);
        list_items[i].setSelected(selected);
      }
      else{
        list_items[i].setNull();
      }
    }
  }

  // Rather than setting a bunch of variables as protected, I decided to do this.
  // I'd only have to override this. Thought this was best at the time.
  protected void updateListItem(final L list_item, final int id, final E value){
    list_item.set(id, value);
  }

  @Override
  public final void onRelease(double p_onRelease_1_, double p_onRelease_3_){
    scrollbar.setY(scrollbar_system.getPosition());
  }

  public final void setResponder(final BiConsumer<E, Integer> responder){
    this.onSelected = responder;
  }

  /** Shortcut to unselect the Index. Does not move scrollbar. */
  public final void unSelect(){
    setSelectedInternal(-1);
  }

  /** Unselects the list item, and moves scrollbar to the top index. */
  public final void reset(){
    setSelectedInternal(-1);
    setScrollbarPosition(0);
  }
  
  /** Primary function to set the selected Index. Can also Unselect the index.
   *  Calls the {@link #onSelected} BiConsumer, if available, to respond to changes.
   * @param list_entry_index Set to any index outside of range to unselect
   */
  public final void setSelected(int list_entry_index){
    setSelectedInternal(list_entry_index);
    if(hasValidSelection() && !selectedIsVisible()){
      scroll_to_value();
    }
  }

  private final void setSelectedInternal(int list_entry_index){
    if(list_entry_index != selected){
      selected = list_entry_index;
      for(final L e : list_items){
        e.setSelected(selected);
      }
      
      if(onSelected != null){
        onSelected.accept(getSelected(), selected);
      }
    }
  }

  /** Sets the value of the scrollbar and scrolls so it's in view.
   *  Unselects the index if we didn't find your value. */
  public final void init(int list_entry_index){
    setSelectedInternal(list_entry_index);
    scroll_to_value();
  }

  /** Sets the value of the scrollbar and scrolls so it's in view.
   *  Unselects the index if we didn't find your value. */
  public final void init(final E value){
    init(find(value));
  }

  /** Goes through all the values in this scrollbar and selects
   *  the first value that passes your Predicate test.  */
  public final void init(final Predicate<E> predicate){
    init(find(predicate));
  }

  /** Searches through the list of values and returns the index if we find the value
   *  you're looking for. Returns -1 if we did not find it in the list. */
  public int find(final E value){
    if(values != null){
      int i;
      for(i = 0; i < list_length; i++){
        if(values[i].equals(value)){
          return i;
        }
      }
    }
    return -1;
  }

  /** Tests all the values in this scrollbar and returns the index of the
   *  first value that passes the predicate. Returns -1 if no value passed. */
  public final int find(final Predicate<E> predicate){
    if(values != null){
      int i;
      for(i = 0; i < list_length; i++){
        if(predicate.test(values[i])){
          return i;
        }
      }
    }
    return -1;
  }

  @Override
  public final boolean mouseScrolled(double mouse_x, double mouse_y, double scroll_direction){
    mouseScrollWheel((int)scroll_direction);
    return true;
  }

  /** Call this to respond to scrollwheel actions. Positive values scroll up, negative values scroll down.
   *  This is public so the Lists can catch Mouse Scroll events and call this function. */
  public final void mouseScrollWheel(int direction){
    setScrollbarPosition(scrollbar_system.getIndex() - direction);
  }

  /** Moves scrollbar so that the selected value is shown in the middle.
   *  If there is no selection then scrollbar moves to first position. */
  private final void scroll_to_value(){
    if(hasValidSelection()){
      setScrollbarPosition(selected - (visible_elements / 2));
    }
    else{
      setScrollbarPosition(0);
    }
  }

  public final E getSelected(){
    return hasValidSelection() ? values[selected] : invalid_value;
  }

  public final int getSelectedIndex(){
    return selected;
  }

  public final boolean hasValidSelection(){
    return ArrayUtil.isInsideBounds(selected, list_length);
  }

  private final boolean selectedIsVisible(){
    int index = scrollbar_system.getIndex();
    return selected >= index && selected < index + visible_elements;
  }

  @Override
  public final void playDownSound(SoundManager p_playDownSound_1_){
  }

  @Override
  public final void updateWidgetNarration(NarrationElementOutput p_169152_){
  }

}
