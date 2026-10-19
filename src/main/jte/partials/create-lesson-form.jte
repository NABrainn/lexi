<form class="padding" data-on:submit="@post('/lessons/create', { contentType: 'form' })">
    <fieldset data-signals:active-tab="'url'" class="surface-container-highest">
        <legend>
            <h4>Create lesson</h4>
        </legend>
        <nav class="row">
            <div class="field border label max">
                <input
                    type="text"
                    name="title"
                    id="title"
                >
                <label>Title</label>
            </div>
        </nav>
        <nav class="row">
            <div class="tabs max">
                <a
                    data-on:click="$activeTab='file'"
                    data-class="{
                        'active': $activeTab === 'file'
                    }"
                >
                    <i>attach_file</i>
                    <span>Import from file</span>
                </a>
                <a
                    data-on:click="$activeTab='url'"
                    data-class="{
                        'active': $activeTab === 'url'
                    }"
                >
                    <i>link</i>
                    <span>Import from URL</span>
                </a>
            </div>
        </nav>
        <nav class="row">
            <div
                style="display: none;"
                data-show="$activeTab === 'file'"
                data-class="{
                    'field border label max': true
                }"
            >
                <button class="max secondary">
                    <i>attach_file</i>
                    <span>Choose a file to import</span>
                </button>
                <input type="file">
            </div>
            <div
                style="display: none;"
                data-show="$activeTab === 'url'"
                data-class="{
                    'field border label max': true
                }"
            >
                <input
                    type="url"
                    name="url"
                    id="url"
                >
                <label>Type URL of a web page</label>
            </div>
        </nav>
        <hr class="large">

        <nav class="row">
            <button class="max" type="submit">Proceed</button>
        </nav>
    </fieldset>
</form>