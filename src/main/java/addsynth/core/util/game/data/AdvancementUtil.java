package addsynth.core.util.game.data;

import java.util.Set;
import addsynth.core.ADDSynthCore;
import addsynth.core.util.player.PlayerUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public final class AdvancementUtil {

  /** This grants the player the criteria you specify.
   * @param player
   * @param advancement_id the ResourceLocation of the advancement (location of the file)
   * @param criteria_id Is the criteria ID label in the advancement.json file
   */
  public static final void grantAdvancement(ServerPlayer player, ResourceLocation advancement_id, String criteria_id){
    @SuppressWarnings("resource")
    final MinecraftServer server = player.getServer();
    if(server != null){
      final ServerAdvancementManager manager = server.getAdvancements();
      final Advancement advancement = manager.getAdvancement(advancement_id);
      if(advancement != null){
        player.getAdvancements().award(advancement, criteria_id);
      }
      else{
        ADDSynthCore.log.error("Advancement "+advancement_id.toString()+" doesn't exist.", new NullPointerException());
      }
    }
  }


  /** This grants the advancement using all of the criteria in the advancement.
   * @param player_name
   * @param world
   * @param advancement
   */
  public static final void grantAdvancement(String player_name, Level world, ResourceLocation advancement){
    final ServerPlayer player = PlayerUtil.getPlayer(world, player_name);
    if(player != null){
      grantAdvancement(player, advancement);
    }
  }

  /** This grants the advancement using all of the criteria in the advancement.
   * @param player
   * @param advancement_id the ResourceLocation of the advancement (location of the file)
   */
  public static final void grantAdvancement(ServerPlayer player, ResourceLocation advancement_id){
    @SuppressWarnings("resource")
    final MinecraftServer server = player.getServer();
    if(server != null){
      final ServerAdvancementManager manager = server.getAdvancements();
      final Advancement advancement = manager.getAdvancement(advancement_id);
      if(advancement != null){
        final Set<String> criteria_set = advancement.getCriteria().keySet();
        for(String criteria : criteria_set){
          player.getAdvancements().award(advancement, criteria);
        }
      }
      else{
        ADDSynthCore.log.error("Advancement "+advancement_id.toString()+" doesn't exist.", new NullPointerException());
      }
    }
  }

}
