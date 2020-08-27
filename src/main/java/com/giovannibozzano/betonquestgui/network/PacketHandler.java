package com.giovannibozzano.betonquestgui.network;

import com.giovannibozzano.betonquestgui.BetonQuestGui;
import com.giovannibozzano.betonquestgui.gui.BetonQuestConversation;
import com.giovannibozzano.betonquestgui.gui.IndexedChoice;
import com.giovannibozzano.betonquestgui.network.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler
{
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(BetonQuestGui.MOD_ID, "main"))
            .clientAcceptedVersions(BetonQuestGui.PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(BetonQuestGui.PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> BetonQuestGui.PROTOCOL_VERSION)
            .simpleChannel();
    private static BetonQuestConversation BETONQUEST_CONVERSATION;

    public static void registerPackets()
    {
        int id = 0;
        INSTANCE.messageBuilder(PacketCreateGui.class, id++).decoder(PacketCreateGui::decode).consumer(PacketCreateGui.Handler::handle).add();
        INSTANCE.messageBuilder(PacketOpenGui.class, id++).decoder(PacketOpenGui::decode).consumer(PacketOpenGui.Handler::handle).add();
        INSTANCE.messageBuilder(PacketCloseGui.class, id++).decoder(PacketCloseGui::decode).encoder(PacketCloseGui::encode).consumer(PacketCloseGui.Handler::handle).add();
        INSTANCE.messageBuilder(PacketAllowCloseGui.class, id++).decoder(PacketAllowCloseGui::decode).consumer(PacketAllowCloseGui.Handler::handle).add();
        INSTANCE.messageBuilder(PacketNpcDialogue.class, id++).decoder(PacketNpcDialogue::decode).consumer(PacketNpcDialogue.Handler::handle).add();
        INSTANCE.messageBuilder(PacketAvailablePlayerChoice.class, id++).decoder(PacketAvailablePlayerChoice::decode).consumer(PacketAvailablePlayerChoice.Handler::handle).add();
        INSTANCE.messageBuilder(PacketPlayerChoice.class, id).encoder(PacketPlayerChoice::encode).add();
    }

    public static void handleCreateGui()
    {
        BETONQUEST_CONVERSATION = new BetonQuestConversation();
    }

    public static void handleCloseGui()
    {
        Minecraft.getInstance().displayGuiScreen(null);
    }

    public static void handleAllowCloseGui()
    {
        if (BETONQUEST_CONVERSATION != null) {
            BETONQUEST_CONVERSATION.allowClose();
        }
    }

    public static void handleOpenGui()
    {
        if (BETONQUEST_CONVERSATION != null) {
            Minecraft.getInstance().displayGuiScreen(BETONQUEST_CONVERSATION);
        }
    }

    public static void handleNpcDialogue(String npcName, String text)
    {
        if (BETONQUEST_CONVERSATION != null) {
            if (Minecraft.getInstance().currentScreen != BETONQUEST_CONVERSATION) {
                Minecraft.getInstance().displayGuiScreen(BETONQUEST_CONVERSATION);
            }
            BETONQUEST_CONVERSATION.updateNpcName(npcName);
            String[] parts = text.split(BetonQuestConversation.FORMATTING_CODE_PATTERN.pattern());
            TextComponent textComponent = new StringTextComponent(": ");
            List<TextFormatting> textFormatting = new ArrayList<>();
            for (String part : parts) {
                if (part.length() == 2 && part.charAt(0) == '\u00A7') {
                    textFormatting.add(TextFormatting.fromFormattingCode(part.charAt(1)));
                } else {
                    TextComponent partTextComponent = new StringTextComponent(part);
                    for (TextFormatting format : textFormatting) {
                        partTextComponent.applyTextStyle(format);
                    }
                    textComponent.appendSibling(partTextComponent);
                    textFormatting.clear();
                }
            }
            BETONQUEST_CONVERSATION.appendToLeft(new StringTextComponent(npcName).applyTextStyle(TextFormatting.BOLD).applyTextStyle(TextFormatting.GREEN), textComponent);
            BETONQUEST_CONVERSATION.resetRightList();
        }
    }

    public static void handlePlayerChoice(int id, String text)
    {
        if (BETONQUEST_CONVERSATION != null) {
            if (Minecraft.getInstance().currentScreen != BETONQUEST_CONVERSATION) {
                Minecraft.getInstance().displayGuiScreen(BETONQUEST_CONVERSATION);
            }
            BETONQUEST_CONVERSATION.appendToRight(new IndexedChoice(id, text));
        }
    }
}
