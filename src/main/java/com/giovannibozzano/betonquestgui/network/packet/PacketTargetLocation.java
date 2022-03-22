package com.giovannibozzano.betonquestgui.network.packet;

import com.giovannibozzano.betonquestgui.network.PacketHandler;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTargetLocation
{
    private final int x;
    private final int y;
    private final int z;

    public PacketTargetLocation(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static PacketTargetLocation decode(FriendlyByteBuf buffer)
    {
        byte[] data = new byte[buffer.capacity()];
        buffer.getBytes(0, data);
        ByteArrayDataInput input = ByteStreams.newDataInput(data);
        input.readByte();
        return new PacketTargetLocation(input.readInt(), input.readInt(), input.readInt());
    }

    public static class Handler
    {
        public static void handle(PacketTargetLocation packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() -> PacketHandler.handleTargetLocation(packet.x, packet.y, packet.z));
            context.get().setPacketHandled(true);
        }
    }
}
