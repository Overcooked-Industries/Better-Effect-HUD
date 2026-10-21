package overcooked_industries.better_effect_hud.client;

public class BetterEffectHudGradientElement {
    int color;
    float pos;

    public BetterEffectHudGradientElement(int color, float pos) {
        this.color = color;
        this.pos = pos;
    }

    public BetterEffectHudGradientElement setColor(int color) {
        this.color = color;
        return this;
    }
    public BetterEffectHudGradientElement setPos(float pos) {
        this.pos = pos;
        return this;
    }

    public int getColor() {
        return color;
    }
    public float getPos() {
        return pos;
    }
}
