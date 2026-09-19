package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class BetterEffectHudLabel extends AbstractWidget {
    private final int color;
    private final BetterEffectHudAlignment alignment;

    public BetterEffectHudLabel(Component text, int posX, int posY, int color, BetterEffectHudAlignment alignment) {
        super(posX, posY, Minecraft.getInstance().font.width(text), Minecraft.getInstance().font.lineHeight, text);
        this.color = color;
        this.alignment = alignment;
    }

    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.text(Minecraft.getInstance().font, getMessage(), getAlignedPos(getX(), alignment.posX, getWidth()), getAlignedPos(getY(), alignment.posY, getHeight()), color);

    }

    private int getAlignedPos(int pos, int alignment, int offset) {
        return (int) ((float) pos - (((float) alignment / 2) * (float) offset));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {narrationElementOutput.add(NarratedElementType.HINT, getMessage());}
}