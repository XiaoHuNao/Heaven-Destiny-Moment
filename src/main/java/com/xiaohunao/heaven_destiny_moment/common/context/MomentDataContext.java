package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecExtra;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.IConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.IEntityInfoContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.IRewardContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record MomentDataContext(int readyTime, Set<IRewardContext> rewards, Set<IConditionContext> conditions,
                                List<List<IEntityInfoContext>> waves, MobSpawnSettingsContext mobSpawnSettingsContext) {
    public static final MomentDataContext EMPTY = new MomentDataContext(0, Sets.newHashSet(), Sets.newHashSet(), Lists.newArrayList(), MobSpawnSettingsContext.EMPTY);

    public static final Codec<MomentDataContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("ready_time", 0).forGetter(MomentDataContext::readyTime),
            CodecExtra.setOf(IRewardContext.CODEC).optionalFieldOf("rewards", Sets.newHashSet()).forGetter(MomentDataContext::rewards),
            CodecExtra.setOf(IConditionContext.CODEC).optionalFieldOf("conditions", Sets.newHashSet()).forGetter(MomentDataContext::conditions),
            Codec.list(Codec.list(IEntityInfoContext.CODEC)).optionalFieldOf("waves", Lists.newArrayList()).forGetter(MomentDataContext::waves),
            MobSpawnSettingsContext.CODEC.optionalFieldOf("mob_spawn_settings", MobSpawnSettingsContext.EMPTY).forGetter(MomentDataContext::mobSpawnSettingsContext)
    ).apply(instance, MomentDataContext::new));





    public static class Builder {
        private int readyTime = 0;
        private Set<IRewardContext> rewards = Sets.newHashSet();
        private Set<IConditionContext> conditions = Sets.newHashSet();
        private List<List<IEntityInfoContext>> waves = Lists.newArrayList();
        private boolean allowOriginalBiomeSpawnSettings = false;
        private boolean forceSurfaceSpawning = false;
        private boolean ignoreLightLevel = false;
        private MobSpawnSettings.Builder mobSpawnSettings = new MobSpawnSettings.Builder();
        private Map<MobCategory, Double> spawnCategoryMultiplier = Maps.newHashMap();
        private MobSpawnListContext mobSpawnListContext = new MobSpawnListContext(Lists.newArrayList(), Lists.newArrayList(), true);
        private ClientSettingsContext.Builder clientSettingsContext = new ClientSettingsContext.Builder();


        public MomentDataContext build() {
            return new MomentDataContext(readyTime, rewards, conditions, waves, new MobSpawnSettingsContext(allowOriginalBiomeSpawnSettings, forceSurfaceSpawning, ignoreLightLevel, mobSpawnSettings.build(), spawnCategoryMultiplier, mobSpawnListContext));
        }

        public Builder addReward(IRewardContext... reward) {
            rewards.addAll(List.of(reward));
            return this;
        }

        public Builder addCondition(IConditionContext... condition) {
            conditions.addAll(List.of(condition));
            return this;
        }

        public Builder readyTime(int readyTime) {
            this.readyTime = readyTime;
            return this;
        }

        public Builder allowOriginalBiomeSpawnSettings(boolean allowOriginalBiomeSpawnSettings) {
            this.allowOriginalBiomeSpawnSettings = allowOriginalBiomeSpawnSettings;
            return this;
        }

        public Builder forceSurfaceSpawning(boolean forceSurfaceSpawning) {
            this.forceSurfaceSpawning = forceSurfaceSpawning;
            return this;
        }

        public Builder ignoreLightLevel() {
            this.ignoreLightLevel = true;
            return this;
        }

        public Builder addEntitySpawnInfo(MobCategory mobCategory, MobSpawnSettings.SpawnerData... spawnerData) {
            for (MobSpawnSettings.SpawnerData data : spawnerData) {
                mobSpawnSettings.addSpawn(mobCategory, data);
            }
            return this;
        }

        public Builder creatureGenerationProbability(float creatureGenerationProbability) {
            this.mobSpawnSettings.creatureGenerationProbability(creatureGenerationProbability);
            return this;
        }

        public Builder spawnMultiplier(MobCategory mobCategory, double multiplier) {
            this.spawnCategoryMultiplier.put(mobCategory, multiplier);
            return this;
        }

        public Builder addBlackWhiteListEntityType(EntityType<?> entityType) {
            this.mobSpawnListContext.addEntityType(entityType);
            return this;
        }

        public Builder addBlackWhiteListTagKey(TagKey<EntityType<?>> tagKey) {
            this.mobSpawnListContext.addTagKey(tagKey);
            return this;
        }

        public Builder switchListType() {
            this.mobSpawnListContext = this.mobSpawnListContext.switchListType();
            return this;
        }
    }
}
