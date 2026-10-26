package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;

public class BetterEffectHudDurationBarTab extends BetterEffectHudAbstractTab {
    public BetterEffectHudDurationBarTab(Screen parent) {
        super(parent, Component.translatable("better_effect_hud.hud.category.duration_bar"));
    }

    public void initialize() throws NoSuchFieldException, IllegalAccessException {
        String directionComponent = Component.translatable(String.format("better_effect_hud.hud.direction.%s", BetterEffectHudConfig.getInstance().barLocationId)).getString();
        String barLocationComponentId = Component.translatable("better_effect_hud.hud.category.duration_bar.button.bar_location_id").getString();
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.duration_bar.toggle_button.enable_duration_bar"), -105, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableDurationBar", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.duration_bar.toggle_button.enable_duration_bar")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newSlider(Component.translatable("better_effect_hud.hud.category.duration_bar.slider.bar_background_opacity"), 105, 34, 200, 21, BetterEffectHudConfig.varHandle("barBackgroundOpacity", double.class), 0, 1, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), Component.translatable("better_effect_hud.hud.help.category.duration_bar.slider.bar_background_opacity")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.duration_bar.toggle_button.bar_reverse"), -105, 65, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("barReverse", Boolean.class), screen, BetterEffectHudConfig.getInstance().barCircle, Component.translatable("better_effect_hud.hud.help.category.duration_bar.toggle_button.bar_reverse")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(105, 65, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, Component.literal(String.format(barLocationComponentId, directionComponent)), (btn) -> {
            BetterEffectHudConfig.getInstance().barLocationId = (BetterEffectHudConfig.getInstance().barLocationId - 1 + 4) % 4;
            BetterEffectHudConfig.serialize();
            Minecraft.getInstance().setScreenAndShow(screen);
        }, BetterEffectHudConfig.getInstance().barCircle, Component.translatable("better_effect_hud.hud.help.category.duration_bar.button.bar_location_id")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.duration_bar.toggle_button.bar_out"), -105, 96, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("barOut", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.duration_bar.toggle_button.bar_out")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.duration_bar.toggle_button.bar_circle"), 105, 96, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("barCircle", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.duration_bar.toggle_button.bar_circle")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newColorSetting(Component.translatable("better_effect_hud.hud.category.duration_bar.color_mode_button.bar_color_mode"), BetterEffectHudConfig.varHandle("barColor", BetterEffectHudColorSetting.class), BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen, -105, 127, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.MIDDLE_RIGHT, false, Component.translatable("better_effect_hud.hud.help.category.duration_bar.color_mode_button.bar_color_mode")));
    }
}