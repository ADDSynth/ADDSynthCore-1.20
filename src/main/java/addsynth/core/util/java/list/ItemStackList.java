package addsynth.core.util.java.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** This class was created in order to keep an array of {@code ArrayList<ItemStack>}
 *  and get around any type safety warnings.
 *  @since Overpowered Technology version 1.5.1, September 14, 2023 (Filter Update)
 */
public final class ItemStackList extends ArrayList<ItemStack> implements Predicate<ItemStack> {

  public ItemStackList(){
    super();
  }
  
  public ItemStackList(final int length){
    super(length);
  }
  
  public ItemStackList(final Collection<ItemStack> collection){
    super(collection);
  }

  @Override
  public final boolean test(final ItemStack stack){
    final Item item = stack.getItem();
    for(final ItemStack s : this){
      if(s.getItem() == item){
        return true;
      }
    }
    return false;
  }

}
