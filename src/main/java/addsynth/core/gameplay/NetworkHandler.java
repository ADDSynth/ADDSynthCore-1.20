package addsynth.core.gameplay;

import addsynth.core.ADDSynthCore;
import addsynth.core.gameplay.blocks.jukebox.JukeboxMessage;
import addsynth.core.gameplay.blocks.music_box.network_messages.ChangeInstrumentMessage;
import addsynth.core.gameplay.blocks.music_box.network_messages.MusicBoxMessage;
import addsynth.core.gameplay.blocks.music_box.network_messages.NoteMessage;
import addsynth.core.gameplay.blocks.team_manager.network_messages.PlayerScoreMessage;
import addsynth.core.gameplay.blocks.team_manager.network_messages.RequestPlayerScoreMessage;
import addsynth.core.gameplay.blocks.team_manager.network_messages.TeamManagerCommand;
import addsynth.core.gameplay.blocks.team_manager.network_messages.TeamManagerSyncMessage;
import addsynth.core.util.network.ADDSynthNetworkHandler;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkHandler extends ADDSynthNetworkHandler {

  public static final SimpleChannel INSTANCE = createChannel(ADDSynthCore.getLocation("network_channel"));

  public static final void registerMessages(){
    registerServerMessage( 0, INSTANCE, MusicBoxMessage.class,                     MusicBoxMessage::decode);
    registerServerMessage( 1, INSTANCE, NoteMessage.class,                         NoteMessage::decode);
    registerServerMessage( 2, INSTANCE, ChangeInstrumentMessage.class,             ChangeInstrumentMessage::decode);
    registerClientMessage( 3, INSTANCE, TeamManagerSyncMessage.class,              TeamManagerSyncMessage::decode);
    registerServerMessage( 5, INSTANCE, RequestPlayerScoreMessage.class,           RequestPlayerScoreMessage::decode);
    registerClientMessage( 6, INSTANCE, PlayerScoreMessage.class,                  PlayerScoreMessage::decode);
    registerServerMessage( 7, INSTANCE, TeamManagerCommand.AddTeam.class,          TeamManagerCommand.AddTeam::decode);
    registerServerMessage( 8, INSTANCE, TeamManagerCommand.EditTeam.class,         TeamManagerCommand.EditTeam::decode);
    registerServerMessage( 9, INSTANCE, TeamManagerCommand.DeleteTeam.class,       TeamManagerCommand.DeleteTeam::decode);
    registerServerMessage(10, INSTANCE, TeamManagerCommand.AddPlayerToTeam.class,  TeamManagerCommand.AddPlayerToTeam::decode);
    registerServerMessage(11, INSTANCE, TeamManagerCommand.RemovePlayerFromTeam.class, TeamManagerCommand.RemovePlayerFromTeam::decode);
    registerServerMessage(12, INSTANCE, TeamManagerCommand.AddObjective.class,     TeamManagerCommand.AddObjective::decode);
    registerServerMessage(13, INSTANCE, TeamManagerCommand.EditObjective.class,    TeamManagerCommand.EditObjective::decode);
    registerServerMessage(14, INSTANCE, TeamManagerCommand.DeleteObjective.class,  TeamManagerCommand.DeleteObjective::decode);
    registerServerMessage(15, INSTANCE, TeamManagerCommand.SetScore.class,         TeamManagerCommand.SetScore::decode);
    registerServerMessage(16, INSTANCE, TeamManagerCommand.AddScore.class,         TeamManagerCommand.AddScore::decode);
    registerServerMessage(17, INSTANCE, TeamManagerCommand.SubtractScore.class,    TeamManagerCommand.SubtractScore::decode);
    registerServerMessage(18, INSTANCE, TeamManagerCommand.ResetScore.class,       TeamManagerCommand.ResetScore::decode);
    registerServerMessage(19, INSTANCE, TeamManagerCommand.SetDisplaySlot.class,   TeamManagerCommand.SetDisplaySlot::decode);
    registerServerMessage(20, INSTANCE, TeamManagerCommand.ClearDisplaySlot.class, TeamManagerCommand.ClearDisplaySlot::decode);
    registerServerMessage(21, INSTANCE, JukeboxMessage.class,                      JukeboxMessage::decode);
  }
}
