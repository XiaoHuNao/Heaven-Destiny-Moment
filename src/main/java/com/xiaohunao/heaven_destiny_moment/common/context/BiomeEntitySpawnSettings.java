package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record BiomeEntitySpawnSettings(Optional<MobSpawnSettings> biomeMobSpawnSettings, Optional<Map<MobCategory, Double>> spawnCategoryMultiplier, Optional<EntitySpawnListContext> entitySpawnListContext) {
    public static final Codec<BiomeEntitySpawnSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    MobSpawnSettings.CODEC.codec().optionalFieldOf("biome_mob_spawn_settings").forGetter(BiomeEntitySpawnSettings::biomeMobSpawnSettings),
                    Codec.unboundedMap(MobCategory.CODEC, Codec.DOUBLE).optionalFieldOf("spawn_category_multiplier").forGetter(BiomeEntitySpawnSettings::spawnCategoryMultiplier),
                    EntitySpawnListContext.CODEC.optionalFieldOf("entitySpawnListContext").forGetter(BiomeEntitySpawnSettings::entitySpawnListContext)
            ).apply(builder, BiomeEntitySpawnSettings::new)
    );


    public static class Builder {
        private MobSpawnSettings biomeMobSpawnSettings;
        private Map<MobCategory, Double> spawnCategoryMultiplier;
        private EntitySpawnListContext entitySpawnListContext;

        public BiomeEntitySpawnSettings build() {
            return new BiomeEntitySpawnSettings(Optional.ofNullable(biomeMobSpawnSettings), Optional.ofNullable(spawnCategoryMultiplier),Optional.ofNullable(entitySpawnListContext));
        }

        public Builder biomeMobSpawnSettings(Function<MobSpawnSettings.Builder,MobSpawnSettings.Builder> biomeMobSpawnSettings) {
            this.biomeMobSpawnSettings = biomeMobSpawnSettings.apply(new MobSpawnSettings.Builder()).build();
            return this;
        }

        public Builder spawnCategoryMultiplier(MobCategory category, Double multiplier) {
            if (spawnCategoryMultiplier == null){
               this.spawnCategoryMultiplier = Maps.newHashMap();
            }
            this.spawnCategoryMultiplier.put(category,multiplier);
            return this;
        }

        public Builder entitySpawnListContext(Function<EntitySpawnListContext.Builder,EntitySpawnListContext.Builder> entitySpawnListContext) {
            this.entitySpawnListContext = entitySpawnListContext.apply(new EntitySpawnListContext.Builder()).build();
            return this;
        }

    }
}
