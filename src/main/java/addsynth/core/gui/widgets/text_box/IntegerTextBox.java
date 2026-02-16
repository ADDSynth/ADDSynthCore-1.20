package addsynth.core.gui.widgets.text_box;

import java.util.function.Consumer;
import addsynth.core.util.math.common.CommonMath;
import net.minecraft.client.gui.Font;

public class IntegerTextBox extends AbstractIntegerTextBox {

  public IntegerTextBox(Font font, int x, int y, int width, int height){
    super(font, x, y, width, height, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, null, IntegerTextBox::check);
  }

  public IntegerTextBox(Font font, int x, int y, int width, int height, Consumer<Integer> responder){
    super(font, x, y, width, height, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, responder, IntegerTextBox::check);
  }

  public IntegerTextBox(Font font, int x, int y, int width, int height, int initial_value){
    super(font, x, y, width, height, initial_value, Integer.MIN_VALUE, Integer.MAX_VALUE, null, IntegerTextBox::check);
  }

  public IntegerTextBox(Font font, int x, int y, int width, int height, int initial_value, Consumer<Integer> responder){
    super(font, x, y, width, height, initial_value, Integer.MIN_VALUE, Integer.MAX_VALUE, responder, IntegerTextBox::check);
  }

  public IntegerTextBox(Font font, int x, int y, int width, int height, int min, int max){
    super(font, x, y, width, height, CommonMath.clamp(0, min, max), min, max, null, IntegerTextBox::check);
  }

  public IntegerTextBox(Font font, int x, int y, int width, int height, int min, int max, Consumer<Integer> responder){
    super(font, x, y, width, height, CommonMath.clamp(0, min, max), min, max, responder, IntegerTextBox::check);
  }

  public IntegerTextBox(Font font, int x, int y, int width, int height, int initial_value, int min, int max){
    super(font, x, y, width, height, CommonMath.clamp(initial_value, min, max), min, max, null, IntegerTextBox::check);
  }

  public IntegerTextBox(Font font, int x, int y, int width, int height, int initial_value, int min, int max, Consumer<Integer> responder){
    super(font, x, y, width, height, CommonMath.clamp(initial_value, min, max), min, max, responder, IntegerTextBox::check);
  }

  private static final boolean check(String text){
    final char[] characters = text.toCharArray();
    final int length = characters.length;
    if(length > 0){
      for(int i = characters[0] == '-' ? 1 : 0; i < length; i++){
        if(!Character.isDigit(characters[i])){
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers){
    return Character.isDigit(codePoint) || codePoint == '-' ? super.charTyped(codePoint, modifiers) : false;
  }

}
