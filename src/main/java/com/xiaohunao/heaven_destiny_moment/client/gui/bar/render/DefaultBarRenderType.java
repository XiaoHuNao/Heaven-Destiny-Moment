package com.xiaohunao.heaven_destiny_moment.client.gui.bar.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.client.gui.hud.MomentBarOverlay;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;

import java.util.Locale;
import java.util.function.Function;

public class DefaultBarRenderType implements IBarRenderType{
    public static final Codec<BossEvent.BossBarOverlay> BOSS_BAR_OVERLAY_CODEC = Codec.STRING.xmap(overlay -> BossEvent.BossBarOverlay.valueOf(overlay.toUpperCase(Locale.ROOT)),
            overlay -> overlay.name().toLowerCase(Locale.ROOT));
    public static final Codec<BossEvent.BossBarColor> BOSS_COLOR_CODEC = Codec.STRING.xmap(color -> BossEvent.BossBarColor.valueOf(color.toUpperCase(Locale.ROOT)),
            color -> color.name().toLowerCase(Locale.ROOT));
    public static final MapCodec<DefaultBarRenderType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BOSS_BAR_OVERLAY_CODEC.optionalFieldOf("overlay",BossEvent.BossBarOverlay.PROGRESS).forGetter(DefaultBarRenderType::getOverlay),
            BOSS_COLOR_CODEC.optionalFieldOf("color",BossEvent.BossBarColor.RED).forGetter(DefaultBarRenderType::getColor)
    ).apply(instance, DefaultBarRenderType::new));


    private BossEvent.BossBarOverlay overlay;
    private BossEvent.BossBarColor color;

    public DefaultBarRenderType(BossEvent.BossBarOverlay overlay, BossEvent.BossBarColor color) {
        this.overlay = overlay;
        this.color = color;
    }
    public DefaultBarRenderType() {
        this.overlay = BossEvent.BossBarOverlay.PROGRESS;
        this.color = BossEvent.BossBarColor.RED;
    }

    @Override
    public MapCodec<? extends IBarRenderType> codec() {
        return HDMContextRegister.DEFAULT_BAR_RENDER_TYPE.get();
    }

    @Override
    public void renderBar(GuiGraphics guiGraphics, MomentBar bar, int index) {
        drawBar(guiGraphics, 0, 0 , bar, color);
    }

    private void drawBar(GuiGraphics guiGraphics, int x, int y, MomentBar bar, BossEvent.BossBarColor color) {
        this.drawBar(guiGraphics, x, y, bar, 182, color, BossHealthOverlay.BAR_BACKGROUND_SPRITES, BossHealthOverlay.OVERLAY_BACKGROUND_SPRITES);
        int i = Mth.lerpDiscrete(bar.getProgress(), 0, 182);
        if (i > 0) {
            this.drawBar(guiGraphics, x, y, bar, i, color, BossHealthOverlay.BAR_PROGRESS_SPRITES, BossHealthOverlay.OVERLAY_PROGRESS_SPRITES);
        }
    }

    private void drawBar(GuiGraphics guiGraphics, int x, int y, MomentBar bar) {
        this.drawBar(guiGraphics, x, y, bar, 182, bar.getColor(), BossHealthOverlay.BAR_BACKGROUND_SPRITES, BossHealthOverlay.OVERLAY_BACKGROUND_SPRITES);
        int i = Mth.lerpDiscrete(bar.getProgress(), 0, 182);
        if (i > 0) {
            this.drawBar(guiGraphics, x, y, bar, i, bar.getColor(), BossHealthOverlay.BAR_PROGRESS_SPRITES, BossHealthOverlay.OVERLAY_PROGRESS_SPRITES);
        }
    }
    private void drawBar(GuiGraphics guiGraphics, int x, int y, MomentBar bar, int progress, BossEvent.BossBarColor color , ResourceLocation[] barProgressSprites, ResourceLocation[] overlayProgressSprites) {
        RenderSystem.enableBlend();
        guiGraphics.blitSprite(barProgressSprites[color.ordinal()], 182, 5, 0, 0, x, y, progress, 5);
        if (bar.getType() instanceof  DefaultBarRenderType  defaultBarRender && defaultBarRender.overlay != BossEvent.BossBarOverlay.PROGRESS) {
            guiGraphics.blitSprite(overlayProgressSprites[defaultBarRender.overlay.ordinal() - 1], 182, 5, 0, 0, x, y, progress, 5);
        }

        RenderSystem.disableBlend();
    }

    public BossEvent.BossBarOverlay getOverlay() {
        return this.overlay;
    }

    public BossEvent.BossBarColor getColor() {
        return color;
    }

    public DefaultBarRenderType setOverlay(BossEvent.BossBarOverlay overlay) {
        this.overlay = overlay;
        return this;
    }
}
