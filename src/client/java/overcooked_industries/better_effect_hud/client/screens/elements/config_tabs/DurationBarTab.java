package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;


import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DurationBarTab extends GridLayoutTab {
    private static final Component TITLE = Component.translatable("better_effect_hud.hud.category.duration_bar");

    public DurationBarTab(Screen parent) {
        super(TITLE);
        GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
    }
}