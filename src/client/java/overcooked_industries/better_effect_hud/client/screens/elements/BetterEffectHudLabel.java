package overcooked_industries.better_effect_hud.client.screens.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;

public class BetterEffectHudLabel extends AbstractWidget {
    private int color;
    private final BetterEffectHudAlignment alignment;
    private final boolean dropshadow;

    public BetterEffectHudLabel(Component text, int posX, int posY, int color, BetterEffectHudAlignment alignment, boolean dropshadow) {
        super(posX, posY, Minecraft.getInstance().font.width(text), Minecraft.getInstance().font.lineHeight, text);
        this.color = color;
        this.alignment = alignment;
        this.dropshadow = dropshadow;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        graphics.drawString(Minecraft.getInstance().font, getMessage(), getAlignedPos(getX(), alignment.posX, getWidth()), getAlignedPos(getY(), alignment.posY, getHeight()), color, dropshadow);
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int getAlignedPos(int pos, int alignment, int offset) {
        return (int) ((float) pos - (((float) alignment / 2) * (float) offset));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {narrationElementOutput.add(NarratedElementType.HINT, getMessage());}
}