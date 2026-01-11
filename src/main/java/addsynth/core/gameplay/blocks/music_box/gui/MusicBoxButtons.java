package addsynth.core.gameplay.blocks.music_box.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gameplay.blocks.music_box.TileMusicBox;
import addsynth.core.gameplay.blocks.music_box.network_messages.ChangeInstrumentMessage;
import addsynth.core.gameplay.blocks.music_box.network_messages.MusicBoxMessage;
import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.widgets.WidgetUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public final class MusicBoxButtons {

  private static final int instrument_texture_size = 64;

  public static final class PlayButton extends AbstractButton {

    private final TileMusicBox tile;

    public PlayButton(int x, int y, int width, TileMusicBox tile){
      super(x, y, width, 14, Component.translatable("gui.addsynthcore.music_box.play"));
      this.tile = tile;
    }

    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new MusicBoxMessage(tile.getBlockPos(),TileMusicBox.Command.PLAY));
    }
    
    @Override
    public final void playDownSound(final SoundManager p_playDownSound_1_){
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_){
    }
  
  }

  public static final Button getNextDirectionButton(int x, int y, int width, TileMusicBox tile){
    return Button.builder(Component.empty(), (Button button) -> {
      NetworkHandler.INSTANCE.sendToServer(new MusicBoxMessage(tile.getBlockPos(), TileMusicBox.Command.CYCLE_NEXT_DIRECTION));
    }).bounds(x, y, width, 14).build();
  }

  public static final class MuteButton extends AbstractButton {
  
    private static final int button_size = 12;
    private static final int texture_size = 24;
    private static final int texture_x = 64;
    private static final int texture_y = 32;
  
    private final TileMusicBox tile;
    private boolean mute;
    private final byte track;

    public MuteButton(final int x, final int y, final byte track, final TileMusicBox tile){
      super(x, y, button_size, button_size, Component.empty());
      this.tile = tile;
      this.track = track;
    }
  
    @Override
    public final void renderWidget(GuiGraphics graphics, final int mouseX, final int mouseY, final float partial_ticks){
      mute = tile.get_mute(track);
      WidgetUtil.common_button_render_setup(GuiReference.widgets);
      graphics.blit(GuiReference.widgets, getX(), getY(), button_size, button_size, mute ? texture_x + texture_size : texture_x, texture_y, texture_size, texture_size, 256, 256);
    }
  
    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new MusicBoxMessage(tile.getBlockPos(), TileMusicBox.Command.TOGGLE_MUTE, track));
    }
  
    @Override
    public final void playDownSound(final SoundManager p_playDownSound_1_){
    }
  
    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_){
    }
  
  }

  public static final class TrackInstrumentButton extends AbstractWidget {

    private static final int button_size = 12;

    private final TileMusicBox tile;
    private final byte track;

    public TrackInstrumentButton(final int x, final int y, final byte track, final TileMusicBox tile){
      super(x, y, button_size, button_size, Component.empty());
      this.track = track;
      this.tile = tile;
    }

    @Override
    public final void renderWidget(GuiGraphics graphics, final int mouse_x, final int mouse_y, final float partial_ticks){
      final int instrument = tile.get_track_instrument(track);
      final int texture_x = instrument_texture_size * (instrument % 4);
      final int texture_y = instrument_texture_size * (instrument / 4);
    
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

      graphics.blit(GuiReference.instruments, getX(), getY(), button_size, button_size, texture_x, texture_y, instrument_texture_size, instrument_texture_size, 256, 256);
    }
  
    @Override
    public final void onClick(double mouse_x, double mouse_y){
      NetworkHandler.INSTANCE.sendToServer(new ChangeInstrumentMessage(tile.getBlockPos(), track, GuiMusicBox.instrument_selected));
    }

    @Override
    public final void playDownSound(final SoundManager p_playDownSound_1_){
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_){
    }
  
  }

  public static final class SelectInstrumentButton extends AbstractWidget {
  
    private static final int button_size = 16;

    private final int instrument;
    private final int texture_x;
    private final int texture_y;

    public SelectInstrumentButton(final int x, final int y, final int instrument){
      super(x, y, button_size, button_size, Component.empty());
      this.instrument = instrument;
      texture_x = instrument_texture_size * (instrument % 4);
      texture_y = instrument_texture_size * (instrument / 4);
    }
  
    @Override
    public final void renderWidget(GuiGraphics graphics, final int mouse_x, final int mouse_y, final float partial_ticks){
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

      graphics.blit(GuiReference.instruments, getX(), getY(), button_size, button_size, texture_x, texture_y, instrument_texture_size, instrument_texture_size, 256, 256);
    }
  
    @Override
    public final void onClick(double mouse_x, double mouse_y){
      GuiMusicBox.instrument_selected = (byte)instrument;
    }

    @Override
    public final void playDownSound(final SoundManager p_playDownSound_1_){
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_){
    }
  
  }

  public static final class SwapTrackButton extends AbstractButton {

    private static final int button_width  = 20;
    private static final int button_height = 12;
    private static final int texture_x = 112;
    private static final int texture_y =  80;
    private static final int texture_width  = 40;
    private static final int texture_height = 24;
    private final TileMusicBox tile;
    private final int track;

    public SwapTrackButton(final int x, final int y, final TileMusicBox tile, final int track){
      super(x, y, button_width, button_height, Component.empty());
      this.tile = tile;
      this.track = track;
    }
    
    @Override
    public final void renderWidget(GuiGraphics graphics, final int mouse_x, final int mouse_y, final float partial_ticks){
      WidgetUtil.renderButton(graphics, this, GuiReference.widgets, texture_x, isHovered ? texture_y + texture_height : texture_y, button_width, button_height, texture_width, texture_height);
    }
    
    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new MusicBoxMessage(tile.getBlockPos(), TileMusicBox.Command.SWAP_TRACK, track));
    }
    
    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_){
    }
  
  }

}
