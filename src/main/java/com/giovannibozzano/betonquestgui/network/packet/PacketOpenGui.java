package com.giovannibozzano.betonquestgui.network.packet;

import com.giovannibozzano.betonquestgui.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenGui
{
    public static PacketOpenGui decode(FriendlyByteBuf buffer)
    {
        return new PacketOpenGui();
    }

    public static class Handler
    {
        public static void handle(PacketOpenGui packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(PacketHandler::handleOpenGui);
            context.get().setPacketHandled(true);
        }
    }
}
