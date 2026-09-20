package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudEffectEditScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.ArrayList;
import java.util.List;

public class WarningTab extends BetterEffectHudAbstractTab {
    public WarningTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            BetterEffectHudEffectEditScreen betterEffectHudEffectEditScreen = new BetterEffectHudEffectEditScreen("effectWarningIdentifiers", new BetterEffectHudConfigScreen(3, parent));
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.literal("+"), (btn) -> {Minecraft.getInstance().setScreenAndShow(betterEffectHudEffectEditScreen);}, 10, 34, 21, 21, BetterEffectHudAlignment.TOP_LEFT, false));
            for (AbstractWidget widget : widgets) layout.addChild(widget, 0, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}