package overcooked_industries.better_effect_hud.client.screens.elements;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BetterEffectHudTexture extends AbstractWidget {
    private final int color;
    private final RenderPipeline renderPipeline;
    private final Identifier texture;

    public BetterEffectHudTexture(int posX, int posY, int width, int height,  int color, RenderPipeline renderPipeline, Identifier texture) {
        super(posX, posY, width, height, Component.literal(""));
        this.color = color;
        this.renderPipeline = renderPipeline;
        this.texture = texture;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blitSprite(renderPipeline, texture, getX(), getY(), getWidth(), getHeight(), color);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {narrationElementOutput.add(NarratedElementType.HINT, getMessage());}
}