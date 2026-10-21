package overcooked_industries.better_effect_hud.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import overcooked_industries.better_effect_hud.BetterEffectHud;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static void reset() {
        instance = new BetterEffectHudConfig();
        serialize();
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
                BetterEffectHud.LOGGER.error("Failed to open configuration file at {}!", PATH);
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
            BetterEffectHud.LOGGER.error("Failed to open configuration file at {}!", PATH);
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
    //Info
    public Boolean enableTimer = false;
    public Boolean enablePotency = false;
    public BetterEffectHudAlignment timerPos = BetterEffectHudAlignment.BOTTOM_MIDDLE;
    public BetterEffectHudAlignment potencyPos = BetterEffectHudAlignment.TOP_RIGHT;
    public BetterEffectHudColorSetting timerColor = new BetterEffectHudColorSetting(0xAAFFFFFF, 0.5f, BetterEffectHudColorModes.CUSTOM, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    public BetterEffectHudColorSetting potencyColor = new BetterEffectHudColorSetting(0xAAFFFFFF, 0.5f, BetterEffectHudColorModes.CUSTOM, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    public double infoSize = 1.0;
    public Boolean timerUseMs = false;
    public Integer timerMinUnit = 0;
    public Integer timerMaxUnit = 6;
    public Integer timerDecimalCount = 0;
    public Boolean[] timerUseDecimals = new Boolean[]{true, true, true, true, true, true, true};
    public Boolean[] timerUseDouble = new Boolean[]{false, false, false, false, false, false};
    public Boolean enableCustomTimerSize = false;
    public Boolean enableFirstPotency = false;
    //Duration Bar
    public Boolean enableDurationBar = false;
    public double barBackgroundOpacity = 0.5;
    public Integer barLocationId = 2;
    public Boolean barReverse = false;
    public Boolean barOut = false;
    public Boolean barCircle = false;
    public BetterEffectHudColorSetting barColor = new BetterEffectHudColorSetting(0xFFFFFFFF, 0.5f, BetterEffectHudColorModes.EFFECT_COLOR, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    //Warning
    public Boolean enableWarning = false;
    public Integer warningTime = 10;
    public Boolean useEffectListWarning = false;

    public Boolean enableVignette = false;
    public Integer vignetteTime = 10;
    public Integer vignetteTransparency = 100;

    public Identifier[] effectWarningIdentifiers = {Identifier.withDefaultNamespace("strength"), Identifier.withDefaultNamespace("speed"), Identifier.withDefaultNamespace("fire_resistance")};
    //Display
    public BetterEffectHudAlignment hudAlignment = BetterEffectHudAlignment.TOP_RIGHT;
    public BetterEffectHudAlignment extraHudAlignment = BetterEffectHudAlignment.TOP_RIGHT;
    public Boolean hudStackY = false;
    public Boolean extraHudStackY = false;
    public Boolean hudInvert = false;
    public Boolean extraHudInvert = false;
    public BetterEffectHudColorSetting backgroundColor = new BetterEffectHudColorSetting(0xFFFFFFFF, 0.5f, BetterEffectHudColorModes.CUSTOM, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    public BetterEffectHudColorSetting extraBackgroundColor = new BetterEffectHudColorSetting(0xFFFFFFFF, 0.5f, BetterEffectHudColorModes.CUSTOM, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    public Identifier[] extraEffectIdentifiers = {Identifier.withDefaultNamespace("strength"), Identifier.withDefaultNamespace("speed"), Identifier.withDefaultNamespace("fire_resistance")};
}