package addsynth.core.game.tiles;

import java.util.function.Predicate;
import addsynth.core.game.inventory.IInputInventory;
import addsynth.core.game.inventory.InputInventory;
import addsynth.core.game.inventory.InventoryUtil;
import addsynth.core.game.inventory.SlotData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This is a TileEntity that only has an Input Inventory, thus it also utilizes
 *  an Item filter and prevents machines from taking items out.
 * @author ADDSynth
 */
public abstract class TileStorageMachine extends TileBase implements IInputInventory {

  protected final InputInventory input_inventory;

  public TileStorageMachine(final BlockEntityType type, BlockPos position, BlockState blockstate, final SlotData[] slots){
    super(type, position, blockstate);
    this.input_inventory = InputInventory.create(this, slots);
  }

  public TileStorageMachine(final BlockEntityType type, BlockPos position, BlockState blockstate, final int input_slots, final Predicate<ItemStack> filter){
    super(type, position, blockstate);
    this.input_inventory = InputInventory.create(this, input_slots, filter);
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    if(input_inventory != null){input_inventory.load(nbt);}
  }

  @Override
  protected void saveAdditional(CompoundTag nbt){
    super.saveAdditional(nbt);
    if(input_inventory != null){input_inventory.save(nbt);}
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side){
    if(remove == false){
      if(capability == ForgeCapabilities.ITEM_HANDLER){
        return InventoryUtil.getInventoryCapability(input_inventory, null, side);
      }
      return super.getCapability(capability, side);
    }
    return LazyOptional.empty();
  }

  @Override
  public void onInventoryChanged(){
    update_data();
  }

  @Override
  public void drop_inventory(){
    InventoryUtil.drop_inventories(worldPosition, level, input_inventory);
  }

  @Override
  public final InputInventory getInputInventory(){
    return input_inventory;
  }

}
