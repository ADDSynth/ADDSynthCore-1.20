package addsynth.core.gameplay;

import addsynth.core.ADDSynthCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class Sounds {

  public static final class Names {
    public static final ResourceLocation watering_can = ADDSynthCore.getLocation("item.watering_can.use");
  }

  public static final RegistryObject<SoundEvent> watering_can_use = RegistryObject.create(Names.watering_can, ForgeRegistries.SOUND_EVENTS);

}
