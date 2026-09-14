package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.elements.config_tabs.*;

public class BetterEffectHudConfigScreen extends BetterEffectHudScreen {

    public BetterEffectHudConfigScreen(Component title, int page, Screen parent) {
        super(title);
        this.page = page;
        this.parent = parent;
    }

    int page;
    Screen parent;

    protected void init() {
        addRenderableWidget(newTabNavigationBar(new Tab[] {
                new GeneralTab(parent),
                new InfoTab(parent),
                new DurationBarTab(parent),
                new WarningTab(parent),
                new PositionTab(parent),
        }, page));
        addRenderableWidget(BetterEffectHudScreen.newButton(Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(parent), 0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE));
    }
}
