package addsynth.core.util.game;

import java.io.File;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public final class Game {

  /** <p>Recommend you use this to register your config classes now.
   *  <p>We switched to this because, previously, we would keep the relevant variables as static fields in the
   *  class. However, accessing the class would initialize static fields in order. If the fields we needed were
   *  at the top, this would call the constructor and try to access other static fields that weren't initialized
   *  yet, causing a NPE. Since I don't really need to keep this data, I moved all necessary code needed to
   *  register the config outside the class.
   *  <p>I may have to revisit this issue if I ever want to change configs in a gui, but this will do for now.
   * @param <C>
   * @param context
   * @param config_constructor
   * @param filename
   */
  public static final <C> void registerConfig(ModLoadingContext context, Function<ForgeConfigSpec.Builder, C> config_constructor, String filename){
    registerConfig(context, ModConfig.Type.COMMON, config_constructor, filename);
  }

  /** <p>Recommend you use this to register your config classes now.
   *  <p>We switched to this because, previously, we would keep the relevant variables as static fields in the
   *  class. However, accessing the class would initialize static fields in order. If the fields we needed were
   *  at the top, this would call the constructor and try to access other static fields that weren't initialized
   *  yet, causing a NPE. Since I don't really need to keep this data, I moved all necessary code needed to
   *  register the config outside the class.
   *  <p>I may have to revisit this issue if I ever want to change configs in a gui, but this will do for now.
   * @param <C>
   * @param context
   * @param config_constructor
   * @param mod_name
   * @param filename
   */
  public static final <C> void registerConfig(ModLoadingContext context, Function<ForgeConfigSpec.Builder, C> config_constructor, String mod_name, String filename){
    registerConfig(context, ModConfig.Type.COMMON, config_constructor, mod_name+File.separator+filename);
  }

  /** <p>Recommend you use this to register your config classes now.
   *  <p>We switched to this because, previously, we would keep the relevant variables as static fields in the
   *  class. However, accessing the class would initialize static fields in order. If the fields we needed were
   *  at the top, this would call the constructor and try to access other static fields that weren't initialized
   *  yet, causing a NPE. Since I don't really need to keep this data, I moved all necessary code needed to
   *  register the config outside the class.
   *  <p>I may have to revisit this issue if I ever want to change configs in a gui, but this will do for now.
   * @param <C>
   * @param context
   * @param type
   * @param config_constructor
   * @param filename
   */
  public static final <C> void registerConfig(ModLoadingContext context, ModConfig.Type type, Function<ForgeConfigSpec.Builder, C> config_constructor, String filename){
    if(!filename.endsWith(".toml")){
      filename = filename+".toml";
    }
    final Pair<C, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(config_constructor);
    final ForgeConfigSpec CONFIG_SPEC = SPEC_PAIR.getRight();
    context.registerConfig(type, CONFIG_SPEC, filename);
  }

  /** <p>Recommend you use this to register your config classes now.
   *  <p>We switched to this because, previously, we would keep the relevant variables as static fields in the
   *  class. However, accessing the class would initialize static fields in order. If the fields we needed were
   *  at the top, this would call the constructor and try to access other static fields that weren't initialized
   *  yet, causing a NPE. Since I don't really need to keep this data, I moved all necessary code needed to
   *  register the config outside the class.
   *  <p>I may have to revisit this issue if I ever want to change configs in a gui, but this will do for now.
   * @param <C>
   * @param context
   * @param type
   * @param config_constructor
   * @param mod_name
   * @param filename
   */
  public static final <C> void registerConfig(ModLoadingContext context, ModConfig.Type type, Function<ForgeConfigSpec.Builder, C> config_constructor, String mod_name, String filename){
    registerConfig(context, type, config_constructor, mod_name+File.separator+filename);
  }

  /** @see Stats#makeCustomStat(String, StatFormatter) */
  public static final void registerCustomStat(final ResourceLocation stat){
    Registry.register(BuiltInRegistries.CUSTOM_STAT, stat.getPath(), stat);
    Stats.CUSTOM.get(stat, StatFormatter.DEFAULT);
  }

}
