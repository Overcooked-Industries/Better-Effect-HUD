package overcooked_industries.better_effect_hud.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BetterEffectHudScreen extends Screen {
    public BetterEffectHudScreen(Component title) {
        super(title);
    }

    public Button newButton(Component name, Button.OnPress onPress, int posX, int posY, int sizeX, int sizeY, int alignX, int alignY) {
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
        this.addRenderableWidget(buttonWidget);
        return buttonWidget;
    }

    public TabNavigationBar tabNavigationBar(Tab[] tabs) {
        TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
        MenuTabBar.Builder menuTabBarBuilder = MenuTabBar.builder(tabManager, Minecraft.getInstance().getWindow().getWidth() / Minecraft.getInstance().getWindow().getGuiScale());
        for (Tab tab : tabs) {menuTabBarBuilder.addTab(tab);}
        TabNavigationBar tabNavigationBar = menuTabBarBuilder.build();
        tabNavigationBar.selectTab(0, false);
        this.addRenderableWidget(tabNavigationBar);
        return tabNavigationBar;
    }

    public void openScreen(BetterEffectHudScreen screen) {
        Minecraft.getInstance().setScreenAndShow(screen);
    }
}
