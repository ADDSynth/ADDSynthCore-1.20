package addsynth.core.gameplay.blocks.team_manager.gui;

import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gameplay.blocks.team_manager.network_messages.TeamManagerCommand;
import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.widgets.WidgetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public final class TeamManagerButtons {

  private static final Component add            = Component.translatable("gui.addsynthcore.team_manager.common.add");
  private static final Component edit           = Component.translatable("gui.addsynthcore.team_manager.common.edit");
  private static final Component delete         = Component.translatable("gui.addsynthcore.team_manager.common.delete");
  private static final Component done           = Component.translatable("gui.addsynthcore.team_manager.common.done");
  private static final Component cancel         = Component.translatable("gui.addsynthcore.team_manager.common.cancel");
  private static final Component add_score      = Component.translatable("gui.addsynthcore.team_manager.score.add");
  private static final Component subtract_score = Component.translatable("gui.addsynthcore.team_manager.score.subtract");
  private static final Component set_score      = Component.translatable("gui.addsynthcore.team_manager.score.set");
  private static final Component reset          = Component.translatable("gui.addsynthcore.team_manager.score.reset");
  private static final Component assign         = Component.translatable("gui.addsynthcore.team_manager.display_slot.assign");
  private static final Component clear          = Component.translatable("gui.addsynthcore.team_manager.display_slot.clear");

  private static final Minecraft minecraft = Minecraft.getInstance();
  public final static int player_button_size = 20;
  public final static int display_slot_button_width = 50;
  public final static int display_slot_button_height = 14;

  public static final Button getAddTeamButton(int x, int y, int width, int height){
    return Button.builder(add, (Button button) -> {
      minecraft.setScreen(new TeamManagerTeamEditGui());
    }).bounds(x, y, width, height).build();
  }

  public static final Button getEditTeamButton(TeamManagerGui gui, int x, int y, int width, int height){
    return Button.builder(edit, (Button button) -> {
      minecraft.setScreen(new TeamManagerTeamEditGui(gui.getTeamSelected()));
    }).bounds(x, y, width, height).build();
  }

  public static final Button getDeleteTeamButton(TeamManagerGui gui, int x, int y, int width, int height){
    return Button.builder(delete, (Button button) -> {
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.DeleteTeam(gui.getTeamSelected()));
      gui.unSelectTeam();
    }).bounds(x, y, width, height).build();
  }

  public static final class MovePlayerToTeamButton extends AbstractButton {

    private final TeamManagerGui gui;
    private final int texture_x = 20;
    private final int texture_y = 184;
    private int final_texture_y;

    public MovePlayerToTeamButton(TeamManagerGui gui, int xIn, int yIn){
      super(xIn, yIn, player_button_size, player_button_size, Component.empty());
      this.gui = gui;
    }

    @Override
    public final void renderWidget(GuiGraphics graphics, int mouse_x, int mouse_y, float partial_ticks){
      final_texture_y = texture_y + (active ? isHovered ? player_button_size : 0 : -player_button_size);
      WidgetUtil.renderButton(graphics, this, GuiReference.widgets, texture_x, final_texture_y, player_button_size, player_button_size);
    }

    @Override
    public final void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.AddPlayerToTeam(gui.getPlayerSelected(), gui.getTeamSelected()));
      gui.unSelectPlayer();
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_){
    }
  
  }
  
  public static final class RemovePlayerFromTeamButton extends AbstractButton {

    private final TeamManagerGui gui;
    private final int texture_x = 0;
    private final int texture_y = 184;
    private int final_texture_y;

    public RemovePlayerFromTeamButton(TeamManagerGui gui, int xIn, int yIn){
      super(xIn, yIn, player_button_size, player_button_size, Component.empty());
      this.gui = gui;
    }

    @Override
    public final void renderWidget(GuiGraphics graphics, int mouse_x, int mouse_y, float partial_ticks){
      final_texture_y = texture_y + (active ? isHovered ? player_button_size : 0 : -player_button_size);
      WidgetUtil.renderButton(graphics, this, GuiReference.widgets, texture_x, final_texture_y, player_button_size, player_button_size);
    }

    @Override
    public final void onPress(){
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.RemovePlayerFromTeam(gui.getPlayerSelected(), gui.getTeamSelected()));
      gui.unSelectPlayer();
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_){
    }
  
  }

  public static final Button getAddObjectiveButton(int x, int y, int width, int height){
    return Button.builder(add, (Button button) -> {
      minecraft.setScreen(new TeamManagerObjectiveGui());
    }).bounds(x, y, width, height).build();
  }

  public static final Button getEditObjectiveButton(TeamManagerGui gui, int x, int y, int width, int height){
    return Button.builder(edit, (Button button) -> {
      minecraft.setScreen(new TeamManagerObjectiveGui(gui.getObjectiveSelected()));
    }).bounds(x, y, width, height).build();
  }

  public static final Button getDeleteObjectiveButton(TeamManagerGui gui, int x, int y, int width, int height){
    return Button.builder(delete, (Button button) -> {
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.DeleteObjective(gui.getObjectiveSelected()));
      gui.unSelectObjective();
    }).bounds(x, y, width, height).build();
  }

  public static final Button getFinishButton(int x, int y, int width, int height, Runnable onClick){
    return Button.builder(done, (Button button) -> {
      onClick.run();
      minecraft.setScreen(new TeamManagerGui());
    }).bounds(x, y, width, height).build();
  }

  public static final Button getCancelButton(int x, int y, int width, int height){
    return Button.builder(cancel, (Button button) -> minecraft.setScreen(new TeamManagerGui()))
      .bounds(x, y, width, height).build();
  }

  public static final Button getSubtractScoreButton(int x, int y, int width, int height, Runnable onClick){
    return Button.builder(subtract_score, (Button button) -> onClick.run()).bounds(x, y, width, height).build();
  }

  public static final Button getAddScoreButton(int x, int y, int width, int height, Runnable onClick){
    return Button.builder(add_score, (Button button) -> onClick.run()).bounds(x, y, width, height).build();
  }

  public static final Button getSetScoreButton(int x, int y, int width, int height, Runnable onClick){
    return Button.builder(set_score, (Button button) -> onClick.run()).bounds(x, y, width, height).build();
  }

  public static final Button getResetScoreButton(TeamManagerGui gui, int x, int y, int width, int height){
    return Button.builder(reset, (Button button) -> {
      NetworkHandler.INSTANCE.sendToServer(
        new TeamManagerCommand.ResetScore(gui.getObjectiveSelected(), gui.getPlayerSelected())
      );
    }).bounds(x, y, width, height).build();
  }
  
  public static final Button getSetDisplaySlotButton(TeamManagerGui gui, int x, int y, int display_slot){
    return Button.builder(assign, (Button button) -> {
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.SetDisplaySlot(gui.getObjectiveSelected(), display_slot));
    }).bounds(x, y, display_slot_button_width, display_slot_button_height).build();
  }
  
  public static final Button getClearDisplaySlotButton(int x, int y, int display_slot){
    return Button.builder(clear, (Button button) -> {
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.ClearDisplaySlot(display_slot));
    }).bounds(x, y, display_slot_button_width, display_slot_button_height).build();
  }

}
