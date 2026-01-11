package addsynth.core.util.time;

import net.minecraft.server.MinecraftServer;

public final class TimeUtil {

   /** @deprecated Skip this and just use {@link System#nanoTime()} */
   public static final long get_start_time(){
     return System.nanoTime();
   }
   
   /** @deprecated Skip this and just use <code>elapsedTime = {@link System#nanoTime()} - startTime</code> */
   public static final long get_elapsed_time(final long start_time){
     return System.nanoTime() - start_time;
   }
   
   public static final boolean time_exceeded(final long start_time, final long nano_seconds){
     return System.nanoTime() - start_time >= nano_seconds;
   }

  public static final boolean time_exceeded(final long start_time, final double seconds){
    return (double)(System.nanoTime() - start_time) / 1_000_000_000 >= seconds;
  }

  public static final boolean exceeded_server_tick_time(final MinecraftServer server, final long start_time){
    return (server.getAverageTickTime() * 1_000_000) + (System.nanoTime() - start_time) >= TimeConstants.tick_time_in_nanoseconds;
  }

// =================================================================================================

  public static final double convert_to_seconds(final long nano_seconds){
    return (double)nano_seconds / 1_000_000_000;
  }

  public static final double seconds_to_nanoseconds(double seconds){
    return seconds * 1_000_000_000;
  }

  public static final double milliseconds_to_nanoseconds(double milli_seconds){
    return milli_seconds * 1_000_000;
  }

  public static final double nanoseconds_to_milliseconds(long nano_seconds){
    return (double)nano_seconds / 1_000_000;
  }

}
