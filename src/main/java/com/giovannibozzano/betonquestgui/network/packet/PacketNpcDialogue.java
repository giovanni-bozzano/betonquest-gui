package com.giovannibozzano.betonquestgui.network.packet;

import com.giovannibozzano.betonquestgui.network.PacketHandler;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketNpcDialogue
{
    private final String npcName;
    private final String text;

    public PacketNpcDialogue(String npcName, String text)
    {
        this.npcName = npcName;
        this.text = text;
    }

    public static PacketNpcDialogue decode(PacketBuffer buffer)
    {
        byte[] data = new byte[buffer.capacity()];
        buffer.getBytes(0, data);
        ByteArrayDataInput input = ByteStreams.newDataInput(data);
        input.readByte();
        return new PacketNpcDialogue(input.readUTF(), input.readUTF());
    }

    public static class Handler
    {
        public static void handle(PacketNpcDialogue packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() -> PacketHandler.handleNpcDialogue(packet.npcName, packet.text));
            context.get().setPacketHandled(true);
        }
    }
}
