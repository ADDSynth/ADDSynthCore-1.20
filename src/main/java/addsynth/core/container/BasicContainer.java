package addsynth.core.container;

import addsynth.core.game.inventory.IStorageInventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Use this Container if your TileEntity uses a standard Inventory that allows
 *  items to be inserted and extracted.
 * @param <T>
 */
@Deprecated // REMOVE addsynth.core.container.BasicContainer in 2030. Merge AbstractContainer and TileEntityContainer? No?
public abstract class BasicContainer<T extends BlockEntity & IStorageInventory> extends TileEntityContainer<T> {

  public BasicContainer(final MenuType type, final int id, final Inventory player_inventory, final T tile){
    super(type, id, player_inventory, tile);
  }

  public BasicContainer(final MenuType type, final int id, final Inventory player_inventory, final FriendlyByteBuf data){
    super(type, id, player_inventory, data);
  }

}
