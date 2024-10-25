package com.xiaohunao.heaven_destiny_moment;

import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.common.attachment.MomentAttachment;
import com.xiaohunao.heaven_destiny_moment.common.init.ModAttachments;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistry;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentTypeRegistry;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

@Mod(HeavenDestinyMoment.MODID)
public class HeavenDestinyMoment {
    public static final String MODID = "heaven_destiny_moment";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HeavenDestinyMoment(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerRegistries);

        MomentTypeRegistry.MOMENT_TYPE.register(modEventBus);
        MomentRegistry.MOMENT.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
    }

    public void registerRegistries(NewRegistryEvent event) {
        event.register(MomentTypeRegistry.REGISTRY);
        event.register(MomentRegistry.REGISTRY);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String asDescriptionId(String path) {
        return MODID + "." + path;
    }

    @SubscribeEvent
    public void onDataPackRegistryNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(MomentRegistry.KEY, Moment.DATA_CODEC);
    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
