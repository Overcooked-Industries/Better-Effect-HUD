package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfigManager;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

public class PositionTab extends GridLayoutTab {
    private static final Component TITLE = Component.literal("Position & Size");

    public PositionTab(Screen parent) {
        super(TITLE);
        for (BetterEffectHudAlignment alignment : BetterEffectHudConfigScreen.betterEffectHudAlignments) {
            String colorPrefix = "";
            if (BetterEffectHudConfigManager.config.hudAlignment == alignment) {colorPrefix = "§l§n";}
            layout.addChild(BetterEffectHudScreen.newButton(
                    Component.literal(
                                colorPrefix +
                                    Component.translatable("better_effect_hud.hud.alignment.short.v." + alignment.posY).getString() +
                                    Component.translatable("better_effect_hud.hud.alignment.short.h." + alignment.posX).getString()
                    ),
                    (btn) -> {
                        BetterEffectHudConfigManager.config.hudAlignment = alignment;
                        BetterEffectHudConfigManager.serialize();
                        Minecraft.getInstance().gui.setScreen(new BetterEffectHudConfigScreen(Component.literal("Config Screen"), 4, parent));
                    },
                    10 + alignment.posX * 25, 34 + alignment.posY * 25, 24, 24,
                    BetterEffectHudAlignment.TOP_LEFT), 0, 0);
        }
    }
}