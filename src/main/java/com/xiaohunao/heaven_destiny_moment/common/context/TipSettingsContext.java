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

    public void addTip(MomentState momentState, Component component) {
        texts.ifPresent(momentStateComponentMap -> momentStateComponentMap.put(momentState, component));
    }
    public void addTip(MomentState momentState, Component component,int color) {
        addTip(momentState, component.copy().withStyle(style -> style.withColor(color)));
    }
    public void addSound(MomentState momentState, Holder<SoundEvent> soundEvent) {
        soundEvents.ifPresent(momentStateComponentMap -> momentStateComponentMap.put(momentState, soundEvent));
    }

}
