package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
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

    private static final BiMap<Long, String> TIME_MAP = HashBiMap.create();

    public static TimeConditionContext exactly(long value) {
        return new TimeConditionContext(Optional.of(value), Optional.of(value));
    }
    public static TimeConditionContext exactly(String value) {
        return TimeConditionContext.exactly(TIME_MAP.inverse().get(value));
    }

    public static TimeConditionContext between(long min, long max) {
        return new TimeConditionContext(Optional.of(min), Optional.of(max));
    }
    public static TimeConditionContext between(String min, String max) {
        return TimeConditionContext.between(TIME_MAP.inverse().get(min), TIME_MAP.inverse().get(max));
    }

    public static TimeConditionContext atLeast(long min) {
        return new TimeConditionContext(Optional.of(min), Optional.empty());
    }
    public static TimeConditionContext atLeast(String min) {
        return TimeConditionContext.atLeast(TIME_MAP.inverse().get(min));
    }

    public static TimeConditionContext atMost(long max) {
        return new TimeConditionContext(Optional.empty(), Optional.of(max));
    }
    public static TimeConditionContext atMost(String max) {
        return TimeConditionContext.atMost(TIME_MAP.inverse().get(max));
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
        return ModContextRegister.TIME_CONDITION.get();
    }


    static {
        TIME_MAP.put(0L, "Sunrise");
        TIME_MAP.put(1000L, "Day");
        TIME_MAP.put(12000L, "Sunset");
        TIME_MAP.put(23000L, "Night");
        TIME_MAP.put(6000L, "Midnight");
        TIME_MAP.put(14000L, "FullMoon");
        TIME_MAP.put(38000L, "WaningGibbous");
        TIME_MAP.put(62000L, "ThirdQuarter");
        TIME_MAP.put(86000L, "WaningCrescent");
        TIME_MAP.put(110000L, "NewMoon");
        TIME_MAP.put(134000L, "WaxingCrescent");
        TIME_MAP.put(158000L, "FirstQuarter");
        TIME_MAP.put(182000L, "WaxingGibbous");
    }
}
