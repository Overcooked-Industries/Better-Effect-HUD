package overcooked_industries.better_effect_hud.client.screens.config_tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.BetterEffectHudTempConfig;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudConfigScreen;

import java.util.ArrayList;
import java.util.List;

public class GeneralTab extends BetterEffectHudAbstractTab {
    public GeneralTab(Screen parent, Component title, int page) {
        super(parent, title, page);
        try {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(BetterEffectHudAbstractScreen.newToggleButton(Component.translatable("better_effect_hud.hud.category.general.toggle_button.enable_mod"), 0, 34, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudConfig.varHandle("enableMod", Boolean.class), screen, false));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.translatable((BetterEffectHudTempConfig.getInstance().unlockReset) ? "better_effect_hud.hud.category.general.button.highlight_reset" : "better_effect_hud.hud.category.general.button.reset"), (btn) -> {BetterEffectHudConfig.reset(); BetterEffectHudTempConfig.getInstance().unlockReset = false; Minecraft.getInstance().setScreenAndShow(screen);}, 0, 65, 200, 21, BetterEffectHudAlignment.TOP_MIDDLE, !BetterEffectHudTempConfig.getInstance().unlockReset));
            widgets.add(BetterEffectHudAbstractScreen.newButton(Component.empty(), (btn) -> {BetterEffectHudTempConfig.getInstance().unlockReset = !BetterEffectHudTempConfig.getInstance().unlockReset; Minecraft.getInstance().setScreenAndShow(screen);}, 120, 65, 21, 21, BetterEffectHudAlignment.TOP_MIDDLE, false));
            widgets.add(BetterEffectHudAbstractScreen.newTexture(121, 65, 20, 20, -1, RenderPipelines.GUI_TEXTURED, (BetterEffectHudTempConfig.getInstance().unlockReset) ? BetterEffectHud.id("textures/gui/unlocked_icon.png") : BetterEffectHud.id("textures/gui/locked_icon.png"), BetterEffectHudAlignment.TOP_MIDDLE));
            for (AbstractWidget widget : widgets) layout.addChild(widget, 0, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}