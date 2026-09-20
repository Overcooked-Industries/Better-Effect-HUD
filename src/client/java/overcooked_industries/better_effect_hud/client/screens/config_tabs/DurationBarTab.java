package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class DurationBarTab extends BetterEffectHudAbstractTab {
    public DurationBarTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        List<AbstractWidget> widgets = new ArrayList<>();
        for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
    }
}