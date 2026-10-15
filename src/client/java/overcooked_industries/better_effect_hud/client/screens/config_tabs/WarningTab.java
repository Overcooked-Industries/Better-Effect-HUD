package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudEffectEditScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;

import java.util.ArrayList;
import java.util.List;

public class WarningTab extends BetterEffectHudAbstractTab {
    public WarningTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            BetterEffectHudEffectEditScreen betterEffectHudEffectEditScreen = new BetterEffectHudEffectEditScreen("effectWarningIdentifiers", new BetterEffectHudConfigScreen(3, parent));
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.enable_warning"), -105, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableWarning", Boolean.class), new BetterEffectHudConfigScreen(3, parent), false));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.enable_vignette"), 105, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableVignette", Boolean.class), new BetterEffectHudConfigScreen(3, parent), false));
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.warning_time"), -105, 65, 200, 21, BetterEffectHudConfig.varHandle("warningTime", Integer.class), 0, 60, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance()));
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.vignette_time"), 105, 65, 200, 21, BetterEffectHudConfig.varHandle("vignetteTime", Integer.class), 0, 60, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance()));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.use_effect_list_warning"), -105, 96, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("useEffectListWarning", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.vignette_transparency"), 105, 96, 200, 21, BetterEffectHudConfig.varHandle("vignetteTransparency", Integer.class), 0, 100, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance()));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.category.warning.button.edit_effects"), (btn) -> {Minecraft.getInstance().setScreenAndShow(betterEffectHudEffectEditScreen);}, 0, 127, 410, 21, BetterEffectHudAlignment.TOP_MIDDLE, false));
            for (AbstractWidget widget : widgets) layout.addChild(widget, 0, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}