package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecExtra;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.IConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.IEntityInfoContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.IRewardContext;

import java.util.*;

public record MomentDataContext(Optional<Integer> readyTime, Optional<Set<IRewardContext>> rewards, Optional<Set<IConditionContext>> conditions,
                                Optional<List<List<IEntityInfoContext>>> waves, Optional<MobSpawnSettingsContext> mobSpawnSettingsContext) {

    public static final Codec<MomentDataContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("ready_time").forGetter(MomentDataContext::readyTime),
            CodecExtra.setOf(IRewardContext.CODEC).optionalFieldOf("rewards").forGetter(MomentDataContext::rewards),
            CodecExtra.setOf(IConditionContext.CODEC).optionalFieldOf("conditions").forGetter(MomentDataContext::conditions),
            Codec.list(Codec.list(IEntityInfoContext.CODEC)).optionalFieldOf("waves").forGetter(MomentDataContext::waves),
            MobSpawnSettingsContext.CODEC.optionalFieldOf("mob_spawn_settings").forGetter(MomentDataContext::mobSpawnSettingsContext)
    ).apply(instance, MomentDataContext::new));
    public static final MomentDataContext EMPTY = new MomentDataContext(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());


    public static class Builder {
        private Optional<Integer> readyTime = Optional.empty();
        private Optional<Set<IRewardContext>> rewards = Optional.empty();
        private Optional<Set<IConditionContext>> conditions = Optional.empty();
        private Optional<List<List<IEntityInfoContext>>> waves = Optional.empty();
        private Optional<MobSpawnSettingsContext> mobSpawnSettingsContext = Optional.empty();


        public MomentDataContext build() {
            return new MomentDataContext(readyTime,rewards,conditions,waves,mobSpawnSettingsContext);
        }

        public Builder readyTime(int readyTime) {
            this.readyTime = Optional.of(readyTime);
            return this;
        }

        public Builder addReward(IRewardContext... reward) {
            if (rewards.isEmpty()){
                rewards = Optional.of(Sets.newHashSet());
            }
            rewards.get().addAll(List.of(reward));
            return this;
        }

        public Builder addCondition(IConditionContext... condition) {
            if (conditions.isEmpty()){
                conditions = Optional.of(Sets.newHashSet());
            }
            conditions.get().addAll(List.of(condition));
            return this;
        }
        public Builder addEntity(IEntityInfoContext... wave) {
            if (waves.isEmpty()){
                waves = Optional.of(Lists.newArrayList());
            }
            waves.get().add(List.of(wave));
            return this;
        }

        public Builder mobSpawnSettings(MobSpawnSettingsContext context){
            mobSpawnSettingsContext = Optional.of(context);
            return this;
        }



//
//
//        public Builder allowOriginalBiomeSpawnSettings(boolean allowOriginalBiomeSpawnSettings) {
//            this.allowOriginalBiomeSpawnSettings = Optional.of(allowOriginalBiomeSpawnSettings);
//            return this;
//        }
//
//        public Builder forceSurfaceSpawning(boolean forceSurfaceSpawning) {
//            this.forceSurfaceSpawning = forceSurfaceSpawning;
//            return this;
//        }
//
//        public Builder ignoreLightLevel() {
//            this.ignoreLightLevel = true;
//            return this;
//        }
//
//        public Builder addEntitySpawnInfo(MobCategory mobCategory, MobSpawnSettings.SpawnerData... spawnerData) {
//            for (MobSpawnSettings.SpawnerData data : spawnerData) {
//                mobSpawnSettings.addSpawn(mobCategory, data);
//            }
//            return this;
//        }
//
//        public Builder creatureGenerationProbability(float creatureGenerationProbability) {
//            this.mobSpawnSettings.creatureGenerationProbability(creatureGenerationProbability);
//            return this;
//        }
//
//        public Builder spawnMultiplier(MobCategory mobCategory, double multiplier) {
//            this.spawnCategoryMultiplier.put(mobCategory, multiplier);
//            return this;
//        }
//
//        public Builder addBlackWhiteListEntityType(EntityType<?> entityType) {
//            this.mobSpawnListContext.addEntityType(entityType);
//            return this;
//        }
//
//        public Builder addBlackWhiteListTagKey(TagKey<EntityType<?>> tagKey) {
//            this.mobSpawnListContext.addTagKey(tagKey);
//            return this;
//        }
//
//        public Builder switchListType() {
//            this.mobSpawnListContext = this.mobSpawnListContext.switchListType();
//            return this;
//        }
    }
}
