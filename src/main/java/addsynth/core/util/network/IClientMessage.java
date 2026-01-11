package addsynth.core.util.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class IClientMessage {

  public abstract void encode(FriendlyByteBuf buf);
  protected abstract void handle();
  
  public static final <M extends IClientMessage> void staticHandle(M message, Supplier<NetworkEvent.Context> context_supplier){
    final NetworkEvent.Context context = context_supplier.get();
    // Forge Docs want me to use DistExecutor, but I see no reason to do that. DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {});
    context.enqueueWork(message::handle);
    context.setPacketHandled(true);
  }

}
