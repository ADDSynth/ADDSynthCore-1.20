package addsynth.core.game.registry;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

/** Although most of the time, you would probably do {@code new Item(new Item.Properties())}
 *  when registering simple Items, for items that are a bit more complex (but not too
 *  complex that it'd be better to have a dedicated class) you may not want their
 *  new instance suppliers in your Register class, so to keep the item definition in one
 *  place, such as your {@code ModItems} or {@code ItemsReference} class, you can use this.
 */
public class ItemRegistryHolder {

  private final ResourceLocation name;
  private final RegistryObject<Item> item;
  private final Supplier<Item> constructor;

  public ItemRegistryHolder(final ResourceLocation name, final Supplier<Item> constructor){
    this.name = name;
    item = RegistryObject.create(name, ForgeRegistries.ITEMS);
    this.constructor = constructor;
  }

  public final Item get(){
    return item.get();
  }

  public final void register(final IForgeRegistry<Item> registry){
    registry.register(name, constructor.get());
  }

}
