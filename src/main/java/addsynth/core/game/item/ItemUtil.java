package addsynth.core.game.item;

import java.util.function.Supplier;
import addsynth.core.game.item.constants.ArmorMaterial;
import addsynth.core.game.item.constants.EquipmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemUtil {

  public static final boolean itemStackExists(final ItemStack stack){
    if(stack == null){   return false; }
    if(stack.isEmpty()){ return false; }
    return true;
  }

  public static final boolean isItemStackEmpty(final ItemStack stack){
    return stack == null ? true : stack.isEmpty();
  }

  public static final void saveItemStack(final CompoundTag nbt, final ItemStack stack, final String key){
    if(stack != null){
      final CompoundTag item = new CompoundTag();
      stack.save(item);
      nbt.put(key, item);
    }
  }

  /** Loads ItemStack if the entry for it exists. Returns an Empty ItemStack if it does not. */
  public static final ItemStack loadItemStack(final CompoundTag nbt, final String key){
    return ItemStack.of(nbt.getCompound(key));
  }

  /** Loads a ResourceLocation and checks to see if it exists in the registry, if it does
   *  then it returns a new ItemStack. If not, then this returns an empty ItemStack. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag){
    final String item = nbt.getString(tag);
    if(!item.equals("")){
      return getItem(item);
    }
    return ItemStack.EMPTY;
  }
  
  /** Loads a ResourceLocation and checks to see if it exists in the registry, if it does
   *  then it returns a new ItemStack. If not, then this returns your supplied fallback Item. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final ItemStack defaultItem){
    final String item = nbt.getString(tag);
    if(!item.equals("")){
      final ItemStack stack = getItem(item);
      if(!stack.isEmpty()){
        return stack;
      }
    }
    return defaultItem;
  }
  
  /** Loads a ResourceLocation and checks to see if it exists in the registry, if it does
   *  then it returns a new ItemStack. If not, then this returns your supplied fallback Item. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final ResourceLocation defaultItem){
    final String item = nbt.getString(tag);
    if(!item.equals("")){
      final ItemStack stack = getItem(item);
      if(!stack.isEmpty()){
        return stack;
      }
    }
    return getItem(defaultItem);
  }
  
  /** Loads a ResourceLocation and checks to see if it exists in the registry, if it does
   *  then it returns a new ItemStack. If not, then this returns your supplied fallback Item. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final Item defaultItem){
    final String item = nbt.getString(tag);
    if(!item.equals("")){
      final ItemStack stack = getItem(item);
      if(!stack.isEmpty()){
        return stack;
      }
    }
    return new ItemStack(defaultItem);
  }
  
  /** Loads a ResourceLocation and checks to see if it exists in the registry, if it does then it
   *  returns a new ItemStack. You can supply a {@link RegistryObject RegistryObject&lt;Item&gt;}
   *  as a fallback if loading fails. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final Supplier<Item> defaultItemSupplier){
    final String item = nbt.getString(tag);
    if(!item.equals("")){
      final ItemStack stack = getItem(item);
      if(!stack.isEmpty()){
        return stack;
      }
    }
    return new ItemStack(defaultItemSupplier.get());
  }

  /** Loads a ResourceLocation using the legacy tag first. Returns an ItemStack if it exists. If not, then
   *  we load the current tag. And if that doesn't exist, then an empty ItemStack is returned. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final String legacyTag){
    final ItemStack legacy = loadItem(nbt, legacyTag);
    if(!legacy.isEmpty()){
      return legacy;
    }
    return loadItem(nbt, tag);
  }
  
  /** Loads a ResourceLocation using the legacy tag first. Returns an ItemStack if it exists. If not, then
   *  we load the current tag. And if that doesn't exist, then your supplied default Item is returned. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final String legacyTag, final ItemStack defaultItem){
    final ItemStack legacy = loadItem(nbt, legacyTag);
    if(!legacy.isEmpty()){
      return legacy;
    }
    return loadItem(nbt, tag, defaultItem);
  }
  
  /** Loads a ResourceLocation using the legacy tag first. Returns an ItemStack if it exists. If not, then
   *  we load the current tag. And if that doesn't exist, then your supplied default Item is returned. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final String legacyTag, final Item defaultItem){
    final ItemStack legacy = loadItem(nbt, legacyTag);
    if(!legacy.isEmpty()){
      return legacy;
    }
    return loadItem(nbt, tag, defaultItem);
  }
  
  /** Loads a ResourceLocation using the legacy tag first. Returns an ItemStack if it exists. If not, then
   *  we load the current tag. And if that doesn't exist, then your supplied default Item is returned. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final String legacyTag, final ResourceLocation defaultItem){
    final ItemStack legacy = loadItem(nbt, legacyTag);
    if(!legacy.isEmpty()){
      return legacy;
    }
    return loadItem(nbt, tag, defaultItem);
  }
  
  /** Loads a ResourceLocation using the legacy tag first. Returns an ItemStack if it exists.
   *  If not, then we load the current tag. If it still doesn't exist, then your supplied
   *  {@link RegistryObject RegistryObject&lt;Item&gt;} is returned instead. */
  public static final ItemStack loadItem(final CompoundTag nbt, final String tag, final String legacyTag, final Supplier<Item> defaultItemSupplier){
    final ItemStack legacy = loadItem(nbt, legacyTag);
    if(!legacy.isEmpty()){
      return legacy;
    }
    return loadItem(nbt, tag, defaultItemSupplier);
  }

  /** Turns the supplied String to a ResourceLocation and gets an Item from the registry
   *  if it exists, otherwise an empty ItemStack is returned. */
  public static final ItemStack getItem(final String resource_location){
    return new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(resource_location)));
  }

  /** Gets an Item from the registry as an ItemStack. Returns empty if it doesn't exist. */
  public static final ItemStack getItem(final ResourceLocation location){
    return new ItemStack(ForgeRegistries.ITEMS.getValue(location));
  }

  /** If you only need to save the Item and not the full ItemStack data, then you can use
   *  this. It saves the Item's ID as a String. But keep in mind that the Item may not
   *  exist in the registry after loading, so we may return an empty ItemStack instead. */
  @SuppressWarnings("null")
  public static final void saveItem(final CompoundTag nbt, final String tag, final ItemStack stack){
    nbt.putString(tag, ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
  }

  /** If you only need to save the Item and not the full ItemStack data, then you can use
   *  this. It saves the Item's ID as a String. But keep in mind that the Item may not
   *  exist in the registry after loading, so we may return an empty ItemStack instead. */
  @SuppressWarnings("null")
  public static final void saveItem(final CompoundTag nbt, final String tag, final Item item){
    nbt.putString(tag, ForgeRegistries.ITEMS.getKey(item).toString());
  }

  public static final Item[] toItemArray(final ItemStack[] input){
    int i;
    int length = input.length;
    final Item[] items = new Item[length];
    for(i = 0; i < length; i++){
      items[i] = input[i].getItem();
    }
    return items;
  }

  public static final Item get_armor(final ArmorMaterial armor_material, final EquipmentType equipment_type){
    Item armor = null;
    switch(armor_material){
    case LEATHER:
      switch(equipment_type){
      case HELMET:     armor = Items.LEATHER_HELMET; break;
      case CHESTPLATE: armor = Items.LEATHER_CHESTPLATE; break;
      case LEGGINGS:   armor = Items.LEATHER_LEGGINGS; break;
      case BOOTS:      armor = Items.LEATHER_BOOTS; break;
      }
      break;
    case CHAINMAIL:
      switch(equipment_type){
      case HELMET:     armor = Items.CHAINMAIL_HELMET; break;
      case CHESTPLATE: armor = Items.CHAINMAIL_CHESTPLATE; break;
      case LEGGINGS:   armor = Items.CHAINMAIL_LEGGINGS; break;
      case BOOTS:      armor = Items.CHAINMAIL_BOOTS; break;
      }
      break;
    case IRON:
      switch(equipment_type){
      case HELMET:     armor = Items.IRON_HELMET; break;
      case CHESTPLATE: armor = Items.IRON_CHESTPLATE; break;
      case LEGGINGS:   armor = Items.IRON_LEGGINGS; break;
      case BOOTS:      armor = Items.IRON_BOOTS; break;
      }
      break;
    case GOLD:
      switch(equipment_type){
      case HELMET:     armor = Items.GOLDEN_HELMET; break;
      case CHESTPLATE: armor = Items.GOLDEN_CHESTPLATE; break;
      case LEGGINGS:   armor = Items.GOLDEN_LEGGINGS; break;
      case BOOTS:      armor = Items.GOLDEN_BOOTS; break;
      }
      break;
    case DIAMOND:
      switch(equipment_type){
      case HELMET:     armor = Items.DIAMOND_HELMET; break;
      case CHESTPLATE: armor = Items.DIAMOND_CHESTPLATE; break;
      case LEGGINGS:   armor = Items.DIAMOND_LEGGINGS; break;
      case BOOTS:      armor = Items.DIAMOND_BOOTS; break;
      }
      break;
    case NETHERITE:
      switch(equipment_type){
      case HELMET:     armor = Items.NETHERITE_HELMET; break;
      case CHESTPLATE: armor = Items.NETHERITE_CHESTPLATE; break;
      case LEGGINGS:   armor = Items.NETHERITE_LEGGINGS; break;
      case BOOTS:      armor = Items.NETHERITE_BOOTS; break;
      }
      break;
    }
    return armor;
  }

}
