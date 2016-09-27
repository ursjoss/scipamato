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
    , '', '', '' -- exposure, exposure_pollutant, exposure_assessment
    , '' -- methods
    , '', '', '' -- method_outcome, method_statistics, method_confounders
    , '' -- result
    , '', ''  -- result_exposure_range, result_effect_estimate
    , '', '' -- comment, intern
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
    , '', '', ''
    , 'Da nur bis 1998 individuelle Informationen über das Rauchverhalten vorlagen, wurden nur die ersten 6 Studienjahre in diese Analyse einbzeogen. Die Abschätzung der Belastung mit Feinstaub wurde mit Landnutzungsmodellen für die geocodierten Adressen bei Studieneintritt vorgenommen, welche sich auf Monatsmittelwerte von PM2.5 der Jahre 1999-2004 von 1464 Messstationen abstützten, unter der Annahme, dass die Feinstaubbelastungen über die Jahre eng korreliert seien. Mit proportionalen Hazard-Modellen nach Cox, stratifiziert für Alter, Geschlecht und Rasse wurde das Überleben bzw. die Sterblichkeit an Lungenkrebs in den ersten 6 Jahren in Abhängigkeit der PM2.5-Belastung in verschiedenen Kategorien (über/unter der 50 Perzentile, über der 66. vs. unter der 33. Perzentile, sowie über der 75. vs. unter der 25. Perzentile) und in Abhängigkeit von Rauchen/nicht Rauchen modelliert. Einbezogen wurden folgende invidivuellen Faktoren: Schulbildung, Zivilstand, BMI, Passivrauchen, Ernährung, Alkoholkonsum und berufliche Belastung. Die Effektmodifikation bezüglich Lungenkrebsterblichkeit wurde mit drei Grössen untersucht: das relative zusätzliche Risiko durch die Interatkion (RERI), der der Interaktion anrechenbare Teil des Risikos (AP) und der Synergie-Index (SI). Lungenkrebs, Kohortenstudie, Statistik, epidemiologische Methoden. ACS-Studie. USA.'
    , '', '', ''
    , 'In 2''509''717 Personen-Jahren der Nachkontrolle ereigneten sich 1921 Todesfälle an Lungenkrebs. Die geschätzte Feinstaubbelastung lag im Durchschnitt bei 12.6 SD 2.85 µg PM2.5/m3, mit der 25. und 75. Perzentile bei 10.59 und 14.44 µg PM2.5/m3. Raucher hatten im Vergleich zu Nichtrauchern ein 13.5 fach erhöhtes Risiko (95%CI 10.2-17.9), an Lungenkrebs zu sterben, wenn ihre PM2.5-Belastung gering war, d.h. unter der 25. Perzentile lag. Nichtraucher hatten ein 1.28 faches Risiko (0.92-1.78), wenn ihre Belastung über der 75. Perzentile der PM2.5-Belastung lag, im Vergleich zu Nichtrauchern mit geringer Belastung. Raucher hatten ein 16 faches Risiko (12.1-21.1), an Lungenkrebs zu sterben, wenn ihre Feinstaubbelastung über der 75. Perzentile lag. Das zusätzliche relative Risiko durch die Interaktion (RERI) für die Kombination von Rauchen und schlechter Luft betrug 2.19 (-0.10;+4.83). Der Risikoanteil, der dem Kombinationseffekt angerechnet werden kann, betrug 14%, der Synergie-Index 1.17. Die Autoren schliessen daraus, dass die Folgen von Rauchen und Luftverschmutzung stärker zusammenwiren als nur additiv. Auch wenn die Lungenkrebfälle am stärksten durch einen Rückgang des Rauchens abnehmen, kann ein solcher Rückgang mit einer Verbesserung der Luftqualität stärker ausfallen als mit einer der beiden Massnahmen allein.'
    , '', ''
    , 'Kommentar von Panagiotou AO, Wacholder S: How Big Is That Interaction (in My Community)-and I. Which Direction? Am. J. Epidemiol. 2014 180: 1150-1158.', ''
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
    , '', '', '' -- exposure, exposure_pollutant, exposure_assessment
    , 'Die Verkalkung der Herzkranzgefässe CAC wurde mit Elektronenstrahl-Computertomographie untersucht und die Agatstonpunktzahl als Zielgrösse definiert. Ein Wert von mehr als Null wurde als Verkalkung interpretiert. Informationen zu weiteren Gesundheitsgrössen der Patienten stammten aus den aktuellsten Ergebnissen der 4 jährlichen Gesundheitsuntersuchungen.\\nAls Belastung mit Verkehr wurde die Distanz der Adresse zum Zeitpunkt der ersten Untersuchung zur nächsten Autobahn oder einer anderen grösseren Verkehrsachse (Klassen A1-A3, Entfernung von max. 50, 50-200, 200-400 und 400-1000m) definiert. Ausserdem wurde die Feinstaubbelastung mit einem Landnutzungsmodell für PM2.5 für das Jahr 2003 und 2003-2009 bestimmt, das auf Satellitendaten mit einer Auflösung von 1 x 1 km basierte.\\nDer Zusammenhang der Koronarverkalkung (binärer CAC-Score ja/nein, kontinuierlich) mit der Strassennähe oder der Feinstaubbelastung wurde einerseits mit verallgemeinerten Schätzgleichungen mit logit Link andererseits mit gemischten linearen Modellen untersucht. Der Zusammenhang der fortschreitenden Verkalkung (1. zur 2. Untersuchung) wurde mit logistischer Regression und mit gemischten linearen Modellen untersucht. In alle Modelle wurden Alter, Geschlecht, BMI, Rauchen, Bildung, %Hausbesitzer auf Quartierebenen als Variable für den sozioökonomischen Status, Kohorte (Enkeln oder Kinder der Ursprungskohorte), Datum der CAC Untersuchung und Abstand zur Gesundheitsuntersuchung (Tage) einbezogen. Sensitivitätsanalysen mit weiteren Störgrössen (Diabetes, Bewegungsverhalten, hoher Blutdruck) und Interaktionen wurden durchgeführt und es wurde versucht, eine nicht-lineare Dosis-Wirkungsbeziehung zu beschreiben.\\nHerz-/Kreislaufkrankheiten, Arteriosklerose, Framingham. Kohortenstudie. USA.' -- methods
    , '', '', '' -- method_outcome, method_statistics, method_confounders
    , 'Die Teilnehmer wohnten median 201m (Interquartilabstand IQR 359m) von einer grossen Strasse (USA: Kategorie A1-A3) entfernt und waren im Jahr 2003 mit median 10.7 (IQR 1.4) µg/m3 PM2.5 belastet. Bei 45% der Teilnehmer wurde Verkalkungen der Herzkranzgefässe festgestellt, bei der zweiten Untersuchung waren es 50%. Der mediane Agatston Score betrug bei Erstuntersuchung 70.5 IQR 278.5, bei der zweiten Untersuchung nach durchschnittlich 6.1 Jahren 137.1 (419.4).\\nPersonen in der Nähe von Autobahnen oder mit höheren Feinstaubbelastung hatten kein erhöhtes Risiko für einen messbaren CAC-Score, also einer Verkalkung der Herzkranzgefässe, das Risiko war tendenziell eher (paradox) reduziert, aber mit weiten Konfidenzintervallen: z.B. OR 0.95 (95%-CI: 0.74-1.22) für Personen die weniger als 50m von einer Autobahnentfernt wohnten gegenüber denen, die über 400m entfernt wohnten. Es konnte auch kein Zusammenhang mit der Höhe des Scores und der Belastung gefunden werden. Das Risiko für fortschreitende Koronararterienverkalkung hing ebenfalls nicht mit der Verkehrsnähe oder der Feinstaubbelastung zusammen. Sie hing paradox mit der logarithmierten Entfernung zur Strasse zusammen; steigender CAC-Agatstonscore von 2.2 (0.1-4.3) pro Jahr pro logarithmierter IQR-Entfernung zur Strasse. Die Ergebnisse blieben auch in den Sensitivitätsanalysen robust.\\nDie Autoren folgern, dass ihre Ergebnisse nicht für einen engen Zusammenhang der Koronarverkalkung mit der Verkehrs- oder Feinstaubbelastung sprechen, betonen aber, dass die Belastung generell tief war und wenig variierte.' -- result
    , '', ''  -- result_exposure_range, result_effect_estimate
    , '', '' -- comment, intern
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
    , '', '', '' -- exposure, exposure_pollutant, exposure_assessment
    , 'In der Nachfolgezeit von 1988-2006 wurde die Inzidenz von erstmaligen Herz-/Kreislaufkrankheiten, nicht tödlicher oder tödlicher koronarer Herzerkrankung und Hirnschlag untersucht. Die Informationen stammten aus den Fragebogen und wurde mit Krankengeschichten, Autopsie und Todesurkunde überprüft. Das Luftqualitätsmessnetz der US-EPA, das Projekt IMPROVE und verschiedene weitere Studien lieferten monatliche Messdaten für PM10, ab 1999 auch PM2.5. Für die Jahre vor 1999 wurde PM2.5 aus PM10 abgeleitet unter Verwendung des PM10/PM2.5 Verhältnisses aus einem Modell, das auf Daten der Zeit nach 1999 basierte. Die Belastung mit der gröberen Fraktion PM10-2.5 wurde aus der Differenz von PM10 und PM2.5 berechnet. Mit einem Landnutzungsmodell wurde den geocodierten Adressen (basierend auf der Postleitzahl) die Belastung kumuliert über 18 Jahre (1988-2006) zugeordnet.\\nMit proportionalen Hazard-Modellen nach Cox wurde die Inzidenz an Herz-/Kreislaufkrankheiten, koronaren Herzerkrankungen und Hirnschlag in Abhängigkeit der Feinstaubbelastung untersucht. In einem Grundmodell wurden Alter, Jahr, Jahreszeit und geografische Region und im erweiterten Modell zusätzlich Rasse, BMI, Wechseljahrstatus, Hormoneinnahme, Bluthochdruck, Hypercholesterinämie, Diabetes, familiäre Anamnese von Herzinfarkt, Rauchen, als Indikator für den Sozialstatus die Schulbildung, Zivilstand und Beruf des Partners und der Sozialstatus im Quartier einbezogen. Ausserdem wurde die Interaktion mit dem Alter, Diabetes, Familienanamnese von Herzinfarkt und Rauchen geprüft. Sensitivitätsanalyen zur Überprüfung der Robusheit wurden durchgeführt. Kohortenstudie. Nurses Health. USA.' -- methods
    , '', '', '' -- method_outcome, method_statistics, method_confounders
    , '6''767 Frauen erkrankten an Herz-Kreislauferkrankungen, 3''878 an  koronaren  Herzkrankheiten und 3''295 erlitten einen Hirnschlag. Die Frauen waren durchschnittlich 68.6 (7.3) Jahre alt, überwiegend weiss und verheiratet. Die durchschnittliche jährliche Belastung und die Standardabweichung (SD) betrug 22.2 (6.5) µg PM10/m3, 8.7 (4.5) µg PM2.5-10/m3 und 13.4 (3.3) µg PM2.5/m3.\\nIm Grundmodell nahm das Risiko bei einem Anstieg von 10 µg/m3 aller Feinstaubfraktionen gering, aber statistisch nicht signifikant zu. Lediglich für einen Anstieg von PM10 und Herz-/Kreislaufkrankheiten und für PM2.5-10 mit allen drei Zielgrössen gab es einen signifikanten Zusammenhang. Die Effektschätzer nahmen jedoch unter Einbezug aller Störfaktoren ab und waren nicht mehr signifikant.\\nDie Inzidenz von Herz-/Kreislaufkrankheiten und Hirnschlag nahm jedoch bei Frauen mit Diabetes gegenüber Frauen ohne Diabetes für PM10, PM2.5-10 und PM2.5 zu mit HR 1.19 (95%CI: 1.1-1.28), 1.17 (1.05-1.3) und 1.44 (1.23-1.68), resp. 1.23 (1.1-1.38), 1.18 (1.01-1.38) und 1.66 (1.31-2.1). Die Belastung mit PM10 und PM2.5-10, nicht aber mit PM2.5 war ausserdem mit einem erhöhten Risiko bei Diabetikerinnnen für koronare Herzkrankheiten mit 1.12 (1.02-1.23) und 1.14 (1-1.3) verbunden.\\nDie Autoren schliessen daraus, dass die Feinstaubbelastung bei Frauen mittleren Alters das Inzidenzrisiko für Herz-/Kreislaufkrankheiten gering, aber nicht signifikant erhöhe, die Effektschätzer aber bei Frauen mit Diabetes erhöht und signfikant waren.' -- result
    , '', ''  -- result_exposure_range, result_effect_estimate
    , '', '' -- comment, intern
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
	, 'exp', 'exp_pollutant', 'exp assessment'
	, 'methods', 'method outcome', 'method statistics', 'method confounder'
	, 'result', 'result exposure range', 'result effect estimate'
	, 'comment', 'int'
	, 1, current_timestamp()
);
