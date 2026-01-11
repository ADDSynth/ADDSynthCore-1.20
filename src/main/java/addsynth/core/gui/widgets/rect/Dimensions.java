package addsynth.core.gui.widgets.rect;

import addsynth.core.gui.widgets.WidgetUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/** This holds all texture coordinates as constant variables since they don't ever change. */
public final class Dimensions {

  private final ResourceLocation texture;
  private final int texture_x;
  private final int texture_y;
  private final int texture_width;
  private final int texture_height;
  /** Texture scale in relation to the gui. Usually either 1 or 2. */
  private final int texture_scale_from_gui;
  /** Texture Image Width. Usually this is 255. */
  private final int image_scale_width;
  /** Texture Image Height. Usually this is 255. */
  private final int image_scale_height;

  public Dimensions(ResourceLocation texture, int texture_x, int texture_y, int texture_width, int texture_height){
    this.texture = texture;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_width = texture_width;
    this.texture_height = texture_height;
    texture_scale_from_gui = 1;
    image_scale_width = 256;
    image_scale_height = 256;
  }
  
  public Dimensions(ResourceLocation texture, int texture_x, int texture_y, int texture_width, int texture_height, int texture_scale){
    this.texture = texture;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_width = texture_width;
    this.texture_height = texture_height;
    texture_scale_from_gui = texture_scale;
    image_scale_width = 256;
    image_scale_height = 256;
  }
  
  public Dimensions(ResourceLocation texture, int texture_x, int texture_y, int texture_width, int texture_height, int image_scale_width, int image_scale_height){
    this.texture = texture;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_width = texture_width;
    this.texture_height = texture_height;
    texture_scale_from_gui = 1;
    this.image_scale_width = image_scale_width;
    this.image_scale_height = image_scale_height;
  }
  
  public Dimensions(ResourceLocation texture, int texture_x, int texture_y, int texture_width, int texture_height, int texture_scale, int image_scale_width, int image_scale_height){
    this.texture = texture;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_width = texture_width;
    this.texture_height = texture_height;
    texture_scale_from_gui = texture_scale;
    this.image_scale_width = image_scale_width;
    this.image_scale_height = image_scale_height;
  }

  
  /** Draws the full texture on the gui using coordinates you specify.
   *  Assumes texture scaling is 1:1 with the gui. */  
  public final void draw(final GuiGraphics graphics, int x, int y){
    graphics.blit(texture, x, y, texture_width, texture_height, texture_x, texture_y, texture_width, texture_height, image_scale_width, image_scale_height);
  }

  /** Draws the full texture on the gui using coordinates you specify. */  
  public final void draw(final GuiGraphics graphics, int x, int y, int width, int height){
    graphics.blit(texture, x, y, width, height, texture_x, texture_y, texture_width, texture_height, image_scale_width, image_scale_height);
  }

  /** Does not draw the entire texture, and instead only draws the
   *  width & height resized based on the gui_to_texture_scaling. */
  public final void drawSized(final GuiGraphics graphics, int x, int y, int width, int height){
    graphics.blit(texture, x, y, width, height, texture_x, texture_y, width*texture_scale_from_gui, height*texture_scale_from_gui, image_scale_width, image_scale_height);
  }

  /** Draws the top and bottom half of texture, based on current gui dimensions.
   *  Assumes texture scaling is 1:1 with the gui. */
  public final void drawVerticalSplit(final GuiGraphics graphics, int x, int y){
    drawVerticalSplit(graphics, x, y, texture_width, texture_height);
  }

  /** Draws the top and bottom half of texture, based on current gui dimensions. */
  public final void drawVerticalSplit(final GuiGraphics graphics, int x, int y, int width, int height){
    final int[]     gui_heights = WidgetUtil.get_half_lengths(height);
    final int[] texture_heights = WidgetUtil.get_half_lengths(Math.min(height * texture_scale_from_gui, texture_height));
    final int                y2 = y + gui_heights[0];
    final int        texture_y2 = texture_y + texture_height - texture_heights[1];
    graphics.blit(texture, x, y,  width, gui_heights[0], texture_x, texture_y,  texture_width, texture_heights[0], image_scale_width, image_scale_height);
    graphics.blit(texture, x, y2, width, gui_heights[1], texture_x, texture_y2, texture_width, texture_heights[1], image_scale_width, image_scale_height);
  }

  /** Draws the left and right half of texture, based on current gui dimensions.
   *  Assumes texture scaling is 1:1 with the gui. */
  public final void drawHorizontalSplit(final GuiGraphics graphics, int x, int y){
    drawHorizontalSplit(graphics, x, y, texture_width, texture_height);
  }

  /** Draws the left and right half of texture, based on current gui dimensions. */
  public final void drawHorizontalSplit(final GuiGraphics graphics, int x, int y, int width, int height){
    final int[]     gui_widths = WidgetUtil.get_half_lengths(width);
    final int[] texture_widths = WidgetUtil.get_half_lengths(Math.min(width * texture_scale_from_gui, texture_width));
    final int               x2 = x + gui_widths[0];
    final int       texture_x2 = texture_x + texture_width - texture_widths[1];
    graphics.blit(texture, x,  y, gui_widths[0], height, texture_x,  texture_y, texture_widths[0], texture_height, image_scale_width, image_scale_height);
    graphics.blit(texture, x2, y, gui_widths[1], height, texture_x2, texture_y, texture_widths[1], texture_height, image_scale_width, image_scale_height);
  }

}
