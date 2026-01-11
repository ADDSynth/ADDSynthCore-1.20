package addsynth.core.game.item.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;

public final class EnchantPair {

  public final Enchantment enchantment;
  public final int level;

  public EnchantPair(final Enchantment enchantment, final int level){
    this.enchantment = enchantment;
    this.level = level;
  }

}
