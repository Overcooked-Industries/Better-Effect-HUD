package overcooked_industries.better_effect_hud.client.screens;

import com.google.common.collect.ImmutableList;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.tabs.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.ffm.mapping.Mapping;
import overcooked_industries.better_effect_hud.BetterEffectHud;
import overcooked_industries.better_effect_hud.client.BetterEffectHudAlignment;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorModes;
import overcooked_industries.better_effect_hud.client.BetterEffectHudColorSetting;
import overcooked_industries.better_effect_hud.client.BetterEffectHudConfig;
import overcooked_industries.better_effect_hud.client.screens.elements.*;

import java.lang.invoke.VarHandle;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@NullMarked
public abstract class BetterEffectHudAbstractScreen extends Screen {
    public BetterEffectHudAbstractScreen() {
        super(Component.literal(""));
    }

    public static final BetterEffectHudAlignment[] betterEffectHudAlignments = new BetterEffectHudAlignment[] {
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

    public static final Map<String, String> colorsAsStrings = Map.ofEntries(
            Map.entry("BLACK"   , "FF000000"),
            Map.entry("BLUE"    , "FF0000FF"),
            Map.entry("GREEN"   , "FF00FF00"),
            Map.entry("CYAN"    , "FF00FFFF"),
            Map.entry("TEAL"    , "FF00FFFF"),
            Map.entry("AQUA"    , "FF00FFFF"),
            Map.entry("RED"     , "FFFF0000"),
            Map.entry("PINK"    , "FFFF00FF"),
            Map.entry("YELLOW"  , "FFFFFF00"),
            Map.entry("WHITE"   , "FFFFFFFF"),
            Map.entry("MAROON"  , "FF550000"),
            Map.entry("PURPLE"  , "FFA00AFF"),
            Map.entry("ORANGE"  , "FFFF5000"),
            Map.entry("GRAY"    , "FFAAAAAA"),
            Map.entry("GREY"    , "FFAAAAAA"),
            Map.entry("GOLD"    , "FFF0C000"),
            Map.entry("LAPIS"   , "FF1C53A8"),
            Map.entry("INDIGO"  , "FF3935AC"),

            //Easter Eggs
            Map.entry("PUKE"    , "FFA08000"),
            Map.entry("BACON"   , "FFF44336"),
            Map.entry("ROLEX"   , "FF1D3636"),
            Map.entry("0WINED"  , "FF2B2B2B"),
            Map.entry("DENIW0"  , "FFD4D4D4"),
            Map.entry("PING"    , "PONG"    ),
            Map.entry("LOREM"   , "IPSUM "  ),
            Map.entry("IPSUM"   , "LOREM "  ),
            Map.entry("CREEPER" , "AWW MAN!"),
            Map.entry("EASTER"  , "EGG"     ),
            Map.entry("D:"      , ":D"      ),
            Map.entry("):"      , "(:"      ),
            Map.entry(":("      , ":)"      ),
            Map.entry("]:"      , "[:"      ),
            Map.entry(":["      , ":]"      ),
            Map.entry("}:"      , "{:"      ),
            Map.entry(":{"      , ":}"      ),
            Map.entry("NEBULA"  , "INDIGO"  ),
            Map.entry("IDK"     , "RANDOM"  ),
            Map.entry("IDC"     , "RANDOM"  )
    );


    private static Button newButtonBuild(Component name, Button.OnPress onPress, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, Component tooltip) {
        return Button.builder(name, onPress)
                .bounds(
                        getAlignedPos(posX, sizeX, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX),
                        getAlignedPos(posY, sizeY, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY),
                        sizeX,
                        sizeY
                )
                .tooltip(Tooltip.create(tooltip))
                .build();
    }
    public static Button newButton(Component name, Button.OnPress onPress, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, boolean disabled) {
        Button button = newButtonBuild(name, onPress, posX, posY, sizeX, sizeY, alignment, Component.empty());
        button.active = !disabled;
        return button;
    }
    public static Button newButton(Component name, Button.OnPress onPress, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, boolean disabled, Component tooltip) {
        Button button = newButtonBuild(name, onPress, posX, posY, sizeX, sizeY, alignment, tooltip);
        button.active = !disabled;
        return button;
    }

    private static Button newToggleButtonBuild(Component name, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, VarHandle handle, Screen screen, boolean disabled, Component tooltip) {
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
        }, posX, posY, sizeX, sizeY, alignment, disabled, tooltip);
    }
    public static Button newToggleButton(Component name, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, VarHandle handle, Screen screen, boolean disabled) throws NoSuchFieldException, IllegalAccessException {
        return newToggleButtonBuild(name, posX, posY, sizeX, sizeY, alignment, handle, screen, disabled, Component.empty());
    }
    public static Button newToggleButton(Component name, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, VarHandle handle, Screen screen, boolean disabled, Component tooltip) {
        return newToggleButtonBuild(name, posX, posY, sizeX, sizeY, alignment, handle, screen, disabled, tooltip);
    }

    public static EditBox newEditBox(Component text, MutableComponent hint, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, int color, int maxLengh, Consumer<String> updateConsumer) {
        EditBox editBox = new EditBox(Minecraft.getInstance().font, getAlignedPos(posX, sizeX, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX), getAlignedPos(posY, sizeY, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY), sizeX, sizeY, null, Component.empty());
        editBox.setHint(hint.setStyle(EditBox.SEARCH_HINT_STYLE));
        editBox.setResponder(updateConsumer);
        editBox.setTextColor(color);
        editBox.setMaxLength(maxLengh);
        editBox.insertText(text.getString());
        return editBox;
    }

    public static BetterEffectHudSlider newSlider(Component title, int posX, int posY, int sizeX, int sizeY, VarHandle handle, double min, double max, BetterEffectHudAlignment alignment, BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction, Object configObject) throws NoSuchFieldException, IllegalAccessException {
        return new BetterEffectHudSlider(
                getAlignedPos(posX, sizeX, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX),
                getAlignedPos(posY, sizeY, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY),
                sizeX,
                sizeY,
                title.getString(),
                handle,
                min,
                max,
                updateFunction,
                configObject
        );
    }
    public static BetterEffectHudIntSlider newIntSlider(Component title, int posX, int posY, int sizeX, int sizeY, VarHandle handle, int min, int max, BetterEffectHudAlignment alignment, BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction, Object configObject) throws NoSuchFieldException, IllegalAccessException {
        return new BetterEffectHudIntSlider(
                getAlignedPos(posX, sizeX, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX),
                getAlignedPos(posY, sizeY, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY),
                sizeX,
                sizeY,
                title.getString(),
                handle,
                min,
                max,
                updateFunction,
                configObject
        );
    }
    public static BetterEffectHudColorSlider newColorSlider(int posX, int posY, BetterEffectHudAlignment alignment, Identifier backgroundSprite, VarHandle handle, BetterEffectHudAbstractScreen.BetterEffectHudCallable updateFunction, Object configObject) {
        return new BetterEffectHudColorSlider(
                getAlignedPos(posX, 7, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX),
                getAlignedPos(posY, 100, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY),
                backgroundSprite,
                handle,
                updateFunction,
                configObject
        );
    }

    private static Checkbox newCheckboxBuild(Component title, int posX, int posY, VarHandle handle, Screen screen, BetterEffectHudAlignment alignment, Component tooltip) {
        Boolean selected = (Boolean) handle.get(BetterEffectHudConfig.getInstance());
        return Checkbox.builder(title, Minecraft.getInstance().font)
                .tooltip(Tooltip.create(tooltip))
                .pos(getAlignedPos(posX, 17, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX), getAlignedPos(posY, 17, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY))
                .selected(selected)
                .onValueChange((checkbox, value) -> {
                    handle.set(BetterEffectHudConfig.getInstance(), (Boolean) !selected);
                    BetterEffectHudConfig.serialize();
                    Minecraft.getInstance().gui.setScreen(screen);
                })
                .build();
    }
    public static Checkbox newCheckbox(Component title, int posX, int posY, VarHandle handle, Screen screen, BetterEffectHudAlignment alignment) throws NoSuchFieldException, IllegalAccessException {
        return newCheckboxBuild(title, posX, posY, handle, screen, alignment, Component.empty());
    }
    public static Checkbox newCheckbox(Component title, int posX, int posY, VarHandle handle, Screen screen, BetterEffectHudAlignment alignment, Component tooltip) {
        return newCheckboxBuild(title, posX, posY, handle, screen, alignment, tooltip);
    }

    private static Checkbox newCustomCheckboxBuild(Component title, int posX, int posY, Checkbox.OnValueChange onToggle, boolean selected, BetterEffectHudAlignment alignment, Component tooltip) {
        return Checkbox.builder(title, Minecraft.getInstance().font)
                .tooltip(Tooltip.create(tooltip))
                .pos(getAlignedPos(posX, 17, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX), getAlignedPos(posY, 17, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY))
                .selected(selected)
                .onValueChange(onToggle)
                .build();
    }
    public static Checkbox newCustomCheckbox(Component title, int posX, int posY, Checkbox.OnValueChange onToggle, boolean selected, BetterEffectHudAlignment alignment) {
        return newCustomCheckboxBuild(title, posX, posY, onToggle, selected, alignment, Component.empty());
    }
    public static Checkbox newCustomCheckbox(Component title, int posX, int posY, Checkbox.OnValueChange onToggle, boolean selected, BetterEffectHudAlignment alignment, Component tooltip) {
        return newCustomCheckboxBuild(title, posX, posY, onToggle, selected, alignment, tooltip);
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

    public static BetterEffectHudLabel newLabel(Component title, int posX, int posY, int color, BetterEffectHudAlignment alignment, BetterEffectHudAlignment textAlignment, boolean dropshadow) {
        return new BetterEffectHudLabel(
                title,
                getAlignedPos(posX, 0, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX),
                getAlignedPos(posY, 0, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY),
                color,
                textAlignment,
                dropshadow
        );
    }
    public static BetterEffectHudTexture newTexture(int posX, int posY, int sizeX, int sizeY, int color, RenderPipeline renderPipeline, Identifier texture, BetterEffectHudAlignment alignment) {
        return new BetterEffectHudTexture(
                getAlignedPos(posX, sizeX, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX),
                getAlignedPos(posY, sizeY, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY),
                sizeX, sizeY, color, renderPipeline, texture
        );
    }

    public static List<AbstractWidget> newAlignmentButtons(int posX, int posY, int sizeX, int sizeY, VarHandle handle, Screen screen, Character color, BetterEffectHudAlignment alignment) throws NoSuchFieldException, IllegalAccessException {
        List<AbstractWidget> out = new ArrayList<>();
        BetterEffectHudAlignment hudAlignmentValue = (BetterEffectHudAlignment) handle.get(BetterEffectHudConfig.getInstance());

        for (BetterEffectHudAlignment align : BetterEffectHudConfigScreen.betterEffectHudAlignments) {
            String colorPrefix = "";
            if (hudAlignmentValue == align) {colorPrefix = "§" + color + "§l§n";}
            out.add(BetterEffectHudAbstractScreen.newButton(
                    Component.literal(
                            colorPrefix +
                                    Component.translatable("better_effect_hud.hud.alignment.short.v." + align.posY).getString() +
                                    Component.translatable("better_effect_hud.hud.alignment.short.h." + align.posX).getString()
                    ),
                    (btn) -> {
                        handle.set(BetterEffectHudConfig.getInstance(), (BetterEffectHudAlignment) align);
                        BetterEffectHudConfig.serialize();
                        Minecraft.getInstance().gui.setScreen(screen);
                    },
                    posX + align.posX * sizeX, posY + align.posY * sizeY, sizeX, sizeY,
                    alignment, hudAlignmentValue == align));
        }
        return out;
    }
    public static List<AbstractWidget> newColorEditBox(int posX, int posY, int sizeX, int sizeY, VarHandle handle, BetterEffectHudAlignment alignment, Screen screen) {
        List<AbstractWidget> out = new ArrayList<>();
        AtomicReference<@Nullable EditBox> editBoxRef = new AtomicReference<>();
        AtomicReference<@Nullable BetterEffectHudTexture> buttonOverlayRef = new AtomicReference<>();
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(BetterEffectHudConfig.getInstance());
        int value = colorSetting.getColor();
        EditBox editBox = BetterEffectHudAbstractScreen.newEditBox(
                Component.literal(addPrefixZeros(Integer.toHexString(value).toUpperCase(), 8)),
                Component.translatable("better_effect_hud.hud.category.general.toggle_button.enable_mod"),
                posX, posY, sizeX, sizeY,
                alignment,
                value,
                8,
                (string) -> {
                    EditBox editBoxRefObj = editBoxRef.get();
                    BetterEffectHudTexture buttonOverlayRefObj = buttonOverlayRef.get();
                    if (editBoxRefObj != null) {
                        string = string.toUpperCase();
                        if (string.length() == 8 && string.matches("[0-9A-F]+")) {
                            editBoxRefObj.setTextColor(Integer.parseUnsignedInt(string, 16));
                            buttonOverlayRefObj.setColor(Integer.parseUnsignedInt(string, 16) & 0x00FFFFFF + 0xAA000000);
                            colorSetting.setColor(Integer.parseUnsignedInt(string, 16));
                            handle.set(BetterEffectHudConfig.getInstance(), colorSetting);
                            BetterEffectHudConfig.serialize();
                        } else {
                            editBoxRefObj.setTextColor(0xFFCC1234);
                            buttonOverlayRefObj.setColor(0x00000000);
                        }
                        if (colorsAsStrings.containsKey(string)) editBoxRefObj.setValue(colorsAsStrings.get(string));
                        if (string.equals("RANDOM")) editBoxRefObj.setValue(String.format("FF%s%s%s", getRandomHexColor(), getRandomHexColor(), getRandomHexColor()));
                        if (string.equals("CRASH")) {
                            Minecraft.getInstance().stop();
                            throw new RuntimeException(Component.translatable("better_effect_hud.hud.easteregg.crash").getString());
                        }
                        if (!editBoxRefObj.getValue().equals(editBoxRefObj.getValue().toUpperCase())) editBoxRefObj.setValue(editBoxRefObj.getValue().toUpperCase());
                    }
                }
        );
        editBox.setSize(sizeX - sizeY, sizeY);
        BetterEffectHudTexture buttonOverlay = newTexture(posX + (sizeX - sizeY) / 2, posY, sizeY, sizeY, value & 0x00FFFFFF + 0xAA000000, RenderPipelines.GUI_TEXTURED, BetterEffectHud.id("textures/gui/color_button.png"), alignment);
        buttonOverlayRef.set(buttonOverlay);
        editBoxRef.set(editBox);
        out.add(newButton(Component.empty(), (btn) -> {
            try {
                Minecraft.getInstance().setScreenAndShow(new BetterEffectHudColorEditScreen(handle, screen));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, posX + (sizeX - sizeY) / 2, posY, sizeY, sizeY, alignment, false));
        out.add(editBox);
        out.add(buttonOverlay);
        return out;
    }

    public static BetterEffectHudGraph newGraph(int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, int cursorSizeX, int cursorSizeY, Identifier backgroundSprite, Identifier cursorSprite, VarHandle handleX, VarHandle handleY, BetterEffectHudCallable updateFunction, Object configObject) {
        return new BetterEffectHudGraph(
                getAlignedPos(posX, sizeX, Minecraft.getInstance().getWindow().getGuiScaledWidth(), alignment.posX),
                getAlignedPos(posY, sizeY, Minecraft.getInstance().getWindow().getGuiScaledHeight(), alignment.posY),
                sizeX, sizeY,
                cursorSizeX, cursorSizeY,
                Component.empty(),
                backgroundSprite, cursorSprite,
                handleX, handleY,
                updateFunction, configObject
        );
    }

    public static Button newColorModeButton(Component title, VarHandle handle, BetterEffectHudCallable updateFunction, Object configObject, Screen screen, int posX, int posY, int sizeX, int sizeY, BetterEffectHudAlignment alignment, boolean disabled) {
        BetterEffectHudColorSetting colorSetting = (BetterEffectHudColorSetting) handle.get(configObject);
        return BetterEffectHudAbstractScreen.newButton(
                Component.literal(String.format(
                        title.getString(),
                        Component.translatable(String.format(
                                "better_effect_hud.hud.color_mode.%s",
                                colorSetting.getColorMode().toString().toLowerCase()
                        )).getString()
                )),
                (btn) -> {
                    colorSetting.setColorMode(colorSetting.getColorMode().next());
                    handle.set(configObject, colorSetting);
                    updateFunction.call();
                    Minecraft.getInstance().setScreenAndShow(screen);
                },
                posX, posY, sizeX, sizeY, alignment, disabled
        );
    }

    static int getAlignedPos(int pos, int size, int windowSize, int alignment) {
        float alignMultiplier = (float) alignment / 2;

        pos -= (int) (size * alignMultiplier);
        pos += (int) (windowSize * alignMultiplier);
        return pos;
    }

    public static String getRandomHexColor() {
        return addPrefixZeros(Integer.toHexString(Math.round(Math.round(Math.random() * 256))), 2);
    }

    public static String addPrefixZeros(String text, int zeroCount) {
        int textLengh = text.length();
        return "0".repeat(zeroCount - textLengh) + text;
    }

    public interface BetterEffectHudCallable {
        void call();
    }
}