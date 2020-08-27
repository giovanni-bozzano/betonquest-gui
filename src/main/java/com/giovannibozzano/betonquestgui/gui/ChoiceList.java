package com.giovannibozzano.betonquestgui.gui;

import java.util.ArrayList;
import java.util.List;

public class ChoiceList
{
    private final List<RowList> choices = new ArrayList<>();
    private final int availableLines;
    private int absoluteShift;
    private int choicesShift;
    private boolean startWithBlankLine;

    public ChoiceList(int availableLines)
    {
        this.availableLines = availableLines;
    }

    public RowList get(int index)
    {
        return this.choices.get(index);
    }

    public int getChoicesShift()
    {
        return this.choicesShift;
    }

    public int getSize()
    {
        return this.choices.size();
    }

    public void add(RowList choice)
    {
        this.choices.add(choice);
    }

    public void setAbsoluteShift(int absoluteShift)
    {
        this.absoluteShift = absoluteShift;
    }

    public boolean canShiftDown()
    {
        return this.absoluteShift < this.getLinesAmountToShow() - this.availableLines;
    }

    public boolean canShiftUp()
    {
        return this.absoluteShift > 0;
    }

    public int getLinesAmountToShow()
    {
        int linesAmount = 0;
        for (RowList rowList : this.choices) {
            linesAmount++;
            linesAmount += rowList.getLinesAmount();
        }
        linesAmount--;
        return linesAmount;
    }

    public boolean startWithBlankLine()
    {
        return this.startWithBlankLine;
    }

    public void updateChoiceShift()
    {
        int linesToCheck = this.absoluteShift;
        this.choicesShift = 0;
        this.startWithBlankLine = false;
        for (int index = 0; index < this.getSize() && linesToCheck > 0; index++, linesToCheck--) {
            if (this.choices.get(index).getLinesAmount() <= linesToCheck) {
                linesToCheck -= this.choices.get(index).getLinesAmount();
                this.choicesShift++;
            } else {
                this.choices.get(index).setShift(linesToCheck);
                for (int counter = index + 1; counter < this.getSize(); counter++) {
                    if (this.choices.get(counter).canShiftDown() || this.choices.get(counter).canShiftUp()) {
                        this.choices.get(counter).setRowModifier(linesToCheck);
                    }
                }
                break;
            }
            // Count blank line
            if (linesToCheck == 0) {
                this.startWithBlankLine = true;
                return;
            }
        }
    }
}
