package com.xiaohunao.heaven_destiny_moment.client.gui.bar.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.client.gui.hud.MomentBarOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.BossEvent;

public class DefaultBarRenderType implements IBarRenderType{
    private BossEvent.BossBarOverlay overlay;


    @Override
    public void renderBar(GuiGraphics guiGraphics, MomentBar bar, int index) {

    }

    public static void drawBar(GuiGraphics guiGraphics,MomentBar bar, int x, int y, int barSize) {
        drawBar(guiGraphics,bar, x, y, barSize, 0);
        int i = (int)(bar.getProgress() * (barSize + 1.0F));
        if (i > 0) {
            drawBar(guiGraphics,bar, x, y, i, 5);
        }
    }

    private static void drawBar(GuiGraphics guiGraphics,MomentBar bar ,int x, int y, int barSize, int p_281636_) {
//        guiGraphics.blit(MomentBarOverlay.GUI_BARS_LOCATION, x, y, 0, bar.getColor().ordinal() * 5 * 2 + p_281636_, 2, 5);
//        guiGraphics.blit(MomentBarOverlay.GUI_BARS_LOCATION, x + 2, y, 2, bar.getColor().ordinal() * 5 * 2 + p_281636_, barSize, 5);
//        guiGraphics.blit(MomentBarOverlay.GUI_BARS_LOCATION, x + 2 + barSize, y, 182 - 2, bar.getColor().ordinal() * 5 * 2 + p_281636_, 2, 5);
//        if (bar.getRenderType() instanceof DefaultBarRenderType defaultRender && defaultRender.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
//            RenderSystem.enableBlend();
//            guiGraphics.blit(MomentBarOverlay.GUI_BARS_LOCATION, x, y, 0, 80 + (defaultRender.getOverlay().ordinal() - 1) * 5 * 2 + p_281636_, 2, 5);
//            guiGraphics.blit(MomentBarOverlay.GUI_BARS_LOCATION, x + 2, y, 2, 80 + (defaultRender.getOverlay().ordinal() - 1) * 5 * 2 + p_281636_, barSize, 5);
//            guiGraphics.blit(MomentBarOverlay.GUI_BARS_LOCATION, x + 2 + barSize, y, 182 - 2, 80 + (defaultRender.getOverlay().ordinal() - 1) * 5 * 2 + p_281636_, 2, 5);
//            RenderSystem.disableBlend();
//        }
    }
    private BossEvent.BossBarOverlay getOverlay() {
        return this.overlay;
    }
}
