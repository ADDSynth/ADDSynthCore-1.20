package addsynth.core.util.block;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;
import addsynth.core.ADDSynthCore;
import addsynth.core.block_network.node.Node;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

/** The REALLY basic block search algorithm. */
public class BlockSearch {

  /** Basically, instead of returning a list of BlockPositions that pass your predicate, this
   *  sort of skips that and only continues searching as long as your supplied function returns true.
   * @param start
   * @param level
   * @param predicate
   */
  public static final void forEach(final BlockPos start, final Level level, final Function<Node, Boolean> predicate){
    final HashSet<BlockPos> searched = new HashSet<>(500);
    searched.add(start);
    if(predicate.apply(new Node(start, level))){
      try{
        search(start, level, searched, predicate);
      }
      catch(StackOverflowError e){
        ADDSynthCore.log.fatal("Block Search looped forever! Someone's code is doing something they're not supposed to be doing! Hopefully not my code!", e);
      }
    }
  }

  /** Use this to run code for all the blocks we find, except for the one we start with.
   *  Search continues as long as your predicate returns true.
   * @param start
   * @param level
   * @param predicate
   */
  public static final boolean forEachAdjacent(final BlockPos start, final Level level, final Function<Node, Boolean> predicate){
    final HashSet<BlockPos> searched = new HashSet<>(500);
    searched.add(start);
    try{
      return search(start, level, searched, predicate);
    }
    catch(StackOverflowError e){
      ADDSynthCore.log.fatal("Block Search looped forever! Someone's code is doing something they're not supposed to be doing! Hopefully not my code!", e);
    }
    return false;
  }

  private static final boolean search(BlockPos from, Level level, HashSet<BlockPos> searched, Function<Node, Boolean> predicate){
    boolean found = false;
    BlockPos position;
    for(final Direction side : Direction.values()){
      position = from.relative(side);
      if(!searched.contains(position)){
        searched.add(position);
        if(predicate.apply(new Node(position, level))){
          found = true;
          search(position, level, searched, predicate);
        }
      }
    }
    return found;
  }

  /** The REALLY basic block search algorithm. All this does is return a list of BlockPositions
   *  that match the Predicate you specify.
   */
  public static final HashSet<BlockPos> find(final BlockPos start, final Level level, final Predicate<Node> predicate){
    final HashSet<BlockPos> searched = new HashSet<>(500);
    final HashSet<BlockPos> list = new HashSet<>(100);
    try{
      searched.add(start);
      if(predicate.test(new Node(start, level))){
        list.add(start);
        search(start, level, searched, list, predicate);
      }
    }
    catch(StackOverflowError e){
      ADDSynthCore.log.fatal("Block Search looped forever! Someone's code is doing something they're not supposed to be doing! Hopefully not my code!", e);
    }
    return list;
  }

  private static final void search(BlockPos from, Level level, HashSet<BlockPos> searched, HashSet<BlockPos> list, Predicate<Node> predicate){
    BlockPos position;
    for(final Direction side : Direction.values()){
      position = from.relative(side);
      if(!searched.contains(position)){
        searched.add(position);
        if(predicate.test(new Node(position, level))){
          list.add(position);
          search(position, level, searched, list, predicate);
        }
      }
    }
  }

}
