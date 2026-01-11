package addsynth.core.util.game.entity;

import net.minecraft.world.entity.Entity;

public final class MobUtil {

  public static final void setPosition(final Entity entity, final double x, final double y, final double z){
    entity.setPos(x, y, z); // also updates bounding box
    entity.xOld = x;
    entity.yOld = y;
    entity.zOld = z;
    entity.xo = x;
    entity.yo = y;
    entity.zo = z;
  }

  /**
   * @param entity
   * @param direction must be a float value from 0.0f to 359.999999f
   * @see net.minecraft.core.Direction#toYRot()
   */
  public static final void setEntityFacingDirection(final Entity entity, final float direction){
    entity.setYRot(direction);
    entity.yRotO = direction;
    entity.setYBodyRot(direction);
    entity.setYHeadRot(direction);
  }

}
