@import Data.Auth.Result.Success.User

@param User user

@template.layout.page(
    user = user,
    content = @`
        <div
            id="home-content"
            class="container relative min-w-screen"
        >
                <button class="button fab">
                    <a href="/lessons/create">+</a>
                </button>
        </div>
    `
)