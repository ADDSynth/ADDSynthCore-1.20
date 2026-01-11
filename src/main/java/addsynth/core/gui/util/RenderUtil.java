package addsynth.core.gui.util;

/*
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
*/

/** I went through all this trouble just to be able to render Items at a varying opacity,
 *  using Reflection.
 * @author ADDSynth
 * @since May 30, 2020
 */

public final class RenderUtil {

  // TODO: The only reason I needed this was to render transparent ItemStacks. I may be able to use
  //       the new ItemStackTileEntityRenderer system: https://mcforge.readthedocs.io/en/1.16.x/rendering/ister/

  /* <p>REPLICA: This is an exact copy of {@link ItemRenderer#renderItemModelIntoGUI(ItemStack, int, int, IBakedModel)}.
   *     You MUST ensure there aren't any changes whenver the Forge version updates!
   * @param stack
   * @param x
   * @param y
   * @param opacity
   *
  @SuppressWarnings({ "null", "resource", "deprecation" })
  public static final void drawItemStack(ItemRenderer itemRenderer, TextureManager textureManager, ItemStack stack, int x, int y, float opacity){
    if(stack.isEmpty()){
      return;
    }
    
    try{
      IBakedModel bakedmodel = itemRenderer.getItemModelWithOverrides(stack, (World)null, (LivingEntity)null);
      
      RenderSystem.pushMatrix();
      GuiUtil.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
      GuiUtil.textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
      RenderSystem.enableRescaleNormal();
      RenderSystem.enableAlphaTest();
      RenderSystem.defaultAlphaFunc();
      RenderSystem.enableBlend();
      // https://www.khronos.org/opengl/wiki/Blending
      // http://docs.gl/gl4/glBlendFunc
      // http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-10-transparency/
      // https://learnopengl.com/Advanced-OpenGL/Blending
      // http://i.stack.imgur.com/i2IAC.jpg
      // https://andersriggelsen.dk/glblendfunc.php
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, opacity);
      RenderSystem.translatef((float)x, (float)y, 100.0F + GuiUtil.itemRenderer.zLevel);
      RenderSystem.translatef(8.0F, 8.0F, 0.0F);
      RenderSystem.scalef(1.0F, -1.0F, 1.0F);
      RenderSystem.scalef(16.0F, 16.0F, 16.0F);
      MatrixStack matrixstack = new MatrixStack();
      IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
      boolean flag = !bakedmodel.func_230044_c_();
      if (flag) {
         RenderHelper.setupGuiFlatDiffuseLighting();
      }

      GuiUtil.itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
      irendertypebuffer$impl.finish();
      RenderSystem.enableDepthTest();
      if (flag) {
         RenderHelper.setupGui3DDiffuseLighting();
      }

      RenderSystem.disableAlphaTest();
      RenderSystem.disableRescaleNormal();
      RenderSystem.popMatrix();
    }
    catch(Exception e){
    }
  }
  */

}
