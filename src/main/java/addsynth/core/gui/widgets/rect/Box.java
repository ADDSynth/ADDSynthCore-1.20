package addsynth.core.gui.widgets.rect;

public final class Box {

  private int x;
  private int y;
  private int width;
  private int height;
  
  private int half_width;
  private int half_height;
  private int center_x;
  private int center_y;
  
  public Box(){
  }
  
  public Box(int width, int height){
    this.width = width;
    this.height = height;
  }
  
  public Box(int x, int y, int width, int height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public final int getX(){return x;}
  public final int getY(){return y;}
  public final int getCenterX(){return center_x;}
  public final int getCenterY(){return center_y;}
  public final int getWidth(){return width;}
  public final int getHeight(){return height;}
  public final int getHalfWidth(){return half_width;}
  public final int getHalfHeight(){return half_height;}
  public final int getLeft(){ return x;}
  public final int getTop(){return y;}
  public final int getRight(){return x+width;}
  public final int getBottom(){return y+height;}
  
  public final void setX(int x){
    this.x = x;
    center_x = x + half_width;
  }
  public final void setY(int y){
    this.y = y;
    center_y = y + half_height;
  }
  public final void setCenterX(int x){
    this.x = x - half_width;
    center_x = x;
  }
  public final void setCenterY(int y){
    this.y = y - half_height;
    center_y = y;
  }
  public final void setWidth(int width){
    this.width = width;
    half_width = width/2;
    center_x = x + half_width;
  }
  public final void setHeight(int height){
    this.height = height;
    half_height = height/2;
    center_y = y + half_height;
  }

  public final boolean isInside(final double x, final double y){
    return x >= getLeft() && x < getRight() && y >= getTop() && y < getBottom();
  }

}
