package Helpers;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.http.Context;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class JteResponses {
    private static final TemplateEngine templateEngine = TemplateEngine.create(new DirectoryCodeResolver(Path.of("src/main/jte")), ContentType.Html);

    public static final class Builder {
        private final Context ctx;
        private Map<String, ?> params;
        private int status;

        private Builder(Context ctx) {
            this.ctx = ctx;
            this.params = Map.of();
            this.status = 200;
        }

        public static Builder of(Context ctx) {
            return new Builder(ctx);
        }

        public Builder params(Map<String, ?> params) {
            Objects.requireNonNull(params, "params cannot be null");
            this.params = params;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public void render(String path) {
            Objects.requireNonNull(path, "path cannot be null");
            ctx.status(status);
            ctx.render(path, params);
        }
    }

    public static Builder with(Context ctx) {
        return Builder.of(ctx);
    }

    public static String output(String tmpl, Map<String, ?> params) {
        var output = new StringOutput();
        templateEngine.render(tmpl, params, output);
        return output.toString();
    }

    public static String output(String tmpl) {
        var output = new StringOutput();
        templateEngine.render(tmpl, Map.of(), output);
        return output.toString();
    }

    private static void render(Context ctx, String filePath, Map<String, ?> params, int status) {
        ctx.status(status);
        ctx.render(filePath, params);
    }
}
