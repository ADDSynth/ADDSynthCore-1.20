package addsynth.core.gui.section;

import addsynth.core.util.java.StringUtil;

/** Used to define sections on a Gui to assist in positioning widgets and text.<br>
 *  You are allowed to edit any variable at any time, though it is generally recommended<br>
 *  that you use the provided functions since they update all variables correctly.
 * @author ADDSynth
 */
public final class MutableGuiSection {

  public int x;
  public int y;
  public int left;
  public int top;
  public int width;
  public int height;
  public int right;
  public int bottom;
  public int center_x;
  public int center_y;

  public final void set(int x, int y, int width, int height){
    this.x        = x;
    this.y        = y;
    this.width    = width;
    this.height   = height;
    this.left     = x;
    this.top      = y;
    this.right    = x + width;
    this.bottom   = y + height;
    this.center_x = x + (width/2);
    this.center_y = y + (height/2);
  }

  public final void setPosition(int x, int y){
    this.x        = x;
    this.y        = y;
    this.left     = x;
    this.top      = y;
    this.right    = x + width;
    this.bottom   = y + height;
    this.center_x = x + (width/2);
    this.center_y = y + (height/2);
  }

  public final void setDimensions(int width, int height){
    this.width    = width;
    this.height   = height;
    this.right    = x + width;
    this.bottom   = y + height;
    this.center_x = x + (width/2);
    this.center_y = y + (height/2);
  }
  
  public final void setBox(int left, int top, int right, int bottom){
    this.x        = left;
    this.y        = top;
    this.width    = right - left;
    this.height   = bottom - top;
    this.left     = left;
    this.top      = top;
    this.right    = right;
    this.bottom   = bottom;
    this.center_x = left + (width/2);
    this.center_y = top + (height/2);
  }
  
  @Override
  public final String toString(){
    return StringUtil.build(MutableGuiSection.class.getSimpleName(), "{X=", x, ", Y=", y,
      ", Width=", width, ", Height=", height, ", Right=", right, ", Bottom=", bottom, "}");
  }

}
