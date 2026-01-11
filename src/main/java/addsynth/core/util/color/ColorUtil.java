package addsynth.core.util.color;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import addsynth.core.ADDSynthCore;
import addsynth.core.util.java.FileUtil;
import addsynth.core.util.java.StringUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

// JAVA #1: What? Java doesn't have unsigned numeric types? That's the stupidest thing I've ever heard!
// https://stackoverflow.com/questions/430346/why-doesnt-java-support-unsigned-ints
// https://en.wikipedia.org/wiki/Criticism_of_Java#Unsigned_integer_types

/** <p>This utility class is used to assist in dealing with Minecraft's color codes.
 *     Minecraft's color codes are integers that use 8 bits each to store the red,
 *     green, and blue channels in that order.
 *  <p>ADDSynthCore will automatically call {@link #dump_map_colors()} if it is enabled
 *     in the config.
 * @author ADDSynth
 * @see Color
 * @see Colors
 * @since October 23, 2019
 */
public final class ColorUtil {

  public static final ImmutableMap<MapColor, String> map_color_names = new ImmutableMap.Builder<MapColor, String>()
    .put(MapColor.NONE,                   "MapColor.NONE")
    .put(MapColor.GRASS,                  "MapColor.GRASS")
    .put(MapColor.SAND,                   "MapColor.SAND")
    .put(MapColor.WOOL,                   "MapColor.WOOL")
    .put(MapColor.FIRE,                   "MapColor.FIRE")
    .put(MapColor.ICE,                    "MapColor.ICE")
    .put(MapColor.METAL,                  "MapColor.METAL")
    .put(MapColor.PLANT,                  "MapColor.PLANT")
    .put(MapColor.SNOW,                   "MapColor.SNOW")
    .put(MapColor.CLAY,                   "MapColor.CLAY")
    .put(MapColor.DIRT,                   "MapColor.DIRT")
    .put(MapColor.STONE,                  "MapColor.STONE")
    .put(MapColor.WATER,                  "MapColor.WATER")
    .put(MapColor.WOOD,                   "MapColor.WOOD")
    .put(MapColor.QUARTZ,                 "MapColor.QUARTZ")
    .put(MapColor.COLOR_ORANGE,           "MapColor.COLOR_ORANGE")
    .put(MapColor.COLOR_MAGENTA,          "MapColor.COLOR_MAGENTA")
    .put(MapColor.COLOR_LIGHT_BLUE,       "MapColor.COLOR_LIGHT_BLUE")
    .put(MapColor.COLOR_YELLOW,           "MapColor.COLOR_YELLOW")
    .put(MapColor.COLOR_LIGHT_GREEN,      "MapColor.COLOR_LIGHT_GREEN")
    .put(MapColor.COLOR_PINK,             "MapColor.COLOR_PINK")
    .put(MapColor.COLOR_GRAY,             "MapColor.COLOR_GRAY")
    .put(MapColor.COLOR_LIGHT_GRAY,       "MapColor.COLOR_LIGHT_GRAY")
    .put(MapColor.COLOR_CYAN,             "MapColor.COLOR_CYAN")
    .put(MapColor.COLOR_PURPLE,           "MapColor.COLOR_PURPLE")
    .put(MapColor.COLOR_BLUE,             "MapColor.COLOR_BLUE")
    .put(MapColor.COLOR_BROWN,            "MapColor.COLOR_BROWN")
    .put(MapColor.COLOR_GREEN,            "MapColor.COLOR_GREEN")
    .put(MapColor.COLOR_RED,              "MapColor.COLOR_RED")
    .put(MapColor.COLOR_BLACK,            "MapColor.COLOR_BLACK")
    .put(MapColor.GOLD,                   "MapColor.GOLD")
    .put(MapColor.DIAMOND,                "MapColor.DIAMOND")
    .put(MapColor.LAPIS,                  "MapColor.LAPIS")
    .put(MapColor.EMERALD,                "MapColor.EMERALD")
    .put(MapColor.PODZOL,                 "MapColor.PODZOL")
    .put(MapColor.NETHER,                 "MapColor.NETHER")
    .put(MapColor.TERRACOTTA_WHITE,       "MapColor.TERRACOTTA_WHITE")
    .put(MapColor.TERRACOTTA_ORANGE,      "MapColor.TERRACOTTA_ORANGE")
    .put(MapColor.TERRACOTTA_MAGENTA,     "MapColor.TERRACOTTA_MAGENTA")
    .put(MapColor.TERRACOTTA_LIGHT_BLUE,  "MapColor.TERRACOTTA_LIGHT_BLUE")
    .put(MapColor.TERRACOTTA_YELLOW,      "MapColor.TERRACOTTA_YELLOW")
    .put(MapColor.TERRACOTTA_LIGHT_GREEN, "MapColor.TERRACOTTA_LIGHT_GREEN")
    .put(MapColor.TERRACOTTA_PINK,        "MapColor.TERRACOTTA_PINK")
    .put(MapColor.TERRACOTTA_GRAY,        "MapColor.TERRACOTTA_GRAY")
    .put(MapColor.TERRACOTTA_LIGHT_GRAY,  "MapColor.TERRACOTTA_LIGHT_GRAY")
    .put(MapColor.TERRACOTTA_CYAN,        "MapColor.TERRACOTTA_CYAN")
    .put(MapColor.TERRACOTTA_PURPLE,      "MapColor.TERRACOTTA_PURPLE")
    .put(MapColor.TERRACOTTA_BLUE,        "MapColor.TERRACOTTA_BLUE")
    .put(MapColor.TERRACOTTA_BROWN,       "MapColor.TERRACOTTA_BROWN")
    .put(MapColor.TERRACOTTA_GREEN,       "MapColor.TERRACOTTA_GREEN")
    .put(MapColor.TERRACOTTA_RED,         "MapColor.TERRACOTTA_RED")
    .put(MapColor.TERRACOTTA_BLACK,       "MapColor.TERRACOTTA_BLACK")
    .put(MapColor.CRIMSON_NYLIUM,         "MapColor.CRIMSON_NYLIUM")
    .put(MapColor.CRIMSON_STEM,           "MapColor.CRIMSON_STEM")
    .put(MapColor.CRIMSON_HYPHAE,         "MapColor.CRIMSON_HYPHAE")
    .put(MapColor.WARPED_NYLIUM,          "MapColor.WARPED_NYLIUM")
    .put(MapColor.WARPED_STEM,            "MapColor.WARPED_STEM")
    .put(MapColor.WARPED_HYPHAE,          "MapColor.WARPED_HYPHAE")
    .put(MapColor.WARPED_WART_BLOCK,      "MapColor.WARPED_WART_BLOCK")
    .put(MapColor.DEEPSLATE,              "MapColor.DEEPSLATE")
    .put(MapColor.RAW_IRON,               "MapColor.RAW_IRON")
    .put(MapColor.GLOW_LICHEN,            "MapColor.GLOW_LICHEN")
    .build();

  private static final class ColorSet implements Comparable<ColorSet> {
    public MapColor mapcolor;
    public int difference;
    public Block[] blocks;
    
    public ColorSet(final MapColor mapcolor, final int difference, final Block[] blocks){
      this.mapcolor = mapcolor;
      this.difference = difference;
      this.blocks = blocks;
    }

    @Override
    public final int compareTo(final ColorSet obj){
      return (int)Math.signum(this.difference - obj.difference);
    }

    @Override
    public final String toString(){
      return map_color_names.get(mapcolor);
    }
  }

  public static final void dump_map_colors(){
    ADDSynthCore.log.info("Begin dumping Minecraft Map Colors...");

    final String debug_file = "debug_map_colors.txt";
    final File file = FileUtil.getNewFile(debug_file);

    if(file != null){
      try(final FileWriter writer = new FileWriter(file)){
        writer.write("ADDSynthCore: debug Minecraft Map Colors:\n\n\n");
  
        int i;
        final Colors[] color_values = Colors.values();
        final int length = color_values.length;
        final ColorSet[][] set = build_color_list();
        final IForgeRegistry<Block> registry = ForgeRegistries.BLOCKS;

        for(i = 0; i < length; i++){
          writer.write(color_values[i].name+" colors: "+StringUtil.printColor(color_values[i].value)+"\n");
          for(ColorSet color : set[i]){
            writer.write("   "+map_color_names.get(color.mapcolor)+" "+StringUtil.printColor(color.mapcolor.col)+"\n");
            for(Block block : color.blocks){
              writer.write("      "+registry.getKey(block)+"\n");
            }
          }
          writer.write("\n\n");
        }
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }
    
    ADDSynthCore.log.info("Done dumping Minecraft Map Colors.");
  }

  private static final ColorSet[][] build_color_list(){
    // Part 1: verify MapColor array is in order
    final int total_length = 64;
    int number_of_colors = 0;
    int i;
    for(i = 0; i < total_length; i++){
      if(MapColor.byId(i) != MapColor.NONE){
        number_of_colors += 1;
      }
    }
    number_of_colors += 1; // so we include MapColor 0, which is NONE.
    final MapColor[] map_colors = new MapColor[number_of_colors];
    for(i = 0; i < number_of_colors; i++){
      map_colors[i] = MapColor.byId(i);
    }

    // Part 2: create difference list. get the difference for each MapColor against each color we're testing.
    final Colors[] color_values = Colors.values();
    final int length = color_values.length;
    final int[][] difference = new int[number_of_colors][length];
    int j;
    int k;
    int map_color;
    int color;
    int red;
    int green;
    int blue;

    for(j = 0; j < number_of_colors; j++){
      for(k = 0; k < length; k++){
        map_color = map_colors[j].col;
        color     = color_values[k].value;
        red   = Math.abs(Color.getRed(map_color) - Color.getRed(color));
        green = Math.abs(Color.getGreen(map_color) - Color.getGreen(color));
        blue  = Math.abs(Color.getBlue(map_color) - Color.getBlue(color));
        difference[j][k] = red + green + blue;
      }
    }

    // Part 3: For each color in color_list, get associated MapColors via override or closest match.
    final ColorSet[][] list = new ColorSet[length][];
    int lowest_value;
    int lowest_index;
    TreeSet<ColorSet> values;
    boolean found_override;

    for(i = 0; i < length; i++){
      values = new TreeSet<ColorSet>();
      for(j = 0; j < number_of_colors; j++){
      
        found_override = false;

        // Part 3a: look for override
        for(k = 0; k < length && !found_override; k++){
          for(MapColor mp : color_values[k].colors){
            if(map_colors[j] == mp){
              found_override = true;
              if(k == i){
                values.add(new ColorSet(mp, difference[j][i], get_blocks_that_match_color(mp)));
              }
              break;
            }
          }
        }
        // Part 3b: no override found, match color with the lowest difference
        if(found_override == false){
          // get lowest for this row
          lowest_index = 0;
          lowest_value = difference[j][0];
          for(k = 1; k < length; k++){
            if(difference[j][k] < lowest_value){
              lowest_value = difference[j][k];
              lowest_index = k;
            }
          }
          if(lowest_index == i){
            values.add(new ColorSet(map_colors[j], lowest_value, get_blocks_that_match_color(map_colors[j])));
          }
        }
      }

      // Part 3c: Set value list to array for Color i
      list[i] = values.toArray(new ColorSet[values.size()]);
    }

    return list;
  }

  public static final Block[] get_blocks_that_match_color(final MapColor test_color){
    final ArrayList<Block> blocks = new ArrayList<>(500);
    for(final Block block : ForgeRegistries.BLOCKS){
      if(block.defaultMapColor() == test_color){
        blocks.add(block);
      }
    }
    return blocks.toArray(new Block[blocks.size()]);
  }

}
