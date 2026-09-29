package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Optional;

public class BetterEffectHudEffectMoveScreen extends BetterEffectHudAbstractScreen {
    VarHandle handleX;
    VarHandle handleY;
    Screen screen;
    public BetterEffectHudEffectMoveScreen(String fieldX, String fieldY, Screen screen) throws NoSuchFieldException, IllegalAccessException {
        super();
        this.handleX = BetterEffectHudConfig.varHandle(fieldX, Integer.class);
        this.handleY = BetterEffectHudConfig.varHandle(fieldY, Integer.class);
        this.screen = screen;
    }

    protected void init() {
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(screen), 0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE, false));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        int valueX = (Integer) handleX.get(BetterEffectHudConfig.getInstance());
        int valueY = (Integer) handleX.get(BetterEffectHudConfig.getInstance());
        graphics.fill(valueX - 1, valueY - 1, 2, 2, 0xFFFFFFFF);
    }
}