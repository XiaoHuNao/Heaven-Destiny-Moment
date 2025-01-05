package com.xiaohunao.heaven_destiny_moment.common.spawn_algorithm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public record OpenAreaSpawnAlgorithm(int attempt, int spawnRange) implements ISpawnAlgorithm {
    public static final OpenAreaSpawnAlgorithm DEFAULT = new OpenAreaSpawnAlgorithm(10, 32);

    public static final MapCodec<OpenAreaSpawnAlgorithm> CODEC =  RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("attempt").forGetter(OpenAreaSpawnAlgorithm::attempt),
            Codec.INT.fieldOf("spawnRange").forGetter(OpenAreaSpawnAlgorithm::spawnRange)
    ).apply(instance, OpenAreaSpawnAlgorithm::new));


    @Override
    public Vec3 spawn(MomentInstance<?> momentInstance, Entity entity) {
        Level level = momentInstance.getLevel();
        Vec3 pos = momentInstance.getRandomSpawnPos();

        for (int i = 0; i < attempt; i++) {

            double x = pos.x() + (level.random.nextDouble() - level.random.nextDouble()) * spawnRange + 0.5D;
            double y = pos.y();
            double z = pos.z() + (level.random.nextDouble() - level.random.nextDouble()) * spawnRange + 0.5D;

            while (level.getBlockState(BlockPos.containing(x, y - 1, z)).isAir() && y > level.getMinBuildHeight()) {
                y--;
            }

            while (!noBlockCollision(level, getAABB(entity, x, y, z))) {
                y++;
            }

            if (noBlockCollision(level, getAABB(entity, x, y, z))) return new Vec3(x, y, z);
        }
        return null;
    }

    @Override
    public MapCodec<? extends ISpawnAlgorithm> codec() {
        return HDMContextRegister.OPEN_AREA_SPAWN_ALGORITHM.get();
    }
}
