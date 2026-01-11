package addsynth.core.game.inventory;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import addsynth.core.ADDSynthCore;
import addsynth.core.game.item.constants.ItemConstants;
import addsynth.core.recipe.RecipeCollection;
import addsynth.core.util.game.MinecraftUtility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

/** SlotData allows you to specify item filter and stack limit on a per-slot basis. All items
 *  are accepted by default. To restrict which items can go in this slot, specify your own Item
 *  filter during creation. Filters should be dynamically tested whenever possible, but if the
 *  number of items is too large, then you can use {@link RecipeCollection#getFilter(int)}.
 *  {@link InputInventory InputInventories} use this. */
// TODO: SlotData is immutable data. Convert to record.
public final class SlotData {

  private final Predicate<ItemStack> is_valid;
  public final int stack_limit;

  @Deprecated
  public final static SlotData[] create_new_array(final int number_of_slots){
    final SlotData[] data = new SlotData[number_of_slots];
    int i;
    for(i = 0; i < number_of_slots; i++){
      data[i] = new SlotData();
    }
    return data;
  }

  @Deprecated
  public final static SlotData[] create_new_array(final int number_of_slots, final int maxStackSize){
    final SlotData[] data = new SlotData[number_of_slots];
    int i;
    for(i = 0; i < number_of_slots; i++){
      data[i] = new SlotData(maxStackSize);
    }
    return data;
  }

  @Deprecated
  public final static SlotData[] create_new_array(final int number_of_slots, @Nonnull final Predicate<ItemStack> filter){
    final SlotData[] data = new SlotData[number_of_slots];
    int i;
    for(i = 0; i < number_of_slots; i++){
      data[i] = new SlotData(filter);
    }
    return data;
  }

  @Deprecated
  public final static SlotData[] create_new_array(int number_of_slots, int maxStackSize, @Nonnull Predicate<ItemStack> filter){
    final SlotData[] data = new SlotData[number_of_slots];
    int i;
    for(i = 0; i < number_of_slots; i++){
      data[i] = new SlotData(filter, maxStackSize);
    }
    return data;
  }

  /** This constructor uses the default Item filter, which allows everything. */
  public SlotData(){
    this.is_valid = (ItemStack) -> true;
    this.stack_limit = ItemConstants.stack_size;
  }

  /** This constructor uses the default Item filter, which allows everything. */
  public SlotData(final int slot_limit){
    this.is_valid = (ItemStack) -> true;
    this.stack_limit = Math.max(slot_limit, 1);
  }

  /** Use this constructor to restrict the slot to only hold a single VANILLA item. */
  public SlotData(@Nonnull final Item item){
    checkVanilla(item);
    this.is_valid = (ItemStack stack) -> stack.getItem() == item;
    this.stack_limit = ItemConstants.stack_size;
  }
  
  /** Use this constructor to restrict the slot to only hold a single VANILLA item. */
  public SlotData(@Nonnull final Item item, final int slot_limit){
    checkVanilla(item);
    this.is_valid = (ItemStack stack) -> stack.getItem() == item;
    this.stack_limit = Math.max(slot_limit, 1);
  }
  
  private static final void checkVanilla(final Item item){
    if(!MinecraftUtility.isVanilla(item)){
      ADDSynthCore.log.error("Forge will dynamically load and unload mods in the future, so modded Items SHOULD NOT BE CACHED. "+
                             "Use another constructor in "+SlotData.class.getSimpleName()+" for modded items.", new IllegalArgumentException());
      // FUTURE: Throw an exception in SlotData once Forge loads/unloads mods at runtime.
      // throw new IllegalArgumentException("Forge will dynamically load and unload mods in the future, so modded Items SHOULD NOT BE CACHED. "+
      //                                    "Use another constructor in "+SlotData.class.getSimpleName()+" for modded items.");
    }
  }

  /** Use this constructor to restrict the slot to only hold a single mod item. */
  public SlotData(@Nonnull final RegistryObject<Item> registry_object){
    this.is_valid = (ItemStack stack) -> stack.getItem() == registry_object.get();
    this.stack_limit = ItemConstants.stack_size;
  }
  
  /** Use this constructor to restrict the slot to only hold a single mod item. */
  public SlotData(@Nonnull final RegistryObject<Item> registry_object, final int slot_limit){
    this.is_valid = (ItemStack stack) -> stack.getItem() == registry_object.get();
    this.stack_limit = Math.max(slot_limit, 1);
  }

  /** Use this constructor to specify your own filter to control which Items are allowed in this slot. */
  public SlotData(@Nonnull final Predicate<ItemStack> filter){
    this.is_valid = filter;
    this.stack_limit = ItemConstants.stack_size;
  }

  /** Use this constructor to specify your own filter to control which Items are allowed in this slot. */
  public SlotData(@Nonnull final Predicate<ItemStack> filter, final int slot_limit){
    this.is_valid = filter;
    this.stack_limit = Math.max(slot_limit, 1);
  }

  public final boolean is_item_valid(@Nonnull final ItemStack stack){
    if(stack.isEmpty() == false){
      return is_valid.test(stack);
    }
    return false;
  }

}
