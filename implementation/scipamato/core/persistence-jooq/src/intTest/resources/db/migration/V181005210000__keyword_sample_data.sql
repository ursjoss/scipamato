INSERT INTO keyword (id, search_override)
VALUES (1, NULL),
       (2, 'Aktivität'),
       (3, NULL);
SELECT setval(pg_get_serial_sequence('keyword', 'id'), coalesce(max(id), 0)+1 , false) FROM keyword;


INSERT INTO keyword_tr (keyword_id, lang_code, name)
VALUES (1, 'de', 'Aerosol'),
       (1, 'en', 'Aerosol'),
       (1, 'fr', 'Aérosol'),
       (2, 'de', 'Aktivität, eingeschränkte'),
       (2, 'en', 'Restricted activity'),
       (2, 'fr', 'Activités réduites'),
       (3, 'de', 'Allergie (not Atopie)'),
       (3, 'en', 'Allergies'),
       (3, 'fr', 'Allérgie');
SELECT setval(pg_get_serial_sequence('keyword_tr', 'id'), coalesce(max(id), 0)+1 , false) FROM keyword_tr;
