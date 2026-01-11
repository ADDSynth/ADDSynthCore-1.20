package addsynth.core.gameplay.blocks.team_manager.network_messages;

import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.util.game.data.ScoreUtil;
import addsynth.core.util.network.INetworkMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Score;
import net.minecraftforge.network.PacketDistributor;

/** Created on the client side whenever the player selects a Player or Objective,
 *  and gets sent to the Server. Determines player's score given the objective and
 *  returns the score back to the client that requested the score. */
public final class RequestPlayerScoreMessage extends INetworkMessage {

  private final String player;
  private final String objective;

  public RequestPlayerScoreMessage(final String player, String objective){
    this.player = player;
    this.objective = objective;
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeUtf(player);
    buf.writeUtf(objective);
  }

  public static final RequestPlayerScoreMessage decode(final FriendlyByteBuf buf){
    return new RequestPlayerScoreMessage(buf.readUtf(), buf.readUtf());
  }

  @Override
  protected final void handle(final ServerPlayer source){
    final Score score = ScoreUtil.getScore(source, player, objective);
    final int player_score = score.getScore();
    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> source), new PlayerScoreMessage(player_score));
  }

}
