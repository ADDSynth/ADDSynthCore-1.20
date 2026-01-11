package addsynth.core.gui;

import javax.annotation.Nonnull;
import addsynth.core.gui.util.GuiUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/** Extend from this class if your gui screen contains item slots. */
public abstract class GuiContainerBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

  public final ResourceLocation GUI_TEXTURE;
  /** Center of gui. For use in drawing graphics, NOT text! */
  protected int guiCenter;
  /** Center of gui. Used for drawing text. */
  protected final int center_x;
  /** Right edge of gui. Used for drawing text against the right edge.
   *  This is equivalent to <code>{@link AbstractContainerScreen#imageWidth imageWidth} - 6</code>. */
  protected final int right_edge;
  
  public GuiContainerBase(final T container, final Inventory player_inventory, final Component title, final ResourceLocation gui_texture){
    super(container, player_inventory, title);
    GUI_TEXTURE = gui_texture;
    center_x = 88;
    right_edge = 170;
  }

  public GuiContainerBase(int width, int height, T container, Inventory player_inventory, Component title, ResourceLocation gui_texture){
    super(container, player_inventory, title);
    GUI_TEXTURE = gui_texture;
    this.imageWidth = width;
    this.imageHeight = height;
    center_x = width / 2;
    right_edge = width - 6;
  }

// ========================================================================================================
  
  /** Draws entire gui texture. (at default texture width and height of 256x256.) */
  protected final void draw_background_texture(final GuiGraphics graphics){
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    graphics.blit(GUI_TEXTURE, leftPos, topPos, imageWidth, imageHeight, 0, 0, imageWidth, imageHeight, 256, 256);
  }

  /** Since whenever we use a gui texture that's too big than 256x256 is often 384x256,
   *  we provide a helper method for this size, since we use it a lot.
   * @param graphics
   */
  protected final void draw_wide_background_texture(final GuiGraphics graphics){
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    graphics.blit(GUI_TEXTURE, leftPos, topPos, imageWidth, imageHeight, 0, 0, imageWidth, imageHeight, 384, 256);
  }

  /** Draws the background texture with custom scaled width and height. Use this
   *  if you have a background texture that is not the default size of 256x256.
   * @param texture_width
   * @param texture_height
   */
  protected final void draw_custom_background_texture(final GuiGraphics graphics, final int texture_width, int texture_height){
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    graphics.blit(GUI_TEXTURE, leftPos, topPos, imageWidth, imageHeight, 0, 0, imageWidth, imageHeight, texture_width, texture_height);
  }

  /** Draws another part of the gui texture at the coordinates you specify.
   *  This draws the texture portion the same size that is drawn on the gui.
   *  Assumes a default texture size of 256x256. */
  protected final void draw(final GuiGraphics graphics, final int x, final int y, final int u, final int v, final int width, final int height){
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    graphics.blit(GUI_TEXTURE, leftPos + x, topPos + y, width, height, u, v, width, height, 256, 256);
  }

  /** Draws another part of the gui texture at the coordinates you specify.
   *  If you need more control over how it's drawn, you might as well use the
   *  vanilla {@link GuiGraphics#blit} function directly. */
  protected final void draw(final GuiGraphics graphics, int x, int y, int u, int v, int width, int height, int texture_width, int texture_height){
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    graphics.blit(GUI_TEXTURE, leftPos + x, topPos + y, width, height, u, v, texture_width, texture_height, 256, 256);
  }

// ========================================================================================================
  
  protected final void draw_title(final GuiGraphics graphics){
    graphics.drawString(font, title, center_x - (font.width(title) / 2), 6, GuiUtil.text_color, false);
  }

  /** This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}. */
  protected final void draw_text_left(final GuiGraphics graphics, final String text, final int x, final int y){
    graphics.drawString(font, text, x, y, GuiUtil.text_color, false);
  }

  /** This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}. */
  protected final void draw_text_left(final GuiGraphics graphics, @Nonnull final Component text, final int x, final int y){
    graphics.drawString(font, text, x, y, GuiUtil.text_color, false);
  }

  /** Draws center-aligned text at the center of the gui.<br />
   *  This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}. */
  protected final void draw_text_center(final GuiGraphics graphics, @Nonnull final Component text, final int y){
    graphics.drawString(font, text, center_x - (font.width(text) / 2), y, GuiUtil.text_color, false);
  }

  /** Vanilla has their own method but mine assumes a few arguments to make it easier.<br />
   *  This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}.
   * @see net.minecraft.client.gui.GuiGraphics#drawCenteredString
   * @param text
   * @param x
   * @param y
   */
  protected final void draw_text_center(final GuiGraphics graphics, @Nonnull final Component text, final int x, final int y){
    graphics.drawString(font, text, x - (font.width(text) / 2), y, GuiUtil.text_color, false);
  }

  /** Draws center-aligned text at the center of the gui.<br />
   *  This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}. */
  protected final void draw_text_center(final GuiGraphics graphics, final String text, final int y){
    graphics.drawString(font, text, center_x - (font.width(text) / 2), y, GuiUtil.text_color, false);
  }

  /** Vanilla has their own method but mine assumes a few arguments to make it easier.<br />
   *  This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}.
   * @see net.minecraft.client.gui.GuiGraphics#drawCenteredString
   * @param text
   * @param x
   * @param y
   */
  protected final void draw_text_center(final GuiGraphics graphics, final String text, final int x, final int y){
    graphics.drawString(font, text, x - (font.width(text) / 2), y, GuiUtil.text_color, false);
  }

  /** Draws along the right-edge of the gui.<br />
   *  This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}. */
  protected final void draw_text_right(final GuiGraphics graphics, final String text, final int y){
    graphics.drawString(font, text, right_edge - font.width(text), y, GuiUtil.text_color, false);
  }

  /** Draws along the right-edge of the gui.<br />
   *  This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}. */
  protected final void draw_text_right(final GuiGraphics graphics, final Component component, final int y){
    graphics.drawString(font, component, right_edge - font.width(component), y, GuiUtil.text_color, false);
  }

  /** Draws the text right-aligned.
   *  This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}. */
  protected final void draw_text_right(final GuiGraphics graphics, final String text, final int x, final int y){
    graphics.drawString(font, text, x - font.width(text), y, GuiUtil.text_color, false);
  }

  /** Draws the text right-aligned.
   *  This will render the string in a different color if you prefix the string with
   *  {@link net.minecraft.ChatFormatting ChatFormatting.COLOR.toString()}. */
  protected final void draw_text_right(final GuiGraphics graphics, final Component component, final int x, final int y){
    graphics.drawString(font, component, x - font.width(component), y, GuiUtil.text_color, false);
  }

// ========================================================================================================

  @Override
  protected void init(){
    super.init();
    guiCenter = leftPos + center_x;
  }

  /** REPLICA: {@link ContainerEventHandler#mouseDragged}<br/>
   *  On normal {@link net.minecraft.client.gui.screens.Screen Screens}, the
   *  {@link ContainerEventHandler#mouseDragged} method is called by
   *  {@link net.minecraft.client.MouseHandler#onMove}. However, in
   *  {@link AbstractContainerScreen ContainerScreens} the
   *  {@link AbstractContainerScreen#mouseDragged} method is overridden with different
   *  instructions. Therefore, we override it again, and call both functions.
   *  This is replicated because we need to call the
   *  {@link net.minecraft.client.gui.components.events.GuiEventListener#mouseDragged} method,
   *  Because {@link addsynth.core.gui.widgets.scrollbar.AbstractScrollbar#onDrag Scrollbars}
   *  depend on this method to move properly!
   */
  @Override
  public boolean mouseDragged(double gui_x, double gui_y, int widget_id, double screen_x, double screen_y){
    super.mouseDragged(gui_x, gui_y, widget_id, screen_x, screen_y);
    // return ((ContainerEventHandler)this).mouseDragged(gui_x, gui_y, widget_id, screen_x, screen_y); Causes a Stackoverflow because we're still calling THIS method.
    final GuiEventListener focused = this.getFocused();
    if(focused != null && this.isDragging() && widget_id == 0){
      return focused.mouseDragged(gui_x, gui_y, widget_id, screen_x, screen_y);
    }
    return false;
  }

  /** Main render function. */
  @Override
  public void render(GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTicks){
    this.renderBackground(graphics);
    super.render(graphics, mouseX, mouseY, partialTicks);
    this.renderTooltip(graphics, mouseX, mouseY);
  }

  /** This draws your main gui window.
   *  {@link #renderBackground(GuiGraphics)} dims the screen behind your gui. */
  @Override
  protected void renderBg(GuiGraphics graphics, final float partialTicks, final int mouseX, final int mouseY){
    draw_background_texture(graphics);
  }

}
