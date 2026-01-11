package addsynth.core.gameplay.blocks.jukebox;

import addsynth.core.container.TileEntityContainer;
import addsynth.core.container.slots.InputSlot;
import addsynth.core.gameplay.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class JukeboxContainer extends TileEntityContainer<TileJukeboxPlayer> {

  public JukeboxContainer(int id, Inventory player_inventory, FriendlyByteBuf data){
    super(Containers.AUTO_JUKEBOX.get(), id, player_inventory, data);
    common_setup(player_inventory);
  }

  public JukeboxContainer(final int id, final Inventory player_inventory, final TileJukeboxPlayer tile){
    super(Containers.AUTO_JUKEBOX.get(), id, player_inventory, tile);
    common_setup(player_inventory);
  }

  private final void common_setup(Inventory player_inventory){
    make_player_inventory(player_inventory, 187, 68);
    final int xStart = 14;
    final int yStart = 90;
    int x, y, slot;
    for(y = 0; y < 3; y++){
      for(x = 0; x < 9; x++){
        slot = x + (y * 9);
        addSlot(new InputSlot(tile, slot, xStart + (x*18), yStart + (y*18)));
      }
    }
  }

}
