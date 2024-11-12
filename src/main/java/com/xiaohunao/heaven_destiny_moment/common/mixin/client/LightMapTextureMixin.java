package com.xiaohunao.heaven_destiny_moment.common.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import com.xiaohunao.heaven_destiny_moment.common.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(LightTexture.class)
public abstract class LightMapTextureMixin {
    @Inject(method = "updateLightTexture", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/LightTexture;blockLightRedFlicker:F"))
    private void doOurLightMap(float p_109882_, CallbackInfo ci, @Local ClientLevel clientlevel, @Local Vector3f vector3f) {
        MomentManager momentManager = MomentManager.of(clientlevel);
        Minecraft minecraft = Minecraft.getInstance();
//        MomentInstance instance;
//        if (momentManager.getOnlyMoment() != null && !momentManager.getOnlyMoment().getMoment().getClientSettingsContext().isEmpty()){
//            instance = momentManager.getOnlyMoment();
//        }
        if (minecraft.player != null) {
            Collection<MomentInstance> playerMoments = momentManager.getPlayerMoment(minecraft.player.getUUID());

        }


    }
}