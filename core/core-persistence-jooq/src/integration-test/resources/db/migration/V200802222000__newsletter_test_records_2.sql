INSERT INTO newsletter_topic(id) VALUES
   (4);
SELECT setval(pg_get_serial_sequence('newsletter_topic', 'id'), coalesce(max(id), 0)+1 , false) FROM newsletter_topic;

INSERT INTO newsletter_topic_tr(id, newsletter_topic_id, lang_code, title) VALUES
   (7, 4, 'de', 'Andere'),
   (8, 4, 'en', 'Others');
SELECT setval(pg_get_serial_sequence('newsletter_topic_tr', 'id'), coalesce(max(id), 0)+1 , false) FROM newsletter_topic_tr;
