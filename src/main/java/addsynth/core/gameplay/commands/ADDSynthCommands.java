package addsynth.core.gameplay.commands;

import addsynth.core.ADDSynthCore;
import addsynth.core.gameplay.config.Config;
import addsynth.core.util.debug.DebugUtil;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;

/** @see net.minecraft.server.network.ServerGamePacketListenerImpl#parseCommand
 *  @see net.minecraft.server.dedicated.DedicatedServer#handleConsoleInput
 *  @see net.minecraft.commands.Commands#performCommand
 */
public final class ADDSynthCommands {

  public static final void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext build_context){
    if(Config.item_explosion_command.get()){
      ItemExplosionCommand.register(dispatcher);
    }
    if(Config.zombie_raid_command.get()){
      ZombieRaidCommand.register(dispatcher);
    }
    if(Config.blackout_command.get()){
      BlackoutCommand.register(dispatcher);
    }
    if(Config.lightning_storm_command.get()){
      LightningStormCommand.register(dispatcher);
    }
    ShowOresCommand.register(dispatcher, build_context);
    MapColorsCommand.register(dispatcher);
  }

  /** This runs every server tick (20 times a second). Assigned to the Forge Event bus by {@link ADDSynthCore}. */
  public static final void tick(final ServerTickEvent tick_event){
    if(tick_event.phase == Phase.START){
      DebugUtil.beginSection(ADDSynthCore.NAME+" Commands");
      ZombieRaidCommand.tick();
      LightningStormCommand.tick();
      DebugUtil.endSection();
    }
  }

}
