package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WeatherConditionContext extends ConditionContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("weather");
    public static final Codec<WeatherConditionContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("weather").forGetter(WeatherConditionContext::getWeather)
    ).apply(instance, WeatherConditionContext::new));
    private final String weather;

    public WeatherConditionContext(String weather) {
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }

    @Override
    public boolean canCreate(MomentInstance moment, Level level, BlockPos pos, Player player) {
        if (level.isRaining()) {
            return weather.equals("rain");
        } else if (level.isThundering()) {
            return weather.equals("thunder");
        } else {
            return weather.equals("clear");
        }
    }
    @Override
    public Codec<WeatherConditionContext> getCodec() {
        return CODEC;
    }
}
