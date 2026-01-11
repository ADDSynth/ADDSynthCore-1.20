package addsynth.core.util.game.tileentity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickingTileEntity {

  /** No need to check if this is run on the server side.
   *  This is gaurenteed to run only on the server. */
  public void serverTick(ServerLevel level, BlockState blockstate);

}
