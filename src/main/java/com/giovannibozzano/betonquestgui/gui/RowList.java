package com.giovannibozzano.betonquestgui.gui;

import net.minecraft.network.chat.FormattedText;

import java.util.ArrayList;
import java.util.List;

public class RowList
{
    private final List<Row> rowList = new ArrayList<>();
    private final int availableLines;
    private int shift;
    private int rowModifier;

    public RowList(int availableLines, int shift)
    {
        this.availableLines = availableLines;
        this.shift = shift;
    }

    public Row getRow(int index)
    {
        return this.rowList.get(this.shift + index);
    }

    public Row getAbsoluteRow(int index)
    {
        return this.rowList.get(index);
    }

    public int getLinesAmount()
    {
        return this.rowList.size();
    }

    public int getShift()
    {
        return this.shift;
    }

    public int getRowModifier()
    {
        return this.rowModifier;
    }

    public void setShift(int shift)
    {
        this.shift = shift;
    }

    public void setRowModifier(int rowModifier)
    {
        this.rowModifier = rowModifier;
    }

    public void add(List<FormattedText> list)
    {
        for (FormattedText text : list) {
            this.rowList.add(new Row(text));
        }
    }

    public boolean canShiftDown()
    {
        return this.shift < this.getLinesAmount() - this.availableLines;
    }

    public boolean canShiftUp()
    {
        return this.shift > 0;
    }
}
