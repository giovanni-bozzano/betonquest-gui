package com.giovannibozzano.betonquestgui.gui.widgets;

import com.giovannibozzano.betonquestgui.gui.Row;
import com.giovannibozzano.betonquestgui.gui.RowList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class WidgetChoice implements Renderable, GuiEventListener, NarratableEntry
{
    private final int x;
    private final int y;
    private final int mouseOverColor;
    private final RowList choice;
    private final int maximumWidth;
    private final int maximumHeight;
    private final WidgetChoice.IPressable onPress;
    private boolean focused = true;

    public WidgetChoice(int x, int y, int mouseOverColor, RowList choice, int maximumWidth, int maximumHeight, IPressable onPress)
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
        this.playDownSound(Minecraft.getInstance().getSoundManager());
        this.onPress.onPress(this);
        return true;
    }

    @Override
    public void render(@Nonnull GuiGraphics matrixStack, int mouseX, int mouseY, float unused)
    {
        Font fontRenderer = Minecraft.getInstance().font;
        for (int row = 0; this.choice.getShift() + row < this.choice.getLinesAmount() && row - this.choice.getRowModifier() < this.maximumHeight / fontRenderer.lineHeight; row++) {
            Row textRow = this.choice.getRow(row);
            if (this.isMouseOver(mouseX, mouseY)) {
                new WidgetRow(this.x, this.y + row * fontRenderer.lineHeight, this.mouseOverColor, textRow).render(matrixStack, mouseX, mouseY, unused);
            } else {
                new WidgetRow(this.x, this.y + row * fontRenderer.lineHeight, textRow).render(matrixStack, mouseX, mouseY, unused);
            }
        }
    }

    public boolean isMouseOver(double mouseX, double mouseY)
    {
        return mouseX >= this.x && mouseX < this.x + this.maximumWidth && mouseY >= this.y && mouseY < this.y + this.maximumHeight && mouseY < this.y + (this.choice.getLinesAmount() - this.choice.getShift()) * Minecraft.getInstance().font.lineHeight;
    }

    @Override
    public void setFocused(boolean p_265728_)
    {
        this.focused = p_265728_;
    }

    @Override
    public boolean isFocused()
    {
        return this.focused;
    }

    public void playDownSound(SoundManager soundHandler)
    {
        soundHandler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public RowList getChoice()
    {
        return this.choice;
    }

    @Override
    public NarrationPriority narrationPriority()
    {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_)
    {

    }

    @OnlyIn(Dist.CLIENT)
    public interface IPressable
    {
        void onPress(WidgetChoice widgetChoice);
    }
}
