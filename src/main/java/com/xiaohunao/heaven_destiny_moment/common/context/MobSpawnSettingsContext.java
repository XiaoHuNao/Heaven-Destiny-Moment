package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;
import java.util.Map;

public record MobSpawnSettingsContext(boolean allowOriginalBiomeSpawnSettings, boolean forceSurfaceSpawning, boolean ignoreLightLevel,
                                      MobSpawnSettings spawnInfo, Map<MobCategory, Double> spawnCategoryMultiplier,
                                      MobSpawnListContext mobSpawnListContext) {
    //SpawnPlacements  这里寻找生物生成的规则

    public static final MobSpawnSettingsContext EMPTY = new MobSpawnSettingsContext(true,false,false,MobSpawnSettings.EMPTY, Maps.newHashMap(), MobSpawnListContext.EMPTY);

    public static final Codec<MobSpawnSettingsContext> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("allow_original_biome_spawn_settings",true).orElse(false).forGetter(MobSpawnSettingsContext::allowOriginalBiomeSpawnSettings),
                    Codec.BOOL.optionalFieldOf("force_surface_spawning",false).orElse(false).forGetter(MobSpawnSettingsContext::forceSurfaceSpawning),
                    Codec.BOOL.optionalFieldOf("ignoreLightLevel",false).orElse(false).forGetter(MobSpawnSettingsContext::ignoreLightLevel),
                    MobSpawnSettings.CODEC.codec().optionalFieldOf("mob_spawn_settings",MobSpawnSettings.EMPTY).forGetter(MobSpawnSettingsContext::spawnInfo),
                    Codec.unboundedMap(MobCategory.CODEC, Codec.DOUBLE).optionalFieldOf("spawn_category_multiplier",Maps.newHashMap()).forGetter(MobSpawnSettingsContext::spawnCategoryMultiplier),
                    MobSpawnListContext.CODEC.optionalFieldOf("mob_spawn_black_white_list_settings", MobSpawnListContext.EMPTY).forGetter(MobSpawnSettingsContext::mobSpawnListContext)
            ).apply(builder, MobSpawnSettingsContext::new)
    );

    public boolean isEmpty() {
        return this == EMPTY;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MobSpawnSettingsContext that = (MobSpawnSettingsContext) obj;

        if (allowOriginalBiomeSpawnSettings != that.allowOriginalBiomeSpawnSettings) return false;
        if (forceSurfaceSpawning != that.forceSurfaceSpawning) return false;
        if (ignoreLightLevel != that.ignoreLightLevel) return false;
        return spawnInfo.equals(that.spawnInfo);
    }

    public Double getSpawnMultiplier(MobCategory mobCategory) {
        return spawnCategoryMultiplier.getOrDefault(mobCategory, 1.0D);
    }

    public WeightedRandomList<MobSpawnSettings.SpawnerData> filterList(MobCategory mobCategory, List<MobSpawnSettings.SpawnerData> originalBiomeSpawnSettings) {
        originalBiomeSpawnSettings.addAll(spawnInfo.spawners.get(mobCategory).unwrap());

        if (mobSpawnListContext.isBlackList()) {
            originalBiomeSpawnSettings.removeIf(spawnerData -> mobSpawnListContext.contains(spawnerData.type));
        } else {
            originalBiomeSpawnSettings.removeIf(spawnerData -> !mobSpawnListContext.contains(spawnerData.type));
        }
        return WeightedRandomList.create(originalBiomeSpawnSettings);
    }
}
