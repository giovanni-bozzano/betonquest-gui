package com.giovannibozzano.betonquestgui.gui;

import net.minecraft.util.text.ITextProperties;

public class Row
{
    private final ITextProperties text;

    public Row(ITextProperties text)
    {
        this.text = text;
    }

    public ITextProperties getText()
    {
        return this.text;
    }
}
