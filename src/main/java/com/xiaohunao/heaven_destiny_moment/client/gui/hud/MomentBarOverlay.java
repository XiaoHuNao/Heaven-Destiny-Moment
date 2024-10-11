package com.xiaohunao.heaven_destiny_moment.client.gui.hud;

import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.common.network.s2c.MomentBarSyncPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class MomentBarOverlay implements IGuiOverlay {
    public static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/demo_background.png");
    public static final ResourceLocation GUI_BARS_LOCATION = new ResourceLocation("textures/gui/bars.png");

    public static final Map<UUID, MomentBar> barMap = Maps.newLinkedHashMap();

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
//        if (!BAR_MAP.isEmpty()) {
//            for (int index = 0; index < BAR_MAP.values().size(); index++) {
//                AftermathBar bar = (AftermathBar) BAR_MAP.values().toArray()[index];
//                bar.getRender().renderBar(guiGraphics,bar,index);
//            }
//        }
    }


}
