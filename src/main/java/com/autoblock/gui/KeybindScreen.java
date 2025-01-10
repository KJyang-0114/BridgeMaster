package com.autoblock.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.autoblock.config.AutoBlockConfig;

import java.io.IOException;

public class KeybindScreen extends GuiScreen {
    private final GuiScreen parentScreen;
    private GuiButton selectedButton;
    private boolean isBinding = false;
    private boolean hasStartedBinding = false;
    private long bindingStartTime;
    
    private GuiButton diagonalKeyButton;
    private GuiButton straightKeyButton;
    private GuiButton lockKeyButton;
    private GuiButton backButton;
    
    public KeybindScreen(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
    
    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int startY = this.height / 4;
        int buttonWidth = 200;
        int buttonHeight = 20;
        
        diagonalKeyButton = new GuiButton(0, centerX - buttonWidth/2, startY,
            "斜向搭橋: " + getKeyDisplayName(AutoBlockConfig.diagonalButton));
        
        straightKeyButton = new GuiButton(1, centerX - buttonWidth/2, startY + 25,
            "直線搭橋: " + getKeyDisplayName(AutoBlockConfig.straightButton));
        
        lockKeyButton = new GuiButton(2, centerX - buttonWidth/2, startY + 50,
            "鎖定方向: " + Keyboard.getKeyName(AutoBlockConfig.lockDirectionKey));
        
        backButton = new GuiButton(3, centerX - buttonWidth/2, startY + 100, "返回");
        
        this.buttonList.add(diagonalKeyButton);
        this.buttonList.add(straightKeyButton);
        this.buttonList.add(lockKeyButton);
        this.buttonList.add(backButton);
    }
    
    private String getKeyDisplayName(int keyCode) {
        if (keyCode < 0) {
            int mouseButton = keyCode + 100; // 轉換為滑鼠按鍵索引
            return "滑鼠" + (mouseButton + 1);
        }
        return Keyboard.getKeyName(keyCode);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == backButton) {
            mc.displayGuiScreen(parentScreen);
            return;
        }
        
        if (!isBinding) {
            selectedButton = button;
            isBinding = true;
            hasStartedBinding = false;
            bindingStartTime = System.currentTimeMillis();
            button.displayString = "請按下按鍵或滑鼠按鍵...";
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (isBinding) {
            // 等待一小段時間後才開始檢測按鍵
            if (!hasStartedBinding && System.currentTimeMillis() - bindingStartTime > 500) {
                hasStartedBinding = true;
                // 清除所有按鍵狀態
                while (Mouse.next()) {}
                while (Keyboard.next()) {}
            }
            
            if (hasStartedBinding) {
                // 檢查滑鼠按鍵
                for (int i = 0; i < 5; i++) {
                    if (Mouse.isButtonDown(i)) {
                        int mouseKeyCode = -100 + i; // 使用負數來表示滑鼠按鍵
                        handleKeyBinding(mouseKeyCode);
                        return;
                    }
                }
            }
        }
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (isBinding) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                isBinding = false;
                hasStartedBinding = false;
                updateButtonText();
                return;
            }
            if (hasStartedBinding) {
                handleKeyBinding(keyCode);
            }
        } else if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }
    }
    
    private void handleKeyBinding(int keyCode) {
        switch (selectedButton.id) {
            case 0:
                AutoBlockConfig.diagonalButton = keyCode;
                break;
            case 1:
                AutoBlockConfig.straightButton = keyCode;
                break;
            case 2:
                if (keyCode >= 0) { // 只允許鍵盤按鍵用於鎖定方向
                    AutoBlockConfig.lockDirectionKey = keyCode;
                }
                break;
        }
        
        isBinding = false;
        hasStartedBinding = false;
        updateButtonText();
        AutoBlockConfig.getConfig().save();
    }
    
    private void updateButtonText() {
        diagonalKeyButton.displayString = "斜向搭橋: " + getKeyDisplayName(AutoBlockConfig.diagonalButton);
        straightKeyButton.displayString = "直線搭橋: " + getKeyDisplayName(AutoBlockConfig.straightButton);
        lockKeyButton.displayString = "鎖定方向: " + Keyboard.getKeyName(AutoBlockConfig.lockDirectionKey);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        
        String title = "按鍵設置";
        int titleWidth = fontRendererObj.getStringWidth(title);
        fontRendererObj.drawString(title, (width - titleWidth) / 2, 20, 0xFFFFFF);
        
        if (isBinding) {
            String helpText = hasStartedBinding ? 
                "按下任意按鍵或滑鼠按鍵..." : 
                "請稍等...";
            int helpWidth = fontRendererObj.getStringWidth(helpText);
            fontRendererObj.drawString(helpText, (width - helpWidth) / 2, height - 40, 0xFFFFFF);
        }
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
} 