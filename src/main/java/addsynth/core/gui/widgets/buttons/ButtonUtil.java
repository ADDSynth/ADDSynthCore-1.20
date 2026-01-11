package addsynth.core.gui.widgets.buttons;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public final class ButtonUtil {

  public static final int width = 16;
  public static final int height = 16;

  public static final Button getLeftArrowButton(int x, int y, Runnable runnable){
    return getArrowButton(true, x, y, runnable);
  }

  public static final Button getRightArrowButton(int x, int y, Runnable runnable){
    return getArrowButton(false, x, y, runnable);
  }

  private static final Button getArrowButton(boolean direction, int x, int y, Runnable runnable){
    return Button.builder(Component.literal(direction ? "<" : ">"), // ◄ and ► don't work, they are too small
      (Button button) -> {if(runnable != null){runnable.run();}}).bounds(x, y, width, height).build();
  }

  public static final Button getBigtLeftArrowButton(int x, int y, Runnable runnable){
    return getBigArrowButton(true, x, y, runnable);
  }

  public static final Button getBigRightArrowButton(int x, int y, Runnable runnable){
    return getBigArrowButton(false, x, y, runnable);
  }

  private static final Button getBigArrowButton(boolean direction, int x, int y, Runnable runnable){
    return Button.builder(Component.literal(direction ? "←" : "→"),
      (Button button) -> {if(runnable != null){runnable.run();}}).bounds(x, y, 30, 20).build();
  }

}
