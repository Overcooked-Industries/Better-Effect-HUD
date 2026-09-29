package overcooked_industries.better_effect_hud.client.mixin;

import com.google.common.collect.Ordering;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.components.ChatComponent;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudLabel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mixin(Hud.class) //TODO: Make this Code more readable and just better.
public abstract class HudMixin {
    @Shadow
    @Final
    private ChatComponent chat;
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
        BetterEffectHudAlignment hudAlignment = BetterEffectHudConfig.getInstance().hudAlignment; int hudAlignmentX = hudAlignment.posX; int hudAlignmentY = hudAlignment.posY;
        Holder<MobEffect> effect = mobEffectInstance.getEffect(); boolean isBeneficial = effect.value().isBeneficial();
        int count = harmfulCount; if (effect.value().isBeneficial()) count = beneficialCount;
        int maxCount = 0; for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) if (i.getEffect().value().isBeneficial() == isBeneficial && i.showIcon()) maxCount += 1;
        float guiDownscale = (float) BetterEffectHudConfig.getInstance().hudSize; float guiUpscale = 1 / guiDownscale;

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false, guiUpscale);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true, guiUpscale);
        if (BetterEffectHudConfig.getInstance().hudStackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false, guiUpscale);
            posY = getPrimaryEffectPos(hudAlignmentY, count, maxCount, true, guiUpscale);
        }

        graphics.pose().pushMatrix();
        graphics.pose().scale(guiDownscale, guiDownscale);
        int color = (mobEffectInstance.getEffect().value().getColor() & 0x00FFFFFF) + 0xFF000000;
        graphics.blitSprite(renderPipeline, location, posX, posY, width, height, color);
        int barLengh = Math.round(((float) mobEffectInstance.getDuration() / currentMobEffectsMaxTime[indexOfEffectType(currentMobEffects, mobEffectInstance.getEffect().value())]) * 17);
        int barBackgroundBrightness = Math.toIntExact(Math.round(BetterEffectHudConfig.getInstance().barBackgroundBrightness * 255));
        int barBackgroundColor = ARGB.color(0xFF, barBackgroundBrightness, barBackgroundBrightness, barBackgroundBrightness);
        drawBar(graphics, posX + 3, posY + 21, 17, false, color & barBackgroundColor);
        drawBar(graphics, posX + 3, posY + 21, barLengh, false, color);
        graphics.pose().popMatrix();
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
        int maxCount = 0; for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) if (i.getEffect().value().isBeneficial() == isBeneficial && i.showIcon()) maxCount += 1;
        float guiDownscale = (float) BetterEffectHudConfig.getInstance().infoSize; float guiUpscale = 1 / guiDownscale;

        int posX = getPrimaryEffectPos(hudAlignmentX, count, maxCount, false, 1);
        int posY = getSecondaryEffectPos(hudAlignmentY, isBeneficial, true, 1);
        if (BetterEffectHudConfig.getInstance().hudStackY) {
            posX = getSecondaryEffectPos(hudAlignmentX, isBeneficial, false, 1);
            posY = getPrimaryEffectPos(hudAlignmentY, count, maxCount, true, 1);
        }
        if (
                Arrays.stream(BetterEffectHudConfig.getInstance().effectWarningIdentifiers).toList().contains(BuiltInRegistries.MOB_EFFECT.getKey(effect.value())) &&
                mobEffectInstance.getDuration() < BetterEffectHudConfig.getInstance().warningTime * 20
        ) {
            warningTargetColor = effect.value().getColor() & 0x00FFFFFF;
        }

        graphics.blitSprite(renderPipeline, location, posX + 3, posY + 3, width, height, color);
        graphics.pose().pushMatrix();
        graphics.pose().scale(guiDownscale, guiDownscale);
        BetterEffectHudAlignment timerPos = BetterEffectHudConfig.getInstance().timerPos;
        BetterEffectHudAlignment potencyPos = BetterEffectHudConfig.getInstance().potencyPos;

        BetterEffectHudLabel timerLabel = new BetterEffectHudLabel(Component.literal(getDuration(mobEffectInstance)), (int) ((posX + 3 + 9 * (timerPos.posX)) * guiUpscale), (int) ((posY + 3 + 10 * (timerPos.posY)) * guiUpscale), 0xFFFFFFFF, timerPos);
        BetterEffectHudLabel potencyLabel = new BetterEffectHudLabel(Component.literal(getPotency(mobEffectInstance)), (int) ((posX + 3 + 9 * (potencyPos.posX)) * guiUpscale), (int) ((posY + 3 + 10 * (potencyPos.posY)) * guiUpscale), 0xFFFFFFFF, potencyPos);
        if (BetterEffectHudConfig.getInstance().enableTimer) timerLabel.extractWidgetRenderState(graphics, 0, 0, 0);
        if (BetterEffectHudConfig.getInstance().enablePotency) potencyLabel.extractWidgetRenderState(graphics, 0, 0, 0);
        graphics.pose().popMatrix();
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getActiveEffects()Ljava/util/Collection;"), method = "extractEffects")
    private Collection<MobEffectInstance> setCurrentEffects(LocalPlayer instance, Operation<Collection<MobEffectInstance>> original) {
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

        warningColor = ARGB.linearLerp(0.1f, warningColor, warningTargetColor);
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

    private int getPrimaryEffectPos(int hudAlignment, int count, int maxCount, boolean isY, float guiUpscale) {
        float pos = 0;
        float windowSize = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        if (isY) {windowSize = Minecraft.getInstance().getWindow().getGuiScaledHeight();}

        if (hudAlignment == 0) {
            pos += 1;
            pos += 25 * guiUpscale * (count - 1);
        }
        if (hudAlignment == 1) {
            pos += windowSize / 2;
            pos -= 12.5f * maxCount;
            pos += 25 * guiUpscale * (count - 1);
        }
        if (hudAlignment == 2) {
            pos += windowSize;
            pos -= 25 * guiUpscale * count;
        }
        return Math.round(pos);
    }
    private int getSecondaryEffectPos(int hudAlignment, boolean isBeneficial, boolean isY, float guiUpscale) {
        int pos = 0;
        int windowSize = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        if (isY) {windowSize = Minecraft.getInstance().getWindow().getGuiScaledHeight();}
        if (hudAlignment == 0) {pos += 1;}

        pos += (int) (windowSize * ((float) hudAlignment / 2));
        pos -= 50 * (hudAlignment / 2);

        if (!isBeneficial) {pos += (int) (25 * guiUpscale);}
        return pos;
    }

    private static int indexOfEffectType(MobEffectInstance[] array, MobEffect type) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].getEffect().value() == type) return i;
        }
        return -1;
    }

    private void drawBar(GuiGraphicsExtractor graphics, int posX, int posY, int lengh, boolean vertical, int color) {
        int offsetX = 1;
        int offsetY = 1;
        if (vertical) {offsetY += lengh;} else {offsetX += lengh;}
        graphics.fill(posX, posY, posX + offsetX,posY + offsetY, color);
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

        String prefix = getPrefixZeros(time, unitConversions[suffixId]);
        String doublePrefix = getPrefixZeros(doubleTime, unitConversions[Math.clamp(suffixId - 1, 0, 6)]);

        int decimalCount = BetterEffectHudConfig.getInstance().timerDecimalCount;
        if (BetterEffectHudConfig.getInstance().timerUseDecimals[suffixId]) return String.format(String.format("%%.%sf%%s", decimalCount), time, suffix);
        if (suffixId != 0) if (BetterEffectHudConfig.getInstance().timerUseDouble[suffixId - 1]) return String.format("%s%.0f:%s%.0f", prefix, Math.floor(time), doublePrefix, Math.floor(doubleTime));
        return (int) Math.floor(time) + suffix;
    }
    private String getPotency(MobEffectInstance mobEffectInstance) {
        int potency = mobEffectInstance.getAmplifier();
        if (potency == 0 && BetterEffectHudConfig.getInstance().enableFirstPotency) return "I";
        return Component.translatableWithFallback("potion.potency." + potency, String.valueOf(potency)).getString();
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
}