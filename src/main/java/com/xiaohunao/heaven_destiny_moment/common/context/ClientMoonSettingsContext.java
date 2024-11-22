package com.xiaohunao.heaven_destiny_moment.common.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record ClientMoonSettingsContext(Optional<Integer> moonColor, Optional<Float> MoonSize, Optional<ResourceLocation> MoonTexture) {
    public static final ClientMoonSettingsContext EMPTY = new ClientMoonSettingsContext(Optional.empty(),Optional.empty(),Optional.empty());
    public static final Codec<ClientMoonSettingsContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("moon_color").forGetter(ClientMoonSettingsContext::moonColor),
            Codec.FLOAT.optionalFieldOf("moon_size").forGetter(ClientMoonSettingsContext::MoonSize),
            ResourceLocation.CODEC.optionalFieldOf("moon_texture").forGetter(ClientMoonSettingsContext::MoonTexture)
    ).apply(instance, ClientMoonSettingsContext::new));

    public boolean isEmpty() {
        return moonColor.isEmpty() && MoonSize.isEmpty() && MoonTexture.isEmpty();
    }


    public static class Builder {
        private Integer moonColor;
        private Float moonSize;
        private ResourceLocation moonTexture;

        public ClientMoonSettingsContext build() {
            return new ClientMoonSettingsContext(Optional.ofNullable(moonColor), Optional.ofNullable(moonSize), Optional.ofNullable(moonTexture));
        }

        public Builder moonColor(int moonColor) {
            this.moonColor = moonColor;
            return this;
        }
        public Builder moonSize(float moonSize) {
            this.moonSize = moonSize;
            return this;
        }
        public Builder moonTexture(ResourceLocation moonTexture) {
            this.moonTexture = moonTexture;
            return this;
        }
    }
}
