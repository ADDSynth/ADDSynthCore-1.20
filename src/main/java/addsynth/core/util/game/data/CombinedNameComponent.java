package addsynth.core.util.game.data;

import java.util.ArrayList;
import java.util.Comparator;
import addsynth.core.gameplay.blocks.team_manager.data.ObjectiveDataUnit;
import addsynth.core.gameplay.blocks.team_manager.data.TeamDataUnit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.scores.PlayerTeam;

/** Especially used in Team Manger, holds an ID name and a display Name, where
 *  the Display Name may have formatting codes, and the ID name is used to find
 *  the Team or Player in the game's data.
 */
public class CombinedNameComponent {

  public static final CombinedNameComponent EMPTY = new CombinedNameComponent();

  protected final Component displayName;
  private final String name;

  private CombinedNameComponent(){
    displayName = Component.empty();
    name = "";
  }

  private CombinedNameComponent(final Component component, final String name){
    displayName = component;
    this.name = name;
  }

  public static class AlwaysTranslated extends CombinedNameComponent {
    public AlwaysTranslated(final String translation_key){
      super(Component.translatable(translation_key), "");
    }
    @Override
    public final String getName(){
      return displayName.getString();
    }
    @Override
    public final String toString(){
      return displayName.getString();
    }
  }

  public CombinedNameComponent(final Block block, final ResourceLocation id){
    displayName = block.getName();
    name = id.toString();
  }

  public CombinedNameComponent(final Item item, final ResourceLocation id){
    displayName = item.getDescription();
    name = id.toString();
  }

  public CombinedNameComponent(final EntityType entity_type, final ResourceLocation id){
    displayName = entity_type.getDescription();
    name = id.toString();
  }

  /** This now correctly translates Stats, in exactly the same way as
   *  {@link net.minecraft.client.gui.screens.achievement.StatsScreen#getTranslationKey StatsScreen} does. */
  public CombinedNameComponent(final Stat<ResourceLocation> stat){
    displayName = Component.translatable("stat." + stat.getValue().toString().replace(':', '.'));
    name = stat.getValue().toString();
  }

  public CombinedNameComponent(final ChatFormatting color){
    displayName = Component.translatable("chatformat.color."+color.getName());
    name = color.getName();
  }

  public CombinedNameComponent(final PlayerTeam team){
    displayName = team.getDisplayName();
    name = team.getName();
  }
  
  public CombinedNameComponent(final TeamDataUnit team_data){
    displayName = team_data.display_name;
    name = team_data.name;
  }
  
  public CombinedNameComponent(final Player player){
    displayName = player.getDisplayName();
    name = player.getScoreboardName();
  }

  public CombinedNameComponent(final ObjectiveDataUnit objective){
    displayName = objective.display_name;
    name = objective.name;
  }

  /** Returns the ID. */
  public String getName(){
    return name;
  }

  /** Returns the translated Component. May have styles applied to Component. */
  public final Component getDisplayName(){
    return displayName;
  }

  /** Returns a copy of the Component without any styling. */
  public final Component getPlainComponent(){
    return displayName.plainCopy();
  }

  // TODO: These functions are here, for now, but I'll want my own interface that objects can implement
  //       that proves they have an Encode and Decode function, so I only have to call a common library
  //       function that automatically saves an array of these objects, while only requiring they implement
  //       the Encode/Decode interface.
  public static final void encodeArray(final FriendlyByteBuf buf, final ArrayList<CombinedNameComponent> list){
    encodeArray(buf, list.toArray(new CombinedNameComponent[list.size()]));
  }
  
  public static final void encodeArray(final FriendlyByteBuf buf, final CombinedNameComponent[] list){
    buf.writeInt(list.length);
    for(CombinedNameComponent name : list){
      buf.writeComponent(name.displayName);
      buf.writeUtf(name.name);
    }
  }

  public static final CombinedNameComponent[] decodeArray(final FriendlyByteBuf buf){
    final int length = buf.readInt();
    final CombinedNameComponent[] list = new CombinedNameComponent[length];
    for(int i = 0; i < length; i++){
      list[i] = new CombinedNameComponent(buf.readComponent(), buf.readUtf());
    }
    return list;
  }

  public static final Comparator<CombinedNameComponent> getSorter(final boolean use_translated_name){
    return use_translated_name ? CombinedNameComponent::sortDisplay : CombinedNameComponent::sortID;
  }

  public static final int sortDisplay(CombinedNameComponent a, CombinedNameComponent b){
    return a.displayName.getString().compareTo(b.displayName.getString());
  }

  public static final int sortID(CombinedNameComponent a, CombinedNameComponent b){
    return a.name.compareTo(b.name);
  }

  @Override
  public String toString(){
    return name;
  }

}
