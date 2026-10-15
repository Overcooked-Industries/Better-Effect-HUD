package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudGraph;

import java.lang.invoke.VarHandle;
import java.util.Map;
import java.util.Optional;

public class BetterEffectHudEffectSelectionScreen extends BetterEffectHudAbstractScreen {
    VarHandle handle;
    int index;
    Screen screen;
    public BetterEffectHudEffectSelectionScreen(VarHandle handle, int index, Screen screen) throws NoSuchFieldException, IllegalAccessException {
        super();
        this.handle = handle;
        this.index = index;
        this.screen = screen;
    }

    protected void init() {
        int i = 0;
        for (Map.Entry<ResourceKey<MobEffect>, MobEffect> effect : BuiltInRegistries.MOB_EFFECT.entrySet()) {
            int width = Math.round((float) (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 40) / 18) * 18;
            int posX = (int) (10 + Math.floor((float) ((i * 21) % width) / 21   ) * 21  + 3);
            int posY = (int) (34 + Math.floor((float) ( i * 21)          / width) * 21) + 3;
            addRenderableWidget(BetterEffectHudAbstractScreen.newButton(Component.empty(),
                    (btn) -> {
                        Identifier[] updatedIdentifiers = (Identifier[]) handle.get(BetterEffectHudConfig.getInstance());
                        updatedIdentifiers[index] = effect.getKey().identifier();
                        handle.set(BetterEffectHudConfig.getInstance(), (Identifier[]) updatedIdentifiers);
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().setScreenAndShow(screen);
                    },
                    posX, posY, 21, 21, BetterEffectHudAlignment.TOP_LEFT,
                    false));
            addRenderableWidget(BetterEffectHudAbstractScreen.newTexture(
                    posX + 1, posY + 1, 18, 18, 0xFFFFFFFF,
                    RenderPipelines.GUI_TEXTURED,
                    Optional.of(effect.getKey()).map(ResourceKey::identifier).map((id) -> id.withPrefix("textures/mob_effect/").withSuffix(".png")).orElseGet(MissingTextureAtlasSprite::getLocation), BetterEffectHudAlignment.TOP_LEFT)
            );
            i += 1;
        }
    }
}