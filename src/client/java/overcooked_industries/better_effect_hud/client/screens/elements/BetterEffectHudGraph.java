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
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;

import java.lang.invoke.VarHandle;

public class BetterEffectHudGraph extends AbstractWidget.WithInactiveMessage {
    Identifier backgroundSprite;
    Identifier cursorSprite;
    int valueX;
    int valueY;
    boolean canChangeValue;
    boolean dragging;
    int cursorSizeX;
    int cursorSizeY;
    VarHandle handleX;
    VarHandle handleY;
    BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction;
    Object configObject;
    int color;

    public void update() {
        valueX = (int) handleX.get(configObject);
        valueY = (int) handleY.get(configObject);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public BetterEffectHudGraph(int posX, int posY, int sizeX, int sizeY, int cursorSizeX, int cursorSizeY, Component message, Identifier backgroundSprite, Identifier cursorSprite, VarHandle handleX, VarHandle handleY, BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction, Object configObject) {
        super(posX, posY, sizeX, sizeY, message);
        this.valueX = (int) handleX.get(configObject);
        this.valueY = (int) handleY.get(configObject);
        this.backgroundSprite = backgroundSprite;
        this.cursorSprite = cursorSprite;
        this.cursorSizeX = cursorSizeX;
        this.cursorSizeY = cursorSizeY;
        this.handleX = handleX;
        this.handleY = handleY;
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
                this.cursorSprite,
                (int) (this.getX() + ((double) this.valueX / 255 * this.getWidth()) - ((double) cursorSizeX / 2)),
                (int) (this.getY() + ((double) this.valueY / 255 * this.getHeight()) - ((double) cursorSizeY / 2)),
                0, 0, cursorSizeX, cursorSizeY, cursorSizeX, cursorSizeY,
                ARGB.white(this.alpha)
        );
        this.extractScrollingStringOverContents(graphics.textRendererForWidget(this, HoveredTextEffects.NONE), this.getMessage(), 2);
        this.handleCursor(graphics);
    }

    protected void handleCursor(final GuiGraphicsExtractor graphics) {
        if (this.isHovered()) {
            graphics.requestCursor(this.isActive() ? (this.dragging ? CursorTypes.POINTING_HAND : CursorTypes.RESIZE_ALL) : CursorTypes.NOT_ALLOWED);
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
                            this.valueX + direction,
                            this.valueY + direction
                    );
                    return true;
                }
            }
            return false;
        }
    }

    private void setValueFromMouse(final MouseButtonEvent event) {
        this.setValue(
                (int) (((event.x() - (double) this.getX()) / getWidth()) * 255),
                (int) (((event.y() - (double) this.getY()) / getHeight()) * 255)
        );
    }

    protected void setValue(final int newValueX, final int newValueY) {
        double oldValueX = this.valueX;
        double oldValueY = this.valueY;
        this.valueX = Mth.clamp(newValueX, 0, 255);
        this.valueY = Mth.clamp(newValueY, 0, 255);
        if (oldValueX != this.valueX || oldValueY != this.valueY) this.applyValue();
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
        handleX.set(configObject, (Integer) valueX);
        handleY.set(configObject, (Integer) valueY);
        try {
            updateFunction.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
}
