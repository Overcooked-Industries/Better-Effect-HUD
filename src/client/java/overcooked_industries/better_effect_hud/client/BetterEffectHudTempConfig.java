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

public class BetterEffectHudTempConfig {
    private static BetterEffectHudTempConfig instance = null;

    //Singleton
    public static BetterEffectHudTempConfig getInstance() {
        if (instance == null) instance = new BetterEffectHudTempConfig();
        return instance;
    }

    private BetterEffectHudTempConfig() {}

    public static Object getField(String fieldName, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        return varHandle(fieldName, type).get(getInstance());
    }

    public static VarHandle varHandle(String fieldName, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        return MethodHandles.lookup().findVarHandle(BetterEffectHudTempConfig.class, fieldName, type);
    }

    public Boolean unlockReset = false;
}