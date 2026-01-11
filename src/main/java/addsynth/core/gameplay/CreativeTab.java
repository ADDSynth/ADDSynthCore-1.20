package addsynth.core.gameplay;

import addsynth.core.ADDSynthCore;
import addsynth.core.compat.Compatibility;
import addsynth.core.gameplay.reference.Core;
import addsynth.core.gameplay.reference.Trophy;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CreativeTab {

  private static final ResourceLocation id = ADDSynthCore.getLocation("creative_tab");
  public  static final ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);

  public static final void register(final Registry<CreativeModeTab> registry){
    final CreativeModeTab creative_tab = CreativeModeTab.builder()
      .title(Component.literal(ADDSynthCore.NAME))
      .icon(() -> new ItemStack(Item.BY_BLOCK.get(Core.caution_block.get())))
      .displayItems((displayParameters, output) -> {
        output.accept(Core.caution_block.get());
        output.accept(Core.music_box.get());
        output.accept(Core.music_sheet.get());
        output.accept(Core.team_manager.get());
        output.accept(Core.auto_jukebox.get());
        output.accept(Core.backpack.get());
        if(Compatibility.CURIOS.isLoaded()){
          output.accept(Core.personal_beacon.get());
        }
        output.accept(Core.watering_can.get());
        output.accept(Core.hedge_trimmers.get());
        // output.accept(Core.iron_shield.get());
        output.accept(Trophy.trophy_base.get());
        output.accept(Trophy.bronze.get());
        output.accept(Trophy.silver.get());
        output.accept(Trophy.gold.get());
        output.accept(Trophy.platinum.get());
        output.accept(Core.conch_shell.get());
        output.accept(Core.sand_dollar.get());
        output.accept(Core.cowrie.get());
        output.accept(Core.lions_paw.get());
        output.accept(Core.scallop.get());
        output.accept(Core.white_scallop.get());
        output.accept(Core.wentletrap.get());
        output.accept(Core.venus_comb.get());
        output.accept(Core.pearl.get());
      }).build();
    Registry.register(registry, key, creative_tab);
  }

}
