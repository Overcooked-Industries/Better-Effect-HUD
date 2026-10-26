package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.BetterEffectHudTempConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;

public class BetterEffectHudColorsTab extends BetterEffectHudAbstractTab {
    public BetterEffectHudColorsTab(Screen parent) {
        super(parent, Component.translatable("better_effect_hud.hud.category.colors"));
    }
    public void initialize() throws NoSuchFieldException, IllegalAccessException {
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(-150, 34, 150, 21, BetterEffectHudAlignment.TOP_MIDDLE, Component.translatable("better_effect_hud.hud.category.colors.button.timer_category"), (btn) -> {BetterEffectHudTempConfig.getInstance().colorTab = 0; Minecraft.getInstance().setScreenAndShow(screen);}, BetterEffectHudTempConfig.getInstance().colorTab == 0));
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(0, 34, 150, 21, BetterEffectHudAlignment.TOP_MIDDLE, Component.translatable("better_effect_hud.hud.category.colors.button.potency_category"), (btn) -> {BetterEffectHudTempConfig.getInstance().colorTab = 1; Minecraft.getInstance().setScreenAndShow(screen);}, BetterEffectHudTempConfig.getInstance().colorTab == 1));
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(150, 34, 150, 21, BetterEffectHudAlignment.TOP_MIDDLE, Component.translatable("better_effect_hud.hud.category.colors.button.is_beneficial"), (btn) -> {BetterEffectHudTempConfig.getInstance().colorTab = 2; Minecraft.getInstance().setScreenAndShow(screen);}, BetterEffectHudTempConfig.getInstance().colorTab == 2));

        switch (BetterEffectHudTempConfig.getInstance().colorTab) {
            case 0 -> {
                addRenderableWidget(BetterEffectHudAbstractScreen.newButton(-105, 70, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, Component.translatable("better_effect_hud.hud.category.colors.button.timer_color"), (btn) -> {}, true, Component.translatable("better_effect_hud.hud.help.category.colors.button.timer_color")));
                addRenderableWidget(BetterEffectHudAbstractScreen.newColorGradientBox(105, 70, 200, 21, BetterEffectHudConfig.varHandle("durationColor", BetterEffectHudColorSetting.class), BetterEffectHudAlignment.TOP_MIDDLE, screen));
                for (int i = 0; i < BetterEffectHudConfig.getInstance().durationColor.getColorGradient().toArray().length; i++)
                    addRenderableWidget(BetterEffectHudAbstractScreen.newGradientHandle(
                            105, 70,
                            200, 21, BetterEffectHudConfig.varHandle("durationColor", BetterEffectHudColorSetting.class), BetterEffectHudAlignment.TOP_MIDDLE, i, screen
                ));
            }
            case 1 -> {
                for (int i = 0; i < 6; i++)
                    addRenderableWidget(BetterEffectHudAbstractScreen.newColorSetting(
                            Component.translatable(String.format("better_effect_hud.hud.category.colors.button.potency.potency%s", i)),
                            BetterEffectHudConfig.varHandle(String.format("potencyColor%s", i), BetterEffectHudColorSetting.class),
                            BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen,
                            -105, 70 + ((Minecraft.getInstance().getWindow().getGuiScale() > 3) ? 26 * i : 31 * i),
                            200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.MIDDLE_RIGHT, false, Component.translatable("better_effect_hud.hud.help.category.colors.button.potency.potency")
                    ));
            }
            case 2 -> {
                addRenderableWidget(BetterEffectHudAbstractScreen.newColorSetting(
                        Component.translatable("better_effect_hud.hud.category.colors.button.is_beneficial.beneficial"),
                        BetterEffectHudConfig.varHandle("beneficialColor", BetterEffectHudColorSetting.class),
                        BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen,
                        -105, 70,
                        200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.MIDDLE_RIGHT, false,
                        Component.translatable("better_effect_hud.hud.help.category.colors.button.is_beneficial.beneficial")
                ));
                addRenderableWidget(BetterEffectHudAbstractScreen.newColorSetting(
                        Component.translatable("better_effect_hud.hud.category.colors.button.is_beneficial.harmful"),
                        BetterEffectHudConfig.varHandle("harmfulColor", BetterEffectHudColorSetting.class),
                        BetterEffectHudConfig::serialize, BetterEffectHudConfig.getInstance(), screen,
                        -105, 101,
                        200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.MIDDLE_RIGHT, false,
                        Component.translatable("better_effect_hud.hud.help.category.colors.button.is_beneficial.harmful")
                ));
            }
        }
    }
}