package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.base.Function;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record ClientSettings(Optional<Integer> environmentColor,
                             Optional<ClientMoonSettings> clientMoonSettingsContext) {
    public static final ClientSettings EMPTY = new ClientSettings(Optional.empty(),Optional.empty());

    public static final Codec<ClientSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("environmentColor").forGetter(ClientSettings::environmentColor),
            ClientMoonSettings.CODEC.optionalFieldOf("clientMoonSettingsContext").forGetter(ClientSettings::clientMoonSettingsContext)
    ).apply(instance, ClientSettings::new));


    public boolean isEmpty() {
        return environmentColor.isEmpty() && clientMoonSettingsContext.isEmpty();
    }


    public static class Builder {
        private Integer environmentColor;
        private ClientMoonSettings clientMoonSettings;

        public Builder environmentColor(int environmentColor) {
            this.environmentColor = environmentColor;
            return this;
        }

        public Builder clientMoonSettingsContext(Function<ClientMoonSettings.Builder, ClientMoonSettings.Builder> clientMoonSettingsContext){
            this.clientMoonSettings = clientMoonSettingsContext.apply(new ClientMoonSettings.Builder()).build();
            return this;
        }

        public ClientSettings build() {
            return new ClientSettings(Optional.ofNullable(environmentColor),Optional.ofNullable(clientMoonSettings));
        }
    }
}
