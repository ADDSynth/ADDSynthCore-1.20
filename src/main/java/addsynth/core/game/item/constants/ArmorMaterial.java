package addsynth.core.game.item.constants;

public enum ArmorMaterial {

  LEATHER("leather"),
  CHAINMAIL("chainmail"),
  IRON("iron"),
  GOLD("gold"),
  DIAMOND("diamond"),
  NETHERITE("netherite");

  public final String name;

  private ArmorMaterial(final String name){
    this.name = name;
  }

}
