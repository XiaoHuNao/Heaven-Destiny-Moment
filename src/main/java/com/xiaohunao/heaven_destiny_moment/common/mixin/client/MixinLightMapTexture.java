package com.xiaohunao.heaven_destiny_moment.common.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.utils.ColorUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public abstract class MixinLightMapTexture {
    @Inject(method = "updateLightTexture", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/LightTexture;blockLightRedFlicker:F"))
    private void doOurLightMap(float p_109882_, CallbackInfo ci, @Local ClientLevel clientlevel, @Local Vector3f vector3f) {
        MomentInstance momentInstance = MomentCap.getCap(clientlevel).getLevelCoverageMomentMoment();
        if (momentInstance != null){
            ClientSettingsContext clientSettingsContext = momentInstance.getMoment().clientSettingsContext();
            int environmentColor = clientSettingsContext.environmentColor();
            if (environmentColor != -1) {
                Vector3f color = ColorUtils.colorToVector3f(environmentColor);
                vector3f.set(color);
            }
        }
    }
}