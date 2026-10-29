package overcooked_industries.better_effect_hud.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class BetterEffectHudClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BetterEffectHudConfig.deserialize();
	}
}