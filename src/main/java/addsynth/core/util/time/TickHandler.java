package addsynth.core.util.time;

import javax.annotation.Nonnegative;

/** Tick Handlers are used to run code at specified intervals, instead of
 *  every tick. By default it executes your code once every second.
 *  Keeps an internal tick counter so as not to clutter up your class.
 * @author ADDSynth
 */
public final class TickHandler {

  private int tick;
  private final int max_tick;

  public TickHandler(){
    max_tick = TimeConstants.ticks_per_second;
  }
  
  public TickHandler(@Nonnegative final int tick_duration){
    max_tick = tick_duration;
  }

  public final boolean tick(){
    tick++;
    if(tick >= max_tick){
      tick = 0;
      return true;
    }
    return false;
  }

  /** Tick with a custom max_tick time, which could change on-the-fly.
   *  This is generally not used. */
  public final boolean tick(final int max_tick){
    if(max_tick <= 0){
      return true;
    }
    tick++;
    if(tick >= max_tick){
      tick = 0;
      return true;
    }
    return false;
  }

  public final void reset(){
    tick = 0;
  }

}
