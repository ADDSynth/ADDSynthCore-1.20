package addsynth.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import addsynth.core.gameplay.Config;
import addsynth.core.util.color.ColorUtil;
import addsynth.core.util.java.FileUtil;
import addsynth.core.util.java.StringUtil;
import addsynth.core.util.java.list.Sorters;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public final class Debug {

  public static final boolean debug_recipes = false;
  
  public static final void log_recipe(Recipe recipe){
    if(debug_recipes){
      ADDSynthCore.log.info("Added "+recipe.getClass().getSimpleName()+" "+recipe.getId()+".");
    }
  }

  public static final void block(final Block block){
    block(block, null);
  }

  public static final void block(final Block block, final BlockPos position){
    final StringBuilder s = new StringBuilder();
    s.append("Debug Block: Class: ");
    s.append(block.getClass().getName());
    s.append(", Registry ID: ");
    s.append(ForgeRegistries.BLOCKS.getKey(block));
    // s.append(", Translation Key: ");
    // s.append(block.getDescriptionId());
    if(position != null){
      s.append(", Position: ");
      s.append(StringUtil.print(position));
    }
    ADDSynthCore.log.warn(s.toString());
  }

  public static final void item(final Item item){
    final StringBuilder s = new StringBuilder();
    s.append("Debug Item: Class: ");
    s.append(item.getClass().getName());
    s.append(", Registry ID: ");
    s.append(ForgeRegistries.ITEMS.getKey(item));
    // s.append(", Translation Key: ");
    // s.append(item.getDescriptionId());
    ADDSynthCore.log.warn(s.toString());
  }

  public static final void debug(){
    if(Config.dump_map_colors.get()){
      ColorUtil.dump_map_colors();
    }
  }

  // This must be run when tags are done being loaded.
  @SuppressWarnings("deprecation")
  public static final void dump_tags(final RegistryAccess registry){
    if(Config.dump_block_tags.get()){
      printTags("block_tags.txt",            "Block",               BuiltInRegistries.BLOCK);
    }
    if(Config.dump_item_tags.get()){
      printTags("item_tags.txt",             "Item",                BuiltInRegistries.ITEM);
    }
    if(Config.dump_entity_tags.get()){
      printTags("entity_tags.txt",           "Entity",              BuiltInRegistries.ENTITY_TYPE);
    }
    if(Config.dump_biome_tags.get()){
      printTags("biome_tags.txt",            "Biome",               registry.registry(Registries.BIOME));
    }
    // if(Config.dump_enchantment_tags.get()){
    // FUTURE: Enchantment Tags aren't until Minecraft 1.20.x
    //   printTags("enchantment_tags.txt",      "Enchantment",         BuiltInRegistries.ENCHANTMENT);
    // }
    if(Config.dump_damage_type_tags.get()){
      printTags("damage_type_tags.txt",      "Damage Type",         registry.registry(Registries.DAMAGE_TYPE));
    }
    if(Config.dump_game_event_tags.get()){
      printTags("game_event_tags.txt",       "Game Event",          BuiltInRegistries.GAME_EVENT);
    }
    if(Config.dump_fluid_tags.get()){
      printTags("fluid_tags.txt",            "Fluid",               BuiltInRegistries.FLUID);
    }
    if(Config.dump_poi_tags.get()){
      printTags("poi_tags.txt",              "Points of Interest",  BuiltInRegistries.POINT_OF_INTEREST_TYPE);
    }
    if(Config.dump_structure_tags.get()){
      // Dump Structure Tags isn't working
      printTags("structure_tags.txt",        "Structure",           registry.registry(Registries.STRUCTURE));
    }
    if(Config.dump_banner_pattern_tags.get()){
      printTags("banner_pattern_tags.txt",   "Banner Pattern",      BuiltInRegistries.BANNER_PATTERN);
    }
    if(Config.dump_painting_variant_tags.get()){
      printTags("painting_variant_tags.txt", "Painting Variant",    BuiltInRegistries.PAINTING_VARIANT);
    }
  }

  private static final <T> void printTags(final String filename, final String type_name, final Optional<Registry<T>> optional){
    if(optional.isPresent()){
      printTags(filename, type_name, optional.get());
    }
  }

  private static final <T> void printTags(final String filename, final String type_name, final Registry<T> registry){
    final List<Pair<TagKey<T>, HolderSet.Named<T>>> tags = registry.getTags().sorted(Sorters::TagPairComparerMinecraftFirst).toList();
    if(tags.size() > 0){
      final File file = FileUtil.getNewFile(filename);
      ResourceLocation location;
      try(final FileWriter writer = new FileWriter(file)){
        writer.write(StringUtil.build("\n", type_name, " Tags: ", tags.size(), "\n\n"));
        for(final Pair<TagKey<T>, HolderSet.Named<T>> pair : tags){
          writer.write(pair.getFirst().location().toString()+" {\n");
          for(final Holder<T> entry : pair.getSecond()){
            location = registry.getKey(entry.get());
            if(location != null){
              writer.write(StringUtil.build("  ", location.toString(), '\n'));
            }
          }
          writer.write("}\n\n");
        }
        ADDSynthCore.log.info("Dumped "+type_name+" Tags.");
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }
  }

}
