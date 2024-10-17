package com.xiaohunao.heaven_destiny_moment.client.gui.hud;

import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import com.xiaohunao.heaven_destiny_moment.common.init.ModRenderBarTypes;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.network.s2c.MomentBarSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
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
        for (int i = 0; i < barMap.values().size(); i++) {
            MomentBar bar = (MomentBar)barMap.values().toArray()[i];
            ResourceKey<Moment> momentKey = bar.getMomentKey();
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                int finalI = i;
                level.registryAccess().registryOrThrow(ModMoments.MOMENT_KEY).getHolder(momentKey).ifPresent(holder -> {
                    Moment moment = holder.get();
                    IBarRenderType value = ModRenderBarTypes.REGISTRY_SUPPLIER.get().getValue(moment.barRenderType());
                    if (value != null) {
                        value.renderBar(guiGraphics,bar, finalI);
                    }
                });
            }
        }
    }


}
