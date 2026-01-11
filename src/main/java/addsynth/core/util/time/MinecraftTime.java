package addsynth.core.util.time;

import javax.annotation.Nonnegative;
import addsynth.core.util.java.StringUtil;

public class MinecraftTime {

  public int ticks;

  public MinecraftTime(){
    ticks = 0;
  }

  public MinecraftTime(int ticks){
    this.ticks = ticks;
  }

  public final void set(int ticks){
    this.ticks = ticks;
  }

  public final void tick(){
    ticks++;
  }

  public final void decrement(){
    if(ticks > 0){
      ticks--;
    }
  }

  public final int getSeconds(){ return (ticks / TimeConstants.ticks_per_second) % 60; }
  public final int getMinutes(){ return (ticks / TimeConstants.ticks_per_minute) % 60; }
  public final int getHours  (){ return (ticks / TimeConstants.ticks_per_hour  ) % 24; }
  public final int getDays   (){ return (ticks / TimeConstants.ticks_per_day   ) % 365; }

  public final boolean hasHours(){return ticks >= TimeConstants.ticks_per_hour; }
  public final boolean hasDays (){return ticks >= TimeConstants.ticks_per_day;  }

  /** Prints this MinecraftTime in the form of 'h# m# s#'. */
  public final String print(){
    return StringUtil.build(getHours(), "h ", getMinutes(), "m ", getSeconds(), "s");
  }

  /** Prints a time given the number of ticks provided in the form of 'h# m# s#'. */
  public static final String print(@Nonnegative final int ticks){
    final int seconds = (ticks / TimeConstants.ticks_per_second) % 60;
    final int minutes = (ticks / TimeConstants.ticks_per_minute) % 60;
    final int hours   = (ticks / TimeConstants.ticks_per_hour) % 24;
    return StringUtil.build(hours, "h ", minutes, "m ", seconds, "s");
  }

  /** Prints a time given the number of ticks in the form of m:ss or h:mm:ss if there are hours. */
  public static final String print2(final int ticks){
    final String second_string = String.format("%02d", (ticks / TimeConstants.ticks_per_second) % 60);
    final int minutes = (ticks / TimeConstants.ticks_per_minute) % 60;
    if(ticks >= TimeConstants.ticks_per_hour){
      return StringUtil.build(ticks / TimeConstants.ticks_per_hour, ':', String.format("%02d", minutes), ':', second_string );
    }
    final StringBuilder s = new StringBuilder();
    s.append(minutes);
    s.append(':');
    s.append(second_string);
    return s.toString();
  }

}
