/*
 * Template for inserting some sample record for development
    INSERT INTO paper VALUES(
    DEFAULT -- id
    , '', 0 -- doi, pm_id
    , '' -- authors
    , '', 0 -- first_author, first_author_overridden
    , '' -- title
    , '', 0 -- location, publication_year
    , '' -- goals
    , '' -- population
    , '', '', '' -- population_place, population_participants, population_duration
    , '', '' -- exposure_pollutant, exposure_assessment
    , '' -- methods
    , '', '', '', '' -- method_study_design, method_outcome, method_statistics, method_confounders
    , '' -- result
    , '', ''  -- result_exposure_range, result_effect_estimate
    , '', '' -- comment, intern
    , null -- code_codeclass1_main
    , 1, current_timestamp() -- version, timestamp
);
 */

INSERT INTO paper VALUES(
    DEFAULT
    , '10.1093/aje/kwu275', 25395026
    , 'Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewski D, Beckermann BS, Samet, JM.'
    , 'Turner', 0
    , 'Interactions Between Cigarette Smoking and Fine Particulate Matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II.'
    , 'Am J Epidemiol 2014 15; 180 (12) 1145-1149.', 2014
    , 'Neue Analyse der Daten der amerikanischen Krebspräventions-Kohertenstudie zur Untersuchung, wie gross das kombinierte Krebsrisiko durch Feinstaub ist.'
    , '429''406 Teilnehmer, Frauen und Männer aus 50 Staaten der USA, welche in den Jahren 1982/1983 im Alter von mindestens 30 Jahren für die Krebsvorsorgestudie der amerikanischen Kriegsgesellschaft (ACS) rekrutiert worden waren, in den Jahren 1984, 1986 und 1988 wieder kontaktiert worden waren, und seit 1989 mit dem nationalen Sterberegister auf ihr Überleben kontaktiert wurden. Nicht in diese Analyse einbezogen wurden Exrauchende und Pfeifen- oder Zigarrenraucher. USA.'
    , '', '', ''
    , '', ''
    , 'Da nur bis 1998 individuelle Informationen über das Rauchverhalten vorlagen, wurden nur die ersten 6 Studienjahre in diese Analyse einbzeogen. Die Abschätzung der Belastung mit Feinstaub wurde mit Landnutzungsmodellen für die geocodierten Adressen bei Studieneintritt vorgenommen, welche sich auf Monatsmittelwerte von PM2.5 der Jahre 1999-2004 von 1464 Messstationen abstützten, unter der Annahme, dass die Feinstaubbelastungen über die Jahre eng korreliert seien. Mit proportionalen Hazard-Modellen nach Cox, stratifiziert für Alter, Geschlecht und Rasse wurde das Überleben bzw. die Sterblichkeit an Lungenkrebs in den ersten 6 Jahren in Abhängigkeit der PM2.5-Belastung in verschiedenen Kategorien (über/unter der 50 Perzentile, über der 66. vs. unter der 33. Perzentile, sowie über der 75. vs. unter der 25. Perzentile) und in Abhängigkeit von Rauchen/nicht Rauchen modelliert. Einbezogen wurden folgende invidivuellen Faktoren: Schulbildung, Zivilstand, BMI, Passivrauchen, Ernährung, Alkoholkonsum und berufliche Belastung. Die Effektmodifikation bezüglich Lungenkrebsterblichkeit wurde mit drei Grössen untersucht: das relative zusätzliche Risiko durch die Interatkion (RERI), der der Interaktion anrechenbare Teil des Risikos (AP) und der Synergie-Index (SI). Lungenkrebs, Kohortenstudie, Statistik, epidemiologische Methoden. ACS-Studie. USA.'
    , '', '', '', ''
    , 'In 2''509''717 Personen-Jahren der Nachkontrolle ereigneten sich 1921 Todesfälle an Lungenkrebs. Die geschätzte Feinstaubbelastung lag im Durchschnitt bei 12.6 SD 2.85 µg PM2.5/m3, mit der 25. und 75. Perzentile bei 10.59 und 14.44 µg PM2.5/m3. Raucher hatten im Vergleich zu Nichtrauchern ein 13.5 fach erhöhtes Risiko (95%CI 10.2-17.9), an Lungenkrebs zu sterben, wenn ihre PM2.5-Belastung gering war, d.h. unter der 25. Perzentile lag. Nichtraucher hatten ein 1.28 faches Risiko (0.92-1.78), wenn ihre Belastung über der 75. Perzentile der PM2.5-Belastung lag, im Vergleich zu Nichtrauchern mit geringer Belastung. Raucher hatten ein 16 faches Risiko (12.1-21.1), an Lungenkrebs zu sterben, wenn ihre Feinstaubbelastung über der 75. Perzentile lag. Das zusätzliche relative Risiko durch die Interaktion (RERI) für die Kombination von Rauchen und schlechter Luft betrug 2.19 (-0.10;+4.83). Der Risikoanteil, der dem Kombinationseffekt angerechnet werden kann, betrug 14%, der Synergie-Index 1.17. Die Autoren schliessen daraus, dass die Folgen von Rauchen und Luftverschmutzung stärker zusammenwiren als nur additiv. Auch wenn die Lungenkrebfälle am stärksten durch einen Rückgang des Rauchens abnehmen, kann ein solcher Rückgang mit einer Verbesserung der Luftqualität stärker ausfallen als mit einer der beiden Massnahmen allein.'
    , '', ''
    , 'Kommentar von Panagiotou AO, Wacholder S: How Big Is That Interaction (in My Community)-and I. Which Direction? Am. J. Epidemiol. 2014 180: 1150-1158.', ''
    , '1F'
    , 1, current_timestamp()
);

INSERT INTO paper VALUES(
    DEFAULT -- id
    , '10.1161/ATVBAHA.116.307141', 27312220 -- doi, pm_id
    , 'Dorans KS, Wilker EH, Li W, Rice MB, Ljungman PL, Schwartz J, Coull BA, Kloog I, Koutrakis P, D''Agostino RB Sr, Massaro JM, Hoffmann U, O''Donnell CJ, Mittleman MA.' -- authors
    , 'Dorans', 0 -- first_author, first_author_overridden
    , 'Residential Proximity to Major Roads, Exposure to Fine Particulate Matter, and Coronary Artery Calcium: The Framingham Heart Study.' -- title
    , 'Arterioscler Thromb Vasc Biol. 2016 Aug;36(8):1679-85', 2016 -- location, publication_year
    , 'Kohortenstudie in den USA zur Untersuchung, ob die Verkalkung der Herzkranzgefässe mit der Belastung mit PM2.5 oder der Distanz zu einer Autobahn zusammenhängt.' -- goals
    , '3399 Nachkommen (Kinder und Enkel, 51% Frauen) der Teilnehmer der Framingham-Kohorte, die bei der ersten von ein (2002-2005) bis zwei (2008-2011) Untersuchungen der Verkalkung der Herzkranzgefässe CAC durchschnittlich 52.2 Jahre alt waren und im Nordosten der USA wohnten.' -- population
    , '', '', '' -- population_place, population_participants, population_duration
    , '', '' -- exposure_pollutant, exposure_assessment
    , 'Die Verkalkung der Herzkranzgefässe CAC wurde mit Elektronenstrahl-Computertomographie untersucht und die Agatstonpunktzahl als Zielgrösse definiert. Ein Wert von mehr als Null wurde als Verkalkung interpretiert. Informationen zu weiteren Gesundheitsgrössen der Patienten stammten aus den aktuellsten Ergebnissen der 4 jährlichen Gesundheitsuntersuchungen.\\nAls Belastung mit Verkehr wurde die Distanz der Adresse zum Zeitpunkt der ersten Untersuchung zur nächsten Autobahn oder einer anderen grösseren Verkehrsachse (Klassen A1-A3, Entfernung von max. 50, 50-200, 200-400 und 400-1000m) definiert. Ausserdem wurde die Feinstaubbelastung mit einem Landnutzungsmodell für PM2.5 für das Jahr 2003 und 2003-2009 bestimmt, das auf Satellitendaten mit einer Auflösung von 1 x 1 km basierte.\\nDer Zusammenhang der Koronarverkalkung (binärer CAC-Score ja/nein, kontinuierlich) mit der Strassennähe oder der Feinstaubbelastung wurde einerseits mit verallgemeinerten Schätzgleichungen mit logit Link andererseits mit gemischten linearen Modellen untersucht. Der Zusammenhang der fortschreitenden Verkalkung (1. zur 2. Untersuchung) wurde mit logistischer Regression und mit gemischten linearen Modellen untersucht. In alle Modelle wurden Alter, Geschlecht, BMI, Rauchen, Bildung, %Hausbesitzer auf Quartierebenen als Variable für den sozioökonomischen Status, Kohorte (Enkeln oder Kinder der Ursprungskohorte), Datum der CAC Untersuchung und Abstand zur Gesundheitsuntersuchung (Tage) einbezogen. Sensitivitätsanalysen mit weiteren Störgrössen (Diabetes, Bewegungsverhalten, hoher Blutdruck) und Interaktionen wurden durchgeführt und es wurde versucht, eine nicht-lineare Dosis-Wirkungsbeziehung zu beschreiben.\\nHerz-/Kreislaufkrankheiten, Arteriosklerose, Framingham. Kohortenstudie. USA.' -- methods
    , '', '', '', '' -- method_study_design, method_outcome, method_statistics, method_confounders
    , 'Die Teilnehmer wohnten median 201m (Interquartilabstand IQR 359m) von einer grossen Strasse (USA: Kategorie A1-A3) entfernt und waren im Jahr 2003 mit median 10.7 (IQR 1.4) µg/m3 PM2.5 belastet. Bei 45% der Teilnehmer wurde Verkalkungen der Herzkranzgefässe festgestellt, bei der zweiten Untersuchung waren es 50%. Der mediane Agatston Score betrug bei Erstuntersuchung 70.5 IQR 278.5, bei der zweiten Untersuchung nach durchschnittlich 6.1 Jahren 137.1 (419.4).\\nPersonen in der Nähe von Autobahnen oder mit höheren Feinstaubbelastung hatten kein erhöhtes Risiko für einen messbaren CAC-Score, also einer Verkalkung der Herzkranzgefässe, das Risiko war tendenziell eher (paradox) reduziert, aber mit weiten Konfidenzintervallen: z.B. OR 0.95 (95%-CI: 0.74-1.22) für Personen die weniger als 50m von einer Autobahnentfernt wohnten gegenüber denen, die über 400m entfernt wohnten. Es konnte auch kein Zusammenhang mit der Höhe des Scores und der Belastung gefunden werden. Das Risiko für fortschreitende Koronararterienverkalkung hing ebenfalls nicht mit der Verkehrsnähe oder der Feinstaubbelastung zusammen. Sie hing paradox mit der logarithmierten Entfernung zur Strasse zusammen; steigender CAC-Agatstonscore von 2.2 (0.1-4.3) pro Jahr pro logarithmierter IQR-Entfernung zur Strasse. Die Ergebnisse blieben auch in den Sensitivitätsanalysen robust.\\nDie Autoren folgern, dass ihre Ergebnisse nicht für einen engen Zusammenhang der Koronarverkalkung mit der Verkehrs- oder Feinstaubbelastung sprechen, betonen aber, dass die Belastung generell tief war und wenig variierte.' -- result
    , '', ''  -- result_exposure_range, result_effect_estimate
    , '', '' -- comment, intern
    , null
    , 1, current_timestamp() -- version, timestamp
);

INSERT INTO paper VALUES(
    DEFAULT -- id
    , '10.1161/JAHA.115.002301', 26607712  -- doi, pm_id
    , 'Hart JE, Puett RC, Rexrode KM, Albert CM, Laden F.' -- authors
    , 'Hart', 0 -- first_author, first_author_overridden
    , 'Effect Modification of Long-Term Air Pollution Exposures and the Risk of Incident Cardiovascular Disease in US Women.' -- title
    , 'J Am Heart Assoc. 2015; 4 (12). pii: e002301.', 2015 -- location, publication_year
    , 'Kohortenstudie an Krankenschwestern zur Untersuchung von Herz-/Kreislauferkrankungen in Abhängigkeit von der langjährigen Belastung mit Feinstaub.' -- goals
    , '114''537 Krankenschwestern in den USA, welche erstmals 1976 im Alter von 30-55 Jahren und seither alle 2 Jahre nachbefragt wurden, von denen mindestens eine Adresse bekannt war, für welche Schadstoffwerte bekannt waren. Nurses Health Studie.' -- population
    , '', '', '' -- population_place, population_participants, population_duration
    , '', '' -- exposure_pollutant, exposure_assessment
    , 'In der Nachfolgezeit von 1988-2006 wurde die Inzidenz von erstmaligen Herz-/Kreislaufkrankheiten, nicht tödlicher oder tödlicher koronarer Herzerkrankung und Hirnschlag untersucht. Die Informationen stammten aus den Fragebogen und wurde mit Krankengeschichten, Autopsie und Todesurkunde überprüft. Das Luftqualitätsmessnetz der US-EPA, das Projekt IMPROVE und verschiedene weitere Studien lieferten monatliche Messdaten für PM10, ab 1999 auch PM2.5. Für die Jahre vor 1999 wurde PM2.5 aus PM10 abgeleitet unter Verwendung des PM10/PM2.5 Verhältnisses aus einem Modell, das auf Daten der Zeit nach 1999 basierte. Die Belastung mit der gröberen Fraktion PM10-2.5 wurde aus der Differenz von PM10 und PM2.5 berechnet. Mit einem Landnutzungsmodell wurde den geocodierten Adressen (basierend auf der Postleitzahl) die Belastung kumuliert über 18 Jahre (1988-2006) zugeordnet.\\nMit proportionalen Hazard-Modellen nach Cox wurde die Inzidenz an Herz-/Kreislaufkrankheiten, koronaren Herzerkrankungen und Hirnschlag in Abhängigkeit der Feinstaubbelastung untersucht. In einem Grundmodell wurden Alter, Jahr, Jahreszeit und geografische Region und im erweiterten Modell zusätzlich Rasse, BMI, Wechseljahrstatus, Hormoneinnahme, Bluthochdruck, Hypercholesterinämie, Diabetes, familiäre Anamnese von Herzinfarkt, Rauchen, als Indikator für den Sozialstatus die Schulbildung, Zivilstand und Beruf des Partners und der Sozialstatus im Quartier einbezogen. Ausserdem wurde die Interaktion mit dem Alter, Diabetes, Familienanamnese von Herzinfarkt und Rauchen geprüft. Sensitivitätsanalyen zur Überprüfung der Robusheit wurden durchgeführt. Kohortenstudie. Nurses Health. USA.' -- methods
    , '', '', '', '' -- method_study_design, method_outcome, method_statistics, method_confounders
    , '6''767 Frauen erkrankten an Herz-Kreislauferkrankungen, 3''878 an  koronaren  Herzkrankheiten und 3''295 erlitten einen Hirnschlag. Die Frauen waren durchschnittlich 68.6 (7.3) Jahre alt, überwiegend weiss und verheiratet. Die durchschnittliche jährliche Belastung und die Standardabweichung (SD) betrug 22.2 (6.5) µg PM10/m3, 8.7 (4.5) µg PM2.5-10/m3 und 13.4 (3.3) µg PM2.5/m3.\\nIm Grundmodell nahm das Risiko bei einem Anstieg von 10 µg/m3 aller Feinstaubfraktionen gering, aber statistisch nicht signifikant zu. Lediglich für einen Anstieg von PM10 und Herz-/Kreislaufkrankheiten und für PM2.5-10 mit allen drei Zielgrössen gab es einen signifikanten Zusammenhang. Die Effektschätzer nahmen jedoch unter Einbezug aller Störfaktoren ab und waren nicht mehr signifikant.\\nDie Inzidenz von Herz-/Kreislaufkrankheiten und Hirnschlag nahm jedoch bei Frauen mit Diabetes gegenüber Frauen ohne Diabetes für PM10, PM2.5-10 und PM2.5 zu mit HR 1.19 (95%CI: 1.1-1.28), 1.17 (1.05-1.3) und 1.44 (1.23-1.68), resp. 1.23 (1.1-1.38), 1.18 (1.01-1.38) und 1.66 (1.31-2.1). Die Belastung mit PM10 und PM2.5-10, nicht aber mit PM2.5 war ausserdem mit einem erhöhten Risiko bei Diabetikerinnnen für koronare Herzkrankheiten mit 1.12 (1.02-1.23) und 1.14 (1-1.3) verbunden.\\nDie Autoren schliessen daraus, dass die Feinstaubbelastung bei Frauen mittleren Alters das Inzidenzrisiko für Herz-/Kreislaufkrankheiten gering, aber nicht signifikant erhöhe, die Effektschätzer aber bei Frauen mit Diabetes erhöht und signfikant waren.' -- result
    , '', ''  -- result_exposure_range, result_effect_estimate
    , '', '' -- comment, intern
    , null
    , 1, current_timestamp() -- version, timestamp
);

INSERT INTO paper VALUES(
	DEFAULT
	, '101234/12345', 1
	, 'Kutlar Joss M, Joss U.'
	, 'Kutlar Joss M.', 0
	, 'Sample Title', 'loc', 2016
	, 'goals'
	, 'pop', 'pop_place', 'pop_participants', 'pop_duration'
	, 'exp_pollutant', 'exp assessment'
	, 'methods'
	, 'method study design', 'method outcome', 'method statistics', 'method confounder'
	, 'result', 'result exposure range', 'result effect estimate'
	, 'comment', 'int'
	, null
	, 1, current_timestamp()
);

INSERT INTO code_class VALUES (1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (2, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (3, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (4, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (5, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (6, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (7, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (8, DEFAULT, DEFAULT);

INSERT INTO code_class_tr VALUES (DEFAULT, 1, 'de', 'Schadstoffe', 'Schadstoffe, Einwirkung, Exposition', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 2, 'de', 'Region', 'Geographie (Europa, andere)', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 3, 'de', 'Kollektiv', 'Stichprobe, Kollektiv', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 4, 'de', 'Zielgrössen', 'Wirkungen, Zielgrössen', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 5, 'de', 'Studientyp', 'Art der Studie, der Publikation', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 6, 'de', 'Spezies', 'Spezies (Mensch, Tier, Pflanze etc.)', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 7, 'de', 'Zeitdauer', 'Zeit der Einwirkung (kurzfristig – langfristig)', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 8, 'de', 'Umgebung', 'Umgebung (berufliche Exposition, Aussenluft…)', DEFAULT, DEFAULT);

INSERT INTO code_class_tr VALUES (DEFAULT, 1, 'en', 'Exposure Agent', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 2, 'en', 'Region', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 3, 'en', 'Study Population', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 4, 'en', 'Health Outcome', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 5, 'en', 'Study Design', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 6, 'en', 'Species', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 7, 'en', 'Duration of Exposure', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 8, 'en', 'Setting', '', DEFAULT, DEFAULT);

INSERT INTO code_class_tr VALUES (DEFAULT, 1, 'fr', 'Polluant nocif', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 2, 'fr', 'Région', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 3, 'fr', 'Echantillons des personnes', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 4, 'fr', 'Effets physiologigue ou nocifs', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 5, 'fr', 'Typ d''étude', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 6, 'fr', 'Espèces investigées', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 7, 'fr', 'Durée de l''exposition', '', DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 8, 'fr', 'Site d''exposition', '', DEFAULT, DEFAULT);

INSERT INTO code VALUES('1F', 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1A', 1, 2, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1B', 1, 3, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1C', 1, 4, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1D', 1, 5, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1E', 1, 6, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1G', 1, 7, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1H', 1, 8, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1I', 1, 9, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1K', 1, 10, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1L', 1, 11, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1M', 1, 12, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1N', 1, 13, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1P', 1, 14, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1R', 1, 15, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1S', 1, 16, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1T', 1, 17, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1U', 1, 18, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1V', 1, 19, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1Y', 1, 20, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1Z', 1, 21, DEFAULT, DEFAULT);

INSERT INTO code VALUES('2R', 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('2N', 2, 2, DEFAULT, DEFAULT);

INSERT INTO code VALUES('3A', 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3B', 3, 2, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3C', 3, 3, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3H', 3, 4, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3D', 3, 5, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3E', 3, 6, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3F', 3, 7, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3G', 3, 8, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3I', 3, 9, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3O', 3, 10, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3K', 3, 11, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3L', 3, 12, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3U', 3, 13, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3Z', 3, 14, DEFAULT, DEFAULT);

INSERT INTO code VALUES('4A', 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4B', 4, 2, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4C', 4, 3, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4D', 4, 4, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4E', 4, 5, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4F', 4, 6, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4G', 4, 7, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4H', 4, 8, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4I', 4, 9, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4K', 4, 10, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4M', 4, 11, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4N', 4, 12, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4O', 4, 13, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4L', 4, 14, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4U', 4, 15, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4X', 4, 16, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4Y', 4, 17, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4Z', 4, 18, DEFAULT, DEFAULT);

INSERT INTO code VALUES('5A', 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5B', 5, 2, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5C', 5, 3, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5E', 5, 4, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5F', 5, 5, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5G', 5, 6, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5H', 5, 7, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5I', 5, 8, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5M', 5, 9, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5S', 5, 10, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5D', 5, 11, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5K', 5, 12, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5U', 5, 13, DEFAULT, DEFAULT);

INSERT INTO code VALUES('6M', 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6T', 6, 2, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6O', 6, 3, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6P', 6, 4, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6Z', 6, 5, DEFAULT, DEFAULT);

INSERT INTO code VALUES('7K', 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7L', 7, 2, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7M', 7, 3, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7Z', 7, 4, DEFAULT, DEFAULT);

INSERT INTO code VALUES('8I', 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8O', 8, 2, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8P', 8, 3, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8W', 8, 4, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8Z', 8, 5, DEFAULT, DEFAULT);


INSERT INTO code_tr VALUES(DEFAULT, '1F', 'de', 'Feinstaub, Partikel', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1A', 'de', 'Ozon, Oxidantien', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1B', 'de', 'Stickoxide, Ammoniak', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1C', 'de', 'Kohlenwasserstoffe, nicht chloriert, VOC', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1D', 'de', 'Kohlenmonoxid', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1E', 'de', 'SO2, Schwefelverbindungen,', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1G', 'de', 'Metalle', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1H', 'de', 'Halogene, halogenierte Stoffe', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1I', 'de', 'Saure Aerosole', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1K', 'de', 'Radioaktive Stoffe', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1L', 'de', 'Asbest, Fasern', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1M', 'de', 'Tabakrauch, Passivrauchen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1N', 'de', 'Keine Messung, andere Art der Exposure (Stadt : Land) z.B. Lärm', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1P', 'de', 'Pollen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1R', 'de', 'Feuchtigkeit in der Raumluft', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1S', 'de', 'Milben, Allergene in den Raumluft', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1T', 'de', 'Temperatur', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1U', 'de', 'Andere / Übrige', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1V', 'de', 'Verkehr', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Y', 'de', 'Gerüche', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Z', 'de', 'Mehr als 8 Schadstoffe, alle gemeinsam', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '2R', 'de', 'Europa', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '2N', 'de', 'Übrige Länder', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '3A', 'de', 'Säuglinge, Vorschulkinder', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3B', 'de', 'Schulkinder und Jugendliche', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3C', 'de', 'Erwachsene (alle)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3H', 'de', 'Betagte Personen (65+ Jahre)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3D', 'de', 'Personen mit Asthma oder Atemwegsallergie', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3E', 'de', 'Pers. mit anderer chronischer Lungenkrankheit, COPD', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3F', 'de', 'Registerdaten, Patienten', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3G', 'de', 'Schwangere Frauen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3I', 'de', 'Sportler/innen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3O', 'de', 'Beruflich Exponierte', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3K', 'de', 'Gewebe', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3L', 'de', 'Zellen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3U', 'de', 'Übrige /Andere', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3Z', 'de', 'Mehr als 8 / alle', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '4A', 'de', 'Sterblichkeit', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4B', 'de', 'Lungenfunktion', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4C', 'de', 'Symptome', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4D', 'de', 'Biochemische oder zelluläre Veränderungen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4E', 'de', 'Atemwegskrankheiten akut', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4F', 'de', 'Atemwegskrankheiten chronisch', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4G', 'de', 'Krebs', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4H', 'de', 'Herz, Kreislauf, Blut', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4I', 'de', 'Gehirn, Nerven, Augen, Ohren', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4K', 'de', 'Belästigung, psychische Folgen, Lebensqualität', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4M', 'de', 'Medikamentenkonsum, Konsultation, Spitaleintritt', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4N', 'de', 'Fertilität, Schwangerschaftsproblem, Abort', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4O', 'de', 'Frühgeburt, Säuglingsgewicht, pränatale Entwicklung', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4L', 'de', 'Missbildungen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4U', 'de', 'Übrige /Andere', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4X', 'de', 'Kosten', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Y', 'de', 'Absenzen, eingeschränkte Aktivität', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Z', 'de', 'Mehr als 8 / Alle', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '5A', 'de', 'Experimentelle Studie unter Belastung / Arbeit', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5B', 'de', 'Experimentelle Studie in Ruhe', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5C', 'de', 'Experimentelle Studie Zellstudie etc.', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5E', 'de', 'Einmalige Exposition, Unfall, Brand', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5F', 'de', 'Zeitreihen, Panel, kurzfristige Längsstudie', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5G', 'de', 'Querschnitt, Fall-Kontrollstudie, deskriptiv', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5H', 'de', 'Kohortenstudie', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5I', 'de', 'Interventionsstudie', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5M', 'de', 'Messstudie, Studienmethodik', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5S', 'de', 'Statistik', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5D', 'de', 'Einzelfallbeschreibung, Fallstudie', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5K', 'de', 'Krankheitsbeschreibung', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5U', 'de', 'Übersichten, Metaanalysen, HIA', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '6M', 'de', 'Mensch', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6T', 'de', 'Tier', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6O', 'de', 'Viren, Bakterien, Einzeller', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6P', 'de', 'Pflanzen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6Z', 'de', 'Alle / allg. Übersichten', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '7K', 'de', 'Kurzfristig', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7L', 'de', 'Langfristig', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7M', 'de', 'Mittelfristig', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7Z', 'de', 'Alle', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '8I', 'de', 'Raumluft', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8O', 'de', 'Aussenluft', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8P', 'de', 'Individuelle Messung', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8W', 'de', 'Berufliche Exposition', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8Z', 'de', 'Alle', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '1F', 'en', 'Particles, Particulate Matter', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1A', 'en', 'Ozone, Oxidants', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1B', 'en', 'Nitrogen Oxides, Ammonia', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1C', 'en', 'Hydrocarbons, non-chlorinated, VOC', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1D', 'en', 'Carbonmonoxide', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1E', 'en', 'SO2, sulfur compounds', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1G', 'en', 'Metals', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1H', 'en', 'Halogens, halogenated substances', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1I', 'en', 'Acidic Aerosols', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1K', 'en', 'Radioactive substances', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1L', 'en', 'Asbestos, fibres', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1M', 'en', 'Tobacco smoke, passive smoking', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1N', 'en', 'Different exposure or assessment (geographic, noise)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1P', 'en', 'Pollen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1R', 'en', 'Indoor air humidity', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1S', 'en', 'Mites, indoor allergens', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1T', 'en', 'Temperature', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1U', 'en', 'Others', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1V', 'en', 'Traffic', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Y', 'en', 'Odours', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Z', 'en', 'More than 8 pollutants, all together', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '2R', 'en', 'Europe', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '2N', 'en', 'Other countries', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '3A', 'en', 'Newborns, infants, pre-school children', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3B', 'en', 'School children, adolescents', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3C', 'en', 'Adults (19+)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3H', 'en', 'Agend persons (65+)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3D', 'en', 'People with asthma or respiratory allergies', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3E', 'en', 'People with chronic respiratory disease, COPD', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3F', 'en', 'Registry data, patient records', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3G', 'en', 'Pregnant women', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3I', 'en', 'Athletes', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3O', 'en', 'Ocupationally exposed', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3K', 'en', 'Tissue', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3L', 'en', 'Cells', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3U', 'en', 'Others', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3Z', 'en', 'More than 8 / all', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '4A', 'en', 'Mortality', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4B', 'en', 'Lung function, pulmonary function', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4C', 'en', 'Symptoms', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4D', 'en', 'Biochemical or cellular responses', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4E', 'en', 'Respiratory diseases acute', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4F', 'en', 'Respiratory diseases chronic', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4G', 'en', 'Cancer', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4H', 'en', 'Heart, cardovascular system, blood', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4I', 'en', 'Brain, nervous system, eyes, ears', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4K', 'en', 'Annoyance, well-being, quality of life', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4M', 'en', 'Drug intake, consultation, hospital admission', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4N', 'en', 'Fertility, pregnancy complications, miscarriage', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4O', 'en', 'Preterm birth, birth weight, prenatal development', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4L', 'en', 'Congenital malformation', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4U', 'en', 'Others', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4X', 'en', 'Cost', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Y', 'en', 'Absences, limited activity (TODO)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Z', 'en', 'More than 8 / all', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '5A', 'en', 'Experimental study TODO (unter Belastung)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5B', 'en', 'Experimental study TODO (in Ruhe)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5C', 'en', 'Experimental study TODO (Zellstudie etc.)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5E', 'en', 'Accidental release, fire', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5F', 'en', 'Panel study,  time series', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5G', 'en', 'Cross-sectional or case-control study, descriptive', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5H', 'en', 'Cohort Study', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5I', 'en', 'Intervention', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5M', 'en', 'Exposure assessment , modelling', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5S', 'en', 'Statistics', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5D', 'en', 'TODO (Einzelfallbeschreibung, Fallstudie)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5K', 'en', 'TODO (Krankheitsbeschreibung)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5U', 'en', 'Review, meta-analysis', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '6M', 'en', 'Humans', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6T', 'en', 'Animals', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6O', 'en', 'Viruses, Bacteria, Protozoa', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6P', 'en', 'Plants', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6Z', 'en', 'All / Gen. overviews', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '7K', 'en', 'Short-term', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7L', 'en', 'Long-term', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7M', 'en', 'Mid-term', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7Z', 'en', 'All', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '8I', 'en', 'Indoor Air', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8O', 'en', 'Ambient air', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8P', 'en', 'Personal measurement', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8W', 'en', 'Occupational exposure', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8Z', 'en', 'All', DEFAULT, DEFAULT);


INSERT INTO code_tr VALUES(DEFAULT, '1F', 'fr', 'Poussière fines, particules', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1A', 'fr', 'Ozone, Oxydants', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1B', 'fr', 'Oxydes d''azote, ammoniac', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1C', 'fr', 'Hydrocarbures non-chlorurés', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1D', 'fr', 'Monoxyde de carbone', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1E', 'fr', 'SO2, composé de soufre', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1G', 'fr', 'Métals', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1H', 'fr', 'Halogènes, substances halogénées', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1I', 'fr', 'Aérosols acides', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1K', 'fr', 'Substances radioactives', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1L', 'fr', 'Amiante, fibres', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1M', 'fr', 'Tabagisme passif, fumée de cigarette', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1P', 'fr', 'Pollen', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1R', 'fr', 'Humidité (air intérieur)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1S', 'fr', 'Allergènes dans l''air intérieur', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1T', 'fr', 'Température', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1U', 'fr', 'Autres', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1V', 'fr', 'Trafic / circulation', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Y', 'fr', 'Odeur', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Z', 'fr', 'Plus de 8 polluants / tous', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '2R', 'fr', 'Europe', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '2N', 'fr', 'Autres pays', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '3A', 'fr', 'Nourisson, infants, enfants préscolaires', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3B', 'fr', 'Enfants scolaires, adolescents', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3C', 'fr', 'Adultes (19+)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3H', 'fr', 'Personnes agées (65+)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3D', 'fr', 'Personnes asthmatiques ou allérgiques', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3E', 'fr', 'Pers. souffrantes des maladies pulmonaire chronique', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3F', 'fr', 'Données de registre ou des patients', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3G', 'fr', 'Femmes enceintes', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3I', 'fr', 'Sportifs', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3O', 'fr', 'Personnes exposées au travail', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3K', 'fr', 'Tissus', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3L', 'fr', 'Cellules', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3U', 'fr', 'Autres (TODO)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3Z', 'fr', 'Plus de 8 / tous (TODO)', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '4A', 'fr', 'Mortalité', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4B', 'fr', 'Fonction pulmonaire', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4C', 'fr', 'Symptômes, Signes', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4D', 'fr', 'Changements biochimiques ou cellulaire', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4E', 'fr', 'Maladie respiratoire, imminante', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4F', 'fr', 'Maladie respiratoire, chronique', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4G', 'fr', 'Cancer', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4H', 'fr', 'Coeur, circulation, sang', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4I', 'fr', 'Système nerveux, yeux, oreilles', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4K', 'fr', 'Nuisance, bien-être, qualité de vie', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4M', 'fr', 'Médicaments, visite médicale, hospitalisation', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4N', 'fr', 'Fertilité, complication de grossesse, fausse couche', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4O', 'fr', 'Naissance prématurée, poids de naissance ', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4L', 'fr', 'Malformation', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4U', 'fr', 'Autres', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4X', 'fr', 'Coûts (TODO)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Y', 'fr', 'Absences, activités limités (TODO)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Z', 'fr', 'Plus de 8 / tous', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '5A', 'fr', 'Etude expérimentale TODO (unter Belastung / Arbeit)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5B', 'fr', 'Etude expérimentale TODO (in Ruhe)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5C', 'fr', 'Etude expérimentale TODO (Zellstudie etc.)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5E', 'fr', 'Accident, incident chimique, feu', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5F', 'fr', 'Etude panel, étude longitudinale à court terme, séries chronologiques', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5G', 'fr', 'Etude transversal ou cas-témoin, descriptive', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5H', 'fr', 'Etude de cohorte', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5I', 'fr', 'Intervention', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5M', 'fr', 'Méthodologie, statistique (TODO diff. 5S)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5S', 'fr', 'Méthodologie, statistique (TODO diff. 5M)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5D', 'fr', 'TODO (Einzelfallbeschreibung, Fallstudie)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5K', 'fr', 'TODO (Krankheitsbeschreibung)', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5U', 'fr', 'Review, révue, méta-analyse', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '6M', 'fr', 'Hommes', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6T', 'fr', 'Animaux', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6O', 'fr', 'Virus, Bactéries, Protozoaires', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6P', 'fr', 'Plantes', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6Z', 'fr', 'Tous / Exposés gen.', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '7K', 'fr', 'à court terme', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7L', 'fr', 'à long terme', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7M', 'fr', 'à moyen terme', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7Z', 'fr', 'tous', DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '8I', 'fr', 'Air intérieur', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8O', 'fr', 'Air extérieur', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8P', 'fr', 'Mesures personalisées', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8W', 'fr', 'Exposition lié au travail', DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8Z', 'fr', 'Tous', DEFAULT, DEFAULT);

INSERT INTO paper_code VALUES(1, '1F', DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '2N', DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '3C', DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '4G', DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '5H', DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '5S', DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '6M', DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '7L', DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '8O', DEFAULT, DEFAULT);