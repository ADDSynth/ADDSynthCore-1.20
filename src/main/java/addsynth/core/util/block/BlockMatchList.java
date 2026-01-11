package addsynth.core.util.block;

import java.util.HashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

/** This holds a list of ResourceLocation IDs for Blocks that can be used by
 *  something to test whether the Block is on the list. For modders, you only
 *  need to add your block once. Adding more than once will not affect anything.
 */
public final class BlockMatchList {

  private final HashSet<ResourceLocation> set = new HashSet<ResourceLocation>();

  public BlockMatchList(){}

  public BlockMatchList(ResourceLocation ... locations){
    for(ResourceLocation location : locations){
      set.add(location);
    }
  }

  @SuppressWarnings("deprecation")
  public final void add(Block block){
    set.add(block.builtInRegistryHolder().key().location());
    // set.add(ForgeRegistries.BLOCKS.getKey(block));
  }
  
  public final void add(ResourceLocation location){
    set.add(location);
  }
  
  public final void add(RegistryObject<Block> registry_holder){
    set.add(registry_holder.getId());
  }

  public final boolean test(BlockState blockstate){
    return test(blockstate.getBlock());
  }

  @SuppressWarnings("deprecation")
  public final boolean test(Block block){
    return set.contains(block.builtInRegistryHolder().key().location());
    // return set.contains(ForgeRegistries.BLOCKS.getKey(block));
  }

}
