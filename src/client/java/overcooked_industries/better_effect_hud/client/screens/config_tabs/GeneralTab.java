package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import java.util.ArrayList;
import java.util.List;

public class GeneralTab extends BetterEffectHudAbstractTab {
    public GeneralTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.general.toggle_button.enable_mod"), 0, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableMod", Boolean.class), screen, false));
            for (AbstractWidget widget : widgets) {
                layout.addChild(widget, 0, 0);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}