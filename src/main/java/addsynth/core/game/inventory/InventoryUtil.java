package addsynth.core.game.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public final class InventoryUtil {

  /**
   * Attempts to transfer the entire contents of the Input Slot to the Output Slot.<br>
   * Returns true if anything changed.
   * @param input_inventory
   * @param input_slot
   * @param output_inventory
   * @param output_slot
   * @return
   */
  public static final boolean transfer(CommonInventory input_inventory, int input_slot, CommonInventory output_inventory, int output_slot){
    if(input_inventory == output_inventory && input_slot == output_slot){
      return false;
    }
    ItemStack itemstack = input_inventory.getStackInSlot(input_slot);
    final int count = itemstack.getCount();
    if(itemstack.isEmpty()){
      return false;
    }
    itemstack = output_inventory.insertItem(output_slot, itemstack, false);
    input_inventory.setStackInSlot(input_slot, itemstack);
    return itemstack.getCount() != count;
  }

  /**
   * Attempts to transfer the specified amount of items from the Input Slot to the Output Slot.<br>
   * The items will NOT transfer if the amount specified do not fit in the output slot.<br>
   * Returns true if a transfer occured.
   * @param input_inventory
   * @param input_slot
   * @param output_inventory
   * @param output_slot
   * @param count
   * @return
   */
  public static final boolean transfer(CommonInventory input_inventory, int input_slot, CommonInventory output_inventory, int output_slot, int count){
    if(count == 0){
      return false;
    }
    if(input_inventory == output_inventory && input_slot == output_slot){
      return false;
    }
    final ItemStack itemstack = input_inventory.extractItem(input_slot, count, true);
    if(itemstack.isEmpty()){
      return false;
    }
    if(output_inventory.can_add(output_slot, itemstack)){
      output_inventory.add(output_slot, itemstack);
      input_inventory.getStackInSlot(input_slot).shrink(itemstack.getCount());
      return true;
    }
    return false;
  }

  /** <p>Used to safely return the Inventory Capability. Use this if your inventory allows bi-directional
   *  transfer of items because we return the inventory regardless of which side we're checking from.<br />
   *  <b>Remember:</b> ONLY USE THIS if you're checking for the
   *  {@link ForgeCapabilities#ITEM_HANDLER ITEM_HANDLER_CAPABILITY}.</p>
   *  <p>Use case:<br />
   *  <pre><code>
   *  &#64;Override
   *  public &lt;T&gt; LazyOptional&lt;T&gt; getCapability(&#64;NotNull Capability&lt;T&gt; capability, &#64;Nullable Direction direction){
   *    if(capability == ForgeCapabilities.ITEM_HANDLER){
   *      return InventoryUtil.getInventoryCapability(inventory);
   *    }
   *    return super.getCapability(capability, direction);
   *  }
   *  </code></pre></p>
   * @param <T>
   * @param inventory
   */
  public static final <T> LazyOptional<T> getInventoryCapability(final CommonInventory inventory){
    return inventory != null ? (LazyOptional.of(() -> inventory)).cast() : LazyOptional.empty();
  }

  /** <p>Used to return either the Input Inventory or Output Inventory depending on which side
   *  we're querying. Pass null to either inventory if your TileEntity doesn't have them.<br />
   *  <b>Remember</b> ONLY USE THIS if you're checking for the
   *  {@link ForgeCapabilities#ITEM_HANDLER ITEM_HANDLER_CAPABILITY}.</p>
   *  <p>Use case:<br />
   *  <pre><code>
   *  &#64;Override
   *  public &lt;T&gt; LazyOptional&lt;T&gt; getCapability(&#64;NotNull Capability&lt;T&gt; capability, &#64;Nullable Direction direction){
   *    if(capability == ForgeCapabilities.ITEM_HANDLER){
   *      return InventoryUtil.getInventoryCapability(input_inventory, output_inventory, direction);
   *    }
   *    return super.getCapability(capability, direction);
   *  }
   *  </code></pre></p>
   * @param <T>
   * @param input_inventory
   * @param output_inventory
   * @param facing
   */
  public static final <T> LazyOptional<T> getInventoryCapability
  (InputInventory input_inventory, OutputInventory output_inventory, Direction facing){
    if(facing != null){
      if(facing == Direction.DOWN){
        return output_inventory != null ? (LazyOptional.of(() -> output_inventory)).cast() : LazyOptional.empty();
      }
      return input_inventory != null ? (LazyOptional.of(() -> input_inventory)).cast() : LazyOptional.empty();
    }
    return LazyOptional.empty();
  }
    

  public static final void drop_inventories(final BlockPos pos, final Level world, final CommonInventory ... inventories){
    for(final CommonInventory inventory : inventories){
      if(inventory != null){
        inventory.drop_in_world(world, pos);
      }
    }
  }

  /** This returns the {@link ItemStackHandler} converted to an Inventory. However, the contents
   *  of the inventory SHOULD NOT be modified. This should only be used as input to the 
   *  {@link Recipe#matches(net.minecraft.world.Container, Level) Recipe.matches()}
   *  method to find a matching recipe.
   * @param handler
   */
  public static final SimpleContainer toInventory(final ItemStackHandler handler){
    return new SimpleContainer(getItemStacks(handler));
  }

  /** Returns an array of all ItemStacks in this Inventory. However, you SHOULD NOT modify
   *  these ItemStacks as the changes will be reflected in the inventory. This should only
   *  be used to help convert the {@link ItemStackHandler} to an array of ItemStacks for
   *  comparing against crafting recipes.
   * @param handler
   */
  public static final ItemStack[] getItemStacks(final ItemStackHandler handler){
    final int max = handler.getSlots();
    final ItemStack[] stacks = new ItemStack[max];
    int i;
    for(i = 0; i < max; i++){
      stacks[i] = handler.getStackInSlot(i);
    }
    return stacks;
  }

}
