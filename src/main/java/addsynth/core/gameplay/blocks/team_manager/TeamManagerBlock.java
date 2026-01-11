package addsynth.core.gameplay.blocks.team_manager;

import addsynth.core.gameplay.client.GuiProvider;
import addsynth.core.util.command.PermissionLevel;
import addsynth.core.util.constants.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public final class TeamManagerBlock extends Block {

  private static final Component you_dont_have_permission = Component.translatable("gui.addsynthcore.team_manager.message.you_do_not_have_permission", PermissionLevel.COMMANDS);

  public TeamManagerBlock(){
    super(Block.Properties.of().mapColor(MapColor.METAL).sound(SoundType.STONE).strength(0.5f, Constants.block_resistance));
  }

  @Override
  @SuppressWarnings({ "deprecation" })
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
    if(world.isClientSide){
      if(player.hasPermissions(PermissionLevel.COMMANDS)){
        GuiProvider.openTeamManagerGui();
      }
      else{
        player.displayClientMessage(you_dont_have_permission, true);
      }
    }
    return InteractionResult.SUCCESS;
  }

}
