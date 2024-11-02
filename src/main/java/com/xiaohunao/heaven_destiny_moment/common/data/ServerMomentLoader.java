package com.xiaohunao.heaven_destiny_moment.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class ServerMomentLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    private final RegistryOps<JsonElement> registryOps;

    public ServerMomentLoader(HolderLookup.Provider registries) {
        super(GSON, Registries.elementsDirPath(MomentRegistries.Keys.MOMENT));
        this.registryOps = RegistryOps.create(JsonOps.INSTANCE, registries);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()){
            Moment.CODEC.decode(registryOps, object.get(entry.getKey()));
        }
    }
}
