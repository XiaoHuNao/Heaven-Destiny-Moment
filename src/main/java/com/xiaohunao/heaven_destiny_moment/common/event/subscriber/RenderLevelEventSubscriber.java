package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber
public class RenderLevelEventSubscriber {
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            return;
        }
        PoseStack matrixStack = event.getPoseStack();
        Camera camera = event.getCamera();
        Vec3 pos = camera.getPosition().reverse();
        matrixStack.pushPose();
        render(matrixStack, Minecraft.getInstance().renderBuffers().bufferSource(), pos);
        matrixStack.popPose();
    }
    public static void render(PoseStack matrixStack, MultiBufferSource.BufferSource bufferSource, Vec3 pos) {
        ClientLevel level = Minecraft.getInstance().level;
        LocalPlayer player = Minecraft.getInstance().player;
        if (level == null || player == null) {
            return;
        }
    }

}
