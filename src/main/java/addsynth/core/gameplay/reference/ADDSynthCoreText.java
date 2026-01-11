package addsynth.core.gameplay.reference;

import addsynth.core.util.color.ColorCode;
import net.minecraft.network.chat.Component;

/** The TextReference class should only be used for common words
 *  or commonly used text that can be used in multiple projects.
 *  Text that is only used in one place, should be defined there.
 *  
 *  Also, keep in mind that just because a word has multiple
 *  meanings in my language, does not mean it has the same meanings
 *  in other languages. */
public final class ADDSynthCoreText {

  public static final Component null_error = Component.translatable("gui.addsynthcore.null_error").withStyle(ColorCode.ERROR);

  public static final Component down  = Component.translatable("gui.addsynthcore.direction.down");
  public static final Component up    = Component.translatable("gui.addsynthcore.direction.up");
  public static final Component north = Component.translatable("gui.addsynthcore.direction.north");
  public static final Component south = Component.translatable("gui.addsynthcore.direction.south");
  public static final Component west  = Component.translatable("gui.addsynthcore.direction.west");
  public static final Component east  = Component.translatable("gui.addsynthcore.direction.east");
  public static final Component getDirection(int direction){
    return switch(direction % 6){
    case 0 -> down;
    case 1 -> up;
    case 2 -> north;
    case 3 -> south;
    case 4 -> west;
    case 5 -> east;
    default -> north;
    };
  }

  // Blocks
  public static final Component team_manager = Core.team_manager.get().getName();

  // Descriptions:
  public static final Component       music_box_description = Component.translatable("gui.addsynthcore.jei_description.music_box");
  public static final Component     music_sheet_description = Component.translatable("gui.addsynthcore.jei_description.music_sheet");
  public static final Component    team_manager_description = Component.translatable("gui.addsynthcore.jei_description.team_manager");
  public static final Component    watering_can_description = Component.translatable("gui.addsynthcore.jei_description.watering_can");
  public static final Component  hedge_trimmers_description = Component.translatable("gui.addsynthcore.jei_description.hedge_trimmers");
  public static final Component personal_beacon_description = Component.translatable("gui.addsynthcore.jei_description.personal_beacon");
  public static final Component    auto_jukebox_description = Component.translatable("gui.addsynthcore.jei_description.auto_jukebox");

}
