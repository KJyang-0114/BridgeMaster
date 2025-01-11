package com.autoblock.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import com.autoblock.AutoBlock;

import java.util.ArrayList;
import java.util.List;

public class AutoBlockGuiConfig extends GuiConfig {
    public AutoBlockGuiConfig(GuiScreen parent) {
        super(parent,
                getConfigElements(),
                AutoBlock.MODID,
                false,
                false,
                "AutoBlock 設置");
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        
        // 添加所有配置類別
        list.add(new ConfigElement(AutoBlockConfig.getConfig().getCategory("延遲設置")));
        list.add(new ConfigElement(AutoBlockConfig.getConfig().getCategory("移動設置")));
        list.add(new ConfigElement(AutoBlockConfig.getConfig().getCategory("視角設置")));
        list.add(new ConfigElement(AutoBlockConfig.getConfig().getCategory("按鍵設置")));
        
        return list;
    }
} 