package com.xiaohunao.heaven_destiny_moment.common.context;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ICondition;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.IReward;

import java.util.*;

public record MomentData(Optional<Integer> readyTime, Optional<HashSet<IReward>> rewards, Optional<HashSet<ICondition>> conditions,
                         Optional<EntitySpawnSettings> entitySpawnSettingsContext) {

    public static final Codec<MomentData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("ready_time").forGetter(MomentData::readyTime),
            Codec.list(IReward.CODEC).xmap(Sets::newHashSet, Lists::newArrayList).optionalFieldOf("rewards").forGetter(MomentData::rewards),
            Codec.list(ICondition.CODEC).xmap(Sets::newHashSet, Lists::newArrayList).optionalFieldOf("conditions").forGetter(MomentData::conditions),
            EntitySpawnSettings.CODEC.optionalFieldOf("entity_spawn_settings").forGetter(MomentData::entitySpawnSettingsContext)
    ).apply(instance, MomentData::new));
    public static final MomentData EMPTY = new MomentData(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());


    public static class Builder {
        private Integer readyTime;
        private HashSet<IReward> rewards;
        private HashSet<ICondition> conditions;
        private EntitySpawnSettings mobSpawnSettingsContext;


        public MomentData build() {
            return new MomentData(Optional.ofNullable(readyTime),Optional.ofNullable(rewards),Optional.ofNullable(conditions),Optional.ofNullable(mobSpawnSettingsContext));
        }

        public Builder readyTime(int readyTime) {
            this.readyTime = readyTime;
            return this;
        }

        public Builder addReward(IReward... reward) {
            if (rewards == null){
                rewards = Sets.newHashSet();
            }
            rewards.addAll(Set.of(reward));
            return this;
        }

        public Builder addCondition(ICondition... condition) {
            if (conditions == null){
                conditions = Sets.newHashSet();
            }
            conditions.addAll(Set.of(condition));
            return this;
        }

        public Builder mobSpawnSettings(Function<EntitySpawnSettings.Builder, EntitySpawnSettings.Builder> entitySpawnSettings){
            mobSpawnSettingsContext = entitySpawnSettings.apply(new EntitySpawnSettings.Builder()).build();
            return this;
        }

    }
}
