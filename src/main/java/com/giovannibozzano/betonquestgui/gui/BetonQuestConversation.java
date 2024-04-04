package com.giovannibozzano.betonquestgui.gui;

import com.giovannibozzano.betonquestgui.gui.widgets.*;
import com.giovannibozzano.betonquestgui.network.PacketHandler;
import com.giovannibozzano.betonquestgui.network.packet.PacketCloseGui;
import com.giovannibozzano.betonquestgui.network.packet.PacketPlayerChoice;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
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
    private final List<Renderable> objects = new ArrayList<>();
    private final List<Renderable> leftRows = new ArrayList<>();
    private final List<Renderable> rightRows = new ArrayList<>();
    private final MutableComponent leftText = Component.literal("");
    private final List<IndexedChoice> indexedChoices = new ArrayList<>();
    private Renderable header;
    private String npcName = "LOADING...";
    private boolean allowClose;
    private Component lastPlayerChoice;
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
        super(Component.literal(""));
    }

    @Override
    public void init()
    {
        this.objects.clear();
        this.header = new WidgetText(Component.literal(this.npcName).setStyle(Style.EMPTY.applyFormat(ChatFormatting.BOLD).applyFormat(ChatFormatting.GREEN)), STRING_BORDER, super.height - NAME_STRING_HEIGHT);
        this.objects.add(new WidgetResizableDiv(this.width / 3 * 2, super.height - CONVERSATION_DIV_HEIGHT, super.width / 3, CONVERSATION_DIV_HEIGHT));
        this.objects.add(new WidgetResizableDiv(this.width / 3 * 2, super.height - NAME_DIV_START_HEIGHT, super.width / 3, NAME_DIV_HEIGHT));
        this.objects.add(new WidgetResizableDiv(0, this.height - CONVERSATION_DIV_HEIGHT, super.width / 3 * 2, CONVERSATION_DIV_HEIGHT));
        this.objects.add(new WidgetResizableDiv(0, this.height - NAME_DIV_START_HEIGHT, super.width / 3 * 2, NAME_DIV_HEIGHT));
        this.objects.add(new WidgetText(this.minecraft.player.getDisplayName().copy().setStyle(Style.EMPTY.applyFormat(ChatFormatting.BOLD).applyFormat(ChatFormatting.YELLOW)), super.width / 3 * 2 + STRING_BORDER, super.height - NAME_STRING_HEIGHT));

        this.leftButtonUp = new WidgetButton(
                this.width / 3 * 2 - BUTTON_DIMENSION - STRING_BORDER,
                this.height - CONVERSATION_DIV_HEIGHT + STRING_BORDER,
                BUTTON_DIMENSION, BUTTON_DIMENSION,
                elementButton ->
                {
                    if (this.leftRowList.canShiftUp()) {
                        this.leftShift--;
                        this.reloadLeftRows();
                    }
                }, 1);
        this.leftButtonDown = new WidgetButton(this.width / 3 * 2 - BUTTON_DIMENSION - STRING_BORDER,
                this.height - BUTTON_DIMENSION - STRING_BORDER,
                BUTTON_DIMENSION, BUTTON_DIMENSION,
                elementButton ->
                {
                    if (this.leftRowList.canShiftDown()) {
                        this.leftShift++;
                        this.reloadLeftRows();
                    }
                }, 0);
        this.rightButtonUp = new WidgetButton(this.width - BUTTON_DIMENSION - STRING_BORDER,
                this.height - CONVERSATION_DIV_HEIGHT + STRING_BORDER,
                BUTTON_DIMENSION, BUTTON_DIMENSION,
                elementButton ->
                {
                    if (this.choices.canShiftUp()) {
                        this.rightShift--;
                        this.reloadRightRows();
                    }
                }, 1);
        this.rightButtonDown = new WidgetButton(this.width - BUTTON_DIMENSION - STRING_BORDER,
                this.height - BUTTON_DIMENSION - STRING_BORDER,
                BUTTON_DIMENSION, BUTTON_DIMENSION,
                elementButton ->
                {
                    if (this.choices.canShiftDown()) {
                        this.rightShift++;
                        this.reloadRightRows();
                    }
                }, 0);

        this.objects.add(this.leftButtonUp);
        this.objects.add(this.leftButtonDown);
        this.objects.add(this.rightButtonUp);
        this.objects.add(this.rightButtonDown);
        this.addWidget(this.leftButtonUp);
        this.addWidget(this.leftButtonDown);
        this.addWidget(this.rightButtonUp);
        this.addWidget(this.rightButtonDown);

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
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick)
    {
        this.renderBackground(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, pPartialTick);

        for (Renderable object : this.objects) {
            object.render(guiGraphics, mouseX, mouseY, pPartialTick);
        }
        for (Renderable leftRow : this.leftRows) {
            leftRow.render(guiGraphics, mouseX, mouseY, pPartialTick);
        }
        for (Renderable rightRow : this.rightRows) {
            rightRow.render(guiGraphics, mouseX, mouseY, pPartialTick);
        }

        this.header.render(guiGraphics, mouseX, mouseY, pPartialTick);
    }

    public void allowClose()
    {
        this.allowClose = true;
        this.rightShift = 0;
        this.rightRows.clear();
        this.children().clear();
        this.addWidget(this.leftButtonUp);
        this.addWidget(this.leftButtonDown);
        this.addWidget(this.rightButtonUp);
        this.addWidget(this.rightButtonDown);
        Font fontRenderer = Minecraft.getInstance().font;
        StringSplitter characterManager = fontRenderer.getSplitter();
        RowList choice = new RowList((STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight, 0);
        choice.add(characterManager.splitLines(Component.translatable("betonQuestGui.closeGui"), super.width / 3 - STRING_BORDER * 3 - BUTTON_DIMENSION, Style.EMPTY));
        WidgetChoice playerChoice = new WidgetChoice( super.width / 3 * 2 + STRING_BORDER, super.height - STRING_HEIGHT, ChatFormatting.YELLOW.getColor(), choice, super.width / 3 - STRING_BORDER * 3 - BUTTON_DIMENSION, ((STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight) * fontRenderer.lineHeight,
                widgetChoice -> this.minecraft.setScreen(null)
        );
        this.rightRows.add(playerChoice);
        this.addWidget(playerChoice);
    }

    public void appendToLeft(MutableComponent name, MutableComponent text)
    {
        if (!this.firstLeftLine) {
            this.leftText.append(Component.literal("\n\n"));
        }
        this.firstLeftLine = false;
        if (this.lastPlayerChoice != null) {
            this.leftText.append(this.minecraft.player.getDisplayName().copy().setStyle(Style.EMPTY.applyFormat(ChatFormatting.BOLD).applyFormat(ChatFormatting.YELLOW)));
            this.leftText.append(this.lastPlayerChoice);
            this.leftText.append(Component.literal("\n\n"));
        }
        this.leftText.append(name);
        this.leftText.append(text);
        this.updateLeftShift();
        this.reloadLeftRows();
    }

    private void updateLeftShift()
    {
        Font fontRenderer = Minecraft.getInstance().font;
        StringSplitter characterManager = fontRenderer.getSplitter();
        if (characterManager.splitLines(this.leftText, super.width / 3 * 2 - STRING_BORDER * 3 - BUTTON_DIMENSION, Style.EMPTY).size() > (STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight) {
            this.leftShift = fontRenderer.split(this.leftText, super.width / 3 * 2 - STRING_BORDER * 3 - BUTTON_DIMENSION).size() - (STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight;
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
        this.header = new WidgetText(Component.literal(this.npcName).withStyle(Style.EMPTY.applyFormat(ChatFormatting.BOLD).applyFormat(ChatFormatting.GREEN)), STRING_BORDER, super.height - NAME_STRING_HEIGHT);
    }

    public void resetRightList()
    {
        this.indexedChoices.clear();
    }

    private void reloadLeftRows()
    {
        Font fontRenderer = Minecraft.getInstance().font;
        StringSplitter characterManager = fontRenderer.getSplitter();
        this.leftRowList = new RowList((STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight, this.leftShift);
        this.leftRowList.add(characterManager.splitLines(this.leftText, super.width / 3 * 2 - STRING_BORDER * 3 - BUTTON_DIMENSION, Style.EMPTY));
        this.leftRows.clear();
        for (int index = 0, row = 0; index < this.leftRowList.getLinesAmount() && row < (STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight; index++, row++) {
            Row textRow = this.leftRowList.getRow(row);
            this.leftRows.add(new WidgetRow(STRING_BORDER, super.height - STRING_HEIGHT + row * fontRenderer.lineHeight, textRow));
        }
        this.leftButtonUp.setVisible(this.leftRowList.canShiftUp());
        this.leftButtonDown.setVisible(this.leftRowList.canShiftDown());
    }

    private void reloadRightRows()
    {
        Font fontRenderer = Minecraft.getInstance().font;
        StringSplitter characterManager = fontRenderer.getSplitter();
        this.choices = new ChoiceList((STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight);
        for (IndexedChoice indexedChoice : this.indexedChoices) {
            RowList choice = new RowList((STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight, 0);
            choice.add(characterManager.splitLines(Component.literal(indexedChoice.getText()), super.width / 3 - STRING_BORDER * 3 - BUTTON_DIMENSION, Style.EMPTY));
            this.choices.add(choice);
        }
        this.choices.setAbsoluteShift(this.rightShift);
        this.choices.updateChoiceShift();
        this.rightRows.clear();
        this.children().clear();
        this.addWidget(this.leftButtonUp);
        this.addWidget(this.leftButtonDown);
        this.addWidget(this.rightButtonUp);
        this.addWidget(this.rightButtonDown);
        int verticalOffset = 0;
        if (this.choices.startWithBlankLine()) {
            // Blank line
            this.rightRows.add(new WidgetRow(super.width / 3 * 2 + STRING_BORDER,
                    super.height - STRING_HEIGHT + fontRenderer.lineHeight,
                    new Row(Component.literal(""))
            ));
            verticalOffset += 1;
        }
        int choicesShift = this.choices.getChoicesShift();
        for (int index = 0, /* Lines amount of the partially hidden choice */ partiallyHiddenLines = 0;
             index < this.choices.getSize() && verticalOffset - partiallyHiddenLines < (STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight; index++) {
            RowList choice = this.choices.get(choicesShift + index);
            int id = choicesShift + index + 1;
            WidgetChoice playerChoice = new WidgetChoice(super.width / 3 * 2 + STRING_BORDER,
                    super.height - STRING_HEIGHT + (verticalOffset - partiallyHiddenLines) * fontRenderer.lineHeight,
                    ChatFormatting.YELLOW.getColor(),
                    choice,
                    super.width / 3 - STRING_BORDER * 3 - BUTTON_DIMENSION,
                    ((STRING_HEIGHT - STRING_BORDER) / fontRenderer.lineHeight - verticalOffset + partiallyHiddenLines) * fontRenderer.lineHeight,
                    widgetChoice -> this.actionPerformedRowList(widgetChoice, id)
            );
            this.rightRows.add(playerChoice);
            this.addWidget(playerChoice);
            // Blank line
            this.rightRows.add(new WidgetRow(super.width / 3 * 2 + STRING_BORDER,
                    super.height - STRING_HEIGHT + (verticalOffset - partiallyHiddenLines + 1) * fontRenderer.lineHeight,
                    new Row(Component.literal(""))
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
            text.append(widgetChoice.getChoice().getAbsoluteRow(counter).getText().getString());
        }
        this.lastPlayerChoice = Component.literal(text.toString());
        PacketHandler.INSTANCE.sendToServer(new PacketPlayerChoice(id));
    }
}
