package overcooked_industries.better_effect_hud.client;

import net.fabricmc.api.ClientModInitializer;

public class BetterEffectHudClient implements ClientModInitializer {
	public static int colorTime = 0;
	@Override
	public void onInitializeClient() {
		BetterEffectHudConfig.deserialize();
	}
	public static void incrementColorTime() {
		colorTime += 1;
	}
}