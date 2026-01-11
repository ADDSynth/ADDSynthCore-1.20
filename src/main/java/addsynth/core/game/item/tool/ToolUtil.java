package addsynth.core.game.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ToolUtil {

  /** @see net.minecraft.world.item.DiggerItem#mineBlock */
  public static boolean mine(final Level level, final BlockState blockstate, final BlockPos position){
    return !level.isClientSide && blockstate.getDestroySpeed(level, position) != 0.0f;
  }

  /** <p>Quick way of converting your percentage (whatever value it is) to the item's
   *  durability bar. All this does is multiply by 13 (bar frames) and rounds the result.
   *  <p>Vanilla's {@link Item#getBarWidth getBarWidth()} subtracts from 13 because
   *  it uses the Item's damage value, which increases. As damage gets closer to max damage,
   *  the durability bar decreases.
   * @param percentage
   * @return
   */
  public static final int getBarWidth(final double percentage){
    return (int)Math.round(percentage * 13);
  }

  /** Convenient way of getting the durability bar using a value from the ItemStack's tag. */
  public static final int getBarWidth(final ItemStack itemstack, final String value_tag, final int max_value){
    final CompoundTag tag = itemstack.getOrCreateTag();
    return max_value > 0 ? Math.round((float)tag.getInt(value_tag) / max_value * 13) : 0;
  }

  /** Convenient way of getting the durability bar using values from the ItemStack's tag. */
  public static final int getBarWidth(final ItemStack itemstack, final String value_tag, final String max_value_tag){
    final CompoundTag tag = itemstack.getOrCreateTag();
    final int max_value = tag.getInt(max_value_tag);
    return max_value > 0 ? Math.round((float)tag.getInt(value_tag) / max_value * 13) : 0;
  }

}
