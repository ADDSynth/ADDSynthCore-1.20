package addsynth.core.gameplay.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Map.Entry;
import addsynth.core.ADDSynthCore;
import addsynth.core.util.command.PermissionLevel;
import addsynth.core.util.command.TagArgumentTester;
import addsynth.core.util.java.FileUtil;
import addsynth.core.util.java.JavaUtils;
import addsynth.core.util.java.StringUtil;
import addsynth.core.util.math.block.BlockMath;
import addsynth.core.util.world.WorldConstants;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITag;

public final class ShowOresCommand {

  private static final int DEFAULT_CHUNK_SIZE = 15;
  private static final int MAX_CHUNK_SIZE = 40;
  private static final String ore_sample_file = "ore_sample.txt";

  public static final void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext build_context){
    /*
    dispatcher.register(
      Commands.literal(ADDSynthCore.MOD_ID).requires(
        (command_source) -> { return command_source.hasPermission(PermissionLevel.COMMANDS); }
      ).then(
        Commands.literal("show_ores").executes(
          (command_context) -> { return show_ores(command_context.getSource(), null); }
        ).then(
          Commands.argument("block", BlockStateArgument.block()).executes(
            (command_context) -> { return show_ores(command_context.getSource(), BlockStateArgument.getBlock(command_context, "block")); }
          )
        )
      )
    );
    */
    dispatcher.register(
      Commands.literal(ADDSynthCore.MOD_ID).requires(
        (command_source) -> { return command_source.hasPermission(PermissionLevel.COMMANDS); }
      ).then(
        Commands.literal("sample_ores").executes(
          (command_context) -> { return print_ore_sample(command_context.getSource(), DEFAULT_CHUNK_SIZE); }
        ).then(
          Commands.argument("size", IntegerArgumentType.integer(1, MAX_CHUNK_SIZE)).executes(
            (command_context) -> { return print_ore_sample(command_context.getSource(), IntegerArgumentType.getInteger(command_context, "size")); }
          )
        )
      )
    );
    dispatcher.register(
      Commands.literal(ADDSynthCore.MOD_ID).requires(
        (command_source) -> {return command_source.hasPermission(PermissionLevel.COMMANDS); }
      ).then(
        Commands.literal("count_blocks").then(
          Commands.argument("block", ResourceOrTagArgument.resourceOrTag(build_context, Registries.BLOCK)).executes(
            (command_context) -> {
              return count_ores(command_context.getSource(), ResourceOrTagArgument.getResourceOrTag(command_context, "block", Registries.BLOCK), DEFAULT_CHUNK_SIZE);
            }
          ).then(
            Commands.argument("size", IntegerArgumentType.integer(1, MAX_CHUNK_SIZE)).executes(
              (command_context) -> {
                return count_ores(command_context.getSource(), ResourceOrTagArgument.getResourceOrTag(command_context, "block", Registries.BLOCK),
                                                               IntegerArgumentType.getInteger(command_context, "size"));
              }
            )
          )
        )
      )
    );
  }

  /* DELETE in 2026
  private static final int show_ores(final CommandSource source, final BlockStateInput input_block_state){
    int ores = 0;
    final Entity entity = source.getEntity();
    if(entity != null){
      final World world = source.getLevel();
      final ITag<Block> ore_blocks = ForgeRegistries.BLOCKS.tags().getTag(Tags.Blocks.ORES);
      final BlockPos position = entity.blockPosition();
      final int x_coord = BlockMath.get_first_block_in_chunk(position.getX());
      final int z_coord = BlockMath.get_first_block_in_chunk(position.getZ());
      int x;
      int y;
      int z;
      BlockPos new_position;
      BlockState blockstate;
      Block block;
      FluidState fluid_state;
      for(y = WorldConstants.world_height - 1; y >= WorldConstants.bottom_level; y--){
        for(x = x_coord - 1; x < x_coord + 17; x++){
          for(z = z_coord - 1; z < z_coord + 17; z++){
            new_position = new BlockPos(x, y, z);
            blockstate = world.getBlockState(new_position);
            if(x == x_coord - 1 || x == x_coord + 16 || z == z_coord - 1 || z == z_coord + 16){
              fluid_state = blockstate.getFluidState();
              if(fluid_state.isSource()){
                world.setBlockAndUpdate(new_position, Blocks.STONE.defaultBlockState());
              }
            }
            else{
              block = blockstate.getBlock();
              if(ore_blocks.contains(block)){
                ores += 1;
              }
              else{
                WorldUtil.delete_block(world, new_position);
              }
            }
          }
        }
      }
    }
    return ores;
  }
  */

  @SuppressWarnings("null")
  private static final int print_ore_sample(final CommandSourceStack source, final int size){
    final Entity entity = source.getEntity();
    if(entity != null){
      
      final TreeMap<String, Integer> block_count = new TreeMap<>();
      final ITag<Block> ore_blocks = ForgeRegistries.BLOCKS.tags().getTag(Tags.Blocks.ORES);
      @SuppressWarnings("resource")
      final ServerLevel world = source.getLevel();
      final int height = world.getMaxBuildHeight();
      final BlockPos position = entity.blockPosition();
      final int chunks = size * size;
      final int offset = ((size - 1) / 2) * WorldConstants.chunk_size;
      final int x_coord = BlockMath.get_first_block_in_chunk(position.getX()) - offset;
      final int z_coord = BlockMath.get_first_block_in_chunk(position.getZ()) - offset;
      final int full_size = size * WorldConstants.chunk_size;
      final IForgeRegistry<Block> registry = ForgeRegistries.BLOCKS;
      int x;
      int y;
      int z;
      BlockPos new_position;
      BlockState blockstate;
      Block block;
      String text;
      int max_text_width = 0;
      for(y = world.getMinBuildHeight(); y < height; y++){
        for(x = 0; x < full_size; x++){
          for(z = 0; z < full_size; z++){
            new_position = new BlockPos(x_coord + x, y, z_coord + z);
            blockstate = world.getBlockState(new_position);
            block = blockstate.getBlock();
            if(ore_blocks.contains(block)){
              text = registry.getKey(block).toString();
              if(block_count.containsKey(text)){
                block_count.put(text, block_count.get(text)+1);
              }
              else{
                block_count.put(text, 1);
                if(text.length() > max_text_width){
                  max_text_width = text.length();
                }
              }
            }
          }
        }
      }
      
      final File file = FileUtil.getNewFile(ore_sample_file);
      try(final FileWriter writer = new FileWriter(file)){
        writer.write("Took ore sample of "+chunks+" chunks:");
        writer.write("\n\n");
        int number_width;
        int max_number_width = 1;
        for(int i : block_count.values()){
          number_width = Integer.toString(i).length();
          if(number_width > max_number_width){
            max_number_width = number_width;
          }
        }
        StringBuilder string_builder = new StringBuilder();
        int text_width;
        for(Entry<String, Integer> entry : block_count.entrySet()){
          string_builder.setLength(0);
          text = entry.getKey();
          text_width = text.length();
          string_builder.append(text);
          string_builder.append(' ');
          while(text_width < max_text_width){
            string_builder.append(' ');
            text_width += 1;
          }
          string_builder.append("= ");
          text = entry.getValue().toString();
          number_width = text.length();
          while(number_width < max_number_width){
            string_builder.append(' ');
            number_width += 1;
          }
          string_builder.append(text);
          string_builder.append('\n');
          writer.write(string_builder.toString());
        }
      }
      catch(IOException e){
        e.printStackTrace();
      }

      source.sendSuccess(() -> Component.literal("Ore sample saved to "+ore_sample_file+"."), true);
    }
    
    return 0;
  }

  private static final int count_ores(final CommandSourceStack source, final ResourceOrTagArgument.Result<Block> target, final int size){
    long blocks = 0;
    final Entity entity = source.getEntity();
    if(entity != null){
      final TagArgumentTester<Block> checker = new TagArgumentTester<Block>(target);
      final String name = checker.isTag ? "blocks matching tag "+checker.name : checker.name + " blocks";
      @SuppressWarnings("resource")
      final ServerLevel world = source.getLevel();
      final int height = world.getMaxBuildHeight();
      final BlockPos position = entity.blockPosition();
      final int chunks = size * size;
      final int offset = ((size - 1) / 2) * WorldConstants.chunk_size;
      final int x_coord = BlockMath.get_first_block_in_chunk(position.getX()) - offset;
      final int z_coord = BlockMath.get_first_block_in_chunk(position.getZ()) - offset;
      final int full_size = size * WorldConstants.chunk_size;
      int x;
      int y;
      int z;
      BlockPos new_position;
      BlockState blockstate;
      Block block;
      for(y = world.getMinBuildHeight(); y < height; y++){
        for(x = 0; x < full_size; x++){
          for(z = 0; z < full_size; z++){
            new_position = new BlockPos(x_coord + x, y, z_coord + z);
            blockstate = world.getBlockState(new_position);
            block = blockstate.getBlock();
            if(checker.test(block)){
              blocks += 1;
            }
          }
        }
      }
      if(blocks > 0){
        final String text = StringUtil.build("Found ", blocks, " ", name, " in ", chunks, " chunks. Average: ", String.format("%.2f", ((double)blocks/chunks)), " per chunk.");
        source.sendSuccess(() -> Component.literal(text), true);
      }
      else{
        final String text = StringUtil.build("No ", name, " found in ", chunks, " chunks.");
        source.sendSuccess(() -> Component.literal(text), true);
      }
    }
    return JavaUtils.cast_to_int(blocks);
  }

  // JAVA #5: This is the better way to do it, but Java prevents accessing local variables in a local function.
  /*
  private static final void count_blocks(final CommandSource source, final int size, final Consumer<Block> consumer){
    final Entity entity = source.getEntity();
    if(entity != null){
      final World world = source.getLevel();
      final BlockPos position = entity.blockPosition();
      final int chunks = size * size;
      final int offset = ((size - 1) / 2) * WorldConstants.chunk_size;
      final int x_coord = BlockMath.get_first_block_in_chunk(position.getX()) - offset;
      final int z_coord = BlockMath.get_first_block_in_chunk(position.getZ()) - offset;
      final int full_size = size * WorldConstants.chunk_size;
      int x;
      int y;
      int z;
      BlockPos new_position;
      BlockState blockstate;
      Block block;
      for(y = WorldConstants.bottom_level; y < WorldConstants.world_height; y++){
        for(x = 0; x < full_size; x++){
          for(z = 0; z < full_size; z++){
            new_position = new BlockPos(x_coord + x, y, z_coord + z);
            blockstate = world.getBlockState(new_position);
            block = blockstate.getBlock();
            consumer.accept(block);
          }
        }
      }
    }
  }
  */

}
