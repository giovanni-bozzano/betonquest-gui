package com.giovannibozzano.betonquestgui.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record PacketPlayerChoice(int id)
{
    public static void encode(PacketPlayerChoice packet, FriendlyByteBuf buffer)
    {
        buffer.writeInt(packet.id);
    }
}
