package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber
public class PlayerEventSubscriber {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }


//        MomentInstance.create(ModMoments.BLOOD_MOON,level,player.blockPosition(),player);



//        MomentInstance bloodMoon1 = MomentInstance.create(level, ModMoments.create("test"));
//        System.out.println(bloodMoon1.getMoment());




//        MobSpawnSettingsContext.CODEC.encodeStart(JsonOps.INSTANCE, MobSpawnSettingsContext.Default).result().ifPresent((element) -> {
//            MandateHeavenTime.LOGGER.info("MobSpawnSettingsContext: {}", element);
//        });
//        MobSpawnSettings build = new MobSpawnSettings.Builder()
//                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKULL, 10, 4, 4))
//                .build();
//
//        System.out.println(build.spawners);


    }
}