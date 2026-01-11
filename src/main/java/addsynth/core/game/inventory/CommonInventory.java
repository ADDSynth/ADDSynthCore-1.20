package addsynth.core.game.inventory;

import javax.annotation.Nullable;
import addsynth.core.ADDSynthCore;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for inventories in ADDSynth's Mods. Contains all common functionality.
 * This can also be used as a standard Inventory and allows machines to insert and extract items.
 * When an inventory changes, the changes must be saved to disk. So for this reason,
 * you must specify a {@link IInventoryUser} that responds to inventory changes.
 * @author ADDSynth
 */
public class CommonInventory extends ItemStackHandler {

  @Nullable
  private IInventoryResponder responder;

  protected CommonInventory(final IInventoryResponder responder, final int number_of_slots){
    super(number_of_slots);
    this.responder = responder;
  }

  public static CommonInventory create(final SlotData[] slots){
    return slots != null ? (slots.length > 0 ? new CommonInventory(null, slots.length) : null) : null;
  }
  
  public static CommonInventory create(final int number_of_slots){
    return number_of_slots > 0 ? new CommonInventory(null, number_of_slots) : null;
  }

  public static CommonInventory create(final IInventoryUser responder, final SlotData[] slots){
    return slots != null ? (slots.length > 0 ? new CommonInventory(responder, slots.length) : null) : null;
  }

  public static CommonInventory create(final IInventoryUser responder, final int number_of_slots){
    return number_of_slots > 0 ? new CommonInventory(responder, number_of_slots) : null;
  }

  /** Use this to set a custom responder to Inventory changes. This overrides the default behavior. */
  public final void setResponder(final IInventoryResponder responder){
    this.responder = responder;
  }

  @Override
  public final void setStackInSlot(final int slot, final @NotNull ItemStack stack){
    if(is_valid_slot(slot)){
      this.stacks.set(slot, stack);
      onContentsChanged(slot);
    }
  }

  /** This is useful for machines. Tests whether the input stack can fully be added to the slot. */
  public final boolean can_add(final int slot, @Nullable final ItemStack input_stack){
    if(input_stack == null){   return false; }
    if(input_stack.isEmpty()){ return false; }
    if(is_valid_slot(slot)){
      final ItemStack existing_stack = getStackInSlot(slot);
      if(existing_stack.isEmpty()){
        return true;
      }
      return ItemStack.isSameItem(existing_stack, input_stack) && existing_stack.getCount() + input_stack.getCount() <= getStackLimit(slot, existing_stack);
    }
    return false;
  }

  /** <p>Alias for {@link #insertItem(int, ItemStack, boolean)} with {@code simulate} set to false.
   *     Commonly used right after calling {@link #can_add(int, ItemStack)}. */
  public final void add(final int slot, final @NotNull ItemStack stack){
    insertItem(slot, stack, false);
  }

  /** Attempts to add the Itemstack to the inventory. Adds as much as possible, and respects slot restrictions.
   * @param itemstack
   * @return The remaining Itemstack that could not be added. Will be EMPTY if the entire Itemstack was added.
   */
  public final ItemStack add(ItemStack itemstack){
    if(itemstack.isEmpty()){
      return ItemStack.EMPTY;
    }
    final int slots = stacks.size();
    int i;
    for(i = 0; i < slots; i++){
      itemstack = insertItem(i, itemstack, false);
      if(itemstack.isEmpty()){
        return ItemStack.EMPTY;
      }
    }
    return itemstack;
  }

  /** Extracts the entire ItemStack from the slot. */
  public final ItemStack extractItemStack(final int slot){
    final ItemStack stack = getStackInSlot(slot);
    setStackInSlot(slot, ItemStack.EMPTY);
    return stack;
  }

  public final boolean isEmpty(){
    for(final ItemStack stack : stacks){
      if(stack.isEmpty() == false){
        return false;
      }
    }
    return true;
  }

  /** Sets all slots in the inventory to Empty. */
  public void setEmpty(){
    int i;
    for(i = 0; i < stacks.size(); i++){
      setStackInSlot(i, ItemStack.EMPTY);
    }
  }

  /** Returns all the ItemStacks in this Inventory. You should only use this
   *  to check if the items matches a crafting recipe. You can also use
   *  {@link InventoryUtil#toInventory(ItemStackHandler)} if the inventory
   *  is an {@link ItemStackHandler}.
   */
  public final ItemStack[] getItemStacks(){
    final int size = stacks.size();
    final ItemStack[] items = new ItemStack[size];
    int i;
    for(i = 0; i < size; i++){
      items[i] = stacks.get(i).copy();
    }
    return items;
  }

  public final void drop_in_world(final Level world, final BlockPos pos){
    for(final ItemStack stack : stacks){
      if(stack.isEmpty() == false){
        Block.popResource(world, pos, stack);
      }
    }
  }

  @Override
  public boolean isItemValid(final int slot, final @NotNull ItemStack stack){
    return is_valid_slot(slot);
  }

  protected final boolean is_valid_slot(final int slot){
    final int size = stacks.size();
    if(slot >= 0 && slot < size){
      return true;
    }
    // if(size == 0){
    //   ADDSynthCore.log.error("Invalid slot: "+slot+", this ItemStackHandler does not have any slots.");
    //   return false;
    // }
    if(size == 1){
      ADDSynthCore.log.error("Invalid slot: "+slot+", there is only slot 0.", new IndexOutOfBoundsException());
      return false;
    }
    ADDSynthCore.log.error("Invalid slot: "+slot+", only 0 to "+Integer.toString(size - 1)+" allowed.", new IndexOutOfBoundsException());
    return false;
  }

  @Override
  @Deprecated
  protected final void validateSlotIndex(int slot){
    // in order to crash properly, this method can't do anything.
  }

  public void save(final CompoundTag nbt){
    nbt.put("Inventory", serializeNBT());
  }

  public void load(final CompoundTag nbt){
    deserializeNBT(nbt.getCompound("Inventory"));
  }

  public final void save(final CompoundTag nbt, final String name){
    nbt.put(name, serializeNBT());
  }
  
  public final void load(final CompoundTag nbt, final String name){
    deserializeNBT(nbt.getCompound(name));
  }

  @Override
  protected final void onContentsChanged(final int slot){
    if(responder != null){
      responder.onInventoryChanged();
    }
  }

}
