package com.giovannibozzano.betonquestgui.network.packet;

import net.minecraft.network.PacketBuffer;

public class PacketPlayerChoice
{
    private final int id;

    public PacketPlayerChoice(int id)
    {
        this.id = id;
    }

    public static void encode(PacketPlayerChoice packet, PacketBuffer buffer)
    {
        buffer.writeInt(packet.id);
    }
}
