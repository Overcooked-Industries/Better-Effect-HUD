package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

public class GeneralTab extends BetterEffectHudAbstractTab {
    public GeneralTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            layout.addChild(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.general.toggle_button.enable_mod"), 55, 34, 200, 21, BetterEffectHudAlignment.TOP_LEFT, "enableMod", screen, false), 0, 0);
            layout.addChild(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.general.toggle_button.enable_experimental"), 55, 55, 200, 21, BetterEffectHudAlignment.TOP_LEFT, "enableExperimental", screen, false), 0, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}