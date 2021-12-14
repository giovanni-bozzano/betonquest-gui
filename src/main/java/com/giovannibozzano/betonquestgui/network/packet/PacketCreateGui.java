package com.giovannibozzano.betonquestgui.network.packet;

import com.giovannibozzano.betonquestgui.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCreateGui
{
    public static PacketCreateGui decode(FriendlyByteBuf buffer)
    {
        return new PacketCreateGui();
    }

    public static class Handler
    {
        public static void handle(PacketCreateGui packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(PacketHandler::handleCreateGui);
            context.get().setPacketHandled(true);
        }
    }
}
