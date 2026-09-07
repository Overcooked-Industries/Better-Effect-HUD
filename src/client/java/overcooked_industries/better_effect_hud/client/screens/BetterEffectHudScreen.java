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
        return Button.builder(name, onPress).bounds(buttonX, buttonY, sizeX, sizeY).build();
    }

    //It is the caller's responsibility to call this method correctly, hence we throw an exception if that contract is not fulfilled
    //If the field being accessed exists and is modifiable without much effort, this method is guaranteed to work correctly
    //From this context, we cannot do much, except delegate the responsibility of dealing with Exceptions to the caller (since providing faulty arguments is on the caller)
    public static Button newToggleButton(Component name, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, String field) throws NoSuchFieldException, IllegalAccessException {
        //Horrible hacks due to lambda
        var config = BetterEffectHudConfig.getInstance();
        VarHandle handle = MethodHandles.lookup().findVarHandle(BetterEffectHudConfig.class, field, Boolean.class);

        boolean isOn = (boolean) handle.get(config);

        var offComponent = Component.translatable("better_effect_hud.button.toggle.off");
        var onComponent = Component.translatable("better_effect_hud.button.toggle.on");

        Component stateComponent = isOn ? onComponent : offComponent;

        return BetterEffectHudScreen.newButton(Component.literal(name.getString() + stateComponent.getString()), (btn) -> {
            //Dirty VarHandle hacks
            //We would like to just mutate the state of a reference type, but we cannot mutate outer variables from inside a lambda body due to https://docs.oracle.com/javase/specs/jls/se10/html/jls-15.html#jls-15.27.2
            //We can get the instance of the singleton and toggle a value by direct assignment like BetterEffectHudConfig.getInstance().enableMod = true, but we unfortuantely cannot tell what field has been passed off Boolean
            //Another solution would be adding a toggle method to the class itself, which would operate on a massive switch statement to set the appropriate field based on a String
            //The toggle method wouldn't be able to outsource the "clean" solution without itself including dirty VarHandle hacks though
            handle.set(config, !(boolean) handle.get(config));

            BetterEffectHudConfigManager.serialize();
            Minecraft.getInstance().gui.setScreen(Minecraft.getInstance().gui.screen());
        }, posX, posY, sizeX, sizeY, alignment);
    }

    public static BetterEffectHudSlider newSlider(Component title, int posX, int posY, int sizeX, int sizeY) {
        return new BetterEffectHudSlider(
                posX,
                posY,
                sizeX,
                sizeY,
                title.getString(),
                1
        );
    }

    // TODO: Added screen reloading, but it doesnt work for tabpages of newTabNavigationBar. It just gets reset to the "page" property.
    public TabNavigationBar newTabNavigationBar(Tab[] tabs, int page) {
        int count = tabs.length;
        // TODO: Please add a better fix for this Exception.
        if (page >= count) {return null;}
        int windowSizeX = Minecraft.getInstance().getWindow().getWidth() / Minecraft.getInstance().getWindow().getGuiScale();
        TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
        List<MenuTabBar.MenuTabButton> newMenuTabButtons = new ArrayList<>();
        for (Tab tab : tabs) {
            newMenuTabButtons.add(new MenuTabBar.MenuTabButton(tabManager, tab, windowSizeX / count, 24));
        }
        TabNavigationBar newTabNavigationBar = new MenuTabBar(0, 0, windowSizeX, 24, tabManager, ImmutableList.copyOf(newMenuTabButtons), ImmutableList.copyOf(tabs));
        newTabNavigationBar.selectTab(page, false);
        return newTabNavigationBar;
    }

    public void newLabel(GuiGraphicsExtractor graphics, Component title, int posX, int posY, int color) {
        graphics.text(minecraft.font, title.getString(), posX, posY, color);
    }

    public void newCenteredLabel(GuiGraphicsExtractor graphics, Component title, int posX, int posY, int color) {
        graphics.centeredText(minecraft.font, title.getString(), posX, posY, color);
    }

    public void openScreen(BetterEffectHudScreen screen) {
        Minecraft.getInstance().setScreenAndShow(screen);
    }
}