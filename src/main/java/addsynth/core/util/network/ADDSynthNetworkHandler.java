package addsynth.core.util.network;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public abstract class ADDSynthNetworkHandler {

  protected static final SimpleChannel createChannel(final ResourceLocation name){
    return createChannel(name, "1");
  }

  protected static final SimpleChannel createChannel(final ResourceLocation name, final String network_version){
    return NetworkRegistry.newSimpleChannel(name, () -> network_version, network_version::equals, network_version::equals);
  }

  protected static final <M extends INetworkMessage> void registerServerMessage(int index, SimpleChannel channel, Class<M> clazz, Function<FriendlyByteBuf, M> decoder){
    channel.registerMessage(index, clazz, (M message, FriendlyByteBuf buf) -> message.encode(buf), decoder, INetworkMessage::staticHandle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
  }

  protected static final <M extends IClientMessage> void registerClientMessage(int index, SimpleChannel channel, Class<M> clazz, Function<FriendlyByteBuf, M> decoder){
    channel.registerMessage(index, clazz, (M message, FriendlyByteBuf buf) -> message.encode(buf), decoder, IClientMessage::staticHandle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
  }

}
