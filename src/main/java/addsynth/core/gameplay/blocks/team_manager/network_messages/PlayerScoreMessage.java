package addsynth.core.gameplay.blocks.team_manager.network_messages;

import addsynth.core.gameplay.blocks.team_manager.gui.TeamManagerGui;
import addsynth.core.util.network.IClientMessage;
import net.minecraft.network.FriendlyByteBuf;

public final class PlayerScoreMessage extends IClientMessage {

  private final int score;

  public PlayerScoreMessage(int score){
    this.score = score;
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeInt(score);
  }

  public static final PlayerScoreMessage decode(final FriendlyByteBuf buf){
    return new PlayerScoreMessage(buf.readInt());
  }

  @Override
  protected final void handle(){
    TeamManagerGui.player_score = score;
  }

}
