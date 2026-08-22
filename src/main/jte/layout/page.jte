@import Data.Auth.Result.Value.User
@import gg.jte.Content
@import java.util.List

@param User user
@param List<String> scripts = null
@param List<String> styleSheets = null
@param Content content

<!doctype html>
<html lang="pl-PL">
    <head data-theme="light">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Lexi">
        <script src="/public/js/htmx.min.js"></script>
        <script src="/public/js/hyperscript.min.js"></script>
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
    <body class="container flex flex-row min-h-screen">
        @if(user != null)
            <?xml version="1.0" encoding="UTF-8"?>
            <svg style="display: none" xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="512" height="512">
                <symbol id="icon-home">
                    <path d="M22,5.724V2c0-.552-.447-1-1-1s-1,.448-1,1v2.366L14.797,.855c-1.699-1.146-3.895-1.146-5.594,0L2.203,5.579c-1.379,.931-2.203,2.48-2.203,4.145v9.276c0,2.757,2.243,5,5,5h2c.553,0,1-.448,1-1V14c0-.551,.448-1,1-1h6c.552,0,1,.449,1,1v9c0,.552,.447,1,1,1h2c2.757,0,5-2.243,5-5V9.724c0-1.581-.744-3.058-2-4Z"/>
                </symbol>
            </svg>
            <?xml version="1.0" encoding="UTF-8"?>
            <svg style="display: none" xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="512" height="512">
                <symbol id="icon-book">
                    <path d="M24,3V20.611l-12,3.429L0,20.611V4c0-.95,.435-1.823,1.194-2.395,.249-.188,.523-.316,.806-.418V19.103l10,2.857,10-2.857V.187c.283,.102,.558,.23,.806,.418,.759,.572,1.194,1.445,1.194,2.395Zm-11,.371v14.263l-1,.286-1-.286V3.371c0-1.332-.895-2.519-2.251-2.903L7.08,.069c-1.571-.375-3.08,.817-3.08,2.432v15.213l8,2.286,8-2.286V2.535c0-1.597-1.477-2.785-3.037-2.442l-1.788,.393c-1.281,.366-2.176,1.553-2.176,2.885Z"/>
                </symbol>
            </svg>
            <?xml version="1.0" encoding="UTF-8"?>
            <svg style="display: none" xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="512" height="512">
                <symbol id="icon-brain">
                    <path d="M15,9c0,.55-.45,1-1,1h-3.01l-.3,.4c-.29,.38-.72,.6-1.19,.6-.12,0-.26-.02-.43-.07l-.81-.24-.38,.76c-.17,.35-.51,.56-.89,.56-.55,0-1-.45-1-1,0-.38,.21-.72,.56-.89l.76-.38-.24-.81c-.05-.17-.07-.3-.07-.43,0-.77,.58-1.41,1.36-1.48l.52-.05,.26-.46c.18-.32,.5-.51,.86-.51,.38,0,.72,.21,.89,.56l.38,.76,.81-.24c.17-.05,.3-.07,.43-.07,.49,0,.96,.25,1.24,.66l.2,.29,.34,.1c.43,.13,.72,.51,.72,.95Zm9,3.79v2.21h-2.33l-.45,3.39c-.2,1.49-1.48,2.61-2.97,2.61h-2.25v3H3v-4.71c0-.46-.16-.91-.46-1.26C.67,15.78-.22,12.92,.05,9.98,.5,4.95,4.46,.79,9.45,.11c4.04-.55,7.91,1.06,10.35,4.31,1.18,1.46,2.29,4.01,3.47,6.71,.21,.49,.43,.98,.64,1.46l.09,.19Zm-7-3.79c0-1.22-.72-2.28-1.82-2.75-.72-.85-1.79-1.33-2.94-1.24-.56-.64-1.37-1.01-2.24-1.01s-1.75,.42-2.32,1.11c-1.56,.37-2.68,1.75-2.68,3.39,0,.09,0,.17,.01,.26-.64,.56-1.01,1.37-1.01,2.24,0,1.65,1.35,3,3,3,.35,0,.68-.08,1-.19v6.19h2v-7.03c.74-.1,1.41-.44,1.94-.97h2.06c1.65,0,3-1.35,3-3Z"/>
                </symbol>
            </svg>
            <?xml version="1.0" encoding="UTF-8"?>
            <svg style="display: none" xmlns="http://www.w3.org/2000/svg">
                <symbol id="icon-cog" viewBox="0 0 512 512">
                    <path
                        d="M34.283,384c17.646,30.626,56.779,41.148,87.405,23.502c0.021-0.012,0.041-0.024,0.062-0.036l9.493-5.483c17.92,15.332,38.518,27.222,60.757,35.072V448c0,35.346,28.654,64,64,64s64-28.654,64-64v-10.944c22.242-7.863,42.841-19.767,60.757-35.115l9.536,5.504c30.633,17.673,69.794,7.167,87.467-23.467c17.673-30.633,7.167-69.794-23.467-87.467l0,0l-9.472-5.461c4.264-23.201,4.264-46.985,0-70.187l9.472-5.461c30.633-17.673,41.14-56.833,23.467-87.467c-17.673-30.633-56.833-41.14-87.467-23.467l-9.493,5.483C362.862,94.638,342.25,82.77,320,74.944V64c0-35.346-28.654-64-64-64s-64,28.654-64,64v10.944c-22.242,7.863-42.841,19.767-60.757,35.115l-9.536-5.525C91.073,86.86,51.913,97.367,34.24,128s-7.167,69.794,23.467,87.467l0,0l9.472,5.461c-4.264,23.201-4.264,46.985,0,70.187l-9.472,5.461C27.158,314.296,16.686,353.38,34.283,384z M256,170.667c47.128,0,85.333,38.205,85.333,85.333S303.128,341.333,256,341.333S170.667,303.128,170.667,256S208.872,170.667,256,170.667z"
                    />
                </symbol>
            </svg>
            <svg style="display: none" id="Layer_1" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" data-name="Layer 1">
                <symbol id="icon-logout">
                    <path d="m15 6c0 3.309-2.691 6-6 6s-6-2.691-6-6 2.691-6 6-6 6 2.691 6 6zm2 16h-1c-.552 0-1-.449-1-1v-6c0-.551.448-1 1-1h1c.553 0 1-.448 1-1s-.447-1-1-1h-1c-1.654 0-3 1.346-3 3v6c0 1.654 1.346 3 3 3h1c.553 0 1-.448 1-1s-.447-1-1-1zm6.404-5.245-2.207-1.946c-.411-.363-1.046-.326-1.411.089-.365.414-.325 1.046.089 1.411l.783.691h-2.658c-.553 0-1 .448-1 1s.447 1 1 1h2.792l-.917.809c-.414.365-.454.997-.089 1.411.366.416.999.453 1.411.089l2.199-1.939c.384-.331.604-.809.604-1.311s-.22-.98-.596-1.304zm-12.404-1.755c0-.256.038-.503.076-.749-.668-.158-1.36-.251-2.076-.251-4.963 0-9 4.038-9 9 0 .552.447 1 1 1h11.026c-.635-.838-1.026-1.87-1.026-3z"/>
                </symbol>
            </svg>


            <section
                id="sidenav-section"
                class="container bg-surface-container-highest flex flex-col flex-1"
            >
                <nav class="nav">
                    <section class="primary-section">
                        <h2 class="heading">Lexi</h2>
                        <ul class="list">
                            <li class="list-item">
                                <a class="link" href="/">
                                    <svg class="icon" aria-hidden="true">
                                        <use href="#icon-home"></use>
                                    </svg>
                                    <span>Home</span>
                                </a>
                            </li>
                            <li>
                                <a class="link" href="/vocabulary">
                                    <svg class="icon" aria-hidden="true">
                                        <use href="#icon-book"></use>
                                    </svg>
                                    <span>Vocabulary</span>
                                </a>
                            </li>
                            <li>
                                <a class="link" href="/review">
                                    <svg class="icon" aria-hidden="true">
                                        <use href="#icon-brain"></use>
                                    </svg>
                                    <span>Review</span>
                                </a>
                            </li>
                            <li>
                                <a class="link" href="/settings">
                                    <svg class="icon" aria-hidden="true">
                                        <use href="#icon-cog"></use>
                                    </svg>
                                    <span>Settings</span>
                                </a>
                            </li>
                        </ul>
                    </section>
                    <section class="secondary-section">
                        <ul class="list">
                            <li class="list-item">
                                <a
                                    class="link"
                                    hx-get="/auth/logout"
                                >
                                    <svg class="icon" aria-hidden="true">
                                        <use href="#icon-logout"></use>
                                    </svg>
                                    <span>Log out</span>
                                </a>
                            </li>
                        </ul>
                    </section>
                </nav>
            </section>
        @endif
        <section
            id="content-section"
            class="container flex flex-col flex-4 items-stretch"
        >
            <main class="container flex-1">
                ${content}
            </main>
        </section>
    </body>
</html>
