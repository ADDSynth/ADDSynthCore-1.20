package addsynth.core.util.game.chat;

import addsynth.core.util.player.PlayerUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;

public final class MessageUtil {

  public static final void send_to_all_players(final Level world, final Component text_component){
    @SuppressWarnings("resource")
    final MinecraftServer server = world.getServer();
    if(server != null){
      send_to_all_players(server, text_component);
    }
  }
  
  private static final void send_to_all_players(final MinecraftServer server, final Component text_component){
    final PlayerList player_list = server.getPlayerList();
    if(player_list != null){
      player_list.broadcastSystemMessage(text_component, false);
       // MAYBE: There's another Task Tag somewhere, but maybe the way I'm sending messages
       // to players should actually be game messages / overlay true? Like the "You may not
       // rest now there are monsters nearby" message.
    }
  }

  public static final void send_to_all_players_in_world(final Level world, final Component text_component){
    @SuppressWarnings("resource")
    final MinecraftServer server = world.getServer();
    if(server != null){
      PlayerUtil.allPlayersInWorld(server, world, (ServerPlayer player) -> {
        player.sendSystemMessage(text_component);
      });
    }
  }

}
