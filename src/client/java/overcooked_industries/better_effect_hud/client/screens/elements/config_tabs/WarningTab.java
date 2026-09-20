package overcooked_industries.better_effect_hud.client.screens.elements.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudEffectSelectionScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarningTab extends BetterEffectHudAbstractTab {
    public WarningTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            BetterEffectHudEffectSelectionScreen betterEffectHudEffectSelectionScreen = new BetterEffectHudEffectSelectionScreen("effectWarningIdentifiers", BetterEffectHudConfig.getInstance().effectWarningIdentifiers.length, new BetterEffectHudConfigScreen(2, parent));
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.literal("+"), (btn) -> {
                List<Identifier> effectWarningIdentifiersCopy = Arrays.stream(BetterEffectHudConfig.getInstance().effectWarningIdentifiers).toList();
                effectWarningIdentifiersCopy.add(Registries.MOB_EFFECT.identifier());
                BetterEffectHudConfig.getInstance().effectWarningIdentifiers = (Identifier[]) effectWarningIdentifiersCopy.stream().toArray();
                BetterEffectHudConfig.serialize();
                Minecraft.getInstance().setScreenAndShow(betterEffectHudEffectSelectionScreen);
            }, 10, 34, 21, 21, BetterEffectHudAlignment.TOP_LEFT, false));
            for (AbstractWidget widget : widgets) {
                layout.addChild(widget, 0, 0);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //No Exception will ever be thrown, since we know the enableMod field exists in the BetterEffectHudAlignment instance and is modifiable
            throw new RuntimeException(e);
        }
    }
}