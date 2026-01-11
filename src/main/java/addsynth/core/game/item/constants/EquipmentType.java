package addsynth.core.game.item.constants;

public enum EquipmentType {

  HELMET("helmet"),
  CHESTPLATE("chestplate"),
  LEGGINGS("leggings"),
  BOOTS("boots");

  public final String name;
  
  private EquipmentType(final String name){
    this.name = name;
  }

}
