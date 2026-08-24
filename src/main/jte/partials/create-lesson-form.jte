<form
    class="container flex flex-col  w-3/5 mx-auto my-8 p-5"
    hx-post="/lessons/create"
>
    <fieldset class="container flex flex-col gap-4">
        <legend class="size-3"><h2>Create lesson</h2></legend>
        <div class="container flex flex-col gap-2">
            <label for="title">Title</label>
            <input
                type="text"
                name="title"
                id="title"
                class="input primary"
            >
        </div>
        <div class="actions">
            <button class="button" type="submit">Submit</button>
        </div>
    </fieldset>
</form>
