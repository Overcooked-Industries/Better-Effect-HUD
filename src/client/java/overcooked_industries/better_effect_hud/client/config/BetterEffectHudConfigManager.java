package overcooked_industries.better_effect_hud.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class BetterEffectHudConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("better_effect_hud.json");

    public static BetterEffectHudConfig config = deserialize();

    public static BetterEffectHudConfig deserialize() {
        // TODO: Better Exception Handling
        if (Files.exists(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH)) {
                return GSON.fromJson(reader, BetterEffectHudConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = BetterEffectHudConfig.getInstance();
        serialize();
        return config;
    }


    public static void serialize() {
        try (Writer writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}