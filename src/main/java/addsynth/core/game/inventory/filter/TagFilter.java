package addsynth.core.game.inventory.filter;

import java.util.function.Predicate;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

/** This filter uses Item tags. Pass in any number of tags.
 *  You can use tags from {@link ItemTags} or {@link Items Tags.Items}
 *  or your own defined tags. Tags are automatically updated when
 *  resources are reloaded or when clients join a world.
 */
public final class TagFilter implements Predicate<ItemStack> {

  private final TagKey<Item>[] tag_filter;

  @SafeVarargs
  public TagFilter(final TagKey<Item> ... tags){
    this.tag_filter = tags;
  }

  @Override
  public final boolean test(final ItemStack stack){
    final Item item = stack.getItem();
    final ITagManager<Item> tag_manager = ForgeRegistries.ITEMS.tags();
    for(final TagKey<Item> item_tag : tag_filter){
      if(tag_manager.isKnownTagName(item_tag)){
        if(tag_manager.getTag(item_tag).contains(item)){
          return true;
        }
      }
    }
    return false;
  }

}
