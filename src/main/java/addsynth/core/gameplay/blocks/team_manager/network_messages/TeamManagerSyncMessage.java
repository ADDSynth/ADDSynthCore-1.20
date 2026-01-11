package addsynth.core.gameplay.blocks.team_manager.network_messages;

import java.util.ArrayList;
import addsynth.core.gameplay.blocks.team_manager.data.ObjectiveDataUnit;
import addsynth.core.gameplay.blocks.team_manager.data.TeamData;
import addsynth.core.gameplay.blocks.team_manager.data.TeamDataUnit;
import addsynth.core.util.game.data.CombinedNameComponent;
import addsynth.core.util.network.IClientMessage;
import net.minecraft.network.FriendlyByteBuf;

/** Sent from the server to the client, to sync the {@link TeamData} */
public final class TeamManagerSyncMessage extends IClientMessage {

  private final ArrayList<CombinedNameComponent> non_team_players;
  private final TeamDataUnit[] teams;
  private final ObjectiveDataUnit[] objectives;
  private final String[] display_slot_objectives;

  public TeamManagerSyncMessage(ArrayList<CombinedNameComponent> non_team_players, TeamDataUnit[] teams, ObjectiveDataUnit[] objectives, String[] display_slot_objectives){
    this.non_team_players = non_team_players;
    this.teams = teams;
    this.objectives = objectives;
    this.display_slot_objectives = display_slot_objectives;
  }

  /** Send data to Clients. */
  @Override
  public final void encode(final FriendlyByteBuf data){
    CombinedNameComponent.encodeArray(data, non_team_players);
    data.writeInt(teams.length);
    for(final TeamDataUnit t : teams){
      t.encode(data);
    }
    data.writeInt(objectives.length);
    for(final ObjectiveDataUnit o : objectives){
      o.encode(data);
    }
    data.writeUtf(display_slot_objectives[0]);
    data.writeUtf(display_slot_objectives[1]);
    data.writeUtf(display_slot_objectives[2]);
  }

  /** Receiving data on client side. */
  public static final TeamManagerSyncMessage decode(final FriendlyByteBuf data){
    final ArrayList<CombinedNameComponent> non_team_players = new ArrayList<CombinedNameComponent>();
    for(final CombinedNameComponent t : CombinedNameComponent.decodeArray(data)){
      non_team_players.add(t);
    }
    int length = data.readInt();
    final TeamDataUnit[] teams = new TeamDataUnit[length];
    int i;
    for(i = 0; i < length; i++){
      teams[i] = TeamDataUnit.decode(data);
    }
    length = data.readInt();
    final ObjectiveDataUnit[] objectives = new ObjectiveDataUnit[length];
    for(i = 0; i < length; i++){
      objectives[i] = ObjectiveDataUnit.decode(data);
    }
    final String[] display_slot_objectives = {data.readUtf(), data.readUtf(), data.readUtf()};
    return new TeamManagerSyncMessage(non_team_players, teams, objectives, display_slot_objectives);
  }

  @Override
  protected final void handle(){
    TeamData.syncClientData(non_team_players, teams, objectives, display_slot_objectives);
  }

}
