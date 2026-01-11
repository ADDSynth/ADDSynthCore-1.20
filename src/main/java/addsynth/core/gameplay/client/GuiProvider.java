package addsynth.core.gameplay.client;

import addsynth.core.gameplay.blocks.music_box.TileMusicBox;
import addsynth.core.gameplay.blocks.music_box.gui.GuiMusicBox;
import addsynth.core.gameplay.blocks.team_manager.gui.TeamManagerGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class GuiProvider {

  @SuppressWarnings("resource")
  public static final void openMusicBoxGui(final TileMusicBox tile, final Component title){
    Minecraft.getInstance().setScreen(new GuiMusicBox(tile, title));
  }

  @SuppressWarnings("resource")
  public static final void openTeamManagerGui(){
    Minecraft.getInstance().setScreen(new TeamManagerGui());
  }

}
