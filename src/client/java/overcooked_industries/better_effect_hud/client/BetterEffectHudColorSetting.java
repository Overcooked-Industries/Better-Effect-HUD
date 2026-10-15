package overcooked_industries.better_effect_hud.client;

public class BetterEffectHudColorSetting {
    int color;
    float speed;
    BetterEffectHudColorModes colorMode;

    BetterEffectHudColorSetting(int color, float speed, BetterEffectHudColorModes colorMode) {
        this.color = color;
        this.speed = speed;
        this.colorMode = colorMode;
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

    public void setColor(int color) {
        this.color = color;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public void setColorMode(BetterEffectHudColorModes colorMode) {
        this.colorMode = colorMode;
    }
}
