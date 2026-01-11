package addsynth.core.util.world;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import addsynth.core.util.server.ServerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public final class WorldUtil {

  /** Ever since Minecraft 1.13, there are now two other types of Air, Cave Air and Void Air.
   *  There may also be other Air blocks that are defined by mods.
   * @param world
   * @param position
   */
  @Deprecated
  public static final boolean isAir(final Level world, final BlockPos position){
    return world.getBlockState(position).isAir();
  }

  /** Assign a function to be called when a {@link LevelTickEvent} is posted on the Forge Event Bus
   *  by using {@link EventBusSubscriber} and {@link SubscribeEvent}. However, logically, you only
   *  want levels to be ticked on the server side, so this is a helper method to do just that.
   * @param event
   * @param code
   */
  public static final void tickLevel(final LevelTickEvent event, Consumer<ServerLevel> code){
    if(event.side == LogicalSide.SERVER){
      if(event.phase == Phase.START){
        code.accept((ServerLevel)event.level);
      }
    }
  }

  /** Assign a function to be called when a {@link LevelTickEvent} is posted on the Forge Event Bus
   *  by using {@link EventBusSubscriber} and {@link SubscribeEvent}. However, logically, you only
   *  want levels to be ticked on the server side, so this is a helper method to do just that.
   * @param event
   * @param code
   */
  @SuppressWarnings("resource")
  public static final void tickLevel(final LevelTickEvent event, BiConsumer<MinecraftServer, ServerLevel> code){
    if(event.side == LogicalSide.SERVER){
      if(event.phase == Phase.START){
        final ServerLevel level = (ServerLevel)event.level;
        code.accept(level.getServer(), level);
      }
    }
  }

  public static final int getTopMostFreeSpace(final Level world, final BlockPos position){
    return world.getChunk(position).getHeight(Heightmap.Types.WORLD_SURFACE, position.getX(), position.getZ()) + 1;
  }
  
  public static final int getTopMostFreeSpace(final Level world, final int x_pos, final int z_pos){
    return world.getChunk(x_pos >> 4, z_pos >> 4).getHeight(Heightmap.Types.WORLD_SURFACE, x_pos, z_pos) + 1;
  }

  public static final boolean validYLevel(final int y_level){
    return y_level >= WorldConstants.bottom_level && y_level < WorldConstants.world_height;
  }

  public static final void delete_block(final Level world, final BlockPos position){
    world.setBlock(position, Blocks.AIR.defaultBlockState(), 3);
  }

  /** Spawns an ItemStack using the vanilla method. */
  public static final void spawnItemStack(LevelAccessor world, BlockPos pos, ItemStack stack){
    Block.popResource((Level)world, pos, stack); // TODO: definitely update these spawnItemStack methods, at least for the 1.17 version.
  }

  /** Spawns an ItemStack using the vanilla method. */
  public static final void spawnItemStack(Level world, BlockPos pos, ItemStack stack){
    Block.popResource(world, pos, stack);
  }

  /** Spawns an ItemStack using the vanilla method. */
  public static final void spawnItemStack(LevelAccessor world, double x, double y, double z, ItemStack stack){
    Block.popResource((Level)world, BlockPos.containing(x, y, z), stack);
  }

  /** Spawns an ItemStack using the vanilla method. */
  public static final void spawnItemStack(Level world, double x, double y, double z, ItemStack stack){
    Block.popResource(world, BlockPos.containing(x, y, z), stack);
  }

  /** Spawns an ItemStack. The final boolean parameter determines if you want to use
   *  vanilla's method of randomly jumping items when they are spawned.
   */
  public static final void spawnItemStack(LevelAccessor world, BlockPos pos, ItemStack stack, boolean vanilla_jump_randomly){
    spawnItemStack((Level)world, pos.getX(), pos.getY(), pos.getZ(), stack, vanilla_jump_randomly);
  }

  /** Spawns an ItemStack. The final boolean parameter determines if you want to use
   *  vanilla's method of randomly jumping items when they are spawned.
   */
  public static final void spawnItemStack(Level world, BlockPos pos, ItemStack stack, boolean vanilla_jump_randomly){
    spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack, vanilla_jump_randomly);
  }

  /** Spawns an ItemStack. The final boolean parameter determines if you want to use
   *  vanilla's method of randomly jumping items when they are spawned.
   */
  public static final void spawnItemStack(LevelAccessor world, double x, double y, double z, ItemStack stack, boolean vanilla_jump_randomly){
    spawnItemStack((Level)world, x, y, z, stack, vanilla_jump_randomly);
  }

  /** Spawns an ItemStack. The final boolean parameter determines if you want to use
   *  vanilla's method of randomly jumping items when they are spawned.
   */
  public static final void spawnItemStack(Level world, double x, double y, double z, ItemStack stack, boolean vanilla_jump_randomly){
    if(vanilla_jump_randomly){
      Block.popResource(world, BlockPos.containing(x, y, z), stack);
    }
    else{
      if(stack.isEmpty() == false){
        final ItemEntity entity = new ItemEntity(world, x + 0.5, y, z + 0.5, stack);
        entity.setDeltaMovement(0.0, 0.0, 0.0);
        world.addFreshEntity(entity);
      }
    }
  }

  /** I don't get it. The /time command sets the time for all worlds, but you can also
   *  set the world's time individually.
   *  DO NOT set time as a negative value!
   * @see addsynth.core.util.time.WorldTime
   * @see net.minecraft.server.commands.TimeCommand
   */
  @Deprecated
  public static final void set_time(final Level world, final int world_time){
    set_time(world, (long)world_time);
  }
  
  /** I don't get it. The /time command sets the time for all worlds, but you can also
   *  set the world's time individually.
   *  DO NOT set time as a negative value!
   * @see addsynth.core.util.time.WorldTime
   * @see net.minecraft.server.commands.TimeCommand
   */
  @Deprecated
  public static final void set_time(final MinecraftServer server, final int world_time){
    set_time(server, (long)world_time);
  }
  
  /** I don't get it. The /time command sets the time for all worlds, but you can also
   *  set the world's time individually.
   *  DO NOT set time as a negative value!
   * @see addsynth.core.util.time.WorldTime
   * @see net.minecraft.server.commands.TimeCommand
   */
  public static final void set_time(final Level world, final long world_time){
    @SuppressWarnings("resource")
    final MinecraftServer server = world.getServer();
    if(server != null){
      for(ServerLevel w : server.getAllLevels()){
        w.setDayTime(world_time >= 0 ? world_time : 0);
      }
    }
    // else{
      // they let me do this? What's going to happen?
      // world.setDayTime(world_time);
    // }
  }

  /** I don't get it. The /time command sets the time for all worlds, but you can also
   *  set the world's time individually.
   *  DO NOT set time as a negative value!
   * @see addsynth.core.util.time.WorldTime
   * @see net.minecraft.server.commands.TimeCommand
   */
  public static final void set_time(final MinecraftServer server, final long world_time){
    for(ServerLevel world : server.getAllLevels()){
      world.setDayTime(world_time >= 0 ? world_time : 0);
    }
  }

  /** @deprecated Look at what this function does. Bypass this and use the vanilla method instead. */
  @Deprecated
  public static final BlockPos getSpawnPosition(final Level world){
    final LevelData world_data = world.getLevelData();
    return new BlockPos(world_data.getXSpawn(), world_data.getYSpawn(), world_data.getZSpawn());
  }

  @SuppressWarnings("resource")
  @Deprecated
  public static final BlockPos getSpawnPosition(){
    final MinecraftServer server = ServerUtils.getServer();
    if(server != null){
      final Level world = server.getLevel(Level.OVERWORLD);
      if(world != null){
        return getSpawnPosition(world);
      }
    }
    return BlockPos.ZERO;
  }

  
  public static final void spawnLightningBolt(final Level world, final BlockPos position){
    final LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
    if(lightningboltentity != null){
      lightningboltentity.moveTo(Vec3.atBottomCenterOf(position));
      world.addFreshEntity(lightningboltentity);
    }
  }

}
