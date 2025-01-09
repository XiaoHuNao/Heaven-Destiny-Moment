package com.xiaohunao.heaven_destiny_moment.common.spawn_algorithm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public record OpenAreaSpawnAlgorithm(int maxTry, int range, Optional<Direction> dir) implements ISpawnAlgorithm {
    public static final OpenAreaSpawnAlgorithm DEFAULT = new OpenAreaSpawnAlgorithm(16, 32,Optional.empty());

    public static final MapCodec<OpenAreaSpawnAlgorithm> CODEC =  RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.optionalFieldOf("maxTry",16).forGetter(OpenAreaSpawnAlgorithm::maxTry),
            Codec.INT.optionalFieldOf("range",32).forGetter(OpenAreaSpawnAlgorithm::range),
            Direction.CODEC.optionalFieldOf("dir").forGetter(OpenAreaSpawnAlgorithm::dir)
    ).apply(instance, OpenAreaSpawnAlgorithm::new));


    @Override
    public Vec3 spawn(MomentInstance<?> momentInstance, Entity entity) {
        Level level = momentInstance.getLevel();
        Vec3 pos = momentInstance.getRandomSpawnPos();

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for(int i = 0; i < maxTry; ++i) {
            Vec3i directionVector = dir.map(Direction::getNormal)
                    .map(vec3i -> {
                        double dx = vec3i.getX() * level.getRandom().nextInt(range);
                        double dz = vec3i.getZ() * level.getRandom().nextInt(range);
                        return new Vec3i((int) dx, 0,(int) dz);
                    })
                    .orElseGet(() -> {
                        float angle = level.random.nextFloat() * (float) Math.PI * 2F;
                        double dx = Math.cos(angle) * level.getRandom().nextInt(range);
                        double dz = Math.sin(angle) * level.getRandom().nextInt(range);
                        return new Vec3i((int) dx, 0,(int) dz);
                    });

            double x = pos.x() + directionVector.getX() + level.random.nextInt(5);
            double z = pos.z() + directionVector.getZ() + level.random.nextInt(5);
            double y = pos.y();
            mutableBlockPos.move((int) x, (int) y, (int) z);

            //TODO :调整生成算法
            if (SpawnPlacementTypes.ON_GROUND.isSpawnPositionOk(level,mutableBlockPos,entity.getType())){
                return Vec3.atCenterOf(mutableBlockPos);
            }
        }

        return Vec3.ZERO;
    }


    @Override
    public MapCodec<? extends ISpawnAlgorithm> codec() {
        return HDMContextRegister.OPEN_AREA_SPAWN_ALGORITHM.get();
    }

    public static class Builder {
        private int maxTry = 16;
        private int range = 32;
        private Direction dir;

        public OpenAreaSpawnAlgorithm build() {
            return new OpenAreaSpawnAlgorithm(maxTry, range, Optional.ofNullable(dir));
        }

        public Builder maxTry(int maxTry) {
            this.maxTry = maxTry;
            return this;
        }

        public Builder range(int range) {
            this.range = range;
            return this;
        }

        public Builder direction(Direction dir) {
            this.dir = dir;
            return this;
        }
    }
}
