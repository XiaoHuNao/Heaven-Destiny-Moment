package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.xiaohunao.heaven_destiny_moment.common.context.BiomeEntitySpawnSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.EntitySpawnSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LocalMobCapCalculator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalMobCapCalculator.class)
public class LocalMobCapCalculatorMixin {
    @Shadow
    @Final
    public ChunkMap chunkMap;

    @Redirect(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LocalMobCapCalculator$MobCounts;canSpawn(Lnet/minecraft/world/entity/MobCategory;)Z"))
    private boolean canSpawn(LocalMobCapCalculator.MobCounts mobCounts, MobCategory mobCategory) {
        MomentManager momentManager = MomentManager.of(chunkMap.level);
        for (MomentInstance instance : momentManager.getRunMoments().values()) {
            instance.getMoment()
                    .map(Moment::getMomentDataContext)
                    .flatMap(MomentDataContext::entitySpawnSettingsContext)
                    .flatMap(EntitySpawnSettingsContext::biomeEntitySpawnSettings)
                    .flatMap(BiomeEntitySpawnSettings::spawnCategoryMultiplier)
                    .map(multiplierMap -> {
                        Object2IntMap<MobCategory> counts = mobCounts.counts;
                        final int currentCount = counts.getOrDefault(mobCategory, 0);
                        double maxLimit = mobCategory.getMaxInstancesPerChunk() * multiplierMap.getOrDefault(mobCategory, 1.0);
                        return currentCount < maxLimit;
                    });
        }
        return mobCounts.canSpawn(mobCategory);
    }
}
