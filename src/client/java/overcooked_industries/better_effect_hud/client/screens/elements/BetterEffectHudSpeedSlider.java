package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;

import java.lang.invoke.VarHandle;

public class BetterEffectHudSpeedSlider extends AbstractSliderButton {
    String text;
    VarHandle handle;
    BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction;
    Object configObject;

    public BetterEffectHudSpeedSlider(int x, int y, int width, int height, VarHandle handle, BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction, Object configObject) {
        this.handle = handle;
        this.text = Component.translatable("better_effect_hud.hud.color.slider").getString();
        this.updateFunction = updateFunction;
        this.configObject = configObject;
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(configObject);
        super(x, y, width, height, Component.empty(), colorSetting.getSpeed());
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(configObject);
        this.setMessage(Component.literal(String.format(text, String.format("%.2f", colorSetting.getSpeed()))));
    }

    @Override
    protected void applyValue() {
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(configObject);
        colorSetting.setSpeed((float) this.value);
        handle.set(configObject, colorSetting);
        updateFunction.call();
    }

    public void update() {
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(configObject);
        value = colorSetting.getSpeed();
        updateMessage();
    }
}
