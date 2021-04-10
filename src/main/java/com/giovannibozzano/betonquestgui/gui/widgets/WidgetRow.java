package com.giovannibozzano.betonquestgui.gui.widgets;

import com.giovannibozzano.betonquestgui.gui.Row;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nonnull;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class WidgetRow extends AbstractGui implements IRenderable
{
    private final int x;
    private final int y;
    private final int color;
    private final Row row;

    public WidgetRow(int x, int y, Row row)
    {
        this(x, y, TextFormatting.WHITE.getColor(), row);
    }

    public WidgetRow(int x, int y, int color, Row row)
    {
        this.x = x;
        this.y = y;
        this.color = color;
        this.row = row;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float unused)
    {
        MutableInt xOffset = new MutableInt();
        this.row.getText().visit((style, text) -> {
            ITextComponent textComponent = new StringTextComponent(text).setStyle(style);
            drawString(matrixStack, Minecraft.getInstance().font, new StringTextComponent(text).setStyle(style), this.x + xOffset.getValue(), this.y, this.color);
            xOffset.add(Minecraft.getInstance().font.width(textComponent));
            return Optional.empty();
        }, Style.EMPTY);
    }
}
