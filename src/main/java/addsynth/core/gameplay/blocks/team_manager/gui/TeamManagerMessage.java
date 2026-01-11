package addsynth.core.gameplay.blocks.team_manager.gui;

import addsynth.core.util.color.ColorCode;
import net.minecraft.network.chat.Component;

public final class TeamManagerMessage {

  public static final Component must_specify_name     = Component.translatable("gui.addsynthcore.team_manager.message.id_required").withStyle(ColorCode.ERROR);
  public static final Component cannot_contain_spaces = Component.translatable("gui.addsynthcore.team_manager.message.no_spaces").withStyle(ColorCode.ERROR);
  public static final Component must_be_shorter       = Component.translatable("gui.addsynthcore.team_manager.message.must_be_shorter_than_16_characters").withStyle(ColorCode.ERROR);
  public static final Component name_already_exists   = Component.translatable("gui.addsynthcore.team_manager.message.name_already_exists").withStyle(ColorCode.WARNING);
  public static final Component must_specify_criteria = Component.translatable("gui.addsynthcore.team_manager.message.criteria_required").withStyle(ColorCode.ERROR);
  public static final Component score_is_readonly     = Component.translatable("gui.addsynthcore.team_manager.message.readonly_score");

}
