package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
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
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 34, 21, 21, "timerPos", screen, 'a'));
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(83, 34, 21, 21, "potencyPos", screen, 'c'));
            int unitMin = BetterEffectHudConfig.getInstance().timerMinUnit;
            int unitMax = BetterEffectHudConfig.getInstance().timerMaxUnit;

            if (BetterEffectHudConfig.getInstance().experimentalEnableCustomTimerSize) {
                widgets.add(BetterEffectHudAbstractScreen.newSlider(Component.literal("Timer Size: %.2f"), 10, 107, 200, 21, "timerSize", 0, 2));
            } else {
                for (int i = 0; i < Minecraft.getInstance().getWindow().getGuiScale() * 2; i++) {
                    float buttonSettingSize = 1.0F / Minecraft.getInstance().getWindow().getGuiScale() * (i + 1);
                    widgets.add(BetterEffectHudAbstractScreen.newButton(
                            Component.literal(String.format("%.2f", buttonSettingSize)), (btn) -> {
                                BetterEffectHudConfig.getInstance().timerSize = buttonSettingSize;
                                BetterEffectHudConfig.serialize();
                                Minecraft.getInstance().gui.setScreen(screen);
                            },
                            10 + i * 28, 107, 28, 21, BetterEffectHudAlignment.TOP_LEFT,
                            BetterEffectHudConfig.getInstance().timerSize == buttonSettingSize
                    ));
                }
            }
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.literal("Decimal Count: %.0f"), 10, 138, 200, 21, "timerDecimalCount", 0, 10));
            for (int i = 0; i < 7; i++) {
                String prefix = "§4";
                int boxPosX = 55 + i * 17;
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

                                boxPosX, 169, 17, 17,
                                BetterEffectHudAlignment.TOP_LEFT, i != 0));
                widgets.add(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 186, (checkbox, value) -> {
                            if (finalI > unitMax) {
                                BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;}
                            BetterEffectHudConfig.getInstance().timerMinUnit = finalI;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerMinUnit == i));
                widgets.add(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 203, (checkbox, value) -> {
                            if (finalI < unitMin) {
                                BetterEffectHudConfig.getInstance().timerMinUnit = finalI;}
                            BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerMaxUnit == i));
                widgets.add(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 220, (checkbox, value) -> {
                            Boolean[] timerUseDecimalsCopy = BetterEffectHudConfig.getInstance().timerUseDecimals.clone();
                            timerUseDecimalsCopy[finalI] = value;
                            BetterEffectHudConfig.getInstance().timerUseDecimals = timerUseDecimalsCopy;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerUseDecimals[i]));
            }
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.unit"), 10, 174, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.min"), 10, 191, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.max"), 10, 208, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.decimal"), 10, 225, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}