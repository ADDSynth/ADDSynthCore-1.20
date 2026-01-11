package addsynth.core.game.item.enchantment;

import java.util.Arrays;
import java.util.List;
import addsynth.core.game.item.ItemUtil;
import addsynth.core.util.java.ArrayUtil;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public final class EnchantmentUtil {

  public static final Enchantment[] common_armor_enchantments = new Enchantment[] {
    Enchantments.UNBREAKING,              // 3 levels
    Enchantments.ALL_DAMAGE_PROTECTION,   // 4 levels
    Enchantments.PROJECTILE_PROTECTION,   // 4 levels
    Enchantments.BLAST_PROTECTION,        // 4 levels, reduces damage and knockback from explosions.
    Enchantments.FIRE_PROTECTION,         // 4 levels, protects against fire-based damage.
    Enchantments.THORNS,                  // 3 levels, damages your attacker, regardless if it was melee or ranged.
    Enchantments.MENDING                  // 1 level,  repairs durability with experience.
  };

  public static final Enchantment[] helmet_enchantments = new Enchantment[] {
    Enchantments.AQUA_AFFINITY,           // only 1 level, enables underwater mining.
    Enchantments.RESPIRATION              // 3 levels, increases underwater breathing time.
  };

  public static final Enchantment[] boot_enchantments = new Enchantment[] {
    Enchantments.DEPTH_STRIDER,           // 3 levels, increases underwater speed.
    Enchantments.FALL_PROTECTION,         // 4 levels, decreases fall damage.
    Enchantments.FROST_WALKER             // 2 levels, freezes water so you can walk on it.
  };

  public static final List<Enchantment> get_enchantments_for_item(final ItemStack stack){
    Enchantment[] enchantment_list = new Enchantment[0];
    if(ItemUtil.itemStackExists(stack)){
      final Item item = stack.getItem();
      if(item instanceof ArmorItem){
        switch(((ArmorItem)item).getType()){
        case HELMET:     enchantment_list = ArrayUtil.combine_arrays(common_armor_enchantments, helmet_enchantments); break;
        case CHESTPLATE: enchantment_list = common_armor_enchantments; break;
        case LEGGINGS:   enchantment_list = common_armor_enchantments; break;
        case BOOTS:      enchantment_list = ArrayUtil.combine_arrays(common_armor_enchantments, boot_enchantments); break;
        }
      }
      if(item instanceof SwordItem){}
      if(item instanceof ShovelItem){}
      if(item instanceof PickaxeItem){}
      if(item instanceof AxeItem){}
      if(item instanceof HoeItem){}
      if(item instanceof ShearsItem){}
      if(item instanceof FishingRodItem){}
      if(item instanceof BowItem){}
    }
    return Arrays.asList(enchantment_list);
  }

  /** @see ItemStack#enchant(Enchantment, int) */
  public static final boolean does_item_have_enchantment(final ItemStack stack, final Enchantment ... enchantments){
    if(stack.isEnchanted()){
      final ListTag enchantment_tag_list = stack.getEnchantmentTags();
      final int list_size = enchantment_tag_list.size();
      final IForgeRegistry<Enchantment> registry = ForgeRegistries.ENCHANTMENTS;
      int i;
      String id;
      ResourceLocation id2;
      for(i = 0; i < list_size; i++){
        id = enchantment_tag_list.getCompound(i).getString("id");
        for(final Enchantment enchantment : enchantments){
          id2 = registry.getKey(enchantment);
          if(id2 != null){
            if(id.equals(id2.toString())){
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /** This will check an ItemStack if it already has an enchantment that conflicts with the one you're
   *  trying to apply. Certain Enchantments are deemed "mutually exclusive" by Mojang.
   * @param enchantment
   * @param stack
   * @return false if there is no conflict.
   */
  public static final boolean check_conflicts(final Enchantment enchantment, final ItemStack stack){
    if(enchantment == Enchantments.FROST_WALKER){
      return does_item_have_enchantment(stack, Enchantments.DEPTH_STRIDER);
    }
    if(enchantment == Enchantments.DEPTH_STRIDER){
      return does_item_have_enchantment(stack, Enchantments.FROST_WALKER);
    }
    if(enchantment == Enchantments.ALL_DAMAGE_PROTECTION){
      return does_item_have_enchantment(stack, Enchantments.BLAST_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.FIRE_PROTECTION);
    }
    if(enchantment == Enchantments.PROJECTILE_PROTECTION){
      return does_item_have_enchantment(stack, Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.BLAST_PROTECTION, Enchantments.FIRE_PROTECTION);
    }
    if(enchantment == Enchantments.BLAST_PROTECTION){
      return does_item_have_enchantment(stack, Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.FIRE_PROTECTION);
    }
    if(enchantment == Enchantments.FIRE_PROTECTION){
      return does_item_have_enchantment(stack, Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.BLAST_PROTECTION);
    }
    if(enchantment == Enchantments.MENDING){
      return does_item_have_enchantment(stack, Enchantments.INFINITY_ARROWS);
    }
    if(enchantment == Enchantments.INFINITY_ARROWS){
      return does_item_have_enchantment(stack, Enchantments.MENDING);
    }
    if(enchantment == Enchantments.BLOCK_FORTUNE){
      return does_item_have_enchantment(stack, Enchantments.SILK_TOUCH);
    }
    if(enchantment == Enchantments.SILK_TOUCH){
      return does_item_have_enchantment(stack, Enchantments.BLOCK_FORTUNE);
    }
    if(enchantment == Enchantments.SHARPNESS){
      return does_item_have_enchantment(stack, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS);
    }
    if(enchantment == Enchantments.SMITE){
      return does_item_have_enchantment(stack, Enchantments.SHARPNESS, Enchantments.BANE_OF_ARTHROPODS);
    }
    if(enchantment == Enchantments.BANE_OF_ARTHROPODS){
      return does_item_have_enchantment(stack, Enchantments.SHARPNESS, Enchantments.SMITE);
    }
    // Trident enchantments
    if(enchantment == Enchantments.CHANNELING){
      return does_item_have_enchantment(stack, Enchantments.RIPTIDE);
    }
    if(enchantment == Enchantments.LOYALTY){
      return does_item_have_enchantment(stack, Enchantments.RIPTIDE);
    }
    if(enchantment == Enchantments.RIPTIDE){
      return does_item_have_enchantment(stack, Enchantments.CHANNELING, Enchantments.LOYALTY);
    }
    // Crossbow enchantments
    if(enchantment == Enchantments.MULTISHOT){
      return does_item_have_enchantment(stack, Enchantments.PIERCING);
    }
    if(enchantment == Enchantments.PIERCING){
      return does_item_have_enchantment(stack, Enchantments.MULTISHOT);
    }
    return false;
  }

}
