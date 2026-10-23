package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.config_tabs.*;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;

import java.util.List;

public class BetterEffectHudConfigScreen extends BetterEffectHudAbstractTabScreen {
    int subpage;

    public BetterEffectHudConfigScreen(int page, Screen parent) {
        super(page, parent);
    }

    @Override
    public BetterEffectHudAbstractTabScreen copy() {
        return new BetterEffectHudConfigScreen(page, parent);
    }

    protected void init() {
        try {
            List<BetterEffectHudAbstractTab> tabs = newTabList(
                    this,
                    new GeneralTab(parent),
                    new InfoTab(parent),
                    new DurationBarTab(parent),
                    new WarningTab(parent),
                    new DisplayTab(parent),
                    new ColorsTab(parent)
            );
            addRenderableWidget(newTabNavigationBar(this, tabs, this.page, 0));
            addRenderableWidget(BetterEffectHudAbstractScreen.newButton(0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE, Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(parent), false));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
