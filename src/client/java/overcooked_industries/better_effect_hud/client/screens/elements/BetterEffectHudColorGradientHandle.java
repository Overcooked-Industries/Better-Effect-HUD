package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
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
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudColorEditScreen;

import java.lang.invoke.VarHandle;

public class BetterEffectHudColorGradientHandle extends AbstractWidget.WithInactiveMessage {
    boolean dragging;
    Screen screen;
    int id;
    int basePosX;
    float min = 0f;
    float max = 1f;
    int baseSizeX;
    float offset;
    VarHandle handle;
    BetterEffectHudColorSetting colorSetting;

    public BetterEffectHudColorGradientHandle(int posX, int posY, int sizeX, VarHandle handle, int id, Screen screen) {
        super(Math.round(posX - 3 + sizeX * getHandleValue(handle, id)), posY - 8, 7, 8, Component.empty());
        this.handle = handle;
        this.id = id;
        this.basePosX = posX;
        this.baseSizeX = sizeX;
        this.colorSetting = (BetterEffectHudColorSetting) handle.get(BetterEffectHudConfig.getInstance());
        this.offset = -getHandleValue(handle, id);
        this.screen = screen;
        float handleGap = 7f / baseSizeX;
        if (id != 0) min = colorSetting.getColorGradient().get(id - 1).getPos() + handleGap;
        if (id != colorSetting.getColorGradient().toArray().length - 1) max = colorSetting.getColorGradient().get(id + 1).getPos() - handleGap;
    }

    protected MutableComponent createNarrationMessage() {
        return Component.translatable("gui.narrate.slider", new Object[]{this.getMessage()});
    }

    public static float getHandleValue(VarHandle handle, int id) {
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(BetterEffectHudConfig.getInstance());
        return colorSetting.getColorGradient().get(id).getPos();
    }

    public void updateWidgetNarration(final NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    private Identifier getHandleOverlaySprite() {
        return this.isActive() && (this.isHovered() || this.isFocused()) ? BetterEffectHud.id("textures/gui/gradient_handle_overlay_highlighted.png") : BetterEffectHud.id("textures/gui/gradient_handle_overlay.png");
    }

    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BetterEffectHud.id("textures/gui/gradient_handle.png"), this.getOffsetPosX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight(), colorSetting.getColorGradient().get(id).getColor());
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.getHandleOverlaySprite(), this.getOffsetPosX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
        this.extractScrollingStringOverContents(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE), this.getMessage(), 2);
        this.handleCursor(graphics);
    }

    public int getOffsetPosX() {
        return Math.round(this.getX() + (colorSetting.getColorGradient().get(id).getPos() + offset) * baseSizeX);
    }

    protected void handleCursor(final GuiGraphicsExtractor graphics) {}

    public void onClick(final MouseButtonEvent event, final boolean doubleClick) {
        if (event.buttonInfo().button() == 3) {
            colorSetting.getColorGradient().remove(id);
            Minecraft.getInstance().setScreenAndShow(screen);
            return;
        }
        this.dragging = this.active;
        this.setValueFromMouse(event);
        if (doubleClick) {
            try {
                Minecraft.getInstance().setScreenAndShow(new BetterEffectHudColorEditScreen(handle, screen, id));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected boolean isValidClickButton(final MouseButtonInfo buttonInfo) {
        return buttonInfo.button() == 3 || buttonInfo.button() == 1;
    }

    private void setValueFromMouse(final MouseButtonEvent event) {
        this.setValue((event.x() - (double) this.getX() - 3) / (double) this.baseSizeX - offset);
    }

    protected void setValue(final double newValue) {
        float oldValue = colorSetting.getColorGradient().get(id).getPos();
        colorSetting.getColorGradient().set(id, colorSetting.getColorGradient().get(id).setPos((float) Mth.clamp(newValue, min, max)));
        if (oldValue != colorSetting.getColorGradient().get(id).getPos()) this.applyValue();
    }

    protected void onDrag(final MouseButtonEvent event, final double dx, final double dy) {
        this.setValueFromMouse(event);
        super.onDrag(event, dx, dy);
    }

    public void playDownSound(final SoundManager soundManager) {
    }

    public void onRelease(final MouseButtonEvent event) {
        this.dragging = false;
        super.playDownSound(Minecraft.getInstance().getSoundManager());
        if (!this.isHovered()) Minecraft.getInstance().setScreenAndShow(this.screen);
    }

    protected void applyValue() {
        handle.set(BetterEffectHudConfig.getInstance(), colorSetting);
        try {
            BetterEffectHudConfig.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
}
