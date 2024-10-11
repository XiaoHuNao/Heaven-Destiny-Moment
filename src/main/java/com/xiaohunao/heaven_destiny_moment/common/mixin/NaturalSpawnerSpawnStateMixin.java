package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.SpawnState.class)
public class NaturalSpawnerSpawnStateMixin {
    @Shadow
    @Final
    private LocalMobCapCalculator localMobCapCalculator;

    @Shadow
    @Final
    private int spawnableChunkCount;

    @Shadow
    @Final
    private Object2IntOpenHashMap<MobCategory> mobCategoryCounts;

    @Inject(method = "canSpawnForCategory", at = @At("HEAD"), cancellable = true)
    private void modifySpawnCapByCategory(MobCategory mobCategory, ChunkPos chunkPos, CallbackInfoReturnable<Boolean> cir) {
        ServerLevel level = localMobCapCalculator.chunkMap.level;
        int maxInstancesPerChunk = mobCategory.getMaxInstancesPerChunk();
        int currentCount = this.mobCategoryCounts.getInt(mobCategory);
        MomentCap.getCap(level).ifPresent(momentCap -> {
            MomentInstance momentInstance = momentCap.getOnlyModifiedMobSpawnSettingsMoment();
            if (momentInstance != null) {
                double spawnMultiplier = momentInstance.getSpawnMultiplier(mobCategory);
                int maxLimit = (int) (maxInstancesPerChunk * (this.spawnableChunkCount * spawnMultiplier) / NaturalSpawner.MAGIC_NUMBER);
                if (currentCount >= maxLimit) {
                    cir.setReturnValue(false);
                } else {
                    cir.setReturnValue(this.localMobCapCalculator.canSpawn(mobCategory, chunkPos));
                }
            }
        });
    }
}
