package com.xiaohunao.heaven_destiny_moment;

import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.client.gui.hud.MomentBarOverlay;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.AmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.EntityInfoContext;
import com.xiaohunao.heaven_destiny_moment.common.context.predicate.PredicateContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.RewardContext;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import java.util.Map;

@Mod(HeavenDestinyMoment.MODID)
public class HeavenDestinyMoment {
    public static final String MODID = "heaven_destiny_moment";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HeavenDestinyMoment(IEventBus modEventBus, ModContainer modContainer) {
        loadClasses();

//        MomentRegistry.MOMENT.register(modEventBus);


        modEventBus.addListener(MomentRegistries::registerRegistries);
        modEventBus.addListener(MomentRegistries::registerDataPackRegistries);
    }


    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String asDescriptionId(String path) {
        return MODID + "." + path;
    }

    public static <T> ResourceKey<T> asResourceKey(ResourceKey<? extends Registry<T>> registryKey, String path) {
        return ResourceKey.create(registryKey, HeavenDestinyMoment.asResource(path));
    }

    public static void loadClasses() {
        Moment.register();
        AmountContext.register();
        ConditionContext.register();
        EntityInfoContext.register();
        PredicateContext.register();
        RewardContext.register();
    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
        @SubscribeEvent
        public static void registerOverlay(RegisterGuiLayersEvent event) {
            event.registerAboveAll(HeavenDestinyMoment.asResource("moment_bar"), new MomentBarOverlay());
        }
    }
}
