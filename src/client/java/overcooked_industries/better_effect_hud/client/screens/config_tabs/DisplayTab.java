package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorModes;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;

import java.util.*;

public class DisplayTab extends BetterEffectHudAbstractTab {
    public DisplayTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.display.label.display"), -100, 34, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_LEFT, true));
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(-90, 44, 21, 21, BetterEffectHudConfig.varHandle("hudAlignment", BetterEffectHudAlignment.class), screen, 'e', BetterEffectHudAlignment.TOP_MIDDLE));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.display.toggle_button.hud_stack_y"), 0, 117, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("hudStackY", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newColorModeButton(Component.translatable("better_effect_hud.hud.category.display.color_mode_button.background_color_mode"), BetterEffectHudConfig.varHandle("backgroundColor", BetterEffectHudColorSetting.class), BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen, (BetterEffectHudConfig.getInstance().backgroundColor.getColorMode() == BetterEffectHudColorModes.CUSTOM) ? -105 : 0, 148, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE,false));
            if (BetterEffectHudConfig.getInstance().backgroundColor.getColorMode() == BetterEffectHudColorModes.CUSTOM) widgets.addAll(BetterEffectHudAbstractScreen.newColorEditBox(105, 148, 200, 21, BetterEffectHudConfig.varHandle("backgroundColor", BetterEffectHudColorSetting.class), BetterEffectHudAlignment.TOP_MIDDLE, screen));
            for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}