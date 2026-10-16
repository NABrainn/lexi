@import Data.Auth.Result.Success.User
@import gg.jte.Content
@import java.util.List
@import java.util.function.Function

@param User user
@param List<String> scripts = null
@param List<String> styleSheets = null
@param Content content

<!doctype html>
<html lang="pl-PL">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Lexi">
        <link href="/public/css/beer.min.css" rel="stylesheet">
        <link href="/public/css/style.css" rel="stylesheet">
        <script type="module" src="/public/js/datastar-rocket.js"></script>
        <script type="module" src="/public/js/beer.min.js"></script>
        <script type="module" src="/public/js/material-dynamic-colors.min.js"></script>
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
        <title>Lexi</title>
    </head>
    <body class="dark surface-container">
        @if(user != null)
            <nav class="max left primary-container">
                !{Function<String, String> isActive = (pathName) -> user.pathName().equals(pathName) ? "active" : "";}
                <a href="/" class="${isActive.apply("/")}">
                    <i>home</i>
                    <span>Lessons</span>
                </a>
                <a href="/vocabulary" class="${isActive.apply("/vocabulary")}">
                    <i>book</i>
                    <span>Vocabulary</span>
                </a>
                <a href="/settings" class="${isActive.apply("/settings")}">
                    <i>settings</i>
                    <span>Settings</span>
                </a>
                <a href="/lessons/create" class="link bold ${isActive.apply("/lessons/create")}">
                    <i>wand_stars</i>
                    <span>Create lesson</span>
                </a>
            </nav>
        @endif
        <main class="responsive">
            ${content}
        </main>
    </body>
</html>
