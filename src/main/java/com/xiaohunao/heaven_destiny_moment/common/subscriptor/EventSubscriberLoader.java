package com.xiaohunao.heaven_destiny_moment.common.subscriptor;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = HeavenDestinyMoment.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EventSubscriberLoader extends SimpleJsonResourceReloadListener {
    public static final EventSubscriberLoader INSTANCE = new EventSubscriberLoader();
    private final List<Consumer<Event>> last = new ArrayList<>();

    public EventSubscriberLoader() {
        super(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), "heaven_destiny_moment/subscriber");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        for (Consumer<Event> consumer : last) {
            NeoForge.EVENT_BUS.unregister(consumer);
        }
        last.clear();
        ClassLoader classLoader = getClass().getClassLoader();
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            EventSubscriber.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).ifSuccess(subscriptor -> {
                try {
                    Class<?> clazz = classLoader.loadClass(subscriptor.eventName());
                    if (Event.class.isAssignableFrom(clazz)) {
                        Consumer<Event> onInvoked = subscriptor::onInvoked;
                        NeoForge.EVENT_BUS.addListener((Class<Event>) clazz, onInvoked);
                        last.add(onInvoked);
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
        RegistryAccess registryAccess = event.getRegistryAccess();
        event.addListener(INSTANCE);
    }
}
