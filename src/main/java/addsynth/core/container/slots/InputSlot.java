package addsynth.core.container.slots;

import addsynth.core.game.inventory.IInputInventory;
import addsynth.core.game.inventory.InputInventory;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

/** As of Minecraft 1.18, in Forge, I noticed the first thing {@link SlotItemHandler#mayPlace} and
 *  {@link ItemStackHandler#insertItem} did was call {@link ItemStackHandler#isItemValid}, which
 *  means I could override that and call my own {@link InputInventory#isItemStackValid} function.
 *  All filter tests now run in the inventory, and the slot doesn't have to do anything. Not sure
 *  why they both needed the filter, most likely because at one point the slots did not run the
 *  filter tests before inserting, or the Item was placed in the slot for a split second on the
 *  Client side, while the Server side would run the filter test and reject the item. */
public final class InputSlot extends SlotItemHandler {

  public InputSlot(IInputInventory tile, int index, int xPosition, int yPosition){
    super(tile.getInputInventory(), index, xPosition, yPosition);
  }

}
