package overcooked_industries.better_effect_hud.client.config;

import net.minecraft.resources.Identifier;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

public class BetterEffectHudConfig {
    private static BetterEffectHudConfig instance = null;

    //Singleton
    public static BetterEffectHudConfig getInstance() {
        if (instance == null) instance = new BetterEffectHudConfig();
        return instance;
    }

    //Must be private.
    private BetterEffectHudConfig() {}

    /// Settings for each Tab.
    //General
    public Boolean enableMod = true;

    //Info
    public Boolean enableTimer = false;
    public Boolean enablePotency = false;
    public BetterEffectHudAlignment timerPos = BetterEffectHudAlignment.BOTTOM_MIDDLE;
    public BetterEffectHudAlignment potencyPos = BetterEffectHudAlignment.TOP_RIGHT;
    public int timerColor = 0x000000AA;
    public int potencyColor = 0x000000AA;
    public Boolean autoTimerColor = true;
    public Boolean autoPotencyColor = true;
    public double timerSize = 1.0;
    public double potencySize = 1.0;

    //Duration Bar
    public Boolean enableDurationBar = false;

    //Warning
    public Boolean enableWarning = false;
    public Identifier[] effectsIdentifiers = {Identifier.withDefaultNamespace("strength"), Identifier.withDefaultNamespace("speed")};

    //Position
    public BetterEffectHudAlignment hudAlignment = BetterEffectHudAlignment.TOP_RIGHT;
    public double hudSize = 1.0;
}
