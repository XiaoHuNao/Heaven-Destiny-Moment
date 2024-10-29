package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TimeConditionContext extends ConditionContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("time");
    public static final Codec<TimeConditionContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("time").forGetter(TimeConditionContext::getTime),
            Codec.INT.fieldOf("flag").forGetter(TimeConditionContext::getFlag)
    ).apply(instance, TimeConditionContext::new));

    private static final BiMap<Integer, String> TIME_MAP = HashBiMap.create();

    private final int time;
    private int end = 0;
    private final int flag;

    public TimeConditionContext(int time, int flag) {
        this.time = time;
        this.flag = flag;
    }
    public TimeConditionContext(int start, int end, int flag) {
        this.time = start;
        this.end = end;
        this.flag = flag;
    }
    public TimeConditionContext(String time, int flag) {
        this.time = TIME_MAP.inverse().get(time);
        this.flag = flag;
    }
    public TimeConditionContext(String start, String end, int flag) {
        this.time = TIME_MAP.inverse().get(start);
        this.end = TIME_MAP.inverse().get(end);
        this.flag = flag;
    }

    public int getTime() {
        return time;
    }

    public int getFlag() {
        return flag;
    }

    @Override
    public boolean test(MomentInstance MomentInstance, Level level, BlockPos pos, Player player) {
        long dayTime = level.getDayTime() % 24000;
        return switch (flag) {
             case 0 -> dayTime == time;
             case 1 -> dayTime > time;
             case 2 -> dayTime < time;
             case 3 -> dayTime >= time;
             case 4 -> dayTime <= time;
             case 5 -> time >= dayTime && dayTime <= end;
            default -> false;
        };
    }

    @Override
    public Codec<? extends ConditionContext> getCodec() {
        return CODEC;
    }



    static {
        TIME_MAP.put(0, "Sunrise");
        TIME_MAP.put(1000, "Day");
        TIME_MAP.put(12000, "Sunset");
        TIME_MAP.put(23000, "Night");
        TIME_MAP.put(6000, "Midnight");
        TIME_MAP.put(14000, "FullMoon");
        TIME_MAP.put(38000, "WaningGibbous");
        TIME_MAP.put(62000, "ThirdQuarter");
        TIME_MAP.put(86000, "WaningCrescent");
        TIME_MAP.put(110000, "NewMoon");
        TIME_MAP.put(134000, "WaxingCrescent");
        TIME_MAP.put(158000, "FirstQuarter");
        TIME_MAP.put(182000, "WaxingGibbous");
    }
}
