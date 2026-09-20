package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;

public abstract class BetterEffectHudAbstractTab extends GridLayoutTab  {
    int page;
    BetterEffectHudConfigScreen screen;

    public BetterEffectHudAbstractTab(Screen parent, Component title, int page) {
        super(title);
        this.page = page;
        this.screen = new BetterEffectHudConfigScreen(page, parent);
    }
}
