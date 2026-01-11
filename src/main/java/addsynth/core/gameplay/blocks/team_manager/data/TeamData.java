package addsynth.core.gameplay.blocks.team_manager.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import addsynth.core.ADDSynthCore;
import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gameplay.blocks.team_manager.gui.TeamManagerGui;
import addsynth.core.gameplay.blocks.team_manager.gui.TeamManagerObjectiveGui;
import addsynth.core.gameplay.blocks.team_manager.network_messages.TeamManagerSyncMessage;
import addsynth.core.util.debug.DebugUtil;
import addsynth.core.util.game.data.CombinedNameComponent;
import addsynth.core.util.java.StringUtil;
import addsynth.core.util.time.TickHandler;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.network.PacketDistributor;

public final class TeamData {

  private static Collection<PlayerTeam> team_list;
  private static int number_of_teams;
  private static PlayerTeam[] team_array;

  private static List<? extends Player> all_players;
  private static Team team;

  private static Collection<Objective> objectives_list;
  private static int number_of_objectives;
  private static Objective[] objective_array;

  private static final TickHandler tick_handler = new TickHandler();

  private static ArrayList<CombinedNameComponent> non_team_players = new ArrayList<CombinedNameComponent>();
  private static TeamDataUnit[] teams;
  private static ObjectiveDataUnit[] objectives;
  /** This is set to true whenever the client is synced with the server data. Used to update guis that are open. */
  public static boolean changed;

  private static String[] display_slot_objective = {"", "", ""};

  /** This runs every server tick (20 times a second). Assigned to the Forge Event bus by {@link ADDSynthCore}. */
  public static final void serverTick(final ServerTickEvent event){
    if(event.phase == Phase.END){
      DebugUtil.beginSection("Team Manager Data Update");
      if(tick_handler.tick()){
        @SuppressWarnings("resource")
        final MinecraftServer server = event.getServer();
        sync(server, server.getScoreboard());
      }
      DebugUtil.endSection();
    }
  }

  /** Gets data from the server and sends to the client. */
  public static final void sync(final MinecraftServer server, final Scoreboard scoreboard){
    
    // Teams
    team_list = scoreboard.getPlayerTeams();
    number_of_teams = team_list.size();
    team_array = team_list.toArray(new PlayerTeam[number_of_teams]);
    teams = new TeamDataUnit[number_of_teams];
    int i;
    for(i = 0; i < number_of_teams; i++){
      teams[i] = new TeamDataUnit();
      teams[i].name = team_array[i].getName();
      teams[i].display_name = team_array[i].getDisplayName();
      teams[i].color = team_array[i].getColor().getId();
      teams[i].prefix = team_array[i].getPlayerPrefix();
      teams[i].suffix = team_array[i].getPlayerSuffix();
      teams[i].pvp = team_array[i].isAllowFriendlyFire();
      teams[i].see_invisible_allys = team_array[i].canSeeFriendlyInvisibles();
      teams[i].nametag_option = team_array[i].getNameTagVisibility().id;
      teams[i].death_message_option = team_array[i].getDeathMessageVisibility().id;
    }
    
    // Players
    all_players = server.getPlayerList().getPlayers();
    non_team_players.clear();
    for(final Player player : all_players){
      team = player.getTeam();
      if(team == null){
        non_team_players.add(new CombinedNameComponent(player));
      }
      else{
        for(final TeamDataUnit team_data : teams){
          if(team_data.matches(team)){
            team_data.players.add(new CombinedNameComponent(player));
          }
        }
      }
    }
    
    // Objectives
    objectives_list = scoreboard.getObjectives();
    number_of_objectives = objectives_list.size();
    objective_array = objectives_list.toArray(new Objective[number_of_objectives]);
    objectives = new ObjectiveDataUnit[number_of_objectives];
    for(i = 0; i < number_of_objectives; i++){
      objectives[i] = new ObjectiveDataUnit();
      objectives[i].name = objective_array[i].getName();
      objectives[i].display_name = objective_array[i].getDisplayName();
      objectives[i].criteria = objective_array[i].getCriteria();
      objectives[i].criteria_name = objectives[i].criteria.getName();
      objectives[i].modify = !objectives[i].criteria.isReadOnly();
    }
    
    // DisplaySlots
    Objective o = scoreboard.getDisplayObjective(0);
    display_slot_objective[0] = o != null ? o.getName() : "";
    o = scoreboard.getDisplayObjective(1);
    display_slot_objective[1] = o != null ? o.getName() : "";
    o = scoreboard.getDisplayObjective(2);
    display_slot_objective[2] = o != null ? o.getName() : "";

    TeamManagerSyncMessage message = new TeamManagerSyncMessage(non_team_players, teams, objectives, display_slot_objective);
    NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), message);
  }

  public static final void syncClientData(ArrayList<CombinedNameComponent> non_team_players, TeamDataUnit[] teams, ObjectiveDataUnit[] objectives, String[] display_slot_objectives){
    TeamData.non_team_players = non_team_players;
    TeamData.teams = teams;
    TeamData.objectives = objectives;
    TeamData.display_slot_objective = display_slot_objectives;
    changed = true;
  }

  public static final int getStandardCriteriaIndex(final String criteria){
    if(criteria.equals(ObjectiveCriteria.DUMMY.getName())){              return  0; }
    if(criteria.equals(ObjectiveCriteria.TRIGGER.getName())){            return  1; }
    if(criteria.equals(ObjectiveCriteria.DEATH_COUNT.getName())){        return  2; }
    if(criteria.equals(ObjectiveCriteria.KILL_COUNT_PLAYERS.getName())){ return  3; }
    if(criteria.equals(ObjectiveCriteria.KILL_COUNT_ALL.getName())){     return  4; }
    if(criteria.equals(ObjectiveCriteria.HEALTH.getName())){             return  5; }
    if(criteria.equals(ObjectiveCriteria.EXPERIENCE.getName())){         return  6; }
    if(criteria.equals(ObjectiveCriteria.LEVEL.getName())){              return  7; }
    if(criteria.equals(ObjectiveCriteria.FOOD.getName())){               return  8; }
    if(criteria.equals(ObjectiveCriteria.AIR.getName())){                return  9; }
    if(criteria.equals(ObjectiveCriteria.ARMOR.getName())){              return 10; }
    return -1;
  }

  public static final int getStandardCriteriaIndex(final ObjectiveCriteria input_criteria){
    if(input_criteria == ObjectiveCriteria.DUMMY){              return  0; }
    if(input_criteria == ObjectiveCriteria.TRIGGER){            return  1; }
    if(input_criteria == ObjectiveCriteria.DEATH_COUNT){        return  2; }
    if(input_criteria == ObjectiveCriteria.KILL_COUNT_PLAYERS){ return  3; }
    if(input_criteria == ObjectiveCriteria.KILL_COUNT_ALL){     return  4; }
    if(input_criteria == ObjectiveCriteria.HEALTH){             return  5; }
    if(input_criteria == ObjectiveCriteria.EXPERIENCE){         return  6; }
    if(input_criteria == ObjectiveCriteria.LEVEL){              return  7; }
    if(input_criteria == ObjectiveCriteria.FOOD){               return  8; }
    if(input_criteria == ObjectiveCriteria.AIR){                return  9; }
    if(input_criteria == ObjectiveCriteria.ARMOR){              return 10; }
    ADDSynthCore.log.error("Invalid ScoreCriteria.", new IllegalArgumentException());
    return -1;
  }

  /** Currently only used in the Objective Gui to get the Standard Criteria name based on the selected List Entry. */
  public static final String getStandardCriteria(final int index){
    return switch(index){
      case  0 -> ObjectiveCriteria.DUMMY.getName();
      case  1 -> ObjectiveCriteria.TRIGGER.getName();
      case  2 -> ObjectiveCriteria.DEATH_COUNT.getName();
      case  3 -> ObjectiveCriteria.KILL_COUNT_PLAYERS.getName();
      case  4 -> ObjectiveCriteria.KILL_COUNT_ALL.getName();
      case  5 -> ObjectiveCriteria.HEALTH.getName();
      case  6 -> ObjectiveCriteria.EXPERIENCE.getName();
      case  7 -> ObjectiveCriteria.LEVEL.getName();
      case  8 -> ObjectiveCriteria.FOOD.getName();
      case  9 -> ObjectiveCriteria.AIR.getName();
      case 10 -> ObjectiveCriteria.ARMOR.getName();
      default -> "invalid";
    };
  }

  /** Returns the {@link ObjectiveCriteria} given the supplied ID. */
  public static final ObjectiveCriteria getCriteria(final String id){
    final Optional<ObjectiveCriteria> criteria = ObjectiveCriteria.byName(id);
    if(criteria.isPresent()){
      return criteria.get();
    }
    ADDSynthCore.log.error("Unable to determine Criteria. Invalid criteria ID '"+id+"'.", new NoSuchElementException());
    return null;
  }

  /** Returns the criteria type given the name. Used for the Radial Button Group
   *  in the Objective Edit Screen.
   * @param criteria
   */
  public static final int getCriteriaType(final String criteria){
    if(criteria.startsWith("teamkill.")){            return CriteriaType.TEAM_KILL; }
    if(criteria.startsWith("killedByTeam.")){        return CriteriaType.KILLED_BY_TEAM; }
    if(criteria.startsWith("minecraft.broken:")){    return CriteriaType.ITEM_BROKEN; }
    if(criteria.startsWith("minecraft.crafted:")){   return CriteriaType.ITEM_CRAFTED; }
    if(criteria.startsWith("minecraft.custom:")){    return CriteriaType.STATISTICS; }
    if(criteria.startsWith("minecraft.dropped:")){   return CriteriaType.ITEM_DROPPED; }
    if(criteria.startsWith("minecraft.killed:")){    return CriteriaType.KILLED; }
    if(criteria.startsWith("minecraft.killed_by:")){ return CriteriaType.KILLED_BY; }
    if(criteria.startsWith("minecraft.mined:")){     return CriteriaType.BLOCK_MINED; }
    if(criteria.startsWith("minecraft.picked_up:")){ return CriteriaType.ITEM_PICKED_UP; }
    if(criteria.startsWith("minecraft.used:")){      return CriteriaType.ITEM_USED; }
    return CriteriaType.STANDARD;
  }

  public static final CombinedNameComponent[] getTeams(){
    if(teams != null){
      int i;
      final int length = teams.length;
      final CombinedNameComponent[] t = new CombinedNameComponent[length];
      for(i = 0; i < length; i++){
        t[i] = new CombinedNameComponent(teams[i]);
      }
      return t;
    }
    return new CombinedNameComponent[0];
  }

  public static final CombinedNameComponent[] getPlayers(){
    return non_team_players != null ? non_team_players.toArray(new CombinedNameComponent[non_team_players.size()]) : new CombinedNameComponent[0];
  }

  /** Used to build the Objectives List on the Main Screen. */
  public static final CombinedNameComponent[] getObjectives(){
    if(objectives != null){
      int i;
      final int length = objectives.length;
      final CombinedNameComponent[] o = new CombinedNameComponent[length];
      for(i = 0; i < objectives.length; i++){
        o[i] = new CombinedNameComponent(objectives[i]);
      }
      return o;
    }
    return new CombinedNameComponent[0];
  }

  public static final CombinedNameComponent[] getTeamPlayers(final String team_selected){
    if(StringUtil.StringExists(team_selected)){
      for(final TeamDataUnit t : teams){
        if(t.name.equals(team_selected)){
          return t.players.toArray(new CombinedNameComponent[t.players.size()]);
        }
      }
      return new CombinedNameComponent[0];
    }
    return new CombinedNameComponent[0];
  }

  public static final TeamDataUnit getTeamData(final String team_name){
    TeamDataUnit team = null;
    for(final TeamDataUnit t : teams){
      if(t.name.equals(team_name)){
        team = t;
        break;
      }
    }
    return team;
  }

  /** Used by {@link TeamManagerObjectiveGui} to get existing objective data
   *  if the player clicked the Edit Objective button. */
  public static final ObjectiveDataUnit getObjectiveData(final String objective_name){
    ObjectiveDataUnit objective = null;
    for(final ObjectiveDataUnit o : objectives){
      if(o.name.equals(objective_name)){
        objective = o;
        break;
      }
    }
    return objective;
  }

  // This is only used in the TeamManagerGui and nowhere else.
  public static final Component getDisplaySlotObjective(final int display_slot){
    if(display_slot == 0 || display_slot == 1 || display_slot == 2){
      if(display_slot_objective[display_slot] != null){
        return Component.literal(display_slot_objective[display_slot]);
      }
    }
    return Component.empty();
  }

  /** Used by {@link TeamManagerGui#tick} to determine whether the selected objective can be modified. */
  public static final boolean canObjectiveBeModified(final String objective){
    boolean can_modify = false;
    for(final ObjectiveDataUnit o : objectives){
      if(o.name.equals(objective)){
        can_modify = o.modify;
        break;
      }
    }
    return can_modify;
  }

}
