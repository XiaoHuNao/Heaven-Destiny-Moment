package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.context.MobSpawnSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;


@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {
    @Inject(method = "mobsAt", at = @At("RETURN"), cancellable = true)
    private static void mobsAt(ServerLevel serverLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, MobCategory mobCategory, BlockPos pos, Holder<Biome> biomeHolder, CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cir) {
        MomentInstance momentInstance = MomentCap.getCap(serverLevel).getLevelCoverageMomentMoment();
        if (momentInstance != null) {
            MobSpawnSettingsContext mobSpawnSettingsContext = momentInstance.getMoment().momentDataContext().mobSpawnSettingsContext();
            if (mobSpawnSettingsContext.allowOriginalBiomeSpawnSettings()) {
                List<MobSpawnSettings.SpawnerData> unwrap = new ArrayList<>(cir.getReturnValue().unwrap());
                cir.setReturnValue(mobSpawnSettingsContext.filterList(mobCategory,unwrap));
            }else {
                cir.setReturnValue(mobSpawnSettingsContext.spawnInfo().getMobs(mobCategory));
            }
        }
    }


    @Inject(method = "getRoughBiome", at = @At("RETURN"), cancellable = true)
    private static void getRoughBiome(BlockPos pos, ChunkAccess chunk, CallbackInfoReturnable<Biome> cir) {
        MomentInstance momentInstance = MomentCap.getCap(((LevelChunk) chunk).getLevel()).getLevelCoverageMomentMoment();
        if (momentInstance != null) {
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

            MobSpawnSettingsContext mobSpawnSettingsContext = momentInstance.getMoment().momentDataContext().mobSpawnSettingsContext();
            if (mobSpawnSettingsContext.allowOriginalBiomeSpawnSettings()) {
                MobSpawnSettings mobSettings = cir.getReturnValue().getMobSettings();
                Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners = Maps.newHashMap(mobSettings.spawners);
                MobSpawnSettings contextMobSpawnSettings = mobSpawnSettingsContext.spawnInfo();

                spawners.forEach(((mobCategory, weightedRandomList) -> {
                    List<MobSpawnSettings.SpawnerData> unwrap = Lists.newArrayList(weightedRandomList.unwrap());
                    spawners.put(mobCategory, mobSpawnSettingsContext.filterList(mobCategory,unwrap));
                }));

                Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts = Maps.newHashMap(mobSettings.mobSpawnCosts);
                mobSpawnCosts.forEach(((entityType, mobSpawnCost) -> {
                    mobSpawnCosts.put(entityType, contextMobSpawnSettings.mobSpawnCosts.get(entityType));
                }));

                float oldCreatureProbability = mobSettings.getCreatureProbability();
                float newCreatureProbability = mobSpawnSettingsContext.spawnInfo().getCreatureProbability();

                MobSpawnSettings mobSpawnSettings = new MobSpawnSettings(Math.max(oldCreatureProbability,newCreatureProbability), spawners, mobSpawnCosts);
                fakeBiome.mobSpawnSettings(mobSpawnSettings);
            } else {
                fakeBiome.mobSpawnSettings(mobSpawnSettingsContext.spawnInfo());
            }
            fakeBiome.generationSettings(BiomeGenerationSettings.EMPTY);
            cir.setReturnValue(fakeBiome.build());
        }
    }



    @Inject(method = "getRandomPosWithin", at = @At("RETURN"), cancellable = true)
    private static void forceSurface(Level level, LevelChunk levelChunk, CallbackInfoReturnable<BlockPos> cir) {
        BlockPos returnValue = cir.getReturnValue();
        Player closestPlayer = level.getNearestPlayer(returnValue.getX(), returnValue.getY(), returnValue.getZ(), -1.0, false);
        if (closestPlayer != null) {
            BlockPos closestPlayerPosition = closestPlayer.blockPosition();
            if (closestPlayerPosition.getY() > level.getHeight(Heightmap.Types.WORLD_SURFACE, closestPlayerPosition.getX(), closestPlayerPosition.getZ())) {
                BlockPos blockPos = new BlockPos(returnValue.getX(), level.getHeight(Heightmap.Types.WORLD_SURFACE, returnValue.getX(), returnValue.getZ()) + 1, returnValue.getZ());
                cir.setReturnValue(blockPos);
            }
        }
    }

    @ModifyReceiver(method = "spawnMobsForChunkGeneration",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/ServerLevelAccessor;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    private static ServerLevelAccessor spawnMobsForChunkGeneration(ServerLevelAccessor serverLevelAccessor, Entity entity) {
        if (entity instanceof LivingEntity livingEntity){
            MomentInstance momentInstance = MomentCap.getCap(serverLevelAccessor.getLevel()).getLevelCoverageMomentMoment();
            if (momentInstance != null) {
                momentInstance.finalizeSpawn(livingEntity);
            }
        }
        return serverLevelAccessor;
    }

}
