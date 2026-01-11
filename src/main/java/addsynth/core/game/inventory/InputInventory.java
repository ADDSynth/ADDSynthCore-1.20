package addsynth.core.game.inventory;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import addsynth.core.util.player.PlayerUtil;
import addsynth.core.util.world.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/** This inventory only allows items to be inserted. It also has an Item filter to control
 *  which items are allowed. Pass null as the item filter to allow all items.
 */
public final class InputInventory extends CommonInventory {

  @Nonnull
  private final SlotData[] slot_data;

  /** Override this to specify your own way of determining whether an item is allowed in this inventory. */
  @Nonnull
  public BiFunction<Integer, ItemStack, Boolean> isItemStackValid;

  private InputInventory(final IInputInventory responder, @Nonnull final SlotData[] slots){
    super(responder, slots.length);
    this.slot_data = slots;
    isItemStackValid = (Integer slot, ItemStack stack) -> {
      return slot_data[slot].is_item_valid(stack);
    };
  }

  /** 
   * @deprecated Machines usually have small inventories that are filtered on a per-slot basis.
   *             All other uses of Input Inventory will NOT use per-slot data, and will use a global filter.
   *             Create a new Storage Inventory class?
   */
  @Deprecated
  public static final InputInventory create(final IInputInventory responder, final int number_of_slots){
    return number_of_slots > 0 ? new InputInventory(responder, SlotData.create_new_array(number_of_slots)) : null;
  }

  /**
   * @deprecated Machines usually have small inventories that are filtered on a per-slot basis.
   *             All other uses of Input Inventory will NOT use per-slot data, and will use a global filter.
   *             Create a new Storage Inventory class?
   */
  @Deprecated
  public static final InputInventory create(final IInputInventory responder, final int number_of_slots, final int maxStackSize){
    return number_of_slots > 0 ? new InputInventory(responder, SlotData.create_new_array(number_of_slots, maxStackSize)) : null;
  }

  /**
   * @deprecated Machines usually have small inventories that are filtered on a per-slot basis.
   *             All other uses of Input Inventory will NOT use per-slot data, and will use a global filter.
   *             Create a new Storage Inventory class?
   */
  @Deprecated
  public static final InputInventory create(final IInputInventory responder, final int number_of_slots, final Predicate<ItemStack> filter){
    return number_of_slots > 0 ? new InputInventory(responder, SlotData.create_new_array(number_of_slots, filter)) : null;
  }

  public static final InputInventory create(final IInputInventory responder, final SlotData slotData){
    return new InputInventory(responder, new SlotData[]{slotData});
  }

  public static final InputInventory create(final IInputInventory responder, final SlotData[] slots){
    return slots != null ? (slots.length > 0 ? new InputInventory(responder, slots) : null) : null;
  }

  @Override
  public final boolean isItemValid(final int slot, final @NotNull ItemStack stack){
    if(isItemStackValid.apply(slot, stack)){
      return is_valid_slot(slot);
    }
    return false;
  }

  @Override
  @NotNull
  public ItemStack extractItem(int slot, int amount, boolean simulate){
    if(is_valid_slot(slot)){
      return super.extractItem(slot, amount, simulate);
    }
    return ItemStack.EMPTY;
  }

  /**
   * Decreases the ItemStack count in the slot by 1.
   * @param slot
   */
  public final void decrease(final int slot){
    decrease(slot, 1);
  }

  /**
   * Decreases the ItemStack count in the slot you want by the amount that you want.
   * @param slot
   * @param amount
   */
  public final void decrease(final int slot, final int amount){
    if(is_valid_slot(slot)){
      if(stacks.get(slot).isEmpty() == false){
        stacks.get(slot).shrink(amount);
      }
    }
  }

  /** Decreases all input slots by 1. */
  public final void decrease_input(){
    decrease_input(1);
  }

  /** Decreases all input slots by the amount that you specify. */
  public final void decrease_input(final int amount){
    int i;
    for(i = 0; i < stacks.size(); i++){
      if(stacks.get(i).isEmpty() == false){
        stacks.get(i).shrink(amount);
      }
    }
  }

  @Override
  public final int getSlotLimit(final int slot){
    return is_valid_slot(slot) ? slot_data[slot].stack_limit : 0;
  }

  @Override
  public final void save(final CompoundTag nbt){
    nbt.put("InputInventory", serializeNBT());
  }

  @Override
  public final void load(final CompoundTag nbt){
    deserializeNBT(nbt.getCompound("InputInventory"));
  }

  /** Checks all slots in this inventory and returns invalid ItemStacks to the player. */
  public final void ejectInvalidItems(final Player player){
    final ArrayList<ItemStack> ejected_itemStacks = getInvalidItemStacks();
    for(ItemStack stack : ejected_itemStacks){
      PlayerUtil.add_to_player_inventory(player, stack);
    }
  }

  /** Checks all slots in this inventory, removes invalid ItemStacks that don't
   *  match the filter, and spawns them in the world. */
  public final void ejectInvalidItems(final Level world, final BlockPos pos){
    final ArrayList<ItemStack> ejected_itemStacks = getInvalidItemStacks();
    for(ItemStack stack : ejected_itemStacks){
      WorldUtil.spawnItemStack(world, pos, stack, false);
    }
  }

  private final ArrayList<ItemStack> getInvalidItemStacks(){
    final int size = stacks.size();
    final ArrayList<ItemStack> ejected_itemStacks = new ArrayList<>(size);
    int i;
    for(i = 0; i < size; i++){
      if(isItemStackValid.apply(i, getStackInSlot(i)) == false){
        ejected_itemStacks.add(extractItemStack(i));
      }
    }
    return ejected_itemStacks;
  }

}
