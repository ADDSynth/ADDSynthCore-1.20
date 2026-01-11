package addsynth.core.gameplay.items;

import java.util.List;
import javax.annotation.Nullable;
import addsynth.core.game.item.tool.ToolUtil;
import addsynth.core.gameplay.Sounds;
import addsynth.core.util.color.Colors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class WateringCan extends Item {

  private static final String uses = "uses";
  private static final int DEFAULT_USES = 64;
  private static final Component tooltip = Component.translatable("gui.addsynthcore.tooltip.watering_can");

  public WateringCan(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public void appendHoverText(ItemStack itemstack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag){
    tooltipComponents.add(tooltip);
    final CompoundTag tag = itemstack.getOrCreateTag();
    final int u = tag.getInt(uses);
    if(u > 0){
      tooltipComponents.add(Component.literal("Uses: "+u+" / "+DEFAULT_USES));
    }
    else{
      tooltipComponents.add(Component.literal("Empty"));
    }
  }

  @Override
  public final InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
    final ItemStack itemstack = player.getItemInHand(hand);
    final BlockHitResult hit_result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
    if(hit_result.getType() == HitResult.Type.MISS){
      return InteractionResultHolder.pass(itemstack);
    }
    if(hit_result.getType() != HitResult.Type.BLOCK){
      return InteractionResultHolder.pass(itemstack);
    }
    final BlockPos position = hit_result.getBlockPos();
    final BlockState blockstate = level.getBlockState(position);
    final Block block = blockstate.getBlock();
    final CompoundTag tag = itemstack.getOrCreateTag();
    if(block instanceof BucketPickup block_pickup){
      final FluidState fluidstate = blockstate.getFluidState();
      if(fluidstate.is(Fluids.WATER)){
        if(!block_pickup.pickupBlock(level, position, blockstate).isEmpty()){
          player.playSound(SoundEvents.AMBIENT_UNDERWATER_ENTER, 1.0f, 1.0f);
          player.awardStat(Stats.ITEM_USED.get(this));
          tag.putInt(uses, DEFAULT_USES);
          itemstack.setTag(tag);
          return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide);
        }
      }
    }
    if(tag.getInt(uses) > 0){
      // handle crops first, because we just want to water them, not bonemeal them
      if(block instanceof CropBlock crop){
        final BlockPos below = position.below();
        final BlockState farmland = level.getBlockState(below);
        // if(farmland.is(Blocks.FARMLAND)){
        //   if(farmland.getValue(FarmBlock.MOISTURE) < FarmBlock.MAX_MOISTURE){
        if(crop.isValidBonemealTarget(level, position, blockstate, level.isClientSide)){
          level.setBlockAndUpdate(below, farmland.setValue(FarmBlock.MOISTURE, FarmBlock.MAX_MOISTURE));
          // hydrating soil is not useful enough. I thought about hydrating a large area,
          // but that also doesn't seem good enough. We'll just grow crops like everything else.
          bonemeal(level, player, crop, position, blockstate, tag, itemstack);
          return InteractionResultHolder.success(itemstack);
        }
      }
      // handle making other things wet first, like sponge, and coral
      if(block == Blocks.SPONGE){return hydrate(level, player, position, Blocks.WET_SPONGE, tag, itemstack); }
      // Dead Coral cannot be turned back to Coral. Source, the wiki: https://minecraft.wiki/w/Dead_Coral#Usage
      if(block instanceof BonemealableBlock bonemeal_block){
        if(bonemeal_block.isValidBonemealTarget(level, position, blockstate, false)){
          bonemeal(level, player, bonemeal_block, position, blockstate, tag, itemstack);
          return InteractionResultHolder.success(itemstack);
        }
      }
    }
    return InteractionResultHolder.fail(itemstack);
  }

  private static final InteractionResultHolder<ItemStack> hydrate(Level level, Player player, BlockPos position, Block block, CompoundTag tag, ItemStack itemstack){
    return hydrate(level, player, position, block.defaultBlockState(), tag, itemstack);
  }

  private static final InteractionResultHolder<ItemStack> hydrate(Level level, Player player, BlockPos position, BlockState blockstate, CompoundTag tag, ItemStack itemstack){
    level.setBlockAndUpdate(position, blockstate);
    useWateringCan(player, tag, itemstack);
    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide);
  }

  /** @see {@link net.minecraft.world.item.BoneMealItem#applyBonemeal BoneMealItem.applyBonemeal()} */
  private static final void bonemeal(Level level, Player player, BonemealableBlock block, BlockPos position, BlockState blockstate, CompoundTag tag, ItemStack itemstack){
    // if(block.isValidBonemealTarget(level, position, blockstate, level.isClientSide)){
    if(level instanceof ServerLevel serverLevel){
      if(block.isBonemealSuccess(level, level.random, position, blockstate)){
        block.performBonemeal(serverLevel, level.random, position, blockstate);
      }
    }
    useWateringCan(player, tag, itemstack);
    //   return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide);
    // }
    //return InteractionResultHolder.pass(itemstack);
  }

  private static final void useWateringCan(final Player player, final CompoundTag tag, final ItemStack itemstack){
    player.playSound(Sounds.watering_can_use.get(), 0.25f, 1.0f);
    player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
    tag.putInt(uses, tag.getInt(uses) - 1);
    itemstack.setTag(tag);
  }

  @Override
  public boolean isBarVisible(ItemStack pStack){
    return true;
  }

  @Override
  public int getBarColor(ItemStack pStack){
    return Colors.WHITE.value;
  }

  @Override
  public int getBarWidth(ItemStack itemstack){
    return ToolUtil.getBarWidth(itemstack, uses, DEFAULT_USES);
  }

}
