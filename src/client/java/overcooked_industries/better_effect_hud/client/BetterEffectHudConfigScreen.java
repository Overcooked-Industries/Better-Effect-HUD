package overcooked_industries.better_effect_hud.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.Minecraft;
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
                addRenderableWidget(tabNavigationBar(new Tab[] {
                        new GeneralTab(parent),
                        new InfoTab(parent),
                        new DurationBarTab(parent),
                        new WarningTab(parent),
                        new PositionTab(parent),
                }));
                addRenderableWidget(BetterEffectHudScreen.newButton(Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(parent), 0, -25, 200, 21, 1, 2));
            }
        };
    }

    private class GeneralTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("General Settings");

        private GeneralTab(Screen parent) {
            super(TITLE);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        }
    }
    private class InfoTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Timer & Potency");

        private InfoTab(Screen parent) {
            super(TITLE);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        }
    }
    private class DurationBarTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Duration Bar");

        private DurationBarTab(Screen parent) {
            super(TITLE);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        }
    }
    private class WarningTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Vignette Warning");

        private WarningTab(Screen parent) {
            super(TITLE);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        }
    }
    private class PositionTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Position & Size");

        private PositionTab(Screen parent) {
            super(TITLE);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        }
    }
}
