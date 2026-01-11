package addsynth.core.container.slots;

import addsynth.core.game.inventory.IInputInventory;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/** REPLICA: Although it isn't likely to change very often, this is a replica of {@link FurnaceFuelSlot}
 */
public final class FuelSlot extends SlotItemHandler {

  public FuelSlot(IInputInventory tile, int index, int xPosition, int yPosition){
    super(tile.getInputInventory(), index, xPosition, yPosition);
  }

  @Override
  public boolean mayPlace(@NotNull ItemStack stack){
    // Let's hold off on Lava Buckets for now.
    return AbstractFurnaceBlockEntity.isFuel(stack) && stack.getItem() != Items.LAVA_BUCKET;
  }

}
