package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;


import com.mojang.serialization.JsonOps;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.LocationArea;
import com.xiaohunao.heaven_destiny_moment.common.utils.HolderUtils;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Optional;


@EventBusSubscriber
public class PlayerEventSubscriber {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return;
        }


        if (level instanceof ServerLevel serverLevel) {
//            MomentInstance.create(ModMoments.BLOOD_MOON,serverLevel,player.blockPosition());
//            Optional<HolderLookup.RegistryLookup<Structure>> lookup = level.registryAccess().lookup(Registries.STRUCTURE);
//            lookup.ifPresent(structureHolderGetter -> {
//                structureHolderGetter.filterElements()
//                HolderSet<Structure> holderSet = HolderUtils.getHolderSet(structureHolderGetter, BuiltinStructures.SWAMP_HUT,BuiltinStructures.ANCIENT_CITY);
////                LocationArea locationArea = new LocationArea();
//                LocationPredicate locationPredicate = LocationPredicate.Builder.location().setStructures(holderSet).build();
//                LocationPredicate.CODEC.encodeStart(RegistryOps.create(JsonOps.INSTANCE, (HolderLookup.Provider) structureHolderGetter), locationPredicate).result().ifPresent(jsonElement -> {
//                    System.out.println(jsonElement);
//                    LocationPredicate.CODEC.decode(RegistryOps.create(JsonOps.INSTANCE, (HolderLookup.Provider) structureHolderGetter),jsonElement).result().ifPresent(locationArea1 -> {
//                        System.out.println(locationArea1);
//                    });
//                });
//            });
        }


    }

}