package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.base.Function;
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
        private Integer environmentColor;
        private ClientMoonSettingsContext clientMoonSettingsContext;

        public Builder environmentColor(int environmentColor) {
            this.environmentColor = environmentColor;
            return this;
        }

        public Builder clientMoonSettingsContext(Function<ClientMoonSettingsContext.Builder,ClientMoonSettingsContext.Builder> clientMoonSettingsContext){
            this.clientMoonSettingsContext = clientMoonSettingsContext.apply(new ClientMoonSettingsContext.Builder()).build();
            return this;
        }

        public ClientSettingsContext build() {
            return new ClientSettingsContext(Optional.ofNullable(environmentColor),Optional.ofNullable(clientMoonSettingsContext));
        }
    }
}
