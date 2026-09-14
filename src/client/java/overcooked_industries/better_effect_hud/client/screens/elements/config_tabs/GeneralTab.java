package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

public class GeneralTab extends GridLayoutTab {
    private static final Component TITLE = Component.literal("General Settings");

    public GeneralTab(Screen parent) {
        super(TITLE);
        try {
            layout.addChild(BetterEffectHudScreen.newToggleButton(Component.literal("Enable Mod"), 10, 34, 100, 21, BetterEffectHudAlignment.TOP_LEFT, "enableMod", new BetterEffectHudConfigScreen(Component.literal("Config Screen"), 0, parent)), 0, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}