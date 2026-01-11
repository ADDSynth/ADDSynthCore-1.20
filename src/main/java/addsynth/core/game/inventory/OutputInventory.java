package addsynth.core.game.inventory;

import net.minecraft.nbt.CompoundTag;

public final class OutputInventory extends CommonInventory {

  private OutputInventory(final IOutputInventory responder, final int output_slots){
    super(responder, output_slots);
  }

  public static final OutputInventory create(final IOutputInventory responder, final int number_of_slots){
    return number_of_slots > 0 ? new OutputInventory(responder, number_of_slots) : null;
  }

  @Override
  public final void save(final CompoundTag nbt){
    nbt.put("OutputInventory", serializeNBT());
  }

  @Override
  public final void load(final CompoundTag nbt){
    deserializeNBT(nbt.getCompound("OutputInventory"));
  }

}
