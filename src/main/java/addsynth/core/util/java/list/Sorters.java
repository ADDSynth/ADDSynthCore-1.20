package addsynth.core.util.java.list;

import java.util.Comparator;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.tags.ITag;

public final class Sorters {

  public static final Comparator<ResourceLocation> NameComparer = (final ResourceLocation o1, final ResourceLocation o2) -> {
    return o1.compareNamespaced(o2);
  };

  public static final Comparator<ResourceLocation> NameComparerMinecraftFirst = (final ResourceLocation o1, final ResourceLocation o2) -> {
    return compareMinecraftAndForge(o1, o2);
  };

  public static final Comparator<Component> ComponentComparer = (final Component c1, final Component c2) -> {
    return c1.getString().compareTo(c2.getString());
  };

  public static final Comparator<TagKey<?>> KeyComparer = (final TagKey<?> k1, final TagKey<?> k2) -> {
    final ResourceLocation o1 = k1.location();
    final ResourceLocation o2 = k2.location();
    return o1.compareNamespaced(o2);
  };

  public static final Comparator<TagKey<?>> KeyComparerMinecraftFirst = (final TagKey<?> k1, final TagKey<?> k2) -> {
    final ResourceLocation o1 = k1.location();
    final ResourceLocation o2 = k2.location();
    return compareMinecraftAndForge(o1, o2);
  };

  public static final Comparator<ITag<?>> TagComparer = (final ITag<?> tag1, final ITag<?> tag2) -> {
    final ResourceLocation o1 = tag1.getKey().location();
    final ResourceLocation o2 = tag2.getKey().location();
    return o1.compareNamespaced(o2);
  };

  public static final Comparator<ITag<?>> TagComparerMinecraftFirst = (final ITag<?> tag1, final ITag<?> tag2 ) -> {
    final ResourceLocation o1 = tag1.getKey().location();
    final ResourceLocation o2 = tag2.getKey().location();
    return compareMinecraftAndForge(o1, o2);
  };

  public static final <T> int TagPairComparerMinecraftFirst(Pair<TagKey<T>, HolderSet.Named<T>> pair1, Pair<TagKey<T>, HolderSet.Named<T>> pair2){
    return KeyComparerMinecraftFirst.compare(pair1.getFirst(), pair2.getFirst());
  }

  public static final int compareMinecraftAndForge(final ResourceLocation o1, final ResourceLocation o2){
    final String namespace1 = o1.getNamespace();
    final String namespace2 = o2.getNamespace();
    if(namespace1.equals("minecraft")){
      if(namespace2.equals("minecraft")){
        return o1.getPath().compareTo(o2.getPath());
      }
      return -1;
    }
    if(namespace2.equals("minecraft")){
      return 1;
    }
    if(namespace1.equals("forge")){
      if(namespace2.equals("forge")){
        return o1.getPath().compareTo(o2.getPath());
      }
      return -1;
    }
    if(namespace2.equals("forge")){
      return 1;
    }
    return o1.compareNamespaced(o2);
  }

}
