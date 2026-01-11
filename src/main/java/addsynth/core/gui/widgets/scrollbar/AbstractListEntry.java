package addsynth.core.gui.widgets.scrollbar;

import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.util.color.Colors;
import addsynth.core.util.java.StringUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public abstract class AbstractListEntry<E> extends AbstractButton {

  private final int texture_x = 0;
  private final int texture_y = 224;

  protected int entry_id;
  // private String text; // not much of a performance benefit
  protected boolean selected;
  protected AbstractScrollbar responder;

  public AbstractListEntry(int x, int y, int width, int height){
    super(x, y, width, height, Component.empty());
  }

  public final void setScrollbar(final AbstractScrollbar scrollbar){
    this.responder = scrollbar;
  }

  @Override
  @SuppressWarnings("resource")
  public void renderWidget(GuiGraphics graphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_){
    Minecraft minecraft = Minecraft.getInstance();
    Font fontrenderer = minecraft.font;
    drawListEntryHighlight(graphics);
    graphics.drawString(fontrenderer, getMessage(), getX() + 1, getY() + 1, Colors.WHITE.value, true);
  }

  /** This is the code that draws a transparent white box under the list entry
   *  whenever the player mouses over this list entry or it is selected.   */
  protected final void drawListEntryHighlight(final GuiGraphics graphics){
    if((isHovered && StringUtil.StringExists(getMessage().getString())) || selected){
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      graphics.blit(GuiReference.highlight, getX(), getY(), texture_x, texture_y, width, height);
    }
  }

  @Override
  public void onPress(){
    if(StringUtil.StringExists(getMessage().getString())){
      responder.setSelected(entry_id);
    }
  }

  @Override
  public final boolean mouseScrolled(double mouse_x, double mouse_y, double scroll_direction){
    if(responder != null){
      responder.mouseScrollWheel((int)scroll_direction);
      return true;
    }
    return false;
  }

  public abstract void set(final int entry_id, final E value);

  /** Do not use this to set the selected entry. Use Scrollbar.setSelected(). */
  public void setSelected(final int selected_entry_id){
    this.selected = selected_entry_id >= 0 && entry_id == selected_entry_id;
  }

  public void setNull(){
    this.entry_id = -1;
    setMessage(Component.empty());
    this.selected = false;
  }

  @Override
  public final void playDownSound(SoundManager p_playDownSound_1_){
  }

  @Override
  public final void updateWidgetNarration(NarrationElementOutput p_169152_){
  }

}
