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
            if (instance == null) new BetterEffectHudConfig();
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
    public BetterEffectHudColorSetting timerColor = new BetterEffectHudColorSetting(0xAAFFFFFF, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    public BetterEffectHudColorSetting potencyColor = new BetterEffectHudColorSetting(0xAAFFFFFF, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    public double infoSize = 1.0;
    public Boolean timerUseMs = false;
    public Integer timerMinUnit = 0;
    public Integer timerMaxUnit = 6;
    public Integer timerDecimalCount = 0;
    public Boolean[] timerUseDecimals = new Boolean[]{true, true, true, true, true, true, true};
    public Boolean[] timerUseDouble = new Boolean[]{false, false, false, false, false, false};
    public Boolean enableFirstPotency = false;
    public Boolean enableNumberPotency = false;
    public Boolean enableInfoGap = true;
    //Duration Bar
    public Boolean enableDurationBar = false;
    public double barBackgroundOpacity = 0.5;
    public Integer barLocationId = 2;
    public Boolean barReverse = false;
    public Boolean barOut = false;
    public Boolean barCircle = false;
    public BetterEffectHudColorSetting barColor = new BetterEffectHudColorSetting(0xFFFFFFFF, 0.5f, BetterEffectHudColorMode.EFFECT_COLOR, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    //Warning
    public Boolean enableWarning = false;
    public Integer warningTime = 10;

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
    public BetterEffectHudColorSetting backgroundColor = new BetterEffectHudColorSetting(0xFFFFFFFF, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    public BetterEffectHudColorSetting extraBackgroundColor = new BetterEffectHudColorSetting(0xFFFFFFFF, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(List.of(new BetterEffectHudGradientElement(0xFFFFFFFF, 0.5f))));
    public Identifier[] extraEffectIdentifiers = {};
    public Boolean backgroundHide = false;
    public Boolean backgroundBlink = false;


    //Color
    public BetterEffectHudColorSetting durationColor = new BetterEffectHudColorSetting(
            0x00000000, 0.0f,
            BetterEffectHudColorMode.CUSTOM_GRADIENT,
            new ArrayList<>(List.of(
                    new BetterEffectHudGradientElement(0xFFC00000, 0.000f),
                    new BetterEffectHudGradientElement(0xFFF01111, 0.080f),
                    new BetterEffectHudGradientElement(0xFFF06010, 0.125f),
                    new BetterEffectHudGradientElement(0xFFFFC010, 0.250f),
                    new BetterEffectHudGradientElement(0xFFDDFF10, 0.375f),
                    new BetterEffectHudGradientElement(0xFF60B010, 0.500f),
                    new BetterEffectHudGradientElement(0xFF60B010, 1.000f)
            )),
            new ArrayList<>(List.of(BetterEffectHudColorMode.CUSTOM_GRADIENT))
    );

    private final ArrayList<BetterEffectHudColorMode> potencyAllowedModes = new ArrayList<>(List.of(BetterEffectHudColorMode.EFFECT_BENEFIT, BetterEffectHudColorMode.EFFECT_DURATION, BetterEffectHudColorMode.EFFECT_COLOR, BetterEffectHudColorMode.MONOCHROME, BetterEffectHudColorMode.RAINBOW, BetterEffectHudColorMode.CUSTOM_BLINK, BetterEffectHudColorMode.CUSTOM_GRADIENT, BetterEffectHudColorMode.CUSTOM));
    private final ArrayList<BetterEffectHudColorMode> isBeneficialAllowedModes = new ArrayList<>(List.of(BetterEffectHudColorMode.EFFECT_POTENCY, BetterEffectHudColorMode.EFFECT_DURATION, BetterEffectHudColorMode.EFFECT_COLOR, BetterEffectHudColorMode.MONOCHROME, BetterEffectHudColorMode.RAINBOW, BetterEffectHudColorMode.CUSTOM_BLINK, BetterEffectHudColorMode.CUSTOM_GRADIENT, BetterEffectHudColorMode.CUSTOM));
    public BetterEffectHudColorSetting potencyColor0 = new BetterEffectHudColorSetting(0xFF06A010, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(), potencyAllowedModes);
    public BetterEffectHudColorSetting potencyColor1 = new BetterEffectHudColorSetting(0xFF66C020, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(), potencyAllowedModes);
    public BetterEffectHudColorSetting potencyColor2 = new BetterEffectHudColorSetting(0xFFC0C020, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(), potencyAllowedModes);
    public BetterEffectHudColorSetting potencyColor3 = new BetterEffectHudColorSetting(0xFFC07020, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(), potencyAllowedModes);
    public BetterEffectHudColorSetting potencyColor4 = new BetterEffectHudColorSetting(0xFFD04020, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(), potencyAllowedModes);
    public BetterEffectHudColorSetting potencyColor5 = new BetterEffectHudColorSetting(0xFF2020A0, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(), potencyAllowedModes);

    public BetterEffectHudColorSetting beneficialColor = new BetterEffectHudColorSetting(0xFF3750F0, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(), isBeneficialAllowedModes);
    public BetterEffectHudColorSetting harmfulColor = new BetterEffectHudColorSetting(0xFFE02040, 0.5f, BetterEffectHudColorMode.CUSTOM, new ArrayList<>(), isBeneficialAllowedModes);
}