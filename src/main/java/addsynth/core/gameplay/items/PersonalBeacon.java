package addsynth.core.gameplay.items;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class PersonalBeacon extends Item {

  private static final Component tooltip = Component.translatable("gui.addsynthcore.tooltip.personal_beacon");

  public PersonalBeacon(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public void appendHoverText(ItemStack itemstack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag){
    tooltipComponents.add(tooltip);
  }

  @Override
  @Nullable
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt){
    return CuriosApi.createCurioProvider(new ICurio(){
      @Override
      public ItemStack getStack(){
        return stack;
      }
      @Override
      public void curioTick(final SlotContext slotContext){
        final LivingEntity entity = slotContext.entity();
        @SuppressWarnings("resource")
        final Level level = entity.level();
        if(!level.isClientSide){
          final MobEffectInstance effect = entity.getEffect(MobEffects.GLOWING);
          if(effect == null || entity.tickCount % 19 == 0){
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0, true, false, true));
          }
        }
      }
      @Override
      public void onUnequip(SlotContext slotContext, ItemStack newStack){
        slotContext.entity().removeEffect(MobEffects.GLOWING);
      }
    });
  }

}
