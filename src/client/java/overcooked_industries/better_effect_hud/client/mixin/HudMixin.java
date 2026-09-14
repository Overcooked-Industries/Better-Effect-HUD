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
import org.lwjgl.system.ffm.mapping.Mapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfigManager;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.util.Collection;

@Mixin(Hud.class) //TODO: Make this Code more readable and just better.
public class HudMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"), method = "extractEffects")
    private void init(
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
        BetterEffectHudAlignment hudAlignment = BetterEffectHudConfigManager.config.hudAlignment;
        Holder<MobEffect> effect = mobEffectInstance.getEffect();
        int hudAlignmentX = hudAlignment.posX;
        int hudAlignmentY = hudAlignment.posY;
        boolean isBeneficial = effect.value().isBeneficial();
        int count = harmfulCount;
        int maxCount = 0;
        if (effect.value().isBeneficial()) {count = beneficialCount;}
        for (MobEffectInstance i : Ordering.natural().reverse().sortedCopy(activeEffects)) {
            if (i.getEffect().value().isBeneficial() == isBeneficial) {maxCount += 1;}
        }
        int[] pos = getEffectPosition(hudAlignmentX, hudAlignmentY, count, maxCount, isBeneficial);
        graphics.blitSprite(renderPipeline, location, pos[0], pos[1], 24, 24);
        graphics.text(minecraft.font, getDuration(mobEffectInstance), pos[0], pos[1], 0xFFFFFFFF);
        graphics.text(minecraft.font, getPotency(mobEffectInstance), pos[0], pos[1] + 16, 0xFFFFFFFF);
    }

    private int[] getEffectPosition(int hudAlignmentX, int hudAlignmentY, int count, int maxCount, boolean isBeneficial) {
        int posX = 0;
        int posY = 0;
        int windowSizeX = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int windowSizeY = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        if (hudAlignmentX == 0) {
            posX += 1;
            posX += 25 * (count - 1);
        }
        if (hudAlignmentX == 1) {
            posX += windowSizeX / 2;
            posX -= (int) (12.5 * maxCount);
            posX += 25 * (count - 1);
        }
        if (hudAlignmentX == 2) {
            posX += windowSizeX;
            posX -= 25 * count;
        }
        if (hudAlignmentY == 0) {posY += 1;}
        posY += (int) (windowSizeY * ((float) hudAlignmentY / 2));
        posY -= 50 * (hudAlignmentY / 2);

        if (!isBeneficial) {posY += 25;}

        return new int[]{posX, posY};
    }

    private String getDuration(MobEffectInstance mobEffectInstance) {
        int time = mobEffectInstance.getDuration();
        Character suffix = 't';

        //This is some bullsh*t code, please make a better alternative
        if (time >= 20) {
            time /= 20; suffix = 's';
            if (time >= 60) {
                time /= 60; suffix = 'm';
                if (time >= 60) {
                    time /= 60; suffix = 'h';
                    if (time >= 24) {
                        time /= 24; suffix = 'd';
                        if (time >= 7) {
                            time /= 7; suffix = 'w';
                        }
                    }
                }
            }
        }

        if (mobEffectInstance.isInfiniteDuration()) {return Component.translatable("effect.duration.infinite").getString();}
        return String.valueOf(time) + suffix;
    }
    private String getPotency(MobEffectInstance mobEffectInstance) {
        int potency = mobEffectInstance.getAmplifier();
        return Component.translatableWithFallback("potion.potency." + potency, String.valueOf(potency)).getString();
    }
}