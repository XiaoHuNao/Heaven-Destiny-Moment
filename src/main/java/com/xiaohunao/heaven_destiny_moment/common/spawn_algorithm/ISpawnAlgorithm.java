package com.xiaohunao.heaven_destiny_moment.common.spawn_algorithm;

import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;

public interface ISpawnAlgorithm {
    BlockPos spawn(MomentInstance<?> momentInstance, LevelChunk chunk);



    ISpawnAlgorithm VANILLA_NATURAL_SPAWNER = SpawnAlgorithms::vanillaNaturalSpawner;

    class SpawnAlgorithms {

        private static BlockPos vanillaNaturalSpawner(MomentInstance<?> momentInstance, LevelChunk chunk) {
            return NaturalSpawner.getRandomPosWithin(momentInstance.getLevel(), chunk);
        }

    }
}
