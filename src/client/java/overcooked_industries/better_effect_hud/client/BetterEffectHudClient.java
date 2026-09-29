package overcooked_industries.better_effect_hud.client;

import net.fabricmc.api.ClientModInitializer;

public class BetterEffectHudClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BetterEffectHudConfig.deserialize();
	}
}