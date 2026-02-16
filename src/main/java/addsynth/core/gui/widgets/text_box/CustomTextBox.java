package addsynth.core.gui.widgets.text_box;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public abstract class CustomTextBox extends EditBox {

  public CustomTextBox(Font font, int x, int y, int width, int height, String text, Predicate<String> filter){
    super(font, x, y, width, height, Component.empty());
    super.setValue(text);
    super.setResponder(this::onChanged);
    super.setFilter(filter);
  }

  @Override
  @Deprecated
  public final void setResponder(Consumer<String> responder){
  }

  @Override
  @Deprecated
  public final void setFilter(Predicate<String> filter){
  }

  protected abstract void onChanged(String text);

}
