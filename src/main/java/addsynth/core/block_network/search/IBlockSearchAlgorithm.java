package addsynth.core.block_network.search;

import java.util.HashSet;
import addsynth.core.block_network.CustomSearch;
import addsynth.core.block_network.node.BlockEntityNode;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface IBlockSearchAlgorithm {

  public HashSet<BlockEntityNode> find_blocks(BlockPos from, ServerLevel world, CustomSearch consumer);

}
