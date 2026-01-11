package addsynth.core.game.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public final class RegistryUtil {

  /** Used to register a {@link BlockItem}. For a more complex BlockItem, it
   *  might be best to have a dedicated class object and manually register it.
   * @param registry
   * @param block
   */
  public static final void register(final IForgeRegistry<Item> registry, final RegistryObject<Block> block){
    registry.register(block.getId(), new BlockItem(block.get(), new Item.Properties()));
  }

  public static final <T extends Recipe<?>> void registerRecipeType(final IForgeRegistry<RecipeType<?>> registry, final ResourceLocation name){
    registry.register(name,
      new RecipeType<T>() {
        @Override
        public final String toString(){
          return name.getPath();
        }
      }
    );
  }

}
