package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;


import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class InfoTab extends GridLayoutTab {
    private static final Component TITLE = Component.literal("Timer & Potency");

    public InfoTab(Screen parent) {
        super(TITLE);
    }
}