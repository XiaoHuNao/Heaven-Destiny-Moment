package com.xiaohunao.heaven_destiny_moment.compat.kubejs;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentData;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettings;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMMomentRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.*;


public class KubeJSMoment extends Moment<KubeJSMoment> {
    public static final MapCodec<KubeJSMoment> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IBarRenderType.CODEC.optionalFieldOf("bar_render_type").forGetter(Moment::barRenderType),
            Area.CODEC.optionalFieldOf("area").forGetter(Moment::area),
            MomentData.CODEC.optionalFieldOf("moment_data_context").forGetter(Moment::momentData),
            TipSettings.CODEC.optionalFieldOf("tips").forGetter(Moment::tipSettings),
            ClientSettings.CODEC.optionalFieldOf("clientSettingsContext").forGetter(Moment::clientSettings)
    ).apply(instance, KubeJSMoment::new));

    public KubeJSMoment(Optional<IBarRenderType> renderType, Optional<Area> area, Optional<MomentData> momentData, 
                       Optional<TipSettings> tipSettings, Optional<ClientSettings> clientSettings) {
        super(renderType, area, momentData, tipSettings, clientSettings);
    }

    @Override
    public MomentInstance<?> newMomentInstance(Level level, ResourceKey<Moment<?>> momentResourceKey) {
        return new KubeJSMomentInstance(level, momentResourceKey);
    }

    @Override
    public MapCodec<? extends Moment<?>> codec() {
        return HDMMomentRegister.KUBEJS_MOMENT.get();
    }

    @FunctionalInterface
    public interface MomentCallback<T, R> {
        R execute(KubeJSMomentInstance instance, T param);
    }


    public static class KubeJSMomentInstance extends MomentInstance<KubeJSMoment> {
        private final Map<String, MomentCallback<?, ?>> callbacks = new HashMap<>();

        protected KubeJSMomentInstance(Level level, ResourceKey<Moment<?>> momentKey) {
            super(HDMMomentRegister.KUBEJS.get(), level, momentKey);
        }

        public KubeJSMomentInstance(UUID uuid, Level level, ResourceKey<Moment<?>> momentKey) {
            super(HDMMomentRegister.KUBEJS.get(), uuid, level, momentKey);
        }


        public <T, R> void registerCallback(String key, MomentCallback<T, R> callback) {
            callbacks.put(key, callback);
        }

        private <T, R> R executeCallback(String key, T param, Supplier<R> defaultAction) {
            MomentCallback<T, R> callback = (MomentCallback<T, R>) callbacks.get(key);
            return callback != null ? callback.execute(this, param) : defaultAction.get();
        }

        @Override
        public void init() {
            executeCallback("init", null, () -> {
                super.init();
                return null;
            });
        }

        @Override
        public void initMomentBar() {
            executeCallback("initMomentBar", null, () -> {
                super.initMomentBar();
                return null;
            });
        }

        @Override
        public void initSpawnPosList() {
            executeCallback("initSpawnPosList", null, () -> {
                super.initSpawnPosList();
                return null;
            });
        }

        @Override
        public Vec3 getRandomSpawnPos() {
            return executeCallback("getRandomSpawnPos", null, super::getRandomSpawnPos);
        }

        @Override
        public CompoundTag serializeNBT() {
            return executeCallback("serializeNBT", null, super::serializeNBT);
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            executeCallback("deserializeNBT", nbt, () -> {
                super.deserializeNBT(nbt);
                return null;
            });
        }

        @Override
        public void ready() {
            executeCallback("ready", null, () -> {
                super.ready();
                return null;
            });
        }

        @Override
        public void start() {
            executeCallback("start", null, () -> {
                super.start();
                return null;
            });
        }

        @Override
        public void ongoing() {
            executeCallback("ongoing", null, () -> {
                super.ongoing();
                return null;
            });
        }

        @Override
        public void victory() {
            executeCallback("victory", null, () -> {
                super.victory();
                return null;
            });
        }

        @Override
        public void lose() {
            executeCallback("lose", null, () -> {
                super.lose();
                return null;
            });
        }

        @Override
        public void tick() {
            executeCallback("tick", null, () -> {
                super.tick();
                return null;
            });
        }

        @Override
        public void end() {
            executeCallback("end", null, () -> {
                super.end();
                return null;
            });
        }

        @Override
        public Predicate<Player> validPlayer() {
            return executeCallback("validPlayer", null, super::validPlayer);
        }

        @Override
        public void finalizeSpawn(Entity entity) {
            executeCallback("finalizeSpawn", entity, () -> {
                super.finalizeSpawn(entity);
                return null;
            });
        }

        @Override
        public void livingDeath(LivingEntity entity) {
            executeCallback("livingDeath", entity, () -> {
                super.livingDeath(entity);
                return null;
            });
        }

        @Override
        public void setEntityTagMark(Entity entity) {
            executeCallback("setEntityTagMark", entity, () -> {
                super.setEntityTagMark(entity);
                return null;
            });
        }

        @Override
        public boolean canCreate(Map<UUID, MomentInstance<?>> runMoments, ServerLevel serverLevel, @Nullable BlockPos pos, @Nullable ServerPlayer player) {
            return executeCallback("canCreate", new CreateParams(runMoments, serverLevel, pos, player), () -> super.canCreate(runMoments, serverLevel, pos, player));
        }

        @Override
        public boolean canSpawnEntity(Level level, Entity entity, BlockPos pos) {
            return executeCallback("canSpawnEntity", new SpawnParams(level, entity, pos), () -> super.canSpawnEntity(level, entity, pos));
        }
    }

    public record CreateParams(Map<UUID, MomentInstance<?>> runMoments, ServerLevel serverLevel, BlockPos pos, ServerPlayer player) {}
    public record SpawnParams(Level level, Entity entity, BlockPos pos) {}
}
