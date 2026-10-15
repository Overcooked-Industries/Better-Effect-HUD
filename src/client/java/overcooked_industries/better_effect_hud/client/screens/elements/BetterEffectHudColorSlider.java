package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor.HoveredTextEffects;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;

import java.lang.invoke.VarHandle;

public class BetterEffectHudColorSlider extends AbstractWidget.WithInactiveMessage {
    Identifier backgroundSprite;
    int value;
    boolean canChangeValue;
    boolean dragging;
    VarHandle handle;
    BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction;
    Object configObject;
    int color;

    public void update() {
        value = (int) handle.get(configObject);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public BetterEffectHudColorSlider(int posX, int posY, Identifier backgroundSprite, VarHandle handle, BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction, Object configObject) {
        super(posX, posY, 7, 100, Component.empty());
        this.value = (int) handle.get(configObject);
        this.backgroundSprite = backgroundSprite;
        this.handle = handle;
        this.updateFunction = updateFunction;
        this.configObject = configObject;
        this.color = ARGB.white(255);
    }

    protected MutableComponent createNarrationMessage() {
        return Component.translatable("gui.narrate.slider", new Object[]{this.getMessage()});
    }

    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.backgroundSprite, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight(), color);
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BetterEffectHud.id("textures/gui/color_slider.png"),
                this.getX() + 1, (int) (this.getY() + ((double) this.value / 255 * this.getHeight()) - 1),
                0, 0, 5, 3, 5, 3,
                ARGB.white(this.alpha)
        );
        this.extractScrollingStringOverContents(graphics.textRendererForWidget(this, HoveredTextEffects.NONE), this.getMessage(), 2);
        this.handleCursor(graphics);
    }

    protected void handleCursor(final GuiGraphicsExtractor graphics) {
        if (this.isHovered()) {
            graphics.requestCursor(this.isActive() ? (this.dragging ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND) : CursorTypes.NOT_ALLOWED);
        }
    }

    public void onClick(final MouseButtonEvent event, final boolean doubleClick) {
        this.dragging = this.active;
        this.setValueFromMouse(event);
    }

    public void setFocused(final boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.canChangeValue = false;
        } else {
            InputType lastInputType = Minecraft.getInstance().getLastInputType();
            if (lastInputType == InputType.MOUSE || lastInputType == InputType.KEYBOARD_TAB) {
                this.canChangeValue = true;
            }

        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {}

    public boolean keyPressed(final KeyEvent event) {
        if (event.isSelection()) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        } else {
            if (this.canChangeValue) {
                boolean left = event.isLeft();
                boolean right = event.isRight();
                if (left || right) {
                    int direction = left ? -1 : 1;
                    this.setValue(
                            this.value + direction
                    );
                    return true;
                }
            }

            return false;
        }
    }

    private void setValueFromMouse(final MouseButtonEvent event) {
        this.setValue(
                (int) (((event.y() - (double) this.getY()) / getHeight()) * 255)
        );
    }

    protected void setValue(final int newValue) {
        double oldValue = this.value;
        this.value = Mth.clamp(newValue, 0, 255);
        if (oldValue != this.value) this.applyValue();
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
    }

    protected void applyValue() {
        handle.set(configObject, (Integer) value);
        try {
            updateFunction.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
}
