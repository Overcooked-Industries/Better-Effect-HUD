package overcooked_industries.better_effect_hud.client;

import net.fabricmc.api.ClientModInitializer;
import overcooked_industries.better_effect_hud.client.config.BetterEffectHudConfig;

public class BetterEffectHUDClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BetterEffectHudConfig.deserialize();
	}
}