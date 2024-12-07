package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record TimeConditionContext(Optional<Long> min,Optional<Long> max) implements IConditionContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("time");
    public static final MapCodec<TimeConditionContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.LONG.optionalFieldOf("min").forGetter(TimeConditionContext::min),
        Codec.LONG.optionalFieldOf("max").forGetter(TimeConditionContext::max)
    ).apply(instance, TimeConditionContext::new));

    public static TimeConditionContext exactly(long value) {
        return new TimeConditionContext(Optional.of(value), Optional.of(value));
    }

    public static TimeConditionContext between(long min, long max) {
        return new TimeConditionContext(Optional.of(min), Optional.of(max));
    }

    public static TimeConditionContext atLeast(long min) {
        return new TimeConditionContext(Optional.of(min), Optional.empty());
    }

    public static TimeConditionContext atMost(long max) {
        return new TimeConditionContext(Optional.empty(), Optional.of(max));
    }

    public boolean matches(long value) {

        if (this.min.isPresent() && this.min.get() > value) {
            return false;
        }
        return this.max.isEmpty() || this.max.get() > value;
    }

    @Override
    public boolean matches(MomentInstance instance, BlockPos pos) {
        Level level = instance.getLevel();
        return this.matches(level.getDayTime());
    }

    @Override
    public MapCodec<? extends IConditionContext> codec() {
        return HDMContextRegister.TIME_CONDITION.get();
    }

}
