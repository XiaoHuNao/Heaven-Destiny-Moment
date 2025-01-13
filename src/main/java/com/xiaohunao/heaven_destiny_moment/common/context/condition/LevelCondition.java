package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.level.DifficultyCondition;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.level.TimeCondition;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record LevelCondition(Optional<DifficultyCondition> difficulty, Optional<TimeCondition> time) implements ICondition{
    public static final MapCodec<LevelCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DifficultyCondition.CODEC.codec().optionalFieldOf("difficulty").forGetter(LevelCondition::difficulty),
            TimeCondition.CODEC.codec().optionalFieldOf("time").forGetter(LevelCondition::time)
    ).apply(instance, LevelCondition::new));
    @Override
    public boolean matches(MomentInstance<?> instance,@Nullable BlockPos pos, @Nullable ServerPlayer serverPlayer) {
        return matchesCondition(difficulty,instance, pos, serverPlayer) &&
                matchesCondition(time,instance, pos, serverPlayer);
    }

    private boolean matchesCondition(Optional<? extends ICondition> condition, MomentInstance<?> instance, BlockPos pos, @Nullable ServerPlayer serverPlayer) {
        return condition.map(cond -> cond.matches(instance, pos, serverPlayer)).orElse(true);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return HDMContextRegister.LEVEL.get();
    }
}
