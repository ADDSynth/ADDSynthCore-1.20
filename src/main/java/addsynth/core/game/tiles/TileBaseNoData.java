package addsynth.core.game.tiles;

import addsynth.core.block_network.BlockNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import net.minecraftforge.network.simple.SimpleChannel;

/** This DOES NOT SAVE any data to the world. It only syncs data from the
 *  server to the client. If you're sending a large amount of data, or are
 *  sending every tick, then it's actually recommended you create your own
 *  network message and send using {@link SimpleChannel#send }.
 * @author ADDSynth
 */
public abstract class TileBaseNoData extends TileAbstractBase {
// TODO: I'm fairly certain this is no longer necessary, and probably doesn't work as intended. Merge both back into a single class.

  public TileBaseNoData(final BlockEntityType type, BlockPos position, BlockState blockstate){
    super(type, position, blockstate);
  }

  @Override
  public final ClientboundBlockEntityDataPacket getUpdatePacket(){
    return ClientboundBlockEntityDataPacket.create(this);
  }

  /** This is called when this Block Entity receives a Client-bound Update Packet.
   *  Defined in {@link IForgeBlockEntity#onDataPacket}. Forge's interface defaults
   *  to calling the {@link #load} method as well. */
  @Override
  public final void onDataPacket(final Connection net, final ClientboundBlockEntityDataPacket pkt){
    final CompoundTag compoundtag = pkt.getTag();
    if(compoundtag != null){
      load(compoundtag);
    }
  }

  /** <p>Helper method to send TileEntity changes to the client.</p>
   *  <p>This should only be called on the server side and should be called when any data changes.
   *     For complex TileEntities that likely has data that changes every tick, we actually recommend
   *     setting a boolean variable to <code>true</code> when any data changes, then check that
   *     boolean variable at the end of the <code>tick()</code> method and call update_data().</p>
   *  <p>For TileEntities which are a part of a {@link BlockNetwork} it is required to override
   *     this so that you instead update the BlockNetwork which then updates each TileEntity manually.</p>
   */
  @SuppressWarnings("null")
  @Override
  public void update_data(){
    if(level != null){
      final BlockState blockstate = getBlockState();
      level.sendBlockUpdated(worldPosition, blockstate, blockstate, Block.UPDATE_ALL);
    }
  }

}
