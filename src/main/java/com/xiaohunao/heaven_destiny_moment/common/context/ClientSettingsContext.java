package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public record ClientSettingsContext(int environmentColor, TipSettingsContext tipSettingsContext,
                                    ClientMoonSettingsContext clientMoonSettingsContext) {
    public static final ClientSettingsContext EMPTY = new ClientSettingsContext(-1, TipSettingsContext.EMPTY, ClientMoonSettingsContext.EMPTY);

    public static final Codec<ClientSettingsContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("environmentColor",null).forGetter(ClientSettingsContext::environmentColor),
            TipSettingsContext.CODEC.optionalFieldOf("tipSettingsContext",null).forGetter(ClientSettingsContext::tipSettingsContext),
            ClientMoonSettingsContext.CODEC.optionalFieldOf("clientMoonSettingsContext",null).forGetter(ClientSettingsContext::clientMoonSettingsContext)
    ).apply(instance, ClientSettingsContext::new));

    public boolean checkLevelCoverage() {
        if (this.equals(EMPTY)){
            return false;
        }
        return true;
    }

    public static class Builder {
        private int environmentColor;

        private int moonSize = 20;
        private ResourceLocation moonTexture = ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");
        private int moonColor = -1;

        private TipSettingsContext tipSettingsContext = new TipSettingsContext(Maps.newHashMap(), Maps.newHashMap());



        public Builder environmentColor(int environmentColor) {
            this.environmentColor = environmentColor;
            return this;
        }

        public Builder modifyMoonSize(int moonSize) {
            this.moonSize = moonSize;
            return this;
        }
        public Builder modifyMoonTexture(ResourceLocation moonTexture) {
            this.moonTexture = moonTexture;
            return this;
        }
        public Builder modifyMoonColor(int moonColor) {
            this.moonColor = moonColor;
            return this;
        }
        public Builder addTip(MomentState momentState, Component component) {
            tipSettingsContext.addTip(momentState, component);
            return this;
        }
        public Builder addTip(MomentState momentState, Component component,int color) {
            addTip(momentState, component.copy().withStyle(style -> style.withColor(color)));
            return this;
        }
        public Builder addSound(MomentState momentState, Holder<SoundEvent> soundEvent) {
            tipSettingsContext.addSound(momentState, soundEvent);
            return this;
        }

        public ClientSettingsContext build() {
            return new ClientSettingsContext(environmentColor, tipSettingsContext, new ClientMoonSettingsContext(moonColor, moonSize, moonTexture));
        }
    }
}
