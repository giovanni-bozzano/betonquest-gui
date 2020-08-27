package com.giovannibozzano.betonquestgui.gui;

import com.giovannibozzano.betonquestgui.gui.widgets.*;
import com.giovannibozzano.betonquestgui.network.PacketHandler;
import com.giovannibozzano.betonquestgui.network.packet.PacketCloseGui;
import com.giovannibozzano.betonquestgui.network.packet.PacketPlayerChoice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@OnlyIn(Dist.CLIENT)
public class BetonQuestConversation extends Screen
{
    public final static Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?<=(?i)\u00a7[0-9A-FK-OR])|(?=(?i)\u00a7[0-9A-FK-OR])");
    private final static int CONVERSATION_DIV_HEIGHT = 120;
    private final static int NAME_DIV_HEIGHT = 30;
    private final static int NAME_DIV_START_HEIGHT = CONVERSATION_DIV_HEIGHT + NAME_DIV_HEIGHT;
    private final static int NAME_STRING_HEIGHT = NAME_DIV_START_HEIGHT - NAME_DIV_HEIGHT / 3 - 1;
    private final static int BUTTON_DIMENSION = 10;
    private final static int STRING_BORDER = 10;
    private final static int STRING_HEIGHT = CONVERSATION_DIV_HEIGHT - STRING_BORDER;
    private final List<IRenderable> objects = new ArrayList<>();
    private final List<IRenderable> leftRows = new ArrayList<>();
    private final List<IRenderable> rightRows = new ArrayList<>();
    private final TextComponent leftText = new StringTextComponent("");
    private final List<IndexedChoice> indexedChoices = new ArrayList<>();
    private IRenderable header;
    private String npcName = "LOADING...";
    private boolean allowClose;
    private ITextComponent lastPlayerChoice;
    private RowList leftRowList;
    private ChoiceList choices;
    WidgetButton leftButtonUp;
    WidgetButton leftButtonDown;
    WidgetButton rightButtonUp;
    WidgetButton rightButtonDown;
    private int leftShift;
    private int rightShift;
    private boolean firstLeftLine = true;

    public BetonQuestConversation()
    {
        super(new StringTextComponent(""));
    }

    @Override
    public void init()
    {
        this.buttons.clear();
        this.objects.clear();
        this.header = new WidgetText(new StringTextComponent(this.npcName).applyTextStyle(TextFormatting.BOLD).applyTextStyle(TextFormatting.GREEN), STRING_BORDER, super.height - NAME_STRING_HEIGHT);
        this.objects.add(new WidgetResizableDiv(this.width / 3 * 2, super.height - CONVERSATION_DIV_HEIGHT, super.width / 3, CONVERSATION_DIV_HEIGHT));
        this.objects.add(new WidgetResizableDiv(this.width / 3 * 2, super.height - NAME_DIV_START_HEIGHT, super.width / 3, NAME_DIV_HEIGHT));
        this.objects.add(new WidgetResizableDiv(0, this.height - CONVERSATION_DIV_HEIGHT, super.width / 3 * 2, CONVERSATION_DIV_HEIGHT));
        this.objects.add(new WidgetResizableDiv(0, this.height - NAME_DIV_START_HEIGHT, super.width / 3 * 2, NAME_DIV_HEIGHT));
        this.objects.add(new WidgetText(this.minecraft.player.getDisplayName().deepCopy().applyTextStyle(TextFormatting.BOLD).applyTextStyle(TextFormatting.YELLOW), super.width / 3 * 2 + STRING_BORDER, super.height - NAME_STRING_HEIGHT));

        this.leftButtonUp = new WidgetButton(
                this.width / 3 * 2 - BUTTON_DIMENSION - STRING_BORDER,
                this.height - CONVERSATION_DIV_HEIGHT + STRING_BORDER,
                BUTTON_DIMENSION, BUTTON_DIMENSION,
                elementButton -> {
                    if (this.leftRowList.canShiftUp()) {
                        this.leftShift--;
                        this.reloadLeftRows();
                    }
                }, 1);
        this.leftButtonDown = new WidgetButton(this.width / 3 * 2 - BUTTON_DIMENSION - STRING_BORDER,
                this.height - BUTTON_DIMENSION - STRING_BORDER,
                BUTTON_DIMENSION, BUTTON_DIMENSION,
                elementButton -> {
                    if (this.leftRowList.canShiftDown()) {
                        this.leftShift++;
                        this.reloadLeftRows();
                    }
                }, 0);
        this.rightButtonUp = new WidgetButton(this.width - BUTTON_DIMENSION - STRING_BORDER,
                this.height - CONVERSATION_DIV_HEIGHT + STRING_BORDER,
                BUTTON_DIMENSION, BUTTON_DIMENSION,
                elementButton -> {
                    if (this.choices.canShiftUp()) {
                        this.rightShift--;
                        this.reloadRightRows();
                    }
                }, 1);
        this.rightButtonDown = new WidgetButton(this.width - BUTTON_DIMENSION - STRING_BORDER,
                this.height - BUTTON_DIMENSION - STRING_BORDER,
                BUTTON_DIMENSION, BUTTON_DIMENSION,
                elementButton -> {
                    if (this.choices.canShiftDown()) {
                        this.rightShift++;
                        this.reloadRightRows();
                    }
                }, 0);

        this.objects.add(this.leftButtonUp);
        this.objects.add(this.leftButtonDown);
        this.objects.add(this.rightButtonUp);
        this.objects.add(this.rightButtonDown);
        this.children.add(this.leftButtonUp);
        this.children.add(this.leftButtonDown);
        this.children.add(this.rightButtonUp);
        this.children.add(this.rightButtonDown);

        this.updateLeftShift();
        this.rightShift = 0;
        this.reloadLeftRows();
        this.reloadRightRows();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc()
    {
        return this.allowClose;
    }

    @Override
    public void onClose()
    {
        super.onClose();
        PacketHandler.INSTANCE.sendToServer(new PacketCloseGui());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount)
    {
        if (amount < 0) {
            if (mouseX > 0 && mouseX < super.width / 3.0F * 2 && mouseY > super.height - CONVERSATION_DIV_HEIGHT && mouseY < super.height) {
                for (int counter = (int) amount; counter != 0; counter++) {
                    if (this.leftRowList.canShiftDown()) {
                        this.leftShift++;
                    }
                    this.reloadLeftRows();
                }
            } else if (mouseX > super.width / 3.0F * 2 && mouseX < super.width && mouseY > super.height - CONVERSATION_DIV_HEIGHT && mouseY < super.height) {
                for (int counter = (int) amount; counter != 0; counter++) {
                    if (this.choices.canShiftDown()) {
                        this.rightShift++;
                    }
                    this.reloadRightRows();
                }
            }
        } else if (amount > 0) {
            if (mouseX > 0 && mouseX < super.width / 3.0F * 2 && mouseY > super.height - CONVERSATION_DIV_HEIGHT && mouseY < super.height) {
                for (int counter = (int) amount; counter != 0; counter--) {
                    if (this.leftRowList.canShiftUp()) {
                        this.leftShift--;
                    }
                    this.reloadLeftRows();
                }
            } else if (mouseX > super.width / 3.0F * 2 && mouseX < super.width && mouseY > super.height - CONVERSATION_DIV_HEIGHT && mouseY < super.height) {
                for (int counter = (int) amount; counter != 0; counter--) {
                    if (this.choices.canShiftUp()) {
                        this.rightShift--;
                    }
                    this.reloadRightRows();
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void render(int mouseX, int mouseY, float unused)
    {
        this.renderBackground(0);
        super.render(mouseX, mouseY, unused);
        for (IRenderable object : this.objects) {
            object.render(mouseX, mouseY, unused);
        }
        for (IRenderable leftRow : this.leftRows) {
            leftRow.render(mouseX, mouseY, unused);
        }
        for (IRenderable rightRow : this.rightRows) {
            rightRow.render(mouseX, mouseY, unused);
        }
        this.header.render(mouseX, mouseY, unused);
    }

    public void allowClose()
    {
        this.allowClose = true;
        this.rightShift = 0;
        this.rightRows.clear();
        this.children.clear();
        this.children.add(this.leftButtonUp);
        this.children.add(this.leftButtonDown);
        this.children.add(this.rightButtonUp);
        this.children.add(this.rightButtonDown);
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        RowList choice = new RowList((STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT, 0);
        choice.add(fontRenderer.listFormattedStringToWidth(new TranslationTextComponent("betonQuestGui.closeGui").getFormattedText(), super.width / 3 - STRING_BORDER * 3 - BUTTON_DIMENSION));
        WidgetChoice playerChoice = new WidgetChoice(super.width / 3 * 2 + STRING_BORDER, super.height - STRING_HEIGHT, TextFormatting.YELLOW.getColor(), choice, super.width / 3 - STRING_BORDER * 3 - BUTTON_DIMENSION, ((STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT) * fontRenderer.FONT_HEIGHT,
                widgetChoice -> this.minecraft.displayGuiScreen(null)
        );
        this.rightRows.add(playerChoice);
        this.children.add(playerChoice);
    }

    public void appendToLeft(ITextComponent name, ITextComponent text)
    {
        if (!this.firstLeftLine) {
            this.leftText.appendSibling(new StringTextComponent("\n\n"));
        }
        this.firstLeftLine = false;
        if (this.lastPlayerChoice != null) {
            this.leftText.appendSibling(this.minecraft.player.getDisplayName().deepCopy().applyTextStyle(TextFormatting.BOLD).applyTextStyle(TextFormatting.YELLOW));
            this.leftText.appendSibling(this.lastPlayerChoice);
            this.leftText.appendSibling(new StringTextComponent("\n\n"));
        }
        this.leftText.appendSibling(name);
        this.leftText.appendSibling(text);
        this.updateLeftShift();
        this.reloadLeftRows();
    }

    private void updateLeftShift()
    {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        if (fontRenderer.listFormattedStringToWidth(this.leftText.getFormattedText(), super.width / 3 * 2 - STRING_BORDER * 3 - BUTTON_DIMENSION).size() > (STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT) {
            this.leftShift = fontRenderer.listFormattedStringToWidth(this.leftText.getFormattedText(), super.width / 3 * 2 - STRING_BORDER * 3 - BUTTON_DIMENSION).size() - (STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT;
        } else {
            this.leftShift = 0;
        }
    }

    public void appendToRight(IndexedChoice indexedChoice)
    {
        this.indexedChoices.add(indexedChoice);
        this.indexedChoices.sort(Comparator.comparingInt(IndexedChoice::getId));
        this.rightShift = 0;
        this.reloadRightRows();
    }

    public void updateNpcName(String npcName)
    {
        this.npcName = npcName;
        this.header = new WidgetText(new StringTextComponent(this.npcName).applyTextStyle(TextFormatting.BOLD).applyTextStyle(TextFormatting.GREEN), STRING_BORDER, super.height - NAME_STRING_HEIGHT);
    }

    public void resetRightList()
    {
        this.indexedChoices.clear();
    }

    private void reloadLeftRows()
    {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        this.leftRowList = new RowList((STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT, this.leftShift);
        this.leftRowList.add(fontRenderer.listFormattedStringToWidth(this.leftText.getFormattedText(), super.width / 3 * 2 - STRING_BORDER * 3 - BUTTON_DIMENSION));
        this.leftRows.clear();
        for (int index = 0, row = 0; index < this.leftRowList.getLinesAmount() && row < (STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT; index++, row++) {
            Row textRow = this.leftRowList.getRow(row);
            this.leftRows.add(new WidgetRow(STRING_BORDER, super.height - STRING_HEIGHT + row * fontRenderer.FONT_HEIGHT, textRow));
        }
        this.leftButtonUp.setVisible(this.leftRowList.canShiftUp());
        this.leftButtonDown.setVisible(this.leftRowList.canShiftDown());
    }

    private void reloadRightRows()
    {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        this.choices = new ChoiceList((STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT);
        for (int index = 0; index < this.indexedChoices.size(); index++) {
            RowList choice = new RowList((STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT, 0);
            choice.add(fontRenderer.listFormattedStringToWidth(this.indexedChoices.get(index).getText(), super.width / 3 - STRING_BORDER * 3 - BUTTON_DIMENSION));
            this.choices.add(choice);
        }
        this.choices.setAbsoluteShift(this.rightShift);
        this.choices.updateChoiceShift();
        this.rightRows.clear();
        this.children.clear();
        this.children.add(this.leftButtonUp);
        this.children.add(this.leftButtonDown);
        this.children.add(this.rightButtonUp);
        this.children.add(this.rightButtonDown);
        int verticalOffset = 0;
        if (this.choices.startWithBlankLine()) {
            // Blank line
            this.rightRows.add(new WidgetRow(
                    super.width / 3 * 2 + STRING_BORDER,
                    super.height - STRING_HEIGHT + fontRenderer.FONT_HEIGHT,
                    new Row("")
            ));
            verticalOffset += 1;
        }
        int choicesShift = this.choices.getChoicesShift();
        for (int index = 0, /* Lines amount of the partially hidden choice */ partiallyHiddenLines = 0;
             index < this.choices.getSize() && verticalOffset - partiallyHiddenLines < (STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT; index++) {
            RowList choice = this.choices.get(choicesShift + index);
            int id = choicesShift + index + 1;
            WidgetChoice playerChoice = new WidgetChoice(
                    super.width / 3 * 2 + STRING_BORDER,
                    super.height - STRING_HEIGHT + (verticalOffset - partiallyHiddenLines) * fontRenderer.FONT_HEIGHT,
                    TextFormatting.YELLOW.getColor(),
                    choice,
                    super.width / 3 - STRING_BORDER * 3 - BUTTON_DIMENSION,
                    ((STRING_HEIGHT - STRING_BORDER) / fontRenderer.FONT_HEIGHT - verticalOffset + partiallyHiddenLines) * fontRenderer.FONT_HEIGHT,
                    widgetChoice -> this.actionPerformedRowList(widgetChoice, id)
            );
            this.rightRows.add(playerChoice);
            this.children.add(playerChoice);
            // Blank line
            this.rightRows.add(new WidgetRow(
                    super.width / 3 * 2 + STRING_BORDER,
                    super.height - STRING_HEIGHT + (verticalOffset - partiallyHiddenLines + 1) * fontRenderer.FONT_HEIGHT,
                    new Row("")
            ));
            verticalOffset += choice.getLinesAmount() + 1;
            partiallyHiddenLines += choice.getShift();
        }
        this.rightButtonUp.setVisible(this.choices.canShiftUp());
        this.rightButtonDown.setVisible(this.choices.canShiftDown());
    }

    private void actionPerformedRowList(WidgetChoice widgetChoice, int id)
    {
        this.resetRightList();
        this.reloadRightRows();
        StringBuilder text = new StringBuilder();
        for (int counter = 0; counter < widgetChoice.getChoice().getLinesAmount(); counter++) {
            if (counter != 0) {
                text.append(" ");
            } else {
                text.append(": ");
            }
            text.append(widgetChoice.getChoice().getAbsoluteRow(counter).getText());
        }
        this.lastPlayerChoice = new StringTextComponent(text.toString());
        PacketHandler.INSTANCE.sendToServer(new PacketPlayerChoice(id));
    }
}
