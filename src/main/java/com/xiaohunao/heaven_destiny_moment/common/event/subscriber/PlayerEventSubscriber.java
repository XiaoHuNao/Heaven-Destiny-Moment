package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.hud.MomentBarOverlay;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = HeavenDestinyMoment.MODID)
public class PlayerEventSubscriber {
    @SubscribeEvent
    public static void onClientPlayerNetworkLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        MomentBarOverlay.barMap.clear();
    }

    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }
        Chicken chicken = EntityType.CHICKEN.create(level);
        Zombie zombie = EntityType.ZOMBIE.create(level);

        BlockPos spawnPos = level.getLevelData().getSpawnPos();

//        chicken.moveTo(Vec3.atLowerCornerOf(spawnPos));
//        zombie.moveTo(Vec3.atLowerCornerOf(spawnPos));

//        chicken.startRiding(zombie);
//        chicken.setPos(Vec3.atLowerCornerOf(event.getPos().above()));
//        level.addFreshEntity(chicken);
//        level.addFreshEntity(zombie);
//        chicken.setPos(Vec3.atLowerCornerOf(event.getPos().above()));



//        Zombie zombie = EntityType.ZOMBIE.create(level);
//        if (chicken != null && zombie != null) {
//            chicken.startRiding(zombie);
//            chicken.moveTo(Vec3.atLowerCornerOf(event.getPos().below()));
//            level.addFreshEntity(chicken);
//        }


    }

}