package addsynth.core.block_network;

import javax.annotation.Nullable;
import addsynth.core.block_network.node.Node;
import net.minecraft.server.level.ServerLevel;

/** Although blocks will become part of the BlockNetwork if they pass the
 *  Predicate test the search algorithm uses, any positions that are searched
 *  regardless of whether they will become part of the BlockNetwork will be
 *  passed to the BlockNetwork using this, so you can perform any operations
 *  on any adjacent blocks that we come across.
 */
@FunctionalInterface
public interface CustomSearch {

  public void accept(@Nullable Node previous, Node node, ServerLevel level);

}
