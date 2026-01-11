package addsynth.core.util.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkUtil {

  public static final void writeBlockPositions(final FriendlyByteBuf data, final BlockPos[] positions){
    data.writeInt(positions.length);
    for(final BlockPos pos : positions){
      data.writeBlockPos(pos);
    }
  }

  public static final BlockPos[] readBlockPositions(final FriendlyByteBuf data){
    int i;
    final int length = data.readInt();
    final BlockPos[] positions = new BlockPos[length];
    for(i = 0; i < length; i++){
      positions[i] = data.readBlockPos();
    }
    return positions;
  }

  public static final void writeStringArray(final FriendlyByteBuf data, final String[] string_array){
    data.writeInt(string_array.length);
    for(final String s : string_array){
      data.writeUtf(s);
    }
  }

  public static final String[] readStringArray(final FriendlyByteBuf data){
    int i;
    final int length = data.readInt();
    final String[] strings = new String[length];
    for(i = 0; i < length; i++){
      strings[i] = data.readUtf();
    }
    return strings;
  }

  public static final void writeTextComponentArray(final FriendlyByteBuf data, final Component[] text_component_array){
    data.writeInt(text_component_array.length);
    for(final Component t : text_component_array){
      data.writeComponent(t);
    }
  }

  public static final Component[] readTextComponentArray(final FriendlyByteBuf data){
    int i;
    final int length = data.readInt();
    final Component[] text_components = new Component[length];
    for(i = 0; i < length; i++){
      text_components[i] = data.readComponent();
    }
    return text_components;
  }

  /** <p>Sends the Network message to all clients in the world you specify.
   *  Must be called on the server side.
   *  <p><b>Note:</b> This may fail on the first tick while you are logging in, because you aren't
   *  logged in yet, so there will be no client players to send to.
   * @param network
   * @param world
   * @param message
   */
  public static final void send_to_clients_in_world(final SimpleChannel network, final Level world, final Object message){
    network.send(PacketDistributor.DIMENSION.with(() -> world.dimension()), message);
  }

  /** <p>Sends a network message to only those players that are close to the TileEntity,
   *  with a default radius of 64 blocks (4 chunks).
   *  <p><b>Note:</b> This may fail on the first tick while you are logging in, because you aren't
   *  logged in yet, so there will be no client players to send to. */
  public static final void send_to_TileEntity(final SimpleChannel network, final BlockEntity tile, final Object message){
    send_to_TileEntity(network, tile, 64, message);
  }

  /** <p>Further restricts which clients to send the network message to by only sending
   *  the message to just the players that are close to the TileEntity.<br>
   *  <p><b>Note:</b> This may fail on the first tick while you are logging in, because you aren't
   *  logged in yet, so there will be no client players to send to. */
  public static final void send_to_TileEntity(final SimpleChannel network, final BlockEntity tile, final double radius, final Object message){
    @SuppressWarnings("resource")
    final Level world = tile.getLevel();
    if(world != null){
      final BlockPos pos = tile.getBlockPos();
      final Supplier<TargetPoint> tp_supplier = () -> {
        return new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, world.dimension());
      };
      network.send(PacketDistributor.NEAR.with(tp_supplier), message);
    }
  }

}
