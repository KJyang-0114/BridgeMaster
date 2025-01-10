package com.autoblock.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class ModernGuiScreen extends GuiScreen {
    private GuiButton keybindButton;
    private GuiButton doneButton;

    @Override
    public void initGui() {
        this.buttonList.clear();
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // 按鍵設定按鈕
        keybindButton = new GuiButton(0, centerX - 100, centerY - 25, 200, 20, "按鍵設定");
        this.buttonList.add(keybindButton);

        // 完成按鈕
        doneButton = new GuiButton(1, centerX - 100, centerY + 25, 200, 20, "完成");
        this.buttonList.add(doneButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == keybindButton) {
            mc.displayGuiScreen(new KeybindScreen(this));
        } else if (button == doneButton) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        String title = "AutoBlock 設定";
        this.drawCenteredString(this.fontRendererObj, title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
} 