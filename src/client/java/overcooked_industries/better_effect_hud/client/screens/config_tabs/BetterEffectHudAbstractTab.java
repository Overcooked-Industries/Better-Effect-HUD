package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractTabScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;

import java.util.List;

public abstract class BetterEffectHudAbstractTab extends GridLayoutTab  {
    BetterEffectHudAbstractTabScreen screen;
    Screen parent;

    public BetterEffectHudAbstractTab(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        this.screen = null;
    }

    public void setScreen(BetterEffectHudAbstractTabScreen screen) {
        this.screen = screen;
    }

    public abstract void initialize() throws NoSuchFieldException, IllegalAccessException;

    void addRenderableWidget(AbstractWidget widget) {
        layout.addChild(widget, 0, 0);
    }
    void addRenderableWidgets(List<AbstractWidget> widgets) {
        for (AbstractWidget widget : widgets) layout.addChild(widget, 0, 0);
    }
}
