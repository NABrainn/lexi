@import gg.jte.Content
@import java.util.List

@param List<String> scripts = null
@param List<String> styleSheets = null
@param Content content

<!doctype html>
<html lang="pl-PL">
    <head data-theme="light">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Lexi">
        <script src="/public/js/htmx.js"></script>
        <link rel="stylesheet" type="text/css" href="/public/css/style.css"/>
        @if(styleSheets != null)
            @for(var style : styleSheets)
                <link rel="stylesheet" type="text/css" href="/public/css/${style}">
            @endfor
        @endif
        @if(scripts != null)
            @for(var script : scripts)
                <script src="/public/js/${script}"></script>
            @endfor
        @endif
        <script>
            htmx.config.defaultSwap = "outerHTML";
            htmx.config.transitions = true;

        </script>
        <title>Lexi</title>
    </head>
    <body>
        <main id="layout">
            ${content}
        </main>
    </body>
</html>
