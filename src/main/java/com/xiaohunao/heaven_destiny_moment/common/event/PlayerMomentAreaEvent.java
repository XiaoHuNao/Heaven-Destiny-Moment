package com.xiaohunao.heaven_destiny_moment.common.event;

import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public abstract class PlayerMomentAreaEvent extends PlayerEvent {
    private Area area;

    public PlayerMomentAreaEvent(Player player, Area area) {
        super(player);
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public static class Enter extends PlayerMomentAreaEvent {
        public Enter(Player player, Area area) {
            super(player, area);
        }
    }
    public static class Exit extends PlayerMomentAreaEvent {

        public Exit(Player player, Area area) {
            super(player, area);
        }
    }
}
