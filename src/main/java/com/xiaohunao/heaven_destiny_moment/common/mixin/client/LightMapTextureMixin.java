package com.xiaohunao.heaven_destiny_moment.common.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import com.xiaohunao.heaven_destiny_moment.common.utils.ColorUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public abstract class LightMapTextureMixin {
    @Inject(method = "updateLightTexture", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/LightTexture;blockLightRedFlicker:F"))
    private void updateLightTexture(float p_109882_, CallbackInfo ci, @Local ClientLevel clientlevel, @Local Vector3f vector3f) {
        MomentManager momentManager = MomentManager.of(clientlevel);

        momentManager.getClientOnlyMoment()
                .flatMap(MomentInstance::getMoment)
                .map(Moment::getClientSettingsContext)
                .flatMap(ClientSettingsContext::environmentColor)
                .ifPresent(color -> vector3f.set(ColorUtils.colorToVector3f(color)));
    }
}