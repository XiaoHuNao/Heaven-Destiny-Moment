package com.xiaohunao.heaven_destiny_moment.common.moment.area;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;


public class LocationArea extends Area<LocationPredicate> {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("location");
    public static final Codec<LocationArea> CODEC = LocationPredicate.CODEC.xmap(LocationArea::new,LocationArea::getLocationPredicate);
    public static final LocationArea EMPTY = new LocationArea(LocationPredicate.Builder.location().build());

    private final LocationPredicate locationPredicate;

    public LocationArea(LocationPredicate locationPredicate) {
        this.locationPredicate = locationPredicate;
    }

    @Override
    public boolean contains(ServerLevel level, BlockPos blockPos) {
        return locationPredicate.matches(level,blockPos.getX(),blockPos.getY(),blockPos.getZ());
    }

    @Override
    public Codec<? extends Area<?>> getCodec() {
        return CODEC;
    }

    public LocationPredicate getLocationPredicate() {
        return locationPredicate;
    }
}
