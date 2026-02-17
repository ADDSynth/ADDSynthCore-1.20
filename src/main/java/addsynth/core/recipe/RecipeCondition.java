package addsynth.core.recipe;

import java.util.function.Supplier;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

/** You can use this to create custom recipe conditions. Create a class file,
 *  and create instances of this as {@code public static final} fields. Then,
 *  pass in your {@code static} function that returns a boolean to use as a
 *  test to determine whether or not to load the recipe. You must register your
 *  recipe conditions in your Register class like this:
 *  <pre>
 *  event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper -> {
 *    CraftingHelper.register(MyRecipeConditions.recipe_condition.serializer);
 *  });
 *  </pre>
 */
public class RecipeCondition implements ICondition {

  private final ResourceLocation id;
  private final Supplier<Boolean> test;
  public final Serializer serializer;

  public RecipeCondition(final ResourceLocation id, final Supplier<Boolean> test){
    this.id = id;
    this.test = test;
    serializer = new Serializer(this);
  }

  @Override
  public ResourceLocation getID(){
    return id;
  }

  @Override
  public boolean test(final IContext context){
    return test.get();
  }

  public static final class Serializer implements IConditionSerializer<RecipeCondition> {

    private final RecipeCondition condition;

    public Serializer(final RecipeCondition condition){
      this.condition = condition;
    }

    @Override
    public final void write(JsonObject json, RecipeCondition value){
    }
    
    @Override
    public RecipeCondition read(JsonObject json){
      return condition;
    }
    
    @Override
    public ResourceLocation getID(){
      return condition.id;
    }

  }

}
