package com.xiaohunao.heaven_destiny_moment.common.context;


import com.google.common.base.Function;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecExtra;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.IConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.IRewardContext;

import java.util.*;
import java.util.function.Consumer;

public record MomentDataContext(Optional<Integer> readyTime, Optional<Set<IRewardContext>> rewards, Optional<Set<IConditionContext>> conditions,
                                Optional<EntitySpawnSettingsContext> entitySpawnSettingsContext) {

    public static final Codec<MomentDataContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("ready_time").forGetter(MomentDataContext::readyTime),
            CodecExtra.setOf(IRewardContext.CODEC).optionalFieldOf("rewards").forGetter(MomentDataContext::rewards),
            CodecExtra.setOf(IConditionContext.CODEC).optionalFieldOf("conditions").forGetter(MomentDataContext::conditions),
            EntitySpawnSettingsContext.CODEC.optionalFieldOf("entity_spawn_settings").forGetter(MomentDataContext::entitySpawnSettingsContext)
    ).apply(instance, MomentDataContext::new));
    public static final MomentDataContext EMPTY = new MomentDataContext(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());


    public static class Builder {
        private Integer readyTime;
        private Set<IRewardContext> rewards;
        private Set<IConditionContext> conditions;
        private EntitySpawnSettingsContext mobSpawnSettingsContext;


        public MomentDataContext build() {
            return new MomentDataContext(Optional.ofNullable(readyTime),Optional.ofNullable(rewards),Optional.ofNullable(conditions),Optional.ofNullable(mobSpawnSettingsContext));
        }

        public Builder readyTime(int readyTime) {
            this.readyTime = readyTime;
            return this;
        }

        public Builder addReward(IRewardContext... reward) {
            if (rewards == null){
                rewards = Sets.newHashSet();
            }
            rewards.addAll(Set.of(reward));
            return this;
        }

        public Builder addCondition(IConditionContext... condition) {
            if (conditions == null){
                conditions = Sets.newHashSet();
            }
            conditions.addAll(Set.of(condition));
            return this;
        }

        public Builder mobSpawnSettings(Function<EntitySpawnSettingsContext.Builder,EntitySpawnSettingsContext.Builder> entitySpawnSettings){
            mobSpawnSettingsContext = entitySpawnSettings.apply(new EntitySpawnSettingsContext.Builder()).build();
            return this;
        }

    }
}
