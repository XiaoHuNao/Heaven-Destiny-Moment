package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.LocationConditionContext;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record MobSpawnSettingsContext(Optional<Boolean> allowOriginalBiomeSpawnSettings, Optional<Boolean> forceSurfaceSpawning,Optional<Boolean> slimesSpawnEverywhere, Optional<Boolean> ignoreLightLevel,
                                      Optional<MobSpawnSettings> spawnInfo, Optional<Map<MobCategory, Double>> spawnCategoryMultiplier,
                                      Optional<MobSpawnListContext> mobSpawnListContext) {
    //SpawnPlacements  这里寻找生物生成的规则

    public static final MobSpawnSettingsContext EMPTY = new MobSpawnSettingsContext(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());

    public static final Codec<MobSpawnSettingsContext> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("allow_original_biome_spawn_settings").forGetter(MobSpawnSettingsContext::allowOriginalBiomeSpawnSettings),
                    Codec.BOOL.optionalFieldOf("force_surface_spawning").forGetter(MobSpawnSettingsContext::forceSurfaceSpawning),
                    Codec.BOOL.optionalFieldOf("slimes_spawn_everywhere").forGetter(MobSpawnSettingsContext::slimesSpawnEverywhere),
                    Codec.BOOL.optionalFieldOf("ignoreLightLevel").forGetter(MobSpawnSettingsContext::ignoreLightLevel),
                    MobSpawnSettings.CODEC.codec().optionalFieldOf("mob_spawn_settings").forGetter(MobSpawnSettingsContext::spawnInfo),
                    Codec.unboundedMap(MobCategory.CODEC, Codec.DOUBLE).optionalFieldOf("spawn_category_multiplier").forGetter(MobSpawnSettingsContext::spawnCategoryMultiplier),
                    MobSpawnListContext.CODEC.optionalFieldOf("mob_spawn_black_white_list_settings").forGetter(MobSpawnSettingsContext::mobSpawnListContext)
            ).apply(builder, MobSpawnSettingsContext::new)
    );

    public boolean isEmpty() {
        return this == EMPTY;
    }



    public WeightedRandomList<MobSpawnSettings.SpawnerData> filterList(MobCategory mobCategory, List<MobSpawnSettings.SpawnerData> originalBiomeSpawnSettings) {
        spawnInfo.ifPresent(mobSpawnSettings -> {
            originalBiomeSpawnSettings.addAll(mobSpawnSettings.spawners.get(mobCategory).unwrap());

            mobSpawnListContext.ifPresent(mobSpawnListContext -> {
                if (mobSpawnListContext.isBlackList()) {
                    originalBiomeSpawnSettings.removeIf(spawnerData -> mobSpawnListContext.contains(spawnerData.type));
                } else {
                    originalBiomeSpawnSettings.removeIf(spawnerData -> !mobSpawnListContext.contains(spawnerData.type));
                }
            });
        });
        return WeightedRandomList.create(originalBiomeSpawnSettings);
    }

    public static class Builder {
        private Optional<Boolean> allowOriginalBiomeSpawnSettings = Optional.empty();
        private Optional<Boolean> forceSurfaceSpawning = Optional.empty();
        private Optional<Boolean> slimesSpawnEverywhere = Optional.empty();
        private Optional<Boolean> ignoreLightLevel = Optional.empty();
        private Optional<MobSpawnSettings> spawnInfo = Optional.empty();
        private Optional<Map<MobCategory, Double>> spawnCategoryMultiplier = Optional.empty();
        private Optional<MobSpawnListContext> mobSpawnListContext = Optional.empty();


        public static Builder settings() {
            return new Builder();
        }

        public Builder allowOriginalBiomeSpawnSettings() {
            this.allowOriginalBiomeSpawnSettings = Optional.of(true);
            return this;
        }

        public Builder forceSurfaceSpawning() {
            this.forceSurfaceSpawning = Optional.of(true);
            return this;
        }

        public Builder slimesSpawnEverywhere(){
            this.forceSurfaceSpawning = Optional.of(true);
        }

        public Builder ignoreLightLevel() {
            this.ignoreLightLevel = Optional.of(true);
            return this;
        }

        public Builder spawnInfo(MobSpawnSettings spawnInfo) {
            this.spawnInfo = Optional.of(spawnInfo);
            return this;
        }

        public Builder spawnCategoryMultiplier(Map<MobCategory, Double> spawnCategoryMultiplier) {
            this.spawnCategoryMultiplier = Optional.of(spawnCategoryMultiplier);
            return this;
        }

        public Builder mobSpawnListContext(MobSpawnListContext mobSpawnListContext) {
            this.mobSpawnListContext = Optional.of(mobSpawnListContext);
            return this;
        }

        public MobSpawnSettingsContext build() {
            return new MobSpawnSettingsContext(allowOriginalBiomeSpawnSettings, forceSurfaceSpawning, slimesSpawnEverywhere,
                    ignoreLightLevel, spawnInfo, spawnCategoryMultiplier, mobSpawnListContext);
        }
    }


}
