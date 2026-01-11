package addsynth.core.gui.widgets.scrollbar;

import addsynth.core.util.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class ItemListEntry extends AbstractListEntry<ItemStack> {

  private ItemStack item;

  public ItemListEntry(int x, int y, int width, int height){
    super(x, y, width, height);
  }

  @Override
  @SuppressWarnings("resource")
  public final void renderWidget(GuiGraphics graphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_){
    Minecraft minecraft = Minecraft.getInstance();
    Font fontrenderer = minecraft.font;
    drawListEntryHighlight(graphics);
    if(item != null){
      graphics.renderItem(item, getX() + 1, getY() + 1);
    }
    graphics.drawString(fontrenderer, getMessage(), getX() + 18, getY() + 5, Colors.WHITE.value, true);
  }

  @Override
  public final void set(final int entry_id, final ItemStack item){
    this.entry_id = entry_id;
    this.item = item;
    setMessage(item != null ? Component.translatable(item.getDescriptionId()) : Component.empty());
  }

  public final void set(final int entry_id, final ItemStack item, final Component message){
    this.entry_id = entry_id;
    this.item = item;
    setMessage(message);
  }

  @Override
  public final void setNull(){
    super.setNull();
    this.item = null;
  }
}
