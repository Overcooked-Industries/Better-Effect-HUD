package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;

import java.lang.invoke.VarHandle;

/*
TODO: Use a Virtual Value system
You have to store an addition value that represents the value you calculate and store
This one will have to be updated alongside the canonical value inside setValueFromMouse()
This will be used for serialization and deserialization
You'll have to additionally track the current value but only for the animation
Look into thresholds and discrete-s
Why do this? Simple: You lack precision when you don't do it this way! This is too lossy and cannot be released to production!
the rest you'll figure out on your own ;)
*/
public class BetterEffectHudIntSlider extends AbstractSliderButton {
    String text;
    VarHandle handle;
    int min;
    int max;

    public BetterEffectHudIntSlider(int x, int y, int width, int height, String text, String field, int min, int max) throws NoSuchFieldException, IllegalAccessException {
        super(x, y, width, height, Component.literal(""),  (double) ((int) BetterEffectHudConfig.getField(field, Integer.class) - min) / (max - min));
        this.handle = BetterEffectHudConfig.varHandle(field, Integer.class);
        this.text = text;
        this.min = min;
        this.max = max;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format(text, Math.round(min + (max - min) * this.value))));
    }

    @Override
    protected void applyValue() {
        handle.set(BetterEffectHudConfig.getInstance(), (int) Math.round(min + (max - min) * this.value));
        BetterEffectHudConfig.serialize();
    }
}
