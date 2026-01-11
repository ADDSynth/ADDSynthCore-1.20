package addsynth.core.util.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class CommandUtil {

  // private static final SimpleCommandExceptionType ARGUMENT_OUT_OF_RANGE =
  //   new SimpleCommandExceptionType(new StringTextComponent("Too many items specified. Only a maximum of "+MAX_ITEMS+" are allowed."));

  public static final boolean isPlayer(final CommandSourceStack command_source) throws CommandSyntaxException {
    if(command_source.getEntity() instanceof ServerPlayer){
      return true;
    }
    // translation key copied from CommandSource.REQUIRES_PLAYER_EXCEPTION_TYPE
    // command_source.sendFeedback(new TranslationTextComponent("permissions.requires.player"), false);
    throw CommandSourceStack.ERROR_NOT_PLAYER.create();
    // return false;
  }

  public static final void check_argument(final String argument, final double value, final double min, final double max) throws CommandSyntaxException {
    if(value > max || value < min){
      throw new SimpleCommandExceptionType(Component.translatable("commands.addsynthcore.argument_fail", argument, min, max)).create();
    }
  }

}
