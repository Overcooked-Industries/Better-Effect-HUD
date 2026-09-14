package overcooked_industries.better_effect_hud.client.screens;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfigManager;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudSlider;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NullMarked
public abstract class BetterEffectHudScreen extends Screen {
    public BetterEffectHudScreen(Component title) {
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

    public static Button newButton(Component name, Button.OnPress onPress, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment) {
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

        return Button.builder(name, onPress).bounds(buttonX, buttonY, sizeX, sizeY).build();
    }

    //It is the caller's responsibility to call this method correctly, hence we throw an exception if that contract is not fulfilled
    //If the field being accessed exists and is modifiable without much effort, this method is guaranteed to work correctly
    //From this context, we cannot do much, except delegate the responsibility of dealing with Exceptions to the caller (since providing faulty arguments is on the caller)
    public static Button newToggleButton(Component name, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, String field, Screen screen) throws NoSuchFieldException, IllegalAccessException {
        //Horrible hacks due to lambda
        var config = BetterEffectHudConfig.getInstance();
        VarHandle handle = MethodHandles.lookup().findVarHandle(BetterEffectHudConfig.class, field, Boolean.class);

        boolean isOn = (boolean) handle.get(config);

        var offComponent = Component.translatable("better_effect_hud.hud.button.toggle.off");
        var onComponent = Component.translatable("better_effect_hud.hud.button.toggle.on");

        Component stateComponent = isOn ? onComponent : offComponent;

        return BetterEffectHudScreen.newButton(Component.literal(name.getString() + stateComponent.getString()), (btn) -> {
            //Dirty VarHandle hacks
            //We would like to just mutate the state of a reference type, but we cannot mutate outer variables from inside a lambda body due to https://docs.oracle.com/javase/specs/jls/se10/html/jls-15.html#jls-15.27.2
            //We can get the instance of the singleton and toggle a value by direct assignment like BetterEffectHudConfig.getInstance().enableMod = true, but we unfortuantely cannot tell what field has been passed off Boolean
            //Another solution would be adding a toggle method to the class itself, which would operate on a massive switch statement to set the appropriate field based on a String
            //The toggle method wouldn't be able to outsource the "clean" solution without itself including dirty VarHandle hacks though
            handle.set(config, !(boolean) handle.get(config));

            BetterEffectHudConfigManager.serialize();
            Minecraft.getInstance().gui.setScreen(screen);
        }, posX, posY, sizeX, sizeY, alignment);
    }

    public static List<Button> newAlignmentButtons(int posX, int posY, int sizeX, int sizeY, String field, Screen screen, Character color) throws NoSuchFieldException, IllegalAccessException {
        List<Button> out = new ArrayList<>();


        VarHandle handle = MethodHandles.lookup().findVarHandle(BetterEffectHudConfig.class, field, BetterEffectHudAlignment.class);
        BetterEffectHudAlignment hudAlignmentValue = (BetterEffectHudAlignment) handle.get(BetterEffectHudConfigManager.config);

        for (BetterEffectHudAlignment alignment : BetterEffectHudConfigScreen.betterEffectHudAlignments) {
            String colorPrefix = "";
            if (hudAlignmentValue == alignment) {colorPrefix = "§" + color + "§l§n";}
            out.add(BetterEffectHudScreen.newButton(
                    Component.literal(
                            colorPrefix +
                                    Component.translatable("better_effect_hud.hud.alignment.short.v." + alignment.posY).getString() +
                                    Component.translatable("better_effect_hud.hud.alignment.short.h." + alignment.posX).getString()
                    ),
                    (btn) -> {
                        handle.set(BetterEffectHudConfigManager.config, (BetterEffectHudAlignment) alignment);
                        BetterEffectHudConfigManager.serialize();
                        Minecraft.getInstance().gui.setScreen(screen);
                    },
                    posX + alignment.posX * sizeX, posY + alignment.posY * sizeY, sizeX, sizeY,
                    BetterEffectHudAlignment.TOP_LEFT));
        }
        return out;
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

    public @Nullable TabNavigationBar newTabNavigationBar(Tab[] tabs, int initialTab) {
        int tabCount = tabs.length;
        // TODO: Please add a better fix for this Exception.

        // Resolving the to-do entry involves deeply considering all approaches! The comments may be removed upon fix!
        /*
        Possible solutions:
        Default to the zero index?
        Change method signature to include a record argument that is validated before calling?
        We could also remove this validation entirely, since newTabNavigationBar.selectTab(initialTab, false) probably throws an ArrayIndexOutOfBoundsException anyway since accessing an illegal index automatically throws this Exception
        We could also throw a new IndexOutOfBoundsException(),
        BUT both of these approaches may be undesirable, as ArrayIndexOutOfBoundsException extends RuntimeException, which means it is allways unchecked
        Another option may be to change the return type to be Optional

        A really solid approach may be to mark this method as private, and create a second method that actually calls addRenderableWidget() if (and only if the return value is not null)
        along the lines of
        private @Nullable TabNavigationBar newTabNavigationBar(Tab[] tabs, int initialTab) {
            someLogic...
            if (initialTab < 0 || initialTab >= tabCount) return null;
            someLogic...
            return new MenuTabBar(...);
        }

        public boolean addTabNavigationBar(Tab[] tabs, int initialTab) {
            if (initialTab < 0 || initialTab >= tabCount) return false;
            addRenderableWidget(newTabNavigationBar(Tab[] tabs, int initialTab));
            return true;
        }

         */

        // RETURNING NULL IS ABSOLUTELY NOT AN OPTION AS IT WILL CAUSE A NULL POINTER EXCEPTION
        if (initialTab < 0 || initialTab >= tabCount) return null;

        int windowSizeX = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
        List<MenuTabBar.MenuTabButton> newMenuTabButtons = new ArrayList<>();
        for (Tab tab : tabs) {
            newMenuTabButtons.add(new MenuTabBar.MenuTabButton(tabManager, tab, windowSizeX / tabCount, 24));
        }
        TabNavigationBar newTabNavigationBar = new MenuTabBar(0, 0, windowSizeX, 24, tabManager, ImmutableList.copyOf(newMenuTabButtons), ImmutableList.copyOf(tabs));
        newTabNavigationBar.selectTab(initialTab, false);
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