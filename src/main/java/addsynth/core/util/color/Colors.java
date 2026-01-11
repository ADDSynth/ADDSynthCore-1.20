package addsynth.core.util.color;

import net.minecraft.world.level.material.MapColor;

public enum Colors {

  // Some of these colors values were obtained from looking them up on Wikipedia.
  WHITE(    "White",     0xFFFFFF, MapColor.SNOW, MapColor.QUARTZ),
  SILVER(   "Silver",    0xC0C0C0, MapColor.WOOL, MapColor.METAL, MapColor.CLAY),
  GRAY(     "Gray",      0x808080, MapColor.STONE, MapColor.COLOR_LIGHT_GRAY),
  DARK_GRAY("Dark Gray", 0x404040, MapColor.COLOR_GRAY),
  BLACK(    "Black",     0x000000, MapColor.NONE, MapColor.COLOR_BLACK),
  RED(      "Red",       0xFF0000, MapColor.FIRE, MapColor.COLOR_RED, MapColor.NETHER),
  ORANGE(   "Orange",    0xFF8000, MapColor.COLOR_ORANGE),
  YELLOW(   "Yellow",    0xFFFF00, MapColor.COLOR_YELLOW, MapColor.GOLD),
  GREEN(    "Green",     0X00FF00, MapColor.GRASS, MapColor.COLOR_LIGHT_GREEN, MapColor.PLANT, MapColor.COLOR_GREEN, MapColor.EMERALD),
  CYAN(     "Cyan",      0x00FFFF, MapColor.COLOR_CYAN, MapColor.DIAMOND),
  BLUE(     "Blue",      0x0000FF, MapColor.COLOR_BLUE, MapColor.COLOR_LIGHT_BLUE, MapColor.WATER, MapColor.ICE, MapColor.LAPIS),
  MAGENTA(  "Magenta",   0xFF00FF, MapColor.COLOR_MAGENTA),
  PURPLE(   "Purple",    0x800080, MapColor.COLOR_PURPLE),
  PINK(     "Pink",      0xFFC0CB, MapColor.COLOR_PINK),
  PEACH(    "Peach",     0xFFE5B4, MapColor.SAND),
  BROWN(    "Brown",     0x964B00, MapColor.COLOR_BROWN, MapColor.DIRT, MapColor.WOOD, MapColor.PODZOL);

  public final String name;
  public final int value;
  public final MapColor[] colors;
  
  private Colors(final String name, final int value, final MapColor ... overrides){
    this.name = name;
    this.value = value;
    this.colors = overrides;
  }

  @Override
  public final String toString(){
    return name+": "+value;
  }

  public final Color toColor(){
    return new Color(value);
  }

}
