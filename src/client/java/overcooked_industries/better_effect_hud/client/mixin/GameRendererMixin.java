package overcooked_industries.better_effect_hud.client.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import overcooked_industries.better_effect_hud.client.BetterEffectHudClient;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Unique private int frames = 0;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(CallbackInfo ci) {
        frames += 1;
        if (frames >= 10) {
            frames = 0;
            BetterEffectHudClient.incrementColorTime();
        }
    }
}