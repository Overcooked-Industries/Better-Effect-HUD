package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;

import java.util.ArrayList;
import java.util.List;

public class BetterEffectHudTimerUnitConfigScreen extends BetterEffectHudAbstractScreen {
    Screen screen;

    public BetterEffectHudTimerUnitConfigScreen(Screen screen) {
        super();
        this.screen = screen;
    }

    protected void init() {
        List<AbstractWidget> widgets = new ArrayList<>();
        widgets.add(newLabel(Component.translatable("better_effect_hud.hud.category.info.label.timer_unit_title"), 0, 10, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_MIDDLE, true));
        widgets.add(newButton(0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE, Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(screen), false));
        int unitMin = BetterEffectHudConfig.getInstance().timerMinUnit;
        int unitMax = BetterEffectHudConfig.getInstance().timerMaxUnit;
        String[] unitNames = new String[]{"T", "S", "M", "H", "D", "W", "Y"};
        if (BetterEffectHudConfig.getInstance().timerUseMs) unitNames[0] = "MS";
        for (int i = 0; i < 7; i++) {
            String prefix = "§4";
            int boxPosX = i * 17 - 45;
            int finalI = i;
            if (i >= unitMin && i <= unitMax) prefix = "§a";
            if (i == unitMin || i == unitMax) prefix = "§e§l§n";
            widgets.add(
                    BetterEffectHudAbstractScreen.newButton(boxPosX, 44, 17, 17, BetterEffectHudAlignment.TOP_MIDDLE, Component.literal(prefix + unitNames[i]),
                            (btn) -> {
                                BetterEffectHudConfig.getInstance().timerUseMs = !BetterEffectHudConfig.getInstance().timerUseMs;
                                BetterEffectHudConfig.serialize();
                                Minecraft.getInstance().setScreenAndShow(this);
                            },

                            i != 0)
            );
            widgets.add(
                    BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 61, (checkbox, value) -> {
                        if (finalI > unitMax) {
                            BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;}
                        BetterEffectHudConfig.getInstance().timerMinUnit = finalI;
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().setScreenAndShow(this);
                    }, BetterEffectHudConfig.getInstance().timerMinUnit == i, BetterEffectHudAlignment.TOP_MIDDLE)
            );
            widgets.add(
                    BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 78, (checkbox, value) -> {
                        if (finalI < unitMin) {
                            BetterEffectHudConfig.getInstance().timerMinUnit = finalI;}
                        BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().setScreenAndShow(this);
                    }, BetterEffectHudConfig.getInstance().timerMaxUnit == i, BetterEffectHudAlignment.TOP_MIDDLE)
            );
            widgets.add(
                    BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 95, (checkbox, value) -> {
                        Boolean[] timerUseDecimalsCopy = BetterEffectHudConfig.getInstance().timerUseDecimals.clone();
                        Boolean[] timerUseDoubleCopy = BetterEffectHudConfig.getInstance().timerUseDouble.clone();
                        Minecraft.getInstance().setScreenAndShow(this);
                        timerUseDecimalsCopy[finalI] = value;
                        timerUseDoubleCopy[Math.clamp(finalI - 1, 0, Integer.MAX_VALUE)] = false;
                        BetterEffectHudConfig.getInstance().timerUseDecimals = timerUseDecimalsCopy;
                        BetterEffectHudConfig.getInstance().timerUseDouble = timerUseDoubleCopy;
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().setScreenAndShow(this);
                    }, BetterEffectHudConfig.getInstance().timerUseDecimals[i], BetterEffectHudAlignment.TOP_MIDDLE)
            );
            if (i == 0) {
                widgets.add(
                        BetterEffectHudAbstractScreen.newButton(boxPosX, 112, 17, 17, BetterEffectHudAlignment.TOP_MIDDLE, Component.empty(), (btn) -> {},
                                true
                        )
                );
            } else {
                widgets.add(
                        BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 112, (checkbox, value) -> {
                            Boolean[] timerUseDecimalsCopy = BetterEffectHudConfig.getInstance().timerUseDecimals.clone();
                            Boolean[] timerUseDoubleCopy = BetterEffectHudConfig.getInstance().timerUseDouble.clone();
                            Minecraft.getInstance().setScreenAndShow(this);
                            timerUseDecimalsCopy[finalI] = false;
                            timerUseDoubleCopy[finalI - 1] = value;
                            BetterEffectHudConfig.getInstance().timerUseDecimals = timerUseDecimalsCopy;
                            BetterEffectHudConfig.getInstance().timerUseDouble = timerUseDoubleCopy;
                            BetterEffectHudConfig.serialize();
                            Minecraft.getInstance().setScreenAndShow(this);
                        }, BetterEffectHudConfig.getInstance().timerUseDouble[i - 1], BetterEffectHudAlignment.TOP_MIDDLE)
                );
            }
        }
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.unit"), -60, 49, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_RIGHT, true));
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.min"), -60, 66, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_RIGHT, true));
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.max"), -60, 83, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_RIGHT, true));
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.decimal"), -60, 100, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_RIGHT, true));
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.double"), -60, 117, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_RIGHT, true));
        try {
            widgets.add(BetterEffectHudAbstractScreen.newIntSlider(Component.translatable("better_effect_hud.hud.category.info.slider.decimal_count"), 0, 139, 200, 21, BetterEffectHudConfig.varHandle("timerDecimalCount", Integer.class), 0, 10, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), Component.translatable("better_effect_hud.hud.help.category.info.slider.decimal_count")));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (AbstractWidget widget : widgets) {addRenderableWidget(widget);}
    }
}