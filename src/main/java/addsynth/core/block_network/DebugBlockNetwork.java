package addsynth.core.block_network;

import addsynth.core.ADDSynthCore;
import addsynth.core.gameplay.config.DebugSettings;
import addsynth.core.util.java.StringUtil;
import net.minecraft.core.BlockPos;

public final class DebugBlockNetwork {

  private static final String getId(final BlockNetwork network){
    return StringUtil.build('(', Integer.toHexString(network.hashCode()), ')');
  }

  private static final String networkName(final BlockNetwork network){
    return StringUtil.build(network.getClass().getSimpleName(), getId(network));
  }

  private static final String position(final BlockPos pos){
    return "position "+StringUtil.print(pos);
  }

  static final void CREATED(final BlockNetwork network, final BlockPos pos){
    debug(StringUtil.singleNoun(network.getClass().getSimpleName(), true)+" was created at "+position(pos)+" with ID "+getId(network)+".");
  }
  
  static final void DATA_LOADED(final BlockNetwork network, final BlockPos pos){
    debug(networkName(network)+" loaded BlockNetwork data from TileEntity at "+position(pos)+".");
  }
  
  static final void JOINED(final BlockPos tile_position, final BlockNetwork network, final BlockPos network_position){
    final String network_name = network.getClass().getSimpleName();
    debug(
      "The TileEntity at "+position(tile_position)+" found the first adjacent "+network_name+" with ID "+getId(network)+
      " at "+position(network_position)+" and has joined it. It's possible a new TileEntity being placed might "+
      "be joining two or more BlockNetworks together. All other adjacent "+network_name+"s will be overwritten."
    );
  }

  // static final void OVERWRITTEN(final BlockNetwork network, final BlockPos pos){
  //   debug(networkName(network)+" at "+position(pos)+" was overwritten.");
  // }

  static final void SPLIT(final BlockPos pos, final BlockNetwork network){
    debug(
      "The TileEntity at "+position(pos)+" is no longer part of "+networkName(network)+
      " or any adjacent BlockNetworks already created, and will be reset to a new BlockNetwork."
    );
  }

  static final void UPDATED(final BlockNetwork network, final BlockPos pos){
    debug(networkName(network)+" started a new Update at "+position(pos)+".");
  }
  
  static final void BLOCKS_CHANGED(final BlockNetwork network, final int old_size, final int new_size){
    final int change = Math.abs(new_size - old_size);
    if(change != 0){
      final String number_of_blocks = change == 1 ? "1 block was " : change+" blocks were ";
      final String action = new_size > old_size ? "added to " : "removed from ";
      final String new_size_blocks = new_size == 1 ? "1 block." : new_size+" blocks.";
      debug(StringUtil.build(number_of_blocks, action, networkName(network), " and now contains ", new_size_blocks));
    }
  }
  
  static final void TILE_REMOVED(final BlockNetwork network, final BlockPos pos){
    debug(
      "A TileEntity was removed from "+networkName(network)+" at "+position(pos)+". "+
      "This may create separate BlockNetworks. The first adjacent TileEntity will be "+
      "updated, while all other adjacent TileEntities will have a new BlockNetwork "+
      "created at that position if they've been split from the original BlockNetwork."
    );
  }

  public enum DEBUG_LEVEL {
    Info, Debug, Trace
  }

  private static final void debug(final String string){
    if(DebugSettings.debug_block_networks.get()){
      switch(DebugSettings.block_network_debug_level.get()){
      case Info:  ADDSynthCore.log.info(string);  break;
      case Debug: ADDSynthCore.log.debug(string); break;
      case Trace: ADDSynthCore.log.trace(string); break;
      }
    }
  }

}
