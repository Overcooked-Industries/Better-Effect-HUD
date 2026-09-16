package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.List;

public class PositionTab extends BetterEffectHudAbstractTab {
    public PositionTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<Button> alignmentButtons = BetterEffectHudAbstractScreen.newAlignmentButtons(55, 34, 24, 24, "hudAlignment", screen, 'e');
            for (Button button : alignmentButtons) {layout.addChild(button, 0, 0);}
            layout.addChild(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.position.toggle_button.stack_y"), 55, 106, 200, 21, BetterEffectHudAlignment.TOP_LEFT, "stackY", screen, false), 0, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}