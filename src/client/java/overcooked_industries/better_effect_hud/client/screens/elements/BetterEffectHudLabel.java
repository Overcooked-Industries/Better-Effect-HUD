package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class BetterEffectHudLabel extends AbstractWidget {
    private final int color;
    private final boolean centered;

    public BetterEffectHudLabel(Component text, int posX, int posY, int color, boolean centered) {
        super(posX, posY, Minecraft.getInstance().font.width(text), Minecraft.getInstance().font.lineHeight, text);
        this.color = color;
        this.centered = centered;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (centered) {
            graphics.centeredText(Minecraft.getInstance().font, getMessage(), getX(), getY(), color);
        } else {
            graphics.text(Minecraft.getInstance().font, getMessage(), getX(), getY(), color);
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {narrationElementOutput.add(NarratedElementType.HINT, getMessage());}
}