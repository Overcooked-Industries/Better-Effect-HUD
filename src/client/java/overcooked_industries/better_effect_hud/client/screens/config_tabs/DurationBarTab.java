package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.ArrayList;
import java.util.List;

public class DurationBarTab extends BetterEffectHudAbstractTab {
    public DurationBarTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.duration_bar.toggle_button.enable_duration_bar"), 10, 34, 200, 21, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudConfig.varHandle("enableDurationBar", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newSlider(Component.translatable("better_effect_hud.hud.category.duration_bar.slider.bar_background_brightness"), 10, 65, 200, 21, "barBackgroundBrightness", 0, 1));
            for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}