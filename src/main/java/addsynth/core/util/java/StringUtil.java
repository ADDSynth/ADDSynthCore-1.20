package addsynth.core.util.java;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import addsynth.core.ADDSynthCore;
import addsynth.core.util.color.Color;
import addsynth.core.util.math.common.CommonMath;
import addsynth.core.util.math.common.RoundMode;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public final class StringUtil {

  /**
   * Generally I would only use this if the class already included this Utility class for something else,
   *  otherwise just put the code in-place of where you need it.
   * @param input_string
   */
  public static final boolean StringExists(final String input_string){
    if(input_string != null){
      if(input_string.trim().length() > 0){
        return true;
      }
    }
    return false;
  }

  /** Accepts any object. Objects will be converted to their String representations.
   *  Null values will cause "null" to be added.  */
  public static final String build(Object ... objects){
    final StringBuilder b = new StringBuilder();
    for(Object s : objects){
      b.append(s);
    }
    return b.toString();
  }

  /** <p>The Minecraft engine will usually handle new line characters '{@code \n}' in
   *  {@link Component Components}, but one place where it isn't handled correctly is in
   *  ItemStack Tooltips. Newline characters are handled inconsistantly. In cases such
   *  as this, you can use this function. This will split the text Component at the new
   *  line points into separate Components and add them to the List of Components.
   *  <p>Do not use style codes in the text strings as that is generally not supported
   *  anymore, but if you do, be aware that the new Components will not have the same style
   *  as the original, so you'll need to reapply style codes after each new line character.
   * @param components
   * @param text_component
   */
  public static final void addNewLineComponent(final List<Component> components, final Component text_component){
    String text = text_component.getString();
    int position = text.indexOf('\n');
    if(position == -1){
      components.add(text_component);
      return;
    }
    final Style style = text_component.getStyle();
    String first_string;
    do{
      first_string = text.substring(0, position);
      text = text.substring(position+1);
      components.add(Component.literal(first_string).withStyle(style));
      position = text.indexOf('\n');
    }
    while(position >= 0);
    // add last string
    components.add(Component.literal(text).withStyle(style));
  }

  public static final String toPercentageString(final double value){
    return CommonMath.toPercentage(value) + "%";
  }
  
  public static final String toPercentageString(final double value, final RoundMode mode){
    return (int)CommonMath.toPercentage(value, 0, mode) + "%";
  }
  
  public static final String toPercentageString(final double value, final int number_of_decimals, final RoundMode mode){
    return CommonMath.toPercentage(value, number_of_decimals, mode) + "%";
  }
  
  public static final String toPercentageString(final double top, final double bottom){
    return CommonMath.toPercentage(top, bottom) + "%";
  }
  
  public static final String toPercentageString(final double top, final double bottom, final RoundMode mode){
    return (int)CommonMath.toPercentage(top, bottom, 0, mode) + "%";
  }
  
  public static final String toPercentageString(final double top, final double bottom, final int number_of_decimals, final RoundMode mode){
    return CommonMath.toPercentage(top, bottom, number_of_decimals, mode) + "%";
  }

  // https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#toLowerCase-java.util.Locale-
  public static final String Capitalize(final String input_string){
    return Character.toUpperCase(input_string.charAt(0)) + input_string.substring(1);
  }

  @Deprecated
  public static final String print_array(final Object[] array){
    return Arrays.deepToString(array);
  }

  /** Returns the noun prefixed by 'an' or 'a' depending on if the first letter is a vowel. */
  public static final String singleNoun(final String noun){
    return singleNoun(noun, false);
  }

  /** Returns the noun prefixed by 'an' or 'a' depending on if the first letter is a vowel. */
  public static final String singleNoun(final String noun, final boolean capitalize){
    final char c = noun.charAt(0);
    if(c == 'a' || c == 'A' || c == 'e' || c == 'E' || c == 'i' || c == 'I' || c == 'o' || c == 'O' || c == 'u' || c == 'U'){
      return (capitalize ? "An " : "an") + noun;
    }
    return (capitalize ? "A " : "a ") + noun;
  }

  /** Prints the Types of your array, such as <code>Class, Object, Integer, Short, Long,
   *  Float, Double, Boolean, String, </code>or <code> Character</code>.
   * @param array
   */
  public static final String print_type_array(final Object[] array){
    if(array == null){ return "null"; }
    final StringBuilder output = new StringBuilder("[");
    int i;
    for(i = 0; i < array.length; i++){
      if(array[i] == null){
        output.append("null");
      }
      else{
        if(array[i] instanceof Class){
          output.append(((Class)array[i]).getSimpleName());
        }
        else{
          output.append(array[i].getClass().getSimpleName());
        }
      }
      if(i + 1 < array.length){
        output.append(", ");
      }
    }
    output.append("]");
    return output.toString();
  }

  public static final String print_minecraft_array(final Collection array){
    if(array == null){ return "null"; }
    return print_minecraft_array(array.toArray());
  }

  /** <p>This is used to better represent an array of Minecraft types such as Item, Block, and ItemStacks.
   *  <p>Note: If you need to print a standard array, use {@link Arrays#deepToString(Object[])}.
   * @param array
   */
  public static final String print_minecraft_array(final Object[] array){
    if(array == null){ return "null"; }
    final StringBuilder output = new StringBuilder("[");
    int i;
    for(i = 0; i < array.length; i++){
      if(array[i] == null){
        ADDSynthCore.log.error("Found null object in array argument for StringUtil.print_minecraft_array().", new NullPointerException());
        output.append("null");
      }
      else{
        if(array[i] instanceof Item){
          output.append(getName((Item)array[i]));
        }
        else{
          if(array[i] instanceof Block){
            output.append(getName((Block)array[i]));
          }
          else{
            if(array[i] instanceof ItemStack){
              output.append(((ItemStack)array[i]).toString());
            }
            else{
              ADDSynthCore.log.error("An object in the array is of the wrong type. StringUtil.print_minecraft_array() only recognizes arrays of Item, Block, or ItemStack objects.", new IllegalArgumentException());
              output.append(array[i].toString());
            }
          }
        }
      }
      if(i + 1 < array.length){
        output.append(", ");
      }
    }
    output.append("]");
    return output.toString();
  }

  /** Starting in Minecraft 1.14, the {@link Item} class has its own {@link Item#toString toString()}
   *  method. However, I still believe this to be superior.
   */
  public static final String getName(final Item item){
    if(item != null){
      final ResourceLocation registry_name = ForgeRegistries.ITEMS.getKey(item);
      if(registry_name == null){
        final String unlocalized_name = item.getDescriptionId();
        if(unlocalized_name.equals("item.null")){
          return item.getClass().getSimpleName();
        }
        return unlocalized_name;
      }
      return registry_name.toString();
    }
    return "null";
  }

  /** Starting in Minecraft 1.14, the {@link Block} class has its own {@link Block#toString toString()}
   *  method. However, I still believe this to be superior.
   */
  public static final String getName(final Block block){
    if(block != null){
      final ResourceLocation registry_name = ForgeRegistries.BLOCKS.getKey(block);
      if(registry_name == null){
        final String unlocalized_name = block.getDescriptionId();
        if(unlocalized_name.equals("tile.null")){
          return block.getClass().getSimpleName();
        }
        return unlocalized_name;
      }
      return registry_name.toString();
    }
    return "null";
  }

  public static final String getName(final EntityType entity){
    if(entity != null){
      final ResourceLocation registry_name = ForgeRegistries.ENTITY_TYPES.getKey(entity);
      if(registry_name == null){
        return entity.getDescriptionId();
      }
      return registry_name.toString();
    }
    return "null";
  }

  public static final String print(final Vec3i position){
    return build("( ", position.toShortString(), " )");
  }

  public static final String printPosition(final int x, final int y, final int z){
    return build("( ", x, ", ", y, ", ", z, " )");
  }

  public static final String print(final Recipe recipe){
    final String class_name = recipe.getClass().getSimpleName();
    final String id = recipe.getId().toString();
    // final String type = recipe.getType().getClass().getSimpleName();
    final String output = recipe.getResultItem(RegistryAccess.EMPTY).toString();
    return build(class_name, "(", id, ", Output: ", output, ")");
  }

  public static final String printColor(final int color){
    return "( "+Color.getRed(color)+" , "+Color.getGreen(color)+" , "+Color.getBlue(color)+" )";
  }

}
