package addsynth.core.gui.widgets.scrollbar;

import addsynth.core.util.game.data.CombinedNameComponent;
import net.minecraft.network.chat.Component;

public class CombinedListEntry extends AbstractListEntry<CombinedNameComponent> {

  public CombinedListEntry(int x, int y, int width, int height){
    super(x, y, width, height);
  }

  @Override
  @Deprecated
  public final void set(final int entry_id, final CombinedNameComponent value){
    this.entry_id = entry_id;
    setMessage(value.getDisplayName());
  }

  public final void set(final int entry_id, final CombinedNameComponent value, final boolean mode){
    this.entry_id = entry_id;
    setMessage(mode ? value.getDisplayName() : Component.literal(value.getName()));
  }

}
