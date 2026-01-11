package addsynth.core.gameplay.compat;

import addsynth.core.ADDSynthCore;
import addsynth.core.gameplay.reference.ADDSynthCoreText;
import addsynth.core.gameplay.reference.Core;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;

@JeiPlugin
public final class CoreJEIPlugin  implements IModPlugin {

  public static final ResourceLocation id = ADDSynthCore.getLocation("jei_plugin");

  @Override
  public ResourceLocation getPluginUid(){
    return id;
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration){
    add_information(registration);
  }

  private static final void add_information(IRecipeRegistration registry){
    registry.addIngredientInfo(new ItemStack(Core.music_box.get()),      VanillaTypes.ITEM_STACK, ADDSynthCoreText.music_box_description);
    registry.addIngredientInfo(new ItemStack(Core.music_sheet.get()),    VanillaTypes.ITEM_STACK, ADDSynthCoreText.music_sheet_description);
    // registry.addIngredientInfo(new ItemStack(Core.team_manager.get()),   VanillaTypes.ITEM_STACK, TextReference.team_manager_description);
    registry.addIngredientInfo(new ItemStack(Core.watering_can.get()),   VanillaTypes.ITEM_STACK, ADDSynthCoreText.watering_can_description);
    registry.addIngredientInfo(new ItemStack(Core.hedge_trimmers.get()), VanillaTypes.ITEM_STACK, ADDSynthCoreText.hedge_trimmers_description);
    if(Core.personal_beacon.isPresent()){
      registry.addIngredientInfo(new ItemStack(Core.personal_beacon.get()), VanillaTypes.ITEM_STACK, ADDSynthCoreText.personal_beacon_description);
    }
    registry.addIngredientInfo(new ItemStack(Core.auto_jukebox.get()),   VanillaTypes.ITEM_STACK, ADDSynthCoreText.auto_jukebox_description);
  }

}
