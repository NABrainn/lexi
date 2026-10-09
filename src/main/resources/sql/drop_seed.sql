-- Removes all operational data from Lexi.
-- Preserves dictionary data in language_codes and does not drop any tables.
-- Transaction management is left to the SQL client.

DELETE FROM translations;
DELETE FROM lessons;
DELETE FROM settings;
DELETE FROM users;

-- Restart generated identifiers while preserving the language-code sequence.
DELETE FROM sqlite_sequence
WHERE name IN ('translations', 'lessons', 'settings', 'users');
