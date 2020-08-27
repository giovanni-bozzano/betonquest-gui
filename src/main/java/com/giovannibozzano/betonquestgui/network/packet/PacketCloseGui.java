package com.giovannibozzano.betonquestgui.network.packet;

import com.giovannibozzano.betonquestgui.network.PacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCloseGui
{
    public static void encode(PacketCloseGui packet, PacketBuffer buffer)
    {
    }

    public static PacketCloseGui decode(PacketBuffer buffer)
    {
        return new PacketCloseGui();
    }

    public static class Handler
    {
        public static void handle(PacketCloseGui packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(PacketHandler::handleCloseGui);
            context.get().setPacketHandled(true);
        }
    }
}
