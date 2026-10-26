package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudEffectEditScreen;

public class BetterEffectHudDisplayTab extends BetterEffectHudAbstractTab {
    public BetterEffectHudDisplayTab(Screen parent) {
        super(parent, Component.translatable("better_effect_hud.hud.category.display"));
    }

    public void initialize() throws NoSuchFieldException, IllegalAccessException {
        BetterEffectHudEffectEditScreen betterEffectHudEffectEditScreen = new BetterEffectHudEffectEditScreen("extraEffectIdentifiers", screen);
        addRenderableWidget(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.display.label.hud_alignment"), 10, 25, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudAlignment.TOP_LEFT, true));
        addRenderableWidget(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.display.label.extra_hud_alignment"), -10, 25, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_RIGHT, BetterEffectHudAlignment.TOP_RIGHT, true));
        if (Minecraft.getInstance().getWindow().getGuiScale() > 3) {
            addRenderableWidget(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 34, 7, 7, BetterEffectHudConfig.varHandle("hudAlignment", BetterEffectHudAlignment.class), screen, 'e', BetterEffectHudAlignment.TOP_LEFT, true));
            addRenderableWidget(BetterEffectHudAbstractScreen.newAlignmentButtons(-24, 34, 7, 7, BetterEffectHudConfig.varHandle("extraHudAlignment", BetterEffectHudAlignment.class), screen, '6', BetterEffectHudAlignment.TOP_RIGHT, true));
        } else {
            addRenderableWidget(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 34, 21, 21, BetterEffectHudConfig.varHandle("hudAlignment", BetterEffectHudAlignment.class), screen, 'e', BetterEffectHudAlignment.TOP_LEFT, false));
            addRenderableWidget(BetterEffectHudAbstractScreen.newAlignmentButtons(-52, 34, 21, 21, BetterEffectHudConfig.varHandle("extraHudAlignment", BetterEffectHudAlignment.class), screen, '6', BetterEffectHudAlignment.TOP_RIGHT, false));
        }

        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.display.toggle_button.hud_stack_y"), -105, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("hudStackY", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.display.toggle_button.hud_stack_y")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.display.toggle_button.extra_hud_stack_y"), 105, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("extraHudStackY", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.display.toggle_button.extra_hud_stack_y")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.display.toggle_button.hud_invert"), -105, 65, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("hudInvert", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.display.toggle_button.hud_invert")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.display.toggle_button.extra_hud_invert"), 105, 65, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("extraHudInvert", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.display.toggle_button.extra_hud_invert")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newColorSetting(Component.translatable("better_effect_hud.hud.category.display.color_mode_button.background_color_mode"), BetterEffectHudConfig.varHandle("backgroundColor", BetterEffectHudColorSetting.class), BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen, -105, 96, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.BOTTOM_MIDDLE, false, Component.translatable("better_effect_hud.hud.help.category.display.color_mode_button.background_color_mode")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newColorSetting(Component.translatable("better_effect_hud.hud.category.display.color_mode_button.extra_background_color_mode"), BetterEffectHudConfig.varHandle("extraBackgroundColor", BetterEffectHudColorSetting.class), BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen, 105, 96, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.BOTTOM_MIDDLE, false, Component.translatable("better_effect_hud.hud.help.category.display.color_mode_button.extra_background_color_mode")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(0, 158, 410, 21, BetterEffectHudAlignment.TOP_MIDDLE, Component.translatable("better_effect_hud.hud.category.display.button.edit_extra_effects"), (btn) -> {Minecraft.getInstance().setScreenAndShow(betterEffectHudEffectEditScreen);}, false, Component.translatable("better_effect_hud.hud.help.category.display.button.edit_extra_effects")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.display.toggle_button.background_hide"), -105, 189, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("backgroundHide", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.display.toggle_button.background_hide")));
        addRenderableWidget(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.display.toggle_button.background_blink"), 105, 189, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("backgroundBlink", Boolean.class), screen, false, Component.translatable("better_effect_hud.hud.help.category.display.toggle_button.background_blink")));
    }
}