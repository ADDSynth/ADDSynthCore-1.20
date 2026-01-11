package addsynth.core.gui.widgets.buttons;

import net.minecraft.network.chat.Component;

/** This is a Client-side only checkbox. It does not need to
 *  send a network message to the server to update the TileEntity. */
public class ClientCheckbox extends Checkbox {

  public boolean checked;

  public ClientCheckbox(final int x, final int y, final Component text_component){
    super(x, y, text_component);
  }

  @Override
  protected boolean get_toggle_state(){
    return checked;
  }

  @Override
  public void onPress(){
    checked = !checked;
  }

}
