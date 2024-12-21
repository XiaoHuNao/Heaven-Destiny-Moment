package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.common.context.ConditionGroup;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentData;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMAttachments;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.registries.DataPackRegistriesHooks;

import java.util.List;
import java.util.Map;

@EventBusSubscriber
public class LevelEventSubscriber {
    
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        MomentManager.of(level).tick();

        if (level instanceof ServerLevel serverLevel) {
            Registry<Moment> moments = level.registryAccess().registryOrThrow(HDMRegistries.Keys.MOMENT);
            moments.entrySet().forEach(resourceKeyMomentEntry -> {
                Moment moment = resourceKeyMomentEntry.getValue();

                moment.momentData
                        .flatMap(MomentData::conditionGroup)
                        .flatMap(ConditionGroup::create)
                        .ifPresent(createCondition -> {
                            MomentInstance<? extends Moment> momentInstance = moment.newMomentInstance(serverLevel, resourceKeyMomentEntry.getKey());

                            boolean allMatch = createCondition.getFirst() &&
                                    createCondition.getSecond().stream()
                                            .allMatch(condition -> condition.matches(momentInstance, BlockPos.ZERO));

                            if (allMatch) {
                                MomentManager.of(serverLevel).addMoment(momentInstance, serverLevel, BlockPos.ZERO, null);
                            }
                        });
            });
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        entity.getData(HDMAttachments.MOMENT_ENTITY).getMomentInstance(entity).ifPresent(instance -> {
            instance.addKillCount(entity);
            instance.livingDeath(entity);
        });
    }


//    @SubscribeEvent
//    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
//        Level level = event.getLevel();
//        InteractionHand hand = event.getHand();
//        Player player = event.getEntity();
//        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
//            return;
//        }
//
//
//    }
}
