package overcooked_industries.better_effect_hud.client;

import java.util.ArrayList;
import java.util.List;

public class BetterEffectHudColorSetting {
    int color;
    ArrayList<BetterEffectHudColorMode> allowedModes = new ArrayList<>(List.of(BetterEffectHudColorMode.EFFECT_BENEFIT, BetterEffectHudColorMode.EFFECT_COLOR, BetterEffectHudColorMode.EFFECT_DURATION, BetterEffectHudColorMode.EFFECT_POTENCY, BetterEffectHudColorMode.RAINBOW, BetterEffectHudColorMode.MONOCHROME, BetterEffectHudColorMode.CUSTOM_GRADIENT, BetterEffectHudColorMode.CUSTOM_BLINK, BetterEffectHudColorMode.CUSTOM));
    ArrayList<BetterEffectHudGradientElement> colorGradient;
    float speed;
    BetterEffectHudColorMode colorMode;

    BetterEffectHudColorSetting(int color, float speed, BetterEffectHudColorMode colorMode, ArrayList<BetterEffectHudGradientElement> colorGradient) {
        this.color = color;
        this.speed = speed;
        this.colorMode = colorMode;
        this.colorGradient = colorGradient;
    }

    BetterEffectHudColorSetting(int color, float speed, BetterEffectHudColorMode colorMode, ArrayList<BetterEffectHudGradientElement> colorGradient, ArrayList<BetterEffectHudColorMode> allowedModes) {
        this.color = color;
        this.speed = speed;
        this.colorMode = colorMode;
        this.colorGradient = colorGradient;
        this.allowedModes = allowedModes;
    }

    public int getColor() {
        return this.color;
    }
    public float getSpeed() {
        return this.speed;
    }
    public BetterEffectHudColorMode getColorMode() {
        if (!allowedModes.contains(colorMode)) return getNextValidColorMode(colorMode);
        return this.colorMode;
    }
    public ArrayList<BetterEffectHudGradientElement> getColorGradient() {
        return colorGradient;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public void setColorMode(BetterEffectHudColorMode colorMode) {
        this.colorMode = colorMode;
        if (!allowedModes.contains(colorMode)) this.colorMode = getNextValidColorMode(colorMode);
    }
    public void setColorGradient(ArrayList<BetterEffectHudGradientElement> colorGradient) {
        this.colorGradient = colorGradient;
    }

    private BetterEffectHudColorMode getNextValidColorMode(BetterEffectHudColorMode colorMode) {
        BetterEffectHudColorMode outMode = BetterEffectHudColorMode.CUSTOM;
        BetterEffectHudColorMode tempMode = colorMode;
        for (int i = 0; i < 10; i++) {
            tempMode = tempMode.next();
            if (this.allowedModes.contains(tempMode)) {
                outMode = tempMode;
                break;
            }
        }
        return outMode;

    }
}
