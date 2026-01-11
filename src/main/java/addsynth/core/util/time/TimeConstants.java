package addsynth.core.util.time;

public final class TimeConstants {

  public static final int ticks_per_second = 20;
  public static final int ticks_per_minute = ticks_per_second * 60;
  public static final int ticks_per_hour = ticks_per_minute * 60;
  public static final int ticks_per_day = ticks_per_hour * 24;
  
  // I was going to calculate these myself, but I'm sure the compiler already does that.
  // better to leave these as they are in case ticks per second changes for whatever reason.
  public static final double tick_time_in_seconds = 1.0 / ticks_per_second;
  public static final long tick_time_in_milliseconds = 1000 / ticks_per_second;
  public static final long tick_time_in_nanoseconds = 1_000_000_000 / ticks_per_second;
  
}
