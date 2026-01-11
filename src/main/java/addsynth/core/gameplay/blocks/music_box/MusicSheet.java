package addsynth.core.gameplay.blocks.music_box;

import java.util.List;
import javax.annotation.Nullable;
import addsynth.core.gameplay.reference.Core;
import addsynth.core.util.player.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Man did I have trouble with this one. I don't feel like explaining how it works right now.
 * Just run in debug mode and step through the code yourself to see how it works.
 * @see net.minecraft.world.level.block.TntBlock#use
 * @see net.minecraft.world.item.BucketItem
 * @see net.minecraft.world.item.EnderEyeItem
 */
@SuppressWarnings("resource")
public final class MusicSheet extends Item {

  // See how vanilla handles the Ender Eye, an item that is used on blocks and by itself.

  private static final Component music_sheet_clear    = Component.translatable("gui.addsynthcore.music_sheet.clear");
  private static final Component music_sheet_paste    = Component.translatable("gui.addsynthcore.music_sheet.paste");
  private static final Component music_sheet_copy     = Component.translatable("gui.addsynthcore.music_sheet.copy");
  private static final Component music_sheet_no_data  = Component.translatable("gui.addsynthcore.music_sheet.no_data");
  private static final Component music_sheet_has_data = Component.translatable("gui.addsynthcore.music_sheet.has_data");

  public MusicSheet(){
    super(new Item.Properties());
  }

  @Override
  public final InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand){
    final ItemStack stack = player.getMainHandItem();
    final BlockHitResult raytrace = getPlayerPOVHitResult(world, player, Fluid.NONE);
    
    // if we're hitting block
    if(raytrace.getType() == BlockHitResult.Type.BLOCK){
      if(world.getBlockState(((BlockHitResult)raytrace).getBlockPos()).getBlock() == Core.music_box.get()){
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, stack);
      }
    }
    
    player.startUsingItem(hand);
    if(world.isClientSide == false){
      if(player.isCrouching()){
        stack.setTag(null);
        player.sendSystemMessage(music_sheet_clear);
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
      }
    }
    
    return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
  }

  @Override
  public final InteractionResult useOn(final UseOnContext context){
    final Level world = context.getLevel();
    final BlockPos pos = context.getClickedPos();
    if(world.getBlockState(pos).getBlock() == Core.music_box.get()){
      if(world.isClientSide){
        return InteractionResult.SUCCESS;
      }
      final Player player = context.getPlayer();
      final ItemStack stack = context.getItemInHand();
      final TileMusicBox tile = (TileMusicBox)world.getBlockEntity(pos);
      if(tile != null && player != null){
        if(player.isCrouching() == false){
          final CompoundTag nbt = stack.getTag();
          if(nbt != null){
            tile.getMusicGrid().load_from_nbt(nbt);
            tile.changed = true;
            player.sendSystemMessage(music_sheet_paste);
            return InteractionResult.SUCCESS;
          }
        }
        // is sneaking
        copy_music_data(stack, player, tile);
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.PASS;
  }

  private static final void copy_music_data(final ItemStack stack, final Player player, final TileMusicBox tile){
    final CompoundTag nbt = new CompoundTag();
    tile.getMusicGrid().save_to_nbt(nbt);
      
    if(stack.getCount() == 1){
      stack.setTag(nbt);
    }
    else{
      stack.shrink(1);
      final ItemStack music_sheet = new ItemStack(Core.music_sheet.get(), 1);
      music_sheet.setTag(nbt);
      PlayerUtil.add_to_player_inventory(player, music_sheet);
    }
      
    player.sendSystemMessage(music_sheet_copy);
  }

  @Override
  public final void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag){
    if(stack.getTag() == null){
      tooltip.add(music_sheet_no_data);
    }
    else{
      tooltip.add(music_sheet_has_data);
    }
  }

}
