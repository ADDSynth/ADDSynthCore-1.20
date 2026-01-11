package addsynth.core.container;

import addsynth.core.container.slots.ImmoveableSlot;
import addsynth.core.container.slots.InputSlot;
import addsynth.core.container.slots.OutputSlot;
import addsynth.core.game.inventory.IInputInventory;
import addsynth.core.game.inventory.IOutputInventory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractContainer extends AbstractContainerMenu {

  /** Be warned! When accessing the Container from the Gui, this is the {@link AbstractClientPlayer}! */
  public final Player player;

  public AbstractContainer(final MenuType type, final int id, final Inventory player_inventory){
    super(type, id);
    player = player_inventory.player;
  }

  // New in 1.14, thanks again to this guy: https://github.com/Cadiboo/Example-Mod/blob/1.14.4/src/main/java/io/github/cadiboo/examplemod/container/ElectricFurnaceContainer.java
  public AbstractContainer(final MenuType type, final int id, final Inventory player_inventory, final FriendlyByteBuf data){
    super(type, id);
    player = player_inventory.player;
  }

  protected final void make_player_inventory(Inventory player_inventory){
    make_player_inventory(player_inventory, 8, 84);
  }

  protected final void make_player_inventory(Inventory player_inventory, int x, int y){
    int i;
    int j;
    for(j = 0; j < 3; j++){
      for(i = 0; i < 9; i++){
        addSlot(new Slot(player_inventory, i + 9 + (j*9), x + (i*18), y + (j*18)));
      }
    }
    for(i = 0; i < 9; i++){
      addSlot(new Slot(player_inventory, i, x + (i*18), y + 58));
    }  
  }

  /** Make player inventory slots, but with the Selected Slot as an {@link ImmoveableSlot}.
   *  This is useful for {@link ItemInventory ItemInventories} where you right-click an
   *  item to open its inventory. The item MUST STAY IN THE SLOT while the inventory is
   *  being accessed, because the inventory changes the item's NBT data when it is changed.
   * @param player_inventory
   */
  protected final void make_immoveable_selected_player_inventory(Inventory player_inventory){
    make_immoveable_selected_inventory(player_inventory, 8, 84);
  }

  /** Make player inventory slots, but with the Selected Slot as an {@link ImmoveableSlot}.
   *  This is useful for {@link ItemInventory ItemInventories} where you right-click an
   *  item to open its inventory. The item MUST STAY IN THE SLOT while the inventory is
   *  being accessed, because the inventory changes the item's NBT data when it is changed.
   * @param player_inventory
   */
  protected final void make_immoveable_selected_inventory(Inventory player_inventory, int x, int y){
    int i;
    int j;
    for(j = 0; j < 3; j++){
      for(i = 0; i < 9; i++){
        addSlot(new Slot(player_inventory, i + 9 + (j*9), x + (i*18), y + (j*18)));
      }
    }
    final int selected = player_inventory.selected; // ID of selected slot in hotbar, 0-8.
    for(i = 0; i < 9; i++){
      if(i == selected){
        addSlot(new ImmoveableSlot(player_inventory, i, x + (i*18), y+58));
      }
      else{
        addSlot(new           Slot(player_inventory, i, x + (i*18), y+58));
      }
    }  
  }

  /**
   * This is a helper method that quickly adds {@link InputSlot InputSlots} in succession. All you need to
   * specify is the starting index, the position of the first slot, and how many rows and columns of slots.
   * Increments slot index from top to bottom, left to right.
   * @param tile
   * @param index Starting Index of first slot
   * @param x X Position of top-left slot (inside the slot border)
   * @param y Y Position of top-left slot (inside the slot border)
   * @param width
   * @param height
   */
  protected final void addInputSlots(final IInputInventory tile, int index, int x, int y, int width, int height){
    int i, j;
    for(j = 0; j < height; j++){
      for(i = 0; i < width; i++){
        addSlot(new InputSlot(tile, index, x + (i * 18), y + (j * 18)));
        index++;
      }
    }
  }

  /**
   * This is a helper method that quickly adds {@link OutputSlot OutputSlots} in succession. All you need to
   * specify is the starting index, the position of the first slot, and how many rows and columns of slots.
   * Increments slot index from top to bottom, left to right.
   * @param tile
   * @param index Starting Index of first slot
   * @param x X Position of top-left slot (inside the slot border)
   * @param y Y Position of top-left slot (inside the slot border)
   * @param width 
   * @param height
   */
  protected final void addOutputSlots(final IOutputInventory tile, int index, int x, int y, int width, int height){
    int i, j;
    for(j = 0; j < height; j++){
      for(i = 0; i < width; i++){
        addSlot(new OutputSlot(tile, index, x + (i * 18), y + (j * 18)));
        index++;
      }
    }
  }

  /**
   * I've overridden the default behaviour of this method. This occurs when the user trys to
   * Shift-click an item in the inventory. Default behaviour is to return the same item that was
   * clicked, so it thinks nothing changed, so it tries again? Why try again? Why did Mojang
   * make this the default behaviour? -_-  Override this method in derived classes to specify
   * your own Shift-click behaviour, using that container's slots.
   */
   // http://www.minecraftforge.net/forum/topic/42322-1102-inventory-gui-shift-clicking/
   // https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/mapping-and-modding-tutorials/1571051-custom-container-how-to-properly-override-shift
  @Override
  public ItemStack quickMoveStack(Player playerIn, int index){
    return ItemStack.EMPTY;
  }

  /*
  protected final ItemStack default_transfer(final int index, final int number_of_input_slots){
    TODO: Add Shift-click support.

    final int player_inventory_max = 36;
    ItemStack stack = ItemStack.EMPTY;
    final Slot slot = getSlot(index);
    if(slot != null){
      if(slot.getHasStack()){
        ItemStack stack2 = slot.getStack();
        stack = stack2.copy();
        final boolean result;
        if(index < player_inventory_max){
          result = mergeItemStack(stack2, player_inventory_max, player_inventory_max + number_of_input_slots, true);
        }
        else{
          result = mergeItemStack(stack2, 0, player_inventory_max, false);
        }
        if(result == false){ return ItemStack.EMPTY; }
      }
    }
    return stack;
  }
    */

}
