package com.giovannibozzano.betonquestgui.network.packet;

import com.giovannibozzano.betonquestgui.network.PacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAllowCloseGui
{
    public static void encode(PacketCloseGui packet, PacketBuffer buffer)
    {
    }

    public static PacketAllowCloseGui decode(PacketBuffer buffer)
    {
        return new PacketAllowCloseGui();
    }

    public static class Handler
    {
        public static void handle(PacketAllowCloseGui packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(PacketHandler::handleAllowCloseGui);
            context.get().setPacketHandled(true);
        }
    }
}
