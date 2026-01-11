package addsynth.core.container;

import addsynth.core.game.inventory.ItemInventory;
import net.minecraft.world.entity.player.Inventory;

@FunctionalInterface
public interface ItemContainerSupplier {

  ItemContainer get(int id, Inventory player_inventory, ItemInventory item_inventory);

}
