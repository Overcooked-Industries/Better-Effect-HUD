package overcooked_industries.better_effect_hud.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BetterEffectHudScreen extends Screen {
    public BetterEffectHudScreen(Component title) {
        super(title);
    }

    public static Button newButton(Component name, Button.OnPress onPress, int posX, int posY, int sizeX, int sizeY, int alignX, int alignY) {
        int windowGuiScale = Minecraft.getInstance().getWindow().getGuiScale();
        int windowSizeX = Minecraft.getInstance().getWindow().getWidth() / windowGuiScale;
        int windowSizeY = Minecraft.getInstance().getWindow().getHeight() / windowGuiScale;
        int buttonX = posX;
        int buttonY = posY;
        float alignMultiplierX = (float) alignX / 2;
        float alignMultiplierY = (float) alignY / 2;
        buttonX -= (int) (sizeX * alignMultiplierX);
        buttonY -= (int) (sizeY * alignMultiplierY);
        buttonX += (int) (windowSizeX * alignMultiplierX);
        buttonY += (int) (windowSizeY * alignMultiplierY);
        Button buttonWidget = Button.builder(name, onPress).bounds(buttonX, buttonY, sizeX, sizeY).build();
        return buttonWidget;
    }

    public TabNavigationBar tabNavigationBar(Tab[] tabs) {
        int count = tabs.length;
        int windowSizeX = Minecraft.getInstance().getWindow().getWidth() / Minecraft.getInstance().getWindow().getGuiScale();
        TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
        List<MenuTabBar.MenuTabButton> newMenuTabButtons = new ArrayList<>();
        for (Tab tab : tabs) {newMenuTabButtons.add(new MenuTabBar.MenuTabButton(tabManager, tab, windowSizeX / count, 24));}
        TabNavigationBar tabNavigationBar = new MenuTabBar(0, 0, windowSizeX, 24, tabManager, ImmutableList.copyOf(newMenuTabButtons), ImmutableList.copyOf(tabs));
        tabNavigationBar.selectTab(0, false);
        return tabNavigationBar;
    }
}
