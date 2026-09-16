package overcooked_industries.better_effect_hud.client.mixin;

import com.google.common.collect.Ordering;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.Collection;

@Mixin(Hud.class) //TODO: Make this Code more readable and just better.
public class HudMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"), method = "extractEffects")
    private void background(
            GuiGraphicsExtractor instance,
            RenderPipeline renderPipeline,
            Identifier location,
            int x, int y, int width, int height,
            Operation<Void> original,
            @Local(name = "instance") MobEffectInstance mobEffectInstance,
            @Local(name = "beneficialCount") int beneficialCount,
            @Local(name = "harmfulCount") int harmfulCount,
            @Local(name = "activeEffects") Collection<MobEffectInstance> activeEffects,
            @Local(name = "graphics", argsOnly = true) GuiGraphicsExtractor graphics
    ) {
        BetterEffectHudAlignment hudAlignment = BetterEffectHudConfig.getInstance().hudAlignment; int hudAlignmentX = hudAlignment.posX; int hudAlignmentY = hudAlignment.posY;
        Holder<MobEffect> effect = mobEffectInstance.getEffect(); boolean isBeneficial = effect.value().isBeneficial();
        int count = harmfulCount; if (effect.value().isBeneficial()) count = beneficialCount;
        int maxCount = 0; for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) if (i.getEffect().value().isBeneficial() == isBeneficial) maxCount += 1;

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true);
        if (BetterEffectHudConfig.getInstance().stackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false);
            posY = getPrimaryEffectPos(hudAlignmentY, count, maxCount, true);
        }

        graphics.blitSprite(renderPipeline, location, posX, posY, width, height);
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"), method = "extractEffects")
    private void icon(
            GuiGraphicsExtractor instance,
            RenderPipeline renderPipeline,
            Identifier location,
            int x, int y, int width, int height, int color,
            Operation<Void> original,
            @Local(name = "instance") MobEffectInstance mobEffectInstance,
            @Local(name = "beneficialCount") int beneficialCount,
            @Local(name = "harmfulCount") int harmfulCount,
            @Local(name = "activeEffects") Collection<MobEffectInstance> activeEffects,
            @Local(name = "graphics", argsOnly = true) GuiGraphicsExtractor graphics
    ) {
        BetterEffectHudAlignment hudAlignment = BetterEffectHudConfig.getInstance().hudAlignment; int hudAlignmentX = hudAlignment.posX; int hudAlignmentY = hudAlignment.posY;
        Holder<MobEffect> effect = mobEffectInstance.getEffect(); boolean isBeneficial = effect.value().isBeneficial();
        int count = harmfulCount; if (effect.value().isBeneficial()) count = beneficialCount;
        int maxCount = 0; for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) if (i.getEffect().value().isBeneficial() == isBeneficial) maxCount += 1;
        float guiDownscale = BetterEffectHudConfig.getInstance().timerSize; float guiUpscale = 1 / guiDownscale;

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true);
        if (BetterEffectHudConfig.getInstance().stackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false);
            posY = getPrimaryEffectPos(hudAlignmentY, count, maxCount, true);
        }

        graphics.blitSprite(renderPipeline, location, posX + 3, posY + 3, width, height, color);
        graphics.pose().pushMatrix();
        graphics.pose().scale(guiDownscale, guiDownscale);
        graphics.text(minecraft.font, getDuration(mobEffectInstance), (int) ((posX + 3) * guiUpscale), (int) ((posY + 3) * guiUpscale), 0xFFFFFFFF);
        graphics.text(minecraft.font, getPotency(mobEffectInstance), posX, posY + 16, BetterEffectHudConfig.getInstance().potencyColor);
        graphics.pose().popMatrix();
    }

    private int getPrimaryEffectPos(int hudAlignment, int count, int maxCount, boolean isY) {
        int pos = 0;
        int windowSize = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        if (isY) {windowSize = Minecraft.getInstance().getWindow().getGuiScaledHeight();}

        if (hudAlignment == 0) {
            pos += 1;
            pos += 25 * (count - 1);
        }
        if (hudAlignment == 1) {
            pos += windowSize / 2;
            pos -= (int) (12.5 * maxCount);
            pos += 25 * (count - 1);
        }
        if (hudAlignment == 2) {
            pos += windowSize;
            pos -= 25 * count;
        }
        return pos;
    }
    private int getSecondaryEffectPos(int hudAlignment, boolean isBeneficial, boolean isY) {
        int pos = 0;
        int windowSize = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        if (isY) {windowSize = Minecraft.getInstance().getWindow().getGuiScaledHeight();}
        if (hudAlignment == 0) {pos += 1;}

        pos += (int) (windowSize * ((float) hudAlignment / 2));
        pos -= 50 * (hudAlignment / 2);

        if (!isBeneficial) {pos += 25;}
        return pos;
    }

    private String getDuration(MobEffectInstance mobEffectInstance) {
        double time = mobEffectInstance.getDuration();
        String suffix = "t";
        boolean useMs = BetterEffectHudConfig.getInstance().timerUseMs;
        int minUnit = BetterEffectHudConfig.getInstance().timerMinUnit;
        int maxUnit = BetterEffectHudConfig.getInstance().timerMaxUnit;

        //This is some bullsh*t code, please make a better alternative
        if ((time >= 20 || minUnit > 0) && maxUnit > 0) {
            time /= 20; suffix = "s";
            if ((time >= 60 || minUnit > 1) && maxUnit > 1) {
                time /= 60; suffix = "m";
                if ((time >= 60 || minUnit > 2) && maxUnit > 2) {
                    time /= 60; suffix = "h";
                    if ((time >= 24 || minUnit > 3) && maxUnit > 3) {
                        time /= 24; suffix = "d";
                        if ((time >= 7 || minUnit > 4) && maxUnit > 4) {
                            time /= 7; suffix = "w";
                            if ((time >= 7 || minUnit > 5) && maxUnit > 5) {
                                time /= 7; suffix = "y";
                            }
                        }
                    }
                }
            }
        }

        if (suffix == "t" && useMs) {
            suffix = "ms";
            time /= 20;
            time *= 1000;
        }

        if (mobEffectInstance.isInfiniteDuration()) {return Component.translatable("effect.duration.infinite").getString();}
        return String.format("%.1f", time) + suffix;
    }
    private String getPotency(MobEffectInstance mobEffectInstance) {
        int potency = mobEffectInstance.getAmplifier();
        return Component.translatableWithFallback("potion.potency." + potency, String.valueOf(potency)).getString();
    }
}