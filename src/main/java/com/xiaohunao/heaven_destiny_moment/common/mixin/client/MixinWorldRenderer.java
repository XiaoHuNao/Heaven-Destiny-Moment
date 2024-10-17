package com.xiaohunao.heaven_destiny_moment.common.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientMoonSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.utils.ColorUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow
    @Final
    private static ResourceLocation MOON_LOCATION;

    @Shadow @Nullable private ClientLevel level;

    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F))
    private float modificationMoonSize(float originalSize) {
        MomentInstance momentInstance = MomentCap.getCap(level).getLevelCoverageMomentMoment();
        if (momentInstance != null){
            ClientMoonSettingsContext clientMoonSettingsContext = momentInstance.getMoment().clientSettingsContext().clientMoonSettingsContext();
            return clientMoonSettingsContext.MoonSize();
        }
        return originalSize;

    }
    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", ordinal = 1))
    private void bindCustomMoonTexture(int moonTextureId, ResourceLocation originaResourceLocation) {
        MomentInstance momentInstance = MomentCap.getCap(level).getLevelCoverageMomentMoment();
        if (momentInstance != null){
            ClientMoonSettingsContext clientMoonSettingsContext = momentInstance.getMoment().clientSettingsContext().clientMoonSettingsContext();
            RenderSystem.setShaderTexture(moonTextureId, clientMoonSettingsContext.MoonTexture());
        }

        RenderSystem.setShaderTexture(moonTextureId, originaResourceLocation);
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I"))
    private void changeMoonColor(PoseStack $$0, Matrix4f $$1, float partialTicks, Camera $$3, boolean $$4, Runnable $$5, CallbackInfo ci) {
        MomentInstance momentInstance = MomentCap.getCap(level).getLevelCoverageMomentMoment();
        if (momentInstance != null){
            ClientMoonSettingsContext clientMoonSettingsContext = momentInstance.getMoment().clientSettingsContext().clientMoonSettingsContext();
            int moonColor = clientMoonSettingsContext.moonColor();
            if (moonColor != -1){
                Vector3f color = ColorUtils.colorToVector3f(moonColor);
                RenderSystem.setShaderColor(color.x,color.y,color.z, 1.0F - level.getRainLevel(partialTicks));
            }
        }
    }
}