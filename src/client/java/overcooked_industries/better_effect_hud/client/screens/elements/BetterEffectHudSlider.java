package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;

import java.lang.invoke.VarHandle;

public class BetterEffectHudSlider extends AbstractSliderButton {
    String text;
    VarHandle handle;
    double min;
    double max;
    BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction;
    Object configObject;

    public BetterEffectHudSlider(int x, int y, int width, int height, String text, VarHandle handle, double min, double max, BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction, Object configObject, Component tooltip) {
        this.handle = handle;
        this.text = text;
        this.min = min;
        this.max = max;
        this.updateFunction = updateFunction;
        this.configObject = configObject;
        super(x, y, width, height, Component.empty(), ((double) handle.get(configObject) - min) / (max - min));
        updateMessage();
        setTooltip(Tooltip.create(tooltip));
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format(text, String.format("%.2f", min + ((max - min) * this.value)))));
    }

    @Override
    protected void applyValue() {
        handle.set(configObject, min + ((max - min) * this.value));
        updateFunction.call();
    }

    public void update() {
        value = ((double) handle.get(configObject) - min) / (max - min);
        updateMessage();
    }
}
