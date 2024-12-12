package com.xiaohunao.heaven_destiny_moment.common.mixin;

import com.xiaohunao.heaven_destiny_moment.common.context.EntitySpawnSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.MobSpawnRule;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentData;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.entity.Mob.checkMobSpawnRules;

@Mixin(Slime.class)
public class SlimeMixin {

    @Inject(method = "checkSlimeSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void checkSlimeSpawnRules(EntityType<Slime> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof ServerLevel serverLevel) {
            MomentManager momentManager = MomentManager.of(serverLevel);
            for (MomentInstance<?> instance : momentManager.getImmutableRunMoments().values()) {
                instance.moment()
                        .filter(moment -> moment.isInArea(serverLevel, pos))
                        .flatMap(Moment::momentDataContext)
                        .flatMap(MomentData::entitySpawnSettingsContext)
                        .flatMap(EntitySpawnSettings::rule)
                        .flatMap(MobSpawnRule::slimesSpawnEverywhere)
                        .ifPresent(slimesSpawnEverywhere -> {
                            boolean origin = pos.getY() > 50 &&
//                                    pos.getY() < 70 &&
                                    random.nextFloat() < 0.5F &&
                                    random.nextFloat() < level.getMoonBrightness() &&
                                    level.getMaxLocalRawBrightness(pos) <= random.nextInt(8);

                            if (slimesSpawnEverywhere && origin && pos.getY() >= serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) - 1) {
                                cir.setReturnValue(checkMobSpawnRules(entityType, serverLevel, spawnType, pos, random));
                            }
                        });
            }
        }
    }
}