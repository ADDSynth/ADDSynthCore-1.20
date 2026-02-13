package addsynth.core.gameplay.reference;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class Trophy {

  public static final RegistryObject<Block> bronze   = RegistryObject.create(Names.BRONZE_TROPHY,   ForgeRegistries.BLOCKS);
  public static final RegistryObject<Block> silver   = RegistryObject.create(Names.SILVER_TROPHY,   ForgeRegistries.BLOCKS);
  public static final RegistryObject<Block> gold     = RegistryObject.create(Names.GOLD_TROPHY,     ForgeRegistries.BLOCKS);
  public static final RegistryObject<Block> platinum = RegistryObject.create(Names.PLATINUM_TROPHY, ForgeRegistries.BLOCKS);

}
