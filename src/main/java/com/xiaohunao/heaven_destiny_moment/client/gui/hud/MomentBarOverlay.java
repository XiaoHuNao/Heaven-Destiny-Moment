package com.xiaohunao.heaven_destiny_moment.client.gui.hud;

import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public class MomentBarOverlay implements  LayeredDraw.Layer{
    public static final ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/demo_background.png");
    public static final ResourceLocation GUI_BARS_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/bars.png");

    public static final Map<UUID, MomentBar> barMap = Maps.newLinkedHashMap();


    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        for (int i = 0; i < barMap.values().size(); i++) {
            MomentBar bar = (MomentBar)barMap.values().toArray()[i];
            ResourceKey<Moment> momentResourceKey = bar.getMoment();
            ClientLevel clientLevel = Minecraft.getInstance().level;
            if (clientLevel == null) {
                return;
            }
            Moment moment = clientLevel.registryAccess().registryOrThrow(MomentRegistries.Keys.MOMENT).get(momentResourceKey);
            if (moment != null){
                IBarRenderType value = MomentRegistries.BAR_RENDER_TYPE.get(moment.getBarRenderType());
                if (value != null) {
                    value.renderBar(guiGraphics,bar, i);
                }
            }
        }

    }
}
