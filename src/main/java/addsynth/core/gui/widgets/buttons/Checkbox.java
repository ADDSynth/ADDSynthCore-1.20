package addsynth.core.gui.widgets.buttons;

import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.util.GuiUtil;
import addsynth.core.gui.widgets.WidgetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class Checkbox extends AbstractButton {

  private static final int texture_x = 0;
  private static final int texture_y = 32;
  private static final int texture_width = 24;
  private static final int texture_height = 24;
  public static final int size = 12;

  public Checkbox(final int x, final int y, final Component text_component){
    super(x, y, size, size, text_component);
  }

  protected abstract boolean get_toggle_state();

  @Override
  public final void renderWidget(GuiGraphics graphics, final int mouseX, final int mouseY, final float partial_ticks){
    @SuppressWarnings("resource")
    final Minecraft minecraft = Minecraft.getInstance();
    final Font font = minecraft.font;
    final boolean checked = get_toggle_state();
    WidgetUtil.common_button_render_setup(GuiReference.widgets);
    graphics.blit(GuiReference.widgets, getX(), getY(), size, size, checked ? texture_x : texture_x + texture_height, texture_y, texture_width, texture_height, 256, 256);
    graphics.drawString(font, getMessage(), getX() + 16, getY() + 2, GuiUtil.text_color, false);
  }

  @Override
  public void updateWidgetNarration(NarrationElementOutput p_169152_){
  }

}
