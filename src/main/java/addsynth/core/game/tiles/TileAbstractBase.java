package addsynth.core.game.tiles;

import addsynth.core.util.game.tileentity.TileEntityUtil;
import addsynth.core.util.java.StringUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** This is our abstract BlockEntity class which only contains a few
 *  basic stuff. Do not override this directly. Either create your own
 *  BlockEntity or override {@link TileBase} or {@link TileBaseNoData}.
 * @author ADDSynth
 */
public abstract class TileAbstractBase extends BlockEntity {

  /** Player who placed the block. */
  protected String owner;
  /** The last player who right-clicked the block to use it. */
  protected String last_used_by;

  public TileAbstractBase(final BlockEntityType type, BlockPos position, BlockState blockstate){
    super(type, position, blockstate);
  }

  @SuppressWarnings("null")
  protected final boolean onServerSide(){
    return !level.isClientSide;
  }

  @SuppressWarnings("null")
  protected final boolean onClientSide(){
    return level.isClientSide;
  }

  public abstract void update_data();

  // ================================================================================

  /** You can set the owner by calling {@link TileEntityUtil#setOwner}. After a player
   *  has set down a block. This is called on the server and client.
   *  Also automatically sets the <code>last_used_by</code> field as well.
   *  However, if this is called on the server side, the TileEntity should be updated. */
  public final void setOwner(final Player player){
    owner = player.getGameProfile().getName();
    last_used_by = owner;
    if(onServerSide()){
      update_data(); // What would happen if I called this on the client side?
    }
  }

  /** This should be called in your TileEntity's {@link MenuProvider#createMenu}
   *  function, or in the {@link Block#use} function. This can be called on the server and
   *  client sides. Automatically sets the owner as well, if there is no owner defined.
   *  However, if this is called on the server side, the TileEntity should be updated. */
  public final void setPlayerAccessed(final Player player){
    last_used_by = player.getGameProfile().getName();
    if(StringUtil.StringExists(owner) == false){
      owner = last_used_by;
    }
    if(onServerSide()){
      update_data();
    }
  }

  public final String getOwner(){
    return owner;
  }
  
  public final String getLastUsedBy(){
    return last_used_by;
  }

  protected final void loadPlayerData(final CompoundTag nbt){
    owner = nbt.getString("Owner");
    last_used_by = nbt.getString("Last Used By");
  }

  protected final void savePlayerData(final CompoundTag nbt){
    nbt.putString("Owner", owner != null ? owner : "");
    nbt.putString("Last Used By", last_used_by != null ? last_used_by : "");
  }

}
