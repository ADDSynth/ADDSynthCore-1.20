package addsynth.core.gui.widgets.scrollbar;

import javax.annotation.Nonnull;
import addsynth.core.util.game.data.CombinedNameComponent;

/** A specific Scrollbar created for use in the Team Manager. Items in the list are of type
 *  {@link CombinedNameComponent} because they need to hold the display name and the ID string.
 *  We can get the ID name for use in Team Manager Commands.
 */
public class CombinedNameScrollbar extends AbstractScrollbar<CombinedNameComponent, CombinedListEntry> {

  private boolean use_translated_names;

  public CombinedNameScrollbar(int x, int y, int height, CombinedListEntry[] list_items){
    super(x, y, height, list_items, null, CombinedNameComponent.EMPTY);
    use_translated_names = true;
  }

  public CombinedNameScrollbar(int x, int y, int height, CombinedListEntry[] list_items, boolean use_translated_names){
    super(x, y, height, list_items, null, CombinedNameComponent.EMPTY);
    this.use_translated_names = use_translated_names;
  }

  public CombinedNameScrollbar(int x, int y, int height, CombinedListEntry[] list_items, CombinedNameComponent[] values){
    super(x, y, height, list_items, values, CombinedNameComponent.EMPTY);
    use_translated_names = true;
  }

  public CombinedNameScrollbar(int x, int y, int height, CombinedListEntry[] list_items, CombinedNameComponent[] values, boolean use_translated_names){
    super(x, y, height, list_items, values, CombinedNameComponent.EMPTY);
    this.use_translated_names = use_translated_names;
  }

  public final void changeDisplayMode(final boolean mode){
    use_translated_names = mode;
    // updateList(); Need to completely replace the values, because they are re-sorted
  }

  @Override
  protected final void updateListItem(final CombinedListEntry list_item, final int id, final CombinedNameComponent value){
    list_item.set(id, value, use_translated_names);
  }

  @Override
  @Nonnull
  protected CombinedNameComponent[] createEmptyValueArray(){
    return new CombinedNameComponent[0];
  }

}
