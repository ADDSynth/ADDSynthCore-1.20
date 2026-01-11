package addsynth.core.gameplay;

import addsynth.core.ADDSynthCore;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class Tags {

  public static final TagKey<Block> HEDGE_TRIMMER_MINEABLE = BlockTags.create(ADDSynthCore.getLocation("hedge_trimmers_mineable"));
  // FUTURE: New in Minecraft 1.21.5, Bush, Firefly Bush, Short/Tall Dry Grass, Leaf Litter, and Wildflowers. Add these new blocks to the Hedge Trimmer mineable tag.

}
