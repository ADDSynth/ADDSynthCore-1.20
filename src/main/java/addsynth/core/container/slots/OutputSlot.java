package addsynth.core.container.slots;

import addsynth.core.game.inventory.IOutputInventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public final class OutputSlot extends SlotItemHandler {

  public OutputSlot(IOutputInventory tile, int index, int xPosition, int yPosition) {
    super(tile.getOutputInventory(), index, xPosition, yPosition);
  }

  @Override
  public final boolean mayPlace(@NotNull final ItemStack stack){
    return false;
  }

  // NOTE: warning. it's possible this is run on client and server!
  /** @see ResultSlot#onTake */
  @Override
  public final void onTake(final Player player, final ItemStack stack){
    ForgeEventFactory.firePlayerCraftingEvent(player, stack, new SimpleContainer(0)); // TODO: This wants the crafting matrix, be nice and give it to them.
    setChanged();
  }

}
