package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;

public enum MomentState {
    READY("ready", 0),
    START("start", 1),
    ONGOING("ongoing", 2),
    VICTORY("victory", 3),
    LOSE("lose", 4),
    CELEBRATING("celebrating", 5),
    END("end", 6);
    public static final Codec<MomentState> CODEC = Codec.STRING.xmap(MomentState::valueOf, MomentState::name);
    private final String name;
    private final int index;

    MomentState(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}