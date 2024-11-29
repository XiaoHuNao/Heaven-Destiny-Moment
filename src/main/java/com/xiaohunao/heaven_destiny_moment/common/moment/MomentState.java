package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;

import java.util.Locale;

public enum MomentState {
    READY("ready", 0),
    START("start", 1),
    ONGOING("ongoing", 2),
    VICTORY("victory", 3),
    LOSE("lose", 4),
    END("end", 5);
    public static final Codec<MomentState> CODEC = Codec.STRING.xmap(name1 -> valueOf(name1.toUpperCase(Locale.ROOT)), state -> state.name().toLowerCase(Locale.ROOT));
    private final String name;
    private final int index;

    MomentState(String name, int index) {
        this.name = name;
        this.index = index;
    }
}