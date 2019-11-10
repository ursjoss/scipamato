INSERT INTO newsletter(id, issue, issue_date, publication_status) VALUES
   (1, '1802', '2018-02-01', 1),
   (2, '1804', '2018-04-01', 0);
SELECT setval(pg_get_serial_sequence('newsletter', 'id'), coalesce(max(id), 0)+1 , false) FROM newsletter;

INSERT INTO newsletter_topic(id) VALUES
   (1),
   (2),
   (3);
SELECT setval(pg_get_serial_sequence('newsletter_topic', 'id'), coalesce(max(id), 0)+1 , false) FROM newsletter_topic;

INSERT INTO newsletter_topic_tr(id, newsletter_topic_id, lang_code, title) VALUES
   (1, 1, 'de', 'Ultrafeine Partikel'),
   (2, 1, 'en', 'Ultrafine Particles'),
   (3, 2, 'de', 'Sterblichkeit'),
   (4, 2, 'en', 'Mortality'),
   (5, 3, 'de', 'Gesundheitsfolgenabschätzung'),
   (6, 3, 'en', 'Health Impact Assessment');
SELECT setval(pg_get_serial_sequence('newsletter_topic_tr', 'id'), coalesce(max(id), 0)+1 , false) FROM newsletter_topic_tr;

INSERT INTO paper_newsletter(paper_id, newsletter_id, newsletter_topic_id, headline) VALUES
   (31, 1, 1, 'some headline'), (33, 1, 1, 'another headline'), (35, 1, 1, null), (20, 1, 2, null), (37, 1, 3, null),
   (39, 2, null, null), (41, 2, null, null);