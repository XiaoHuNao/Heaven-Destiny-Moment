package com.xiaohunao.heaven_destiny_moment.common.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record ClientMoonSettingsContext(int moonColor, float MoonSize, ResourceLocation MoonTexture) {
    public static final ClientMoonSettingsContext EMPTY = new ClientMoonSettingsContext(-1, 20, ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png"));
    public static final Codec<ClientMoonSettingsContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("moon_color",-1).forGetter(ClientMoonSettingsContext::moonColor),
            Codec.FLOAT.optionalFieldOf("moon_size",20.F).forGetter(ClientMoonSettingsContext::MoonSize),
            ResourceLocation.CODEC.optionalFieldOf("moon_texture",ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png")).forGetter(ClientMoonSettingsContext::MoonTexture)
    ).apply(instance, ClientMoonSettingsContext::new));

}
