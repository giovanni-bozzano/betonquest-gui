package com.giovannibozzano.betonquestgui.gui.widgets;

import com.giovannibozzano.betonquestgui.gui.Row;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    public void render(int mouseX, int mouseY, float unused)
    {
        this.drawString(Minecraft.getInstance().fontRenderer, this.row.getText(), this.x, this.y, this.color);
    }
}
