package com.xiaohunao.heaven_destiny_moment.common.moment.area;

import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public abstract class Area<T> implements CodecProvider<Area<?>> {
    public final static CodecMap<Area<?>> CODEC = new CodecMap<>("Area");

    public static void register() {
        CODEC.register(LocationArea.ID, LocationArea.CODEC);

    }

    public abstract boolean contains(ServerLevel level, BlockPos blockPos);
}
