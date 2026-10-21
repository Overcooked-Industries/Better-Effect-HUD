package overcooked_industries.better_effect_hud.client;

import java.util.Arrays;
import java.util.Objects;

public enum BetterEffectHudColorModes {
    EFFECT_BENEFIT,
    EFFECT_COLOR,
    EFFECT_DURATION,
    RAINBOW,
    MONOCHROME,
    CUSTOM_GRADIENT,
    CUSTOM_BLINK,
    CUSTOM;

    public BetterEffectHudColorModes next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public boolean matchesEffect(Boolean speed, Boolean color, Boolean colorGradient) {
        boolean out = true;
        if (speed != null && speed != Arrays.asList(RAINBOW, MONOCHROME, CUSTOM_GRADIENT, CUSTOM_BLINK).contains(this)) out = false;
        if (color != null && color != Arrays.asList(CUSTOM_BLINK, CUSTOM).contains(this)) out = false;
        if (colorGradient != null && colorGradient != Objects.equals(CUSTOM_GRADIENT, this)) out = false;
        return out;
    }
}
