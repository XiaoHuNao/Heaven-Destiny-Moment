package com.xiaohunao.heaven_destiny_moment.common.subscriptor;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum ConditionType implements StringRepresentable {
    ENTITY {
        @Override
        public List<EventData> getData(MinecraftServer server, Event event) {
            if (event instanceof EntityEvent entityEvent) {
                Entity entity = entityEvent.getEntity();
                if (!entity.level().isClientSide) {
                    return new EventData(MomentManager.of(entity.level()), entity.blockPosition(), null).listOf();
                }
            }
            return List.of();
        }
    },
    PLAYER {
        @Override
        public List<EventData> getData(MinecraftServer server, Event event) {
            if (event instanceof PlayerEvent playerEvent && playerEvent.getEntity() instanceof ServerPlayer player) {
                return new EventData(MomentManager.of(player.level()), player.blockPosition(), player).listOf();
            }
            return List.of();
        }
    },
    BLOCK {
        @Override
        public List<EventData> getData(MinecraftServer server, Event event) {
            if (event instanceof BlockEvent blockEvent && blockEvent.getLevel() instanceof ServerLevel level) {
                return new EventData(MomentManager.of(level), blockEvent.getPos(), null).listOf();
            }
            return List.of();
        }
    },
    LEVEL {
        @Override
        public List<EventData> getData(MinecraftServer server, Event event) {
            List<EventData> datas = new ArrayList<>();
            for (ServerLevel level : server.getAllLevels()) {
                datas.add(new EventData(MomentManager.of(level), null, null));
            }
            return datas;
        }
    };

    public static final Codec<ConditionType> CODEC = StringRepresentable.fromEnum(ConditionType::values);

    public abstract List<EventData> getData(MinecraftServer server, Event event);

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
