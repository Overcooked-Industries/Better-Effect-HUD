package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudEffectMoveScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.*;

public class PositionTab extends BetterEffectHudAbstractTab {
    public PositionTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newLabel(Component.translatable("better_effect_hud.hud.category.position.label.position"), 10, 34, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT));
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 44, 21, 21, BetterEffectHudConfig.varHandle("hudAlignment", BetterEffectHudAlignment.class), screen, 'e'));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.position.toggle_button.hud_stack_y"), 10, 117, 200, 21, BetterEffectHudAlignment.TOP_LEFT, BetterEffectHudConfig.varHandle("hudStackY", Boolean.class), screen, false));
            if (BetterEffectHudConfig.getInstance().enableCustomHudSize) {
                widgets.add(BetterEffectHudAbstractScreen.newSlider(Component.translatable("better_effect_hud.hud.category.position.slider.size"), 10, 147, 200, 21, "hudSize", 0, 2));
            } else {
                int buttonSize = Math.round((float) 200 / (Minecraft.getInstance().getWindow().getGuiScale() * 2));
                for (int i = 0; i < Minecraft.getInstance().getWindow().getGuiScale() * 2; i++) {
                    float buttonSettingSize = 1.0F / Minecraft.getInstance().getWindow().getGuiScale() * (i + 1);
                    widgets.add(BetterEffectHudAbstractScreen.newButton(
                            Component.literal(String.format("%.2f", buttonSettingSize)), (btn) -> {
                                BetterEffectHudConfig.getInstance().hudSize = buttonSettingSize;
                                BetterEffectHudConfig.serialize();
                                Minecraft.getInstance().gui.setScreen(screen);
                            },
                            10 + i * buttonSize, 147, buttonSize, 21, BetterEffectHudAlignment.TOP_LEFT,
                            BetterEffectHudConfig.getInstance().hudSize == buttonSettingSize
                    ));
                }
            }
            widgets.add(BetterEffectHudAbstractScreen.newCheckbox(Component.translatable("better_effect_hud.hud.category.position.checkbox.enable_custom_size"), 220, 149, BetterEffectHudConfig.varHandle("enableCustomHudSize", Boolean.class), screen));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable("better_effect_hud.hud.button.move"), (btn) -> {
                try {
                    Minecraft.getInstance().setScreenAndShow(new BetterEffectHudEffectMoveScreen("hudPosX", "hudPosY", screen));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }, 220, 180, 200, 21, BetterEffectHudAlignment.TOP_LEFT, false));
            for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}