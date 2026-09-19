package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.List;

public class InfoTab extends BetterEffectHudAbstractTab {
    public InfoTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            String[] unitNames = new String[]{"T", "S", "M", "H", "D", "W", "Y"};
            if (BetterEffectHudConfig.getInstance().timerUseMs) unitNames[0] = "MS";
            List<Button> timerAlignmentButtons = BetterEffectHudAbstractScreen.newAlignmentButtons(55, 34, 24, 24, "timerPos", screen, 'a');
            List<Button> potencyAlignmentButtons = BetterEffectHudAbstractScreen.newAlignmentButtons(131, 34, 24, 24, "potencyPos", screen, 'c');
            for (Button button : timerAlignmentButtons) {layout.addChild(button, 0, 0);}
            for (Button button : potencyAlignmentButtons) {layout.addChild(button, 0, 0);}
            int unitMin = BetterEffectHudConfig.getInstance().timerMinUnit;
            int unitMax = BetterEffectHudConfig.getInstance().timerMaxUnit;

            if (BetterEffectHudConfig.getInstance().experimentalEnableCustomTimerSize) {
                layout.addChild(BetterEffectHudAbstractScreen.newSlider(Component.literal("Timer Size: %.2f"), 55, 110, 200, 21, "timerSize", 0, 2), 0, 0);
            } else {
                for (int i = 0; i < Minecraft.getInstance().getWindow().getGuiScale() * 2; i++) {
                    float buttonSettingSize = 1.0F / Minecraft.getInstance().getWindow().getGuiScale() * (i + 1);
                    layout.addChild(BetterEffectHudAbstractScreen.newButton(
                            Component.literal(String.format("%.2f", buttonSettingSize)), (btn) -> {
                                BetterEffectHudConfig.getInstance().timerSize = buttonSettingSize;
                                BetterEffectHudConfig.serialize();
                                Minecraft.getInstance().gui.setScreen(screen);
                            },
                            55 + i * 28, 110, 28, 21, BetterEffectHudAlignment.TOP_LEFT,
                            BetterEffectHudConfig.getInstance().timerSize == buttonSettingSize
                    ), 0, 0);
                }
            }
            layout.addChild(BetterEffectHudAbstractScreen.newIntSlider(Component.literal("Decimal Count: %.0f"), 55, 133, 200, 21, "timerDecimalCount", 0, 5), 0, 0);
            for (int i = 0; i < 7; i++) {
                String prefix = "§4";
                int boxPosX = 55 + i * 17;
                int finalI = i;
                if (i >= unitMin && i <= unitMax) {prefix = "§a";}
                if (i == unitMin || i == unitMax) {prefix = "§e§l§n";}
                layout.addChild(
                        BetterEffectHudAbstractScreen.newButton(Component.literal(prefix + unitNames[i]),
                                (btn) -> {
                                    BetterEffectHudConfig.getInstance().timerUseMs = !BetterEffectHudConfig.getInstance().timerUseMs;
                                    BetterEffectHudConfig.serialize();
                                    Minecraft.getInstance().gui.setScreen(screen);
                                },

                                boxPosX, 165, 17, 17,
                                BetterEffectHudAlignment.TOP_LEFT, i != 0),
                        0, 0);
                layout.addChild(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 182, (checkbox, value) -> {
                            if (finalI > unitMax) {
                                BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;}
                            BetterEffectHudConfig.getInstance().timerMinUnit = finalI;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerMinUnit == i), 0, 0);
                layout.addChild(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 199, (checkbox, value) -> {
                            if (finalI < unitMin) {
                                BetterEffectHudConfig.getInstance().timerMinUnit = finalI;}
                            BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerMaxUnit == i), 0, 0);
                layout.addChild(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 216, (checkbox, value) -> {
                            Boolean[] timerUseDecimalsCopy = BetterEffectHudConfig.getInstance().timerUseDecimals.clone();
                            timerUseDecimalsCopy[finalI] = value;
                            BetterEffectHudConfig.getInstance().timerUseDecimals = timerUseDecimalsCopy;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().gui.setScreen(screen);
                        }, BetterEffectHudConfig.getInstance().timerUseDecimals[i]), 0, 0);
            }
            layout.addChild(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.unit"), 10, 170, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT), 0, 0);
            layout.addChild(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.min"), 10, 187, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT), 0, 0);
            layout.addChild(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.max"), 10, 204, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT), 0, 0);
            layout.addChild(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.decimal"), 10, 221, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT), 0, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}