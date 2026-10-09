CREATE TABLE users (
    id INTEGER NOT NULL UNIQUE,
    login TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT current_timestamp,
    updated_at TEXT NOT NULL DEFAULT current_timestamp,
    PRIMARY KEY (id AUTOINCREMENT)
);

CREATE TABLE language_codes (
    id INTEGER NOT NULL,
    key TEXT NOT NULL UNIQUE,
    value TEXT NOT NULL,
    PRIMARY KEY (id AUTOINCREMENT)
);

CREATE TABLE lessons (
    id INTEGER NOT NULL,
    img_url TEXT,
    title TEXT NOT NULL,
    content BLOB NOT NULL,
    content_url TEXT,
    created_by TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT current_timestamp,
    updated_at TEXT NOT NULL DEFAULT current_timestamp,
    PRIMARY KEY (id AUTOINCREMENT),
    FOREIGN KEY (created_by) REFERENCES users(login)
);

CREATE TABLE settings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user TEXT NOT NULL,
    ui_language TEXT NOT NULL DEFAULT 'en',
    source_language TEXT NOT NULL DEFAULT 'no',
    target_language TEXT NOT NULL DEFAULT 'en',
    created_at TEXT NOT NULL DEFAULT current_timestamp,
    updated_at TEXT NOT NULL DEFAULT current_timestamp,

    UNIQUE (user, source_language, target_language),
    CHECK (target_language != source_language),
    FOREIGN KEY (user) REFERENCES users(login),
    FOREIGN KEY (ui_language) REFERENCES language_codes(key),
    FOREIGN KEY (source_language) REFERENCES language_codes(key),
    FOREIGN KEY (target_language) REFERENCES language_codes(key)
);

CREATE TABLE translations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user TEXT NOT NULL,
    translation_kind TEXT NOT NULL
        CHECK (translation_kind IN ('word', 'phrase')),
    source_text TEXT NOT NULL,
    target_text BLOB NOT NULL,
    source_language TEXT NOT NULL,
    target_language TEXT NOT NULL,
    familiarity INTEGER NOT NULL DEFAULT 1
        CHECK (familiarity IN (1, 2, 3, 4, 5)),
    created_at TEXT NOT NULL DEFAULT current_timestamp,
    updated_at TEXT NOT NULL DEFAULT current_timestamp,

    CHECK (source_language != target_language),
    FOREIGN KEY (user) REFERENCES users(login),
    FOREIGN KEY (source_language) REFERENCES language_codes(key),
    FOREIGN KEY (target_language) REFERENCES language_codes(key)
);
