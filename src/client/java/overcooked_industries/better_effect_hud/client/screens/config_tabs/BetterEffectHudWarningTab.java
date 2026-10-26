package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudEffectEditScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;

public class BetterEffectHudWarningTab extends BetterEffectHudAbstractTab {
    public BetterEffectHudWarningTab(Screen parent) {
        super(parent, Component.translatable("better_effect_hud.hud.category.warning"));
    }
    public void initialize() throws NoSuchFieldException, IllegalAccessException {
        BetterEffectHudEffectEditScreen betterEffectHudEffectEditScreen = new BetterEffectHudEffectEditScreen("effectWarningIdentifiers", screen);
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.enable_warning"), -160, 34, 150, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableWarning", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.warning.toggle_button.enable_warning")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.enable_vignette"), 0, 34, 150, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableVignette", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.warning.toggle_button.enable_vignette")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.enable_sound"), 160, 34, 150, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableSound", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.warning.toggle_button.enable_sound")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.warning_time"), -160, 65, 150, 21, BetterEffectHudConfig.varHandle("warningTime", Integer.class), 0, 60, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), Component.translatable("better_effect_hud.hud.help.category.warning.slider.warning_time")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.vignette_time"), 0, 65, 150, 21, BetterEffectHudConfig.varHandle("vignetteTime", Integer.class), 0, 60, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), Component.translatable("better_effect_hud.hud.help.category.warning.slider.vignette_time")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.sound_time"), 160, 65, 150, 21, BetterEffectHudConfig.varHandle("soundTime", Integer.class), 0, 60, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), Component.translatable("better_effect_hud.hud.help.category.warning.slider.sound_time")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(-160, 96, 150, 21, BetterEffectHudAlignment.TOP_MIDDLE, Component.translatable("better_effect_hud.hud.category.warning.button.edit_effects"), (btn) -> Minecraft.getInstance().setScreenAndShow(betterEffectHudEffectEditScreen), false, Component.translatable("better_effect_hud.hud.help.category.warning.button.edit_effects")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.warning.slider.vignette_transparency"), 0, 96, 150, 21, BetterEffectHudConfig.varHandle("vignetteTransparency", Integer.class), 0, 100, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), Component.translatable("better_effect_hud.hud.help.category.warning.slider.vignette_transparency")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.warning.toggle_button.loop_sound"), 160, 96, 150, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("loopSound", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.warning.toggle_button.loop_sound")));
    }
}