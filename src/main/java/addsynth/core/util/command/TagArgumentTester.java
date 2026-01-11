package addsynth.core.util.command;

import java.util.function.Predicate;
import com.mojang.datafixers.util.Either;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

public final class TagArgumentTester<T> implements Predicate<T> {

  private final Holder.Reference<T> holder;
  private final HolderSet.Named<T> set;
  public final boolean isTag;
  public final String name;

  public TagArgumentTester(ResourceOrTagArgument.Result<T> argument_result){
    final Either<Holder.Reference<T>, HolderSet.Named<T>> result = argument_result.unwrap();
    holder = result.left().orElse(null);
    set = result.right().orElse(null);
    isTag = set != null;
    name = argument_result.asPrintable();
  }

  @Override
  public final boolean test(T input){
    if(holder != null){
      return holder.value() == input;
    }
    if(set != null){
      for(final Holder<T> h : set){
        if(h.value() == input){
          return true;
        }
      }
    }
    return false;
  }

}
