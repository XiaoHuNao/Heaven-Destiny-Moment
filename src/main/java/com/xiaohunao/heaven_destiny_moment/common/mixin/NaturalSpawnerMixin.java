package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.xiaohunao.heaven_destiny_moment.common.context.BiomeEntitySpawnSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.EntitySpawnSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MobSpawnRule;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;


@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {
    @Inject(method = "mobsAt", at = @At("RETURN"), cancellable = true)
    private static void mobsAt(ServerLevel serverLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, MobCategory mobCategory, BlockPos pos, Holder<Biome> biomeHolder, CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cir) {
        MomentManager momentManager = MomentManager.of(serverLevel);
        for (MomentInstance instance : momentManager.getRunMoments().values()) {
            instance.getMoment()
                    .filter(moment -> moment.isInArea(serverLevel, pos))
                    .map(Moment::getMomentDataContext)
                    .flatMap(MomentDataContext::entitySpawnSettingsContext)
                    .ifPresent(entitySpawnSettingsContext -> {
                        List<MobSpawnSettings.SpawnerData> unwrap = new ArrayList<>(cir.getReturnValue().unwrap());
                        cir.setReturnValue(entitySpawnSettingsContext.adjustmentBiomeEntitySpawnSettings(mobCategory,unwrap));
                    });
        }
    }


    @Inject(method = "getRoughBiome", at = @At("RETURN"), cancellable = true)
    private static void getRoughBiome(BlockPos pos, ChunkAccess chunk, CallbackInfoReturnable<Biome> cir) {
        Level level = ((LevelChunk) chunk).getLevel();
        if (level.isClientSide) {
            return;
        }
        MomentManager momentManager = MomentManager.of(level);
        Biome.BiomeBuilder fakeBiome = new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(1)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build()
                );

        for (MomentInstance instance : momentManager.getRunMoments().values()) {
            instance.getMoment()
                    .filter(moment -> moment.isInArea((ServerLevel) level, pos))
                    .map(Moment::getMomentDataContext)
                    .flatMap(MomentDataContext::entitySpawnSettingsContext)
                    .ifPresent(entitySpawnSettingsContext -> {
                        MobSpawnSettings mobSettings = cir.getReturnValue().getMobSettings();
                        entitySpawnSettingsContext.biomeEntitySpawnSettings().flatMap(BiomeEntitySpawnSettings::biomeMobSpawnSettings).ifPresent(mobSpawnSettings ->{
                            Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners = Maps.newHashMap(mobSettings.spawners);
                            Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> newSpawners = Maps.newHashMap();
                            for (Map.Entry<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> entry : spawners.entrySet()) {
                                MobCategory mobCategory = entry.getKey();
                                WeightedRandomList<MobSpawnSettings.SpawnerData> weightedRandomList = entry.getValue();
                                List<MobSpawnSettings.SpawnerData> unwrap = Lists.newArrayList(weightedRandomList.unwrap());
                                newSpawners.put(mobCategory,entitySpawnSettingsContext.adjustmentBiomeEntitySpawnSettings(mobCategory, unwrap));
                            }


                            Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts = Maps.newHashMap(mobSettings.mobSpawnCosts);
                            for (Map.Entry<EntityType<?>, MobSpawnSettings.MobSpawnCost> entry : mobSpawnCosts.entrySet()) {
                                EntityType<?> entityType = entry.getKey();
                                MobSpawnSettings.MobSpawnCost mobSpawnCost = entry.getValue();
                                mobSpawnCosts.put(entityType, mobSpawnSettings.mobSpawnCosts.get(entityType));
                            }


                            float oldCreatureProbability = mobSettings.getCreatureProbability();
                            float newCreatureProbability = mobSpawnSettings.getCreatureProbability();
                            MobSpawnSettings newMobSpawnSettings = new MobSpawnSettings(Math.max(oldCreatureProbability, newCreatureProbability), newSpawners, mobSpawnCosts);
                            fakeBiome.mobSpawnSettings(newMobSpawnSettings);
                        });

                        if (fakeBiome.mobSpawnSettings == null){
                            fakeBiome.mobSpawnSettings(mobSettings);
                        }


                        fakeBiome.generationSettings(BiomeGenerationSettings.EMPTY);
                        cir.setReturnValue(fakeBiome.build());
                    });

        }
    }

    @Inject(method = "getRandomPosWithin", at = @At("RETURN"), cancellable = true)
    private static void forceSurface(Level level, LevelChunk levelChunk, CallbackInfoReturnable<BlockPos> cir) {
        if (level.isClientSide){
            return;
        }

        BlockPos returnValue = cir.getReturnValue();
        MomentManager momentManager = MomentManager.of(level);
        for (MomentInstance instance : momentManager.getRunMoments().values()) {
            instance.getMoment()
                    .filter(moment -> moment.isInArea((ServerLevel) level, returnValue))
                    .map(Moment::getMomentDataContext)
                    .flatMap(MomentDataContext::entitySpawnSettingsContext)
                    .flatMap(EntitySpawnSettingsContext::rule)
                    .flatMap(MobSpawnRule::forceSurfaceSpawning)
                    .ifPresent(mobSpawnSettingsContext -> {
                        Player closestPlayer = level.getNearestPlayer(returnValue.getX(), returnValue.getY(), returnValue.getZ(), -1.0, false);
                        if (closestPlayer != null) {
                            BlockPos closestPlayerPosition = closestPlayer.blockPosition();
                            if (closestPlayerPosition.getY() > level.getHeight(Heightmap.Types.WORLD_SURFACE, closestPlayerPosition.getX(), closestPlayerPosition.getZ())) {
                                cir.setReturnValue(new BlockPos(returnValue.getX(), level.getHeight(Heightmap.Types.WORLD_SURFACE, returnValue.getX(), returnValue.getZ()) + 1, returnValue.getZ()));
                            }
                        }

                    });
        }
    }

    @ModifyReceiver(method = "spawnMobsForChunkGeneration", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/ServerLevelAccessor;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    private static ServerLevelAccessor spawnMobsForChunkGeneration(ServerLevelAccessor serverLevelAccessor, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            MomentManager momentManager = MomentManager.of(serverLevelAccessor.getLevel());
            for (MomentInstance instance : momentManager.getRunMoments().values()) {
                instance.getMoment()
                        .filter(moment -> moment.isInArea(serverLevelAccessor.getLevel(), entity.blockPosition()))
                        .ifPresent(moment -> {
                            instance.finalizeSpawn(livingEntity);
                        });

            }
        }
        return serverLevelAccessor;
    }
}
