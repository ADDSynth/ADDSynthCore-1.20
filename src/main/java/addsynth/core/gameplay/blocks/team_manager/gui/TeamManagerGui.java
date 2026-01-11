package addsynth.core.gameplay.blocks.team_manager.gui;

import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gameplay.blocks.team_manager.data.TeamData;
import addsynth.core.gameplay.blocks.team_manager.network_messages.RequestPlayerScoreMessage;
import addsynth.core.gameplay.blocks.team_manager.network_messages.TeamManagerCommand;
import addsynth.core.gameplay.reference.ADDSynthCoreText;
import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.GuiBase;
import addsynth.core.gui.util.GuiUtil;
import addsynth.core.gui.widgets.WidgetUtil;
import addsynth.core.gui.widgets.scrollbar.CombinedListEntry;
import addsynth.core.gui.widgets.scrollbar.CombinedNameScrollbar;
import addsynth.core.util.game.data.CombinedNameComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public final class TeamManagerGui extends GuiBase {

  private static final Component         team_header_text = Component.translatable("gui.addsynthcore.team_manager.main.teams");
  private static final Component  all_players_header_text = Component.translatable("gui.addsynthcore.team_manager.main.all_players");
  private static final Component team_players_header_text = Component.translatable("gui.addsynthcore.team_manager.main.players_in_team");
  private static final Component    objective_header_text = Component.translatable("gui.addsynthcore.team_manager.main.objectives");
  private static final Component       team_selected_text = Component.translatable("gui.addsynthcore.team_manager.main.team_selected");
  private static final Component     player_selected_text = Component.translatable("gui.addsynthcore.team_manager.main.player_selected");
  private static final Component  objective_selected_text = Component.translatable("gui.addsynthcore.team_manager.main.objective_selected");
  private static final Component       current_score_text = Component.translatable("gui.addsynthcore.team_manager.score.current_score");
  private static final Component         input_value_text = Component.translatable("gui.addsynthcore.team_manager.score.input");
  private static final Component display_slot_header_text = Component.translatable("gui.addsynthcore.team_manager.display_slot.header");
  private static final Component      display_slot_text_1 = Component.translatable("gui.addsynthcore.team_manager.display_slot.player_list");
  private static final Component      display_slot_text_2 = Component.translatable("gui.addsynthcore.team_manager.display_slot.sidebar");
  private static final Component      display_slot_text_3 = Component.translatable("gui.addsynthcore.team_manager.display_slot.nametag");

  @Nonnull
  private CombinedNameComponent team_selected = CombinedNameComponent.EMPTY;
  @Nonnull
  private CombinedNameComponent player_selected = CombinedNameComponent.EMPTY;
  @Nonnull
  private CombinedNameComponent objective_selected = CombinedNameComponent.EMPTY;
  private static boolean team_is_selected;
  private static boolean player_is_selected;
  private static boolean player_selected_position;
  private static boolean objective_is_selected;

  public final String getPlayerSelected(){ return player_selected.getName(); }
  public final String getTeamSelected(){ return team_selected.getName(); }
  public final String getObjectiveSelected(){ return objective_selected.getName(); }
  public final void unSelectPlayer(){ player_selected = CombinedNameComponent.EMPTY; }
  public final void unSelectTeam(){ team_selected = CombinedNameComponent.EMPTY; }
  public final void unSelectObjective(){ objective_selected = CombinedNameComponent.EMPTY; }

  private static final int        team_list_size = 8;
  private static final int   objective_list_size = 8;
  private static final int      player_list_size = 11;
  // private static final int team_player_list_size = 11;

  private final CombinedListEntry[] team_entries = new CombinedListEntry[team_list_size];
  private CombinedNameScrollbar team_list;

  private final CombinedListEntry[] all_players = new CombinedListEntry[player_list_size];
  private CombinedNameScrollbar all_players_list;

  private final CombinedListEntry[] team_players = new CombinedListEntry[player_list_size];
  private CombinedNameScrollbar team_players_list;

  private final CombinedListEntry[] objectives_entries = new CombinedListEntry[objective_list_size];
  private CombinedNameScrollbar objectives_list;

  private final BiConsumer<CombinedNameComponent, Integer> onTeamSelected = (CombinedNameComponent value, Integer id) -> {
    team_selected = value;
    if(!player_selected_position){
      unSelectPlayer();
    }
    updateTeamPlayerList();
  };
  private final BiConsumer<CombinedNameComponent, Integer> onPlayerSelected = (CombinedNameComponent value, Integer id) -> {
    if(value != null){
      player_selected = value;
      player_selected_position = true;
      team_players_list.unSelect();
    }
  };
  private final BiConsumer<CombinedNameComponent, Integer> onTeamPlayerSelected = (CombinedNameComponent value, Integer id) -> {
    if(value != null){
      player_selected = value;
      player_selected_position = false;
      all_players_list.unSelect();
    }
  };
  private final BiConsumer<CombinedNameComponent, Integer> onObjectiveSelected = (CombinedNameComponent value, Integer id) -> {
    objective_selected = value;
  };

  private TeamManagerButtons.MovePlayerToTeamButton player_to_team_button;
  private TeamManagerButtons.RemovePlayerFromTeamButton player_from_team_button;
  private Button edit_team_button;
  private Button delete_team_button;
  private Button edit_objective_button;
  private Button delete_objective_button;
  private EditBox new_score;
  private Button set_score_button;
  private Button add_score_button;
  private Button subtract_score_button;
  private Button[] display_slot_button = new Button[3];

  public static int player_score;
  private static boolean player_score_can_be_changed;

  public TeamManagerGui(){
    super(442, 326, ADDSynthCoreText.team_manager, GuiReference.team_manager);
    TeamData.changed = true;
  }

  /** gap between the 3 list sections. */
  private static final int list_spacing = 10;
  private static final int list_width = 119;
  // private static final int player_list_width = 120;
  // private static final int objectives_list_width = 120;
  private static final int scrollbar_width = 12;
  private static final int entry_height = 11;

  private static final int button_y_spacing = 8;
  private static final int button_x_spacing = 4;
  private static final int button_height = 20; // max button height is 20
  private static final int widget_spacing = 2;
  private static final int text_box_width = 80;
  private static final int text_box_height = 14;

  private static final int       team_list_height = entry_height*team_list_size;
  private static final int objectives_list_height = entry_height*objective_list_size;
  private static final int     player_list_height = entry_height*player_list_size;

  private static final int text_space = 11;

  private static final int text_x1 = 12;
  private static final int text_x2 = text_x1 + list_width + scrollbar_width + list_spacing;
  private static final int text_x3 = text_x2 + list_width + scrollbar_width + list_spacing;
  private static final int line_1 = 18;

  private static final int player_buttons_y = line_1 + text_space + player_list_height + button_x_spacing;
  /** Y position all players header text should draw at. */
  private static final int players_text_line = player_buttons_y + TeamManagerButtons.player_button_size + button_x_spacing;
  

  private int selected_text_left;
  private int selected_text_right;

  private static final int selected_text_y = line_1 + text_space + team_list_height + button_y_spacing + button_height + 8 + button_y_spacing;
  private static final int score_spacing = 6;
  /** Current Score text. */
  private static final int line_2 = selected_text_y + 30 + 7; // 30 is the 3 lines of text, 7 is spacing
  private static final int text_box_x_adjust = 74;
  private static final int text_box_y = line_2 + 8 + score_spacing;
  /** New Score text. */
  private static final int line_3 = WidgetUtil.centerYAdjacent(text_box_y, text_box_height);

  private static final int line_4 = text_box_y + text_box_height + score_spacing + button_height + button_y_spacing;
  private static final int display_slot_spacing = 2;
  private static final int display_slot_button_y1 = line_4 + text_space;
  private static final int display_slot_button_y2 = display_slot_button_y1 + TeamManagerButtons.display_slot_button_height + display_slot_spacing;
  private static final int display_slot_button_y3 = display_slot_button_y2 + TeamManagerButtons.display_slot_button_height + display_slot_spacing;
  private static final int display_slot_text_y1 = WidgetUtil.centerYAdjacent(display_slot_button_y1, TeamManagerButtons.display_slot_button_height);
  private static final int display_slot_text_y2 = WidgetUtil.centerYAdjacent(display_slot_button_y2, TeamManagerButtons.display_slot_button_height);
  private static final int display_slot_text_y3 = WidgetUtil.centerYAdjacent(display_slot_button_y3, TeamManagerButtons.display_slot_button_height);
  private static final int last_x = text_x3 + list_width + scrollbar_width;
  private static final int display_slot_button_x2 = last_x - TeamManagerButtons.display_slot_button_width;
  private static final int display_slot_button_x1 = display_slot_button_x2 - TeamManagerButtons.display_slot_button_width - 6;
  private int display_slot_x1;
  private int display_slot_x2;

  @Override
  protected final void init(){
    super.init();
    
    // Common Data
    int i;
    final int start_y = guiBox.top + line_1 + text_space;
    final int x_position_1 = guiBox.left + text_x1;
    final int x_position_2 = guiBox.left + text_x2;
    final int x_position_3 = guiBox.left + text_x3;

    // Selected
    selected_text_left  = text_x2 + GuiUtil.getMaxStringWidth(font, team_selected_text, player_selected_text, objective_selected_text);
    selected_text_right = selected_text_left + 5;

    // Player Controls
    final int player_x_center = x_position_1 + (list_width / 2);
    final int player_button_x1 = player_x_center - 3 - TeamManagerButtons.player_button_size;
    final int player_button_x2 = player_x_center + 3;
    final int player_list_y = guiBox.top + players_text_line + text_space;
    for(i = 0; i < team_players.length; i++){
      team_players[i] = new CombinedListEntry(x_position_1, start_y + (entry_height * i), list_width, entry_height);
      addRenderableWidget(team_players[i]);
    }
    team_players_list = new CombinedNameScrollbar(x_position_1 + list_width, start_y, player_list_height, team_players);
    team_players_list.setResponder(onTeamPlayerSelected);
    addRenderableWidget(team_players_list);
    
    player_to_team_button   = new TeamManagerButtons.MovePlayerToTeamButton(    this, player_button_x1, guiBox.top + player_buttons_y);
    player_from_team_button = new TeamManagerButtons.RemovePlayerFromTeamButton(this, player_button_x2, guiBox.top + player_buttons_y);
    addRenderableWidget(player_to_team_button);
    addRenderableWidget(player_from_team_button);
    
    for(i = 0; i < all_players.length; i++){
      all_players[i] = new CombinedListEntry(x_position_1, player_list_y + (entry_height * i), list_width, entry_height);
      addRenderableWidget(all_players[i]);
    }
    all_players_list = new CombinedNameScrollbar(x_position_1 + list_width, player_list_y, player_list_height, all_players);
    all_players_list.setResponder(onPlayerSelected);
    addRenderableWidget(all_players_list);
    
    // Team controls
    final int team_buttons_y = start_y + team_list_height + button_y_spacing;
    for(i = 0; i < team_entries.length; i++){
      team_entries[i] = new CombinedListEntry(x_position_2, start_y + (entry_height * i), list_width, entry_height);
      addRenderableWidget(team_entries[i]);
    }
    team_list = new CombinedNameScrollbar(x_position_2 + list_width, start_y, team_list_height, team_entries);
    team_list.setResponder(onTeamSelected);
    addRenderableWidget(team_list);
      edit_team_button = TeamManagerButtons.getEditTeamButton(  this, x_position_2 + 34, team_buttons_y, 30, button_height);
    delete_team_button = TeamManagerButtons.getDeleteTeamButton(this, x_position_2 + 68, team_buttons_y, 50, button_height);
    addRenderableWidget( TeamManagerButtons.getAddTeamButton(         x_position_2,      team_buttons_y, 30, button_height));
    addRenderableWidget(edit_team_button);
    addRenderableWidget(delete_team_button);

    // Objectives
    final int objective_buttons_y = start_y + objectives_list_height + button_y_spacing;
    for(i = 0; i < objectives_entries.length; i++){
      objectives_entries[i] = new CombinedListEntry(x_position_3, start_y + (entry_height * i), list_width, entry_height);
      addRenderableWidget(objectives_entries[i]);
    }
    objectives_list = new CombinedNameScrollbar(x_position_3 + list_width, start_y, objectives_list_height, objectives_entries);
    objectives_list.setResponder(onObjectiveSelected);
    addRenderableWidget(objectives_list);
    edit_objective_button   = TeamManagerButtons.getEditObjectiveButton(  this, x_position_3 + 34, objective_buttons_y, 30, button_height);
    delete_objective_button = TeamManagerButtons.getDeleteObjectiveButton(this, x_position_3 + 68, objective_buttons_y, 50, button_height);
    addRenderableWidget(      TeamManagerButtons.getAddObjectiveButton(         x_position_3,      objective_buttons_y, 30, button_height));
    addRenderableWidget(edit_objective_button);
    addRenderableWidget(delete_objective_button);
    
    // Score Controls
    new_score = new EditBox(this.font, x_position_2 + text_box_x_adjust, guiBox.top + text_box_y, text_box_width, text_box_height, Component.literal("0"));
    addWidget(new_score);
    final int score_buttons_y = guiBox.top + text_box_y + text_box_height + score_spacing;
    final int[] score_buttons_width = {70, 50, 75, 60};
    final int score_button_x2 =    x_position_2 + score_buttons_width[0] + button_x_spacing;
    final int score_button_x3 = score_button_x2 + score_buttons_width[1] + button_x_spacing;
    subtract_score_button = TeamManagerButtons.getSubtractScoreButton(x_position_2, score_buttons_y, score_buttons_width[0], button_height, this::subtract_score);
         add_score_button = TeamManagerButtons.getAddScoreButton(  score_button_x2, score_buttons_y, score_buttons_width[1], button_height, this::add_score);
         set_score_button = TeamManagerButtons.getSetScoreButton(  score_button_x3, score_buttons_y, score_buttons_width[2], button_height, this::set_score);
    addRenderableWidget(subtract_score_button);
    addRenderableWidget(add_score_button);
    addRenderableWidget(set_score_button);
    
    // Display Slot widgets
    display_slot_x1 = text_x2 + GuiUtil.getMaxStringWidth(font, display_slot_text_1, display_slot_text_2, display_slot_text_3);
    display_slot_x2 = (display_slot_x1 + 5 + display_slot_button_x1) / 2;
    display_slot_button[0] = TeamManagerButtons.getSetDisplaySlotButton(this, guiBox.left + display_slot_button_x1, guiBox.top + display_slot_button_y1, 0);
    display_slot_button[1] = TeamManagerButtons.getSetDisplaySlotButton(this, guiBox.left + display_slot_button_x1, guiBox.top + display_slot_button_y2, 1);
    display_slot_button[2] = TeamManagerButtons.getSetDisplaySlotButton(this, guiBox.left + display_slot_button_x1, guiBox.top + display_slot_button_y3, 2);
    addRenderableWidget(display_slot_button[0]);
    addRenderableWidget(display_slot_button[1]);
    addRenderableWidget(display_slot_button[2]);
    addRenderableWidget(TeamManagerButtons.getClearDisplaySlotButton(guiBox.left + display_slot_button_x2, guiBox.top + display_slot_button_y1, 0));
    addRenderableWidget(TeamManagerButtons.getClearDisplaySlotButton(guiBox.left + display_slot_button_x2, guiBox.top + display_slot_button_y2, 1));
    addRenderableWidget(TeamManagerButtons.getClearDisplaySlotButton(guiBox.left + display_slot_button_x2, guiBox.top + display_slot_button_y3, 2));
    
    TeamData.changed = true;
  }

  @Override
  public void tick(){
    super.tick();
    team_is_selected = team_selected != CombinedNameComponent.EMPTY;
    player_is_selected = player_selected != CombinedNameComponent.EMPTY;
    objective_is_selected = objective_selected != CombinedNameComponent.EMPTY;
    player_score_can_be_changed = objective_is_selected && player_is_selected && TeamData.canObjectiveBeModified(objective_selected.getName());
    updateButtons();
    updateLists();
    if(player_is_selected && objective_is_selected){
      NetworkHandler.INSTANCE.sendToServer(new RequestPlayerScoreMessage(player_selected.getName(), objective_selected.getName()));
    }
    new_score.tick();
  }

  private final void updateButtons(){
    player_to_team_button.active = player_is_selected && player_selected_position && team_is_selected;
    player_from_team_button.active = player_is_selected && !player_selected_position && team_is_selected;
    edit_team_button.active = team_is_selected;
    delete_team_button.active = team_is_selected;
    edit_objective_button.active = objective_is_selected;
    delete_objective_button.active = objective_is_selected;
    set_score_button.active = player_score_can_be_changed;
    add_score_button.active = player_score_can_be_changed;
    subtract_score_button.active = player_score_can_be_changed;
    display_slot_button[0].active = objective_is_selected;
    display_slot_button[1].active = objective_is_selected;
    display_slot_button[2].active = objective_is_selected;
  }

  private final void updateLists(){
    if(TeamData.changed){
      team_list.updateScrollbar(TeamData.getTeams());
      updateTeamPlayerList();
      all_players_list.updateScrollbar(TeamData.getPlayers());
      objectives_list.updateScrollbar(TeamData.getObjectives());
      TeamData.changed = false;
    }
  }

  private final void updateTeamPlayerList(){
    team_players_list.updateScrollbar(TeamData.getTeamPlayers(team_selected.getName()));
  }

  private final void set_score(){
    try{
      final String objective = objective_selected.getName();
      final String player = player_selected.getName();
      final int score = Integer.parseInt(new_score.getValue());
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.SetScore(objective, player, score));
    }
    catch(NumberFormatException e){}
  }

  private final void add_score(){
    try{
      final String objective = objective_selected.getName();
      final String player = player_selected.getName();
      final int score = Integer.parseInt(new_score.getValue());
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.AddScore(objective, player, score));
    }
    catch(NumberFormatException e){}
  }

  private final void subtract_score(){
    try{
      final String objective = objective_selected.getName();
      final String player = player_selected.getName();
      final int score = Integer.parseInt(new_score.getValue());
      NetworkHandler.INSTANCE.sendToServer(new TeamManagerCommand.SubtractScore(objective, player, score));
    }
    catch(NumberFormatException e){}
  }

  @Override
  public void render(GuiGraphics graphics, int mouse_x, int mouse_y, float partialTicks){
    super.render(graphics, mouse_x, mouse_y, partialTicks);
    new_score.render(graphics, mouse_x, mouse_y, partialTicks);
  }

  @Override
  protected final void drawGuiBackgroundLayer(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY){
    draw_custom_background_texture(graphics, 512, 384);
  }

  @Override
  protected final void drawGuiForegroundLayer(GuiGraphics graphics, final int mouseX, final int mouseY){
    draw_title(graphics);
    // Main Labels
    draw_text_left(graphics, team_players_header_text.getString()+":", text_x1, line_1);
    draw_text_left(graphics,  all_players_header_text.getString()+":", text_x1, players_text_line);
    draw_text_left(graphics,         team_header_text.getString()+":", text_x2, line_1);
    draw_text_left(graphics,    objective_header_text.getString()+":", text_x3, line_1);
    // Selected
    draw_text_right(graphics,      team_selected_text.getString()+":", selected_text_left, selected_text_y);
    draw_text_right(graphics,    player_selected_text.getString()+":", selected_text_left, selected_text_y + 10);
    draw_text_right(graphics, objective_selected_text.getString()+":", selected_text_left, selected_text_y + 20);
    draw_text_left(graphics,      team_selected.getPlainComponent(), selected_text_right, selected_text_y);
    draw_text_left(graphics,    player_selected.getName(), selected_text_right, selected_text_y + 10);
    draw_text_left(graphics, objective_selected.getPlainComponent(), selected_text_right, selected_text_y + 20);
    // Score Text
    draw_text_left(graphics, current_score_text.getString()+": "+player_score, text_x2, line_2);
    draw_text_left(graphics, input_value_text.getString()+":", text_x2, line_3);
    // Display Slot text
    draw_text_left(graphics, display_slot_header_text.getString()+":", text_x2, line_4);
    draw_text_right(graphics, display_slot_text_1, display_slot_x1, display_slot_text_y1);
    draw_text_right(graphics, display_slot_text_2, display_slot_x1, display_slot_text_y2);
    draw_text_right(graphics, display_slot_text_3, display_slot_x1, display_slot_text_y3);
    draw_text_center(graphics, TeamData.getDisplaySlotObjective(0), display_slot_x2, display_slot_text_y1);
    draw_text_center(graphics, TeamData.getDisplaySlotObjective(1), display_slot_x2, display_slot_text_y2);
    draw_text_center(graphics, TeamData.getDisplaySlotObjective(2), display_slot_x2, display_slot_text_y3);
  }

}
