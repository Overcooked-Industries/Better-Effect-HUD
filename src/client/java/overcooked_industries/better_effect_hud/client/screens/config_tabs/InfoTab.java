package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.ArrayList;
import java.util.List;

public class InfoTab extends BetterEffectHudAbstractTab {
    public InfoTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            String[] unitNames = new String[]{"T", "S", "M", "H", "D", "W", "Y"};
            if (BetterEffectHudConfig.getInstance().timerUseMs) unitNames[0] = "MS";
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.timer_alignment"), 10, 34, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.potency_alignment"), 83, 34, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 44, 21, 21, BetterEffectHudConfig.varHandle("timerPos", BetterEffectHudAlignment.class), screen, 'a'));
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(83, 44, 21, 21, BetterEffectHudConfig.varHandle("potencyPos", BetterEffectHudAlignment.class), screen, 'c'));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.toggle.long.off"), (btn) -> {BetterEffectHudConfig.getInstance().enableTimer = false; BetterEffectHudConfig.serialize(); Minecraft.getInstance().setScreenAndShow(screen);}, 10, 107, 31, 17, BetterEffectHudAlignment.TOP_LEFT, !BetterEffectHudConfig.getInstance().enableTimer));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.toggle.long.on"), (btn) -> {BetterEffectHudConfig.getInstance().enableTimer = true; BetterEffectHudConfig.serialize(); Minecraft.getInstance().setScreenAndShow(screen);}, 41, 107, 32, 17, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudConfig.getInstance().enableTimer));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.toggle.long.off"), (btn) -> {BetterEffectHudConfig.getInstance().enablePotency = false; BetterEffectHudConfig.serialize(); Minecraft.getInstance().setScreenAndShow(screen);}, 83, 107, 31, 17, BetterEffectHudAlignment.TOP_LEFT, !BetterEffectHudConfig.getInstance().enablePotency));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.toggle.long.on"), (btn) -> {BetterEffectHudConfig.getInstance().enablePotency = true; BetterEffectHudConfig.serialize(); Minecraft.getInstance().setScreenAndShow(screen);}, 114, 107, 32, 17, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudConfig.getInstance().enablePotency));
            int unitMin = BetterEffectHudConfig.getInstance().timerMinUnit;
            int unitMax = BetterEffectHudConfig.getInstance().timerMaxUnit;
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.timer_unit_title"), 175, 34, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            for (int i = 0; i < 7; i++) {
                String prefix = "§4";
                int boxPosX = 220 + i * 17;
                int finalI = i;
                if (i >= unitMin && i <= unitMax) {prefix = "§a";}
                if (i == unitMin || i == unitMax) {prefix = "§e§l§n";}
                widgets.add(
                        BetterEffectHudAbstractScreen.newButton(Component.literal(prefix + unitNames[i]),
                                (btn) -> {
                                    BetterEffectHudConfig.getInstance().timerUseMs = !BetterEffectHudConfig.getInstance().timerUseMs;
                                    BetterEffectHudConfig.serialize();
                                    Minecraft.getInstance().gui.setScreen(screen);
                                },

                                boxPosX, 44, 17, 17,
                                BetterEffectHudAlignment.TOP_LEFT, i != 0)
                );
                widgets.add(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 61, (checkbox, value) -> {
                            if (finalI > unitMax) {
                                BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;}
                            BetterEffectHudConfig.getInstance().timerMinUnit = finalI;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerMinUnit == i)
                );
                widgets.add(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 78, (checkbox, value) -> {
                            if (finalI < unitMin) {
                                BetterEffectHudConfig.getInstance().timerMinUnit = finalI;}
                            BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerMaxUnit == i)
                );
                widgets.add(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 95, (checkbox, value) -> {
                            Boolean[] timerUseDecimalsCopy = BetterEffectHudConfig.getInstance().timerUseDecimals.clone();
                            Boolean[] timerUseDoubleCopy = BetterEffectHudConfig.getInstance().timerUseDouble.clone();
                            Minecraft.getInstance().gui.setScreen(screen);
                            timerUseDecimalsCopy[finalI] = value;
                            timerUseDoubleCopy[Math.clamp(finalI - 1, 0, Integer.MAX_VALUE)] = false;
                            BetterEffectHudConfig.getInstance().timerUseDecimals = timerUseDecimalsCopy;
                            BetterEffectHudConfig.getInstance().timerUseDouble = timerUseDoubleCopy;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerUseDecimals[i])
                );
                if (i == 0) {
                    widgets.add(
                            BetterEffectHudAbstractScreen.newButton(Component.empty(), (btn) -> {},
                                    boxPosX, 112, 17, 17,
                                    BetterEffectHudAlignment.TOP_LEFT, true
                            )
                    );
                } else {
                    widgets.add(
                            BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 112, (checkbox, value) -> {
                                Boolean[] timerUseDecimalsCopy = BetterEffectHudConfig.getInstance().timerUseDecimals.clone();
                                Boolean[] timerUseDoubleCopy = BetterEffectHudConfig.getInstance().timerUseDouble.clone();
                                Minecraft.getInstance().gui.setScreen(screen);
                                timerUseDecimalsCopy[finalI] = false;
                                timerUseDoubleCopy[finalI - 1] = value;
                                BetterEffectHudConfig.getInstance().timerUseDecimals = timerUseDecimalsCopy;
                                BetterEffectHudConfig.getInstance().timerUseDouble = timerUseDoubleCopy;
                                BetterEffectHudConfig.serialize();
                                Minecraft.getInstance().gui.setScreen(screen);
                            }, BetterEffectHudConfig.getInstance().timerUseDouble[i - 1])
                    );
                }
            }
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.unit"), 175, 49, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.min"), 175, 66, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.max"), 175, 83, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.decimal"), 175, 100, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.double"), 175, 117, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));

            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.size"), 10, 127, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            if (BetterEffectHudConfig.getInstance().enableCustomTimerSize) {
                widgets.add(BetterEffectHudAbstractScreen.newSlider(Component.translatable("better_effect_hud.hud.category.info.slider.size"), 10, 137, 200, 21, "infoSize", 0, 2));
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
                            10 + i * buttonSize, 137, buttonSize, 21, BetterEffectHudAlignment.TOP_LEFT,
                            BetterEffectHudConfig.getInstance().infoSize == buttonSettingSize
                    ));
                }
            }
            widgets.add(BetterEffectHudAbstractScreen.newCheckbox(Component.translatable("better_effect_hud.hud.category.info.checkbox.enable_custom_size"), 220, 139, BetterEffectHudConfig.varHandle("enableCustomTimerSize", Boolean.class), screen));
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.info.slider.decimal_count"), 10, 168, 200, 21, "timerDecimalCount", 0, 10));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.info.toggle_button.enable_first_potency"), 10, 199, 200, 21, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudConfig.varHandle("enableFirstPotency", Boolean.class), screen, false));
            for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}