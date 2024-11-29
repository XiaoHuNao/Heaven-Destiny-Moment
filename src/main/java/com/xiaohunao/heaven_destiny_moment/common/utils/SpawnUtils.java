package com.xiaohunao.heaven_destiny_moment.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpawnUtils {

    public static Vec3 spawn(Level level, Vec3 pos, Entity entity, int attempt, int spawnRange){
        for (int i = 0; i < attempt; i++) {
            // Select a position
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

    public static boolean noBlockCollision(Level level, AABB pCollisionBox) {
        for (VoxelShape voxelshape : level.getBlockCollisions(null, pCollisionBox)) {
            if (!voxelshape.isEmpty()) {
                return false;
            }
        }

        return true;
    }
    public static AABB getAABB(Entity e, double x, double y, double z) {
        return e.getDimensions(Pose.STANDING).makeBoundingBox(x, y, z);
    }
}
