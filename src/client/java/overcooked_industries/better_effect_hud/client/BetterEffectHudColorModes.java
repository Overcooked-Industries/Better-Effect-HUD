package overcooked_industries.better_effect_hud.client;

public enum BetterEffectHudColorModes {
    EFFECT_BENEFIT,
    EFFECT_COLOR,
    EFFECT_DURATION,
    RAINBOW,
    MONOCHROME,
    CUSTOM_BLINK,
    CUSTOM;

    public BetterEffectHudColorModes next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
