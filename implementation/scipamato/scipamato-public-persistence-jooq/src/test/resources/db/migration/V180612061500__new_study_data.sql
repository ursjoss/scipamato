INSERT INTO newsletter(id, issue, version, created, last_modified) VALUES
(1, '2018/04', 1, TIMESTAMP '2018-03-25 14:47:29.431', TIMESTAMP '2018-03-25 14:47:29.431'),
(2, '2018/06', 1, TIMESTAMP '2018-05-20 11:34:52.123', TIMESTAMP '2018-05-20 11:34:52.123');

INSERT INTO newsletter_topic(id, lang_code, title, version, created, last_modified) VALUES
(1, 'de', 'Tiefe Belastungen'                              , 1, TIMESTAMP '2018-01-01 12:53:15.264', TIMESTAMP '2018-01-01 12:53:15.264'),
(2, 'de', 'Hirnleistung und neurodegenerative Erkrankungen', 1, TIMESTAMP '2018-01-01 12:53:15.264', TIMESTAMP '2018-01-01 12:53:15.264'),
(3, 'de', 'Feinstaubkomponenten und PAK'                   , 1, TIMESTAMP '2018-01-01 12:53:15.264', TIMESTAMP '2018-01-01 12:53:15.264'),
(4, 'de', 'Hirnschlag'                                     , 1, TIMESTAMP '2018-01-01 12:53:15.264', TIMESTAMP '2018-01-01 12:53:15.264');

INSERT INTO new_study_topic(id, newsletter_id, newsletter_topic_id, sort, version, created, last_modified) VALUES
(1, 1, 1, 1, 1, TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(2, 1, 2, 2, 1, TIMESTAMP '2018-03-28 14:53:15.264', TIMESTAMP '2018-03-28 14:53:15.264'),
(3, 1, 3, 3, 1, TIMESTAMP '2018-03-29 09:53:15.264', TIMESTAMP '2018-03-29 09:53:15.264'),
(4, 1, 4, 4, 1, TIMESTAMP '2018-04-01 09:53:15.264', TIMESTAMP '2018-04-01 09:53:15.264'),
(5, 2, 2, 1, 1, TIMESTAMP '2018-01-01 12:53:15.264', TIMESTAMP '2018-01-01 12:53:15.264');

SELECT setval(pg_get_serial_sequence('new_study_topic', 'id'), coalesce(max(id), 0)+1 , false) FROM new_study_topic;

INSERT INTO new_study(newsletter_id, new_study_topic_id, sort, paper_number, year, authors, headline, description, version, created, last_modified) VALUES
(1, 1, 1, 8924, 2017, 'Di et al.'         , 'USA: Grosse Kohortenstudie zeigt, dass auch ein PM2.5-Grenzwert von 12 µg/m3 oder 10 µg/m3 die Bevölkerung nicht schützt.', 'Registerkohortenstudie in den USA zur Untersuchung, ob die Sterblichkeit mit der langfristigen Feinstaub- oder Ozonbelastung auch unterhalb der Grenzwerte zusammenhängen und ob es empfindliche Gruppen gäbe.', 1, TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 1, 2, 8993, 2017, 'Makar et al.'      , 'USA: Das Risiko für Spitaleintritte war mit der langfristigen Feinstaubbelastung zwischen 8-12µg PM2.5/m3 höher als für Belastungen oberhalb des US-Grenzwerts.', 'Kohortenstudie mit älteren Personen in den USA zur Untersuchung, ob die Sterblichkeit oder Spitaleintritte kausal mit der langsfristigen Feinstaubbelastung auch unterhalb des US-Grenzwerts zusammenhängen.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 2, 1, 8973, 2017, 'Tallon et al.'     , 'USA: Die langfristige Feinstaub- und NO2-Belastung war mit einer beschleunigten Abnahme der Hirnleistung verbunden.', 'Querschnittstudie an älteren Personen zur Untersuchung, ob die Hirnfunktion mit der Luftbelastung zusammenhänge und ob Krankheiten oder die Gemütslage den Zusammenhang beeinflussen.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 2, 2, 8983, 2017, 'Jayaraj et al.'    , 'bersicht: Aufgrund epidemiologischer und klinischer Studien wird ein Zusammenhang zwischen neurodegenerativen Krankheiten und der kognitiven Funktion und der Schadstoffbelastung immer wahrscheinlicher.', 'Übersicht zum Zusammenhang zwischen neurodegenerativen Krankheiten und der langfristigen Schadstoffbelastung und deren Wirkungsmechanismen.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 2, 3, 8984, 2017, 'Chen et al.'       , 'Kanada: Grosse Kohortenstudie findet Zusammenhang mit Demenz in Abhängigkeit von Wohnen in Strassennähe.', 'Kanadische registerbasierte Kohortenstudie zur Untersuchung, ob Demenz, Parkinson oder Multiple Sklerose mit Wohnen in Strassennähe zusammenhängen.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 3, 1, 8933, 2017, 'Guerreiro et al.'  , 'Europa: Der europäische Zielwert für Benzo(a)pyren wird in europäischen Städten um das Zwei- bis Dreifache überschritten und verursacht bis zu 450 zusätzliche Lungenkrebsfälle.', 'Abschätzung der Benzoapyren-Belastung (PAK) in Europa im Jahr 2012 zur Untersuchung der Gesundheitsfolgen.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 3, 2, 8897, 2017, 'Rückerl et al.'    , 'Deutschland: Die Längenkonzentration und aktive Oberfläche ultrafeiner Partikel könnten bessere UFP-Parameter sein als beispielsweise die Partikelzahl.', 'Panelstudie in Augsburg an Personen mit Diabetes oder einer genetischen Veranlagung zu verminderter antioxidativer Abwehr zur Untersuchung, ob Blutindikatoren der Entzündung und Gerinnung mit verschiedenen neuartigen Feinstaubparametern zusammenhängen.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 3, 3, 8861, 2017, 'Krall et al.'      , 'USA: Notfallmässige Spitaleintritte hingen mit der Feinstaubbelastung insgesamt und jener aus der Biomasseverbrennung zusammen.', 'Zeitreihenstudie in vier US-Städten zur Untersuchung, ob Notfalleintritte wegen Atemwegserkrankungen mit der Feinstaubbelastung aus unterschiedlichen Quellen zusammenhängen.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 4, 1, 8916, 2017, 'Qiu et al.'        , 'China: Die langfristige Feinstaubbelastung erhöhte das Risiko für ischämische Hirnschläge, nicht aber für hämorrhagische Hirnschläge.', 'Kohortenstudie an älteren Personen zur Untersuchung, ob die Inzidenz an Hirnschlägen mit der langfristigen Feinstaubbelastung zusammenhänge.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(1, 4, 2, 8934, 2017, 'Desikan et al.'    , 'Grossbritannien: Hirnschlagpatienten - insbesondere solche mit ischämischem Hirnschlag - überlebten weniger lang, wenn sie höheren Feinstaubbelastungen ausgesetzt waren.', 'Registerstudie im südlichen London zur Untersuchung, ob die Überlebenswahrscheinlichkeit nach Hirnschlag mit der langfristigen verkehrsbedingten Feinstaubbelastung und seiner Quellen (Abgas, Nicht-Abgas) zusammenhängt.', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264'),
(2, 5, 1, 1871, 1992, 'Santos-Melo et al.', 'whatever.', 'some', 1,TIMESTAMP '2018-03-28 12:53:15.264', TIMESTAMP '2018-03-28 12:53:15.264');