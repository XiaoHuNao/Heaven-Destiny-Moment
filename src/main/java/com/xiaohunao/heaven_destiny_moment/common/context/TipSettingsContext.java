package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.sounds.SoundEvent;

import java.util.Map;
import java.util.Optional;

public record TipSettingsContext(Optional<Map<MomentState, Holder<SoundEvent>>> soundEvents, Optional<Map<MomentState, Component>> texts) {
    public static final TipSettingsContext EMPTY = new TipSettingsContext(Optional.empty(),Optional.empty());

    public static final Codec<TipSettingsContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(MomentState.CODEC, SoundEvent.CODEC).optionalFieldOf("soundEvents").forGetter(TipSettingsContext::soundEvents),
            Codec.unboundedMap(MomentState.CODEC, ComponentSerialization.CODEC).optionalFieldOf("texts").forGetter(TipSettingsContext::texts)
    ).apply(instance, TipSettingsContext::new));



    public static class Builder {
        private Optional<Map<MomentState, Component>> texts = Optional.empty();
        private Optional<Map<MomentState, Holder<SoundEvent>>> soundEvents = Optional.empty();

        public Builder addTip(MomentState momentState, Component component) {
            if (texts.isEmpty()){
                texts = Optional.of(Maps.newHashMap());
            }
            texts.get().put(momentState,component);
            return this;
        }

        public Builder addTip(MomentState momentState, Component component,int color) {
            if (texts.isEmpty()){
                texts = Optional.of(Maps.newHashMap());
            }
            texts.get().put(momentState,component.copy().withStyle(style -> style.withColor(color)));
            return this;
        }

        public Builder addSound(MomentState momentState, Holder<SoundEvent> soundEvent) {
            if (soundEvents.isEmpty()){
                soundEvents = Optional.of(Maps.newHashMap());
            }
            soundEvents.get().put(momentState,soundEvent);
            return this;
        }

        public TipSettingsContext build() {
            return new TipSettingsContext(soundEvents, texts);
        }

    }
}
