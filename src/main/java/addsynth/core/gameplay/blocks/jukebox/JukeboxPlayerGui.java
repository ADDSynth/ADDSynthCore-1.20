package addsynth.core.gameplay.blocks.jukebox;

import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.GuiContainerBase;
import addsynth.core.gui.widgets.rect.ProgressBar;
import addsynth.core.util.java.StringUtil;
import addsynth.core.util.math.common.RoundMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;

public class JukeboxPlayerGui extends GuiContainerBase<JukeboxContainer> {

  private static final Component PLAYING_STATUS = Component.literal("Status: Playing");
  private static final Component STOPPED_STATUS = Component.literal("Status: Stopped");
  private static final Component TRACK          = Component.literal("Track:");

  private static final int guiWidth = 361;
  private static final int guiHeight = 149;
  private final ProgressBar progress_bar = new ProgressBar(14, 60, 160, 5, 14, 158, 384, 192);
  
  private static final int left_end = (guiWidth / 2) - 6;
  @SuppressWarnings("hiding")
  private static final int center_x = (6 + left_end) / 2;
  private static final int right_side = 186;
  private static final int line_2 = 17;
  private static final int itemstackY = 28;
  private static final int line_3 = 32; // itemstack line
  private static final int itemstackNameX = 25;
  private static final int line_4 = 47;

  private static final int   buttonY = 69;
  private static final int[] buttonX = {23, 66, 119};

  private static final int checkbox_line_1 =  6;
  private static final int checkbox_line_2 = 24;
  private static final int checkbox_line_3 = 42;
  private static final int checkbox_second_column = 252;
  // private static final int playerInventoryX = 180;
  private static final int playerInventoryY = 55;

  private final TileJukeboxPlayer tile;
  private ItemStack itemstack = ItemStack.EMPTY;

  private JukeboxGuiComponents.PlayStopButton playStopButton;
  private JukeboxGuiComponents.SkipLeftButton skipLeftButton;
  private JukeboxGuiComponents.SkipRightButton skipRightButton;
  private JukeboxGuiComponents.SongDelayTextBox trackDelayEditBox;

  public JukeboxPlayerGui(JukeboxContainer container, Inventory player_inventory, Component title){
    super(guiWidth, guiHeight, container, player_inventory, title, GuiReference.auto_jukebox);
    tile = container.getTileEntity();
  }

  @Override
  protected final void init(){
    super.init();
    // control buttons
    skipLeftButton = new JukeboxGuiComponents.SkipLeftButton(leftPos + buttonX[0], topPos + buttonY, tile);
    addRenderableWidget(skipLeftButton);
    playStopButton = new JukeboxGuiComponents.PlayStopButton(leftPos + buttonX[1], topPos + buttonY, tile);
    addRenderableWidget(playStopButton);
    skipRightButton = new JukeboxGuiComponents.SkipRightButton(leftPos + buttonX[2], topPos + buttonY, tile);
    addRenderableWidget(skipRightButton);
    // checkboxes
    addRenderableWidget(new JukeboxGuiComponents.ToggleShuffleCheckbox(leftPos + right_side, topPos + checkbox_line_1, tile));
    addRenderableWidget(new JukeboxGuiComponents.ToggleRepeatCheckbox( leftPos + right_side, topPos + checkbox_line_2, tile));
    addRenderableWidget(new JukeboxGuiComponents.ToggleRedstoneCheckbox(leftPos + checkbox_second_column, topPos + checkbox_line_1, tile));
    // extra features
    addRenderableWidget(new JukeboxGuiComponents.ResetButton(leftPos + 300, topPos + checkbox_line_2 - 2, tile));
    trackDelayEditBox = new JukeboxGuiComponents.SongDelayTextBox(font, leftPos + 293, topPos + checkbox_line_3 + 2, tile);
    addRenderableWidget(trackDelayEditBox);
  }

  @Override
  protected final void containerTick(){
    itemstack = tile.getItemStack();
    playStopButton.active = tile.hasMusicDiscs();
    skipLeftButton.active = tile.canSkipLeft();
    skipRightButton.active = tile.canSkipRight();
    trackDelayEditBox.tick();
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY){
    draw_custom_background_texture(graphics, 384, 192);
    progress_bar.draw(graphics, this, ProgressBar.Direction.LEFT_TO_RIGHT, tile.getSongPercentage(), RoundMode.Round);
  }

  @Override
  protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY){
    // titles
    draw_text_center(graphics, title, center_x, 6);
    graphics.drawString(font, playerInventoryTitle, right_side, playerInventoryY, 4210752, false);
    // status
    graphics.drawString(font, tile.isPlaying() ? PLAYING_STATUS : STOPPED_STATUS, 6, line_2, 4210752, false);
    draw_text_right(graphics, Component.literal(StringUtil.build("Track: ", tile.getIndex(), " / ", tile.getNumberOfTracks())), left_end, line_2);
    // draw song + time
    if(!itemstack.isEmpty()){
      graphics.renderItem(itemstack, 6, itemstackY);
      final Component displayName = (itemstack.getItem() instanceof RecordItem record) ? record.getDisplayName() : itemstack.getDisplayName();
      graphics.drawString(font, displayName, itemstackNameX, line_3, 4210752, false);
    }
    else{
      graphics.drawString(font, "------------", itemstackNameX, line_3, 4210752, false);
    }
    graphics.drawString(font, Component.literal(tile.getTime() + " / " + tile.getTotalTime()), 6, line_4, 4210752, false);
    draw_text_right(graphics, StringUtil.toPercentageString(tile.getSongPercentage()), left_end, line_4);
    // settings
    graphics.drawString(font, Component.literal("Track Delay Time:"), right_side, checkbox_line_3, 4210752, false);
  }

}
