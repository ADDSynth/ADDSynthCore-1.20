package addsynth.core.util.math.block;

import net.minecraft.core.BlockPos;

/** Used to loop through an area of blocks in a particular direction.<br>
 *  Be sure to specify Start and End positions that are WITHIN the area
 *  you want to loop through (inclusive). For example, for a loop that
 *  goes from 0 to 8, the loop will end once we reach 9.<br>
 *  {@link DirectionalLoop.South} is the default DirectionalLoop.
 */
public abstract class DirectionalLoop {

  protected int x;
  protected int y;
  protected int z;
  protected final int start_x;
  protected final int start_y;
  protected final int start_z;
  protected final int end_x;
  protected final int end_y;
  protected final int end_z;

  private DirectionalLoop(int start_x, int start_y, int start_z, int end_x, int end_y, int end_z){
    this.start_x = start_x;
    this.start_y = start_y;
    this.start_z = start_z;
    x = start_x;
    y = start_y;
    z = start_z;
    this.end_x = end_x;
    this.end_y = end_y;
    this.end_z = end_z;
  }

  public abstract void increment();
  
  public final void reset(){
    x = start_x;
    y = start_y;
    z = start_z;
  }
  
  public abstract boolean hasReachedEnd();
  
  public final BlockPos getPosition(){ return new BlockPos(x, y, z); }
  
  public static final class East extends DirectionalLoop {
  
    public East(final BlockArea area){
      super(area.min_x, area.min_y, area.min_z, area.max_x, area.max_y, area.max_z);
    }
    
    public East(int min_x, int min_y, int min_z, int max_x, int max_y, int max_z){
      super(min_x, min_y, min_z, max_x, max_y, max_z);
    }
    
    @Override
    public final void increment(){
      y++;
      if(y > end_y){
        y = start_y;
        z++;
        if(z > end_z){
          z = start_z;
          x++;
        }
      }
    }
    
    @Override
    public final boolean hasReachedEnd(){
      return x > end_x;
    }
    
  }

  public static final class West extends DirectionalLoop {
  
    public West(final BlockArea area){
      super(area.max_x, area.min_y, area.min_z, area.min_x, area.max_y, area.max_z);
    }
    
    public West(int min_x, int min_y, int min_z, int max_x, int max_y, int max_z){
      super(max_x, min_y, min_z, min_x, max_y, max_z);
    }
    
    @Override
    public final void increment(){
      y++;
      if(y > end_y){
        y = start_y;
        z++;
        if(z > end_z){
          z = start_z;
          x--;
        }
      }
    }
    
    @Override
    public final boolean hasReachedEnd(){
      return x < end_x;
    }
    
  }

  public static final class Up extends DirectionalLoop {
  
    public Up(final BlockArea area){
      super(area.min_x, area.min_y, area.min_z, area.max_x, area.max_y, area.max_z);
    }
    
    public Up(int min_x, int min_y, int min_z, int max_x, int max_y, int max_z){
      super(min_x, min_y, min_z, max_x, max_y, max_z);
    }
    
    @Override
    public final void increment(){
      x++;
      if(x > end_x){
        x = start_x;
        z++;
        if(z > end_z){
          z = start_z;
          y++;
        }
      }
    }
    
    @Override
    public final boolean hasReachedEnd(){
      return y > end_y;
    }
    
  }

  public static final class Down extends DirectionalLoop {
  
    public Down(final BlockArea area){
      super(area.min_x, area.max_y, area.min_z, area.max_x, area.min_y, area.max_z);
    }
    
    public Down(int min_x, int min_y, int min_z, int max_x, int max_y, int max_z){
      super(min_x, max_y, min_z, max_x, min_y, max_z);
    }
    
    @Override
    public final void increment(){
      x++;
      if(x > end_x){
        x = start_x;
        z++;
        if(z > end_z){
          z = start_z;
          y--;
        }
      }
    }
    
    @Override
    public final boolean hasReachedEnd(){
      return y < end_y;
    }
    
  }

  // Default loop
  public static final class South extends DirectionalLoop {
  
    public South(final BlockArea area){
      super(area.min_x, area.min_y, area.min_z, area.max_x, area.max_y, area.max_z);
    }
    
    public South(int min_x, int min_y, int min_z, int max_x, int max_y, int max_z){
      super(min_x, min_y, min_z, max_x, max_y, max_z);
    }
    
    @Override
    public final void increment(){
      x++;
      if(x > end_x){
        x = start_x;
        y++;
        if(y > end_y){
          y = start_y;
          z++;
        }
      }
    }
    
    @Override
    public final boolean hasReachedEnd(){
      return z > end_z;
    }
    
  }

  public static final class North extends DirectionalLoop {
  
    public North(final BlockArea area){
      super(area.min_x, area.min_y, area.max_z, area.max_x, area.max_y, area.min_z);
    }
    
    public North(int min_x, int min_y, int min_z, int max_x, int max_y, int max_z){
      super(min_x, min_y, max_z, max_x, max_y, min_z);
    }
    
    @Override
    public final void increment(){
      x++;
      if(x > end_x){
        x = start_x;
        y++;
        if(y > end_y){
          y = start_y;
          z--;
        }
      }
    }
    
    @Override
    public final boolean hasReachedEnd(){
      return z < end_z;
    }
    
  }

}
