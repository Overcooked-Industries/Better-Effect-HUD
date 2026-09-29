package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudEffectEditScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.ArrayList;
import java.util.List;

public class WarningTab extends BetterEffectHudAbstractTab {
    public WarningTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            BetterEffectHudEffectEditScreen betterEffectHudEffectEditScreen = new BetterEffectHudEffectEditScreen("effectWarningIdentifiers", new BetterEffectHudConfigScreen(3, parent));
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.enable_warning"), 10, 34, 200, 21, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudConfig.varHandle("enableWarning", Boolean.class), new BetterEffectHudConfigScreen(3, parent), false));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.enable_vignette"), 220, 34, 200, 21, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudConfig.varHandle("enableVignette", Boolean.class), new BetterEffectHudConfigScreen(3, parent), false));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.slider.use_effect_list_warning"), 10, 65, 200, 21, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudConfig.varHandle("useEffectListWarning", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.warning_time"), 10, 96, 200, 21, "warningTime", 0, 60));
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.vignette_transparency"), 220, 96, 200, 21, "vignetteTransparency", 0, 100));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.category.warning.button.edit_effects"), (btn) -> {Minecraft.getInstance().setScreenAndShow(betterEffectHudEffectEditScreen);}, 10, 127, 200, 21, BetterEffectHudAlignment.TOP_LEFT, false));
            for (AbstractWidget widget : widgets) layout.addChild(widget, 0, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}