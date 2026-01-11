package addsynth.core.game.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AdvancedItem extends Item {

  private final ChatFormatting style;

  public AdvancedItem(final ChatFormatting style){
    super(new Item.Properties());
    this.style = style;
  }
  
  public AdvancedItem(final ChatFormatting style, final Item.Properties properties){
    super(properties);
    this.style = style;
  }
  
  @Override
  public Component getName(ItemStack stack){
    return ((MutableComponent)super.getName(stack)).withStyle(style);
  }

}
