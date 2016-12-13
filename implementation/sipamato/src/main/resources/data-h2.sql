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
    , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT -- version, created, created_by, last_modified, last_modified_by
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
    , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT
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
    , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT
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
    , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT
);

INSERT INTO paper VALUES(
	DEFAULT
	, '10.1234/12345', 1
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
	, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT
);

INSERT INTO code_class VALUES (1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (3, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (4, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (5, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (6, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (7, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (8, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_class_tr VALUES (DEFAULT, 1, 'de', 'Schadstoffe', 'Schadstoffe, Einwirkung, Exposition', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 2, 'de', 'Region', 'Geographie (Europa, andere)', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 3, 'de', 'Kollektiv', 'Stichprobe, Kollektiv', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 4, 'de', 'Zielgrössen', 'Wirkungen, Zielgrössen', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 5, 'de', 'Studientyp', 'Art der Studie, der Publikation', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 6, 'de', 'Spezies', 'Spezies (Mensch, Tier, Pflanze etc.)', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 7, 'de', 'Zeitdauer', 'Zeit der Einwirkung (kurzfristig – langfristig)', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 8, 'de', 'Umgebung', 'Umgebung (berufliche Exposition, Aussenluft…)', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_class_tr VALUES (DEFAULT, 1, 'en', 'Exposure Agent', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 2, 'en', 'Region', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 3, 'en', 'Study Population', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 4, 'en', 'Health Outcome', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 5, 'en', 'Study Design', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 6, 'en', 'Species', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 7, 'en', 'Duration of Exposure', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 8, 'en', 'Setting', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_class_tr VALUES (DEFAULT, 1, 'fr', 'Polluant nocif', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 2, 'fr', 'Région', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 3, 'fr', 'Echantillons des personnes', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 4, 'fr', 'Effets physiologigue ou nocifs', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 5, 'fr', 'Typ d''étude', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 6, 'fr', 'Espèces investigées', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 7, 'fr', 'Durée de l''exposition', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_class_tr VALUES (DEFAULT, 8, 'fr', 'Site d''exposition', '', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code VALUES('1F', 1, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1A', 1, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1B', 1, 3, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1C', 1, 4, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1D', 1, 5, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1E', 1, 6, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1G', 1, 7, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1H', 1, 8, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1I', 1, 9, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1K', 1, 10, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1L', 1, 11, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1M', 1, 12, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1N', 1, 13, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1P', 1, 14, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1R', 1, 15, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1S', 1, 16, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1T', 1, 17, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1U', 1, 18, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1V', 1, 19, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1Y', 1, 20, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1Z', 1, 21, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code VALUES('2R', 2, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('2N', 2, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code VALUES('3A', 3, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3B', 3, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3C', 3, 3, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3H', 3, 4, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3D', 3, 5, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3E', 3, 6, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3F', 3, 7, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3G', 3, 8, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3I', 3, 9, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3O', 3, 10, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3K', 3, 11, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3L', 3, 12, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3U', 3, 13, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3Z', 3, 14, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code VALUES('4A', 4, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4B', 4, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4C', 4, 3, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4D', 4, 4, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4E', 4, 5, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4F', 4, 6, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4G', 4, 7, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4H', 4, 8, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4I', 4, 9, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4K', 4, 10, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4M', 4, 11, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4N', 4, 12, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4O', 4, 13, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4L', 4, 14, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4U', 4, 15, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4X', 4, 16, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4Y', 4, 17, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4Z', 4, 18, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code VALUES('5A', 5, 1, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5B', 5, 2, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5C', 5, 3, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5E', 5, 4, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5F', 5, 5, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5G', 5, 6, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5H', 5, 7, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5I', 5, 8, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5M', 5, 9, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5S', 5, 10, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5D', 5, 11, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5K', 5, 12, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5U', 5, 13, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code VALUES('6M', 6, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6T', 6, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6O', 6, 3, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6P', 6, 4, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6Z', 6, 5, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code VALUES('7K', 7, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7L', 7, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7M', 7, 3, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7Z', 7, 4, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code VALUES('8I', 8, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8O', 8, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8P', 8, 3, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8W', 8, 4, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8Z', 8, 5, 1      , DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);


INSERT INTO code_tr VALUES(DEFAULT, '1F', 'de', 'Feinstaub, Partikel', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1A', 'de', 'Ozon, Oxidantien', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1B', 'de', 'Stickoxide, Ammoniak', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1C', 'de', 'Kohlenwasserstoffe, nicht chloriert, VOC', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1D', 'de', 'Kohlenmonoxid', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1E', 'de', 'SO2, Schwefelverbindungen,', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1G', 'de', 'Metalle', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1H', 'de', 'Halogene, halogenierte Stoffe', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1I', 'de', 'Saure Aerosole', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1K', 'de', 'Radioaktive Stoffe', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1L', 'de', 'Asbest, Fasern', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1M', 'de', 'Tabakrauch, Passivrauchen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1N', 'de', 'Keine Messung, andere Art der Exposure (Stadt : Land) z.B. Lärm', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1P', 'de', 'Pollen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1R', 'de', 'Feuchtigkeit in der Raumluft', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1S', 'de', 'Milben, Allergene in den Raumluft', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1T', 'de', 'Temperatur', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1U', 'de', 'Andere / Übrige', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1V', 'de', 'Verkehr', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Y', 'de', 'Gerüche', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Z', 'de', 'Mehr als 8 Schadstoffe, alle gemeinsam', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '2R', 'de', 'Europa', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '2N', 'de', 'Übrige Länder', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '3A', 'de', 'Säuglinge, Vorschulkinder', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3B', 'de', 'Schulkinder und Jugendliche', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3C', 'de', 'Erwachsene (alle)', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3H', 'de', 'Betagte Personen (65+ Jahre)', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3D', 'de', 'Personen mit Asthma oder Atemwegsallergie', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3E', 'de', 'Pers. mit anderer chronischer Lungenkrankheit, COPD', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3F', 'de', 'Registerdaten, Patienten', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3G', 'de', 'Schwangere Frauen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3I', 'de', 'Sportler/innen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3O', 'de', 'Beruflich Exponierte', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3K', 'de', 'Gewebe', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3L', 'de', 'Zellen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3U', 'de', 'Übrige / Andere', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3Z', 'de', 'Mehr als 8 Teilnehmergruppen / alle', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '4A', 'de', 'Sterblichkeit', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4B', 'de', 'Lungenfunktion', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4C', 'de', 'Symptome', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4D', 'de', 'Biochemische oder zelluläre Veränderungen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4E', 'de', 'Atemwegskrankheiten akut', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4F', 'de', 'Atemwegskrankheiten chronisch', 'wie COPD/Asthma', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4G', 'de', 'Krebs', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4H', 'de', 'Herz, Kreislauf, Blut', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4I', 'de', 'Gehirn, Nerven, Augen, Ohren', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4K', 'de', 'Belästigung, psychische Folgen, Lebensqualität', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4M', 'de', 'Medikamentenkonsum, Konsultation, Spitaleintritt', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4N', 'de', 'Fertilität, Schwangerschaftsproblem, Abort', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4O', 'de', 'Frühgeburt, Säuglingsgewicht, pränatale Entwicklung', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4L', 'de', 'Missbildungen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4U', 'de', 'Übrige / Andere', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4X', 'de', 'Kosten', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Y', 'de', 'Absenzen, eingeschränkte Aktivität', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Z', 'de', 'Mehr als 8 ges. Zielgrössen / Alle', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '5A', 'de', 'Experimentelle Studie unter Belastung / Arbeit', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5B', 'de', 'Experimentelle Studie in Ruhe', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5C', 'de', 'Experimentelle Studie Zellstudie etc.', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5E', 'de', 'Einmalige Exposition, Unfall, Brand', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5F', 'de', 'Zeitreihen, Panel, kurzfristige Längsstudie', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5G', 'de', 'Querschnitt, Fall-Kontrollstudie, deskriptiv', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5H', 'de', 'Kohortenstudie', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5I', 'de', 'Interventionsstudie', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5M', 'de', 'Messstudie, Studienmethodik', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5S', 'de', 'Statistik', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5D', 'de', 'Einzelfallbeschreibung, Fallstudie', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5K', 'de', 'Krankheitsbeschreibung', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5U', 'de', 'Übersichten, Metaanalysen, HIA', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '6M', 'de', 'Mensch', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6T', 'de', 'Tier', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6O', 'de', 'Viren, Bakterien, Einzeller', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6P', 'de', 'Pflanzen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6Z', 'de', 'Alle / allg. Übersichten', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '7K', 'de', 'Kurzfristig', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7L', 'de', 'Langfristig', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7M', 'de', 'Mittelfristig', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7Z', 'de', 'Alle', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '8I', 'de', 'Raumluft', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8O', 'de', 'Aussenluft', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8P', 'de', 'Individuelle Messung', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8W', 'de', 'Berufliche Exposition', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8Z', 'de', 'Alle', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);


INSERT INTO code_tr VALUES(DEFAULT, '1F', 'en', 'Particles, Particulate Matter', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1A', 'en', 'Ozone, Oxidants', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1B', 'en', 'Nitrogen Oxides, Ammonia', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1C', 'en', 'Hydrocarbons, non-chlorinated, VOC', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1D', 'en', 'Carbonmonoxide', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1E', 'en', 'SO2, sulfur compounds', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1G', 'en', 'Metals', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1H', 'en', 'Halogens, halogenated substances', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1I', 'en', 'Acidic Aerosols', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1K', 'en', 'Radioactive substances', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1L', 'en', 'Asbestos, fibres', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1M', 'en', 'Tobacco smoke, passive smoking', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1N', 'en', 'Different exposure or assessment (geographic, noise)', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1P', 'en', 'Pollen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1R', 'en', 'Indoor air humidity', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1S', 'en', 'Mites, indoor allergens', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1T', 'en', 'Temperature', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1U', 'en', 'Others', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1V', 'en', 'Traffic', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Y', 'en', 'Odours', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Z', 'en', 'More than 8 pollutants, all together', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '2R', 'en', 'Europe', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '2N', 'en', 'Other countries', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '3A', 'en', 'Newborns, infants, pre-school children', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3B', 'en', 'School children, adolescents', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3C', 'en', 'Adults (19+)', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3H', 'en', 'Agend persons (65+)', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3D', 'en', 'People with asthma or respiratory allergies', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3E', 'en', 'People with chronic respiratory disease, COPD', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3F', 'en', 'Registry data, patient records', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3G', 'en', 'Pregnant women', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3I', 'en', 'Athletes', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3O', 'en', 'Ocupationally exposed', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3K', 'en', 'Tissue', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3L', 'en', 'Cells', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3U', 'en', 'Others', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3Z', 'en', 'More than 8 participant groups / all', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '4A', 'en', 'Mortality', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4B', 'en', 'Lung function, pulmonary function', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4C', 'en', 'Symptoms', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4D', 'en', 'Biochemical or cellular responses', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4E', 'en', 'Respiratory diseases acute', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4F', 'en', 'Respiratory diseases chronic', 'like COPD/Asthma', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4G', 'en', 'Cancer', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4H', 'en', 'Heart, cardovascular system, blood', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4I', 'en', 'Brain, nervous system, eyes, ears', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4K', 'en', 'Annoyance, well-being, quality of life', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4M', 'en', 'Drug intake, consultation, hospital admission', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4N', 'en', 'Fertility, pregnancy complications, miscarriage', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4O', 'en', 'Preterm birth, birth weight, prenatal development', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4L', 'en', 'Congenital malformation', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4U', 'en', 'Others', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4X', 'en', 'Costs', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Y', 'en', 'Absenteeism, restricted activity', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Z', 'en', 'More than 8 health outcomes / all', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '5A', 'en', 'Experimental study under exercising conditions', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5B', 'en', 'Experimental study under resting conditions', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5C', 'en', 'Experimental study with cells, tissues', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5E', 'en', 'Accidental release, fire', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5F', 'en', 'Panel study,  time series', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5G', 'en', 'Cross-sectional or case-control study, descriptive', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5H', 'en', 'Cohort Study', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5I', 'en', 'Intervention', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5M', 'en', 'Methodological study, Exposure assessment, modelling', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5S', 'en', 'Statistics', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5D', 'en', 'Case study', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5U', 'en', 'Review, meta-analysis', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '6M', 'en', 'Humans', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6T', 'en', 'Animals', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6O', 'en', 'Viruses, Bacteria, Protozoa', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6P', 'en', 'Plants', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6Z', 'en', 'All / General Overviews', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '7K', 'en', 'Short-term', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7L', 'en', 'Long-term', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7M', 'en', 'Mid-term', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7Z', 'en', 'All', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '8I', 'en', 'Indoor Air', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8O', 'en', 'Ambient air', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8P', 'en', 'Personal measurement', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8W', 'en', 'Occupational exposure', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8Z', 'en', 'All', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);


INSERT INTO code_tr VALUES(DEFAULT, '1F', 'fr', 'Poussière fines, particules', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1A', 'fr', 'Ozone, Oxydants', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1B', 'fr', 'Oxydes d''azote, ammoniac', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1C', 'fr', 'Hydrocarbures non-chlorurés', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1D', 'fr', 'Monoxyde de carbone', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1E', 'fr', 'SO2, composé de soufre', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1G', 'fr', 'Métals', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1H', 'fr', 'Halogènes, substances halogénées', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1I', 'fr', 'Aérosols acides', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1K', 'fr', 'Substances radioactives', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1L', 'fr', 'Amiante, fibres', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1M', 'fr', 'Tabagisme passif, fumée de cigarette', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1P', 'fr', 'Pollen', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1R', 'fr', 'Humidité (air intérieur)', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1S', 'fr', 'Allergènes dans l''air intérieur', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1T', 'fr', 'Température', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1U', 'fr', 'Autres', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1V', 'fr', 'Trafic / circulation', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Y', 'fr', 'Odeur', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '1Z', 'fr', 'Plus de 8 polluants / tous', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '2R', 'fr', 'Europe', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '2N', 'fr', 'Autres pays', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '3A', 'fr', 'Nourisson, infants, enfants préscolaires', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3B', 'fr', 'Enfants scolaires, adolescents', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3C', 'fr', 'Adultes (19+)', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3H', 'fr', 'Personnes agées (65+)', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3D', 'fr', 'Personnes asthmatiques ou allérgiques', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3E', 'fr', 'Pers. souffrantes des maladies pulmonaire chronique', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3F', 'fr', 'Données de registre ou des patients', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3G', 'fr', 'Femmes enceintes', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3I', 'fr', 'Sportifs', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3O', 'fr', 'Personnes exposées au travail', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3K', 'fr', 'Tissus', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3L', 'fr', 'Cellules', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3U', 'fr', 'Autres', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '3Z', 'fr', 'Plus de 8 / tous', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '4A', 'fr', 'Mortalité', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4B', 'fr', 'Fonction pulmonaire', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4C', 'fr', 'Symptômes, Signes', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4D', 'fr', 'Changements biochimiques ou cellulaire', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4E', 'fr', 'Maladie respiratoire, imminante', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4F', 'fr', 'Maladie respiratoire, chronique', 'comme COPD/Asthma', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4G', 'fr', 'Cancer', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4H', 'fr', 'Coeur, circulation, sang', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4I', 'fr', 'Système nerveux, yeux, oreilles', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4K', 'fr', 'Nuisance, bien-être, qualité de vie', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4M', 'fr', 'Médicaments, visite médicale, hospitalisation', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4N', 'fr', 'Fertilité, complication de grossesse, fausse couche', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4O', 'fr', 'Naissance prématurée, poids de naissance ', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4L', 'fr', 'Malformation', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4U', 'fr', 'Autres', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4X', 'fr', 'Frais / coûts', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Y', 'fr', 'Absentéisme, activités réduites', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '4Z', 'fr', 'Plus de 8 effets / tous', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '5A', 'fr', 'Etude expérimentale dans des conditions exercicantes', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5B', 'fr', 'Etude expérimentale dans des conditions sédentaires', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5C', 'fr', 'Etude expérimentale avec cellules etc.', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5E', 'fr', 'Accident, incident chimique, feu', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5F', 'fr', 'Etude panel, étude longitudinale à court terme, séries chronologiques', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5G', 'fr', 'Etude transversal ou cas-témoin, descriptive', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5H', 'fr', 'Etude de cohorte', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5I', 'fr', 'Intervention', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5M', 'fr', 'Méthodologie', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5S', 'fr', 'Statistique ', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5D', 'fr', 'Étude de cas', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '5U', 'fr', 'Review, révue, méta-analyse', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '6M', 'fr', 'Hommes', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6T', 'fr', 'Animaux', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6O', 'fr', 'Virus, Bactéries, Protozoaires', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6P', 'fr', 'Plantes', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '6Z', 'fr', 'Tous / Exposés gen.', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '7K', 'fr', 'à court terme', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7L', 'fr', 'à long terme', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7M', 'fr', 'à moyen terme', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '7Z', 'fr', 'tous', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO code_tr VALUES(DEFAULT, '8I', 'fr', 'Air intérieur', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8O', 'fr', 'Air extérieur', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8P', 'fr', 'Mesures personalisées', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8W', 'fr', 'Exposition lié au travail', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO code_tr VALUES(DEFAULT, '8Z', 'fr', 'Tous', null, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);


INSERT INTO paper_code VALUES(1, '1F', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '2N', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '3C', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '4G', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '5H', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '5S', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '6M', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '7L', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO paper_code VALUES(1, '8O', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);


INSERT INTO user VALUES(1, 'testuser', 'user', 'test', 'testuser@sipamato.org', '$2a$04$ENEpFnWzYVveVg3FPdkDdeucUwdVn4nxu4IP2Zj1bFXiPvLQED6.C', true, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO user VALUES(2, 'admin', 'admin', 'admin', 'admin@sipamato.org', '$2a$04$oOL75tgCf3kXdr6vO5gagu6sIUZWfXyEhZHmDd4LpGvOPTaO5xEoO', true, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO user VALUES(3, 'mkj', 'm', 'k j', 'mkj@sipamato.org', '$2a$04$9n.7Iv3eBNU6gqq5cOg8X.QRFBrDbzUOW/wBq5KdHKjOeVt4r0jwG', true, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO user VALUES(4, 'rk', 'r', 'k', 'rk@sipamato.org', '$2a$04$UrFcx9Rqadw5JKkJwDpF8unmfubbgna8IZT6FsqW0uEoSd8kMs8.6', true, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO user_role VALUES(1, 1, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO user_role VALUES(2, 2, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO user_role VALUES(3, 3, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO user_role VALUES(4, 3, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO user_role VALUES(5, 4, 1, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO user_role VALUES(6, 4, 2, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);


INSERT INTO search_order VALUES(1, 2, 0, DEFAULT, DEFAULT, 2, DEFAULT, 2);
    INSERT INTO search_condition VALUES(1, 1, DEFAULT, DEFAULT, 2, DEFAULT, 2);
        INSERT INTO search_term VALUES(1, 1, 2, 'authors', 'kutlar', DEFAULT, DEFAULT, 2, DEFAULT, 2);
    INSERT INTO search_condition VALUES(2, 1, DEFAULT, DEFAULT, 2, DEFAULT, 2);
		INSERT INTO search_term VALUES(2, 2, 1, 'publication_year', '2014-2015', DEFAULT, DEFAULT, 2, DEFAULT, 2);
		INSERT INTO search_term VALUES(3, 2, 2, 'authors', 'turner', DEFAULT, DEFAULT, 2, DEFAULT, 2);
INSERT INTO search_order VALUES(2, 3, 1, DEFAULT, DEFAULT, 3, DEFAULT, 3);
    INSERT INTO search_exclusion VALUES(1, 2, 3, NULL, DEFAULT, DEFAULT, 4, DEFAULT, 4);
    INSERT INTO search_condition VALUES(3, 2, DEFAULT, DEFAULT, 3, DEFAULT, 3);
		INSERT INTO search_term VALUES(4, 3, 2, 'goals', 'pm2.5', DEFAULT, DEFAULT, 3, DEFAULT, 3);
INSERT INTO search_order VALUES(3, 4, 0, DEFAULT, DEFAULT, 4, DEFAULT, 4);
	INSERT INTO search_condition VALUES(4, 3, DEFAULT, DEFAULT, 4, DEFAULT, 4);
        INSERT INTO search_term VALUES(5, 4, 0, 'first_author_overridden', 'true', DEFAULT, DEFAULT, 4, DEFAULT, 4);
        INSERT INTO search_condition_code VALUES(4, '1F', DEFAULT, DEFAULT, 4, DEFAULT, 4);
INSERT INTO search_order VALUES(4, 4, 1, DEFAULT, DEFAULT, 4, DEFAULT, 4);
    INSERT INTO search_exclusion VALUES(2, 4, 1, NULL, DEFAULT, DEFAULT, 4, DEFAULT, 4);
	INSERT INTO search_condition VALUES(5, 4, DEFAULT, DEFAULT, 4, DEFAULT, 4);
        INSERT INTO search_term VALUES(6, 5, 0, 'first_author_overridden', 'false', DEFAULT, DEFAULT, 4, DEFAULT, 4);
        INSERT INTO search_condition_code VALUES(5, '1F', DEFAULT, DEFAULT, 4, DEFAULT, 4);

