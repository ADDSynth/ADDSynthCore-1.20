package addsynth.core.gameplay.blocks.team_manager.gui;

import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gameplay.blocks.team_manager.data.TeamData;
import addsynth.core.gameplay.blocks.team_manager.data.TeamDataUnit;
import addsynth.core.gameplay.blocks.team_manager.network_messages.TeamManagerCommand;
import addsynth.core.gameplay.reference.GuiReference;
import addsynth.core.gui.GuiBase;
import addsynth.core.gui.widgets.buttons.ClientCheckbox;
import addsynth.core.gui.widgets.buttons.RadialButtonGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public final class TeamManagerTeamEditGui extends GuiBase {

  private static final Component           team_gui_title = Component.translatable("gui.addsynthcore.team_manager.team_edit.gui_title");
  private static final Component        team_id_name_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.id_name");
  private static final Component   team_display_name_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.display_name");
  private static final Component       friendly_fire_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.friendly_fire");
  private static final Component see_invisible_allys_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.see_invisible_allys");
  private static final Component          team_color_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.team_color");
  private static final Component show_nametag_option_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.show_nametag");
  private static final Component show_death_messages_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.show_death_messages");
  private static final Component       member_prefix_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.member_prefix");
  private static final Component       member_suffix_text = Component.translatable("gui.addsynthcore.team_manager.team_edit.member_suffix");

  private final boolean new_team;
  private final String existing_team;
  private Component message = Component.empty();
  private EditBox team_id_name;
  private EditBox team_display_name;
  private ClientCheckbox friendly_fire;
  private ClientCheckbox see_invisible_allys;
  private ColorButtons color_buttons;
  private RadialButtonGroup nametag_controls;
  private RadialButtonGroup death_message_controls;
  private EditBox member_prefix;
  private EditBox member_suffix;
  private Button finish_button;

  private static final int center_space = 3;
  private static final int widget_spacing = 2;
  /** Used to space out the sections of elements on the gui. */
  private static final int spacing = 8;
  private static final int text_box_height = 14;
  private static final int button_width = 80;
  private static final int button_height = 20;
  private static final Component[] team_options = {
    Component.translatable("gui.addsynthcore.team_manager.team_edit.always"),
    Component.translatable("gui.addsynthcore.team_manager.team_edit.never"),
    Component.translatable("gui.addsynthcore.team_manager.team_edit.this_team_only"),
    Component.translatable("gui.addsynthcore.team_manager.team_edit.other_teams_only")
  };

  // Text
  private static final int left_text_x = 6;
  private int right_text_x;
  private static final int line_1 = 18;
  private static final int line_2 = line_1 + 8 + widget_spacing + text_box_height + spacing;
  private static final int line_3 = line_2 + 8 + widget_spacing + (ColorButtons.gui_height) + spacing;
  private static final int line_4 = line_3 + 8 + widget_spacing + (RadialButtonGroup.radial_gui_size*4)+(RadialButtonGroup.line_spacing*3) + spacing;
  private static final int line_5 = line_4 + 8 + widget_spacing + text_box_height + 6;


  public TeamManagerTeamEditGui(){
    super(274, 244, team_gui_title, GuiReference.edit_team_gui);
    new_team = true;
    existing_team = null;
  }

  public TeamManagerTeamEditGui(final String existing_team){
    super(274, 244, team_gui_title, GuiReference.edit_team_gui);
    new_team = false;
    this.existing_team = existing_team;
  }

  @Override
  protected final void init(){
    super.init();
    
    right_text_x = center_x + center_space;

    // Widgets
    final int left_x     = guiBox.left + 6;
    final int left_edge  = guiBox.center_x - center_space;
    final int right_x    = guiBox.center_x + center_space;
    final int right_edge = guiBox.right - 6;
    final int widget_line_1 = guiBox.top + line_1 + 8 + widget_spacing;
    final int widget_line_2 = guiBox.top + line_2 + 8 + widget_spacing;
    final int widget_line_3 = guiBox.top + line_3 + 8 + widget_spacing;
    final int widget_line_4 = guiBox.top + line_4 + 8 + widget_spacing;
    final int button_y      = guiBox.top + line_5 + 8 + 4;
    final int left_text_box_width  =  left_edge -  left_x;
    final int right_text_box_width = right_edge - right_x; // even though technically these should be the same
    final int button_x1 = (( left_x +  left_edge) / 2) - (button_width/2);
    final int button_x2 = ((right_x + right_edge) / 2) - (button_width/2);
    
    team_id_name           = new EditBox(this.font,  left_x, widget_line_1,  left_text_box_width, text_box_height, Component.empty());
    team_display_name      = new EditBox(this.font, right_x, widget_line_1, right_text_box_width, text_box_height, Component.empty());
    friendly_fire          = new ClientCheckbox(     left_x, widget_line_2 + 2, friendly_fire_text);
    see_invisible_allys    = new ClientCheckbox(     left_x, widget_line_2 + ColorButtons.button_gui_size + 2, see_invisible_allys_text);
    color_buttons          = new ColorButtons(      right_x, widget_line_2,
      (Integer color) -> {
        // team_display_name.setTextColor(TextFormatting.fromColorIndex(color).getColor());
      }
    );
    nametag_controls       = new RadialButtonGroup(          left_x, widget_line_3, team_options);
    death_message_controls = new RadialButtonGroup(         right_x, widget_line_3, team_options);
    member_prefix          = new EditBox(this.font,  left_x, widget_line_4, left_text_box_width, text_box_height, Component.empty());
    member_suffix          = new EditBox(this.font, right_x, widget_line_4, right_text_box_width, text_box_height, Component.empty());
    
    addRenderableWidget(friendly_fire);
    addRenderableWidget(see_invisible_allys);
    addRenderableWidget(color_buttons);
    addRenderableWidget(nametag_controls);
    addRenderableWidget(death_message_controls);
    addWidget(team_id_name);
    addWidget(team_display_name);
    addWidget(member_prefix);
    addWidget(member_suffix);
    finish_button = TeamManagerButtons.getFinishButton(button_x1, button_y, button_width, button_height, this::create_team);
    addRenderableWidget(finish_button);
    addRenderableWidget(TeamManagerButtons.getCancelButton(button_x2, button_y, button_width, button_height));

    if(new_team == false){
      // editing pre-existing team, load all data
      final TeamDataUnit team = TeamData.getTeamData(existing_team);
      team_id_name.setValue(team.name);
      // team_id_name.isEnabled = false;
      team_display_name.setValue(team.display_name.getString());
      color_buttons.setColor(team.color);
      friendly_fire.checked = team.pvp;
      see_invisible_allys.checked = team.see_invisible_allys;
      nametag_controls.option_selected = team.nametag_option;
      death_message_controls.option_selected = team.death_message_option;
      member_prefix.setValue(team.prefix.getString());
      member_suffix.setValue(team.suffix.getString());
    }
  }

  public final void create_team(){
    final String team_name            = team_id_name.getValue().replace(' ', '_');
    final String display_name         = team_display_name.getValue();
    final boolean pvp                 = friendly_fire.checked;
    final boolean see_invisible_allys = this.see_invisible_allys.checked;
    final int team_color              = color_buttons.getColor();
    final int nametag_option          = nametag_controls.getSelectedOption();
    final int death_message_option    = death_message_controls.getSelectedOption();
    final String prefix               = member_prefix.getValue();
    final String suffix               = member_suffix.getValue();
    if(new_team){
      NetworkHandler.INSTANCE.sendToServer(
        new TeamManagerCommand.AddTeam(team_name, display_name, pvp, see_invisible_allys, team_color, nametag_option, death_message_option, prefix, suffix)
      );
    }
    else{
      NetworkHandler.INSTANCE.sendToServer(
        new TeamManagerCommand.EditTeam(team_name, display_name, pvp, see_invisible_allys, team_color, nametag_option, death_message_option, prefix, suffix)
      );
    }
  }

  @Override
  public final void tick(){
    super.tick();
    team_id_name.tick();
    team_display_name.tick();
    member_prefix.tick();
    member_suffix.tick();
    validate();
  }

  private final void validate(){
    if(team_id_name.getValue().isEmpty()){
      message = TeamManagerMessage.must_specify_name;
      finish_button.active = false;
      return;
    }
    if(team_id_name.getValue().contains(" ")){
      message = TeamManagerMessage.cannot_contain_spaces;
      finish_button.active = false;
      return;
    }
    if(team_id_name.getValue().length() > 16){
      message = TeamManagerMessage.must_be_shorter;
      finish_button.active = false;
      return;
    }
    message = Component.empty();
    finish_button.active = true;
  }

  @Override
  public final void render(GuiGraphics graphics, final int mouse_x, final int mouse_y, final float partialTicks){
    super.render(graphics, mouse_x, mouse_y, partialTicks);
         team_id_name.render(graphics, mouse_x, mouse_y, partialTicks);
    team_display_name.render(graphics, mouse_x, mouse_y, partialTicks);
        member_prefix.render(graphics, mouse_x, mouse_y, partialTicks);
        member_suffix.render(graphics, mouse_x, mouse_y, partialTicks);
  }

  @Override
  protected void drawGuiBackgroundLayer(GuiGraphics graphics, float partialTicks, int mouse_x, int mouse_y){
    draw_custom_background_texture(graphics, 384, 256);
  }

  @Override
  protected void drawGuiForegroundLayer(GuiGraphics graphics, int mouseX, int mouseY){
    draw_title(graphics);
    draw_text_left(graphics, team_id_name_text.getString()+":",         left_text_x, line_1);
    draw_text_left(graphics, team_display_name_text.getString()+":",   right_text_x, line_1);
    draw_text_left(graphics, team_color_text.getString()+":",          right_text_x, line_2);
    draw_text_left(graphics, show_nametag_option_text.getString()+":",  left_text_x, line_3);
    draw_text_left(graphics, show_death_messages_text.getString()+":", right_text_x, line_3);
    draw_text_left(graphics, member_prefix_text.getString()+":",        left_text_x, line_4);
    draw_text_left(graphics, member_suffix_text.getString()+":",       right_text_x, line_4);
    draw_text_left(graphics, message,                       left_text_x, line_5);
  }

}
