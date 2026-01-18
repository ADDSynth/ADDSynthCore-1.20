package addsynth.core.gameplay;

import addsynth.core.block_network.DebugBlockNetwork;
import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {

  // Debug
  public static ForgeConfigSpec.BooleanValue debug_mod_detection;
  public static ForgeConfigSpec.BooleanValue dump_map_colors;

  public static ForgeConfigSpec.BooleanValue dump_block_tags;
  public static ForgeConfigSpec.BooleanValue dump_item_tags;
  public static ForgeConfigSpec.BooleanValue dump_entity_tags;
  public static ForgeConfigSpec.BooleanValue dump_biome_tags;
  // public static ForgeConfigSpec.BooleanValue dump_enchantment_tags;
  public static ForgeConfigSpec.BooleanValue dump_damage_type_tags;
  public static ForgeConfigSpec.BooleanValue dump_game_event_tags;
  public static ForgeConfigSpec.BooleanValue dump_fluid_tags;
  public static ForgeConfigSpec.BooleanValue dump_poi_tags;
  public static ForgeConfigSpec.BooleanValue dump_structure_tags;
  public static ForgeConfigSpec.BooleanValue dump_banner_pattern_tags;
  public static ForgeConfigSpec.BooleanValue dump_painting_variant_tags;

  public static ForgeConfigSpec.BooleanValue debug_block_networks;
  public static ForgeConfigSpec.EnumValue<DebugBlockNetwork.DEBUG_LEVEL> block_network_debug_level;

  // Music Box
  public static ForgeConfigSpec.BooleanValue enable_left_hand;

  // Team Manager
  public static ForgeConfigSpec.BooleanValue translate_criteria_list;

  // Hedge Trimmers
  public static class HedgeTrimmers {
    // public static ForgeConfigSpec.IntValue     horizontal_distance; Save this for a Values config file
    // public static ForgeConfigSpec.IntValue     vertical_distance;
    // public static ForgeConfigSpec.BooleanValue realistic_distance;
  }

  public static ForgeConfigSpec.BooleanValue item_explosion_command;
  public static ForgeConfigSpec.BooleanValue zombie_raid_command;
  public static ForgeConfigSpec.BooleanValue blackout_command;
  public static ForgeConfigSpec.BooleanValue lightning_storm_command;

  // Other Mods
  public enum EMCValueDefinition {DEVELOPER_DEFINED, ACCURATE}
  public static ForgeConfigSpec.EnumValue<EMCValueDefinition> emc_definition;
  public static final boolean emcDeveloperDefined(){
    return emc_definition.get() == EMCValueDefinition.DEVELOPER_DEFINED;
  }

  public static ForgeConfigSpec.BooleanValue show_advanced_config;

  public Config(final ForgeConfigSpec.Builder builder){

    builder.push("Debug");
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
        //     dump_enchantment_tags = builder.define("Dump Enchantment Tags", false);
             dump_damage_type_tags = builder.define("Dump Damage Types Tags", false);
              dump_game_event_tags = builder.define("Dump Game Event Tags", false);
                   dump_fluid_tags = builder.define("Dump Fluid Tags", false);
                     dump_poi_tags = builder.define("Dump POI Tags", false);
               dump_structure_tags = builder.define("Dump Structure Tags", false);
          dump_banner_pattern_tags = builder.define("Dump Banner Pattern Tags", false);
        dump_painting_variant_tags = builder.define("Dump Painting Variant Tags", false);
      builder.pop();
    builder.pop();

    builder.push("Music Box");
    enable_left_hand = builder.comment(
      "By default, the Music Box uses Right-Hand controls (Left-click adds notes, Right-click deletes notes.)\n"+
      "Set this to true to enable Left-Hand controls, which will swap these functions.")
      .define("Enable Left Hand", false);
    builder.pop();

    builder.push("Team Manager");
    translate_criteria_list = builder.comment(
      "Client Only. Determines whether the Criteria List in the Team Manager displays translated names (true)\n"+
      "or displays the ID names (false). You can also change this in the Team Manager Objective Edit screen.")
      .define("Translate Criteria List", true);
    builder.pop();

    // builder.push("Hedge Trimmers");
    //   HedgeTrimmers.realistic_distance = builder.comment(
    //     "Destroy blocks in a spherical shape (true) or in a cube shape (false)."
    //   ).define("Mine in Spherical Shape", true);
    // builder.pop();

    builder.push("Compatibility");
      builder.push("Project E");
        emc_definition = builder.defineEnum("How Should EMC Values be Calculated", EMCValueDefinition.DEVELOPER_DEFINED);
      builder.pop();
    builder.pop();

    builder.push("Commands");
    item_explosion_command  = builder.define("Item Explosion",  true);
    zombie_raid_command     = builder.define("Zombie Raid",     true);
    blackout_command        = builder.define("Blackout",        true);
    lightning_storm_command = builder.define("Lightning Storm", true);
    builder.pop();

    builder.push("Advanced");
    show_advanced_config = builder.comment(
      "Enabling this will grant you access to advanced configuration options in the Mod's Configuration screen.\n"+
      "Advanced configuration options such as those in the worldgen.toml file allow you access to internal game values,\n"+
      "and adjusting them will vastly alter gameplay. They are only intended to be used for debug, testing, or\n"+
      "experimental purposes. In order to maintain a standard gameplay experience (the way the author intended)\n"+
      "we encourage you to leave these values at their defaults. (However, modpack authors may want to adjust these\n"+
      "values in order to create a balanced gameplay.)")
      .define("Show Advanced Config in Client Gui", false);
    builder.pop();
  }

}
