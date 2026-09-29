package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.config_tabs.*;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.ArrayList;
import java.util.List;

public class BetterEffectHudConfigScreen extends BetterEffectHudAbstractTabScreen {

    public BetterEffectHudConfigScreen(int page, Screen parent) {
        super(page, parent);
    }

    protected void init() {
        List<Tab> tabs = new ArrayList<>();
        tabs.add(new GeneralTab(parent, Component.translatable("better_effect_hud.hud.category.general"), 0));
        tabs.add(new InfoTab(parent, Component.translatable("better_effect_hud.hud.category.info"), 1));
        tabs.add(new DurationBarTab(parent, Component.translatable("better_effect_hud.hud.category.duration_bar"), 2));
        tabs.add(new WarningTab(parent, Component.translatable("better_effect_hud.hud.category.warning"), 3));
        tabs.add(new PositionTab(parent, Component.translatable("better_effect_hud.hud.category.position"), 4));

        addRenderableWidget(newTabNavigationBar(this, tabs, page));
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(parent), 0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE, false));
    }
}
