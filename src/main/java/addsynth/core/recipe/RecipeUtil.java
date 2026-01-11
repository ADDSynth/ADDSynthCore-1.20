package addsynth.core.recipe;

import java.util.List;
import addsynth.core.game.inventory.InventoryUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

/** The Recipe Utility class is now only used for simple / generic recipe queries.
 *  For more specific control over recipes, use the {@link RecipeManager}. If you
 *  need to keep a list of your recipes, or filter slots based on your recipes,
 *  then you can use a {@link RecipeCollection}.
 *  @see Level#getRecipeManager()
 */
public final class RecipeUtil {

  public static final boolean isIngredient(final Recipe<Container> recipe, final Item item){
    for(final Ingredient ingredient : recipe.getIngredients()){
      for(final ItemStack stack : ingredient.getItems()){
        if(stack.getItem() == item){
          return true;
        }
      }
    }
    return false;
  }

  public static final boolean match(final Recipe<Container> recipe, final ItemStackHandler inventory, final Level world){
    return recipe.matches(InventoryUtil.toInventory(inventory), world);
  }

  public static final <C extends Container, R extends Recipe<C>> List<R> getRecipesofType(final RecipeType<R> type, final Level world){
    final RecipeManager recipe_manager = world.getRecipeManager();
    return recipe_manager.getAllRecipesFor(type);
  }

}
