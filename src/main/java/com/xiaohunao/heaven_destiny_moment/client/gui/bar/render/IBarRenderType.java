package com.xiaohunao.heaven_destiny_moment.client.gui.bar.render;

import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import net.minecraft.client.gui.GuiGraphics;

public interface IBarRenderType {
    void renderBar(GuiGraphics guiGraphics, MomentBar bar, int index);
}
