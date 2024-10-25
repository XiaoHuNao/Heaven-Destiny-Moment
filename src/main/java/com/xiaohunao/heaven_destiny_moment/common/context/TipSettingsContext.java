package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import java.util.Map;

public record TipSettingsContext(Map<MomentState, Holder<SoundEvent>> soundEventMap, Map<MomentState, Component> tipMap) {
    public static final TipSettingsContext EMPTY = new TipSettingsContext(Maps.newHashMap(), Maps.newHashMap());

    public static final Codec<TipSettingsContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(MomentState.CODEC, SoundEvent.CODEC).optionalFieldOf("soundEventMap", Maps.newHashMap()).forGetter(TipSettingsContext::soundEventMap),
            Codec.unboundedMap(MomentState.CODEC, ComponentSerialization.CODEC).optionalFieldOf("tipMap",Maps.newHashMap()).forGetter(TipSettingsContext::tipMap)
    ).apply(instance, TipSettingsContext::new));

    public void addTip(MomentState momentState, Component component) {
        tipMap.put(momentState, component.copy());
    }
    public void addTip(MomentState momentState, Component component,int color) {
        addTip(momentState, component.copy().withStyle(style -> style.withColor(color)));
    }
    public void addSound(MomentState momentState, Holder<SoundEvent> soundEvent) {
        soundEventMap.put(momentState, soundEvent);
    }

}
