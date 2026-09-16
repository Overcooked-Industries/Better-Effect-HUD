package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfig;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class BetterEffectHudIntSlider extends AbstractSliderButton {
    String text;
    VarHandle handle;
    int min;
    int max;

    public BetterEffectHudIntSlider(int x, int y, int width, int height, String text, String field, int min, int max) throws NoSuchFieldException, IllegalAccessException {
        VarHandle handle = MethodHandles.lookup().findVarHandle(BetterEffectHudConfig.class, field, Integer.class);
        this.handle = handle;
        this.text = text;
        this.min = min;
        this.max = max;
        System.out.println((int) handle.get(BetterEffectHudConfig.getInstance()));


        super(x, y, width, height, Component.literal(""),  ((double) handle.get(BetterEffectHudConfig.getInstance()) - min) / (max - min));
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format(text, min + (max - min) * this.value)));
    }

    @Override
    protected void applyValue() {
        System.out.println(value);
        handle.set(BetterEffectHudConfig.getInstance(), (int) Math.floor(min + (max - min) * this.value));
        BetterEffectHudConfig.serialize();
    }
}
