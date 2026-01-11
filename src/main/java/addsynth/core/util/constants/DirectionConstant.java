package addsynth.core.util.constants;

import net.minecraft.core.Direction;

/** <p>These are the same indexes as that in the {@link Direction} class.
 *     You can get them by calling {@link Direction#get3DDataValue()}.
 *  <p>It's better to get the data values directly from Minecraft's code,
 *     IN CASE they ever change, but they probably never will. Even though
 *     specified as final, getting the values by calling a method means
 *     they aren't a constant expression. Now they are constant expressions,
 *     allowing use in switch statements.
 */
public final class DirectionConstant {

  public static final int DOWN  = 0;
  public static final int UP    = 1;
  public static final int NORTH = 2;
  public static final int SOUTH = 3;
  public static final int WEST  = 4;
  public static final int EAST  = 5;

}
