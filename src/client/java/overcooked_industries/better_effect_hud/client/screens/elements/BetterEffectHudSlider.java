package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfig;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class BetterEffectHudSlider extends AbstractSliderButton {
    String text;
    VarHandle handle;
    double min;
    double max;

    public BetterEffectHudSlider(int x, int y, int width, int height, String text, String field, double min, double max) throws NoSuchFieldException, IllegalAccessException {
        VarHandle handle = MethodHandles.lookup().findVarHandle(BetterEffectHudConfig.class, field, double.class);
        this.handle = handle;
        this.text = text;
        this.min = min;
        this.max = max;
        super(x, y, width, height, Component.literal(""), (double) handle.get(BetterEffectHudConfig.getInstance()));
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format(text, min + ((max - min) * this.value))));
    }

    @Override
    protected void applyValue() {
        handle.set(BetterEffectHudConfig.getInstance(), min + ((max - min) * this.value));
        BetterEffectHudConfig.serialize();
        double value = this.value;
    }
}
