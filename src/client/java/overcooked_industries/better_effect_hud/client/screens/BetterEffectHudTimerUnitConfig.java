package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.*;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.List;

public class BetterEffectHudTimerUnitConfig extends BetterEffectHudAbstractScreen {
    Screen screen;

    public BetterEffectHudTimerUnitConfig(Screen screen) {
        super();
        this.screen = screen;
    }

    protected void init() {
        List<AbstractWidget> widgets = new ArrayList<>();
        widgets.add(newLabel(Component.translatable("better_effect_hud.hud.category.info.label.timer_unit_title"), 0, 10, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_MIDDLE, true));
        widgets.add(newButton(Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(screen), 0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE, false));
        int unitMin = BetterEffectHudConfig.getInstance().timerMinUnit;
        int unitMax = BetterEffectHudConfig.getInstance().timerMaxUnit;
        String[] unitNames = new String[]{"T", "S", "M", "H", "D", "W", "Y"};
        if (BetterEffectHudConfig.getInstance().timerUseMs) unitNames[0] = "MS";
        for (int i = 0; i < 7; i++) {
            String prefix = "§4";
            int boxPosX = i * 17 - 55;
            int finalI = i;
            if (i >= unitMin && i <= unitMax) prefix = "§a";
            if (i == unitMin || i == unitMax) prefix = "§e§l§n";
            widgets.add(
                    BetterEffectHudAbstractScreen.newButton(Component.literal(prefix + unitNames[i]),
                            (btn) -> {
                                BetterEffectHudConfig.getInstance().timerUseMs = !BetterEffectHudConfig.getInstance().timerUseMs;
                                BetterEffectHudConfig.serialize();
                                Minecraft.getInstance().gui.setScreen(screen);
                            },

                            boxPosX, 44, 17, 17,
                            BetterEffectHudAlignment.TOP_MIDDLE, i != 0)
            );
            widgets.add(
                    BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 61, (checkbox, value) -> {
                        if (finalI > unitMax) {
                            BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;}
                        BetterEffectHudConfig.getInstance().timerMinUnit = finalI;
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().gui.setScreen(screen);
                    }, BetterEffectHudConfig.getInstance().timerMinUnit == i, BetterEffectHudAlignment.TOP_MIDDLE)
            );
            widgets.add(
                    BetterEffectHudAbstractScreen.newCustomCheckbox(Component.literal(""), boxPosX, 78, (checkbox, value) -> {
                        if (finalI < unitMin) {
                            BetterEffectHudConfig.getInstance().timerMinUnit = finalI;}
                        BetterEffectHudConfig.getInstance().timerMaxUnit = finalI;
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().gui.setScreen(screen);
                    }, BetterEffectHudConfig.getInstance().timerMaxUnit == i, BetterEffectHudAlignment.TOP_MIDDLE)
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
                    }, BetterEffectHudConfig.getInstance().timerUseDecimals[i], BetterEffectHudAlignment.TOP_MIDDLE)
            );
            if (i == 0) {
                widgets.add(
                        BetterEffectHudAbstractScreen.newButton(Component.empty(), (btn) -> {},
                                boxPosX, 112, 17, 17,
                                BetterEffectHudAlignment.TOP_MIDDLE, true
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
                        }, BetterEffectHudConfig.getInstance().timerUseDouble[i - 1], BetterEffectHudAlignment.TOP_MIDDLE)
                );
            }
        }
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.unit"), 10, 49, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_MIDDLE, true));
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.min"), 10, 66, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_MIDDLE, true));
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.max"), 10, 83, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_MIDDLE, true));
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.decimal"), 10, 100, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_MIDDLE, true));
        widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.info.label.double"), 10, 117, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_MIDDLE, true));

        for (AbstractWidget widget : widgets) {addRenderableWidget(widget);}
    }
}