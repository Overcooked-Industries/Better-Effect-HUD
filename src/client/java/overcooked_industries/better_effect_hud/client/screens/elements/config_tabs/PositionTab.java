package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudScreen;

import java.util.List;

public class PositionTab extends GridLayoutTab {
    private static final Component TITLE = Component.translatable("better_effect_hud.hud.category.position");

    public PositionTab(Screen parent) {
        super(TITLE);
        try {
            List<Button> alignmentButtons = BetterEffectHudScreen.newAlignmentButtons(10, 34, 24, 24, "hudAlignment", new BetterEffectHudConfigScreen(Component.literal("Config Screen"), 4, parent), 'e');
            for (Button button : alignmentButtons) {layout.addChild(button, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}