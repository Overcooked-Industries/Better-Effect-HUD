package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudTimerUnitConfigScreen;

import java.util.ArrayList;
import java.util.List;

public class InfoTab extends BetterEffectHudAbstractTab {
    public InfoTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.timer_alignment"), 10, 25, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudAlignment.TOP_LEFT, true));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.potency_alignment"), -10, 25, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_RIGHT, BetterEffectHudAlignment.TOP_RIGHT, true));
            if (Minecraft.getInstance().getWindow().getGuiScale() > 3) {
                widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 34, 7, 7, BetterEffectHudConfig.varHandle("timerPos", BetterEffectHudAlignment.class), screen, 'a', BetterEffectHudAlignment.TOP_LEFT, true));
                widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(-24, 34, 7, 7, BetterEffectHudConfig.varHandle("potencyPos", BetterEffectHudAlignment.class), screen, 'c', BetterEffectHudAlignment.TOP_RIGHT, true));
            } else {
                widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 34, 21, 21, BetterEffectHudConfig.varHandle("timerPos", BetterEffectHudAlignment.class), screen, 'a', BetterEffectHudAlignment.TOP_LEFT, false));
                widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(-52, 34, 21, 21, BetterEffectHudConfig.varHandle("potencyPos", BetterEffectHudAlignment.class), screen, 'c', BetterEffectHudAlignment.TOP_RIGHT, false));
            }
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.info.toggle_button.enable_timer"), -105, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableTimer", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.info.toggle_button.enable_potency"), 105, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enablePotency", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.category.info.button.timer_unit_config"), (btn) -> {Minecraft.getInstance().setScreenAndShow(new BetterEffectHudTimerUnitConfigScreen(new BetterEffectHudConfigScreen(page, screen)));}, -105, 65, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, false));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.info.toggle_button.enable_first_potency"), 105, 65, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableFirstPotency", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.size"), -205, 88, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_LEFT, true));
            if (BetterEffectHudConfig.getInstance().enableCustomTimerSize) {
                widgets.add(BetterEffectHudAbstractScreen.newSlider(Component.translatable("better_effect_hud.hud.category.info.slider.size"), -105, 96, 200, 21, BetterEffectHudConfig.varHandle("infoSize", double.class), 0, 2, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance()));
            } else {
                int buttonSize = Math.round((float) 200 / (Minecraft.getInstance().getWindow().getGuiScale() * 2));
                for (int i = 0; i < Minecraft.getInstance().getWindow().getGuiScale() * 2; i++) {
                    float buttonSettingSize = 1.0F / Minecraft.getInstance().getWindow().getGuiScale() * (i + 1);
                    widgets.add(BetterEffectHudAbstractScreen.newButton(
                            Component.literal(String.format("%.2f", buttonSettingSize)), (btn) -> {
                                BetterEffectHudConfig.getInstance().infoSize = buttonSettingSize;
                                BetterEffectHudConfig.serialize();
                                Minecraft.getInstance().gui.setScreen(screen);
                            },
                            i * buttonSize - 205 + buttonSize / 2, 96, buttonSize, 21, BetterEffectHudAlignment.TOP_MIDDLE,
                            BetterEffectHudConfig.getInstance().infoSize == buttonSettingSize
                    ));
                }
            }
            widgets.add(BetterEffectHudAbstractScreen.newCheckbox(Component.translatable("better_effect_hud.hud.category.info.checkbox.enable_custom_size"), 13, 98, BetterEffectHudConfig.varHandle("enableCustomTimerSize", Boolean.class), screen, BetterEffectHudAlignment.TOP_MIDDLE));
            widgets.addAll(BetterEffectHudAbstractScreen.newColorSetting(Component.translatable("better_effect_hud.hud.category.info.color_mode_button.timer_color"), BetterEffectHudConfig.varHandle("timerColor", BetterEffectHudColorSetting.class), BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen, -105, 127, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.BOTTOM_MIDDLE, false));
            widgets.addAll(BetterEffectHudAbstractScreen.newColorSetting(Component.translatable("better_effect_hud.hud.category.info.color_mode_button.potency_color"), BetterEffectHudConfig.varHandle("potencyColor", BetterEffectHudColorSetting.class), BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen, 105, 127, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.BOTTOM_MIDDLE, false));
            for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}