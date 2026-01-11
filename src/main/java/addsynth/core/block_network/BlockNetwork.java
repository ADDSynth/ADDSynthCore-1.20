package addsynth.core.block_network;

import java.util.Collection;
import java.util.function.Function;
import javax.annotation.Nullable;
import addsynth.core.ADDSynthCore;
import addsynth.core.block_network.node.AbstractNode;
import addsynth.core.block_network.node.Node;
import addsynth.core.block_network.search.IBlockSearchAlgorithm;
import addsynth.core.block_network.search.StandardBlockSearch;
import addsynth.core.game.tiles.TileBase;
import addsynth.core.util.game.tileentity.ITickingTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * <p>A Block Network is a collection of TileEntities that should act as a single entity and/or share
 * data between them. To stay in-sync it needs to be updated whenever a TileEntity is added or removed.
 * 
 * <p>Here are the steps to creating your own BlockNetwork:
 * 
 * <p><b>Step 1:</b> Extend this class with your own derived BlockNetwork class. It must be sub-typed
 *    by your TileEntity you want to use as the BlockNetwork. It should also contain a public static
 *    {@link BlockNetworkHandler} sub-typed with the TileEntity and the BlockNetwork. It needs your
 *    TileEntity class and BlockNetwork constructor as arguments. Use the BlockNetworkHandler for
 *    most functions that deal with your BlockNetwork. Pass in the handler as an argument to the
 *    super constructor if your BlockNetwork is going to use a standard block search algorithm,
 *    or use the other constructor and supply your own search algorithm.<br>
 *    <pre><code>
 *    public class MyBlockNetwork extends BlockNetwork&lt;MyTileEntity&gt; {
 *    
 *      public static final BlockNetworkHandler&lt;MyTileEntity, MyBlockNetwork&gt; handler = new BlockNetworkHandler&lt;&gt;(MyTileEntity.class, MyBlockNetwork::new);
 *      
 *      public MyBlockNetwork(BlockPos position){
 *        super(position, handler);
 *      }
 *      
 *      // implement any abstract methods
 *    }
 *    </code></pre>
 * 
 * <p><b>Step 2:</b> The TileEntity that should act as your BlockNetwork should implement the
 *    {@link IBlockNetworkUser} interface, and sub-typed with your BlockNetwork. This TileEntity
 *    must keep a reference to your BlockNetwork, but not initialize it. In your TileEntity's
 *    {@link ITickingTileEntity#serverTick serverTick()} method, you must call either
 *    {@link BlockNetworkHandler#check} to just initialize the BlockNetwork or call
 *    {@link BlockNetworkHandler#tick(BlockNetwork, ServerLevel, BlockEntity)}. These methods
 *    automatically ensure your BlockNetwork is initialized on the first tick using your passed
 *    in constructor. It also ensures that the BlockNetwork is only ticked once, by checking the
 *    passed in TileEntity is the first one listed in our internal saved list of TileEntities.
 * <p>You CANNOT create your BlockNetwork in your TileEntity's {@link BlockEntity#onLoad()} function,
 *    because the BlockNetwork updates right after it is created, and that update may cause other
 *    TileEntities to load and also call their {@link BlockEntity#onLoad()} function and create more
 *    BlockNetworks and perform another Update.
 *    <pre><code>
 *    public class MyTileEntity implements IBlockNetworkUser&lt;MyBlockNetwork&gt; {
 *    
 *      private MyBlockNetwork network;
 *      
 *      &#64;Override
 *      public void serverTick(ServerLevel level, BlockState blockstate){
 *        MyBlockNetwork.handler.tick(network, level, this);
 *        // OR
 *        MyBlockNetwork.handler.check(network, level, this);
 *      }
 *      
 *      &#64;Override
 *      public void setBlockNetwork(MyBlockNetwork network){
 *        this.network = network;
 *      }
 *      
 *      &#64;Override
 *      &#64;Nullable
 *      public MyBlockNetwork getBlockNetwork(){
 *        return network;
 *      }
 *      
 *    }
 *    </code></pre>
 * 
 * 
 * <p><b>Step 3:</b> Removing a TileEntity might cause a hole in your BlockNetwork, and some blocks
 *    won't be connected anymore. For any blocks that might be part of your BlockNetwork, you must call
 *    {@link BlockNetworkHandler#onRemove} in your Block's {@link Block#onRemove} function, like this:<br>
 *    <code>MyBlockNetwork.handler.onRemove(super::onRemove, oldState, level, pos, newState, isMoving);</code><br>
 *    The {@link Block#onRemove} function is called only on the server side whenever a block is mined
 *    by a player, destroyed by an explosion, or moved by a piston (but pistons can't move BlockEntities).
 * <p>Calling this function will handle everything that needs to be done, by first getting a reference
 *    to your TileEntity, then calling the {@code super.onRemove} method (which removes the TileEntity
 *    from the world) and then actually removing the TileEntity from the BlockNetwork, and checking
 *    adjacent positions to see if they are still a part of the original BlockNetwork or have split off
 *    to a new BlockNetwork.
 * 
 * <p><b>Optional Step 1:</b> If you want to reference data from another BlockNetwork, then you must call
 *    That TileEntity's {@link IBlockNetworkUser#getBlockNetwork()} function to get the BlockNetwork.
 *    If it returns null, then that means that TileEntity hasn't been ticked yet. You can call
 *    {@link BlockNetworkHandler#createBlockNetwork} to create a BlockNetwork at that position. This will
 *    also cause that TileEntity to load it's BlockNetwork data from disk.
 * 
 * <p><b>Optional Step 2:</b> If you want BlockNetwork data to be saved to disk so that is exists after
 *    quitting a world and rejoining, then you must call
 *    your BlockNetwork's {@link BlockList#forAllTileEntities blocks.forAllTileEntities} function, and
 *    call some <code>saveDataFromNetwork</code> method, which writes the TileEntities data, then calls
 *    the {@link TileBase#update_data update_data} method. Here's an example:
 *    <code>blocks.forAllTileEntities((MyTileEntity tile) -> tile.saveDataFromNetwork(data));</code>
 * <p>You must ensure you never update an individual TileEntity's data. BlockNetwork data should be saved
 *    to all TileEntities, because you never know which one will be loaded first on world load.
 * <p>On the first tick, {@link IBlockNetworkUser#load_block_network_data()} will be called so
 *    your BlockNetwork's data can be loaded from the TileEntity.
 * 
 * <p><b>Optional Step 3:</b> Your BlockNetwork will always update whenever a TileEntity associated
 *    with the BlockNetwork is added or removed. If you want your BlockNetwork to update when other blocks
 *    are added or removed, (such as keeping a list of other attached blocks) then you'll want to
 *    implement the {@link #neighbor_was_changed} method. If you're only keeping track of one type
 *    of block, then it's best to check it there. You'll be responsible for updating your own list of
 *    blocks, adding or removing as needed. Rather than detecting if a block was removed, it's best to
 *    check your entire list first, and remove blocks that are invalid or don't exist anymore.
 * <p>In the {@link Block Blocks} associated with your BlockNetwork, you need to override the
 *    {@link Block#neighborChanged(BlockState, Level, BlockPos, Block, BlockPos, boolean)} function
 *    and call {@link BlockNetworkHandler#neighbor_changed(Level, BlockPos, BlockPos)}.
 * <p>If you checking for several types of blocks, then we need to do another approach. You need to call
 *    {@link #updateBlockNetwork(ServerLevel, BlockPos)} In the {@link #neighbor_was_changed} function.
 *    It's best to only update the BlockNetwork if the neighbor block is relevant to your BlockNetwork,
 *    so you need to check what kind of block it is. You'll also need to implement the {@link #clear_custom_data()}
 *    and {@link #customSearch(Node, Node, ServerLevel)} methods, to clear your data and add new blocks respectively.
 * 
 * <p>-----------------------------------------------------------------------------------------------
 * <p>Here I will describe how BlockNetworks function for my own sanity and others:
 * 
 * <p><b>1.</b> When a player sets down a TileEntity that is new, its block_network variable is null,
 * so it must be initialized to a new BlockNetwork instance. Then it must immediately be updated to add
 * itself to the list of blocks in the BlockNetwork.
 * 
 * <p><b>2.</b> Now say the player places another TileEntity down next to the first one. The easiest
 * solution would be to initialize a new BlockNetwork, and update it. If you do this, then you
 * had BETTER ensure that all TileEntities you find that use this BlockNetwork should be set to THIS
 * BlockNetwork.
 * <p>However, we have a problem. What you did was create a NEW BlockNetwork, then set all TileEntity's
 * block_network variable to this new BlockNetwork. This overwrites the data that any existing
 * BlockNetwork had to their default values.
 * <p>So, the correct way to handle this is, when a TileEntity is created, you first check adjacent
 * positions for existing BlockNetworks, then set our own block_network variable to the one we found.
 * 
 * <p><b>3.</b> So Now let's set down a bunch of TileEntities, and they all share the same BlockNetwork.
 * You Quit the world, then load it back up again. The game will load the TileEntities, but in what order?
 * You can safely assume that the first TileEntity to load will correctly initialize its BlockNetwork field,
 * call its update function, and correctly find all connecting TileEntities and set their block_network variable.
 * So, whenever the World loads the next TileEntity, you'd better ONLY create a new BlockNetwork if it's null.
 * 
 * <p><b>4.</b> What happens when we remove a single TileEntity is pretty straight-forward. Minecraft
 * invalidates the TileEntity and then removes it, and when that happens there are no more references to the
 * TileEntity or its block_network variable. But what about a TileEntity that is a part of a BlockNetwork
 * of multiple TileEntities? Well first we must detect the TileEntity is being removed, then we must update
 * the BlockNetwork, but prevent it from finding the TileEntity that is being removed.
 * 
 * <p><b>5.</b> Continuing from #2, You must've wondered what happens if you place a TileEntity that
 * is adjacent to multiple TileEntities, but they were not connected, so they each have a different
 * BlockNetwork. We still must find an existing BlockNetwork and join it, but which one? Well each one
 * contains the same type of data but possibly different values, it would be far too cumbersome to try
 * to average them all out, so to simplify this case scenerio, we just accept the first one we find and
 * call the update method, which will find all TileEntities and overwrite their block_network variable
 * and their data.
 * 
 * <p><b>6.</b> Continuing from #4, and the inverse of #5, what happens if you have a row of TileEntities,
 * and you remove one in the middle, thus creating multiple BlockNetworks? This is by far the most
 * complicated scenerio that can happen. The TileEntity CAN be removed from the BlockNetwork's internal
 * list of blocks, but all TileEntities are still part of the SAME BlockNetwork, even though there's a gap!
 * So what we need to do is call the updateBlockNetwork function using the position of one of the
 * adjacent TileEntities. This will correctly setup one side of the gap, but not the others! We also need
 * to ensure that we keep the BlockNetwork's data as best as we can.
 * <p>So here's how we do it. when a TileEntity is removed, go ahead and detect which sides have the same
 * type of TileEntity on each side, this means they WERE part of the original BlockNetwork. Call the update
 * method on the first position we find, this will correctly re-assign all block positions. Continue
 * checking all the other sides of the TileEntity being deleted, and if we find a TileEntity, it's either
 * an orphan or now counted as part of the first BlockNetwork. We should check that position against the
 * positions we've just finished discovering from the first BlockNetwork. If they aren't in the new list,
 * that means they are no longer connected, and should be a new BlockNetwork.
 * <p>Any new BlockNetworks we create, we can assign their data to that of the original BlockNetwork's data,
 * but this just duplicates the data. Where there were previously 1 BlockNetwork with the data, now there
 * are 2 or more. This duplicates data such as Energy, or items that were a part of the BlockNetwork.
 * We obviously don't want players to exploit this duplication, so all new BlockNetworks must be reset
 * to their default data.
 *
 * <p><b>7.</b> Oh dang. There's actually one more scenario I forgot about. Some block networks have extra
 * data, sure, but let's say you want to keep a list of TileEntities this BlockNetwork connects to,
 * Tiles that AREN'T part of the Block Network. For example, the EnergyNetwork
 * keeps track of IEnergyUsers that the network connects to.
 * In this case, you want to update the Block Network (or at least the BlockNetwork's data) whenever
 * you detect the block next to a TileEntity that belongs to this Block Network was CHANGED, because
 * a block could've been added or removed, and you need to update the data accordingly. So, we use
 * {@link Block#neighborChanged(BlockState, Level, BlockPos, Block, BlockPos, boolean)}
 * to detect the block beside us has changed and then call our BlockNetwork's update event.
 *
 * <p><b>8.</b> So now let's talk about BlockNetwork data. Out of the 5 BlockNetwork examples that exist in
 * ADDSynth's mods as of writing this, EnergyNetwork and DataCableNetwork only store positions
 * of TileEntities. That can be calculated at runtime and doesn't need to save any data to TileEntities.
 * However, LaserNetwork has a shared boolean value that determines if the LaserNetwork is running
 * or not. When a player toggles the On/Off switch, this must set the BlockNetwork's variable, and then
 * update all TileEntities. And BridgeNetwork allows a player to insert a Lens in any Suspension Bridge
 * block, and all Bridge blocks share the same network, so the player can access the Lens from any other block.
 * <p>The function for updating all the TileEntities in the {@link BlockNetwork#blocks} list doesn't exist,
 * so you'll have to make your own, and call it whenever your BlockNetwork's data changes. Also, when the
 * world loads, it does create new BlockNetworks, but you also want it to load saved data. For this reason
 * {@link BlockNetworkHandler#create_or_join} automatically
 * calls your TileEntity's {@link IBlockNetworkUser#load_block_network_data()} function.
 *
 * <p><b>9.</b> One last bit of advice. Sometimes a BlockNetwork wants information on another BlockNetwork,
 * so it calls that TileEntity's {@link IBlockNetworkUser#getBlockNetwork()} function. But the return
 * value of that function MIGHT be null. This only happens during World load, when one BlockNetwork starts
 * loading and updating before the other. Because of the way {@link BlockNetworkHandler#create_or_join} is
 * set up right now, you can't put anything in the {@link IBlockNetworkUser#getBlockNetwork()} that will
 * initialize a BlockNetwork because that would cause an infinite loop. Instead, if you depend on
 * another BlockNetwork during a BlockNetwork update, check if it is null and initialize it yourself
 * by calling {@link BlockNetworkHandler#createBlockNetwork}.
 *
 * <p><b>10.</b> Continuing from #7, This scenerio involves when during the BlockNetwork update, it changes
 * a block in the world, such as by calling {@link Level#setBlock(BlockPos, BlockState, int)}.
 * This will then call {@link Block#neighborChanged(BlockState, Level, BlockPos, Block, BlockPos, boolean)}
 * Which can cause some Block Networks to begin updating again, while during the first update, which may
 * trigger a Null Pointer Exception. Argh, I'm not explaining this very well. You must either check
 * if the Block Network is null, or check what kind of BlockNetwork it is and create a new one yourself,
 * which in turn calls that BlockNetwork's update method anyway.
 * <p>Currently, all of our neighbor detections assumes this only occurs during normal gameplay, and NOT
 * DURING WORLD LOAD! So there are no null checks. Luckily it seems that only the Energy Suspension Bridge
 * changes the world during its Block Network update, and it does not need to detect when blocks next to it
 * has changed.
 *
 * @see EnergyNetwork
 * @see SolarPanelNetwork
 * @see <a href="https://github.com/ADDSynth/Overpowered-Technology-1.20/blob/1.20/src/main/java/addsynth/overpoweredtechnology/machines/data_cable/DataCableNetwork.java">DataCableNetwork</a>
 * @see <a href="https://github.com/ADDSynth/Overpowered-Technology-1.20/blob/1.20/src/main/java/addsynth/overpoweredtechnology/machines/laser/machine/LaserNetwork.java">LaserNetwork</a>
 * @see <a href="https://github.com/ADDSynth/Overpowered-Technology-1.20/blob/1.20/src/main/java/addsynth/overpoweredtechnology/machines/suspension_bridge/BridgeNetwork.java">BridgeNetwork</a>
 * @author ADDSynth
 * @since July 1, 2020
 */
public abstract class BlockNetwork<T extends BlockEntity & IBlockNetworkUser> {

  /** All the blocks that are in this block network. */
  protected final BlockList<T> blocks = new BlockList<>();

  /** Search algorithm this BlockNetwork will use. Most BlockNetworks
   *  will just use the {@link StandardBlockSearch}. */
  private final IBlockSearchAlgorithm search_algorithm;

  /** This constructor will get a {@link StandardBlockSearch} algorithm from your {@link BlockNetworkHandler}.
   * @param position
   * @param handler
   */
  public BlockNetwork(final BlockPos position, final BlockNetworkHandler handler){
    this.search_algorithm = handler.standardBlockSearch();
    DebugBlockNetwork.CREATED(this, position);
  }

  public BlockNetwork(final BlockPos position, final IBlockSearchAlgorithm search_algorithm){
    this.search_algorithm = search_algorithm;
    DebugBlockNetwork.CREATED(this, position);
  }

  // This works perfectly and very efficiently. Never change it!
  protected static final void remove_invalid_nodes(final Collection<? extends AbstractNode> node_list){
    node_list.removeIf((AbstractNode n) -> n == null ? true : n.isInvalid());
  }

  /**
   * Must be called when splitting or joining BlockNetworks, and right after creating BlockNetworks during TileEntity load.
   * @param from
   */
  public final void updateBlockNetwork(final ServerLevel world, final BlockPos from){
    if(world != null){
      try{
        DebugBlockNetwork.UPDATED(this, from);
        clear_custom_data();
        blocks.update(search_algorithm, world, from, this, this::customSearch);
        onUpdateNetworkFinished(world);
      }
      catch(Exception e){
        ADDSynthCore.log.fatal("Error occured in BlockNetwork update! WHAT HAPPENED???", e);
      }
    }
  }

  /** Internal method only. Removes the TileEntity from this BlockNetwork's list of blocks.<br>
   *  Returns true if there was at least 1 other BlockEntity in this BlockNetwork. */
  final boolean removeTile(final ServerLevel world, final T destroyed_tile){
    blocks.remove(destroyed_tile);
    if(blocks.size() == 0){
      lastTileWasRemoved(world, destroyed_tile);
      return false;
    }
    return true;
  }

  final void baseTick(final ServerLevel world, final T tile){
    if(blocks.isFirstTile(tile)){
      tick(world);
    }
  }

  /** Override this method if you want your BlockNetwork to execute code every tick. We have internal
   *  code that ensures your BlockNetwork is only ticked once per tick. To tick your BlockNetwork you
   *  must call the static {@link BlockNetwork#tick(BlockNetwork, ServerLevel, BlockEntity, Function)}
   *  in your TileEntity's {@link ITickingTileEntity#serverTick()} method. */
  protected void tick(final ServerLevel world){
  }

  public final int getCount(){
    return blocks == null ? 0 : blocks.size();
  }

  @Nullable
  public final T getFirstTile(){
    return blocks.getFirstTile();
  }

  protected abstract void clear_custom_data();

  /** Called when {@link #updateBlockNetwork} function is completed. */
  protected void onUpdateNetworkFinished(final ServerLevel level){
  }

  protected void customSearch(@Nullable final Node previous, final Node node, final ServerLevel world){
  }

  // TODO: Once we start coding for MC 1.16 and leave 1.12 behind, retest the onNeighborChange / neighborChanged issue, and
  //         remove all references to onNeighborChange.
  /**<p>
   *   Call this in the block's
   *   {@link Block#neighborChanged(BlockState, Level, BlockPos, Block, BlockPos, boolean)} method.<br>
   *   <b>Do not use</b> the {@link Block#onNeighborChange(BlockState, LevelReader, BlockPos, BlockPos)} method!
   *   Starting in Minecraft 1.11 the {@link Level#updateNeighbourForOutputSignal(BlockPos, Block)} method
   *   is no longer called at the end of the {@link Level#setBlockEntity(BlockEntity)} function,
   *   so it doesn't update Block Networks at all.
   * <p>NOTE: It seems <code>onNeighborChange()</code> is added by Forge and only gets called if the neighbor block was
   *   a TileEntity, whereas <code>neighborChanged()</code> gets called every time an adjacent block gets changed.
   *   Best continue to call Minecraft's <code>neighborChanged()</code> function, in case someone's BlockNetwork
   *   wants to keep track of basic blocks.
   * <p>
   *   We actually recommend you call the simplified helper function
   *   {@link BlockNetworkHandler#neighbor_changed(Level, BlockPos, BlockPos)} instead.
   * <p>
   *   Your BlockNetwork automatically responds to block changes if the block is part of your BlockNetwork.
   *   So this is only used to respond to block changes that your BlockNetwork keeps track of, that are
   *   NOT part of your BlockNetwork.
   * <p>
   *   First, you do not want to update on EVERY neighbor block change, only the blocks that affect your
   *   BlockNetwork. Once you detect the type of block, the simplest way to update is to call your
   *   BlockNetwork's {@link #updateBlockNetwork(ServerLevel, BlockPos)} function. This will automatically clear
   *   your custom data and call {@link #customSearch(Node, Node, ServerLevel)} which again checks the block and then
   *   you can decide what to do with it. This is recommended if your BlockNetwork keeps track of lots of
   *   different kinds of blocks. But if you only track one type of block then we recommend doing the
   *   optimized approach, by checking the position of the neighbor and adding or removing it from your
   *   list accordingly.
   * <p>
   *   If the list of TileEntities you keep track of utilize {@link Node Nodes}, then you can call
   *   {@link #remove_invalid_nodes(Collection)} to automatically remove TileEntities that were removed.
   *   Otherwise you'll have to check for removed TileEntities yourself!
   * @param current_position
   * @param position_of_neighbor
   */
  public void neighbor_was_changed(final ServerLevel world, final BlockPos current_position, final BlockPos position_of_neighbor){
  }

  /** Override this to perform special actions when the last TileEntity
   *  belongs to this BlockNetwork has been removed. */
  protected void lastTileWasRemoved(final ServerLevel world, final T last_tile){
  }

}
