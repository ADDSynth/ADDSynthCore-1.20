package addsynth.core.util.game.data;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class NBTUtil {

  @Nullable
  public static final ResourceLocation loadResourceLocation(final CompoundTag nbt, final String tag){
    final String loaded = nbt.getString(tag);
    if(!loaded.equals("")){
      return ResourceLocation.parse(loaded);
    }
    return null;
  }
  
  public static final ResourceLocation loadResourceLocation(final CompoundTag nbt, final String tag, final ResourceLocation fallback){
    final String loaded = nbt.getString(tag);
    if(!loaded.equals("")){
      return ResourceLocation.parse(loaded);
    }
    return fallback;
  }

  /** Loads a ResourceLocation from the Compound NBT. Uses fallback if tag doesn't exist. Next we check
   *  if an Item exists that matches the ResourceLocation, and returns the fallback if it doesn't. */
  public static final ResourceLocation loadResourceLocationAndCheckItem(final CompoundTag nbt, final String tag, final ResourceLocation fallback){
    final ResourceLocation location = loadResourceLocation(nbt, tag, fallback);
    return ForgeRegistries.ITEMS.containsKey(location) ? location : fallback;
  }

  public static final void saveResourceLocation(final CompoundTag nbt, final String tag, final ResourceLocation resource_location){
    nbt.putString(tag, resource_location.toString());
  }

}
