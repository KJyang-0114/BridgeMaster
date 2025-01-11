package com.autoblock.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import com.autoblock.AutoBlock;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AutoBlockConfig {
    private static Configuration config;
    
    // 延遲設置
    public static int minPlaceDelay;
    public static int maxPlaceDelay;
    public static boolean randomizeDelay;
    
    // 移動設置
    public static boolean smoothMovement;
    public static float movementRandomness;
    public static boolean autoSneak;
    
    // 視角設置
    public static float bridgePitch;
    public static float pitchRandomness;
    public static boolean smoothRotation;
    
    // 按鍵設置 (使用負數表示滑鼠按鍵，例如 -98 表示滑鼠按鍵2)
    public static int diagonalButton;
    public static int straightButton;
    public static int lockDirectionKey;
    
    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }
    
    public static void loadConfig() {
        // 延遲設置
        minPlaceDelay = config.getInt("最小放置延遲", "延遲設置", 1, 0, 20, "方塊放置的最小延遲（刻）");
        maxPlaceDelay = config.getInt("最大放置延遲", "延遲設置", 3, 0, 20, "方塊放置的最大延遲（刻）");
        randomizeDelay = config.getBoolean("隨機延遲", "延遲設置", true, "是否使用隨機延遲");
        
        // 移動設置
        smoothMovement = config.getBoolean("平滑移動", "移動設置", true, "啟用更自然的移動模式");
        movementRandomness = config.getFloat("移動隨機性", "移動設置", 0.2f, 0.0f, 1.0f, "移動的隨機性程度");
        autoSneak = config.getBoolean("自動潛行", "移動設置", true, "自動在邊緣潛行");
        
        // 視角設置
        bridgePitch = config.getFloat("搭橋視角", "視角設置", 81.5f, 0.0f, 90.0f, "搭橋時的俯視角度");
        pitchRandomness = config.getFloat("視角隨機性", "視角設置", 0.5f, 0.0f, 5.0f, "視角的隨機變化範圍");
        smoothRotation = config.getBoolean("平滑旋轉", "視角設置", true, "啟用平滑的視角轉動");
        
        // 按鍵設置
        diagonalButton = config.getInt("斜向按鍵", "按鍵設置", -96, -100, 255, "斜向搭橋的按鍵 (-96表示滑鼠側鍵4)");
        straightButton = config.getInt("直線按鍵", "按鍵設置", -95, -100, 255, "直線搭橋的按鍵 (-95表示滑鼠側鍵5)");
        lockDirectionKey = config.getInt("鎖定方向按鍵", "按鍵設置", Keyboard.KEY_R, 0, 255, "鎖定方向的按鍵 (默認為R)");
        
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    public static Configuration getConfig() {
        return config;
    }
} 