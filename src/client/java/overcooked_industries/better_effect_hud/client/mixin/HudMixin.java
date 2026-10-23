package overcooked_industries.better_effect_hud.client.mixin;

import com.google.common.collect.Ordering;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.border.WorldBorder;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.*;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudColorGradientBox;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudLabel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mixin(Hud.class)
public abstract class HudMixin {
    @Unique public int warningColor;
    @Unique public int warningTargetColor;
    @Unique public MobEffectInstance[] currentMobEffects = new MobEffectInstance[0];
    @Unique public int[] currentMobEffectsMaxTime = new int[0];
    @Unique private static final float[] unitConversions = {20, 60, 60, 24, 7, 52.1429f, 1};
    @Unique public int beneficialCount = 0;
    @Unique public int harmfulCount = 0;
    @Unique public int extraBeneficialCount = 0;
    @Unique public int extraHarmfulCount = 0;

    @Inject(method = "extractEffects", at = @At("HEAD"))
    private void extractEffects(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        beneficialCount = 0;
        harmfulCount = 0;
        extraBeneficialCount = 0;
        extraHarmfulCount = 0;
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"), method = "extractEffects")
    private void background(
            GuiGraphicsExtractor instance,
            RenderPipeline renderPipeline,
            Identifier location,
            int x, int y, int width, int height,
            Operation<Void> original,
            @Local(name = "instance") MobEffectInstance mobEffectInstance,
            @Local(name = "activeEffects") Collection<MobEffectInstance> activeEffects,
            @Local(name = "graphics", argsOnly = true) GuiGraphicsExtractor graphics
    ) {
        if (!BetterEffectHudConfig.getInstance().enableMod) {
            original.call(instance, renderPipeline, location, x, y, width, height);
            return;
        }
        MobEffect effect = mobEffectInstance.getEffect().value();
        List<Identifier> extraIdentifiers = Arrays.asList(BetterEffectHudConfig.getInstance().extraEffectIdentifiers);
        boolean extra = extraIdentifiers.contains(BuiltInRegistries.MOB_EFFECT.getKey(effect));
        boolean isBeneficial = effect.isBeneficial();
        if (extra) {
            if (isBeneficial) extraBeneficialCount += 1;
            else extraHarmfulCount += 1;
        } else {
            if (isBeneficial) beneficialCount += 1;
            else harmfulCount += 1;
        }
        boolean stackY = (extra) ? BetterEffectHudConfig.getInstance().extraHudStackY : BetterEffectHudConfig.getInstance().hudStackY;
        boolean invert = (extra) ? BetterEffectHudConfig.getInstance().extraHudInvert : BetterEffectHudConfig.getInstance().hudInvert;
        BetterEffectHudAlignment hudAlignment = (extra) ? BetterEffectHudConfig.getInstance().extraHudAlignment : BetterEffectHudConfig.getInstance().hudAlignment;
        int hudAlignmentX = hudAlignment.posX;
        int hudAlignmentY = hudAlignment.posY;
        int count = (extra) ? (isBeneficial ? extraBeneficialCount : extraHarmfulCount) : (isBeneficial ? beneficialCount : harmfulCount);
        int maxCount = 0;
        for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) if (i.getEffect().value().isBeneficial() == isBeneficial && i.showIcon() && extraIdentifiers.contains(BuiltInRegistries.MOB_EFFECT.getKey(i.getEffect().value())) == extra) maxCount += 1;

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false, 1);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true, invert, 1);
        if (stackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false, invert, 1);
            posY = getPrimaryEffectPos(hudAlignmentY, count, maxCount, true, 1);
        }

        int backgroundColor = getColorOfMode((extra) ? BetterEffectHudConfig.getInstance().extraBackgroundColor : BetterEffectHudConfig.getInstance().backgroundColor, mobEffectInstance);
        int barColor = getColorOfMode(BetterEffectHudConfig.getInstance().barColor, mobEffectInstance);

        float backgroundAlpha = 1.0f;

        if (mobEffectInstance.endsWithin(200) && BetterEffectHudConfig.getInstance().backgroundBlink) {
            int remainingDuration = mobEffectInstance.getDuration() - 4;
            int usedSeconds = 10 - remainingDuration / 20;
            backgroundAlpha = Mth.clamp((float) remainingDuration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((float) remainingDuration * (float) Math.PI / 5.0F) * Mth.clamp((float) usedSeconds / 10.0F * 0.25F, 0.0F, 0.25F);
            backgroundAlpha = Mth.clamp(backgroundAlpha, 0.0F, 1.0F);
        }
        if (!BetterEffectHudConfig.getInstance().backgroundHide) graphics.blitSprite(renderPipeline, location, posX, posY, width, height, backgroundColor & ARGB.colorFromFloat(backgroundAlpha, 1, 1, 1));
        double barLocationId = BetterEffectHudConfig.getInstance().barLocationId;
        float barValue = (float) mobEffectInstance.getDuration() / currentMobEffectsMaxTime[indexOfEffectType(currentMobEffects, mobEffectInstance.getEffect().value())];
        boolean vertical = barLocationId % 2 == 1;
        boolean otherside = barLocationId > 1;
        boolean reverse = BetterEffectHudConfig.getInstance().barReverse;
        boolean out = BetterEffectHudConfig.getInstance().barOut;
        int barBackgroundBrightness = Math.toIntExact(Math.round(BetterEffectHudConfig.getInstance().barBackgroundOpacity * 255));
        int barBackgroundColor = ARGB.color(barBackgroundBrightness, 255, 255, 255);
        if (BetterEffectHudConfig.getInstance().enableDurationBar) {
            if (BetterEffectHudConfig.getInstance().barCircle) {
                drawDurationBarCircle(graphics, posX, posY, 1f, barColor & barBackgroundColor, out);
                drawDurationBarCircle(graphics, posX, posY, barValue, barColor, out);
            } else {
                drawDurationBar(graphics, posX, posY, 1f, barColor & barBackgroundColor, vertical, otherside, reverse, out);
                drawDurationBar(graphics, posX, posY, barValue, barColor, vertical, otherside, reverse, out);
            }
        }
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"), method = "extractEffects")
    private void foreground(
            GuiGraphicsExtractor instance,
            RenderPipeline renderPipeline,
            Identifier location,
            int x, int y, int width, int height, int color,
            Operation<Void> original,
            @Local(name = "instance") MobEffectInstance mobEffectInstance,
            @Local(name = "activeEffects") Collection<MobEffectInstance> activeEffects,
            @Local(name = "graphics", argsOnly = true) GuiGraphicsExtractor graphics
    ) {
        if (!BetterEffectHudConfig.getInstance().enableMod) {
            original.call(instance, renderPipeline, location, x, y, width, height, color);
            return;
        }
        MobEffect effect = mobEffectInstance.getEffect().value();
        boolean isBeneficial = effect.isBeneficial();
        List<Identifier> extraIdentifiers = Arrays.asList(BetterEffectHudConfig.getInstance().extraEffectIdentifiers);
        boolean extra = extraIdentifiers.contains(BuiltInRegistries.MOB_EFFECT.getKey(effect));
        BetterEffectHudAlignment hudAlignment = (extra) ? BetterEffectHudConfig.getInstance().extraHudAlignment : BetterEffectHudConfig.getInstance().hudAlignment;
        boolean stackY = (extra) ? BetterEffectHudConfig.getInstance().extraHudStackY : BetterEffectHudConfig.getInstance().hudStackY;
        boolean invert = (extra) ? BetterEffectHudConfig.getInstance().extraHudInvert : BetterEffectHudConfig.getInstance().hudInvert;
        int hudAlignmentX = hudAlignment.posX;
        int hudAlignmentY = hudAlignment.posY;
        int count = (extra) ? (isBeneficial ? extraBeneficialCount : extraHarmfulCount) : (isBeneficial ? beneficialCount : harmfulCount);
        int maxCount = 0;
        for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) if (i.getEffect().value().isBeneficial() == isBeneficial && i.showIcon() && extraIdentifiers.contains(BuiltInRegistries.MOB_EFFECT.getKey(i.getEffect().value())) == extra) maxCount += 1;
        float guiDownscale = (float) BetterEffectHudConfig.getInstance().infoSize;
        float guiUpscale = 1 / guiDownscale;

        if (
                Arrays.stream(BetterEffectHudConfig.getInstance().effectWarningIdentifiers).toList().contains(BuiltInRegistries.MOB_EFFECT.getKey(effect)) &&
                mobEffectInstance.getDuration() < BetterEffectHudConfig.getInstance().vignetteTime * 20
        ) {
            warningTargetColor = effect.getColor() & 0x00FFFFFF;
        }

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false, 1);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true, invert, 1);
        if (stackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false, invert, 1);
            posY = getPrimaryEffectPos(hudAlignmentY, count, maxCount, true, 1);
        }
        BetterEffectHudAlignment timerPos = BetterEffectHudConfig.getInstance().timerPos;
        BetterEffectHudAlignment potencyPos = BetterEffectHudConfig.getInstance().potencyPos;

        int timerPosX = Math.round((posX + 3 + 9 * timerPos.posX) * guiUpscale);
        int timerPosY = Math.round((posY + 3 + 9 * timerPos.posY) * guiUpscale);
        int potencyPosX = Math.round((posX + 3 + 9 * potencyPos.posX) * guiUpscale);
        int potencyPosY = Math.round((posY + 3 + 9 * potencyPos.posY) * guiUpscale);

        graphics.blitSprite(renderPipeline, location, posX + 3, posY + 3, width, height, color);
        graphics.pose().pushMatrix();
        graphics.pose().scale(guiDownscale);
        BetterEffectHudLabel timerLabel = BetterEffectHudAbstractScreen.newLabel(Component.literal(getDuration(mobEffectInstance)), timerPosX, timerPosY, getColorOfMode(BetterEffectHudConfig.getInstance().timerColor, mobEffectInstance), BetterEffectHudAlignment.TOP_LEFT, timerPos, true);
        BetterEffectHudLabel potencyLabel = BetterEffectHudAbstractScreen.newLabel(Component.literal(getPotency(mobEffectInstance)), potencyPosX, potencyPosY, getColorOfMode(BetterEffectHudConfig.getInstance().potencyColor, mobEffectInstance), BetterEffectHudAlignment.TOP_LEFT, potencyPos, true);

        if (BetterEffectHudConfig.getInstance().enableTimer) timerLabel.extractWidgetRenderState(graphics, 0, 0, 0);
        if (BetterEffectHudConfig.getInstance().enablePotency) potencyLabel.extractWidgetRenderState(graphics, 0, 0, 0);
        graphics.pose().scale(guiUpscale);

        if (BetterEffectHudConfig.getInstance().enableWarning && mobEffectInstance.getDuration() < BetterEffectHudConfig.getInstance().warningTime * 20) graphics.blit(renderPipeline, BetterEffectHud.id("textures/gui/effect_warning.png"), posX, posY, 0, 0, 8, 8, 8, 8, color);
        graphics.pose().popMatrix();
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getActiveEffects()Ljava/util/Collection;"), method = "extractEffects")
    private Collection<MobEffectInstance> setCurrentEffects(LocalPlayer instance, Operation<Collection<MobEffectInstance>> original) {
        if (!BetterEffectHudConfig.getInstance().enableMod) return original.call(instance);
        Collection<MobEffectInstance> activeEffects = original.call(instance);
        if (currentMobEffects == null) currentMobEffects = new MobEffectInstance[0];
        if (currentMobEffectsMaxTime == null) currentMobEffectsMaxTime = new int[0];

        for (MobEffectInstance effect : activeEffects) {
            int index = indexOfEffectType(currentMobEffects, effect.getEffect().value());
            if (index == -1) {
                MobEffectInstance[] currentMobEffectsCopy = Arrays.copyOf(currentMobEffects, currentMobEffects.length + 1);
                int[] currentMobEffectsMaxTimeCopy = Arrays.copyOf(currentMobEffectsMaxTime, currentMobEffectsMaxTime.length + 1);
                currentMobEffectsCopy[currentMobEffects.length] = effect;
                currentMobEffectsMaxTimeCopy[currentMobEffectsMaxTime.length] = effect.getDuration();
                currentMobEffects = currentMobEffectsCopy;
                currentMobEffectsMaxTime = currentMobEffectsMaxTimeCopy;
            } else if (effect.getDuration() > currentMobEffectsMaxTime[index]) {
                currentMobEffectsMaxTime[index] = effect.getDuration();
                currentMobEffects[index] = effect;
            }
        }

        List<MobEffectInstance> stillActive = new ArrayList<>();
        List<Integer> stillActiveMaxTime = new ArrayList<>();
        for (int i = 0; i < currentMobEffects.length; i++) {
            MobEffect type = currentMobEffects[i].getEffect().value();
            boolean stillHasType = false;
            for (MobEffectInstance active : activeEffects) {
                if (active.getEffect().value() == type) {
                    stillHasType = true;
                    break;
                }
            }
            if (stillHasType) {
                stillActive.add(currentMobEffects[i]);
                stillActiveMaxTime.add(currentMobEffectsMaxTime[i]);
            }
        }
        currentMobEffects = stillActive.toArray(new MobEffectInstance[0]);
        currentMobEffectsMaxTime = stillActiveMaxTime.stream().mapToInt(Integer::intValue).toArray();

        return activeEffects;
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getDuration()I"), method = "extractEffects")
    private int extractVignette(MobEffectInstance instance, Operation<Integer> original) {
        return original.call(instance) - 4;
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;colorFromFloat(FFFF)I"), method = "extractVignette")
    private int extractVignette(float alpha, float red, float green, float blue, Operation<Integer> original, @Local(name = "camera", argsOnly = true) @Nullable Entity camera) {
        if (!BetterEffectHudConfig.getInstance().enableMod) return original.call(alpha, red, green, blue);
        int org = original.call(alpha, red, green, blue);

        WorldBorder worldBorder = Minecraft.getInstance().level.getWorldBorder();
        float borderWarningStrength = 0.0F;
        if (camera != null) {
            float distToBorder = (float) worldBorder.getDistanceToBorder(camera);
            double movingBlocksThreshold = Math.min(worldBorder.getLerpSpeed() * (double) worldBorder.getWarningTime(), Math.abs(worldBorder.getLerpTarget() - worldBorder.getSize()));
            double warningDistance = Math.max(worldBorder.getWarningBlocks(), movingBlocksThreshold);
            if ((double) distToBorder < warningDistance) {
                borderWarningStrength = 1.0F - (float) ((double) distToBorder / warningDistance);
            }
            borderWarningStrength = Mth.clamp(borderWarningStrength, 0.0F, 1.0F);
        }
        int vignetteTransparency = Math.round((float) (BetterEffectHudConfig.getInstance().vignetteTransparency) / 100 * 255);
        int borderRed = Math.clamp((int) (borderWarningStrength * 255) + vignetteTransparency, 0, 255);
        int borderGreenBlue = Math.clamp(vignetteTransparency - (int) (borderWarningStrength * 255), 0, 255);
        int borderColor = ARGB.color(255, borderRed, borderGreenBlue, borderGreenBlue);

        warningColor = ARGB.linearLerp(0.05f, warningColor, warningTargetColor);
        int color = warningColor;
        warningTargetColor = borderColor;

        if (BetterEffectHudConfig.getInstance().enableVignette) return getVignetteBlendColor(ARGB.color(255, color), 0xFF_FF_FF_FF);
        return org;
    }

    ///# Uses Blend mode ONE_MINUS_SRC_COLOR
    ///### GL and VK respectively:
    /// - GL_ONE_MINUS_SRC_COLOR 0x301
    /// - VK_BLEND_FACTOR_ONE_MINUS_SRC_COLOR 0x3
    private int getVignetteBlendColor(int target, int baseColor) {
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

    private int getPrimaryEffectPos(int hudAlignment, int count, int maxCount, boolean isY, float guiDownscale) {
        float pos = 0;
        float windowSize = (isY) ? Minecraft.getInstance().getWindow().getGuiScaledHeight() : Minecraft.getInstance().getWindow().getGuiScaledWidth();

        switch (hudAlignment) {
            case 0 -> {
                pos += 1;
                pos += 25 * (count - 1);
            }
            case 1 -> {
                pos += windowSize / 2;
                pos -= 12.5f * maxCount;
                pos += 25 * (count - 1);
            }
            case 2 -> {
                pos += windowSize;
                pos -= 25 * count;
            }
        }
        return Math.round(pos / guiDownscale);
    }
    private int getSecondaryEffectPos(int hudAlignment, boolean isBeneficial, boolean isY, boolean invert, float guiDownscale) {
        int pos = 0;
        float windowSize = (isY) ? Minecraft.getInstance().getWindow().getGuiScaledHeight() : Minecraft.getInstance().getWindow().getGuiScaledWidth();
        if (hudAlignment == 0) pos += 1;

        pos += (int) (windowSize * ((float) hudAlignment / 2));
        pos -= 50 * (hudAlignment / 2);

        if (!isBeneficial == invert) pos += 25;
        return Math.round(pos / guiDownscale);
    }

    private static int indexOfEffectType(MobEffectInstance[] array, MobEffect type) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].getEffect().value() == type) return i;
        }
        return -1;
    }

    private void drawDurationBar(GuiGraphicsExtractor graphics, int posX, int posY, float value, int color, boolean vertical, boolean otherside, boolean reverse, boolean out) {
        int lengh = (int) Math.floor(value * 18);
        posX += 2;
        posY += 2;
        int offsetX = 0;
        int offsetY = 0;
        if (vertical) {
            offsetX += 1;
            offsetY += lengh;
            posY += 1;
            if (otherside) posX += 19;
            if (reverse) posY += 18 - lengh;
            if (out) posX -= 3;
            if (otherside && out) posX += 6;
        } else {
            offsetY += 1;
            offsetX += lengh;
            posX += 1;
            if (otherside) posY += 19;
            if (reverse) posX += 18 - lengh;
            if (out) posY -= 3;
            if (otherside && out) posY += 6;
        }
        graphics.fill(posX, posY, posX + offsetX,posY + offsetY, color);
    }
    private void drawDurationBarCircle(GuiGraphicsExtractor graphics, int posX, int posY, float barValue, int color, boolean out) {
        drawDurationBar(graphics, posX, posY, inverseLerpClamp(0.5f, 0.75f, barValue), color, false, false, true, out);
        drawDurationBar(graphics, posX, posY, inverseLerpClamp(0f, 0.25f, barValue), color, false, true, false, out);
        drawDurationBar(graphics, posX, posY, inverseLerpClamp(0.75f, 1.00f, barValue), color, true, false, false, out);
        drawDurationBar(graphics, posX, posY, inverseLerpClamp(0.25f, 0.5f, barValue), color, true, true, true, out);
    }

    private String getDuration(MobEffectInstance mobEffectInstance) {
        if (mobEffectInstance.isInfiniteDuration()) {return Component.translatable("effect.duration.infinite").getString();}
        float time = mobEffectInstance.getDuration();
        float doubleTime = 0;
        int suffixId = 0;
        String suffix;
        boolean useMs = BetterEffectHudConfig.getInstance().timerUseMs;

        int minUnit = BetterEffectHudConfig.getInstance().timerMinUnit;
        int maxUnit = BetterEffectHudConfig.getInstance().timerMaxUnit;

        for (int index = 0; index < 6; index++) {
            if (!proceedNextUnit(time, index, minUnit, maxUnit)) break;
            time = getNextUnit(time, index, minUnit, maxUnit);
            suffixId += 1;
        }
        if (suffixId != 0) {
            doubleTime = (float) ((time - Math.floor(time)) * unitConversions[suffixId - 1]);
        }
        suffix = new String[]{"t", "s", "m", "h", "d", "w", "y"}[suffixId];
        if (suffixId == 0 && useMs) {
            suffix = "ms";
            time /= 20;
            time *= 1000;
        }

        String doubleZeros = getPrefixZeros(doubleTime, unitConversions[Math.clamp(suffixId - 1, 0, 6)]);

        int decimalCount = BetterEffectHudConfig.getInstance().timerDecimalCount;
        if (BetterEffectHudConfig.getInstance().timerUseDecimals[suffixId]) return String.format(String.format("%%.%sf%%s", decimalCount), time, suffix);
        if (suffixId != 0) if (BetterEffectHudConfig.getInstance().timerUseDouble[suffixId - 1]) return String.format("%.0f:%s%.0f", Math.floor(time), doubleZeros, Math.floor(doubleTime));
        return (int) Math.floor(time) + suffix;
    }
    private String getPotency(MobEffectInstance mobEffectInstance) {
        int potency = mobEffectInstance.getAmplifier();
        if (potency == 0 && BetterEffectHudConfig.getInstance().enableFirstPotency) return "I";
        return Component.translatableWithFallback("potion.potency." + potency, String.valueOf(potency)).getString();
    }

    private int getDurationColor(int time) {
        return BetterEffectHudColorGradientBox.getGradientPiece((float) Math.clamp(time, 0, 2400) / 2400, BetterEffectHudConfig.getInstance().durationColor);
    }

    private int getPotencyColor(int potency) {
        int out = BetterEffectHudConfig.getInstance().potencyColor5.getColor();
        switch (potency) {
            case 0 -> out = BetterEffectHudConfig.getInstance().potencyColor0.getColor();
            case 1 -> out = BetterEffectHudConfig.getInstance().potencyColor1.getColor();
            case 2 -> out = BetterEffectHudConfig.getInstance().potencyColor2.getColor();
            case 3 -> out = BetterEffectHudConfig.getInstance().potencyColor3.getColor();
            case 4 -> out = BetterEffectHudConfig.getInstance().potencyColor4.getColor();
        }
        return out;
    }

    private int getColorOfMode(BetterEffectHudColorSetting color, MobEffectInstance mobEffectInstance) {
        int out = 0xFFFFFFFF;
        final int speed = Math.round(color.getSpeed() * 100);
        float time = (float) BetterEffectHudClient.colorTime / 255 * speed / 10;
        switch (color.getColorMode()) {
            case EFFECT_BENEFIT -> out = (mobEffectInstance.getEffect().value().isBeneficial()) ? getColorOfMode(BetterEffectHudConfig.getInstance().beneficialColor, mobEffectInstance) : getColorOfMode(BetterEffectHudConfig.getInstance().harmfulColor, mobEffectInstance);
            case EFFECT_COLOR -> out = (mobEffectInstance.getEffect().value().getColor() & 0x00FFFFFF) + 0xFF000000;
            case EFFECT_DURATION -> out = getDurationColor(mobEffectInstance.getDuration());
            case EFFECT_POTENCY -> out = getPotencyColor(mobEffectInstance.getAmplifier());
            case RAINBOW -> out = ARGB.color(255, Color.HSBtoRGB(time % 1.0f, 1.0f, 1.0f));
            case MONOCHROME -> out = ARGB.color(255, Color.HSBtoRGB(1.0f, 0.0f, getBrightness(time * 5)));
            case CUSTOM_GRADIENT -> out = BetterEffectHudColorGradientBox.getGradientPiece(time % 1.0f, color);
            case CUSTOM_BLINK -> out = color.getColor() & ARGB.colorFromFloat(1f, getBrightness(time * 5), getBrightness(time * 5), getBrightness(time * 5));
            case CUSTOM -> out = color.getColor();
        }
        return out;
    }

    private static float getBrightness(float time) {
        time = time % 2.0f;
        return time > 1.0f ? 2.0f - time : time;
    }

    private float getNextUnit(float value, int index, int minUnit, int maxUnit) {
        if ((value >= unitConversions[index] || minUnit > index) && maxUnit > index) value /= unitConversions[index];
        return value;
    }
    private boolean proceedNextUnit(float value, int index, int minUnit, int maxUnit) {
        return (value >= unitConversions[index] || minUnit > index) && maxUnit > index;
    }

    private String getPrefixZeros(float number, float maxNumber) {
        int numberLengh = String.valueOf(Math.floor(number)).length();
        int maxNumberLengh = String.valueOf(Math.floor(maxNumber)).length();
        int zeroCount = Math.clamp(maxNumberLengh - numberLengh, 0, Integer.MAX_VALUE);
        return "0".repeat(zeroCount);
    }

    private float inverseLerpClamp(float min, float max, float number) {
        return Math.clamp((number - min) / (max - min), 0, 1);
    }

}