package overcooked_industries.better_effect_hud.client.mixin;

import com.google.common.collect.Ordering;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

import static net.minecraft.client.gui.Hud.getMobEffectSprite;

@Mixin(Hud.class)
public class HudMixin {
    // TODO: I know this mixin is complete dogsh*t, so please rewrite it so it injects at this part and it removes all the commented part.
    @Shadow
    private final Minecraft minecraft;

    public HudMixin(Minecraft minecraft) {
        this.minecraft = minecraft;
    }
    @Shadow
    private static final Identifier EFFECT_BACKGROUND_AMBIENT_SPRITE = Identifier.withDefaultNamespace("hud/effect_background_ambient");
    @Shadow
    private static final Identifier EFFECT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/effect_background");

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void extractEffects(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker) {
        Collection<MobEffectInstance> activeEffects = this.minecraft.player.getActiveEffects();
        if (!activeEffects.isEmpty() && (this.minecraft.gui.screen() == null || !this.minecraft.gui.screen().showsActiveEffects())) {
            int beneficialCount = 0;
            int harmfulCount = 0;

            for(MobEffectInstance instance : Ordering.natural().reverse().sortedCopy(activeEffects)) {
                // <-- INJECT HERE
                /* Holder<MobEffect> effect = instance.getEffect();
                if (instance.showIcon()) {
                    int x = graphics.guiWidth();
                    int y = 1;
                    if (this.minecraft.isDemo()) {
                        y += 15;
                    }

                    if (((MobEffect)effect.value()).isBeneficial()) {
                        ++beneficialCount;
                        x -= 25 * beneficialCount;
                    } else {
                        ++harmfulCount;
                        x -= 25 * harmfulCount;
                        y += 26;
                    } */

                    float alpha = 1.0F;
                    if (instance.isAmbient()) {
                        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_AMBIENT_SPRITE, x, y, 24, 24);
                    } else {
                        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SPRITE, x, y, 24, 24);
                        if (instance.endsWithin(200)) {
                            int remainingDuration = instance.getDuration();
                            int usedSeconds = 10 - remainingDuration / 20;
                            alpha = Mth.clamp((float)remainingDuration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((double)((float)remainingDuration * (float)Math.PI / 5.0F)) * Mth.clamp((float)usedSeconds / 10.0F * 0.25F, 0.0F, 0.25F);
                            alpha = Mth.clamp(alpha, 0.0F, 1.0F);
                        }
                    }

                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getMobEffectSprite(effect), x + 3, y + 3, 18, 18, ARGB.white(alpha));
                }
            }

        }
    }
}