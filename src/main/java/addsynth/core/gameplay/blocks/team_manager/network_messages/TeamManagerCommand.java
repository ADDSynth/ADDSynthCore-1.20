package addsynth.core.gameplay.blocks.team_manager.network_messages;

import addsynth.core.gameplay.blocks.team_manager.data.TeamData;
import addsynth.core.util.game.data.ScoreUtil;
import addsynth.core.util.network.INetworkMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team.Visibility;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public final class TeamManagerCommand {

  public static final class ClearDisplaySlot extends INetworkMessage {
    private final int display_slot;
    public ClearDisplaySlot(int display_slot){
      this.display_slot = display_slot;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeInt(display_slot);
    }
    public static final ClearDisplaySlot decode(final FriendlyByteBuf data){
      return new ClearDisplaySlot(data.readInt());
    }
    @Override
    protected final void handle(final ServerPlayer player){
      final MinecraftServer server = player.server;
      final Scoreboard scoreboard = server.getScoreboard();
      scoreboard.setDisplayObjective(display_slot, null);
      TeamData.sync(server, scoreboard);
    }
  }
  
  public static final class DeleteTeam extends INetworkMessage {
    private final String team_name;
    public DeleteTeam(String team_name){
      this.team_name = team_name;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(team_name);
    }
    public static final DeleteTeam decode(final FriendlyByteBuf data){
      return new DeleteTeam(data.readUtf());
    }
    @Override
    protected final void handle(final ServerPlayer player){
      final MinecraftServer server = player.server;
      final Scoreboard scoreboard = server.getScoreboard();
      scoreboard.removePlayerTeam(scoreboard.getPlayerTeam(team_name));
      TeamData.sync(server, scoreboard);
    }
  }
  
  public static final class DeleteObjective extends INetworkMessage {
    private final String objective_name;
    public DeleteObjective(String objective_name){
      this.objective_name = objective_name;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(objective_name);
    }
    public static final DeleteObjective decode(final FriendlyByteBuf data){
      return new DeleteObjective(data.readUtf());
    }
    @Override
    protected final void handle(final ServerPlayer player){
      final MinecraftServer server = player.server;
      final Scoreboard scoreboard = server.getScoreboard();
      scoreboard.removeObjective(scoreboard.getObjective(objective_name));
      TeamData.sync(server, scoreboard);
    }
  }
  
  public static final class SetDisplaySlot extends INetworkMessage {
    private final String objective_name;
    private final int display_slot;
    public SetDisplaySlot(String objective, int display_slot){
      this.objective_name = objective;
      this.display_slot = display_slot;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(objective_name);
      data.writeInt(display_slot);
    }
    public static final SetDisplaySlot decode(final FriendlyByteBuf data){
      return new SetDisplaySlot(data.readUtf(), data.readInt());
    }
    @Override
    protected final void handle(final ServerPlayer player){
      final MinecraftServer server = player.server;
      final Scoreboard scoreboard = server.getScoreboard();
      final Objective objective = scoreboard.getObjective(objective_name);
      scoreboard.setDisplayObjective(display_slot, objective);
      TeamData.sync(server, scoreboard);
    }
  }
  
  public static final class AddPlayerToTeam extends INetworkMessage {
    private final String player;
    private final String team;
    public AddPlayerToTeam(String player, String team){
      this.player = player;
      this.team = team;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(player);
      data.writeUtf(team);
    }
    public static final AddPlayerToTeam decode(final FriendlyByteBuf data){
      return new AddPlayerToTeam(data.readUtf(), data.readUtf());
    }
    @Override
    protected final void handle(final ServerPlayer sender){
      final MinecraftServer server = sender.server;
      final Scoreboard scoreboard = server.getScoreboard();
      if(scoreboard.addPlayerToTeam(player, scoreboard.getPlayerTeam(team))){
        TeamData.sync(server, scoreboard);
      }
    }
  }
  
  public static final class RemovePlayerFromTeam extends INetworkMessage {
    private final String player;
    private final String team_name;
    public RemovePlayerFromTeam(String player, String team){
      this.player = player;
      this.team_name = team;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(player);
      data.writeUtf(team_name);
    }
    public static final RemovePlayerFromTeam decode(final FriendlyByteBuf data){
      return new RemovePlayerFromTeam(data.readUtf(), data.readUtf());
    }
    @Override
    protected final void handle(final ServerPlayer sender){
      final MinecraftServer server = sender.server;
      final Scoreboard scoreboard = server.getScoreboard();
      final PlayerTeam team = scoreboard.getPlayersTeam(player);
      if(team != null){ // player is on a team
        if(team.getName().equals(team_name)){ // players team is the one we want him out of
          scoreboard.removePlayerFromTeam(player, team);
          TeamData.sync(server, scoreboard);
        }
      }
    }
  }
  
  public static final class SetScore extends INetworkMessage {
    private final String objective;
    private final String player;
    private final int new_score_value;
    public SetScore(String objective, String player, int new_score_value){
      this.objective = objective;
      this.player = player;
      this.new_score_value = new_score_value;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(objective);
      data.writeUtf(player);
      data.writeInt(new_score_value);
    }
    public static final SetScore decode(final FriendlyByteBuf data){
      return new SetScore(data.readUtf(), data.readUtf(), data.readInt());
    }
    @Override
    protected final void handle(final ServerPlayer sender){
      ScoreUtil.getScore(sender, player, objective).setScore(new_score_value);
    }
  }
  
  public static final class AddScore extends INetworkMessage {
    private final String objective;
    private final String player;
    private final int score_to_add;
    public AddScore(String objective, String player, int score_to_add){
      this.objective = objective;
      this.player = player;
      this.score_to_add = score_to_add;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(objective);
      data.writeUtf(player);
      data.writeInt(score_to_add);
    }
    public static final AddScore decode(final FriendlyByteBuf data){
      return new AddScore(data.readUtf(), data.readUtf(), data.readInt());
    }
    @Override
    protected final void handle(final ServerPlayer sender){
      ScoreUtil.getScore(sender, player, objective).add(score_to_add);
    }
  }
  
  public static final class SubtractScore extends INetworkMessage {
    private final String objective;
    private final String player;
    private final int score_to_subtract;
    public SubtractScore(String objective, String player, int score_to_subtract){
      this.objective = objective;
      this.player = player;
      this.score_to_subtract = score_to_subtract;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(objective);
      data.writeUtf(player);
      data.writeInt(score_to_subtract);
    }
    public static final SubtractScore decode(final FriendlyByteBuf data){
      return new SubtractScore(data.readUtf(), data.readUtf(), data.readInt());
    }
    @Override
    protected final void handle(final ServerPlayer sender){
      ScoreUtil.getScore(sender, player, objective).add(-score_to_subtract);
    }
  }
  
  public static final class ResetScore extends INetworkMessage {
    private final String objective;
    private final String player;
    public ResetScore(String objective, String player){
      this.objective = objective;
      this.player = player;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(objective);
      data.writeUtf(player);
    }
    public static final ResetScore decode(final FriendlyByteBuf data){
      return new ResetScore(data.readUtf(), data.readUtf());
    }
    @Override
    protected final void handle(final ServerPlayer sender){
      ScoreUtil.getScore(sender, player, objective).reset();
    }
  }
  
  public static final class AddObjective extends INetworkMessage {
    private final String objective_id;
    private final String display_name;
    private final String criteria;
    public AddObjective(String objective_id, String display_name, String criteria){
      this.objective_id = objective_id;
      this.display_name = display_name;
      this.criteria = criteria;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(objective_id);
      data.writeUtf(display_name);
      data.writeUtf(criteria);
    }
    public static final AddObjective decode(final FriendlyByteBuf data){
      return new AddObjective(data.readUtf(), data.readUtf(), data.readUtf());
    }
    @Override
    protected final void handle(final ServerPlayer player){
      edit_objective(player, objective_id, display_name, criteria);
    }
  }
  
  public static final class EditObjective extends INetworkMessage {
    private final String objective_id;
    private final String display_name;
    private final String criteria;
    public EditObjective(String objective_id, String display_name, String criteria){
      this.objective_id = objective_id;
      this.display_name = display_name;
      this.criteria = criteria;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(objective_id);
      data.writeUtf(display_name);
      data.writeUtf(criteria);
    }
    public static final EditObjective decode(final FriendlyByteBuf data){
      return new EditObjective(data.readUtf(), data.readUtf(), data.readUtf());
    }
    @Override
    protected final void handle(final ServerPlayer player){
      edit_objective(player, objective_id, display_name, criteria);
    }
  }
  
  public static final class AddTeam extends INetworkMessage {
    private final String team_id;
    private final String display_name;
    private final boolean pvp;
    private final boolean see_invisible_allys;
    private final int team_color;
    private final int nametag_option;
    private final int death_message_option;
    private final String member_prefix;
    private final String member_suffix;
    public AddTeam(String team_id, String display_name, boolean pvp, boolean see_invisible_allys, int team_color,
                   int nametag_option, int death_message_option, String member_prefix, String member_suffix){
      this.team_id = team_id;
      this.display_name = display_name;
      this.pvp = pvp;
      this.see_invisible_allys = see_invisible_allys;
      this.team_color = team_color;
      this.nametag_option = nametag_option;
      this.death_message_option = death_message_option;
      this.member_prefix = member_prefix;
      this.member_suffix = member_suffix;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(team_id);
      data.writeUtf(display_name);
      data.writeBoolean(pvp);
      data.writeBoolean(see_invisible_allys);
      data.writeInt(team_color);
      data.writeInt(nametag_option);
      data.writeInt(death_message_option);
      data.writeUtf(member_prefix);
      data.writeUtf(member_suffix);
    }
    public static final AddTeam decode(final FriendlyByteBuf data){
      return new AddTeam(
        data.readUtf(),
        data.readUtf(),
        data.readBoolean(),
        data.readBoolean(),
        data.readInt(),
        data.readInt(),
        data.readInt(),
        data.readUtf(),
        data.readUtf()
      );
    }
    @Override
    protected final void handle(final ServerPlayer player){
      edit_team(player, team_id, display_name, pvp, see_invisible_allys, team_color, nametag_option, death_message_option, member_prefix, member_suffix);
    }
  }
  
  public static final class EditTeam extends INetworkMessage {
    private final String team_id;
    private final String display_name;
    private final boolean pvp;
    private final boolean see_invisible_allys;
    private final int team_color;
    private final int nametag_option;
    private final int death_message_option;
    private final String member_prefix;
    private final String member_suffix;
    public EditTeam(String team_id, String display_name, boolean pvp, boolean see_invisible_allys, int team_color,
                   int nametag_option, int death_message_option, String member_prefix, String member_suffix){
      this.team_id = team_id;
      this.display_name = display_name;
      this.pvp = pvp;
      this.see_invisible_allys = see_invisible_allys;
      this.team_color = team_color;
      this.nametag_option = nametag_option;
      this.death_message_option = death_message_option;
      this.member_prefix = member_prefix;
      this.member_suffix = member_suffix;
    }
    @Override
    public final void encode(final FriendlyByteBuf data){
      data.writeUtf(team_id);
      data.writeUtf(display_name);
      data.writeBoolean(pvp);
      data.writeBoolean(see_invisible_allys);
      data.writeInt(team_color);
      data.writeInt(nametag_option);
      data.writeInt(death_message_option);
      data.writeUtf(member_prefix);
      data.writeUtf(member_suffix);
    }
    public static final EditTeam decode(final FriendlyByteBuf data){
      return new EditTeam(
        data.readUtf(),
        data.readUtf(),
        data.readBoolean(),
        data.readBoolean(),
        data.readInt(),
        data.readInt(),
        data.readInt(),
        data.readUtf(),
        data.readUtf()
      );
    }
    @Override
    protected final void handle(final ServerPlayer player){
      edit_team(player, team_id, display_name, pvp, see_invisible_allys, team_color, nametag_option, death_message_option, member_prefix, member_suffix);
    }
  }
  
  private static final void edit_team(final ServerPlayer player, final String team_name, final String display_name, final boolean pvp,
                                      final boolean see_invisible_allys, final int team_color, final int nametag_option,
                                      final int death_message_option, final String member_prefix, final String member_suffix){
    final MinecraftServer server = player.server;
    final Scoreboard scoreboard = server.getScoreboard();
    if(team_name.isEmpty()){
      final Component message = Component.translatable("gui.addsynthcore.team_manager.message.create_team_failed");
      player.sendSystemMessage(message);
      return;
    }
    PlayerTeam team = scoreboard.getPlayerTeam(team_name);
    if(team == null){
      team = scoreboard.addPlayerTeam(team_name);
    }
    
    team.setDisplayName(Component.literal(display_name.isEmpty() ? team_name : display_name));
    team.setAllowFriendlyFire(pvp);
    team.setSeeFriendlyInvisibles(see_invisible_allys);
    team.setColor(ChatFormatting.getById(team_color));
    team.setNameTagVisibility(Visibility.values()[nametag_option]);
    team.setDeathMessageVisibility(Visibility.values()[death_message_option]);
    team.setPlayerPrefix(Component.literal(member_prefix));
    team.setPlayerSuffix(Component.literal(member_suffix));
    
    // MessageUtil.send_to_player(player, "gui.addsynthcore.team_manager.message.edit_team_success", team_name);
    TeamData.sync(server, scoreboard);
  }

  private static final void edit_objective(final ServerPlayer player, final String objective_name, final String display_name, final String criteria_name){
    final MinecraftServer server = player.server;
    final Scoreboard scoreboard = server.getScoreboard();
    if(objective_name.isEmpty()){
      final Component message = Component.translatable("gui.addsynthcore.team_manager.message.create_objective_failed");
      player.sendSystemMessage(message);
      return;
    }
    final Objective existing_objective = scoreboard.getObjective(objective_name);
    final ObjectiveCriteria criteria = TeamData.getCriteria(criteria_name);
    if(existing_objective == null){
      scoreboard.addObjective(objective_name, criteria, Component.literal(display_name), criteria.getDefaultRenderType());
    }
    else{
      // Objective exists
      if(criteria_name.equals(existing_objective.getCriteria().getName())){
        existing_objective.setDisplayName(Component.literal(display_name));
      }
      else{
        // Can't change criteria. Must delete existing Objective and create a new one.
        scoreboard.removeObjective(existing_objective);
        scoreboard.addObjective(objective_name, criteria, Component.literal(display_name), criteria.getDefaultRenderType());
      }
    }
    TeamData.sync(server, scoreboard);
  }

}
