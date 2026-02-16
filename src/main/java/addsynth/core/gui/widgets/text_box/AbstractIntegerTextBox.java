package addsynth.core.gui.widgets.text_box;

import java.util.function.Consumer;
import java.util.function.Predicate;
import addsynth.core.util.math.common.CommonMath;
import net.minecraft.client.gui.Font;

public abstract class AbstractIntegerTextBox extends CustomTextBox {

  private int value;
  private int temp;
  private Consumer<Integer> action;
  private boolean skip_respond;
  private int min;
  private int max;
  // private int clamped_value;

  AbstractIntegerTextBox(Font font, int x, int y, int width, int height, int initial_value, int min, int max, Consumer<Integer> responder, Predicate<String> filter){
    super(font, x, y, width, height, Integer.toString(initial_value), filter);
    this.value = initial_value;
    this.min = min;
    this.max = max;
    action = responder;
  }

  public final void setCallback(Consumer<Integer> responder){
    this.action = responder;
  }

  public final void setClamped(int min, int max){
    this.min = Math.min(min, max);
    this.max = Math.max(min, max);
    checkClamped(value);
  }

  @Override
  protected final void onChanged(final String text){
    if(text.isEmpty()){
      super.setValue("0"); // set text to 0, and onChanged() will be called again.
      return;
    }
    try{
      temp = Integer.parseInt(text);
    }
    catch(NumberFormatException e){
      return;
    }
    if(checkClamped(temp)){
      return;
    }
    if(temp != value){
      value = temp;
      if(skip_respond){
        skip_respond = false;
        return;
      }
      if(action != null){
        action.accept(value);
      }
    }
  }

  private boolean checkClamped(int value){
    temp = CommonMath.clamp(value, min, max);
    if(temp != value){
      super.setValue(Integer.toString(temp)); // set text to clamped value and this will call our onChanged() again
      return true;
    }
    return false;
  }

  @Override
  @Deprecated
  public String getValue(){
    return super.getValue();
  }

  @Override
  @Deprecated
  public void setValue(String text){
  }

  public int get_value(){
    return value;
  }

  public void set_value(int value){
    set_value(value, false);
  }

  public void set_value(int value, boolean check_changes){
    skip_respond = !check_changes;
    // Use vanilla setValue() first, so it will run checks on the input, such as reducing length
    // if it goes over max characters allowed. This will trigger our onChanged() callback
    super.setValue(Integer.toString(value)); // this will call our function
  }

}
