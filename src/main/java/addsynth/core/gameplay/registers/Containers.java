package addsynth.core.gameplay.registers;

import addsynth.core.gameplay.blocks.jukebox.JukeboxContainer;
import addsynth.core.gameplay.items.backpack.BackpackContainer;
import addsynth.core.gameplay.reference.Names;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class Containers {

  public static final RegistryObject<MenuType<JukeboxContainer>> AUTO_JUKEBOX  =
    RegistryObject.create(Names.AUTO_JUKEBOX, ForgeRegistries.MENU_TYPES);

  public static final RegistryObject<MenuType<BackpackContainer>> BACKPACK =
    RegistryObject.create(Names.BACKPACK, ForgeRegistries.MENU_TYPES);

}
