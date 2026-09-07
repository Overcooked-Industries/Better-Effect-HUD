package overcooked_industries.better_effect_hud.client.screens;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;


public class BetterEffectHudConfigScreen implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (Screen parent) -> new BetterEffectHudScreen(Component.literal("Better Effect Hud Config")) {
            @Override
            protected void init() {
                addRenderableWidget(newTabNavigationBar(new Tab[] {
                        new GeneralTab(),
                        new InfoTab(),
                        new DurationBarTab(),
                        new WarningTab(),
                        new PositionTab(),
                }, 1));
                addRenderableWidget(BetterEffectHudScreen.newButton(Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(parent), 0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE));
            }
        };
    }

    //TODO: Move these helper classes out into their own package!!!!!!!
    private class GeneralTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("General Settings");

        private GeneralTab() {
            super(TITLE);
            try {
                layout.addChild(BetterEffectHudScreen.newToggleButton(Component.literal("Enable Mod"), 10, 34, 100, 21, BetterEffectHudAlignment.TOP_LEFT, "enableMod"), 0, 0);
            } catch (NoSuchFieldException | IllegalAccessException _) {
                //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
                //TODO: Consider throwing a RuntimeException instead to fail loudly? Discuss!
            }
        }
    }

    private static class InfoTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Timer & Potency");

        private InfoTab() {
            super(TITLE);
            try {
                layout.addChild(BetterEffectHudScreen.newToggleButton(Component.literal("Enable Mod"), 10, 34, 100, 21, BetterEffectHudAlignment.TOP_LEFT, "enableMod"), 0, 0);
            } catch (NoSuchFieldException | IllegalAccessException _) {
                //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
                //TODO: Consider throwing a RuntimeException instead to fail loudly? Discuss!
            }
        }
    }
    private static class DurationBarTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Duration Bar");

        private DurationBarTab() {
            super(TITLE);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        }
    }
    private static class WarningTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Vignette Warning");

        private WarningTab() {
            super(TITLE);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        }
    }
    private static class PositionTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Position & Size");

        private PositionTab() {
            super(TITLE);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        }
    }
}
