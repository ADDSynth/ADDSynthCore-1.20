package addsynth.core.gui.section;

import addsynth.core.util.java.StringUtil;
import net.minecraft.client.gui.screens.Screen;

/** Used to define sections on a Gui to assist in positioning widgets and text.<br>
 *  This version has its width and height fixed at creation.<br>
 *  This version is primarily used to represent the dimensions of the main gui window.
 * @author ADDSynth
 * @see addsynth.core.gui.GuiBase#guiBox
 */
public final class FixedSizeGuiSection {

  public int x;
  public int y;
  public int left;
  public int top;
  public final int width;
  public final int height;
  private final int half_width;
  private final int half_height;
  public int right;
  public int bottom;
  public int center_x;
  public int center_y;

  public FixedSizeGuiSection(int width, int height){
    this.width       = width;
    this.height      = height;
    this.half_width  = width / 2;
    this.half_height = height / 2;
    this.right       = width;
    this.bottom      = height;
    this.center_x    = half_width;
    this.center_y    = half_height;
  }

  public FixedSizeGuiSection(int x, int y, int width, int height){
    this.x           = x;
    this.y           = y;
    this.width       = width;
    this.height      = height;
    this.half_width  = width / 2;
    this.half_height = height / 2;
    this.left        = x;
    this.top         = y;
    this.right       = x + width;
    this.bottom      = y + height;
    this.center_x    = x + half_width;
    this.center_y    = y + half_height;
  }

  public final void setPosition(int x, int y){
    this.x        = x;
    this.y        = y;
    this.left     = x;
    this.top      = y;
    this.right    = x + width;
    this.bottom   = y + height;
    this.center_x = x + half_width;
    this.center_y = y + half_height;
  }

  /** This function should be called in the {@link Screen#init()} method to<br>
   *  adjust the gui dimensions because the sreen window was resized.
   * @param screen_width
   * @param screen_height
   */
  public final void screenResize(int screen_width, int screen_height){
    this.x        = (screen_width  - width ) / 2;
    this.y        = (screen_height - height) / 2;
    this.left     = x;
    this.top      = y;
    this.right    = x + width;
    this.bottom   = y + height;
    this.center_x = x + half_width;
    this.center_y = y + half_height;
  }

  @Override
  public final String toString(){
    return StringUtil.build(FixedSizeGuiSection.class.getSimpleName(), "{X=", x, ", Y=", y,
      ", Width=", width, ", Height=", height, ", Right=", right, ", Bottom=", bottom, "}");
  }

}
