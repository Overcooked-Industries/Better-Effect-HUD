package overcooked_industries.better_effect_hud.client.mixin;

import com.google.common.collect.Ordering;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
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
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.*;
import overcooked_industries.better_effect_hud.client.screens.BetterEffectHudAbstractScreen;
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
        if (!BetterEffectHudConfig.getInstance().enableMod) {
            original.call(instance, renderPipeline, location, x, y, width, height);
            return;
        }
        BetterEffectHudAlignment hudAlignment = BetterEffectHudConfig.getInstance().hudAlignment; int hudAlignmentX = hudAlignment.posX; int hudAlignmentY = hudAlignment.posY;
        Holder<MobEffect> effect = mobEffectInstance.getEffect(); boolean isBeneficial = effect.value().isBeneficial();
        int count = harmfulCount; if (effect.value().isBeneficial()) count = beneficialCount;
        int maxCount = 0; for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) if (i.getEffect().value().isBeneficial() == isBeneficial && i.showIcon()) maxCount += 1;

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false, 1);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true, 1);
        if (BetterEffectHudConfig.getInstance().hudStackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false, 1);
            posY = getPrimaryEffectPos(hudAlignmentY, count, maxCount, true, 1);
        }

        int backgroundColor = getColorOfMode(BetterEffectHudConfig.getInstance().backgroundColor, mobEffectInstance);
        int barColor = getColorOfMode(BetterEffectHudConfig.getInstance().barColor, mobEffectInstance);

        graphics.blitSprite(renderPipeline, location, posX, posY, width, height, backgroundColor);
        double barLocationId = BetterEffectHudConfig.getInstance().barLocationId;
        float barValue = (float) mobEffectInstance.getDuration() / currentMobEffectsMaxTime[indexOfEffectType(currentMobEffects, mobEffectInstance.getEffect().value())];
        int barLengh = Math.round(barValue * 18);
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
                drawDurationBar(graphics, posX, posY, 18, barColor & barBackgroundColor, vertical, otherside, reverse, out);
                drawDurationBar(graphics, posX, posY, barLengh, barColor, vertical, otherside, reverse, out);
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
            @Local(name = "beneficialCount") int beneficialCount,
            @Local(name = "harmfulCount") int harmfulCount,
            @Local(name = "activeEffects") Collection<MobEffectInstance> activeEffects,
            @Local(name = "graphics", argsOnly = true) GuiGraphicsExtractor graphics
    ) {
        if (!BetterEffectHudConfig.getInstance().enableMod) {
            original.call(instance, renderPipeline, location, x, y, width, height, color);
            return;
        }
        BetterEffectHudAlignment hudAlignment = BetterEffectHudConfig.getInstance().hudAlignment; int hudAlignmentX = hudAlignment.posX; int hudAlignmentY = hudAlignment.posY;
        Holder<MobEffect> effect = mobEffectInstance.getEffect(); boolean isBeneficial = effect.value().isBeneficial();
        int count = harmfulCount; if (effect.value().isBeneficial()) count = beneficialCount;
        int maxCount = 0; for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) if (i.getEffect().value().isBeneficial() == isBeneficial && i.showIcon()) maxCount += 1;
        float guiDownscale = (float) BetterEffectHudConfig.getInstance().infoSize;
        float guiUpscale = 1 / guiDownscale;

        if (
                Arrays.stream(BetterEffectHudConfig.getInstance().effectWarningIdentifiers).toList().contains(BuiltInRegistries.MOB_EFFECT.getKey(effect.value())) &&
                mobEffectInstance.getDuration() < BetterEffectHudConfig.getInstance().vignetteTime * 20
        ) {
            warningTargetColor = effect.value().getColor() & 0x00FFFFFF;
        }

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false, 1);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true, 1);
        if (BetterEffectHudConfig.getInstance().hudStackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false, 1);
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
        BetterEffectHudLabel timerLabel = BetterEffectHudAbstractScreen.newLabel(Component.literal(getDuration(mobEffectInstance)), timerPosX, timerPosY, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT, timerPos, true);
        BetterEffectHudLabel potencyLabel = BetterEffectHudAbstractScreen.newLabel(Component.literal(getPotency(mobEffectInstance)), potencyPosX, potencyPosY, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_LEFT, potencyPos, true);

        if (BetterEffectHudConfig.getInstance().enableTimer) timerLabel.extractWidgetRenderState(graphics, 0, 0, 0);
        if (BetterEffectHudConfig.getInstance().enablePotency) potencyLabel.extractWidgetRenderState(graphics, 0, 0, 0);
        graphics.pose().scale(guiUpscale);
        if (BetterEffectHudConfig.getInstance().enableWarning && mobEffectInstance.getDuration() < BetterEffectHudConfig.getInstance().warningTime * 20 && (BetterEffectHudConfig.getInstance().useEffectListWarning || Arrays.asList(BetterEffectHudConfig.getInstance().effectWarningIdentifiers).contains(location))) graphics.blit(renderPipeline, BetterEffectHud.id("textures/gui/effect_warning.png"), posX, posY, 0, 0, 8, 8, 8, 8, color);
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
        float windowSize = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        if (isY) {windowSize = Minecraft.getInstance().getWindow().getGuiScaledHeight();}

        if (hudAlignment == 0) {
            pos += 1;
            pos += 25 * (count - 1);
        }
        if (hudAlignment == 1) {
            pos += windowSize / 2;
            pos -= 12.5f * maxCount;
            pos += 25 * (count - 1);
        }
        if (hudAlignment == 2) {
            pos += windowSize;
            pos -= 25 * count;
        }
        return Math.round(pos / guiDownscale);
    }
    private int getSecondaryEffectPos(int hudAlignment, boolean isBeneficial, boolean isY, float guiDownscale) {
        int pos = 0;
        int windowSize = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        if (isY) {windowSize = Minecraft.getInstance().getWindow().getGuiScaledHeight();}
        if (hudAlignment == 0) {pos += 1;}

        pos += (int) (windowSize * ((float) hudAlignment / 2));
        pos -= 50 * (hudAlignment / 2);

        if (!isBeneficial) pos += 25;
        return Math.round(pos / guiDownscale);
    }

    private static int indexOfEffectType(MobEffectInstance[] array, MobEffect type) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].getEffect().value() == type) return i;
        }
        return -1;
    }

    private void drawDurationBar(GuiGraphicsExtractor graphics, int posX, int posY, int lengh, int color, boolean vertical, boolean otherside, boolean reverse, boolean out) {
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
        drawDurationBar(graphics, posX, posY, (int) (inverseLerpClamp(0.5f, 0.75f, barValue) * 18), color, false, false, true, out);
        drawDurationBar(graphics, posX, posY, (int) (inverseLerpClamp(0f, 0.25f, barValue) * 18), color, false, true, false, out);
        drawDurationBar(graphics, posX, posY, (int) (inverseLerpClamp(0.75f, 1.00f, barValue) * 18), color, true, false, false, out);
        drawDurationBar(graphics, posX, posY, (int) (inverseLerpClamp(0.25f, 0.5f, barValue) * 18), color, true, true, true, out);
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
        int out = 0xFFFFFFFF;
        if (time < 100) out = 0xFFF01111;
        if (100 <= time && time < 200) out = 0xFFF06010;
        if (200 <= time && time < 400) out = 0xFFFFC010;
        if (400 <= time && time < 1200) out = 0xFFDDFF10;
        if (1200 <= time) out = 0xFF60B010;
        return out;
    }

    private int getColorOfMode(BetterEffectHudColorSetting color, MobEffectInstance mobEffectInstance) {
        int out = 0xFFFFFFFF;
        final int speed = Math.round(color.getSpeed() * 100);
        final float brightness = Math.abs((((float) BetterEffectHudClient.colorTime / 255 * speed * 2) % (speed * 2)) - speed) * -1 + speed;
        switch (color.getColorMode()) {
            case EFFECT_BENEFIT -> out = (mobEffectInstance.getEffect().value().isBeneficial()) ? 0xFF3750F0 : 0xFFE02040;
            case EFFECT_COLOR -> out = (mobEffectInstance.getEffect().value().getColor() & 0x00FFFFFF) + 0xFF000000;
            case EFFECT_DURATION -> out = getDurationColor(mobEffectInstance.getDuration());
            case RAINBOW -> out = ARGB.color(255, Color.HSBtoRGB((((float) BetterEffectHudClient.colorTime / 255 * speed) % speed) / speed, 1.0f, 1.0f));
            case MONOCHROME -> out = ARGB.color(255, Color.HSBtoRGB(1.0f, 0.0f, brightness));
            case CUSTOM_BLINK -> out = ARGB.color(ARGB.alpha(color.getColor()), (int) (ARGB.red(color.getColor()) * brightness), (int) (ARGB.green(color.getColor()) * brightness), (int) (ARGB.blue(color.getColor()) * brightness));
            case CUSTOM -> out = color.getColor();
        }
        return out;
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