package addsynth.core.gameplay;

import addsynth.core.ADDSynthCore;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class Tags {

  public static final TagKey<Block> HEDGE_TRIMMER_MINEABLE = BlockTags.create(ADDSynthCore.getLocation("hedge_trimmers_mineable"));
  // FUTURE: in MC 1.21.4: #minecraft:tall_flowers block tag was removed. Must switch to using my #tall_flowers block tag.
  //         Also, Biomes O' Plenty tall flowers won't be in the #minecraft:tall_flowers block tag anymore.
  // FUTURE: New in Minecraft 1.21.5, Bush, Firefly Bush, Short/Tall Dry Grass, Leaf Litter, and Wildflowers. Add these new blocks to the Hedge Trimmer mineable tag.

}
