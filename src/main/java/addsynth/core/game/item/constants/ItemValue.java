package addsynth.core.game.item.constants;

import net.minecraft.world.item.Rarity;
import net.minecraft.ChatFormatting;

public enum ItemValue {

  COMMON   (0, Rarity.COMMON,   Rarity.COMMON.color),   // White
  GOOD     (1, Rarity.UNCOMMON, Rarity.UNCOMMON.color), // Yellow
  RARE     (2, Rarity.RARE,     Rarity.RARE.color),     // Cyan
  EPIC     (3, Rarity.EPIC,     Rarity.EPIC.color),     // Purple
  LEGENDARY(4, Rarity.EPIC,     ChatFormatting.GOLD);   // Orange

  public final int value;
  public final Rarity rarity;
  public final ChatFormatting color;
  
  private ItemValue(final int value, final Rarity rarity, final ChatFormatting color){
    this.value = value;
    this.rarity = rarity;
    this.color = color;
  }

}
