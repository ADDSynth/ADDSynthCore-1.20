package addsynth.core.container;

import java.util.function.Function;
import addsynth.core.container.slots.InputSlot;
import addsynth.core.game.inventory.IInputInventory;
import addsynth.core.game.inventory.ItemInventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

/** <p>Since our {@link AbstractContainer} doesn't add any slots by default, you'll
 *  need to create your own ItemContainer class and extend from this class. And since
 *  the {@link InputSlot InputSlots} need access to an {@link IInputInventory}, we
 *  simplify this for you by saving a reference to the ItemInventory. You must have
 *  an ItemInventory as a parameter in the constructor. However, the second constructor,
 *  the one that has the {@link FriendlyByteBuf} is called by Forge internally, so we
 *  can't pass in our ItemInventory. We could encode and decode our inventory to/from
 *  the FriendlyByteBuf, but I've thought of a better way. Since we know this is an item
 *  inventory, and the inventory can only be opened from the Item in the player's hand,
 *  we can create the Inventory on-the-fly, by getting the ItemStack currently selected
 *  by the player and calling a static {@code MyItem.getInventory(ItemStack)} function
 *  to create the ItemInventory with the settings the inventory needs all in one place.
 *  <p>Here's an example:
 *  <pre><code>
 *  public class MyItemContainer extends ItemContainer {
 *  
 *    public MyItemContainer(int ContainerID, Inventory player_inventory, ItemInventory inventory){
 *      super(MyContainerType.get(), containerID, player_inventory, inventory);
 *      ... add Slots
 *    }
 *  
 *    public MyItemContainer(int ContainerID, Inventory player_inventory, FriendlyByteBuf buf){
 *      super(MyContainerType.get(), containerID, player_inventory, Myitem::getInventory)
 *      ... add Slots
 *    }
 *  
 *  }
 *  </code></pre>
 */
public abstract class ItemContainer extends AbstractContainer {

  protected final ItemInventory inventory;

  public ItemContainer(MenuType type, int id, Inventory player_inventory, ItemInventory inventory){
    super(type, id, player_inventory);
    this.inventory = inventory;
  }

  public ItemContainer(MenuType type, int id, Inventory player_inventory, FriendlyByteBuf buf, Function<ItemStack, ItemInventory> inventory_constructor){
    super(type, id, player_inventory, buf);
    inventory = inventory_constructor.apply(player_inventory.getSelected());
  }

  @Override
  public boolean stillValid(Player player){
    return true;
  }

}
