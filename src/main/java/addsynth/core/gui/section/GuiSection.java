package addsynth.core.gui.section;

import addsynth.core.util.java.StringUtil;

/** Used to define sections on a Gui to assist in positioning widgets and text.<br>
 *  If you're positioning widgets, you must add <code>guiLeft</code> and <code>guiTop</code> to the<br>
 *  dimensions, or extend from an existing GuiSection that already has those added.
 * @author ADDSynth
 */
public final class GuiSection {

  public final int x;
  public final int y;
  public final int left;
  public final int top;
  public final int width;
  public final int height;
  public final int right;
  public final int bottom;
  public final int horizontal_center;
  public final int vertical_center;

  private GuiSection(int x, int y, int width, int height, int right, int bottom){
    this.x = x;
    this.y = y;
    this.left = x;
    this.top = y;
    this.width = width;
    this.height = height;
    this.right = right;
    this.bottom = bottom;
    horizontal_center = x + (width/2);
      vertical_center = y + (height/2);
  }

  public static final GuiSection dimensions(int x, int y, int width, int height){
    return new GuiSection(x, y, width, height, x + width, y + height);
  }
  
  public static final GuiSection box(int left, int top, int right, int bottom){
    return new GuiSection(left, top, right - left, bottom - top, right, bottom);
  }

  @Override
  public final String toString(){
    return StringUtil.build(GuiSection.class.getSimpleName(), "{X=", x, ", Y=", y,
      ", Width=", width, ", Height=", height, ", Right=", right, ", Bottom=", bottom, "}");
  }

}
