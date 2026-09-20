package overcooked_industries.better_effect_hud.client.mixin;

import com.google.common.collect.Ordering;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudLabel;

import java.util.Collection;

@Mixin(Hud.class) //TODO: Make this Code more readable and just better.
public abstract class HudMixin {
    @Unique
    public int warningColor;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"), method = "extractEffects")
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
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"), method = "extractEffects")
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
        float guiDownscale = (float) BetterEffectHudConfig.getInstance().timerSize; float guiUpscale = 1 / guiDownscale;

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true);
        if (BetterEffectHudConfig.getInstance().stackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false);
            posY = getPrimaryEffectPos(hudAlignmentY, count, maxCount, true);
        }
        warningColor = effect.value().getColor();

        graphics.blitSprite(renderPipeline, location, posX + 3, posY + 3, width, height, color);
        graphics.pose().pushMatrix();
        graphics.pose().scale(guiDownscale, guiDownscale);
        BetterEffectHudAlignment timerPos = BetterEffectHudConfig.getInstance().timerPos;
        BetterEffectHudAlignment potencyPos = BetterEffectHudConfig.getInstance().potencyPos;
        BetterEffectHudLabel timerLabel = new BetterEffectHudLabel(Component.literal(getDuration(mobEffectInstance)), (int) ((posX + 3 + 9 * (timerPos.posX)) * guiUpscale), (int) ((posY + 3 + 10 * (timerPos.posY)) * guiUpscale), 0xFFFFFFFF, timerPos);
        BetterEffectHudLabel potencyLabel = new BetterEffectHudLabel(Component.literal(getPotency(mobEffectInstance)), (int) ((posX + 3 + 9 * (potencyPos.posX)) * guiUpscale), (int) ((posY + 3 + 10 * (potencyPos.posY)) * guiUpscale), 0xFFFFFFFF, potencyPos);
        timerLabel.extractWidgetRenderState(graphics, 0, 0, 0);
        potencyLabel.extractWidgetRenderState(graphics, 0, 0, 0);
        graphics.pose().popMatrix();
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;colorFromFloat(FFFF)I"), method = "extractVignette")
    private int extractVignette(float alpha, float red, float green, float blue, Operation<Integer> original) {
        return getVignetteBlendColor(ARGB.color(0xFF, warningColor), 0xFF_FF_FF_FF);
    }

    ///# Uses Blend mode ONE_MINUS_SRC_COLOR
    ///### GL and VK respectively:
    /// - GL_ONE_MINUS_SRC_COLOR 0x301
    /// - VK_BLEND_FACTOR_ONE_MINUS_SRC_COLOR 0x3
    int getVignetteBlendColor(int target, int baseColor) {
        int baseA = ARGB.alpha(target);
        int baseR = ARGB.red(baseColor);
        int baseG = ARGB.green(baseColor);
        int baseB = ARGB.blue(baseColor);

        int targetR = ARGB.red(target);
        int targetG = ARGB.green(target);
        int targetB = ARGB.blue(target);

        int resultR = baseR == 0 ? 0 : 0xFF - (targetR << 8 / baseR);
        int resultG = baseG == 0 ? 0 : 0xFF - (targetG << 8 / baseG);
        int resultB = baseB == 0 ? 0 : 0xFF - (targetB << 8 / baseB);

        resultR = Math.clamp(resultR, 0, 0xFF);
        resultG = Math.clamp(resultG, 0, 0xFF);
        resultB = Math.clamp(resultB, 0, 0xFF);
        return (baseA << 24) | (resultR << 16) | (resultG << 8) | resultB;
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
        if (mobEffectInstance.isInfiniteDuration()) {return Component.translatable("effect.duration.infinite").getString();}
        double time = mobEffectInstance.getDuration();
        int suffixId = 0;
        String suffix;
        boolean useMs = BetterEffectHudConfig.getInstance().timerUseMs;
        int minUnit = BetterEffectHudConfig.getInstance().timerMinUnit;
        int maxUnit = BetterEffectHudConfig.getInstance().timerMaxUnit;

        //This is some bullsh*t code, please make a better alternative
        if ((time >= 20 || minUnit > 0) && maxUnit > 0) {
            time /= 20; suffixId = 1;
            if ((time >= 60 || minUnit > 1) && maxUnit > 1) {
                time /= 60; suffixId = 2;
                if ((time >= 60 || minUnit > 2) && maxUnit > 2) {
                    time /= 60; suffixId = 3;
                    if ((time >= 24 || minUnit > 3) && maxUnit > 3) {
                        time /= 24; suffixId = 4;
                        if ((time >= 7 || minUnit > 4) && maxUnit > 4) {
                            time /= 7; suffixId = 5;
                            if ((time >= 52.1429 || minUnit > 5) && maxUnit > 5) {
                                time /= 52.1429; suffixId = 6;
                            }
                        }
                    }
                }
            }
        }

        suffix = new String[]{"t", "s", "m", "h", "d", "w", "y"}[suffixId];
        if (suffixId == 0 && useMs) {
            suffix = "ms";
            time /= 20;
            time *= 1000;
        }
        if (BetterEffectHudConfig.getInstance().timerUseDecimals[suffixId]) return String.format("%." + BetterEffectHudConfig.getInstance().timerDecimalCount + "f", time) + suffix;
        return String.format("%.0f", time) + suffix;
    }
    private String getPotency(MobEffectInstance mobEffectInstance) {
        int potency = mobEffectInstance.getAmplifier();
        return Component.translatableWithFallback("potion.potency." + potency, String.valueOf(potency)).getString();
    }
}