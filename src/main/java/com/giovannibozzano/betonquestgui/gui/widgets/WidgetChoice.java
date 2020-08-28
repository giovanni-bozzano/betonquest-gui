package com.giovannibozzano.betonquestgui.gui.widgets;

import com.giovannibozzano.betonquestgui.gui.Row;
import com.giovannibozzano.betonquestgui.gui.RowList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class WidgetChoice extends AbstractGui implements IRenderable, IGuiEventListener
{
    private final int x;
    private final int y;
    private final int mouseOverColor;
    private final RowList choice;
    private final int maximumWidth;
    private final int maximumHeight;
    private final WidgetChoice.IPressable onPress;

    public WidgetChoice(int x, int y, int mouseOverColor, RowList choice, int maximumWidth, int maximumHeight, WidgetChoice.IPressable onPress)
    {
        this.x = x;
        this.y = y;
        this.mouseOverColor = mouseOverColor;
        this.choice = choice;
        this.maximumWidth = maximumWidth;
        this.maximumHeight = maximumHeight;
        this.onPress = onPress;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (!this.isMouseOver(mouseX, mouseY)) {
            return false;
        }
        this.playDownSound(Minecraft.getInstance().getSoundHandler());
        this.onPress.onPress(this);
        return true;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float unused)
    {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        for (int row = 0; this.choice.getShift() + row < this.choice.getLinesAmount() && row - this.choice.getRowModifier() < this.maximumHeight / fontRenderer.FONT_HEIGHT; row++) {
            Row textRow = this.choice.getRow(row);
            if (this.isMouseOver(mouseX, mouseY)) {
                new WidgetRow(this.x, this.y + row * fontRenderer.FONT_HEIGHT, this.mouseOverColor, textRow).render(matrixStack, mouseX, mouseY, unused);
            } else {
                new WidgetRow(this.x, this.y + row * fontRenderer.FONT_HEIGHT, textRow).render(matrixStack, mouseX, mouseY, unused);
            }
        }
    }

    public boolean isMouseOver(double mouseX, double mouseY)
    {
        return mouseX >= this.x && mouseX < this.x + this.maximumWidth && mouseY >= this.y && mouseY < this.y + this.maximumHeight && mouseY < this.y + (this.choice.getLinesAmount() - this.choice.getShift()) * Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
    }

    public void playDownSound(SoundHandler soundHandler)
    {
        soundHandler.play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public RowList getChoice()
    {
        return this.choice;
    }

    @OnlyIn(Dist.CLIENT)
    public interface IPressable
    {
        void onPress(WidgetChoice widgetChoice);
    }
}
