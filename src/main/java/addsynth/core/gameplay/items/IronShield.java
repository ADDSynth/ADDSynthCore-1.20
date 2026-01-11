package addsynth.core.gameplay.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;

public class IronShield extends ShieldItem {

  public IronShield(){
    super(new Item.Properties().durability(1024));
  }

  @Override
  public boolean isValidRepairItem(ItemStack material, ItemStack shiled){
    return material.is(Items.IRON_INGOT);
  }

}
