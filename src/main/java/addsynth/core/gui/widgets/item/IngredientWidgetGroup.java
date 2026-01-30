package addsynth.core.gui.widgets.item;

import addsynth.core.util.time.TickHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.crafting.Ingredient;

public final class IngredientWidgetGroup {

  private static final TickHandler tick_handler = new TickHandler();
  private final IngredientWidget[] ingredient;
  private final int max_length;
  private int length;
  private int i;

  public IngredientWidgetGroup(int max_number_of_ingredients){
    max_length = max_number_of_ingredients;
    ingredient = new IngredientWidget[max_length];
    for(i = 0; i < max_length; i++){
      ingredient[i] = new IngredientWidget();
    }
  }

  public IngredientWidgetGroup(int max_number_of_ingredients, final Ingredient ... recipe){
    this(max_number_of_ingredients);
    setRecipe(recipe);
  }

  public final void setRecipe(final Ingredient ... recipe){
    // recipes that are set lower than max_length will not set the extra Ingredients so they
    // will remain as they were, but they won't be rendered or updated either, so it's safe.
    // if(recipe.length > max_length){
    //   ADDSynthCore.log.error("Recipe input for "+IngredientWidgetGroup.class.getSimpleName()+" has a length greater than the maximum allowed ("+max_length+").", new IndexOutOfBoundsException());
    // }
    length = Math.min(recipe.length, max_length);
    for(i = 0; i < length; i++){
      ingredient[i].setIngredient(recipe[i]);
    }
    tick_handler.reset();
  }

  public final void tick(){
    if(tick_handler.tick()){
      for(i = 0; i < length; i++){
        ingredient[i].update();
      }
    }
  }

  public final int getLength(){
    return length;
  }

  public final void drawIngredient(final GuiGraphics graphics, int index, int x, int y){
    ingredient[index].draw(graphics, x, y);
  }
  
  public final void draw(final GuiGraphics graphics, int guiLeft, int[] x, int guiTop, int[] y){
    for(i = 0; i < length; i++){
      ingredient[i].draw(graphics, guiLeft + x[i], guiTop + y[i]);
    }
  }

  public final void drawTooltip(GuiGraphics graphics, Font font, Screen screen, int index, int x, int y, int mouse_x, int mouse_y){
    ingredient[index].drawTooltip(graphics, font, screen, x, y, mouse_x, mouse_y);
  }

  // public final void drawTooltip(PoseStack matrix, Screen screen, int[] x, int[] y, mouse_x, mouse_y){
  // }
  
  public final void drawTooltips(GuiGraphics graphics, Font font, Screen screen, int guiLeft, int[] x, int guiTop, int y, int mouse_x, int mouse_y){
    for(i = 0; i < length; i++){
      ingredient[i].drawTooltip(graphics, font, screen, guiLeft + x[i], guiTop + y, mouse_x, mouse_y);
    }
  }

  public final void drawTooltips(GuiGraphics graphics, Font font, Screen screen, int guiLeft, int[] x, int guiTop, int[] y, int mouse_x, int mouse_y){
    for(i = 0; i < length; i++){
      ingredient[i].drawTooltip(graphics, font, screen, guiLeft + x[i], guiTop + y[i], mouse_x, mouse_y);
    }
  }

}
