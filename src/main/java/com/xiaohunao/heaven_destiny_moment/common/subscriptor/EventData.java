package com.xiaohunao.heaven_destiny_moment.common.subscriptor;

import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public record EventData(MomentManager momentManager, @Nullable BlockPos blockPos, @Nullable ServerPlayer serverPlayer) {
    public List<EventData> listOf() {
        return Collections.singletonList(this);
    }
}
