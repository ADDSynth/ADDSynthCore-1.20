package addsynth.core.gameplay.commands;

import addsynth.core.ADDSynthCore;
import addsynth.core.util.command.CommandUtil;
import addsynth.core.util.command.PermissionLevel;
import addsynth.core.util.math.block.BlockMath;
import addsynth.core.util.world.WorldUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.phys.Vec3;

public final class BlackoutCommand {

  private static final int MAX_BATS = 2_000;
  private static final int DEFAULT_BATS = 200;

  private static final int above_ground = 12;
  private static final int horizontal_radius = 19;
  private static final int vertical_radius = 1;

  public static final void register(CommandDispatcher<CommandSourceStack> dispatcher){
    dispatcher.register(
      Commands.literal(ADDSynthCore.MOD_ID).requires(
        (command_source) -> { return command_source.hasPermission(PermissionLevel.COMMANDS); }
      ).then(
        Commands.literal("blackout").executes(
          (command_context) -> { return blackout(command_context.getSource(), DEFAULT_BATS); }
        ).then(
          Commands.argument("size", IntegerArgumentType.integer(1, MAX_BATS)).executes(
            (command_context) -> { return blackout(command_context.getSource(), IntegerArgumentType.getInteger(command_context, "size")); }
          )
        )
      )
    );
  }

  @SuppressWarnings("resource")
  private static final int blackout(final CommandSourceStack command_source, final int number_of_bats) throws CommandSyntaxException {
    CommandUtil.check_argument("size", number_of_bats, 1, MAX_BATS);
    // get data
    final Vec3 position = command_source.getPosition();
    final ServerLevel world = command_source.getLevel();
    final int x = (int)Math.floor(position.x);
    final int z = (int)Math.floor(position.z);
    // get top-most block over player
    final BlockPos origin = new BlockPos(x, WorldUtil.getTopMostFreeSpace(world, x, z) + above_ground, z);
    // WorldUtil.getTopMostFreeSpace(world, x, z) + up_in_the_sky + (vertical_radius / 2),
    // spawn bats
    int count = 0;
    for(final BlockPos pos : BlockMath.getBlockPositionsAroundPillar(origin, horizontal_radius, vertical_radius)){
      if(WorldUtil.isAir(world, pos)){
        final Bat bat = new Bat(EntityType.BAT, world);
        bat.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        world.addFreshEntity(bat);
        count += 1;
        if(count >= number_of_bats){
          break;
        }
      }
    }
    // feeback
    final int final_count = count;
    command_source.sendSuccess(() -> Component.translatable(
      "commands.addsynthcore.blackout.success", command_source.getDisplayName().getString(), final_count
    ), true);
    return number_of_bats;
  }

}
