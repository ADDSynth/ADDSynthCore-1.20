package addsynth.core.gameplay.blocks.team_manager.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import addsynth.core.ADDSynthCore;
import addsynth.core.gameplay.config.Config;
import addsynth.core.util.game.data.CombinedNameComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/** Client-only class. Builds all the Criteria lists used in the Team Manager.
 *  Fires only once on game start, when mods are loaded. */
public final class CriteriaData {

  private static CombinedNameComponent[] statistics;
  private static CombinedNameComponent[] blocks;
  private static CombinedNameComponent[] items_with_durability;
  private static CombinedNameComponent[] items;
  private static CombinedNameComponent[] entities;
  public static final CombinedNameComponent[] standard_criteria = {
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.dummy"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.trigger"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.death_count"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.player_kills"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.total_kills"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.health"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.xp"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.xp_level"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.air"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.food"),
    new CombinedNameComponent.AlwaysTranslated("gui.addsynthcore.team_manager.criteria.armor")
  };
  public static final CombinedNameComponent[] chat_colors = {
    // the Minecraft colors are translated, however the teams use the ChatFormatting colors.
    new CombinedNameComponent(ChatFormatting.BLACK),
    new CombinedNameComponent(ChatFormatting.DARK_BLUE),
    new CombinedNameComponent(ChatFormatting.DARK_GREEN),
    new CombinedNameComponent(ChatFormatting.DARK_AQUA),
    new CombinedNameComponent(ChatFormatting.DARK_RED),
    new CombinedNameComponent(ChatFormatting.DARK_PURPLE),
    new CombinedNameComponent(ChatFormatting.GOLD),
    new CombinedNameComponent(ChatFormatting.GRAY),
    new CombinedNameComponent(ChatFormatting.DARK_GRAY),
    new CombinedNameComponent(ChatFormatting.BLUE),
    new CombinedNameComponent(ChatFormatting.GREEN),
    new CombinedNameComponent(ChatFormatting.AQUA),
    new CombinedNameComponent(ChatFormatting.RED),
    new CombinedNameComponent(ChatFormatting.LIGHT_PURPLE),
    new CombinedNameComponent(ChatFormatting.YELLOW),
    new CombinedNameComponent(ChatFormatting.WHITE)
  };

  public static final void calculate(){
    try{
      ADDSynthCore.log.info("Recalculating Criteria Data for Team Manager (Client Only) ...");
      calculateBlocks();
      calculateItems();
      calculateStatistics();
      calculateEntities();
      sort();
    }
    catch(Exception e){
      ADDSynthCore.log.error("Encountered an error while calculating Criteria Data for the Team Manager.", e);
    }
  }
  
  public static final void sort(){
    final boolean translate_names = Config.translate_criteria_list.get();
    final Comparator<CombinedNameComponent> sorter = CombinedNameComponent.getSorter(translate_names);
    Arrays.sort(blocks, sorter);
    Arrays.sort(items, sorter);
    Arrays.sort(items_with_durability, sorter);
    Arrays.sort(statistics, sorter);
    Arrays.sort(entities, sorter);
  }
  
  private static final void calculateBlocks(){
    final Set<Map.Entry<ResourceKey<Block>, Block>> entries = ForgeRegistries.BLOCKS.getEntries();
    final int length = entries.size();
    blocks = new CombinedNameComponent[length];
    int i = 0;
    for(final Map.Entry<ResourceKey<Block>, Block> entry : entries){
      blocks[i] = new CombinedNameComponent(entry.getValue(), entry.getKey().location());
      i++;
    }
  }
  
  private static final void calculateItems(){
    final Set<Map.Entry<ResourceKey<Item>, Item>> entries = ForgeRegistries.ITEMS.getEntries();
    final int length = entries.size();
    items = new CombinedNameComponent[length];
    final ArrayList<CombinedNameComponent> durability_items = new ArrayList<CombinedNameComponent>(length);
    int i = 0;
    for(final Map.Entry<ResourceKey<Item>, Item> entry : entries){
      items[i] = new CombinedNameComponent(entry.getValue(), entry.getKey().location());
      if(entry.getValue().canBeDepleted()){
        durability_items.add(items[i]);
      }
      i++;
    }
    items_with_durability = durability_items.toArray(new CombinedNameComponent[durability_items.size()]);
  }
  
  private static final void calculateStatistics(){
    final ArrayList<CombinedNameComponent> list = new ArrayList<CombinedNameComponent>(200);
    for(final Stat<ResourceLocation> stat : Stats.CUSTOM){
      list.add(new CombinedNameComponent(stat));
    }
    statistics = list.toArray(new CombinedNameComponent[list.size()]);
  }
  
  private static final void calculateEntities(){
    final Set<Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>>> entries = ForgeRegistries.ENTITY_TYPES.getEntries();
    final int length = entries.size();
    entities = new CombinedNameComponent[length];
    int i = 0;
    for(final Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : entries){
      entities[i] = new CombinedNameComponent(entry.getValue(), entry.getKey().location());
      i++;
    }
  }
  
  
  
  
  public static final CombinedNameComponent[] getStatistics(){
    return statistics;
  }
  
  public static final CombinedNameComponent[] getAllBlocks(){
    return blocks;
  }
  
  public static final CombinedNameComponent[] getAllItems(){
    return items;
  }
  
  public static final CombinedNameComponent[] getItemsWithDurability(){
    return items_with_durability;
  }

  public static final CombinedNameComponent[] getEntities(){
    return entities;
  }

}
