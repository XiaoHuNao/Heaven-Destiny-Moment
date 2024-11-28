package com.xiaohunao.heaven_destiny_moment.common.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

//SpawnPlacements  这里寻找生物生成的规则

public record MobSpawnRule(Optional<Boolean> allowOriginalBiomeSpawnSettings, Optional<Boolean> forceSurfaceSpawning,
                           Optional<Boolean> slimesSpawnEverywhere, Optional<Boolean> ignoreLightLevel, Optional<Boolean> ignoreDistance) {
    public static final Codec<MobSpawnRule> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("allow_original_biome_spawn_settings").forGetter(MobSpawnRule::allowOriginalBiomeSpawnSettings),
                    Codec.BOOL.optionalFieldOf("force_surface_spawning").forGetter(MobSpawnRule::forceSurfaceSpawning),
                    Codec.BOOL.optionalFieldOf("slimes_spawn_everywhere").forGetter(MobSpawnRule::slimesSpawnEverywhere),
                    Codec.BOOL.optionalFieldOf("ignoreLightLevel").forGetter(MobSpawnRule::ignoreLightLevel),
                    Codec.BOOL.optionalFieldOf("ignoreDistance").forGetter(MobSpawnRule::ignoreDistance)
            ).apply(builder, MobSpawnRule::new)
    );

    public static class Builder {
        private Boolean allowOriginalBiomeSpawnSettings;
        private Boolean forceSurfaceSpawning;
        private Boolean slimesSpawnEverywhere;
        private Boolean ignoreLightLevel;
        private Boolean ignoreDistance;

        public Builder allowOriginalBiomeSpawnSettings(boolean allowOriginalBiomeSpawnSettings) {
            this.allowOriginalBiomeSpawnSettings = allowOriginalBiomeSpawnSettings;
            return this;
        }

        public Builder forceSurfaceSpawning() {
            this.forceSurfaceSpawning = true;
            return this;
        }

        public Builder slimesSpawnEverywhere(){
            this.forceSurfaceSpawning = true;
            return this;
        }

        public Builder ignoreLightLevel() {
            this.ignoreLightLevel = true;
            return this;
        }
        public Builder ignoreDistance() {
            this.ignoreDistance = true;
            return this;
        }

        public MobSpawnRule build() {
            return new MobSpawnRule(Optional.ofNullable(allowOriginalBiomeSpawnSettings), Optional.ofNullable(forceSurfaceSpawning),
                    Optional.ofNullable(slimesSpawnEverywhere), Optional.ofNullable(ignoreLightLevel),
                    Optional.ofNullable(ignoreDistance)
            );
        }
    }
}
