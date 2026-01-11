package addsynth.core.game.inventory.filter;

import java.util.function.Predicate;
import addsynth.core.ADDSynthCore;
import addsynth.core.recipe.RecipeCollection;
import addsynth.core.util.java.ArrayUtil;
import addsynth.core.util.java.StringUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

/** This filter is used if you want to set the slots to accept 1 recipe at a time.
 *  It is likely you'll want each machine of the same type to be set to different
 *  recipes, therefore you should make this an instance field, NOT STATIC.
 *  You can get a list of recipes by calling either {@link RecipeCollection#getRecipes()}
 *  or {@link RecipeManager#getAllRecipesFor(RecipeType)}.
 */
public final class RecipeFilter {

  private final int size;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private int ingredients_length = 0;
  
  public RecipeFilter(final int recipe_size){
    size = recipe_size;
  }
  
  public final void reset(){
    ingredients = NonNullList.create();
    ingredients_length = 0;
  }
  
  public final void set(final Recipe<?> recipe){
    if(recipe != null){
      ingredients = recipe.getIngredients();
      ingredients_length = ingredients.size();
      if(ingredients_length > size){
        ADDSynthCore.log.warn(
          StringUtil.build(
            "Cannot set the ",
            RecipeFilter.class.getSimpleName(),
            " to match ",
            StringUtil.print(recipe),
            " because the recipe has too many ingredients (",
            ingredients_length,
            ")."
          )
        );
      }
    }
    else{
      ADDSynthCore.log.error("Tried to set "+RecipeFilter.class.getSimpleName()+" to a null recipe.", new NullPointerException());
      reset();
    }
  }
  
  /** You can use this to get a Predicate filter of a specific slot.
   *  It will work correctly as the filter gets updated.
   * @param slot
   * @return
   */
  public final Predicate<ItemStack> getFilter(final int slot){
    return (ItemStack stack) -> test(slot, stack);
  }
  
  /** Since the internal ingredient list changes on every recipe change,
   *  we can't return a Predicate of a single Ingredient, so instead you
   *  can call {@link #getFilter(int)} to get the Predicate filter.
   * @param index
   * @param stack
   * @return
   */
  public final boolean test(final int index, final ItemStack stack){
    if(ArrayUtil.isInsideBounds(index, ingredients_length)){
      return ingredients.get(index).test(stack);
    }
    return false;
  }

  public final Ingredient getIngredient(final int index){
    return ingredients.get(index);
  }

  public final Ingredient[] getIngredients(){
    return ingredients.toArray(new Ingredient[ingredients_length]);
  }

}
