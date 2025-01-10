package com.autoblock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

import com.autoblock.config.AutoBlockConfig;
import com.autoblock.util.MovementHelper;
import com.autoblock.gui.ModernGuiScreen;

@Mod(modid = AutoBlock.MODID, version = AutoBlock.VERSION, name = AutoBlock.NAME)
public class AutoBlock {
    public static final String MODID = "autoblock";
    public static final String VERSION = "3.0";
    public static final String NAME = "AutoBlock";

    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean wasOnEdge = false;
    private boolean isSneaking = false;
    private boolean isSpeedBridging = false;
    private float originalPitch = 0.0F;
    private KeyBinding speedBridgeKey;
    private KeyBinding openGuiKey;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AutoBlockConfig.init(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        
        // 註冊按鍵
        speedBridgeKey = new KeyBinding("Speed Bridge", AutoBlockConfig.straightButton, "AutoBlock");
        openGuiKey = new KeyBinding("Open Settings", Keyboard.KEY_P, "AutoBlock");
        
        ClientRegistry.registerKeyBinding(speedBridgeKey);
        ClientRegistry.registerKeyBinding(openGuiKey);
    }

    private boolean isKeyDown(KeyBinding binding) {
        if (binding.getKeyCode() >= 0) {
            return binding.isKeyDown();
        } else {
            int mouseButton = binding.getKeyCode() + 100;
            return mouseButton >= 0 && mouseButton < Mouse.getButtonCount() && Mouse.isButtonDown(mouseButton);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        
        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return;

        // 檢查是否打開GUI
        if (openGuiKey.isPressed()) {
            mc.displayGuiScreen(new ModernGuiScreen());
            return;
        }

        // 檢查是否手持方塊
        ItemStack heldItem = player.getHeldItem();
        boolean holdingBlock = heldItem != null && heldItem.getItem() instanceof ItemBlock;

        // 檢查是否在方塊邊緣
        boolean onEdge = isPlayerOnEdge();

        // 處理速疊邏輯
        if (isKeyDown(speedBridgeKey) && holdingBlock) {
            if (!isSpeedBridging) {
                isSpeedBridging = true;
                originalPitch = player.rotationPitch;
            }

            if (onEdge) {
                // 自動蹲下
                setSneaking(true);
                // 調整視角
                player.rotationPitch = 81.5F;
            } else {
                setSneaking(false);
            }
        } else {
            if (isSpeedBridging) {
                isSpeedBridging = false;
                setSneaking(false);
                player.rotationPitch = originalPitch;
            }
        }

        wasOnEdge = onEdge;
    }

    private boolean isPlayerOnEdge() {
        EntityPlayerSP player = mc.thePlayer;
        double relativeX = player.posX - Math.floor(player.posX);
        double relativeZ = player.posZ - Math.floor(player.posZ);
        return relativeX > 0.7 || relativeX < 0.3 || relativeZ > 0.7 || relativeZ < 0.3;
    }

    private void setSneaking(boolean sneak) {
        if (sneak != isSneaking) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), sneak);
            isSneaking = sneak;
        }
    }
}
