package addsynth.core.gameplay.blocks.jukebox;

import javax.annotation.Nullable;
import addsynth.core.game.blocks.TileEntityBlock;
import addsynth.core.game.inventory.IInventoryUser;
import addsynth.core.gameplay.registers.Tiles;
import addsynth.core.util.game.MinecraftUtility;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class JukeboxPlayer extends TileEntityBlock {

  public JukeboxPlayer(){
    super(Block.Properties.of().mapColor(MapColor.STONE).strength(0.5f, 3.0f));
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos position, BlockState blockstate){
    return new TileJukeboxPlayer(position, blockstate);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockstate, BlockEntityType<T> type){
    return standardTicker(level, type, Tiles.AUTO_JUKEBOX.get());
  }

  @Override
  @SuppressWarnings("deprecation")
  public InteractionResult use(BlockState state, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hit_result){
    if(!level.isClientSide){
      final TileJukeboxPlayer tile = MinecraftUtility.getTileEntity(position, level, TileJukeboxPlayer.class);
      if(tile != null){
        final ItemStack itemstack = player.getItemInHand(hand);
        if(itemstack.getItem() instanceof RecordItem){
          if(tile.addMusicDisc(itemstack)){
            player.setItemInHand(hand, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
          }
        }
        NetworkHooks.openScreen((ServerPlayer)player, tile, position);
      }
    }
    // TODO: interaction blocks should return InteractionResult.CONSUME on server side? See AbstractFurnace.
    return InteractionResult.SUCCESS;
  }

  @Override
  @SuppressWarnings("deprecation")
   public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving){
    final BlockEntity tile = world.getBlockEntity(pos);
    super.onRemove(state, world, pos, newState, isMoving);
    if(tile != null){
      if(tile.isRemoved()){
        if(tile instanceof IInventoryUser){
          ((IInventoryUser)tile).drop_inventory();
        }
      }
    }
  }
  
}
