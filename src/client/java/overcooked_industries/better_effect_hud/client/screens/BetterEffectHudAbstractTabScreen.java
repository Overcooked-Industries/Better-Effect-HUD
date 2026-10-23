package overcooked_industries.better_effect_hud.client.screens;

import net.minecraft.client.gui.screens.Screen;
import overcooked_industries.better_effect_hud.client.screens.config_tabs.BetterEffectHudAbstractTab;

import java.util.ArrayList;
import java.util.List;

public abstract class BetterEffectHudAbstractTabScreen extends BetterEffectHudAbstractScreen {
    int page;
    Screen parent;

    public BetterEffectHudAbstractTabScreen(int page, Screen parent) {
        super();
        this.page = page;
        this.parent = parent;
    }

    public static List<BetterEffectHudAbstractTab> newTabList(BetterEffectHudAbstractTabScreen screen, BetterEffectHudAbstractTab... tabs) throws NoSuchFieldException, IllegalAccessException {
        List<BetterEffectHudAbstractTab> list = new ArrayList<>();
        for (BetterEffectHudAbstractTab tab : tabs) {
            BetterEffectHudAbstractTabScreen tabScreen = screen.copy();
            tabScreen.page = list.toArray().length;
            tab.setScreen(tabScreen);
            tab.initialize();
            list.add(tab);
        }
        return list;
    }

    public abstract BetterEffectHudAbstractTabScreen copy();
}
