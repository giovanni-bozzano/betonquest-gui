package com.giovannibozzano.betonquestgui.gui;

import net.minecraft.network.chat.FormattedText;

public record Row(FormattedText text)
{
    public FormattedText getText()
    {
        return this.text;
    }
}
