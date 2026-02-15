package addsynth.core.gameplay.config;

import addsynth.core.block_network.DebugBlockNetwork;
import net.minecraftforge.common.ForgeConfigSpec;

/** This is a special configuration file that holds all the debug configuration
 *  settings and keeps them separate from the main configuration file. */
public final class DebugSettings {

  public static ForgeConfigSpec.BooleanValue debug_mod_detection;
  public static ForgeConfigSpec.BooleanValue dump_map_colors;

  public static ForgeConfigSpec.BooleanValue dump_block_tags;
  public static ForgeConfigSpec.BooleanValue dump_item_tags;
  public static ForgeConfigSpec.BooleanValue dump_entity_tags;
  public static ForgeConfigSpec.BooleanValue dump_biome_tags;
  public static ForgeConfigSpec.BooleanValue dump_enchantment_tags;
  public static ForgeConfigSpec.BooleanValue dump_damage_type_tags;
  public static ForgeConfigSpec.BooleanValue dump_game_event_tags;
  public static ForgeConfigSpec.BooleanValue dump_fluid_tags;
  public static ForgeConfigSpec.BooleanValue dump_poi_tags;
  public static ForgeConfigSpec.BooleanValue dump_structure_tags;
  public static ForgeConfigSpec.BooleanValue dump_banner_pattern_tags;
  public static ForgeConfigSpec.BooleanValue dump_painting_variant_tags;

  public static ForgeConfigSpec.BooleanValue debug_block_networks;
  public static ForgeConfigSpec.EnumValue<DebugBlockNetwork.DEBUG_LEVEL> block_network_debug_level;

  public DebugSettings(final ForgeConfigSpec.Builder builder){

    debug_mod_detection = builder.define("Print Mod Detection Results", false);
    dump_map_colors     = builder.define("Dump Map Colors", false);
    builder.push("Block Networks");
      debug_block_networks = builder.define("Print Debug Messages to Log", false);
      block_network_debug_level = builder.defineEnum("Debug Level", DebugBlockNetwork.DEBUG_LEVEL.Debug);
    builder.pop();
    builder.push("Dump Tags");
                 dump_block_tags = builder.define("Dump Block Tags", false);
                  dump_item_tags = builder.define("Dump Item Tags", false);
                dump_entity_tags = builder.define("Dump Entity Tags", false);
                 dump_biome_tags = builder.define("Dump Biome Tags", false);
           dump_enchantment_tags = builder.define("Dump Enchantment Tags", false);
           dump_damage_type_tags = builder.define("Dump Damage Type Tags", false);
            dump_game_event_tags = builder.define("Dump Game Event Tags", false);
                 dump_fluid_tags = builder.define("Dump Fluid Tags", false);
                   dump_poi_tags = builder.define("Dump POI Tags", false);
             dump_structure_tags = builder.define("Dump Structure Tags", false);
        dump_banner_pattern_tags = builder.define("Dump Banner Pattern Tags", false);
      dump_painting_variant_tags = builder.define("Dump Painting Variant Tags", false);
    builder.pop();
  }

}
