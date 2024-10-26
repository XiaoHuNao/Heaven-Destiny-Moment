package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.xiaohunao.heaven_destiny_moment.common.mixed.MomentManagerContainer;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(Level.class)
public abstract class LevelMixin implements MomentManagerContainer {
    @Unique
    private MomentManager heaven_destiny_moment$momentManager;

    @Override
    public MomentManager heaven_destiny_moment$getMomentManager() {
        return heaven_destiny_moment$momentManager;
    }

    @Override
    public void heaven_destiny_moment$setMomentManager(MomentManager momentManager) {
        this.heaven_destiny_moment$momentManager = momentManager;
    }

    @Inject(at = @At("HEAD"), method = "close")
    private void heaven_destiny_moment$onClose(CallbackInfo ci) {
        heaven_destiny_moment$momentManager = null;
    }
}