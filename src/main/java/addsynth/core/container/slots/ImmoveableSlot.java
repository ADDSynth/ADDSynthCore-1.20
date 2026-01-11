package addsynth.core.container.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** This is a special slot that does not allow any item to be placed here,
 *  or allow its current item to be taken out. This is important for items
 *  that have an inventory saved within its NBT tag. The item MUST REMAIN
 *  in the slot it was opened from while the inventory is being accessed.
 */
public final class ImmoveableSlot extends Slot {

  public ImmoveableSlot(Container container, int index, int x, int y){
    super(container, index, x, y);
  }

  @Override
  public final boolean mayPlace(ItemStack stack){
    return false;
  }

  @Override
  public final boolean mayPickup(Player player){
    return false;
  }

  @Override
  public final boolean allowModification(Player player){
    return false;
  }

  @Override
  public final boolean isHighlightable(){
    return false;
  }

}
