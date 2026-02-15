package addsynth.core.gameplay.blocks.music_box.gui;

import org.lwjgl.glfw.GLFW;
import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gameplay.blocks.music_box.TileMusicBox;
import addsynth.core.gameplay.blocks.music_box.network_messages.NoteMessage;
import addsynth.core.gameplay.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public final class NoteButton extends AbstractWidget {
  
  public static final Component[] note = new Component[]{
                                                        Component.literal("F#3"), Component.literal("G3" ),
    Component.literal("G#3"), Component.literal("A3" ), Component.literal("A#3"), Component.literal("B3" ),
    Component.literal("C4" ), Component.literal("C#4"), Component.literal("D4" ), Component.literal("D#4"),
    Component.literal("E4" ), Component.literal("F4" ), Component.literal("F#4"), Component.literal("G4" ),
    Component.literal("G#4"), Component.literal("A4" ), Component.literal("A#4"), Component.literal("B4" ),
    Component.literal("C5" ), Component.literal("C#5"), Component.literal("D5" ), Component.literal("D#5"),
    Component.literal("E5" ), Component.literal("F5" ), Component.literal("F#5")
  };

  private static final int center_x = Math.round((float)GuiMusicBox.note_button_width / 2);
  private static final int text_draw_y = Math.round((float)GuiMusicBox.note_button_height / 2) - 4;

  private final TileMusicBox tile;
  private final byte track;
  private final byte frame;

  public NoteButton(int x_position, int y_position, byte track, byte frame, TileMusicBox tile){
    super(x_position, y_position , GuiMusicBox.note_button_width, GuiMusicBox.note_button_height, note[tile.get_note(frame, track)]);
    this.tile = tile;
    this.track = (byte)track;
    this.frame = (byte)frame;
  }

  @Override
  public void render(GuiGraphics graphics, int p_render_1_, int p_render_2_, float p_render_3_){
    visible = tile.note_exists(track, frame);
    if(visible){
      setMessage(note[tile.get_note(frame, track)]);
    }
    super.render(graphics, p_render_1_, p_render_2_, p_render_3_);
  }

  @Override
  public final void renderWidget(GuiGraphics graphics, final int mouseX, final int mouseY, final float partial){
    final String note = getMessage().getString();
    if(note != null){
      @SuppressWarnings("resource")
      final Minecraft mc = Minecraft.getInstance();
      graphics.drawString(mc.font, note, getX() + center_x - (mc.font.width(note) / 2), getY() + text_draw_y, 4210752, false);
    }
  }

  @Override
  public boolean mouseClicked(double mouse_x, double mouse_y, int button){ // overriden so it doesn't play the button sound?
    if(active){
      if(clicked(mouse_x, mouse_y)){
        final boolean left_hand = Config.enable_left_hand.get();
        if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT){
          if(left_hand){
            delete_note();
          }
          else{
            set_note();
          }
          return true;
        }
        if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT){
          if(left_hand){
            set_note();
          }
          else{
            delete_note();
          }
          return true;
        }
      }
    }
    return false;
  }

  @Override
  protected boolean clicked(double mouse_x, double mouse_y){
    final double x = (double)getX();
    final double y = (double)getY();
    return this.active &&
      mouse_x >= x              && mouse_y >= y &&
      mouse_x <  x + this.width && mouse_y <  y + this.height;
  }

  private final void set_note(){
    NetworkHandler.INSTANCE.sendToServer(new NoteMessage(tile.getBlockPos(), frame, track, GuiMusicBox.note_selected, 1.0f));
  }

  private final void delete_note(){
    if(visible == true){
      NetworkHandler.INSTANCE.sendToServer(new NoteMessage(tile.getBlockPos(), frame, track));
    }
  }

  @Override
  public void updateWidgetNarration(NarrationElementOutput p_169152_){
  }

}
