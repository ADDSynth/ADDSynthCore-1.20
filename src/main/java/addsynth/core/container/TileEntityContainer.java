package addsynth.core.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Use this Container only if your TileEntity doesn't have any inventories.
 *  If it does, you'll have to use one of the other Container classes.
 * @param <T>
 */
public abstract class TileEntityContainer<T extends BlockEntity> extends AbstractContainer {

  protected final T tile;

  public TileEntityContainer(final MenuType type, final int id, final Inventory player_inventory, final T tile){
    super(type, id, player_inventory);
    this.tile = tile;
  }

  @SuppressWarnings({ "unchecked", "resource" })
  public TileEntityContainer(final MenuType type, final int id, final Inventory player_inventory, final FriendlyByteBuf data){
    super(type, id, player_inventory, data);
    this.tile = (T)(player_inventory.player.level().getBlockEntity(data.readBlockPos()));
  }

  @Override
  public boolean stillValid(final Player player){
    return stillValid(ContainerLevelAccess.NULL, player, tile.getBlockState().getBlock());
  }

  public final T getTileEntity(){
    return tile;
  }

}
