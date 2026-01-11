package addsynth.core.util.debug;

import java.util.Objects;
import javax.annotation.Nullable;
import addsynth.core.ADDSynthCore;
import addsynth.core.util.server.ServerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.profiling.ProfilerFiller;

public final class DebugUtil {

  /** Similar to {@link Objects#requireNonNull(Object, String)}, except this
   *  merely prints a NullPointerException to the log instead of throwing it.
   * @param obj
   * @param message
   */
  public static final void checkForNull(Object obj, String message){
    if(obj == null){
      ADDSynthCore.log.error(message, new NullPointerException());
    }
  }

  @Nullable
  @SuppressWarnings("resource")
  public static final ProfilerFiller getProfiler(){
    final MinecraftServer server = ServerUtils.getServer();
    return server != null ? server.getProfiler() : null;
  }

  /** Use this only if you don't have the server readily available.<br/>
   *  You can see your profiler sections when you press F3+L in a
   *  single-player world, or /perf in the server console. */
  public static final void beginSection(final String name){
    @SuppressWarnings("resource")
    final MinecraftServer server = ServerUtils.getServer();
    if(server != null){
      server.getProfiler().push(name);
    }
  }

  /** Use this only if you don't have the server readily available. */
  public static final void endSection(){
    @SuppressWarnings("resource")
    final MinecraftServer server = ServerUtils.getServer();
    if(server != null){
      server.getProfiler().pop();
    }
  }

}
