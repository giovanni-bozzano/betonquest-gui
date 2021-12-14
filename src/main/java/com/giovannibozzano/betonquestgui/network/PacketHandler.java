package com.giovannibozzano.betonquestgui.network;

import com.giovannibozzano.betonquestgui.BetonQuestGui;
import com.giovannibozzano.betonquestgui.gui.BetonQuestConversation;
import com.giovannibozzano.betonquestgui.gui.IndexedChoice;
import com.giovannibozzano.betonquestgui.network.packet.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
        Minecraft.getInstance().setScreen(null);
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
            Minecraft.getInstance().setScreen(BETONQUEST_CONVERSATION);
        }
    }

    public static void handleNpcDialogue(String npcName, String text)
    {
        if (BETONQUEST_CONVERSATION != null) {
            if (Minecraft.getInstance().screen != BETONQUEST_CONVERSATION) {
                Minecraft.getInstance().setScreen(BETONQUEST_CONVERSATION);
            }
            BETONQUEST_CONVERSATION.updateNpcName(npcName);
            String[] parts = text.split(BetonQuestConversation.FORMATTING_CODE_PATTERN.pattern());
            BaseComponent textComponent = new TextComponent(": ");
            List<ChatFormatting> textFormatting = new ArrayList<>();
            for (String part : parts) {
                if (part.length() == 2 && part.charAt(0) == '\u00A7') {
                    textFormatting.add(ChatFormatting.getByCode(part.charAt(1)));
                } else {
                    Style style = Style.EMPTY;
                    for (ChatFormatting format : textFormatting) {
                        style = style.applyFormat(format);
                    }
                    textComponent.append(new TextComponent(part).setStyle(style));
                    textFormatting.clear();
                }
            }
            BETONQUEST_CONVERSATION.appendToLeft(new TextComponent(npcName).setStyle(Style.EMPTY.applyFormat(ChatFormatting.BOLD).applyFormat(ChatFormatting.GREEN)), textComponent);
            BETONQUEST_CONVERSATION.resetRightList();
        }
    }

    public static void handlePlayerChoice(int id, String text)
    {
        if (BETONQUEST_CONVERSATION != null) {
            if (Minecraft.getInstance().screen != BETONQUEST_CONVERSATION) {
                Minecraft.getInstance().setScreen(BETONQUEST_CONVERSATION);
            }
            BETONQUEST_CONVERSATION.appendToRight(new IndexedChoice(id, text));
        }
    }
}
