package addsynth.core.recipe.shapeless;

import javax.annotation.Nullable;
import addsynth.core.util.java.JavaUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

/** REPLICA: Code here is copied from {@link net.minecraft.world.item.crafting.ShapelessRecipe.Serializer ShapelessRecipe.Serializer} */
public class ShapelessRecipeSerializer<T extends AbstractRecipe> implements RecipeSerializer<T> {

  private final int max_size;
  private final Class<T> recipe_type;
  
  public ShapelessRecipeSerializer(final Class<T> recipe_type, final int max_size){
    this.recipe_type = recipe_type;
    this.max_size = max_size;
  }

  private static final NonNullList<Ingredient> readIngredients(JsonArray json_array){
    NonNullList<Ingredient> nonnulllist = NonNullList.create();
    for(int i = 0; i < json_array.size(); ++i) {
      Ingredient ingredient = Ingredient.fromJson(json_array.get(i));
      if(!ingredient.isEmpty()){
        nonnulllist.add(ingredient);
      }
    }
    return nonnulllist;
  }

  // Start Here
  @Override
  public T fromJson(ResourceLocation recipeId, JsonObject json){
    final String group = GsonHelper.getAsString(json, "group", "");
    NonNullList<Ingredient> nonnulllist = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
    if(nonnulllist.isEmpty()){
      throw new JsonParseException("No ingredients for "+recipe_type.getSimpleName()+" recipe");
    }
    if(nonnulllist.size() > max_size){
      throw new JsonParseException("Too many ingredients for "+recipe_type.getSimpleName()+" recipe. There can only be a max of "+max_size+" ingredients.");
    }
    ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
    return JavaUtils.InvokeConstructor(recipe_type, recipeId, group, itemstack, nonnulllist);
  }

  @Override
  @Nullable
  public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer){
    final String group = buffer.readUtf(32767);
    final int i = buffer.readVarInt();
    NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

    for(int j = 0; j < nonnulllist.size(); ++j) {
       nonnulllist.set(j, Ingredient.fromNetwork(buffer));
    }

    ItemStack itemstack = buffer.readItem();
    return JavaUtils.InvokeConstructor(recipe_type, recipeId, group, itemstack, nonnulllist);
  }

  @Override
  public void toNetwork(FriendlyByteBuf buffer, T recipe){
    buffer.writeUtf(recipe.getGroup());
    buffer.writeVarInt(recipe.getIngredients().size());

    for(Ingredient ingredient : recipe.getIngredients()){
      ingredient.toNetwork(buffer);
    }

    buffer.writeItemStack(recipe.getResultItem(), false);
  }

}
