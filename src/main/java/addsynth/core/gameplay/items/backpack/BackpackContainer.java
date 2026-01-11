package addsynth.core.gameplay.items.backpack;

import addsynth.core.container.ItemContainer;
import addsynth.core.game.inventory.ItemInventory;
import addsynth.core.gameplay.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class BackpackContainer extends ItemContainer {

  public BackpackContainer(int id, Inventory player_inventory, ItemInventory inventory){
    super(Containers.BACKPACK.get(), id, player_inventory, inventory);
    addInputSlots(inventory, 0, 8, 18, 9, 3);
    make_immoveable_selected_player_inventory(player_inventory);
  }

  public BackpackContainer(int id, Inventory player_inventory, FriendlyByteBuf buf){
    super(Containers.BACKPACK.get(), id, player_inventory, buf, Backpack::getInventory);
    addInputSlots(inventory, 0, 8, 18, 9, 3);
    make_immoveable_selected_player_inventory(player_inventory);
  }

}
