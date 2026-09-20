package overcooked_industries.better_effect_hud.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import overcooked_industries.better_effect_hud.BetterEffectHUD;
import overcooked_industries.better_effect_hud.client.screens.elements.BetterEffectHudAlignment;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BetterEffectHudConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("better_effect_hud.json");
    private static BetterEffectHudConfig instance = null;

    //Singleton
    public static BetterEffectHudConfig getInstance() {
        if (instance == null) instance = new BetterEffectHudConfig();
        return instance;
    }

    //Must be private.
    private BetterEffectHudConfig() {}

    /**
     * <p>Deserializes the config instance</p>
     * <strong>Old instance WILL be overridden!</strong>
     * <p>Here be dragons!</p>
     */
    public static void deserialize() {
        // TODO: Better Exception Handling
        if (Files.exists(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH)) {
                instance = GSON.fromJson(reader, BetterEffectHudConfig.class);
            } catch (IOException e) {
                BetterEffectHUD.LOGGER.error("Failed to open configuration file at {}!", PATH);
                e.printStackTrace();
            }
        } else {
            if(instance == null) new BetterEffectHudConfig();
            serialize();
        }
    }

    public static void serialize() {
        try (Writer writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            BetterEffectHUD.LOGGER.error("Failed to open configuration file at {}!", PATH);
            e.printStackTrace();
        }
    }

    //Anything below this comment may not use "instance"
    //Use getInstance() instead

    public static Object getField(String fieldName, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        return varHandle(fieldName, type).get(getInstance());
    }

    public static VarHandle varHandle(String fieldName, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        return MethodHandles.lookup().findVarHandle(BetterEffectHudConfig.class, fieldName, type);
    }

    /// Settings for each Tab.
    //General
    public Boolean enableMod = true;
    public Boolean enableExperimental = false;
    //Info
    public Boolean enableTimer = false;
    public Boolean enablePotency = false;
    public BetterEffectHudAlignment timerPos = BetterEffectHudAlignment.BOTTOM_MIDDLE;
    public BetterEffectHudAlignment potencyPos = BetterEffectHudAlignment.TOP_RIGHT;
    public int timerColor = 0xFFFFFFFF;
    public int potencyColor = 0xFFFFFFFF;
    public Boolean autoTimerColor = true;
    public Boolean autoPotencyColor = true;
    public double timerSize = 1.0f;
    public double potencySize = 1.0;
    public Boolean timerUseMs = false;
    public int timerMinUnit = 0;
    public int timerMaxUnit = 6;
    public Integer timerDecimalCount = 0;
    public Boolean[] timerUseDecimals = new Boolean[]{true, true, true, true, true, true, true};
    //Duration Bar
    public Boolean enableDurationBar = false;
    //Warning
    public Boolean enableWarning = false;
    public Identifier[] effectWarningIdentifiers = {Identifier.withDefaultNamespace("strength"), Identifier.withDefaultNamespace("speed")};
    //Position
    public BetterEffectHudAlignment hudAlignment = BetterEffectHudAlignment.TOP_RIGHT;
    public BetterEffectHudAlignment extraHudAlignment = BetterEffectHudAlignment.TOP_LEFT;
    public Boolean stackY = false;
    public Boolean extraStackY = false;
    public Double hudSize = 1.0;
    //Experimental
    public Boolean experimentalEnableCustomTimerSize = false;
}