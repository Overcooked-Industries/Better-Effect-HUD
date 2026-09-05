package overcooked_industries.better_effect_hud.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class BetterEffectHudConfigScreen implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (Screen parent) -> new BetterEffectHudScreen(Component.literal("Better Effect Hud Config")) {
            @Override
            protected void init() {
                tabNavigationBar(new Tab[] {new GridLayoutTab(Component.literal("a"))});
            }
        };
    }

    private class GeneralTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("General");

        private GeneralTab() {
            super(Component.literal("aaaa"));
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
            LayoutSettings buttonLayoutSettings = helper.newCellSettings();
        }
    }
}
