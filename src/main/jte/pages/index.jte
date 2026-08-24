@import Data.Auth.Result.Value.User

@param User user

@template.layout.page(
    user = user,
    content = @`
        <div
            id="home-content"
            class="container relative min-w-screen"
        >
            <button
                class="button fab"
                hx-get="/lessons/create"
                hx-target="#home-content"
                hx-push-url="true"
            >+</button>
        </div>
    `
)