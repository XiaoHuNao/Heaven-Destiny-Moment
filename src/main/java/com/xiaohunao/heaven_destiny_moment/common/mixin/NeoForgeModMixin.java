package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.xiaohunao.heaven_destiny_moment.common.event.RegisterCallbackEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NeoForgeMod.class)
public class NeoForgeModMixin {
    @Inject(method = "<init>", at = @At("TAIL"),remap = false)
    private void init(CallbackInfo ci) {
        NeoForge.EVENT_BUS.start();
        NeoForge.EVENT_BUS.post(new RegisterCallbackEvent());
    }
}
