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

import java.util.Random;

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
    private float originalYaw = 0.0F;
    private float targetPitch = 0.0F;
    private float targetYaw = 0.0F;
    private KeyBinding speedBridgeKey;
    private KeyBinding openGuiKey;
    private long lastClickTime = 0;
    private Random random = new Random();
    private float smoothness = 0.3F; // 視角平滑度 (0-1)
    private float randomRange = 0.8F; // 視角隨機範圍改為±0.8度
    private boolean isJumping = false;
    private static final int NORMAL_MIN_CPS = 8;  // 正常時最小CPS
    private static final int NORMAL_MAX_CPS = 16; // 正常時最大CPS
    private static final int JUMP_MIN_CPS = 12;   // 跳疊時的最小CPS
    private static final int JUMP_MAX_CPS = 18;   // 跳疊時的最大CPS

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AutoBlockConfig.init(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        
        speedBridgeKey = new KeyBinding("Speed Bridge", -98, "AutoBlock"); // -98 代表滑鼠側鍵3
        openGuiKey = new KeyBinding("Open Settings", Keyboard.KEY_P, "AutoBlock");
        
        ClientRegistry.registerKeyBinding(speedBridgeKey);
        ClientRegistry.registerKeyBinding(openGuiKey);
    }

    private boolean isKeyDown(int keyCode) {
        if (keyCode >= 0) {
            return Keyboard.isKeyDown(keyCode);
        } else {
            int mouseButton = keyCode + 100;
            return mouseButton >= 0 && mouseButton < Mouse.getButtonCount() && Mouse.isButtonDown(mouseButton);
        }
    }

    private float getOptimalPitch(float yaw) {
        // 將視角規範化到 0-360 度
        yaw = yaw % 360;
        if (yaw < 0) yaw += 360;

        float basePitch = 79.5F; // 基礎視角改為79.5度
        
        // 添加小幅隨機偏移
        return basePitch + (random.nextFloat() * 2 - 1) * randomRange;
    }

    private float getOptimalYaw(float currentYaw) {
        // 將視角規範化到 -180 到 180 度
        currentYaw = currentYaw % 360;
        if (currentYaw > 180) currentYaw -= 360;
        if (currentYaw < -180) currentYaw += 360;

        // 定義四個目標角度
        float[] targetAngles = {45, -45, 135, -135};  // 北、西、東、南
        float minDiff = Float.MAX_VALUE;
        float bestAngle = 45;  // 預設值

        // 尋找最接近的角度
        for (float angle : targetAngles) {
            float diff = Math.abs(currentYaw - angle);
            // 處理角度循環的情況
            if (diff > 180) {
                diff = 360 - diff;
            }
            if (diff < minDiff) {
                minDiff = diff;
                bestAngle = angle;
            }
        }

        return bestAngle;
    }

    private void smoothRotateTo(EntityPlayerSP player, float targetPitch, float targetYaw) {
        float currentPitch = player.rotationPitch;
        float currentYaw = player.rotationYaw;
        
        // 將當前yaw規範化到 -180 到 180 度
        currentYaw = currentYaw % 360;
        if (currentYaw > 180) currentYaw -= 360;
        if (currentYaw < -180) currentYaw += 360;
        
        // 計算pitch差值
        float pitchDiff = targetPitch - currentPitch;
        
        // 計算yaw差值（考慮360度循環）
        float yawDiff = targetYaw - currentYaw;
        if (yawDiff > 180) yawDiff -= 360;
        if (yawDiff < -180) yawDiff += 360;
        
        // 平滑過渡
        player.rotationPitch += pitchDiff * smoothness;
        player.rotationYaw += yawDiff * smoothness;
    }

    private void simulateRightClick() {
        long currentTime = System.currentTimeMillis();
        
        // 根據是否在跳躍決定CPS範圍
        int minCps = isJumping ? JUMP_MIN_CPS : NORMAL_MIN_CPS;
        int maxCps = isJumping ? JUMP_MAX_CPS : NORMAL_MAX_CPS;
        
        int minDelay = 1000 / maxCps;
        int maxDelay = 1000 / minCps;
        int randomDelay = minDelay + random.nextInt(maxDelay - minDelay);
        
        if (currentTime - lastClickTime >= randomDelay) {
            KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            lastClickTime = currentTime;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        
        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return;

        if (openGuiKey.isPressed()) {
            mc.displayGuiScreen(new ModernGuiScreen());
            return;
        }

        ItemStack heldItem = player.getHeldItem();
        boolean holdingBlock = heldItem != null && heldItem.getItem() instanceof ItemBlock;
        boolean onEdge = isPlayerOnEdge();
        boolean speedBridgeActive = isKeyDown(speedBridgeKey.getKeyCode());

        // 更新跳躍狀態
        isJumping = !player.onGround;

        if (speedBridgeActive && holdingBlock) {
            if (!isSpeedBridging) {
                isSpeedBridging = true;
                originalPitch = player.rotationPitch;
                originalYaw = player.rotationYaw;
                targetPitch = getOptimalPitch(player.rotationYaw);
                targetYaw = getOptimalYaw(player.rotationYaw);
            }

            // 只在邊緣時自動蹲下
            if (onEdge) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                isSneaking = true;
            } else if (isSneaking) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                isSneaking = false;
            }
            
            // 平滑視角調整
            smoothRotateTo(player, targetPitch, targetYaw);
            
            // 每隔一段時間更新目標視角，產生微小移動
            if (random.nextInt(20) == 0) {
                targetPitch = getOptimalPitch(player.rotationYaw);
            }
            
            // 持續點擊右鍵
            simulateRightClick();
        } else {
            if (isSpeedBridging) {
                isSpeedBridging = false;
                if (isSneaking) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                    isSneaking = false;
                }
                // 平滑返回原始視角
                smoothRotateTo(player, originalPitch, originalYaw);
            }
        }

        wasOnEdge = onEdge;
    }

    private boolean isPlayerOnEdge() {
        EntityPlayerSP player = mc.thePlayer;
        BlockPos pos = new BlockPos(player.posX, player.posY - 0.5, player.posZ);
        return !mc.theWorld.getBlockState(pos).getBlock().isFullBlock() &&
               (player.posX - Math.floor(player.posX) > 0.7 || 
                player.posX - Math.floor(player.posX) < 0.3 ||
                player.posZ - Math.floor(player.posZ) > 0.7 || 
                player.posZ - Math.floor(player.posZ) < 0.3);
    }
}
