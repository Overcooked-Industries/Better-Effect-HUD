package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudScreen;

import java.util.List;

public class InfoTab extends GridLayoutTab {
    private static final Component TITLE = Component.translatable("better_effect_hud.hud.category.info");

    public InfoTab(Screen parent) {
        super(TITLE);
        try {
            List<Button> timerAlignmentButtons = BetterEffectHudScreen.newAlignmentButtons(10, 34, 24, 24, "timerPos", new BetterEffectHudConfigScreen(Component.literal("Config Screen"), 1, parent), 'a');
            List<Button> potencyAlignmentButtons = BetterEffectHudScreen.newAlignmentButtons(86, 34, 24, 24, "potencyPos", new BetterEffectHudConfigScreen(Component.literal("Config Screen"), 1, parent), 'c');
            for (Button button : timerAlignmentButtons) {layout.addChild(button, 0, 0);}
            for (Button button : potencyAlignmentButtons) {layout.addChild(button, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}