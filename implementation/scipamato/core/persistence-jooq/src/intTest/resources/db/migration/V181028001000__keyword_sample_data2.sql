INSERT INTO keyword_tr (keyword_id, lang_code, name)
VALUES (3, 'de', 'Allergie');

SELECT setval(pg_get_serial_sequence('keyword_tr', 'id'), coalesce(max(id), 0)+1 , false) FROM keyword_tr;
