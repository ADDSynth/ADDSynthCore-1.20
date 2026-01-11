package addsynth.core.compat;

import addsynth.core.ADDSynthCore;
import addsynth.core.util.java.StringUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;

public final class JeiUtil {

  public static final void addInputSlotIngredients(final Recipe<?> recipe, final IRecipeSlotBuilder[] slots){
    addInputSlotIngredients(recipe, slots, recipe.getIngredients());
  }

  public static final void addInputSlotIngredients(final Recipe recipe, final IRecipeSlotBuilder[] slots, final NonNullList<Ingredient> ingredients){
    int length = ingredients.size();
    if(length > slots.length){
      ADDSynthCore.log.error(JeiUtil.class.getSimpleName()+" -> Failed to add enough slots to display the ingredients for the recipe: "+StringUtil.print(recipe)+".");
      length = slots.length;
    }
    int i;
    for(i = 0; i < length; i++){
      slots[i].addIngredients(ingredients.get(i));
    }
  }

}
