package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.BetterEffectHudGradientElement;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudColorEditScreen;

import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Comparator;

public class BetterEffectHudColorGradientBox extends AbstractWidget.WithInactiveMessage {
    VarHandle handle;
    BetterEffectHudColorSetting colorSetting;
    Screen screen;

    public BetterEffectHudColorGradientBox(int posX, int posY, int sizeX, int sizeY, VarHandle handle, Screen screen) {
        super(posX, posY, sizeX, sizeY, Component.empty());
        this.handle = handle;
        this.screen = screen;
        this.colorSetting = (BetterEffectHudColorSetting) handle.get(BetterEffectHudConfig.getInstance());
    }

    protected MutableComponent createNarrationMessage() {
        return Component.translatable("gui.narrate.slider", new Object[]{this.getMessage()});
    }

    public void updateWidgetNarration(final NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    private Identifier getHandleOverlaySprite() {
        return this.isActive() && (this.isHovered() || this.isFocused()) ? Identifier.withDefaultNamespace("widget/slider_highlighted") : Identifier.withDefaultNamespace("widget/slider");
    }

    public void onClick(final MouseButtonEvent event, final boolean doubleClick) {
        float pos = (float) Math.clamp((event.x() - (float) this.getX()) / (float) this.width, 0f, 1f);
        boolean cantAdd = false;
        float gap = 7f / this.getWidth();
        int index = 0;
        for (int i = 0; i < colorSetting.getColorGradient().toArray().length; i++) {
            float value = colorSetting.getColorGradient().get(i).getPos();
            if (pos - gap < value && value < pos + gap) {
                cantAdd = true;
                break;
            }
            if (value < pos) index = i + 1;
        }
        if (cantAdd) return;
        colorSetting.getColorGradient().add(index, new BetterEffectHudGradientElement(0xFFFFFFFF, pos));
        Minecraft.getInstance().setScreenAndShow(screen);
    }

    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getHandleOverlaySprite(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
        for (int i = 1; i < this.getWidth() - 1; i++) graphics.blit(RenderPipelines.GUI_TEXTURED, BetterEffectHud.id("textures/gui/gradient.png"), this.getX() + i, this.getY(), 0, 0, 1, this.getHeight(), 1, this.getHeight(), getGradientPiece((float) i / this.getWidth(), colorSetting));
        this.extractScrollingStringOverContents(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE), this.getMessage(), 2);
        this.handleCursor(graphics);
    }

    public static int getGradientPiece(float piece, BetterEffectHudColorSetting colorSetting) {
        var colorGradient = colorSetting.getColorGradient();
        if (colorGradient == null || colorGradient.isEmpty()) return 0xFFFFFFFF;

        var left = colorGradient.stream().filter(e -> e.getPos() <= piece).max(Comparator.comparingDouble(BetterEffectHudGradientElement::getPos)).orElse(null);
        var right = colorGradient.stream().filter(e -> e.getPos() >= piece).min(Comparator.comparingDouble(BetterEffectHudGradientElement::getPos)).orElse(null);
        if (left == null) left = right;
        if (right == null) right = left;

        float range = right.getPos() - left.getPos();
        float t = (range <= 0.0F) ? 0.0F : (piece - left.getPos()) / range;
        return ARGB.srgbLerp(t, left.getColor(), right.getColor());
    }
}
