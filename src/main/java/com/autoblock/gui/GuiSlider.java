package com.autoblock.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiSlider extends GuiButton {
    private float sliderValue;
    private boolean dragging;
    private final double minValue;
    private final double maxValue;
    private final String prefix;
    private final String suffix;
    private final boolean drawString;
    
    public GuiSlider(int id, int x, int y, int width, int height, String prefix, String suffix,
                     double minValue, double maxValue, double currentValue, boolean drawString) {
        super(id, x, y, width, height, "");
        this.prefix = prefix;
        this.suffix = suffix;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.drawString = drawString;
        
        this.sliderValue = (float)((currentValue - minValue) / (maxValue - minValue));
        this.displayString = getDisplayString();
    }
    
    private String getDisplayString() {
        return prefix + Math.round(getValue() * 100.0) / 100.0 + suffix;
    }
    
    public double getValue() {
        return minValue + (maxValue - minValue) * sliderValue;
    }
    
    @Override
    protected int getHoverState(boolean mouseOver) {
        return 0;
    }
    
    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                
                if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
                }
                
                if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
                }
                
                this.displayString = getDisplayString();
            }
            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(
                this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)),
                this.yPosition,
                0,
                66,
                4,
                20
            );
            this.drawTexturedModalRect(
                this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4,
                this.yPosition,
                196,
                66,
                4,
                20
            );
        }
    }
    
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            
            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }
            
            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }
            
            this.displayString = getDisplayString();
            this.dragging = true;
            return true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }
} 