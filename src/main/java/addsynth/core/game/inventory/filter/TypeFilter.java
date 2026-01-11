package addsynth.core.game.inventory.filter;

import java.util.function.Predicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Uses object types to use as an Item filter. */
public final class TypeFilter implements Predicate<ItemStack> {

  private final Class<? extends Item>[] filter;

  @SafeVarargs
  public TypeFilter(final Class<? extends Item> ... input){
    this.filter = input;
  }

  @Override
  public final boolean test(final ItemStack stack){
    final Item item = stack.getItem();
    for(final Class<? extends Item> type : filter){
      if(type.isInstance(item)){
        return true;
      }
    }
    return false;
  }

}
