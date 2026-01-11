package addsynth.core.util.game.redstone;

public enum RedstoneState {

  OFF,
  RISING_EDGE,
  ON,
  FALLING_EDGE;

  public static RedstoneState get(final boolean powered, final boolean previous){
    return powered ? (previous ? ON : RISING_EDGE) : (previous ? FALLING_EDGE : OFF);
  }

}
