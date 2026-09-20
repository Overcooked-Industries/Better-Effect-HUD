package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.*;

public class PositionTab extends BetterEffectHudAbstractTab {
    public PositionTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 34, 21, 21, "hudAlignment", screen, 'e'));
            widgets.addAll(BetterEffectHudAbstractScreen.newAlignmentButtons(10, 107, 21, 21, "extraHudAlignment", screen, '6'));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.position.toggle_button.stack_y"), 83, 34, 200, 21, BetterEffectHudAlignment.TOP_LEFT, "stackY", screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.position.toggle_button.extra_stack_y"), 83, 107, 200, 21, BetterEffectHudAlignment.TOP_LEFT, "extraStackY", screen, false));
            for (AbstractWidget widget : widgets) {layout.addChild(widget, 0, 0);}
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}