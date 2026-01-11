package addsynth.core.gui.widgets.rect;

import addsynth.core.gui.widgets.WidgetUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/** This a combination Box class that contains gui coordinates and texture coordinates. */
public final class TextureRect {

  /** Gui X position */
  public int x;
  /** Gui Y position */
  public int y;
  /** Gui Width */
  public int width;
  /** Gui Height */
  public int height;
  /** Size of Texture in relation to gui coordinates. Usually texture is equal to or twice the size of gui. */
  private final int texture_scale_from_gui;
  /** Texture X coordinate. */
  public final int texture_x;
  /** Texture Y coordinate. */
  public final int texture_y;
  /** Texture Width */
  public final int texture_width;
  /** Texture Height */
  public final int texture_height;
  /** Texture Full Image Width. This is usually 256. */
  public final int image_scale_width;
  /** Texture Full Image Height. This is usually 256. */
  public final int image_scale_height;

  public TextureRect(int x, int y, int width, int height, int texture_x, int texture_y, int texture_width, int texture_height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    texture_scale_from_gui = 1;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_width = texture_width;
    this.texture_height = texture_height;
    image_scale_width = 256;
    image_scale_height = 256;
  }

  public TextureRect(int x, int y, int width, int height, int texture_scale, int texture_x, int texture_y, int texture_width, int texture_height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.texture_scale_from_gui = texture_scale;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_width = texture_width;
    this.texture_height = texture_height;
    image_scale_width = 256;
    image_scale_height = 256;
  }

  public TextureRect(int x, int y, int width, int height, int texture_x, int texture_y, int texture_width, int texture_height, int texture_image_width, int texture_image_height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    texture_scale_from_gui = 1;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_width = texture_width;
    this.texture_height = texture_height;
    this.image_scale_width = texture_image_width;
    this.image_scale_height = texture_image_height;
  }

  public TextureRect(int x, int y, int width, int height, int texture_scale, int texture_x, int texture_y, int texture_width, int texture_height, int texture_image_width, int texture_image_height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.texture_scale_from_gui = texture_scale;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_width = texture_width;
    this.texture_height = texture_height;
    this.image_scale_width = texture_image_width;
    this.image_scale_height = texture_image_height;
  }

  /** Draws the full texture, into the full gui dimensions. */
  public final void draw(final GuiGraphics graphics, final ResourceLocation texture){
    graphics.blit(texture, x, y, width, height, texture_x, texture_y, texture_width, texture_height, image_scale_width, image_scale_height);
  }

  /** Draws the full texture, into the full gui dimensions. */
  public final void draw(final GuiGraphics graphics, final ResourceLocation texture, final int x, final int y){
    graphics.blit(texture, x, y, width, height, texture_x, texture_y, texture_width, texture_height, image_scale_width, image_scale_height);
  }

  /** Draws the top and bottom half of texture, based on current gui dimensions. */
  public final void drawVerticalSplit(final GuiGraphics graphics, final ResourceLocation texture){
    final int[]     gui_heights = WidgetUtil.get_half_lengths(height);
    final int[] texture_heights = WidgetUtil.get_half_lengths(Math.min(height * texture_scale_from_gui, texture_height));
    final int                y2 = y + gui_heights[0];
    final int        texture_y2 = texture_y + texture_height - texture_heights[1];
    graphics.blit(texture, x, y,  width, gui_heights[0], texture_x, texture_y,  texture_width, texture_heights[0], image_scale_width, image_scale_height);
    graphics.blit(texture, y, y2, width, gui_heights[1], texture_x, texture_y2, texture_width, texture_heights[1], image_scale_width, image_scale_height);
  }

  /** Draws the left and right half of texture, based on current gui dimensions. */
  public final void drawHorizontalSplit(final GuiGraphics graphics, final ResourceLocation texture){
    final int[]     gui_widths = WidgetUtil.get_half_lengths(width);
    final int[] texture_widths = WidgetUtil.get_half_lengths(Math.min(width * texture_scale_from_gui, texture_width));
    final int               x2 = x + gui_widths[0];
    final int       texture_x2 = texture_x + texture_width - texture_widths[1];
    graphics.blit(texture, x,  y, gui_widths[0], height, texture_x,  texture_y, texture_widths[0], texture_height, image_scale_width, image_scale_height);
    graphics.blit(texture, x2, y, gui_widths[1], height, texture_x2, texture_y, texture_widths[1], texture_height, image_scale_width, image_scale_height);
  }

}
