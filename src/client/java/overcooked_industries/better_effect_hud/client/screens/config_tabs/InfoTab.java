package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudTimerUnitConfig;

import java.util.ArrayList;
import java.util.List;

public class InfoTab extends BetterEffectHudAbstractTab {
    public InfoTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.timer_alignment"), -67, 34, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_LEFT, true));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.potency_alignment"), 67, 34, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_RIGHT, true));
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(-58, 44, 21, 21, BetterEffectHudConfig.varHandle("timerPos", BetterEffectHudAlignment.class), screen, 'a', BetterEffectHudAlignment.TOP_MIDDLE));
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(15, 44, 21, 21, BetterEffectHudConfig.varHandle("potencyPos", BetterEffectHudAlignment.class), screen, 'c', BetterEffectHudAlignment.TOP_MIDDLE));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.toggle.long.off"), (btn) -> {BetterEffectHudConfig.getInstance().enableTimer = false; BetterEffectHudConfig.serialize(); Minecraft.getInstance().setScreenAndShow(screen);}, -53, 107, 31, 17, BetterEffectHudAlignment.TOP_MIDDLE, !BetterEffectHudConfig.getInstance().enableTimer));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.toggle.long.on"), (btn) -> {BetterEffectHudConfig.getInstance().enableTimer = true; BetterEffectHudConfig.serialize(); Minecraft.getInstance().setScreenAndShow(screen);}, -21, 107, 32, 17, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.getInstance().enableTimer));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.toggle.long.off"), (btn) -> {BetterEffectHudConfig.getInstance().enablePotency = false; BetterEffectHudConfig.serialize(); Minecraft.getInstance().setScreenAndShow(screen);}, 20, 107, 31, 17, BetterEffectHudAlignment.TOP_MIDDLE, !BetterEffectHudConfig.getInstance().enablePotency));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.toggle.long.on"), (btn) -> {BetterEffectHudConfig.getInstance().enablePotency = true; BetterEffectHudConfig.serialize(); Minecraft.getInstance().setScreenAndShow(screen);}, 52, 107, 32, 17, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.getInstance().enablePotency));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.size"), -205, 137, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_LEFT, true));
            if (BetterEffectHudConfig.getInstance().enableCustomTimerSize) {
                widgets.add(BetterEffectHudAbstractScreen.newSlider(Component.translatable("better_effect_hud.hud.category.info.slider.size"), -105, 147, 200, 21, BetterEffectHudConfig.varHandle("infoSize", double.class), 0, 2, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance()));
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
                            i * buttonSize - 205 + buttonSize / 2, 147, buttonSize, 21, BetterEffectHudAlignment.TOP_MIDDLE,
                            BetterEffectHudConfig.getInstance().infoSize == buttonSettingSize
                    ));
                }
            }
            widgets.add(BetterEffectHudAbstractScreen.newCheckbox(Component.translatable("better_effect_hud.hud.category.info.checkbox.enable_custom_size"), 13, 149, BetterEffectHudConfig.varHandle("enableCustomTimerSize", Boolean.class), screen, BetterEffectHudAlignment.TOP_MIDDLE));
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.info.slider.decimal_count"), -105, 178, 200, 21, BetterEffectHudConfig.varHandle("timerDecimalCount", Integer.class), 0, 10, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance()));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.info.toggle_button.enable_first_potency"), 105, 178, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableFirstPotency", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.category.info.button.timer_unit_config"), (btn) -> {Minecraft.getInstance().setScreenAndShow(new BetterEffectHudTimerUnitConfig(new BetterEffectHudConfigScreen(page, screen)));}, -105, 209, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, false));
            for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}