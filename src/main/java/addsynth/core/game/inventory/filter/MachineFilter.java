package addsynth.core.game.inventory.filter;

import java.util.Collection;
import java.util.function.Predicate;
import addsynth.core.ADDSynthCore;
import addsynth.core.recipe.RecipeCollection;
import addsynth.core.util.java.StringUtil;
import addsynth.core.util.java.list.ItemStackList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

/** This is primarily used in {@link RecipeCollection}s. The RecipeCollection
 *  reads all recipes belonging to that {@link RecipeType}, and only allows
 *  an item in the slot if it is in that position in one of the recipes.
 *  For example, given the recipes (Book, Ruby) and (Book, Topaz), only Book is
 *  allowed in the first slot, and only Ruby and Topaz are allowed in the second.
 */
public final class MachineFilter {

  private int i;
  private final int length;
  private ItemStackList[] ingredient_list;

  public MachineFilter(final int slots){
    this.length = slots;
    this.ingredient_list = new ItemStackList[slots];
    for(i = 0; i < slots; i++){
      ingredient_list[i] = new ItemStackList(100);
    }
  }

  public final void clear(){
    for(final ItemStackList item_list : ingredient_list){
      item_list.clear();
    }
  }

  public final <T extends Recipe<?>> void set(final Collection<T> recipes){
    // clear arrays
    for(final ItemStackList item_list : ingredient_list){
      item_list.clear();
    }
    // loop through recipes
    int recipe_size;
    NonNullList<Ingredient> recipe_ingredients;
    for(final Recipe<?> recipe : recipes){
      // get ingredients
      recipe_ingredients = recipe.getIngredients();
      recipe_size = recipe_ingredients.size();
      // check recipe size fits
      if(recipe_size <= length){
        // add each ItemStack to their respective ArrayList slots
        for(i = 0; i < recipe_size; i++){
          for(final ItemStack stack : recipe_ingredients.get(i).getItems()){
            ingredient_list[i].add(stack);
          }
        }
      }
      else{
        // ERROR
        ADDSynthCore.log.error("The recipe "+StringUtil.print(recipe)+" is too big to fit in this filter. This filter is "+length+" slots wide, while the recipe has "+recipe_size+" ingredients!");
      }
    }
  }

  public final Predicate<ItemStack> get(final int slot){
    return ingredient_list[slot]::test;
  }

}
