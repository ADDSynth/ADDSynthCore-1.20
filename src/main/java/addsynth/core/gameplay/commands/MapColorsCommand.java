package addsynth.core.gameplay.commands;

import addsynth.core.ADDSynthCore;
import addsynth.core.util.command.PermissionLevel;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.MapColor;

public final class MapColorsCommand {

  private static final int DEFAULT_SIZE = 5;
  private static final int MAX_SIZE = 10;
  private static final int DEFAULT_REMOVE_SIZE = 256;

  private static int x,y,z;
  private static int start_x, start_z;
  private static int end_x, end_z;
  private static MutableBlockPos position = new MutableBlockPos();

  public static final void register(CommandDispatcher<CommandSourceStack> dispatcher){
    dispatcher.register(
      Commands.literal(ADDSynthCore.MOD_ID).requires(
        (command_source) -> {return command_source.hasPermission(PermissionLevel.COMMANDS); }
      ).then(
        Commands.literal("map_colors").executes(
          (command_context) -> { return spawn_map_colors(command_context.getSource(), DEFAULT_SIZE); }
        ).then(
          Commands.argument("size", IntegerArgumentType.integer(1, MAX_SIZE)).executes(
            (command_context) -> { return spawn_map_colors(command_context.getSource(),
                                                           IntegerArgumentType.getInteger(command_context, "size")); }
          )
        ).then(
          Commands.literal("remove").executes(
            (command_context) -> { return remove_map_colors(command_context.getSource()); }
          )
        )
      )
    );
  }

  @SuppressWarnings("resource")
  private static final int spawn_map_colors(final CommandSourceStack command_source, final int size){
    // Check dimension
    final ServerLevel level = command_source.getLevel();
    if(checkDimension(level, command_source)){
      return 0;
    }
    // Check if Entity is Player and has a map.
    final Entity entity = command_source.getEntity();
    if(entity instanceof Player){
      final Player player = (Player)entity;
      final Inventory inventory = player.getInventory();
      final boolean has_map = inventory.hasAnyMatching((ItemStack stack) -> {
        return stack.getItem() == Items.FILLED_MAP;
      });
      if(!has_map){
        command_source.sendFailure(Component.literal("Player MUST have an opened map in their inventory."));
        return 0;
      }
    }
    else{
      command_source.sendFailure(
        Component.literal("/addsynthcore map_colors Command can only be executed by a player with a map.")
      );
      return 0;
    }
    final BlockPos origin = BlockPos.containing(command_source.getPosition());
    y = level.getMaxBuildHeight() - 1;
    position.setY(y);
    // No block uses MapColor.NONE, all Transparent blocks (blocks that don't show up on map) use this
    // White
    spawnMapColor(level, MapColor.SNOW,               Blocks.SNOW_BLOCK,         origin, -6, -2, size);
    spawnMapColor(level, MapColor.QUARTZ,             Blocks.QUARTZ_BLOCK,       origin, -6, -1, size);
    spawnMapColor(level, MapColor.WOOL,               Blocks.COBWEB,             origin, -6,  0, size); // 199
    spawnMapColor(level, MapColor.CLAY,               Blocks.CLAY,               origin, -6,  1, size); // 172
    // Gray
    spawnMapColor(level, MapColor.TERRACOTTA_LIGHT_BLUE, Blocks.LIGHT_BLUE_TERRACOTTA, origin, -5, -3, size);
    spawnMapColor(level, MapColor.METAL,              Blocks.IRON_BLOCK,         origin, -5, -2, size); // 167
    spawnMapColor(level, MapColor.COLOR_LIGHT_GRAY,   Blocks.LIGHT_GRAY_WOOL,    origin, -5, -1, size); // 153
    spawnMapColor(level, MapColor.STONE,              Blocks.STONE,              origin, -5,  0, size); // 112
    spawnMapColor(level, MapColor.DEEPSLATE,          Blocks.DEEPSLATE,          origin, -5,  1, size); // 100
    spawnMapColor(level, MapColor.TERRACOTTA_CYAN,    Blocks.CYAN_TERRACOTTA,    origin, -5,  2, size); //  90
    // Black
    spawnMapColor(level, MapColor.TERRACOTTA_BLUE,    Blocks.BLUE_TERRACOTTA,    origin, -4, -3, size);
    spawnMapColor(level, MapColor.COLOR_GRAY,         Blocks.GRAY_WOOL,          origin, -4, -2, size); //  76
    spawnMapColor(level, MapColor.TERRACOTTA_GRAY,    Blocks.GRAY_TERRACOTTA,    origin, -4, -1, size); //  44
    spawnMapColor(level, MapColor.TERRACOTTA_BLACK,   Blocks.BLACK_TERRACOTTA,   origin, -4,  0, size); //  25 (red)
    spawnMapColor(level, MapColor.COLOR_BLACK,        Blocks.BLACK_WOOL,         origin, -4,  1, size); //  25
    // Brown
    spawnMapColor(level, MapColor.SAND,               Blocks.SANDSTONE,          origin, -3, -4, size);
    spawnMapColor(level, MapColor.TERRACOTTA_WHITE,   Blocks.WHITE_TERRACOTTA,   origin, -3, -3, size);
    spawnMapColor(level, MapColor.RAW_IRON,           Blocks.RAW_IRON_BLOCK,     origin, -3, -2, size);
    spawnMapColor(level, MapColor.DIRT,               Blocks.DIRT,               origin, -3, -1, size);
    spawnMapColor(level, MapColor.WOOD,               Blocks.OAK_PLANKS,         origin, -3,  0, size);
    spawnMapColor(level, MapColor.PODZOL,             Blocks.PODZOL,             origin, -3,  1, size);
    spawnMapColor(level, MapColor.TERRACOTTA_LIGHT_GRAY, Blocks.LIGHT_GRAY_TERRACOTTA, origin, -3, 2, size);
    spawnMapColor(level, MapColor.COLOR_BROWN,        Blocks.BROWN_WOOL,         origin, -3,  3, size);
    spawnMapColor(level, MapColor.TERRACOTTA_BROWN,   Blocks.BROWN_TERRACOTTA,   origin, -3,  4, size);
    // Red
    spawnMapColor(level, MapColor.FIRE,               Blocks.TNT,                origin, -2, -3, size);
    spawnMapColor(level, MapColor.CRIMSON_NYLIUM,     Blocks.CRIMSON_NYLIUM,     origin, -2, -2, size);
    spawnMapColor(level, MapColor.TERRACOTTA_PINK,    Blocks.PINK_TERRACOTTA,    origin, -2, -1, size);
    spawnMapColor(level, MapColor.TERRACOTTA_RED,     Blocks.RED_TERRACOTTA,     origin, -2,  0, size);
    spawnMapColor(level, MapColor.COLOR_RED,          Blocks.RED_WOOL,           origin, -2,  1, size);
    spawnMapColor(level, MapColor.NETHER,             Blocks.NETHERRACK,         origin, -2,  2, size);
    spawnMapColor(level, MapColor.CRIMSON_HYPHAE,     Blocks.CRIMSON_HYPHAE,     origin, -2,  3, size);
    // Orange
    spawnMapColor(level, MapColor.TERRACOTTA_YELLOW,  Blocks.YELLOW_TERRACOTTA,  origin, -1, -1, size);
    spawnMapColor(level, MapColor.COLOR_ORANGE,       Blocks.PUMPKIN,            origin, -1,  0, size);
    spawnMapColor(level, MapColor.TERRACOTTA_ORANGE,  Blocks.ORANGE_TERRACOTTA,  origin, -1,  1, size);
    // Yellow
    spawnMapColor(level, MapColor.GOLD,               Blocks.GOLD_BLOCK,         origin,  0, -1, size);
    spawnMapColor(level, MapColor.COLOR_YELLOW,       Blocks.YELLOW_WOOL,        origin,  0,  0, size);
    // Green
    spawnMapColor(level, MapColor.COLOR_LIGHT_GREEN,  Blocks.LIME_WOOL,          origin,  1, -3, size);
    spawnMapColor(level, MapColor.GRASS,              Blocks.GRASS_BLOCK,        origin,  1, -2, size);
    spawnMapColor(level, MapColor.EMERALD,            Blocks.EMERALD_BLOCK,      origin,  1, -1, size);
    spawnLeaves(level, origin, 1, 0, size);
    spawnMapColor(level, MapColor.COLOR_GREEN,        Blocks.GREEN_WOOL,         origin,  1,  1, size);
    spawnMapColor(level, MapColor.TERRACOTTA_LIGHT_GREEN, Blocks.LIME_TERRACOTTA, origin, 1,  2, size);
    spawnMapColor(level, MapColor.TERRACOTTA_GREEN,   Blocks.GREEN_TERRACOTTA,   origin,  1,  3, size);
    // Cyan
    spawnMapColor(level, MapColor.DIAMOND,            Blocks.DIAMOND_BLOCK,      origin, 2, -3, size);
    spawnMapColor(level, MapColor.WARPED_WART_BLOCK,  Blocks.WARPED_WART_BLOCK,  origin, 2, -2, size);
    spawnMapColor(level, MapColor.GLOW_LICHEN,        Blocks.VERDANT_FROGLIGHT,  origin, 2, -1, size);
    spawnMapColor(level, MapColor.WARPED_STEM,        Blocks.WARPED_STEM,        origin, 2,  0, size);
    spawnMapColor(level, MapColor.COLOR_CYAN,         Blocks.CYAN_WOOL,          origin, 2,  1, size);
    spawnMapColor(level, MapColor.WARPED_NYLIUM,      Blocks.WARPED_NYLIUM,      origin, 2,  2, size);
    // Blue
    spawnMapColor(level, MapColor.ICE,                Blocks.PACKED_ICE,         origin, 3, -2, size);
    spawnMapColor(level, MapColor.COLOR_LIGHT_BLUE,   Blocks.LIGHT_BLUE_WOOL,    origin, 3, -1, size);
    spawnMapColor(level, MapColor.LAPIS,              Blocks.LAPIS_BLOCK,        origin, 3,  0, size);
    spawnWater(level, origin, 3, 1, size);
    spawnMapColor(level, MapColor.COLOR_BLUE,         Blocks.BLUE_WOOL,          origin, 3,  2, size);
    // Purple
    spawnMapColor(level, MapColor.COLOR_PINK,         Blocks.PINK_WOOL,          origin, 4, -1, size);
    spawnMapColor(level, MapColor.COLOR_MAGENTA,      Blocks.MAGENTA_WOOL,       origin, 4,  0, size);
    spawnMapColor(level, MapColor.COLOR_PURPLE,       Blocks.PURPLE_WOOL,        origin, 4,  1, size);
    // Magenta
    spawnMapColor(level, MapColor.CRIMSON_STEM,       Blocks.CRIMSON_STEM,       origin, 5, -2, size);
    spawnMapColor(level, MapColor.TERRACOTTA_MAGENTA, Blocks.MAGENTA_TERRACOTTA, origin, 5, -1, size);
    spawnMapColor(level, MapColor.TERRACOTTA_PURPLE,  Blocks.PURPLE_TERRACOTTA,  origin, 5,  0, size);
    spawnMapColor(level, MapColor.WARPED_HYPHAE,      Blocks.WARPED_HYPHAE,      origin, 5,  1, size);
    
    command_source.sendSuccess(() -> {
      return Component.literal("Spawned MapColor blocks at "+origin.toShortString()+".");
    }, true);
    return 0;
  }

  private static final void spawnMapColor(final ServerLevel level, final MapColor color, final Block block, final BlockPos origin, int xStart, int zStart, int size){
    start_x = origin.getX() + (xStart * size);
    start_z = origin.getZ() + (zStart * size);
    end_x = start_x + size;
    end_z = start_z + size;
    for(z = start_z; z < end_z; z++){
      position.setZ(z);
      for(x = start_x; x < end_x; x++){
        position.setX(x);
        level.setBlockAndUpdate(position, block.defaultBlockState());
      }
    }
  }

  private static final void spawnLeaves(final ServerLevel level, final BlockPos origin, int xStart, int zStart, int size){
    position.setY(y - 1);
    spawnMapColor(level, MapColor.WOOD, Blocks.OAK_WOOD, origin, xStart, zStart, size);
    position.setY(y);
    spawnMapColor(level, MapColor.PLANT, Blocks.OAK_LEAVES, origin, xStart, zStart, size);
  }

  private static final void spawnWater(final ServerLevel level, final BlockPos origin, int xStart, int zStart, int size){
    position.setY(y - 1);
    spawnMapColor(level, MapColor.STONE, Blocks.STONE, origin, xStart, zStart, size);
    position.setY(y);
    spawnMapColor(level, MapColor.WATER, Blocks.WATER, origin, xStart, zStart, size);
  }

  @SuppressWarnings("resource")
  private static final int remove_map_colors(final CommandSourceStack command_source){
    final ServerLevel level = command_source.getLevel();
    if(checkDimension(level, command_source)){
      return 0;
    }
    final BlockPos origin = BlockPos.containing(command_source.getPosition());
    start_x = origin.getX() - DEFAULT_REMOVE_SIZE;
    y = level.getMaxBuildHeight() - 2;
    start_z = origin.getZ() - DEFAULT_REMOVE_SIZE;
    end_x = origin.getX() + DEFAULT_REMOVE_SIZE;
    final int end_y = level.getMaxBuildHeight();
    end_z = origin.getZ() + DEFAULT_REMOVE_SIZE;
    for(; y < end_y; y++){
      position.setY(y);
      for(z = start_z; z < end_z; z++){
        position.setZ(z);
        for(x = start_x; x < end_x; x++){
          position.setX(x);
          // level.removeBlock(position, false); // does not remove water
          level.setBlockAndUpdate(position, Blocks.AIR.defaultBlockState());
        }
      }
    }
    command_source.sendSuccess(() -> {
      return Component.literal("Removed MapColor Blocks.");
    }, true);
    return 0;
  }

  private static final boolean checkDimension(ServerLevel level, CommandSourceStack command_source){
    final DimensionType dimension = level.dimensionType();
    if(dimension.hasCeiling() || level.dimensionTypeId().equals(BuiltinDimensionTypes.NETHER)){
      command_source.sendFailure(Component.literal("This command is not allowed in this dimension."));
      return true;
    }
    return false;
  }

}
