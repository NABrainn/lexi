package Core.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.config.JavalinConfig;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class Json {
    public static void configure(JavalinConfig config) {
        Gson gson = new GsonBuilder().create();
        JsonMapper gsonMapper = new JsonMapper() {
            @NotNull
            @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return gson.toJson(obj, type);
            }

            @NotNull
            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return gson.fromJson(json, targetType);
            }
        };
        config.jsonMapper(gsonMapper);
    }
}
