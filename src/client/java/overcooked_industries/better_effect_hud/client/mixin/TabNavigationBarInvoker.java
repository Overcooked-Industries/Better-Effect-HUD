package overcooked_industries.better_effect_hud.client.mixin;

import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TabNavigationBar.class)
public class TabNavigationBarInvoker {
    @Invoker(value = "<init>")
    public static TabNavigationBar init(int i, TabManager tabManager, Iterable<Tab> iterable) { return null;}
}
