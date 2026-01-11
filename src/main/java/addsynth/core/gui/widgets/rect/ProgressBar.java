package addsynth.core.gui.widgets.rect;

import addsynth.core.gui.GuiContainerBase;
import addsynth.core.util.math.common.RoundMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class ProgressBar {

  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final int texture_x;
  private final int texture_y;
  private final int texture_scale_width;
  private final int texture_scale_height;
  private double texture_percentage;
  private int progress;

  public enum Direction {LEFT_TO_RIGHT, RIGHT_TO_LEFT, BOTTOM_TO_TOP, TOP_TO_BOTTOM}

  public ProgressBar(int x, int y, int width, int height, int texture_x, int texture_y){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    texture_scale_width = 256;
    texture_scale_height = 256;
  }

  public ProgressBar(int x, int y, int width, int height, int texture_x, int texture_y, int texture_scale_width, int texture_scale_height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.texture_x = texture_x;
    this.texture_y = texture_y;
    this.texture_scale_width = texture_scale_width;
    this.texture_scale_height = texture_scale_height;
  }

  public void draw(GuiGraphics graphics, GuiContainerBase gui, Direction direction, double value, RoundMode round_mode){
    texture_percentage = switch (direction) {
    case LEFT_TO_RIGHT, RIGHT_TO_LEFT -> width  * Mth.clamp(value, 0.0f, 1.0f);
    case BOTTOM_TO_TOP, TOP_TO_BOTTOM -> height * Mth.clamp(value, 0.0f, 1.0f);
    };
    progress = switch (round_mode) {
    case Floor   -> (int)Math.floor(texture_percentage);
    case Round   -> (int)Math.round(texture_percentage);
    case Ceiling -> (int)Math.ceil(texture_percentage);
    };
    switch(direction){
    case LEFT_TO_RIGHT: graphics.blit(gui.GUI_TEXTURE, gui.getGuiLeft() + x, gui.getGuiTop() + y,                     texture_x, texture_y,                     progress, height, texture_scale_width, texture_scale_height); break;
    case RIGHT_TO_LEFT: graphics.blit(gui.GUI_TEXTURE, gui.getGuiLeft() + x + width - progress, gui.getGuiTop() + y,  texture_x + width - progress, texture_y,  progress, height, texture_scale_width, texture_scale_height); break;
    case TOP_TO_BOTTOM: graphics.blit(gui.GUI_TEXTURE, gui.getGuiLeft() + x, gui.getGuiTop() + y,                     texture_x, texture_y,                     width, progress, texture_scale_width, texture_scale_height); break;
    case BOTTOM_TO_TOP: graphics.blit(gui.GUI_TEXTURE, gui.getGuiLeft() + x, gui.getGuiTop() + y + height - progress, texture_x, texture_y + height - progress, width, progress, texture_scale_width, texture_scale_height); break;
    }
  }

}
