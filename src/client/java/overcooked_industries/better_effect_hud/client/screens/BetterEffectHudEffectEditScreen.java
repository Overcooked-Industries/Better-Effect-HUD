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
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class BetterEffectHudEffectEditScreen extends BetterEffectHudAbstractScreen {
    VarHandle handle;
    Screen screen;
    public BetterEffectHudEffectEditScreen(String field, Screen screen) throws NoSuchFieldException, IllegalAccessException {
        super();
        this.handle = BetterEffectHudConfig.varHandle(field, Identifier[].class);
        this.screen = screen;
    }

    protected void init() {
        BetterEffectHudEffectSelectionScreen plusButtonScreen;
        Identifier[] value = (Identifier[]) handle.get(BetterEffectHudConfig.getInstance());
        try {
            plusButtonScreen = new BetterEffectHudEffectSelectionScreen(handle, value.length, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(Component.literal("+"), (btn) -> {
            Identifier[] effectWarningIdentifiers = BetterEffectHudConfig.getInstance().effectWarningIdentifiers.clone();
            Identifier[] updatedEffectWarningIdentifiers = Arrays.copyOf(effectWarningIdentifiers, effectWarningIdentifiers.length + 1);
            updatedEffectWarningIdentifiers[effectWarningIdentifiers.length] = Identifier.withDefaultNamespace("mob_effect/strengh");
            BetterEffectHudConfig.getInstance().effectWarningIdentifiers = updatedEffectWarningIdentifiers;
            BetterEffectHudConfig.serialize();
            Minecraft.getInstance().setScreenAndShow(plusButtonScreen);
        }, 12,  5, 21, 21, BetterEffectHudAlignment.TOP_LEFT, false));
        int i = 0;
        for (Identifier effect : value) {
            int width = Math.round((float) (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 40) / 18) * 18;
            int posX = (int) (10 + Math.floor((float) ((i * 21) % width) / 21   ) * 21  + 3);
            int posY = (int) (34 + Math.floor((float) ( i * 21)          / width) * 21) + 3;
            int finalI = i;
            addRenderableWidget(BetterEffectHudAbstractScreen.newButton(Component.empty(),
                    (btn) -> {
                        Identifier[] effectWarningIdentifiers = BetterEffectHudConfig.getInstance().effectWarningIdentifiers.clone();
                        Identifier[] updatedEffectWarningIdentifiers = new Identifier[effectWarningIdentifiers.length - 1];
                        for (int j = 0; j < effectWarningIdentifiers.length - 1; j++) {
                            if (j < finalI) {updatedEffectWarningIdentifiers[j] = effectWarningIdentifiers[j]; continue;}
                            updatedEffectWarningIdentifiers[j] = effectWarningIdentifiers[j + 1 ];
                        }
                        BetterEffectHudConfig.getInstance().effectWarningIdentifiers = updatedEffectWarningIdentifiers;
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().setScreenAndShow(this);
                    },
                    posX, posY, 21, 21, BetterEffectHudAlignment.TOP_LEFT,
                    false));
            addRenderableWidget(BetterEffectHudAbstractScreen.newTexture(
                    posX + 1, posY + 1, 18, 18, 0xFFFFFFFF,
                    RenderPipelines.GUI_TEXTURED,
                    Optional.of(effect).map((id) -> id.withPrefix("mob_effect/")).orElseGet(MissingTextureAtlasSprite::getLocation)));
            i += 1;
        }
    }
}