package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;

import java.lang.invoke.VarHandle;
import java.util.Arrays;
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
            Identifier[] identifiers = (Identifier[]) handle.get(BetterEffectHudConfig.getInstance());
            Identifier[] updatedIdentifiers = Arrays.copyOf(identifiers, identifiers.length + 1);
            updatedIdentifiers[identifiers.length] = Identifier.withDefaultNamespace("strength");
            handle.set(BetterEffectHudConfig.getInstance(), updatedIdentifiers);
            BetterEffectHudConfig.serialize();
            Minecraft.getInstance().setScreenAndShow(plusButtonScreen);
        }, 13,  13, 21, 21, BetterEffectHudAlignment.TOP_LEFT, false));
        addRenderableWidget(BetterEffectHudAbstractScreen.newButton(Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(screen), 0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE, false));
        int i = 0;
        for (Identifier effect : value) {
            int width = Math.round((float) (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 40) / 18) * 18;
            int posX = (int) (10 + Math.floor((float) ((i * 21) % width) / 21   ) * 21  + 3);
            int posY = (int) (34 + Math.floor((float) ( i * 21)          / width) * 21) + 3;
            int finalI = i;
            addRenderableWidget(BetterEffectHudAbstractScreen.newButton(Component.empty(),
                    (btn) -> {
                        Identifier[] identifiers = (Identifier[]) handle.get(BetterEffectHudConfig.getInstance());
                        Identifier[] updatedIdentifiers = new Identifier[identifiers.length - 1];
                        for (int j = 0; j < identifiers.length - 1; j++) {
                            if (j < finalI) {updatedIdentifiers[j] = identifiers[j]; continue;}
                            updatedIdentifiers[j] = identifiers[j + 1 ];
                        }
                        handle.set(BetterEffectHudConfig.getInstance(), updatedIdentifiers);
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().setScreenAndShow(this);
                    },
                    posX, posY, 21, 21, BetterEffectHudAlignment.TOP_LEFT,
                    false));
            addRenderableWidget(BetterEffectHudAbstractScreen.newTexture(
                    posX + 1, posY + 1, 18, 18, 0xFFFFFFFF,
                    RenderPipelines.GUI_TEXTURED,
                    Optional.of(effect).map((id) -> id.withPrefix("textures/mob_effect/").withSuffix(".png")).orElseGet(MissingTextureAtlasSprite::getLocation), BetterEffectHudAlignment.TOP_LEFT));
            i += 1;
        }
    }
}