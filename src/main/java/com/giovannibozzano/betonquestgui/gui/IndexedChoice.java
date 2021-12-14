package com.giovannibozzano.betonquestgui.gui;

public record IndexedChoice(int id, String text)
{
    public int getId()
    {
        return this.id;
    }

    public String getText()
    {
        return this.text;
    }
}
