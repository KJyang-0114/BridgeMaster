package com.autoblock.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import com.autoblock.config.AutoBlockConfig;

public class MovementHelper {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static float targetYaw = 0.0F;
    private static float targetPitch = 0.0F;
    private static final float ROTATION_SPEED = 15.0F;

    public static void smoothRotation(float yaw, float pitch) {
        if (!AutoBlockConfig.smoothRotation) {
            setRotation(yaw, pitch);
            return;
        }

        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return;

        // 計算需要旋轉的角度
        float yawDiff = MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw);
        float pitchDiff = pitch - player.rotationPitch;

        // 平滑旋轉
        float rotationSpeed = ROTATION_SPEED;
        player.rotationYaw += MathHelper.clamp_float(yawDiff, -rotationSpeed, rotationSpeed);
        player.rotationPitch += MathHelper.clamp_float(pitchDiff, -rotationSpeed, rotationSpeed);

        // 確保pitch在有效範圍內
        player.rotationPitch = MathHelper.clamp_float(player.rotationPitch, -90.0F, 90.0F);
    }

    public static void setRotation(float yaw, float pitch) {
        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return;

        player.rotationYaw = yaw;
        player.rotationPitch = pitch;
    }

    public static float getRandomizedPitch() {
        float basePitch = AutoBlockConfig.bridgePitch;
        if (AutoBlockConfig.pitchRandomness > 0) {
            basePitch += (Math.random() - 0.5) * AutoBlockConfig.pitchRandomness;
        }
        return MathHelper.clamp_float(basePitch, 0.0F, 90.0F);
    }

    public static void applyRandomMovement() {
        if (!AutoBlockConfig.smoothMovement) return;

        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return;

        // 添加隨機移動
        if (Math.random() < AutoBlockConfig.movementRandomness) {
            // 隨機調整玩家的移動
            player.motionX += (Math.random() - 0.5) * 0.02;
            player.motionZ += (Math.random() - 0.5) * 0.02;
        }
    }
} 