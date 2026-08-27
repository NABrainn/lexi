package Configuration;

import io.javalin.config.StartupConfig;

public class Startup {
    public static void configure(StartupConfig config) {
        config.showJavalinBanner = false;
        config.showOldJavalinVersionWarning = false;
    }
}
