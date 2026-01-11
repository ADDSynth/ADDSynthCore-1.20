package addsynth.core.recipe.shapeless;

import addsynth.core.util.java.StringUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import it.unimi.dsi.fastutil.ints.IntList;

/** REPLICA: Nearly all of this code was copied from {@link net.minecraft.world.item.crafting.ShapelessRecipe} */
public abstract class AbstractRecipe implements Recipe<Container> {

  private final ResourceLocation id;
  private final String group;
  private final ItemStack result;
  protected final NonNullList<Ingredient> ingredients;

  public AbstractRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> input){
    this.id = id;
    this.group = group;
    this.result = output;
    this.ingredients = input;
    // this.isSimple = input.stream().allMatch(Ingredient::isSimple);
  }

  @Override
  public String getGroup(){
    return group;
  }

  @Override
  public NonNullList<Ingredient> getIngredients(){
    return ingredients; // what are the consequences of returning the EXACT list object? it can be manipulated from an external source.
  }

  /** Gets all valid ItemStacks for each Ingredient in this recipe. */
  public final ItemStack[][] getItemStackIngredients(){
    if(ingredients != null){
      final int number_of_ingredients = ingredients.size();
      final ItemStack[][] stacks = new ItemStack[number_of_ingredients][];
      int i;
      for(i = 0; i < number_of_ingredients; i++){
        stacks[i] = ingredients.get(i).getItems();
      }
      return stacks;
    }
    return new ItemStack[0][];
  }

  @Override
  public boolean matches(Container inv, Level world){
    if(ingredients == null){
      return false;
    }
    final StackedContents recipeitemhelper = new StackedContents();
    int items_in_inventory = 0;
    int j;

    for(j = 0; j < inv.getContainerSize(); j++){
      final ItemStack itemstack = inv.getItem(j);
      if(itemstack.isEmpty() == false){
        items_in_inventory += 1;
        recipeitemhelper.accountStack(itemstack, 1);
      }
    }

    return items_in_inventory == this.ingredients.size() && recipeitemhelper.canCraft(this, (IntList)null);
  }

  @Override
  public boolean canCraftInDimensions(int width, int height){
    return width * height >= this.ingredients.size();
  }

  /** @return A copy of the output ItemStack. */
  @Override
  public ItemStack assemble(Container inv, RegistryAccess registry){
    return result.copy();
  }

  /** @return EXACT object. DO NOT EDIT! */
  public ItemStack getResultItem(){
    return result;
  }

  /** @return EXACT object. DO NOT EDIT! */
  @Override
  public ItemStack getResultItem(RegistryAccess registry){
    return result;
  }

  @Override
  public ResourceLocation getId(){
    return id;
  }

  @Override
  public String toString(){
    final String name = getClass().getSimpleName();
    final int number_of_ingredients = ingredients != null ? ingredients.size() : 0;
    if(number_of_ingredients != 0){
      return StringUtil.build(name, "(Ingredients: ", number_of_ingredients, ", Output: ", result.toString(), ')');
    }
    return StringUtil.build(name, "(Output: ", result.toString(), ')');
  }

}
