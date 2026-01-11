package addsynth.core.util.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public abstract class INetworkMessage {

  public abstract void encode(FriendlyByteBuf buf);
  /** If you need the {@link MinecraftServer}, get it from {@link ServerPlayer#server}! */
  protected abstract void handle(ServerPlayer sender);
  
  public static final <M extends INetworkMessage> void staticHandle(M message, Supplier<NetworkEvent.Context> context_supplier){
    final NetworkEvent.Context context = context_supplier.get();
    context.enqueueWork(() -> {
      final ServerPlayer sender = context.getSender();
      if(sender != null){
        message.handle(sender);
      }
    });
    context.setPacketHandled(true);
  }

}
