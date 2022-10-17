package com.giovannibozzano.betonquestgui.gui.widgets;

import com.giovannibozzano.betonquestgui.gui.Row;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nonnull;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class WidgetRow extends GuiComponent implements Widget
{
    private final int x;
    private final int y;
    private final int color;
    private final Row row;

    public WidgetRow(int x, int y, Row row)
    {
        this(x, y, ChatFormatting.WHITE.getColor(), row);
    }

    public WidgetRow(int x, int y, int color, Row row)
    {
        this.x = x;
        this.y = y;
        this.color = color;
        this.row = row;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float unused)
    {
        MutableInt xOffset = new MutableInt();
        this.row.getText().visit((style, text) ->
        {
            Component textComponent = Component.literal(text).setStyle(style);
            drawString(matrixStack, Minecraft.getInstance().font, Component.literal(text).setStyle(style), this.x + xOffset.getValue(), this.y, this.color);
            xOffset.add(Minecraft.getInstance().font.width(textComponent));
            return Optional.empty();
        }, Style.EMPTY);
    }
}
