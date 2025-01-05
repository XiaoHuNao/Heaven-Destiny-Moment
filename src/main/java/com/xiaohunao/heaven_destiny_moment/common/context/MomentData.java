package com.xiaohunao.heaven_destiny_moment.common.context;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.IReward;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public record MomentData(Optional<List<IReward>> rewards, Optional<ConditionGroup> conditionGroup,
                         Optional<EntitySpawnSettings> entitySpawnSettings) {

    public static final Codec<MomentData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(IReward.CODEC).optionalFieldOf("rewards").forGetter(MomentData::rewards),
            ConditionGroup.CODEC.optionalFieldOf("conditionGroup").forGetter(MomentData::conditionGroup),
            EntitySpawnSettings.CODEC.optionalFieldOf("entity_spawn_settings").forGetter(MomentData::entitySpawnSettings)

    ).apply(instance, MomentData::new));
    public static final MomentData EMPTY = new MomentData(Optional.empty(),Optional.empty(),Optional.empty());


    public static class Builder {
        private List<IReward> rewards;
        private ConditionGroup conditionGroup;
        private EntitySpawnSettings entitySpawnSettings;


        public MomentData build() {
            return new MomentData(Optional.ofNullable(rewards),Optional.ofNullable(conditionGroup),Optional.ofNullable(entitySpawnSettings));
        }

        public Builder addReward(IReward... reward) {
            if (rewards == null){
                rewards = Lists.newArrayList();
            }
            rewards.addAll(Set.of(reward));
            return this;
        }

        public Builder conditionGroup(Function<ConditionGroup.Builder,ConditionGroup.Builder> conditionGroup) {
            this.conditionGroup = conditionGroup.apply(new ConditionGroup.Builder()).build();
            return this;
        }

        public Builder entitySpawnSettings(Function<EntitySpawnSettings.Builder, EntitySpawnSettings.Builder> entitySpawnSettings){
            this.entitySpawnSettings = entitySpawnSettings.apply(new EntitySpawnSettings.Builder()).build();
            return this;
        }

    }
}
