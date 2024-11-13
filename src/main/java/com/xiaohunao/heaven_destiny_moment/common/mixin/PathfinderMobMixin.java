package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.xiaohunao.heaven_destiny_moment.common.context.MobSpawnSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.server.level.ServerLevel;
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
        if (pLevel.isClientSide()){
            return;
        }

        MomentManager momentManager = MomentManager.of((ServerLevel) pLevel);
        PathfinderMob mob = (PathfinderMob)(Object)this;
        for (MomentInstance instance : momentManager.getRunMoments().values()) {
            instance.getMoment()
                    .filter(moment -> moment.isInArea((ServerLevel) pLevel,mob.blockPosition()))
                    .map(Moment::getMomentDataContext)
                    .flatMap(MomentDataContext::mobSpawnSettingsContext)
                    .flatMap(MobSpawnSettingsContext::ignoreLightLevel)
                    .ifPresent(cir::setReturnValue);
        }
    }
}