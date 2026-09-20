package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class BetterEffectHudAbstractTabScreen extends BetterEffectHudAbstractScreen {
    int page;
    Screen parent;

    public BetterEffectHudAbstractTabScreen(int page, Screen parent) {
        super();
        this.page = page;
        this.parent = parent;
    }
}
