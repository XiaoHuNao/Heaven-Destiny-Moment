package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PathfinderMob.class)
public class PathfinderMobMixin {
    @Inject(method = "checkSpawnRules", at = @At(value = "HEAD", ordinal = 0), remap = true, cancellable = true)
    public void checkSpawnRules(LevelAccessor pLevel, MobSpawnType pSpawnReason, CallbackInfoReturnable<Boolean> cir) {
        MomentInstance momentInstance = MomentCap.getCap((Level) pLevel).getLevelCoverageMomentMoment();
        if (momentInstance != null) {
            boolean ignoreLightLevel = momentInstance.getMoment().momentDataContext().mobSpawnSettingsContext().ignoreLightLevel();
            if (ignoreLightLevel) {
                cir.setReturnValue(true);
            }
        }
    }
}