package com.giovannibozzano.betonquestgui.gui;

public class IndexedChoice
{
    private final int id;
    private final String text;

    public IndexedChoice(int id, String text)
    {
        this.id = id;
        this.text = text;
    }

    public int getId()
    {
        return this.id;
    }

    public String getText()
    {
        return this.text;
    }
}
