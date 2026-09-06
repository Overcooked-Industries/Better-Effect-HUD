package overcooked_industries.better_effect_hud.client.config;

import net.minecraft.resources.Identifier;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

public class BetterEffectHudConfig {
    //General
    boolean enableMod = true;

    //Info
    boolean enableTimer = false;
    boolean enablePotency = false;
    BetterEffectHudAlignment timerPos = BetterEffectHudAlignment.BOTTOM_MIDDLE;
    BetterEffectHudAlignment potencyPos = BetterEffectHudAlignment.TOP_RIGHT;
    int timerColor = 0x000000AA;
    int potencyColor = 0x000000AA;
    boolean autoTimerColor = true;
    boolean autoPotencyColor = true;
    double timerSize = 1.0;
    double potencySize = 1.0;

    //Duration Bar
    boolean enableDurationBar = false;

     //Warning
    boolean enableWarning = false;
    Identifier[] effectsIdentifiers = {Identifier.withDefaultNamespace("strength"), Identifier.withDefaultNamespace("speed")};

    //Position
    BetterEffectHudAlignment hudAlignment = BetterEffectHudAlignment.TOP_RIGHT;
    double hudSize = 1.0;
}
