package overcooked_industries.better_effect_hud.client;

import java.util.ArrayList;

public class BetterEffectHudColorSetting {
    int color;
    ArrayList<BetterEffectHudGradientElement> colorGradient;
    float speed;
    BetterEffectHudColorModes colorMode;

    BetterEffectHudColorSetting(int color, float speed, BetterEffectHudColorModes colorMode, ArrayList<BetterEffectHudGradientElement> colorGradient) {
        this.color = color;
        this.speed = speed;
        this.colorMode = colorMode;
        this.colorGradient = colorGradient;
    }

    public int getColor() {
        return this.color;
    }
    public float getSpeed() {
        return this.speed;
    }
    public BetterEffectHudColorModes getColorMode() {
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
    public void setColorMode(BetterEffectHudColorModes colorMode) {
        this.colorMode = colorMode;
    }
    public void setColorGradient(ArrayList<BetterEffectHudGradientElement> colorGradient) {
        this.colorGradient = colorGradient;
    }
}
