package addsynth.core.game.item.tool;

public final class ToolConstants {

public static final int   sword_damage   = 3;
public static final float shovel_damage  = 1.5f;
public static final int   pickaxe_damage = 1;
public static final float axe_damage     = 6.0f;

public static final float sword_speed   = -2.4f;
public static final float pickaxe_speed = -2.8f;
public static final float shovel_speed  = -3.0f;
public static final float axe_speed     = -3.2f;

/** For hoes, the speed depends on the Material, so check the speeds and make your best judgement. */
public static final class HOE {
  public static final class WOODEN {
    public static final int damage = 0;
    public static final int speed = -3;
  }
  public static final class STONE {
    public static final int damage = -1;
    public static final int speed = -2;
  }
  public static final class IRON {
    public static final int damage = -2;
    public static final int speed = -1;
  }
  public static final class GOLD {
    public static final int damage = 0;
    public static final int speed = -3;
  }
  public static final class DIAMOND {
    public static final int damage = -3;
    public static final int speed = 0;
  }
  public static final class NETHERITE {
    public static final int damage = -4;
    public static final int speed = 0;
  }
}

}
