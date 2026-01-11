package addsynth.core.gameplay.items;

import java.util.List;
import javax.annotation.Nullable;
import addsynth.core.gameplay.Tags;
import addsynth.core.util.math.block.BlockMath;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HedgeTrimmers extends Item implements Vanishable {

  private static final int horizontal_distance = 4; // 9
  private static final int   vertical_distance = 2; // 5
  private static final Component tooltip = Component.translatable("gui.addsynthcore.tooltip.hedge_trimmers");

  public HedgeTrimmers(){
    super(new Item.Properties().durability(238));
  }

  @Override
  public void appendHoverText(ItemStack itemstack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag){
    tooltipComponents.add(tooltip);
  }

  private static final void mineBlocks(final Level level, final BlockPos origin, final Entity entity){
    // Could've probably used a BlockArea here, but I already typed all this
    final int startX = origin.getX() - horizontal_distance;
    final int startY = origin.getY() - vertical_distance;
    final int startZ = origin.getZ() - horizontal_distance;
    final int endX = origin.getX() + horizontal_distance;
    final int endY = origin.getY() + vertical_distance;
    final int endZ = origin.getZ() + horizontal_distance;
    final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos();
    // final double max_distance = (;
    // double distance;
    for(int x = startX; x <= endX; x++){
      position.setX(x);
      for(int y = startY; y <= endY; y++){
        position.setY(y);
        for(int z = startZ; z <= endZ; z++){
          position.setZ(z);
          if(level.getBlockState(position).is(Tags.HEDGE_TRIMMER_MINEABLE)){
            // if(Config.HedgeTrimmers.realistic_distance.get()){
              if(BlockMath.isWithinHorizontal(origin, position, horizontal_distance)){
                level.destroyBlock(position, true, entity);
              }
            // }
            // else{
            //   level.destroyBlock(position, true, entity);
            // }
          }
        }
      }
    }
  }

  @Override
  public boolean mineBlock(ItemStack itemstack, Level level, BlockState blockstate, BlockPos position, LivingEntity miningEntity){
    final boolean pass = blockstate.is(Tags.HEDGE_TRIMMER_MINEABLE);
    if(pass){
      mineBlocks(level, position, miningEntity);
      // TODO: shear sound isn't being played by CLIENT who used it.
      miningEntity.playSound(SoundEvents.SHEEP_SHEAR, 1.0f, 1.0f);
    }
    itemstack.hurtAndBreak(1, miningEntity, (LivingEntity entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    return pass;
  }

}
