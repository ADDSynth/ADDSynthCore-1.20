package addsynth.core.gameplay.blocks.jukebox;

import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gui.widgets.buttons.Checkbox;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class JukeboxGuiComponents {

  public static class PlayStopButton extends AbstractButton {

    private final TileJukeboxPlayer tile;
    private boolean playing;

    public PlayStopButton(int x, int y, TileJukeboxPlayer tile){
      super(x, y, 50, 16, Component.literal("▶"));
      this.tile = tile;
      active = tile.hasMusicDiscs();
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick){
      super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
      if(tile.isPlaying() != playing){
        if(tile.isPlaying()){
          setMessage(Component.literal("⏹"));
          playing = true;
        }
        else{
          setMessage(Component.literal("▶"));
          playing = false;
        }
      }
    }

    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new JukeboxMessage(tile.getBlockPos(), JukeboxMessage.Command.PLAYSTOP));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput){
    }
  
  }

  public static class SkipLeftButton extends AbstractButton {

    private final TileJukeboxPlayer tile;

    public SkipLeftButton(int x, int y, TileJukeboxPlayer tile){
      super(x, y, 40, 16, Component.literal("⏮"));
      this.tile = tile;
      active = tile.canSkipLeft();
    }

    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new JukeboxMessage(tile.getBlockPos(), JukeboxMessage.Command.SKIP_LEFT));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput){
    }
  
  }

  public static class SkipRightButton extends AbstractButton {

    private final TileJukeboxPlayer tile;

    public SkipRightButton(int x, int y, TileJukeboxPlayer tile){
      super(x, y, 40, 16, Component.literal("⏭"));
      this.tile = tile;
      active = tile.canSkipRight();
    }

    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new JukeboxMessage(tile.getBlockPos(), JukeboxMessage.Command.SKIP_RIGHT));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput){
    }
  
  }

  public static class ToggleShuffleCheckbox extends Checkbox {

    private final TileJukeboxPlayer tile;

    public ToggleShuffleCheckbox(int x, int y, TileJukeboxPlayer tile){
      super(x, y, Component.literal("Shuffle"));
      this.tile = tile;
    }

    @Override
    protected boolean get_toggle_state(){
      return tile.getShuffle();
    }

    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new JukeboxMessage(tile.getBlockPos(), JukeboxMessage.Command.TOGGLE_SHUFFLE));
    }
  
  }

  public static class ToggleRepeatCheckbox extends Checkbox {

    private final TileJukeboxPlayer tile;

    public ToggleRepeatCheckbox(int x, int y, TileJukeboxPlayer tile){
      super(x, y, Component.literal("Repeat Track"));
      this.tile = tile;
    }

    @Override
    protected boolean get_toggle_state(){
      return tile.getRepeat();
    }

    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new JukeboxMessage(tile.getBlockPos(), JukeboxMessage.Command.TOGGLE_REPEAT));
    }
  
  }

  public static class ToggleRedstoneCheckbox extends Checkbox {

    private final TileJukeboxPlayer tile;

    public ToggleRedstoneCheckbox(int x, int y, TileJukeboxPlayer tile){
      super(x, y, Component.literal("Output Redstone"));
      this.tile = tile;
    }

    @Override
    protected boolean get_toggle_state(){
      return tile.getOutputRedstone();
    }

    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new JukeboxMessage(tile.getBlockPos(), JukeboxMessage.Command.TOGGLE_OUTPUT_REDSTONE));
    }
  
  }

  public static class ResetButton extends AbstractButton {

    private final TileJukeboxPlayer tile;

    public ResetButton(int x, int y, TileJukeboxPlayer tile){
      super(x, y, 50, 18, Component.literal("Reset"));
      this.tile = tile;
    }

    @Override
    public void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new JukeboxMessage(tile.getBlockPos(), JukeboxMessage.Command.RESET));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput){
    }
  
  }

  public static class SongDelayTextBox extends EditBox {

    public SongDelayTextBox(Font font, int x, int y, TileJukeboxPlayer tile){
      super(font, x, y, 58, 18, Component.literal("0"));
    }
  
  }

}
