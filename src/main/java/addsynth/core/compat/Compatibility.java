package addsynth.core.compat;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import addsynth.core.ADDSynthCore;
import net.minecraftforge.fml.ModList;

/**
 * <p>Developers can use this class to query whether a certain mod is loaded.
 *    All Mod IDs are listed here in case they ever change in the future.</p>
 * @author ADDSynth
 * @since October 28, 2019
 * @version 1.0 December 9, 2025
 */
public final class Compatibility {

  // JAVA #2: Java doesn't have structs either?! That's another stupid thing about Java!

  // TODO: Add authors, What Minecraft versions are available, and possibly but not likely a short description.

  public static final class CompatInfo {
    public final String name;
    public final String modid;
    public final ModType type;
    public CompatInfo(String name, String modid, ModType type){
      this.name = name;
      this.modid = modid;
      this.type = type;
    }
    /** Forge intends for mods to be loaded and unloaded dynamically at runtime,
     *  meaning this will NOT be constant throughout the lifetime of the program. */
    public boolean isLoaded(){
      return ModList.get().isLoaded(modid);
    }
    public boolean isAPI(){
      return type == ModType.API || type == ModType.Library || type == ModType.CoreMod;
    }
    public boolean isTechMod(){
      return type == ModType.Tech || type == ModType.Computer || type == ModType.Rail_Transport;
    }
  }

  public enum ModType {
    Tech, Magic, Library, Misc, Vanilla, Client, Recipe, Biomes, Food, Decoration,
    Blocks, Tools, Weapons, Computer, Rail_Transport, Info, Map, Compatibility, API,
    Dimension, CoreMod, Shader, Tweak, Diagnostics, Materials, Utility, Performance
  }

  public static final CompatInfo ACTUALLY_ADDITIONS =
    new CompatInfo("Actually Additions",                 "actuallyadditions",    ModType.Tech);

  public static final CompatInfo ADDSYNTH_ENERGY =
    new CompatInfo("ADDSynth Energy",                    "addsynth_energy",      ModType.Tech);

  public static final CompatInfo ADDSYNTH_MATERIALS = // currently bundled with ADDSynthCore, so it will always be loaded.
    new CompatInfo("ADDSynth Materials",                 "addsynth_materials",   ModType.Materials);

  public static final CompatInfo APPLESKIN =
    new CompatInfo("AppleSkin",                          "appleskin",            ModType.Info);

  public static final CompatInfo APPLIED_ENERGISTICS =
    new CompatInfo("Applied Energistics 2",              "appliedenergistics2",  ModType.Tech);

  public static final CompatInfo ARCHERS_PARADOX =
    new CompatInfo("Archer's Paradox",                   "archers_paradox",      ModType.Weapons);

  public static final CompatInfo ARCHITECTURY =
    new CompatInfo("Architectury API",                   "architectury",         ModType.API);

  public static final CompatInfo BAD_PACKETS =
    new CompatInfo("bad packets",                        "badpackets",           ModType.API);

  public static final CompatInfo BIOMES_O_PLENTY =
    new CompatInfo("Biomes O' Plenty",                   "biomesoplenty",        ModType.Biomes);
  
  public static final CompatInfo BLOCK_RENDERER =
    new CompatInfo("BlockRenderer",                      "block_renderer",       ModType.Utility);
  
  public static final CompatInfo BLOOD_MAGIC =
    new CompatInfo("Blood Magic",                        "bloodmagic",           ModType.Magic);
  
  public static final CompatInfo BOOKSHELF =
    new CompatInfo("Bookshelf",                          "bookshelf",            ModType.API);
  
  public static final CompatInfo BOTANIA =
    new CompatInfo("Botania",                            "botania",              ModType.Magic);
  
  public static final CompatInfo BUILDCRAFT =
    new CompatInfo("Buildcraft",                         "buildcraftcore",       ModType.Tech);
  
  public static final CompatInfo CHANCE_CUBES =
    new CompatInfo("Chance Cubes",                       "chancecubes",          ModType.Misc);
  
  public static final CompatInfo CHISELS_AND_BITS =
    new CompatInfo("Chisels & Bits",                     "chiselsandbits",       ModType.Decoration);
  
  public static final CompatInfo CODE_CHICKEN_LIB =
    new CompatInfo("CodeChicken Lib",                    "codechickenlib",       ModType.Library);
  
  public static final CompatInfo COFH_CORE =
    new CompatInfo("CoFH Core",                          "cofh_core",            ModType.CoreMod);
  
  public static final CompatInfo COMMON_CAPABILITIES =
    new CompatInfo("Common Capabilities",                "commoncapabilities",   ModType.Compatibility);
  
  public static final CompatInfo CONFIGURED =
    new CompatInfo("Configured",                         "configured",           ModType.Utility);
  
  public static final CompatInfo COOKING_FOR_BLOCKHEADS =
    new CompatInfo("Cooking for Blockheads",             "cookingforblockheads", ModType.Food);
  
  public static final CompatInfo CRAFTTWEAKER =
    // helps modders modify the game
    new CompatInfo("CraftTweaker",                       "crafttweaker",         ModType.API);
  
  public static final CompatInfo CREATE =
    new CompatInfo("Create",                             "create",               ModType.Tech);
  
  public static final CompatInfo CURIOS =
    new CompatInfo("Curios",                             "curios",               ModType.API);
  
  public static final CompatInfo CYCLOPS_CORE =
    new CompatInfo("Cyclops Core",                       "cyclopscore",          ModType.Library);
  
  public static final CompatInfo DRACONIC_EVOLUTION =
    new CompatInfo("Draconic Evolution",                 "draconicevolution",    ModType.Magic);
  
  public static final CompatInfo DYNAMIC_SURROUNDINGS =
    new CompatInfo("Dynamic Surroundings",               "dsurround",            ModType.Client);
  
  public static final CompatInfo ENDER_IO =
    new CompatInfo("Ender IO",                           "enderio",              ModType.Tech);
  
  public static final CompatInfo ENSORCELLATION =
    new CompatInfo("Ensorcellation",                     "ensorcellation",       ModType.Magic);
  
  // By Team Abnormals
  public static final CompatInfo ENVIRONMENTAL =
    new CompatInfo("Environmental",                      "environmental",        ModType.Biomes);
  
  public static final CompatInfo ENVIRONMENTAL_MATERIALS =
    new CompatInfo("Environmental Materials",            "enviromats",           ModType.Blocks);
  
  public static final CompatInfo EVILCRAFT =
    new CompatInfo("EvilCraft",                          "evilcraft",            ModType.Magic);
  
  // Ex Nihilo (1.7.10) -> Ex Nihilo: Adscensio (1.10) -> Ex Nihilo: Creatio (1.12) -> Ex Nihilo: Sequentia (1.15+)
  public static final CompatInfo EX_NIHILO_SEQUENTIA =
    new CompatInfo("Ex Nihilo: Sequentia",               "exnihilosequentia",    ModType.Misc);
  
  public static final CompatInfo EXTREME_REACTORS =
    new CompatInfo("Extreme Reactors",                   "bigreactors",          ModType.Tech);
  
  public static final CompatInfo FASTWORKBENCH =
    new CompatInfo("FastWorkbench",                      "fastbench",            ModType.Performance);
  
  public static final CompatInfo FOAMFIX =
    new CompatInfo("FoamFix",                            "foamfix",              ModType.Performance);
  
  public static final CompatInfo FORESTRY =
    new CompatInfo("Forestry",                           "forestry",             ModType.Misc);
  
  public static final CompatInfo FORGE_MULTIPART =
    new CompatInfo("Forge Multipart CBE",                "forgemultipartcbe",    ModType.API);
  
  public static final CompatInfo GALACTICRAFT =
    new CompatInfo("Galacticraft",                       "galacticraftcore",     ModType.Tech);
  
  public static final CompatInfo GRAVESTONE_MOD =
    new CompatInfo("GraveStone Mod",                     "gravestone",           ModType.Misc);
  
  // Probably the latest version of Greg Tech, been rewritten various times to be keep up with Minecraft
  public static final CompatInfo GREGTECH =
    new CompatInfo("GregTech CEu Modern",                "gtceu",                ModType.Tech);
  
  public static final CompatInfo GUNPOWDERLIB =
    new CompatInfo("GunpowderLib",                       "gunpowderlib",         ModType.Library);
  
  public static final CompatInfo IMMERSIVE_ENGINEERING =
    new CompatInfo("Immersive Engineering",              "immersiveengineering", ModType.Tech);

  public static final CompatInfo IMMERSIVE_RAILROADING =
    new CompatInfo("Immersive Railroading",              "immersiverailroading", ModType.Rail_Transport);
  
  public static final CompatInfo INDUSTRIAL_CRAFT =
    new CompatInfo("Industrial Craft",                   "ic2",                  ModType.Tech);
  
  public static final CompatInfo INDUSTRIAL_FOREGOING =
    new CompatInfo("Industrial Foregoing",               "industrialforegoing",  ModType.Tech);
  
  public static final CompatInfo INTEGRATED_DYNAMICS =
    new CompatInfo("Integrated Dynamics",                "integrateddynamics",   ModType.Tech);
  
  public static final CompatInfo INTEGRATED_TERMINALS =
    new CompatInfo("Integrated Terminals",               "integratedterminals",  ModType.Tech);
  
  public static final CompatInfo INTEGRATED_TUNNELS =
    new CompatInfo("Integrated Tunnels",                 "integratedtunnels",    ModType.Tech);
  
  public static final CompatInfo INVENTORY_TWEAKS_RENEWED =
    new CompatInfo("Inventory Tweaks Renewed",           "invtweaks",            ModType.Tweak);
  
  public static final CompatInfo ITEM_ZOOM =
    new CompatInfo("Item Zoom",                          "itemzoom",             ModType.Utility);
  
  public static final CompatInfo JEI =
    new CompatInfo("JEI",                                "jei",                  ModType.Recipe);
  
  public static final CompatInfo JOURNEY_MAP =
    new CompatInfo("Journey Map",                        "journeymap",           ModType.Map);
  
  public static final CompatInfo MANTLE =
    new CompatInfo("Mantle",                             "mantle",               ModType.Library);
  
  public static final CompatInfo MCJTY_LIB =
    new CompatInfo("McJtyLib",                           "mcjtylib_ng",          ModType.Library);
  
  public static final CompatInfo MEKANISM =
    new CompatInfo("Mekanism",                           "mekanism",             ModType.Tech);

  // More Blocks Mod: https://www.curseforge.com/minecraft/mc-mods/mbm-more-blocks-mod (Fabric Only)

  public static final CompatInfo MOUSE_TWEAKS =
    new CompatInfo("Mouse Tweaks",                       "mousetweaks",          ModType.Tweak);

  public static final CompatInfo MYSTICAL_AGRICULTURE =
    new CompatInfo("Mystical Agriculture",               "mysticalagriculture",  ModType.Magic);
  
  public static final CompatInfo NATURA =
    new CompatInfo("Natura",                             "natura",               ModType.Magic);
  
  public static final CompatInfo NEAT =
    new CompatInfo("Neat",                               "neat",                 ModType.Info);
  
  public static final CompatInfo NO_CUBES =
    // Renders blocks differently, has collisions, may conflict with other mods, High FPS
    new CompatInfo("NoCubes",                            "nocubes",              ModType.Shader);

  public static final CompatInfo NO_CUBES_RELOADED =
    // Uses Forge API to change the shape of blocks, no collisions, High compatibility with other mods, Low FPS
    new CompatInfo("No Cubes Reloaded",                  "nocubesreloadedbase",  ModType.Shader);
  
  public static final CompatInfo OH_MY_GOURD =
    new CompatInfo("Oh My Gourd",                        "omgourd",              ModType.Blocks);
  
  public static final CompatInfo OPENCOMPUTERS =
    new CompatInfo("OpenComputers",                      "opencomputers",        ModType.Computer);
  
  public static final CompatInfo OVERPOWERED_TECHNOLOGY =
    new CompatInfo("Overpowered Technology",             "overpowered_technology", ModType.Tech);
  
  public static final CompatInfo PAMS_HARVESTCRAFT_2_FOOD_CORE =
    new CompatInfo("Pam's Harvestcraft 2 - Food Core",   "pamhc2foodcore",       ModType.Food);

  public static final CompatInfo PAMS_HARVESTCRAFT_2_CROPS =
    new CompatInfo("Pam's Harvestcraft 2 - Crops",       "pamhc2crops",          ModType.Food);

  public static final CompatInfo PAMS_HARVESTCRAFT_2_TREES =
    new CompatInfo("Pam's Harvestcraft 2 - Trees",       "pamhc2trees",          ModType.Food);

  public static final CompatInfo PAMS_HARVESTCRAFT_2_FOOD_EXTENDED =
    new CompatInfo("Pam's Harvestcraft 2 - Food Extended", "pamhc2foodextended", ModType.Food);
  
  public static final CompatInfo PATCHOULI =
    new CompatInfo("Patchouli",                          "patchouli",            ModType.API);
  
  public static final CompatInfo PORTAL_GUN =
    new CompatInfo("Portal Gun Mod",                     "portalgun",            ModType.Misc);
    // Can't decide which portal gun mod is the OFFICIAL Portal Gun Mod, there's Immersive Portal Gun as well.
  
  public static final CompatInfo PROJECT_E =
    new CompatInfo("Project E",                          "projecte",             ModType.Magic);
  
  public static final CompatInfo PROJECT_RED =
    new CompatInfo("Project Red",                        "projectred-core",      ModType.Tech);
  
  public static final CompatInfo QUARK =
    new CompatInfo("Quark",                              "quark",                ModType.Vanilla);
  
  public static final CompatInfo RFTOOLS_UTILITY =
    new CompatInfo("RFTools Utility",                    "rttoolsutility",       ModType.Tech);

  public static final CompatInfo SHADOWFACTS_FORGELIN =
    new CompatInfo("Shadowfacts' Forgelin",              "forgelin",             ModType.Library);
  
  public static final CompatInfo SOUND_FILTERS =
    new CompatInfo("Sound Filters",                      "soundfilters",         ModType.Client);
  
  public static final CompatInfo STEVES_CARTS =
    new CompatInfo("Steve's Carts",                      "stevescarts",          ModType.Rail_Transport);
  
  public static final CompatInfo THE_BENEATH =
    new CompatInfo("The Beneath",                        "beneath",              ModType.Dimension);

  public static final CompatInfo THE_ONE_PROBE =
    new CompatInfo("The One Probe",                      "theoneprobe",          ModType.Info);

  public static final CompatInfo THERMAL_CULTIVATION =
    new CompatInfo("Thermal Cultivation",                "thermal_cultivation",  ModType.Food);

  public static final CompatInfo THERMAL_EXPANSION = // The original
    new CompatInfo("Thermal Expansion",                  "thermal_expansion",    ModType.Tech);

  public static final CompatInfo THERMAL_FOUNDATION =
    new CompatInfo("Thermal Foundation",                 "thermal_foundation",   ModType.Materials);

  public static final CompatInfo THERMAL_INNOVATION =
    new CompatInfo("Thermal Innovation",                 "thermal_innovation",   ModType.Tools);

  public static final CompatInfo THERMAL_INTEGRATION =
    // Compatibility module for the Thermal series
    new CompatInfo("Thermal Integration",                "thermal_integration",  ModType.Compatibility);

  public static final CompatInfo THERMAL_LOCOMOTION =
    new CompatInfo("Thermal Locomotion",                 "thermal_locomotion",   ModType.Rail_Transport);

  public static final CompatInfo TINKERS_CONSTRUCT =
    new CompatInfo("Tinkers' Construct",                 "tconstruct",           ModType.Tools);
  
  public static final CompatInfo TINY_PROGRESSIONS =
    new CompatInfo("Tiny Progressions",                  "tp",                   ModType.Misc);

  public static final CompatInfo TOOLS_COMPLEMENT =
    new CompatInfo("Tool's Complement",                  "tools_complement",     ModType.Tools);

  public static final CompatInfo TRACK_API =
    new CompatInfo("Track API",                          "trackapi",             ModType.API);

  public static final CompatInfo TRASH_SLOT =
    new CompatInfo("TrashSlot",                          "trashslot",            ModType.Misc);

  public static final CompatInfo WTHIT = // What The Hell Is That? replacement of Hwyla for MC 1.16+
    new CompatInfo("WTHIT",                              "wthit",                ModType.Info);
  
  public static final CompatInfo XAEROS_MINI_MAP =
    new CompatInfo("Xaero's Minimap",                    "xaerominimap",         ModType.Map);
  
  public static final CompatInfo XAEROS_WORLD_MAP =
    new CompatInfo("Xaero's World Map",                  "xaeroworldmap",        ModType.Map);
  
  public static final CompatInfo XNET =
    new CompatInfo("XNet",                               "xnet",                 ModType.Tech);




  public static final void debug(){
    ADDSynthCore.log.info("Begin printing ADDSynthCore mod detection results:");

    final TreeMap<String, Boolean> modlist = new TreeMap<>();
    CompatInfo mod_info;
    String mod_name;
    int max_string = 0;

    try{
      for(final Field field : Compatibility.class.getDeclaredFields()){
        if(field.getType().equals(CompatInfo.class)){
          mod_info = CompatInfo.class.cast(field.get(null));
          mod_name = mod_info.name;
          modlist.put(mod_name, mod_info.isLoaded());
          if(mod_name.length() > max_string){
            max_string = mod_name.length();
          }
        }
      }
    }
    catch(Exception e){
      ADDSynthCore.log.error("An error occured while printing mod detection results.", e);
    }

    for(final Map.Entry<String, Boolean> mod : modlist.entrySet()){
      mod_name = adjust_mod_name(mod.getKey(), max_string);
      ADDSynthCore.log.info(mod_name + ": "+(mod.getValue() ? "loaded" : "not detected"));
    }

    ADDSynthCore.log.info("Done checking. ADDSynthCore does not check for any mods that are not listed here.");
  }

  private static final String adjust_mod_name(final String mod_name, final int length){
    boolean first = true;
    final StringBuilder str = new StringBuilder(mod_name);
    while(str.length() < length){
      if(first){
        str.append(' ');
        first = false;
      }
      else{
        str.append('-');
      }
    }
    return str.toString();
  }

}
