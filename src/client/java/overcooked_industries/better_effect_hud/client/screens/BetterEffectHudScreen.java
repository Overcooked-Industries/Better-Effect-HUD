package overcooked_industries.better_effect_hud.client.screens;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfigManager;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudSlider;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BetterEffectHudScreen extends Screen {
    public BetterEffectHudScreen(Component title) {
        super(title);
    }

    public static Button newButton(Component name, Button.OnPress onPress, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment) {
        int windowGuiScale = Minecraft.getInstance().getWindow().getGuiScale();
        int windowSizeX = Minecraft.getInstance().getWindow().getWidth() / windowGuiScale;
        int windowSizeY = Minecraft.getInstance().getWindow().getHeight() / windowGuiScale;
        int buttonX = posX;
        int buttonY = posY;
        int alignX = alignment.posX;
        int alignY = alignment.posY;
        float alignMultiplierX = (float) alignX / 2;
        float alignMultiplierY = (float) alignY / 2;
        buttonX -= (int) (sizeX * alignMultiplierX);
        buttonY -= (int) (sizeY * alignMultiplierY);
        buttonX += (int) (windowSizeX * alignMultiplierX);
        buttonY += (int) (windowSizeY * alignMultiplierY);
        Button buttonWidget = Button.builder(name, onPress).bounds(buttonX, buttonY, sizeX, sizeY).build();
        return buttonWidget;
    }

    public static Button newToggleButton(Component name, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, String field) {
        //Horrible hacks due to lambda
        VarHandle handle;
        try {
            handle = MethodHandles.lookup().findVarHandle(BetterEffectHudConfig.class, field, Boolean.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Component stateComponent = Component.translatable("better_effect_hud.button.toggle.off");

        if (((Boolean) handle.get(BetterEffectHudConfig.getInstance()))) {
            stateComponent = Component.translatable("better_effect_hud.button.toggle.on");
        }

        Button newButton = BetterEffectHudScreen.newButton(Component.literal(name.getString() + stateComponent), (btn) -> {
            //Dirty VarHandle hacks
            //We would like to just mutate the state of a reference type, but we cannot mutate outer variables from inside a lambda body due to https://docs.oracle.com/javase/specs/jls/se10/html/jls-15.html#jls-15.27.2
            //We can get the instance of the singleton and toggle a value by direct assignment like BetterEffectHudConfig.getInstance().enableMod = true, but we unfortuantely cannot tell what field has been passed off Boolean
            //Another solution would be adding a toggle method to the class itself, which would operate on a massive switch statement to set the appropriate field based on a String
            //The toggle method wouldn't be able to outsource the "clean" solution without itself including dirty VarHandle hacks though
            handle.set(BetterEffectHudConfig.getInstance(), !((Boolean) handle.get(BetterEffectHudConfig.getInstance())));

            BetterEffectHudConfigManager.serialize();
        }, posX, posY, sizeX, sizeY, alignment);
        return newButton;
    }

    public static BetterEffectHudSlider newSlider(Component title, int posX, int posY, int sizeX, int sizeY) {
        BetterEffectHudSlider newBetterEffectHudSlider = new BetterEffectHudSlider(
                posX,
                posY,
                sizeX,
                sizeY,
                title.getString(),
                1
        );
        return newBetterEffectHudSlider;
    }

    public void newLabel(GuiGraphicsExtractor graphics, Component title, int posX, int posY, int color) {
        graphics.text(minecraft.font, title.getString(), posX, posY, color);
    }

    public void newCenteredLabel(GuiGraphicsExtractor graphics, Component title, int posX, int posY, int color) {
        graphics.centeredText(minecraft.font, title.getString(), posX, posY, color);
    }

    public TabNavigationBar newTabNavigationBar(Tab[] tabs) {
        int count = tabs.length;
        int windowSizeX = Minecraft.getInstance().getWindow().getWidth() / Minecraft.getInstance().getWindow().getGuiScale();
        TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
        List<MenuTabBar.MenuTabButton> newMenuTabButtons = new ArrayList<>();
        for (Tab tab : tabs) {
            newMenuTabButtons.add(new MenuTabBar.MenuTabButton(tabManager, tab, windowSizeX / count, 24));
        }
        TabNavigationBar newTabNavigationBar = new MenuTabBar(0, 0, windowSizeX, 24, tabManager, ImmutableList.copyOf(newMenuTabButtons), ImmutableList.copyOf(tabs));
        newTabNavigationBar.selectTab(0, false);
        return newTabNavigationBar;
    }
}