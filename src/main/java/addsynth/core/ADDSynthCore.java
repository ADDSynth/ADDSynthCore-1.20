package addsynth.core;

import addsynth.core.compat.Compatibility;
import addsynth.core.compat.EMCValue;
import addsynth.core.gameplay.NetworkHandler;
import addsynth.core.gameplay.blocks.jukebox.JukeboxPlayerGui;
import addsynth.core.gameplay.blocks.team_manager.data.CriteriaData;
import addsynth.core.gameplay.blocks.team_manager.data.TeamData;
import addsynth.core.gameplay.commands.ADDSynthCommands;
import addsynth.core.gameplay.config.*;
import addsynth.core.gameplay.items.backpack.BackpackGui;
import addsynth.core.gameplay.registers.Containers;
import addsynth.core.recipe.FurnaceRecipes;
import addsynth.core.util.CommonUtil;
import addsynth.core.util.constants.DevStage;
import addsynth.core.util.game.Game;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(value = ADDSynthCore.MOD_ID)
public final class ADDSynthCore {

  public static final String MOD_ID = "addsynthcore";
  public static final String NAME = "ADDSynthCore";
  public static final String VERSION = "1.0";
  public static final String VERSION_DATE = "April 5, 2025";
  public static final DevStage DEV_STAGE = DevStage.STABLE;

  public static final Logger log = LogManager.getLogger(NAME);

  public static final ResourceLocation getLocation(final String path){
    return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
  }

  public ADDSynthCore(final FMLJavaModLoadingContext context){
    ADDSynthCore.log.info("Begin constructing ADDSynthCore class object...");

    final IEventBus bus = context.getModEventBus();
    bus.addListener(ADDSynthCore::main_setup);
    bus.addListener(ADDSynthCore::client_setup);
    MinecraftForge.EVENT_BUS.addListener(ADDSynthCore::onServerStarting);
    MinecraftForge.EVENT_BUS.addListener(ADDSynthCore::onServerStarted);
    MinecraftForge.EVENT_BUS.addListener(ADDSynthCore::registerCommands);

    init_config(context);

    MinecraftForge.EVENT_BUS.addListener(TeamData::serverTick);
    MinecraftForge.EVENT_BUS.addListener(ADDSynthCommands::tick);
    
    ADDSynthCore.log.info("Done constructing ADDSynthCore class object.");
  }

  private static final void init_config(final ModLoadingContext context){
    Game.registerConfig(context,        Config::new, NAME, "main.toml");
    Game.registerConfig(context, DebugSettings::new, NAME, "Debug.toml");
  }

  private static final void main_setup(final FMLCommonSetupEvent event){
    log.info("Begin ADDSynthCore main setup...");
    CommonUtil.displayModInfo(log, NAME, "ADDSynth", VERSION, DEV_STAGE, VERSION_DATE);
  
    Debug.debug();
    if(DebugSettings.debug_mod_detection.get()){
      event.enqueueWork(Compatibility::debug);
    }
    NetworkHandler.registerMessages();
    MinecraftForge.EVENT_BUS.addListener((TagsUpdatedEvent tag_event) -> Debug.dump_tags(tag_event.getRegistryAccess()));
    
    FurnaceRecipes.INSTANCE.register();

    log.info("Finished ADDSynthCore main setup.");
  }

  private static final void client_setup(final FMLClientSetupEvent event){
    MenuScreens.register(Containers.AUTO_JUKEBOX.get(), JukeboxPlayerGui::new);
    MenuScreens.register(Containers.BACKPACK.get(), BackpackGui::new);
    CriteriaData.calculate();
  }

  public static void onServerStarting(final ServerStartingEvent event){
  }

  public static void onServerStarted(final ServerStartedEvent event){
    if(Compatibility.PROJECT_E.isLoaded()){
      EMCValue.check_internal_emc_values();
      if(DEV_STAGE.isDevelopment){
        EMCValue.check_items(MOD_ID);
      }
    }
  }

  public static final void registerCommands(final RegisterCommandsEvent event){
    ADDSynthCommands.register(event.getDispatcher(), event.getBuildContext());
  }

}
