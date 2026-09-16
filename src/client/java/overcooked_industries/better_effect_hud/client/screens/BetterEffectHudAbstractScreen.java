package overcooked_industries.better_effect_hud.client.screens;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.tabs.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NullMarked;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudIntSlider;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudLabel;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudSlider;

import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.List;

@NullMarked
public abstract class BetterEffectHudAbstractScreen extends Screen {
    public BetterEffectHudAbstractScreen(Component title) {
        super(title);
    }

    public static BetterEffectHudAlignment[] betterEffectHudAlignments = new BetterEffectHudAlignment[] {
            BetterEffectHudAlignment.TOP_LEFT,
            BetterEffectHudAlignment.TOP_MIDDLE,
            BetterEffectHudAlignment.TOP_RIGHT,
            BetterEffectHudAlignment.MIDDLE_LEFT,
            BetterEffectHudAlignment.MIDDLE_MIDDLE,
            BetterEffectHudAlignment.MIDDLE_RIGHT,
            BetterEffectHudAlignment.BOTTOM_LEFT,
            BetterEffectHudAlignment.BOTTOM_MIDDLE,
            BetterEffectHudAlignment.BOTTOM_RIGHT,
    };

    public static Button newButton(Component name, Button.OnPress onPress, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, boolean disabled) {
        var window = Minecraft.getInstance().getWindow();

        int windowSizeX = window.getGuiScaledWidth();
        int windowSizeY = window.getGuiScaledHeight();

        int buttonX = posX;
        int buttonY = posY;
        int alignX = alignment.posX;
        int alignY = alignment.posY;

        //These casts are slightly more lossy than they could be
        float alignMultiplierX = (float) alignX / 2;
        float alignMultiplierY = (float) alignY / 2;

        buttonX -= (int) (sizeX * alignMultiplierX);
        buttonY -= (int) (sizeY * alignMultiplierY);
        buttonX += (int) (windowSizeX * alignMultiplierX);
        buttonY += (int) (windowSizeY * alignMultiplierY);

        Button button = Button.builder(name, onPress).bounds(buttonX, buttonY, sizeX, sizeY).build();
        button.active = !disabled;
        return button;
    }

    //It is the caller's responsibility to call this method correctly, hence we throw an exception if that contract is not fulfilled
    //If the field being accessed exists and is modifiable without much effort, this method is guaranteed to work correctly
    //From this context, we cannot do much, except delegate the responsibility of dealing with Exceptions to the caller (since providing faulty arguments is on the caller)
    public static Button newToggleButton(Component name, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, String field, Screen screen, boolean disabled) throws NoSuchFieldException, IllegalAccessException {
        VarHandle handle = BetterEffectHudConfig.varHandle(field, Boolean.class);

        boolean isOn = (boolean) handle.get(BetterEffectHudConfig.getInstance());

        var offComponent = Component.translatable("better_effect_hud.hud.button.toggle.off");
        var onComponent = Component.translatable("better_effect_hud.hud.button.toggle.on");

        Component stateComponent = isOn ? onComponent : offComponent;

        return BetterEffectHudAbstractScreen.newButton(Component.literal(name.getString() + stateComponent.getString()), (btn) -> {
            //Dirty VarHandle hacks
            //We would like to just mutate the state of a reference type, but we cannot mutate outer variables from inside a lambda body due to https://docs.oracle.com/javase/specs/jls/se10/html/jls-15.html#jls-15.27.2
            //We can get the instance of the singleton and toggle a value by direct assignment like BetterEffectHudConfig.getInstance().enableMod = true, but we unfortuantely cannot tell what field has been passed off Boolean
            //Another solution would be adding a toggle method to the class itself, which would operate on a massive switch statement to set the appropriate field based on a String
            //The toggle method wouldn't be able to outsource the "clean" solution without itself including dirty VarHandle hacks though
            handle.set(BetterEffectHudConfig.getInstance(), !(boolean) handle.get(BetterEffectHudConfig.getInstance()));

            BetterEffectHudConfig.serialize();
            Minecraft.getInstance().gui.setScreen(screen);
        }, posX, posY, sizeX, sizeY, alignment, disabled);
    }

    public static List<Button> newAlignmentButtons(int posX, int posY, int sizeX, int sizeY, String field, Screen screen, Character color) throws NoSuchFieldException, IllegalAccessException {
        List<Button> out = new ArrayList<>();


        VarHandle handle = BetterEffectHudConfig.varHandle(field, BetterEffectHudAlignment.class);
        BetterEffectHudAlignment hudAlignmentValue = (BetterEffectHudAlignment) handle.get(BetterEffectHudConfig.getInstance());

        for (BetterEffectHudAlignment alignment : BetterEffectHudConfigScreen.betterEffectHudAlignments) {
            String colorPrefix = "";
            if (hudAlignmentValue == alignment) {colorPrefix = "§" + color + "§l§n";}
            out.add(BetterEffectHudAbstractScreen.newButton(
                    Component.literal(
                            colorPrefix +
                                    Component.translatable("better_effect_hud.hud.alignment.short.v." + alignment.posY).getString() +
                                    Component.translatable("better_effect_hud.hud.alignment.short.h." + alignment.posX).getString()
                    ),
                    (btn) -> {
                        handle.set(BetterEffectHudConfig.getInstance(), (BetterEffectHudAlignment) alignment);
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().gui.setScreen(screen);
                    },
                    posX + alignment.posX * sizeX, posY + alignment.posY * sizeY, sizeX, sizeY,
                    BetterEffectHudAlignment.TOP_LEFT, hudAlignmentValue == alignment));
        }
        return out;
    }

    public static BetterEffectHudSlider newSlider(Component title, int posX, int posY, int sizeX, int sizeY, String field, double min, double max) throws NoSuchFieldException, IllegalAccessException {
        return new BetterEffectHudSlider(
                posX,
                posY,
                sizeX,
                sizeY,
                title.getString(),
                field,
                min,
                max
        );
    }

    public static BetterEffectHudIntSlider newIntSlider(Component title, int posX, int posY, int sizeX, int sizeY, String field, int min, int max) throws NoSuchFieldException, IllegalAccessException {
        return new BetterEffectHudIntSlider(
                posX,
                posY,
                sizeX,
                sizeY,
                title.getString(),
                field,
                min,
                max
        );
    }

    public static Checkbox newCheckbox(Component title, int posX, int posY, String field, Screen screen) throws NoSuchFieldException, IllegalAccessException {
        VarHandle handle = BetterEffectHudConfig.varHandle(field, Boolean.class);
        Boolean selected = (Boolean) handle.get(BetterEffectHudConfig.getInstance());

        return Checkbox.builder(title, Minecraft.getInstance().font).pos(posX, posY).selected(selected).onValueChange((checkbox, value) -> {handle.set(BetterEffectHudConfig.getInstance(), (Boolean) !selected); BetterEffectHudConfig.serialize(); Minecraft.getInstance().gui.setScreen(screen);}).build();
    }

    public static Checkbox newCustomCheckbox(Component title, int posX, int posY, Checkbox.OnValueChange onToggle, boolean selected) {
        return Checkbox.builder(title, Minecraft.getInstance().font).pos(posX, posY).selected(selected).onValueChange(onToggle).build();
    }

    public static TabNavigationBar newTabNavigationBar(BetterEffectHudAbstractScreen screen, @UnknownNullability List<Tab> tabs, int initialTab) {
        int tabCount = tabs.toArray().length;
        if (initialTab < 0 || initialTab >= tabCount) initialTab = 0;

        int windowSizeX = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        TabManager tabManager = new TabManager(screen::addRenderableWidget, screen::removeWidget);
        List<MenuTabBar.MenuTabButton> newMenuTabButtons = new ArrayList<>();
        for (Tab tab : tabs) {
            newMenuTabButtons.add(new MenuTabBar.MenuTabButton(tabManager, tab, windowSizeX / tabCount, 24));
        }
        TabNavigationBar newTabNavigationBar = new MenuTabBar(0, 0, windowSizeX, 24, tabManager, ImmutableList.copyOf(newMenuTabButtons), ImmutableList.copyOf(tabs));
        newTabNavigationBar.selectTab(initialTab, false);
        return newTabNavigationBar;
    }

    public static BetterEffectHudLabel newLabel(Component title, int posX, int posY, int color, boolean centered) {
        return new BetterEffectHudLabel(title, posX, posY, color, centered);
    }


    public void openScreen(BetterEffectHudAbstractScreen screen) {
        Minecraft.getInstance().setScreenAndShow(screen);
    }
}