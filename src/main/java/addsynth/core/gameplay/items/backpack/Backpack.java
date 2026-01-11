package addsynth.core.gameplay.items.backpack;

import java.util.function.Predicate;
import addsynth.core.game.inventory.ItemInventory;
import addsynth.core.game.inventory.filter.BasicFilter;
import addsynth.core.gameplay.reference.Core;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Backpack extends Item {

  // This will ensure that you NEVER insert ANY backpack inside another backpack
  private static final Predicate<ItemStack> filter = new BasicFilter(Core.backpack).negate();

  public static final ItemInventory getInventory(ItemStack itemstack){
    return ItemInventory.of(itemstack, 27, filter, BackpackContainer::new);
  }

  public Backpack(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
    final ItemStack itemstack = player.getItemInHand(hand);
    if(level.isClientSide){
      player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER);
      return InteractionResultHolder.success(itemstack);
    }
    final ItemInventory inventory = getInventory(itemstack);
    inventory.openInventory((ServerPlayer)player);
    return InteractionResultHolder.consume(itemstack);
  }

}
