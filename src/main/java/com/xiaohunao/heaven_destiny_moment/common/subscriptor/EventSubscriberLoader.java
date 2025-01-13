package com.xiaohunao.heaven_destiny_moment.common.subscriptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = HeavenDestinyMoment.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EventSubscriberLoader extends SimpleJsonResourceReloadListener {
    public static final EventSubscriberLoader INSTANCE = new EventSubscriberLoader();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public EventSubscriberLoader() {
        super(GSON, Registries.elementsDirPath(HDMRegistries.Keys.MOMENT) + "/subscriber");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ClassLoader classLoader = getClass().getClassLoader();
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            EventSubscriber.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).ifSuccess(subscriptor -> {
                try {
                    Class<?> clazz = classLoader.loadClass(subscriptor.eventName());
                    if (Event.class.isAssignableFrom(clazz)) {
                        NeoForge.EVENT_BUS.addListener((Class<Event>) clazz, subscriptor::onInvoked);
                    } else {
                        HeavenDestinyMoment.LOGGER.error("{} is not an Event class!", subscriptor.eventName());
                    }
                } catch (ClassNotFoundException e) {
                    HeavenDestinyMoment.LOGGER.error(e.getMessage());
                }
            });
        }
    }

    @Override
    public String getName() {
        return "EventSubscriberLoader";
    }

    @SubscribeEvent
    public static void register(AddReloadListenerEvent event) {
        event.addListener(INSTANCE);
    }
}
