package com.xiaohunao.heaven_destiny_moment.common.context.condition.level;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ICondition;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.Nullable;

public record DifficultyCondition(Difficulty difficulty) implements ICondition {
    public static final DifficultyCondition PEACEFUL = new DifficultyCondition(Difficulty.PEACEFUL);
    public static final DifficultyCondition EASY = new DifficultyCondition(Difficulty.EASY);
    public static final DifficultyCondition NORMAL = new DifficultyCondition(Difficulty.NORMAL);
    public static final DifficultyCondition HARD = new DifficultyCondition(Difficulty.HARD);

    public static final MapCodec<DifficultyCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Difficulty.CODEC.fieldOf("difficulty").forGetter(DifficultyCondition::difficulty)
    ).apply(instance, DifficultyCondition::new));
    @Override
    public boolean matches(MomentInstance<?> instance, @Nullable BlockPos pos, @Nullable ServerPlayer serverPlayer) {
        MinecraftServer server = instance.getLevel().getServer();
        if (server != null){
            return server.getWorldData().getDifficulty() == difficulty;
        }
        return false;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return HDMContextRegister.DIFFICULTY.get();
    }
}
