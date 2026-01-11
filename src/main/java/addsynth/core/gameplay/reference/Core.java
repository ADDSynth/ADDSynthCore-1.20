package addsynth.core.gameplay.reference;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class Core {

  public static final RegistryObject<Block> caution_block = RegistryObject.create(Names.CAUTION_BLOCK, ForgeRegistries.BLOCKS);
  public static final RegistryObject<Block> music_box     = RegistryObject.create(Names.MUSIC_BOX,     ForgeRegistries.BLOCKS);
  public static final RegistryObject<Item>  music_sheet   = RegistryObject.create(Names.MUSIC_SHEET,   ForgeRegistries.ITEMS);
  public static final RegistryObject<Block> team_manager  = RegistryObject.create(Names.TEAM_MANAGER,  ForgeRegistries.BLOCKS);
  public static final RegistryObject<Block> auto_jukebox  = RegistryObject.create(Names.AUTO_JUKEBOX,  ForgeRegistries.BLOCKS);

  public static final RegistryObject<Item> backpack        = RegistryObject.create(Names.BACKPACK,        ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> personal_beacon = RegistryObject.create(Names.PERSONAL_BEACON, ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> watering_can    = RegistryObject.create(Names.WATERING_CAN,    ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> hedge_trimmers  = RegistryObject.create(Names.HEDGE_TRIMMERS,  ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> iron_shield     = RegistryObject.create(Names.IRON_SHIELD,     ForgeRegistries.ITEMS);

  public static final RegistryObject<Item> conch_shell    = RegistryObject.create(Names.CONCH_SHELL,   ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> sand_dollar    = RegistryObject.create(Names.SAND_DOLLAR,   ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> cowrie         = RegistryObject.create(Names.COWRIE,        ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> lions_paw      = RegistryObject.create(Names.LIONS_PAW,     ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> scallop        = RegistryObject.create(Names.SCALLOP,       ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> white_scallop  = RegistryObject.create(Names.WHITE_SCALLOP, ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> wentletrap     = RegistryObject.create(Names.WENTLETRAP,    ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> venus_comb     = RegistryObject.create(Names.VENUS_COMB,    ForgeRegistries.ITEMS);
  public static final RegistryObject<Item> pearl          = RegistryObject.create(Names.PEARL,         ForgeRegistries.ITEMS);

}
