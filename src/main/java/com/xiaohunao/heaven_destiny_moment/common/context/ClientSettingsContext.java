package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public record ClientSettingsContext(Optional<Integer> environmentColor,
                                    Optional<ClientMoonSettingsContext> clientMoonSettingsContext) {
    public static final ClientSettingsContext EMPTY = new ClientSettingsContext(Optional.empty(),Optional.empty());

    public static final Codec<ClientSettingsContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("environmentColor").forGetter(ClientSettingsContext::environmentColor),
            ClientMoonSettingsContext.CODEC.optionalFieldOf("clientMoonSettingsContext").forGetter(ClientSettingsContext::clientMoonSettingsContext)
    ).apply(instance, ClientSettingsContext::new));


    public boolean isEmpty() {
        return environmentColor.isEmpty() && clientMoonSettingsContext.isEmpty();
    }


    public static class Builder {
        private Optional<Integer> environmentColor;
        private final ClientMoonSettingsContext.Builder clientMoonSettingsContext = new ClientMoonSettingsContext.Builder();

        public Builder environmentColor(int environmentColor) {
            this.environmentColor = Optional.of(environmentColor);
            return this;
        }

        public Builder moonSize(Float moonSize) {
            this.clientMoonSettingsContext.moonSize(moonSize);
            return this;
        }
        public Builder moonTexture(ResourceLocation moonTexture) {
            this.clientMoonSettingsContext.moonTexture(moonTexture);
            return this;
        }
        public Builder moonColor(int moonColor) {
            this.clientMoonSettingsContext.moonColor(moonColor);
            return this;
        }
        public ClientSettingsContext build() {
            ClientMoonSettingsContext moonSettingsContext = clientMoonSettingsContext.build();
            if (moonSettingsContext.isEmpty()){
                return new ClientSettingsContext(environmentColor,Optional.empty());
            }
            return new ClientSettingsContext(environmentColor,Optional.of(moonSettingsContext));
        }
    }
}
