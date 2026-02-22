package addsynth.core.gameplay.blocks.music_box.gui;

import org.lwjgl.glfw.GLFW;
import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gameplay.blocks.music_box.TileMusicBox;
import addsynth.core.gameplay.blocks.music_box.data.MusicGrid;
import addsynth.core.gameplay.blocks.music_box.network_messages.MusicBoxMessage;
import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.GuiBase;
import addsynth.core.gui.widgets.buttons.ButtonUtil;
import addsynth.core.util.constants.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public final class GuiMusicBox extends GuiBase {

  private final TileMusicBox tile;

  private static final int gui_width = 263;
  private static final int gui_height = 210;
  
  // gui text
  private static final Component         next_text = Component.translatable("gui.addsynthcore.music_box.next").append(":");
  private static final Component        tempo_text = Component.translatable("gui.addsynthcore.music_box.tempo").append(":");
  private static final String           ticks_text = "gui.addsynthcore.music_box.ticks";
  private static final String             bpm_text = "gui.addsynthcore.music_box.bpm";
  private static final Component current_note_text = Component.translatable("gui.addsynthcore.music_box.current_note");
  private static final Component   instrument_text = Component.translatable("gui.addsynthcore.music_box.instrument");
  private static final Component[] instrument = new Component[] {
    Component.translatable("gui.addsynthcore.instrument.harp"),
    Component.translatable("gui.addsynthcore.instrument.bass"),
    Component.translatable("gui.addsynthcore.instrument.bass_drum"),
    Component.translatable("gui.addsynthcore.instrument.snare_drum"),
    Component.translatable("gui.addsynthcore.instrument.click"),
    Component.translatable("gui.addsynthcore.instrument.bell"),
    Component.translatable("gui.addsynthcore.instrument.chime"),
    Component.translatable("gui.addsynthcore.instrument.flute"),
    Component.translatable("gui.addsynthcore.instrument.guitar"),
    Component.translatable("gui.addsynthcore.instrument.xylophone"),
    Component.translatable("gui.addsynthcore.instrument.iron_xylophone"),
    Component.translatable("gui.addsynthcore.instrument.cow_bell"),
    Component.translatable("gui.addsynthcore.instrument.didgeridoo"),
    Component.translatable("gui.addsynthcore.instrument.square"),
    Component.translatable("gui.addsynthcore.instrument.banjo"),
    Component.translatable("gui.addsynthcore.instrument.pling")
  };

  // variables
  public static byte note_selected;
  public static byte instrument_selected;
  private byte ticks;
  private int bpm;

  // controls
  private static final int play_button_width = 46;
  
  private static final int tempo_text_width = 50;
  private static final int tempo_text_height = 18;
  private static final int tempo_text_x_center = 6 + ButtonUtil.width + Math.round(tempo_text_width / 2);
  private static final int tempo_text_y_center = 17 + 9;
  private static final int tempo_button_y = tempo_text_y_center - Math.round(ButtonUtil.height / 2);
  
  private static final int next_direction_button_width = 50;
  private Button next_direction_button;

  private static final int info_text_y = 17 + tempo_text_height + 3;

  private static final int music_grid_x = 35;
  private static final int music_grid_y = 57;
  private static final int track_width  = 25;
  private static final int track_height = 13; // + 1 pixel border

  public static final int note_button_width = 24;
  public static final int note_button_height = 12;
  
  private static final int mute_button_x      = music_grid_x - 27;
  private static final int track_instrument_x = music_grid_x - 13;

  private static final int playhead_x = music_grid_x + Math.round((float)note_button_width / 2) - 8;
  private static final int playhead_y = music_grid_y - 9;
  private static final int playhead_texture_x = 64;
  private static final int playhead_texture_y = 24;

  private static final int instrument_button_size = 20;
  private static final int instrument_buttons = 8;
  private static final int instrument_max_width = instrument_button_size * instrument_buttons;
  private static final int instrument_cursor_x = (gui_width - instrument_max_width) / 2;
  private static final int instrument_cursor_y = 164;
  private static final int instrument_button_x = instrument_cursor_x + 2;
  private static final int instrument_button_y = instrument_cursor_y + 2;
  
  private static final int track_swap_button_x = music_grid_x + (track_width * MusicGrid.frames) + 2;
  private static final int track_swap_button_y = music_grid_y + (track_height / 2);

  public GuiMusicBox(final TileMusicBox tileEntity, final Component title){
    super(gui_width, gui_height, title, GuiReference.music_box_gui);
    this.tile = tileEntity;
  }

  @Override
  protected final void init(){
    super.init();

    // controls list
    final int play_button_x = guiBox.center_x - (play_button_width / 2);
    final int tempo_x1 = guiBox.left + 6;
    final int tempo_x2 = guiBox.left + 6 + ButtonUtil.width + tempo_text_width;
    final int tempo_y = guiBox.top + tempo_button_y;
    addRenderableWidget(new MusicBoxButtons.PlayButton(play_button_x, guiBox.top + 17, play_button_width, tile));
    addRenderableWidget(ButtonUtil.getLeftArrowButton(tempo_x1, tempo_y,
      () -> NetworkHandler.INSTANCE.sendToServer(new MusicBoxMessage(tile.getBlockPos(), TileMusicBox.Command.CHANGE_TEMPO, 0))
    ));
    addRenderableWidget(ButtonUtil.getRightArrowButton(tempo_x2, tempo_y,
      () -> NetworkHandler.INSTANCE.sendToServer(new MusicBoxMessage(tile.getBlockPos(), TileMusicBox.Command.CHANGE_TEMPO, 1))
    ));
    next_direction_button = MusicBoxButtons.getNextDirectionButton(guiBox.right - 6 - next_direction_button_width, guiBox.top + 17, next_direction_button_width, tile);
    addRenderableWidget(next_direction_button);

    // music grid buttons
    create_dynamic_buttons();
  }

  private final void create_dynamic_buttons(){
    byte i;
    byte j;
    int x;
    int y;

    // Mute Buttons
    x = guiBox.left + mute_button_x;
    for(i = 0; i < MusicGrid.tracks; i++){
      y = guiBox.top + music_grid_y + (i * (track_height));
      addRenderableWidget(new MusicBoxButtons.MuteButton(x, y, i, tile));
    }

    // Track Instrument Buttons
    x = guiBox.left + track_instrument_x;
    for(i = 0; i < MusicGrid.tracks; i++){
      y = guiBox.top + music_grid_y + (i * track_height);
      addRenderableWidget(new MusicBoxButtons.TrackInstrumentButton(x, y, i, tile));
    }

    // Note Buttons
    for(j = 0; j < MusicGrid.tracks; j++){
      for(i = 0; i < MusicGrid.frames; i++){
        x = guiBox.left + music_grid_x + (i * track_width);
        y = guiBox.top  + music_grid_y + (j * track_height);
        addRenderableWidget(new NoteButton(x, y, j, i, tile));
      }
    }
    // New Instrument Buttons
    int instrument;
    for(j = 0; j < 2; j++){
      for(i = 0; i < instrument_buttons; i++){
        instrument = i + (j * instrument_buttons);
        if(instrument < Constants.note_block_instruments){
          x = guiBox.left + instrument_button_x + (i * instrument_button_size);
          y = guiBox.top  + instrument_button_y + (j * instrument_button_size);
          addRenderableWidget(new MusicBoxButtons.SelectInstrumentButton(x, y, instrument));
        }
      }
    }
    
    // Track Swap Buttons
    x = guiBox.left + track_swap_button_x;
    for(i = 0; i < MusicGrid.tracks - 1; i++){
      y = guiBox.top + track_swap_button_y + (i * track_height);
      addRenderableWidget(new MusicBoxButtons.SwapTrackButton(x, y, tile, i));
    }
  }

  @Override
  public final void tick(){
    next_direction_button.setMessage(tile.getNextDirectionText());
  }

  @Override
  protected final void drawGuiBackgroundLayer(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY){
    draw_custom_background_texture(graphics, 384, 256);
    get_variables_from_music_box();
    draw_playhead(graphics);
    draw_muted_tracks();
    draw_instrument_selected(graphics);
  }

  private final void get_variables_from_music_box(){
    ticks = tile.getTempo();
    bpm = Math.round((20.0f / ticks) * 30);
  }

  private final void draw_playhead(final GuiGraphics graphics){
    if(tile != null){
      if(tile.is_playing()){
        graphics.blit(GuiReference.widgets, guiBox.left + playhead_x + (tile.playhead * track_width), guiBox.top + playhead_y,
                                       playhead_texture_x, playhead_texture_y, 16, 8);
      }
    }
  }

  private final void draw_muted_tracks(){
    byte i;
    for(i = 0; i < MusicGrid.tracks; i++){
      if(tile.get_mute(i)){
      }
    }
  }

  private final void draw_instrument_selected(final GuiGraphics graphics){
    final int texture_x = 112;
    final int texture_y = 32;
    final int texture_size = 40;
    final int x = guiBox.left + instrument_cursor_x + ( (instrument_selected % instrument_buttons) * instrument_button_size);
    final int y = guiBox.top  + instrument_cursor_y + ( (instrument_selected / instrument_buttons) * instrument_button_size);
    graphics.blit(GuiReference.widgets, x, y, instrument_button_size, instrument_button_size, texture_x, texture_y, texture_size, texture_size, 256, 256);
  }

  @Override
  protected final void drawGuiForegroundLayer(GuiGraphics graphics, final int mouseX, final int mouseY){
    draw_title(graphics);
    // draw tempo:
    draw_text_center(graphics,                        tempo_text,         tempo_text_x_center,  6);
    draw_text_center(graphics, Component.translatable(ticks_text, ticks), tempo_text_x_center, 17);
    draw_text_center(graphics, Component.translatable(  bpm_text, bpm  ), tempo_text_x_center, 27);
    
    draw_text_center(graphics, next_text, right_edge - (next_direction_button_width / 2), 6);
    
    draw_text_left(graphics, current_note_text.getString()+": "+NoteButton.note[note_selected].getString(),            6, info_text_y);
    draw_text_left(graphics, instrument_text.getString()+": "+instrument[instrument_selected].getString(), center_x - 10, info_text_y);
  }

  @Override
  public
  final boolean keyPressed(final int keyCode, final int par2, final int par3){
    if(keyCode == GLFW.GLFW_KEY_ESCAPE){
      onClose();
      return true;
    }
    // MAYBE: maybe make these keys changeable in the Controls Options screen.
    if(keyCode == GLFW.GLFW_KEY_A){ note_selected = 0; return true; }
    if(keyCode == GLFW.GLFW_KEY_Z){ note_selected = 1; return true; }
    if(keyCode == GLFW.GLFW_KEY_S){ note_selected = 2; return true; }
    if(keyCode == GLFW.GLFW_KEY_X){ note_selected = 3; return true; }
    if(keyCode == GLFW.GLFW_KEY_D){ note_selected = 4; return true; }
    if(keyCode == GLFW.GLFW_KEY_C){ note_selected = 5; return true; }
    if(keyCode == GLFW.GLFW_KEY_V){ note_selected = 6; return true; }
    if(keyCode == GLFW.GLFW_KEY_G){ note_selected = 7; return true; }
    if(keyCode == GLFW.GLFW_KEY_B){ note_selected = 8; return true; }
    if(keyCode == GLFW.GLFW_KEY_H){ note_selected = 9; return true; }
    if(keyCode == GLFW.GLFW_KEY_N){ note_selected = 10; return true; }
    if(keyCode == GLFW.GLFW_KEY_M){ note_selected = 11; return true; }
    if(keyCode == GLFW.GLFW_KEY_1){ note_selected = 12; return true; }
    if(keyCode == GLFW.GLFW_KEY_Q){ note_selected = 13; return true; }
    if(keyCode == GLFW.GLFW_KEY_2){ note_selected = 14; return true; }
    if(keyCode == GLFW.GLFW_KEY_W){ note_selected = 15; return true; }
    if(keyCode == GLFW.GLFW_KEY_3){ note_selected = 16; return true; }
    if(keyCode == GLFW.GLFW_KEY_E){ note_selected = 17; return true; }
    if(keyCode == GLFW.GLFW_KEY_R){ note_selected = 18; return true; }
    if(keyCode == GLFW.GLFW_KEY_5){ note_selected = 19; return true; }
    if(keyCode == GLFW.GLFW_KEY_T){ note_selected = 20; return true; }
    if(keyCode == GLFW.GLFW_KEY_6){ note_selected = 21; return true; }
    if(keyCode == GLFW.GLFW_KEY_Y){ note_selected = 22; return true; }
    if(keyCode == GLFW.GLFW_KEY_U){ note_selected = 23; return true; }
    if(keyCode == GLFW.GLFW_KEY_8){ note_selected = 24; return true; }
    return super.keyPressed(keyCode, par2, par3); // handle another key
  }

}
