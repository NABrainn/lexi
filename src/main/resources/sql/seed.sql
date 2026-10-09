-- Natural development data for Lexi.
-- Creates 5 realistic users, 50 Norwegian lessons and genuine word/phrase translations.
-- Safe to run repeatedly. Transaction management is left to the SQL client.
-- Expected relationship:
--   translations(user, source_language, target_language)
--       -> settings(user, source_language, target_language)

CREATE TEMP TABLE seed_people (ordinal INTEGER PRIMARY KEY, login TEXT NOT NULL UNIQUE, display_name TEXT NOT NULL);

INSERT INTO seed_people (ordinal, login, display_name) VALUES
                                                           (1, 'anna.kowalska', 'Anna Kowalska'),
                                                           (2, 'michal.nowak', 'Michał Nowak'),
                                                           (3, 'katarzyna.wisniewska', 'Katarzyna Wiśniewska'),
                                                           (4, 'piotr.wojcik', 'Piotr Wójcik'),
                                                           (5, 'aleksandra.kaminska', 'Aleksandra Kamińska');

CREATE TEMP TABLE seed_existing_translations (
                                                 user TEXT NOT NULL,
                                                 source_text TEXT NOT NULL,
                                                 PRIMARY KEY (user, source_text)
) WITHOUT ROWID;

INSERT OR IGNORE INTO seed_existing_translations (user, source_text)
SELECT t.user, t.source_text FROM translations t JOIN seed_people p ON p.login = t.user;

INSERT OR IGNORE INTO language_codes (key, value) VALUES
                                                      ('en', 'English'), ('pl', 'Polish'), ('no', 'Norwegian');

INSERT OR IGNORE INTO users (login, password, created_at, updated_at)
SELECT
    p.login,
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    datetime('2025-01-02 08:00:00', printf('+%d hours', p.ordinal * 11)),
    datetime('2025-01-02 08:00:00', printf('+%d hours', p.ordinal * 11 + p.ordinal % 48))
FROM seed_people p;

INSERT INTO settings (user, ui_language, source_language, target_language, created_at, updated_at)
SELECT
    p.login,
    CASE WHEN p.ordinal % 2 = 0 THEN 'pl' ELSE 'en' END,
    'no',
    CASE WHEN p.ordinal % 2 = 0 THEN 'pl' ELSE 'en' END,
    datetime('2025-01-02 08:00:00', printf('+%d hours', p.ordinal * 11)),
    datetime('2025-01-02 08:00:00', printf('+%d hours', p.ordinal * 11 + p.ordinal % 48))
FROM seed_people p
WHERE NOT EXISTS (
    SELECT 1 FROM settings s
    WHERE s.user = p.login AND s.source_language = 'no'
      AND s.target_language = CASE WHEN p.ordinal % 2 = 0 THEN 'pl' ELSE 'en' END
);

CREATE TEMP TABLE seed_lesson_templates (id INTEGER PRIMARY KEY, title TEXT NOT NULL, content TEXT NOT NULL, image_url TEXT);

INSERT INTO seed_lesson_templates (id, title, content, image_url) VALUES
                                                                      (1, 'En rolig morgen i Oslo',
                                                                       'Solen står opp over Oslo, og gatene fylles langsomt med mennesker. Noen sykler til jobb, mens andre venter på trikken. På det lille bakeriet ved hjørnet lukter det ferskt brød, kaffe og kanel. Ingrid kjøper et rundstykke og setter seg ved vinduet før arbeidsdagen begynner.',
                                                                       'https://images.unsplash.com/photo-1520769669658-f07657f5a307'),
                                                                      (2, 'Togreisen til Trondheim',
                                                                       'Toget kjører gjennom grønne daler og over høye fjell. Fra vinduet ser Erik små gårder, klare innsjøer og snø som fortsatt ligger på toppene. Reisen tar flere timer, men han har med en god bok og niste. I spisevognen møter han en familie som skal besøke venner i Trondheim.',
                                                                       'https://images.unsplash.com/photo-1473445361085-b9a07f55608b'),
                                                                      (3, 'På kafé i Bergen',
                                                                       'Regnet faller stille over brosteinene i Bergen. Nora finner et ledig bord på en kafé nær Bryggen og bestiller kaffe og dagens suppe. Servitøren forteller at solen kanskje kommer fram senere. Rundt henne snakker folk lavt, leser aviser og ser ut på de fargerike husene.',
                                                                       'https://images.unsplash.com/photo-1554118811-1e0d58224f24'),
                                                                      (4, 'Nordlyset over Tromsø',
                                                                       'Det er en kald og klar kveld utenfor Tromsø. Gruppen går et stykke bort fra lysene i byen og venter ved et lite bål. Plutselig viser et grønt bånd seg på himmelen. Lyset beveger seg raskt mellom stjernene, og alle blir stille for å nyte synet.',
                                                                       'https://images.unsplash.com/photo-1483347756197-71ef80e95f73'),
                                                                      (5, 'En søndagstur i skogen',
                                                                       'Familien pakker termos, appelsiner og varme klær før de går ut. Stien følger en elv og fortsetter opp gjennom skogen. Barna leter etter kongler og spor etter dyr. Ved et utsiktspunkt tar de pause, drikker kakao og ser utover byen langt nede i dalen.',
                                                                       'https://images.unsplash.com/photo-1448375240586-882707db888b'),
                                                                      (6, 'Middag med gode venner',
                                                                       'Jonas inviterer fire venner på middag i den nye leiligheten. Han lager laks med poteter og grønnsaker, mens vennene dekker bordet. De snakker om ferieplaner, arbeid og en konsert de nylig var på. Etter maten blir de sittende lenge med kaffe og eplekake.',
                                                                       'https://images.unsplash.com/photo-1515003197210-e0cd71810b5f'),
                                                                      (7, 'Første dag på universitetet',
                                                                       'Sara kommer tidlig til universitetet fordi hun ikke vet hvor auditoriet ligger. En student viser henne veien og forteller om biblioteket og kantinen. Etter forelesningen møter hun studiegruppen sin. De utveksler telefonnumre og avtaler å lese sammen neste uke.',
                                                                       'https://images.unsplash.com/photo-1562774053-701939374585'),
                                                                      (8, 'Markedet ved havnen',
                                                                       'Hver lørdag er det marked ved havnen. Lokale bønder selger ost, grønnsaker, honning og nybakt brød. Ved siden av står en fisker med dagens fangst. Mari smaker på jordbær, kjøper en pose epler og slår av en prat med kvinnen som lager syltetøy.',
                                                                       'https://images.unsplash.com/photo-1488459716781-31db52582fe9'),
                                                                      (9, 'Været skifter fort',
                                                                       'Om morgenen skinner solen, men før lunsj kommer mørke skyer inn fra havet. Det begynner å blåse, og snart regner det kraftig. Henrik finner fram regnjakken fra sekken. En time senere er himmelen blå igjen, og gatene tørker overraskende fort.',
                                                                       'https://images.unsplash.com/photo-1501691223387-dd0500403074'),
                                                                      (10, 'Hytta ved innsjøen',
                                                                       'Hytta ligger stille mellom skogen og en liten innsjø. Det finnes ikke strøm der, så om kvelden tenner familien stearinlys og fyrer i ovnen. De lager enkel mat, spiller kort og hører vinden utenfor. Om morgenen tar de båten ut for å fiske.',
                                                                       'https://images.unsplash.com/photo-1449158743715-0a90ebb6d2d8');

INSERT INTO lessons (img_url, title, content, content_url, created_by, created_at, updated_at)
SELECT
    t.image_url, t.title, CAST(t.content AS BLOB), NULL, p.login,
    datetime('2025-02-01 09:00:00', printf('+%d hours', p.ordinal * 13 + t.id * 5)),
    datetime('2025-02-01 09:00:00', printf('+%d hours', p.ordinal * 13 + t.id * 5 + (p.ordinal + t.id) % 24))
FROM seed_people p CROSS JOIN seed_lesson_templates t
WHERE NOT EXISTS (SELECT 1 FROM lessons l WHERE l.created_by = p.login AND l.title = t.title);

CREATE TEMP TABLE seed_vocabulary (
                                      id INTEGER PRIMARY KEY,
                                      translation_kind TEXT NOT NULL DEFAULT 'word' CHECK (translation_kind IN ('word', 'phrase')),
                                      norwegian TEXT NOT NULL,
                                      english TEXT NOT NULL,
                                      polish TEXT NOT NULL
);

INSERT INTO seed_vocabulary (id, norwegian, english, polish) VALUES
                                                                 (1, 'hus', 'house', 'dom'),
                                                                 (2, 'bok', 'book', 'książka'),
                                                                 (3, 'vann', 'water', 'woda'),
                                                                 (4, 'mat', 'food', 'jedzenie'),
                                                                 (5, 'venn', 'friend', 'przyjaciel'),
                                                                 (6, 'arbeid', 'work', 'praca'),
                                                                 (7, 'reise', 'journey', 'podróż'),
                                                                 (8, 'språk', 'language', 'język'),
                                                                 (9, 'by', 'city', 'miasto'),
                                                                 (10, 'fjell', 'mountain', 'góra'),
                                                                 (11, 'sjø', 'sea', 'morze'),
                                                                 (12, 'skog', 'forest', 'las'),
                                                                 (13, 'morgen', 'morning', 'poranek'),
                                                                 (14, 'kveld', 'evening', 'wieczór'),
                                                                 (15, 'familie', 'family', 'rodzina'),
                                                                 (16, 'skole', 'school', 'szkoła'),
                                                                 (17, 'spørsmål', 'question', 'pytanie'),
                                                                 (18, 'svar', 'answer', 'odpowiedź'),
                                                                 (19, 'butikk', 'shop', 'sklep'),
                                                                 (20, 'bil', 'car', 'samochód'),
                                                                 (21, 'tog', 'train', 'pociąg'),
                                                                 (22, 'sykkel', 'bicycle', 'rower'),
                                                                 (23, 'hund', 'dog', 'pies'),
                                                                 (24, 'katt', 'cat', 'kot'),
                                                                 (25, 'kaffe', 'coffee', 'kawa'),
                                                                 (26, 'brød', 'bread', 'chleb'),
                                                                 (27, 'rom', 'room', 'pokój'),
                                                                 (28, 'dør', 'door', 'drzwi'),
                                                                 (29, 'vindu', 'window', 'okno'),
                                                                 (30, 'bord', 'table', 'stół'),
                                                                 (31, 'stol', 'chair', 'krzesło'),
                                                                 (32, 'telefon', 'telephone', 'telefon'),
                                                                 (33, 'dag', 'day', 'dzień'),
                                                                 (34, 'uke', 'week', 'tydzień'),
                                                                 (35, 'måned', 'month', 'miesiąc'),
                                                                 (36, 'år', 'year', 'rok'),
                                                                 (37, 'sol', 'sun', 'słońce'),
                                                                 (38, 'regn', 'rain', 'deszcz'),
                                                                 (39, 'snø', 'snow', 'śnieg'),
                                                                 (40, 'vind', 'wind', 'wiatr'),
                                                                 (41, 'lege', 'doctor', 'lekarz'),
                                                                 (42, 'lærer', 'teacher', 'nauczyciel'),
                                                                 (43, 'barn', 'child', 'dziecko'),
                                                                 (44, 'nabo', 'neighbour', 'sąsiad'),
                                                                 (45, 'musikk', 'music', 'muzyka'),
                                                                 (46, 'film', 'film', 'film'),
                                                                 (47, 'bilde', 'picture', 'obraz'),
                                                                 (48, 'historie', 'story', 'historia'),
                                                                 (49, 'frokost', 'breakfast', 'śniadanie'),
                                                                 (50, 'middag', 'dinner', 'obiad'),
                                                                 (51, 'kjøkken', 'kitchen', 'kuchnia'),
                                                                 (52, 'bad', 'bathroom', 'łazienka'),
                                                                 (53, 'seng', 'bed', 'łóżko'),
                                                                 (54, 'klær', 'clothes', 'ubrania'),
                                                                 (55, 'sko', 'shoes', 'buty'),
                                                                 (56, 'jakke', 'jacket', 'kurtka'),
                                                                 (57, 'vei', 'road', 'droga'),
                                                                 (58, 'gate', 'street', 'ulica'),
                                                                 (59, 'plass', 'place', 'miejsce'),
                                                                 (60, 'land', 'country', 'kraj'),
                                                                 (61, 'jobb', 'job', 'praca'),
                                                                 (62, 'penger', 'money', 'pieniądze'),
                                                                 (63, 'pris', 'price', 'cena'),
                                                                 (64, 'tid', 'time', 'czas'),
                                                                 (65, 'hjelp', 'help', 'pomoc'),
                                                                 (66, 'navn', 'name', 'imię'),
                                                                 (67, 'nummer', 'number', 'numer'),
                                                                 (68, 'adresse', 'address', 'adres'),
                                                                 (69, 'ferie', 'holiday', 'wakacje'),
                                                                 (70, 'flyplass', 'airport', 'lotnisko'),
                                                                 (71, 'billett', 'ticket', 'bilet'),
                                                                 (72, 'bagasje', 'luggage', 'bagaż'),
                                                                 (73, 'kart', 'map', 'mapa'),
                                                                 (74, 'hotell', 'hotel', 'hotel'),
                                                                 (75, 'nøkkel', 'key', 'klucz'),
                                                                 (76, 'å være', 'to be', 'być'),
                                                                 (77, 'å ha', 'to have', 'mieć'),
                                                                 (78, 'å gjøre', 'to do', 'robić'),
                                                                 (79, 'å gå', 'to go', 'iść'),
                                                                 (80, 'å komme', 'to come', 'przychodzić'),
                                                                 (81, 'å se', 'to see', 'widzieć'),
                                                                 (82, 'å høre', 'to hear', 'słyszeć'),
                                                                 (83, 'å si', 'to say', 'mówić'),
                                                                 (84, 'å snakke', 'to speak', 'rozmawiać'),
                                                                 (85, 'å spørre', 'to ask', 'pytać'),
                                                                 (86, 'å svare', 'to answer', 'odpowiadać'),
                                                                 (87, 'å vite', 'to know', 'wiedzieć'),
                                                                 (88, 'å forstå', 'to understand', 'rozumieć'),
                                                                 (89, 'å lære', 'to learn', 'uczyć się'),
                                                                 (90, 'å lese', 'to read', 'czytać'),
                                                                 (91, 'å skrive', 'to write', 'pisać'),
                                                                 (92, 'å spise', 'to eat', 'jeść'),
                                                                 (93, 'å drikke', 'to drink', 'pić'),
                                                                 (94, 'å sove', 'to sleep', 'spać'),
                                                                 (95, 'å bo', 'to live', 'mieszkać'),
                                                                 (96, 'å kjøpe', 'to buy', 'kupować'),
                                                                 (97, 'å betale', 'to pay', 'płacić'),
                                                                 (98, 'å vente', 'to wait', 'czekać'),
                                                                 (99, 'å finne', 'to find', 'znajdować'),
                                                                 (100, 'å bruke', 'to use', 'używać'),
                                                                 (101, 'stor', 'big', 'duży'),
                                                                 (102, 'liten', 'small', 'mały'),
                                                                 (103, 'god', 'good', 'dobry'),
                                                                 (104, 'dårlig', 'bad', 'zły'),
                                                                 (105, 'ny', 'new', 'nowy'),
                                                                 (106, 'gammel', 'old', 'stary'),
                                                                 (107, 'varm', 'warm', 'ciepły'),
                                                                 (108, 'kald', 'cold', 'zimny'),
                                                                 (109, 'glad', 'happy', 'szczęśliwy'),
                                                                 (110, 'trist', 'sad', 'smutny'),
                                                                 (111, 'lett', 'easy', 'łatwy'),
                                                                 (112, 'vanskelig', 'difficult', 'trudny'),
                                                                 (113, 'rask', 'fast', 'szybki'),
                                                                 (114, 'langsom', 'slow', 'powolny'),
                                                                 (115, 'tidlig', 'early', 'wcześnie'),
                                                                 (116, 'sent', 'late', 'późno'),
                                                                 (117, 'alltid', 'always', 'zawsze'),
                                                                 (118, 'aldri', 'never', 'nigdy'),
                                                                 (119, 'ofte', 'often', 'często'),
                                                                 (120, 'sjelden', 'rarely', 'rzadko');

INSERT INTO seed_vocabulary (id, translation_kind, norwegian, english, polish) VALUES
                                                                                   (121, 'phrase', 'Jeg har bodd her i tre år.', 'I have lived here for three years.', 'Mieszkam tutaj od trzech lat.'),
                                                                                   (122, 'phrase', 'Kan du snakke litt saktere?', 'Could you speak a little more slowly?', 'Czy możesz mówić trochę wolniej?'),
                                                                                   (123, 'phrase', 'Jeg forstår ikke alle ordene.', 'I do not understand all the words.', 'Nie rozumiem wszystkich słów.'),
                                                                                   (124, 'phrase', 'Det tar tjue minutter til sentrum.', 'It takes twenty minutes to reach the centre.', 'Droga do centrum zajmuje dwadzieścia minut.'),
                                                                                   (125, 'phrase', 'Vi møtes utenfor togstasjonen etter jobb.', 'We will meet outside the train station after work.', 'Spotkamy się przed dworcem po pracy.'),
                                                                                   (126, 'phrase', 'Jeg glemte paraplyen hjemme.', 'I forgot the umbrella at home.', 'Zapomniałem parasola w domu.'),
                                                                                   (127, 'phrase', 'Hun lærer norsk hver dag.', 'She learns Norwegian every day.', 'Ona codziennie uczy się norweskiego.'),
                                                                                   (128, 'phrase', 'Vil du bli med på kino?', 'Would you like to go to the cinema?', 'Czy chcesz pójść do kina?'),
                                                                                   (129, 'phrase', 'Jeg må levere rapporten før fredag.', 'I have to submit the report before Friday.', 'Muszę oddać raport przed piątkiem.'),
                                                                                   (130, 'phrase', 'Bussen var forsinket på grunn av været.', 'The bus was delayed because of the weather.', 'Autobus był opóźniony z powodu pogody.'),
                                                                                   (131, 'phrase', 'Det er viktig å øve hver dag.', 'It is important to practise every day.', 'Ważne jest, aby ćwiczyć codziennie.'),
                                                                                   (132, 'phrase', 'Jeg vet ikke hva jeg skal gjøre.', 'I do not know what I should do.', 'Nie wiem, co powinienem zrobić.'),
                                                                                   (133, 'phrase', 'Kan jeg få regningen?', 'Could I have the bill?', 'Czy mogę prosić o rachunek?'),
                                                                                   (134, 'phrase', 'Vi bestilte et rom med utsikt.', 'We booked a room with a view.', 'Zarezerwowaliśmy pokój z widokiem.'),
                                                                                   (135, 'phrase', 'Hun spurte om veien til apoteket.', 'She asked for directions to the pharmacy.', 'Zapytała o drogę do apteki.'),
                                                                                   (136, 'phrase', 'Jeg drikker kaffe før jobb.', 'I drink coffee before work.', 'Piję kawę przed pracą.'),
                                                                                   (137, 'phrase', 'Barna lekte ute i regnet.', 'The children played outside in the rain.', 'Dzieci bawiły się na dworze w deszczu.'),
                                                                                   (138, 'phrase', 'Vi må handle før butikkene stenger.', 'We must shop before the shops close.', 'Musimy zrobić zakupy przed zamknięciem sklepów.'),
                                                                                   (139, 'phrase', 'Jeg har aldri sett nordlyset.', 'I have never seen the northern lights.', 'Nigdy nie widziałem zorzy polarnej.'),
                                                                                   (140, 'phrase', 'Møtet ble flyttet til mandag.', 'The meeting was moved to Monday.', 'Spotkanie przeniesiono na poniedziałek.'),
                                                                                   (141, 'phrase', 'Det ligger en kafé over gaten.', 'There is a café across the street.', 'Po drugiej stronie ulicy jest kawiarnia.'),
                                                                                   (142, 'phrase', 'Jeg ringer deg når jeg kommer hjem.', 'I will call you when I get home.', 'Zadzwonię, kiedy wrócę do domu.'),
                                                                                   (143, 'phrase', 'Hun finner ikke nøklene sine.', 'She cannot find her keys.', 'Ona nie może znaleźć swoich kluczy.'),
                                                                                   (144, 'phrase', 'Toget var billigere enn flyet.', 'The train was cheaper than the plane.', 'Pociąg był tańszy od samolotu.'),
                                                                                   (145, 'phrase', 'Har du vært på fjelltur?', 'Have you been hiking in the mountains?', 'Czy byłeś na górskiej wędrówce?'),
                                                                                   (146, 'phrase', 'Jeg trenger mer tid til å tenke.', 'I need more time to think.', 'Potrzebuję więcej czasu do namysłu.'),
                                                                                   (147, 'phrase', 'Det var hyggelig å møte familien din.', 'It was nice to meet your family.', 'Miło było poznać twoją rodzinę.'),
                                                                                   (148, 'phrase', 'Kan du vise meg hvordan den fungerer?', 'Can you show me how it works?', 'Czy możesz pokazać mi, jak to działa?'),
                                                                                   (149, 'phrase', 'Vi har ikke valgt reisemål ennå.', 'We have not chosen a destination yet.', 'Nie wybraliśmy jeszcze celu podróży.'),
                                                                                   (150, 'phrase', 'Naboens hund vekket meg tidlig.', 'The neighbour’s dog woke me up early.', 'Pies sąsiada obudził mnie wcześnie.'),
                                                                                   (151, 'phrase', 'Hun jobber hjemmefra to dager ukentlig.', 'She works from home two days a week.', 'Pracuje z domu dwa dni w tygodniu.'),
                                                                                   (152, 'phrase', 'Det er vanskelig å konsentrere seg.', 'It is difficult to concentrate.', 'Trudno się skoncentrować.'),
                                                                                   (153, 'phrase', 'Jeg vil bestille et bord for fire.', 'I would like to book a table for four.', 'Chciałbym zarezerwować stolik dla czterech osób.'),
                                                                                   (154, 'phrase', 'Takk for hjelpen med flyttingen.', 'Thank you for helping with the move.', 'Dziękuję za pomoc przy przeprowadzce.'),
                                                                                   (155, 'phrase', 'Husk å slå av lyset.', 'Remember to turn off the light.', 'Pamiętaj, żeby zgasić światło.'),
                                                                                   (156, 'phrase', 'Hun sa at hun blir forsinket.', 'She said that she will be late.', 'Powiedziała, że się spóźni.'),
                                                                                   (157, 'phrase', 'Jeg gleder meg til å se deg.', 'I am looking forward to seeing you.', 'Nie mogę się doczekać spotkania z tobą.'),
                                                                                   (158, 'phrase', 'Det finnes mange turstier i området.', 'There are many hiking trails in the area.', 'W okolicy jest wiele szlaków turystycznych.'),
                                                                                   (159, 'phrase', 'Kan vi snakke om dette i morgen?', 'Can we talk about this tomorrow?', 'Czy możemy porozmawiać o tym jutro?'),
                                                                                   (160, 'phrase', 'Jeg tok feil buss hjem.', 'I took the wrong bus home.', 'Wsiadłem do niewłaściwego autobusu do domu.'),
                                                                                   (161, 'phrase', 'Hun leser og maler på fritiden.', 'She reads and paints in her free time.', 'W wolnym czasie czyta i maluje.'),
                                                                                   (162, 'phrase', 'Vi spiste middag ute på terrassen.', 'We ate dinner outside on the terrace.', 'Zjedliśmy obiad na tarasie.'),
                                                                                   (163, 'phrase', 'Telefonen min var tom for strøm.', 'My phone battery was dead.', 'Mój telefon był rozładowany.'),
                                                                                   (164, 'phrase', 'Det blir sannsynligvis sol i ettermiddag.', 'It will probably be sunny this afternoon.', 'Po południu prawdopodobnie będzie słonecznie.'),
                                                                                   (165, 'phrase', 'Hun lærte oppskriften av bestemoren sin.', 'She learned the recipe from her grandmother.', 'Nauczyła się przepisu od swojej babci.'),
                                                                                   (166, 'phrase', 'Jeg prøver å bruke nye ord.', 'I try to use new words.', 'Staram się używać nowych słów.'),
                                                                                   (167, 'phrase', 'Vi fant en koselig restaurant i nærheten.', 'We found a cosy restaurant nearby.', 'Znaleźliśmy w pobliżu przytulną restaurację.'),
                                                                                   (168, 'phrase', 'Kan du sende meg adressen?', 'Can you send me the address?', 'Czy możesz wysłać mi adres?'),
                                                                                   (169, 'phrase', 'Prøven var vanskeligere enn forventet.', 'The test was more difficult than expected.', 'Test był trudniejszy, niż się spodziewałem.'),
                                                                                   (170, 'phrase', 'Selv om vi var slitne, fortsatte vi.', 'Even though we were tired, we continued.', 'Mimo że byliśmy zmęczeni, szliśmy dalej.');

INSERT INTO translations (
    user, translation_kind, source_text, target_text, source_language,
    target_language, familiarity, created_at, updated_at
)
SELECT
    people.login,
    words.translation_kind,
    words.norwegian,
    CAST(json_array(CASE
                        WHEN people.ordinal % 2 = 0 THEN words.polish
                        ELSE words.english
        END) AS BLOB),
    'no',
    CASE WHEN people.ordinal % 2 = 0 THEN 'pl' ELSE 'en' END,
    ((people.ordinal + words.id) % 5) + 1,
    datetime('2025-03-01 10:00:00', printf('+%d minutes', people.ordinal * 29 + words.id * 7)),
    datetime('2025-03-01 10:00:00', printf('+%d minutes', people.ordinal * 29 + words.id * 9))
FROM seed_people people
         CROSS JOIN seed_vocabulary words
WHERE NOT EXISTS (
    SELECT 1 FROM seed_existing_translations existing
    WHERE existing.user = people.login
      AND existing.source_text = words.norwegian
);

DROP TABLE seed_vocabulary;
DROP TABLE seed_lesson_templates;
DROP TABLE seed_existing_translations;
DROP TABLE seed_people;
