package addsynth.core.gameplay.items.backpack;

import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.GuiContainerBase;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BackpackGui extends GuiContainerBase<BackpackContainer> {

  public BackpackGui(BackpackContainer container, Inventory player_inventory, Component title){
    super(container, player_inventory, title, GuiReference.backpack_gui);
    // also hindsight, since the backpack gui is IDENTICAL to the single-chest gui, perhaps I could've directly used it instead of copying it.
  }

}
