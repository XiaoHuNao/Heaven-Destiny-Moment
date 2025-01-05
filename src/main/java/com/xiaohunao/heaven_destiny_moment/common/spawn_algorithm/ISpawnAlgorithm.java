package com.xiaohunao.heaven_destiny_moment.common.spawn_algorithm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;


public interface ISpawnAlgorithm {
    Codec<ISpawnAlgorithm> CODEC = Codec.lazyInitialized(() -> HDMRegistries.Suppliers.SPAWN_ALGORITHM_CODEC.get().byNameCodec()).dispatch(ISpawnAlgorithm::codec, Function.identity());

    Vec3 spawn(MomentInstance<?> momentInstance, Entity entity);

    MapCodec<? extends ISpawnAlgorithm> codec();

    default boolean noBlockCollision(Level level, AABB pCollisionBox) {
        for (VoxelShape voxelshape : level.getBlockCollisions(null, pCollisionBox)) {
            if (!voxelshape.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    default AABB getAABB(Entity e, double x, double y, double z) {
        return e.getDimensions(Pose.STANDING).makeBoundingBox(x, y, z);
    }
}
