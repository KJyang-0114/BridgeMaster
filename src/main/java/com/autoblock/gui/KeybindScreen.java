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
    private int lastMouseButton = -1;
    
    private GuiButton speedBridgeButton;
    private GuiButton backButton;
    
    public KeybindScreen(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
    
    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int startY = this.height / 4;
        int buttonWidth = 200;
        
        speedBridgeButton = new GuiButton(0, centerX - buttonWidth/2, startY,
            "速疊按鍵: " + getKeyDisplayName(AutoBlockConfig.straightButton));
        
        backButton = new GuiButton(1, centerX - buttonWidth/2, startY + 50, "返回");
        
        this.buttonList.add(speedBridgeButton);
        this.buttonList.add(backButton);
    }
    
    private String getKeyDisplayName(int keyCode) {
        if (keyCode < 0) {
            int mouseButton = keyCode + 100; // 轉換為滑鼠按鍵索引
            return "滑鼠按鍵 " + (mouseButton + 1);
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
            lastMouseButton = -1;
            button.displayString = "請按下按鍵...";
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (isBinding) {
            // 等待一小段時間後才開始檢測按鍵
            if (!hasStartedBinding && System.currentTimeMillis() - bindingStartTime > 500) {
                hasStartedBinding = true;
            }
            
            if (hasStartedBinding) {
                // 檢查滑鼠按鍵
                for (int i = 0; i < Mouse.getButtonCount(); i++) {
                    if (Mouse.isButtonDown(i)) {
                        if (lastMouseButton != i) {
                            lastMouseButton = i;
                            int mouseKeyCode = -100 + i; // 使用負數來表示滑鼠按鍵
                            handleKeyBinding(mouseKeyCode);
                            return;
                        }
                    }
                }
                lastMouseButton = -1;
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
        if (selectedButton == speedBridgeButton) {
            AutoBlockConfig.straightButton = keyCode;
        }
        
        isBinding = false;
        hasStartedBinding = false;
        updateButtonText();
        AutoBlockConfig.getConfig().save();
    }
    
    private void updateButtonText() {
        speedBridgeButton.displayString = "速疊按鍵: " + getKeyDisplayName(AutoBlockConfig.straightButton);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        
        String title = "按鍵設置";
        int titleWidth = fontRendererObj.getStringWidth(title);
        fontRendererObj.drawString(title, (width - titleWidth) / 2, 20, 0xFFFFFF);
        
        if (isBinding) {
            String helpText = hasStartedBinding ? 
                "請按下任意按鍵..." : 
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