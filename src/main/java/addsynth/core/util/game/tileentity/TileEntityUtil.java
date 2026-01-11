package addsynth.core.util.game.tileentity;

import javax.annotation.Nonnull;
import addsynth.core.ADDSynthCore;
import addsynth.core.game.tiles.TileAbstractBase;
import addsynth.core.util.game.chat.MessageUtil;
import addsynth.core.util.world.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityUtil {

  /**
   * Porting over my code from Minecraft 1.16 to Minecraft 1.17, all the TileEntities that had a tick method, only
   * had that method because they implement the {@link ITickingTileEntity} interface, and was an instance method.
   * Well starting in Minecraft 1.17, the tick method must now be a static method. I use this as an abstraction
   * to ease the transition. Many tick functions use instance variables because they were designed to be called
   * as an instance method.
   * Using this, your TileEntity is only expected to have the {@link ITickingTileEntity#serverTick(ServerLevel, BlockState) serverTick()} method.
   * @param <T>
   * @param world
   * @param position
   * @param state
   * @param tile
   * @since Minecraft 1.17
   */
  public static final <T extends BlockEntity & ITickingTileEntity> void tick(Level world, BlockPos position, BlockState state, T tile){
    if(tile != null){
      try{
        tile.serverTick((ServerLevel)world, state);
      }
      catch(Exception e){
        report_ticking_error(world, position, tile, e);
      }
    }
  }

  public static final <T extends BlockEntity & IClientTick> void clientTick(Level world, BlockPos position, BlockState state, T tile){
    if(tile != null){
      try{
        tile.clientTick();
      }
      catch(Exception e){
        report_ticking_error(world, position, tile, e);
      }
    }
  }

  private static final void report_ticking_error(final Level level, final BlockPos position, @Nonnull final BlockEntity tile, final Throwable e){
    final String class_name = tile.getClass().getSimpleName();
    
    ADDSynthCore.log.error(
      "Encountered an error while ticking TileEntity: "+class_name+", at position: "+position+". Please report this to the developer.", e);

    WorldUtil.delete_block(level, position);

    final MutableComponent message = Component.translatable("message.addsynthcore.tileentity_error",
      class_name, position.getX(), position.getY(), position.getZ());

    message.setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
    MessageUtil.send_to_all_players_in_world(level, message);
  }



  /** Call this in the {@link Block#setPlacedBy} method to set the TileEntity's
   *  {@link TileAbstractBase#owner owner} field. This function automatically handles everything. */
  public static final void setOwner(final Level world, final LivingEntity player, final BlockPos position){
    if(player instanceof Player){
      final BlockEntity tile = world.getBlockEntity(position);
      if(tile instanceof TileAbstractBase){
        ((TileAbstractBase)tile).setOwner((Player)player);
      }
    }
  }

}
