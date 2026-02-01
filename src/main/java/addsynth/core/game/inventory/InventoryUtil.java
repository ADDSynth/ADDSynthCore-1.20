package addsynth.core.game.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public final class InventoryUtil {

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

  /** Transfers as many items as possible from the inventory into the player's inventory.
   * @param from
   * @param player_inventory
   */
  public static final void transfer(CommonInventory from, Inventory player_inventory){
    int i;
    final int slots = from.getSlots();
    ItemStack stack;
    for(i = 0; i < slots; i++){
      stack = from.getStackInSlot(i);
      if(!stack.isEmpty()){
        player_inventory.add(stack); // automatically deducts count from ItemStack.
      }
    }
  }

  /**
   * Attempts to transfers items from the player's inventory to the output inventory.<br>
   * Will transfer items from the inventory, hotbar, and offhand slot.
   * @param player_inventory
   * @param output
   */
  public static final void transfer(Inventory player_inventory, CommonInventory output){
    int x, y;
    final int max = player_inventory.items.size();
    final int slots = output.getSlots();
    ItemStack stack;
    for(y = 0; y < max; y++){
      stack = player_inventory.items.get(y);
      if(!stack.isEmpty()){
        for(x = 0; x < slots && !stack.isEmpty(); x++){
          stack = output.insertItem(x, stack, false);
        }
        player_inventory.items.set(y, stack);
      }
    }
    // offhand slot
    stack = player_inventory.offhand.get(0);
    if(!stack.isEmpty()){
      for(x = 0; x < slots && !stack.isEmpty(); x++){
        stack = output.insertItem(x, stack, false);
      }
      player_inventory.offhand.set(0, stack);
    }
  }

  /**
   * Attempts to transfer the entire contents of the inventory to the output inventory.
   * @param from
   * @param to
   */
  public static final void transfer(CommonInventory from, CommonInventory to){
    int x, y;
    final int maxX = to.getSlots();
    final int maxY = from.getSlots();
    ItemStack stack;
    for(y = 0; y < maxY; y++){
      stack = from.getStackInSlot(y);
      if(!stack.isEmpty()){
        for(x = 0; x < maxX && !stack.isEmpty(); x++){
          stack = to.insertItem(x, stack, false);
        }
        from.setStackInSlot(y, stack);
      }
    }
  }

  /**
   * Attempts to transfer the entire ItemStack from the Source Slot to the Destination Slot.<br>
   * Returns true if anything changed.
   * @param from_inventory
   * @param from_slot
   * @param to_inventory
   * @param to_slot
   * @return true if anything changed.
   */
  public static final boolean transfer(CommonInventory from_inventory, int from_slot, CommonInventory to_inventory, int to_slot){
    if(from_inventory == to_inventory && from_slot == to_slot){
      return false;
    }
    ItemStack itemstack = from_inventory.getStackInSlot(from_slot);
    if(itemstack.isEmpty()){
      return false;
    }
    final int count = itemstack.getCount();
    itemstack = to_inventory.insertItem(to_slot, itemstack, false);
    from_inventory.setStackInSlot(from_slot, itemstack);
    return itemstack.getCount() != count;
  }

  /**
   * Attempts to transfer the specified amount of items from the Source Slot to the Destination Slot.<br>
   * The items will NOT transfer if the amount specified does not fit in the destination slot.<br>
   * Returns true if a transfer occured.
   * @param from_inventory
   * @param from_slot
   * @param to_inventory
   * @param to_slot
   * @param count
   * @return true if a transfer occured.
   */
  public static final boolean transfer(CommonInventory from_inventory, int from_slot, CommonInventory to_inventory, int to_slot, int count){
    if(count == 0){
      return false;
    }
    if(from_inventory == to_inventory && from_slot == to_slot){
      return false;
    }
    final ItemStack itemstack = from_inventory.extractItem(from_slot, count, true);
    if(itemstack.isEmpty()){
      return false;
    }
    if(to_inventory.can_add(to_slot, itemstack)){
      to_inventory.add(to_slot, itemstack);
      from_inventory.getStackInSlot(from_slot).shrink(itemstack.getCount());
      return true;
    }
    return false;
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
