package addsynth.core.gameplay.registers;

import addsynth.core.game.registry.BlockEntityHolder;
import addsynth.core.gameplay.blocks.jukebox.TileJukeboxPlayer;
import addsynth.core.gameplay.blocks.music_box.TileMusicBox;
import addsynth.core.gameplay.reference.Core;
import addsynth.core.gameplay.reference.Names;

public final class Tiles {

  public static final BlockEntityHolder<TileMusicBox> MUSIC_BOX =
    new BlockEntityHolder<>(Names.MUSIC_BOX, TileMusicBox::new, Core.music_box);

  public static final BlockEntityHolder<TileJukeboxPlayer> AUTO_JUKEBOX =
    new BlockEntityHolder<>(Names.AUTO_JUKEBOX, TileJukeboxPlayer::new, Core.auto_jukebox);

}
