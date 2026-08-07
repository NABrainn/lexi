package Helpers;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;

import java.nio.file.Path;
import java.util.Map;

public class Jte {
    private static final TemplateEngine templateEngine = TemplateEngine.create(new DirectoryCodeResolver(Path.of("src/main/jte")), ContentType.Html);

    public static String output(String tmpl, Map<String, Object> params) {
        var output = new StringOutput();
        templateEngine.render(tmpl, params, output);
        return output.toString();
    }

    public static String output(String tmpl) {
        var output = new StringOutput();
        templateEngine.render(tmpl, Map.of(), output);
        return output.toString();
    }
}
