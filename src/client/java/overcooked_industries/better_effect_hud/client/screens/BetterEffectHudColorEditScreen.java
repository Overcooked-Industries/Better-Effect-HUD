package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.*;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class BetterEffectHudColorEditScreen extends BetterEffectHudAbstractScreen {
    VarHandle handle;
    Screen screen;
    Integer alpha;
    Integer red;
    Integer green;
    Integer blue;
    BetterEffectHudTexture colorBox;
    BetterEffectHudColorGraph colorGraph;
    BetterEffectHudColorSlider colorBarBlue;
    BetterEffectHudColorSlider colorBarAlpha;
    BetterEffectHudIntSlider[] sliders;

    BetterEffectHudLabel colorText;

    int gradientId;

    public void update() {
        int color = ARGB.color(alpha, red, green, blue);
        for (BetterEffectHudIntSlider slider : sliders) {slider.update();}
        colorGraph.setColor((color & 0x000000FF) | 0xFFFFFF00);
        colorGraph.update();
        colorBarBlue.update();
        colorBarAlpha.update();
        colorText.setColor((invertColor(color) & 0x00FFFFFF) | 0xFF000000);
        colorText.setMessage(getColorAsHex(color));
        colorBox.setColor(color);
        colorBarAlpha.setColor((color & 0x00FFFFFF) | 0xFF000000);
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(BetterEffectHudConfig.getInstance());
        if (gradientId == -1) colorSetting.setColor(color);
        else colorSetting.getColorGradient().get(gradientId).setColor(color);
        handle.set(BetterEffectHudConfig.getInstance(), colorSetting);
        BetterEffectHudConfig.serialize();
    }

    private static Component getColorAsHex(int color) {
        return Component.literal(addPrefixZeros(Integer.toHexString(color).toUpperCase(), 8));
    }

    public static VarHandle varHandle(String fieldName, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        return MethodHandles.lookup().findVarHandle(BetterEffectHudColorEditScreen.class, fieldName, type);
    }

    private static int invertColor(int color) {
        return ARGB.color(255 - ARGB.alpha(color), 255 - ARGB.red(color), 255 - ARGB.green(color), 255 - ARGB.blue(color));
    }

    public BetterEffectHudColorEditScreen(VarHandle handle, Screen screen, int gradientId) throws NoSuchFieldException, IllegalAccessException {
        super();
        this.gradientId = gradientId;
        this.handle = handle;
        this.screen = screen;
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(BetterEffectHudConfig.getInstance());
        int color = colorSetting.getColor();
        if (gradientId != -1) color = colorSetting.getColorGradient().get(gradientId).getColor();
        this.alpha = ARGB.alpha(color);
        this.red = ARGB.red(color);
        this.green = ARGB.green(color);
        this.blue = ARGB.blue(color);
        this.colorBox = newTexture(0, 135, 100, 16, 0, RenderPipelines.GUI_TEXTURED, BetterEffectHud.id("textures/gui/color_box.png"), BetterEffectHudAlignment.TOP_MIDDLE);
        this.colorBarBlue = newColorSlider(54, 34, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHud.id("textures/gui/blue_bar.png"), varHandle("blue", Integer.class), this::update, this);
        this.colorBarAlpha = newColorSlider(-55, 34, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHud.id("textures/gui/alpha_bar.png"), varHandle("alpha", Integer.class), this::update, this);

        this.colorText = newLabel(getColorAsHex(color), 0, 145, (invertColor(color) & 0x00FFFFFF) | 0xFF000000, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.MIDDLE_MIDDLE, false);
        this.sliders = new BetterEffectHudIntSlider[]{
                newIntSlider(Component.translatable("better_effect_hud.hud.color.red"), -105, 155, 200, 21, varHandle("red", Integer.class), 0, 255, BetterEffectHudAlignment.TOP_MIDDLE, this::update, this, Component.empty()),
                newIntSlider(Component.translatable("better_effect_hud.hud.color.green"), 105, 155, 200, 21, varHandle("green", Integer.class), 0, 255, BetterEffectHudAlignment.TOP_MIDDLE, this::update, this, Component.empty()),
                newIntSlider(Component.translatable("better_effect_hud.hud.color.blue"), -105, 186, 200, 21, varHandle("blue", Integer.class), 0, 255, BetterEffectHudAlignment.TOP_MIDDLE, this::update, this, Component.empty()),
                newIntSlider(Component.translatable("better_effect_hud.hud.color.alpha"), 105, 186, 200, 21, varHandle("alpha", Integer.class), 0, 255, BetterEffectHudAlignment.TOP_MIDDLE, this::update, this, Component.empty()),
        };
        this.colorGraph = newGraph(0, 34, 100, 100, BetterEffectHudAlignment.TOP_MIDDLE, 5, 5, BetterEffectHud.id("textures/gui/color_graph.png"), BetterEffectHud.id("textures/gui/color_cursor.png"), varHandle("red", Integer.class), varHandle("green", Integer.class), this::update, this);
        update();
    }

    protected void init() {
        addRenderableWidget(newLabel(Component.translatable("better_effect_hud.hud.color.title"), 0, 10, 0xFFFFFFFF, BetterEffectHudAlignment.TOP_MIDDLE, BetterEffectHudAlignment.TOP_MIDDLE, true));
        addRenderableWidget(newButton(0, -25, 200, 21, BetterEffectHudAlignment.BOTTOM_MIDDLE, Component.translatable("gui.done"), (btn) -> Minecraft.getInstance().setScreenAndShow(screen), false));
        for (BetterEffectHudIntSlider widget : sliders) {addRenderableWidget(widget);}
        addRenderableWidget(colorBox);
        addRenderableWidget(colorBarBlue);
        addRenderableWidget(colorBarAlpha);

        addRenderableWidget(colorText);
        addRenderableWidget(colorGraph);
    }
}