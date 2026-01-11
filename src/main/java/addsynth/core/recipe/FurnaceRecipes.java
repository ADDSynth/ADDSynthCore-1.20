package addsynth.core.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

/** Use this to query Furnace recipes, or to get a filter that accepts
 *  only smelting recipe ingredients such as ores, sand, and raw meat.
 *  This is automatically kept up-to-date.
 */
public final class FurnaceRecipes extends RecipeCollection<SmeltingRecipe> {

  public static final FurnaceRecipes INSTANCE = new FurnaceRecipes();

  private FurnaceRecipes(){
    super(() -> RecipeType.SMELTING, 1);
  }

  public static final boolean isFurnaceIngredient(final ItemStack stack){
    for(final SmeltingRecipe recipe : INSTANCE.getRecipes()){
      for(final Ingredient ingredient : recipe.getIngredients()){
        if(ingredient.test(stack)){
          return true;
        }
      }
    }
    return false;
  }

  public static final int getSmeltingTime(final ItemStack stack){
    for(final SmeltingRecipe recipe : INSTANCE.getRecipes()){
      for(final Ingredient ingredient : recipe.getIngredients()){
        if(ingredient.test(stack)){
          return recipe.getCookingTime();
        }
      }
    }
    return 0;
  }

}
