package addsynth.core.gui.widgets.text_box;

import java.util.function.Consumer;
import net.minecraft.client.gui.Font;

public class UnsignedIntegerTextBox extends AbstractIntegerTextBox {

  public UnsignedIntegerTextBox(Font font, int x, int y, int width, int height){
    super(font, x, y, width, height, 0, 0, Integer.MAX_VALUE, null, UnsignedIntegerTextBox::check);
  }

  public UnsignedIntegerTextBox(Font font, int x, int y, int width, int height, Consumer<Integer> responder){
    super(font, x, y, width, height, 0, 0, Integer.MAX_VALUE, responder, UnsignedIntegerTextBox::check);
  }

  public UnsignedIntegerTextBox(Font font, int x, int y, int width, int height, int initial_value){
    super(font, x, y, width, height, Math.max(initial_value, 0), 0, Integer.MAX_VALUE, null, UnsignedIntegerTextBox::check);
  }

  public UnsignedIntegerTextBox(Font font, int x, int y, int width, int height, int initial_value, Consumer<Integer> responder){
    super(font, x, y, width, height, Math.max(initial_value, 0), 0, Integer.MAX_VALUE, responder, UnsignedIntegerTextBox::check);
  }

  public UnsignedIntegerTextBox(Font font, int x, int y, int width, int height, int min, int max){
    super(font, x, y, width, height, 0, Math.max(min, 0), max, null, UnsignedIntegerTextBox::check);
  }

  public UnsignedIntegerTextBox(Font font, int x, int y, int width, int height, int min, int max, Consumer<Integer> responder){
    super(font, x, y, width, height, 0, Math.max(min, 0), max, responder, UnsignedIntegerTextBox::check);
  }

  public UnsignedIntegerTextBox(Font font, int x, int y, int width, int height, int initial_value, int min, int max){
    super(font, x, y, width, height, Math.max(initial_value, min), Math.max(min, 0), max, null, UnsignedIntegerTextBox::check);
  }

  public UnsignedIntegerTextBox(Font font, int x, int y, int width, int height, int initial_value, int min, int max, Consumer<Integer> responder){
    super(font, x, y, width, height, Math.max(initial_value, min), Math.max(min, 0), max, responder, UnsignedIntegerTextBox::check);
  }

  private static final boolean check(String text){
    for(char c : text.toCharArray()){
      if(!Character.isDigit(c)){
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers){
    return Character.isDigit(codePoint) ? super.charTyped(codePoint, modifiers) : false;
  }

}
