package addsynth.core.gameplay.blocks.team_manager.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public final class ObjectiveDataUnit {

  public String name;
  public Component display_name;
  public int criteria_type;
  public String criteria_name;
  public ObjectiveCriteria criteria;
  /** If this objective can be modified (NOT readOnly) */
  public boolean modify;
  
  public final void encode(final FriendlyByteBuf data){
    data.writeUtf(name);
    data.writeComponent(display_name);
    data.writeUtf(criteria.getName());
  }
  
  public static final ObjectiveDataUnit decode(final FriendlyByteBuf data){
    final ObjectiveDataUnit objective = new ObjectiveDataUnit();
    objective.name = data.readUtf();
    objective.display_name = data.readComponent();
    objective.criteria_name = data.readUtf();
    objective.criteria = TeamData.getCriteria(objective.criteria_name);
    objective.criteria_type = TeamData.getCriteriaType(objective.criteria_name);
    objective.modify = !objective.criteria.isReadOnly();
    return objective;
  }

}
