package Helpers;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.http.Context;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class Jte {
    private static final TemplateEngine templateEngine = TemplateEngine.create(new DirectoryCodeResolver(Path.of("src/main/jte")), ContentType.Html);

    public static final class Builder {
        private Context ctx;
        private String path;
        private Map<String, ?> params;
        private int status;

        private Builder(Context ctx) {
            this.ctx = ctx;
            this.path = null;
            this.params = Map.of();
            this.status = 200;
        }

        public static Builder of(Context ctx) {
            return new Builder(ctx);
        }

        public Builder path(String path) {
            Objects.requireNonNull(path, "path cannot be null");
            this.path = path;
            return this;
        }

        public Builder params(Map<String, ?> params) {
            Objects.requireNonNull(path, "params cannot be null");
            this.params = params;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public void render() {
            ctx.status(status);
            ctx.render(path, params);
        }
    }

    public static Builder ctx(Context ctx) {
        return Builder.of(ctx);
    }

    public static void render200(Context ctx, String filePath, Map<String, ?> params) {
        render(ctx, filePath, params, 200);
    }

    public static void render200(Context ctx, String filePath) {
        render(ctx, filePath, Map.of(), 200);
    }

    public static void render400(Context ctx, String filePath, Map<String, ?> params) {
        render(ctx, filePath, params, 400);
    }

    public static void render400(Context ctx, String filePath) {
        render(ctx, filePath, Map.of(), 400);
    }

    public static void render500(Context ctx, String filePath, Map<String, ?> params) {
        render(ctx, filePath, params, 500);
    }

    public static void render500(Context ctx, String filePath) {
        render(ctx, filePath, Map.of(), 500);
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
