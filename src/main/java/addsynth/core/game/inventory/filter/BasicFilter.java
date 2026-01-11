package addsynth.core.game.inventory.filter;

import java.util.function.Predicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

/** Allows you to pass in any number of {@code RegistryObject<Item>s} to use as a filter. */
public final class BasicFilter implements Predicate<ItemStack> {

  private final RegistryObject<Item>[] filter;

  @SafeVarargs
  public BasicFilter(final RegistryObject<Item> ... input){
    this.filter = input;
  }

  @Override
  public final boolean test(final ItemStack stack){
    final Item item = stack.getItem();
    for(final RegistryObject<Item> holder : filter){
      if(holder.isPresent()){
        if(holder.get() == item){
          return true;
        }
      }
    }
    return false;
  }

}
