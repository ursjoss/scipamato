package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.Tables.PAPER;
import static ch.difty.sipamato.persistance.jooq.TestDbConstants.MAX_ID_PREPOPULATED;
import static ch.difty.sipamato.persistance.jooq.TestDbConstants.RECORD_COUNT_PREPOPULATED;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.Paper;

/**
 * Note: The test will insert some records into the DB. It will try to wipe those records after the test suite terminates.
 *
 * If however, the number of records in the db does not match with the defined constants a few lines further down, the 
 * additional records in the db would be wiped out by the tearDown method. So please make sure the number of records (plus
 * the highest id) match the declarations further down.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
@ActiveProfiles({ "DB_JOOQ" })
public class JooqPaperRepoIntegrationTest {

    // @formatter:off
    private static final String PAPER1_WO_CODE_CLASSES =
            "Paper[id=1,doi=10.1093/aje/kwu275,pmId=25395026"
            + ",authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewski D, Beckermann BS, Samet, JM.,firstAuthor=Turner,firstAuthorOverridden=false"
            + ",title=Interactions Between Cigarette Smoking and Fine Particulate Matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II."
            + ",location=Am J Epidemiol 2014 15; 180 (12) 1145-1149."
            + ",publicationYear=2014,goals=Neue Analyse der Daten der amerikanischen Krebspräventions-Kohertenstudie zur Untersuchung, wie gross das kombinierte Krebsrisiko durch Feinstaub ist."
            + ",population=429'406 Teilnehmer, Frauen und Männer aus 50 Staaten der USA, welche in den Jahren 1982/1983 im Alter von mindestens 30 Jahren für die Krebsvorsorgestudie der amerikanischen Kriegsgesellschaft (ACS) rekrutiert worden waren, in den Jahren 1984, 1986 und 1988 wieder kontaktiert worden waren, und seit 1989 mit dem nationalen Sterberegister auf ihr Überleben kontaktiert wurden. Nicht in diese Analyse einbezogen wurden Exrauchende und Pfeifen- oder Zigarrenraucher. USA.,populationPlace=,populationParticipants=,populationDuration=,exposurePollutant=,exposureAssessment=,methods=Da nur bis 1998 individuelle Informationen über das Rauchverhalten vorlagen, wurden nur die ersten 6 Studienjahre in diese Analyse einbzeogen. Die Abschätzung der Belastung mit Feinstaub wurde mit Landnutzungsmodellen für die geocodierten Adressen bei Studieneintritt vorgenommen, welche sich auf Monatsmittelwerte von PM2.5 der Jahre 1999-2004 von 1464 Messstationen abstützten, unter der Annahme, dass die Feinstaubbelastungen über die Jahre eng korreliert seien. Mit proportionalen Hazard-Modellen nach Cox, stratifiziert für Alter, Geschlecht und Rasse wurde das Überleben bzw. die Sterblichkeit an Lungenkrebs in den ersten 6 Jahren in Abhängigkeit der PM2.5-Belastung in verschiedenen Kategorien (über/unter der 50 Perzentile, über der 66. vs. unter der 33. Perzentile, sowie über der 75. vs. unter der 25. Perzentile) und in Abhängigkeit von Rauchen/nicht Rauchen modelliert. Einbezogen wurden folgende invidivuellen Faktoren: Schulbildung, Zivilstand, BMI, Passivrauchen, Ernährung, Alkoholkonsum und berufliche Belastung. Die Effektmodifikation bezüglich Lungenkrebsterblichkeit wurde mit drei Grössen untersucht: das relative zusätzliche Risiko durch die Interatkion (RERI), der der Interaktion anrechenbare Teil des Risikos (AP) und der Synergie-Index (SI). Lungenkrebs, Kohortenstudie, Statistik, epidemiologische Methoden. ACS-Studie. USA.,methodStudyDesign=,methodOutcome=,methodStatistics=,methodConfounders=,result=In 2'509'717 Personen-Jahren der Nachkontrolle ereigneten sich 1921 Todesfälle an Lungenkrebs. Die geschätzte Feinstaubbelastung lag im Durchschnitt bei 12.6 SD 2.85 µg PM2.5/m3, mit der 25. und 75. Perzentile bei 10.59 und 14.44 µg PM2.5/m3. Raucher hatten im Vergleich zu Nichtrauchern ein 13.5 fach erhöhtes Risiko (95%CI 10.2-17.9), an Lungenkrebs zu sterben, wenn ihre PM2.5-Belastung gering war, d.h. unter der 25. Perzentile lag. Nichtraucher hatten ein 1.28 faches Risiko (0.92-1.78), wenn ihre Belastung über der 75. Perzentile der PM2.5-Belastung lag, im Vergleich zu Nichtrauchern mit geringer Belastung. Raucher hatten ein 16 faches Risiko (12.1-21.1), an Lungenkrebs zu sterben, wenn ihre Feinstaubbelastung über der 75. Perzentile lag. Das zusätzliche relative Risiko durch die Interaktion (RERI) für die Kombination von Rauchen und schlechter Luft betrug 2.19 (-0.10;+4.83). Der Risikoanteil, der dem Kombinationseffekt angerechnet werden kann, betrug 14%, der Synergie-Index 1.17. Die Autoren schliessen daraus, dass die Folgen von Rauchen und Luftverschmutzung stärker zusammenwiren als nur additiv. Auch wenn die Lungenkrebfälle am stärksten durch einen Rückgang des Rauchens abnehmen, kann ein solcher Rückgang mit einer Verbesserung der Luftqualität stärker ausfallen als mit einer der beiden Massnahmen allein."
            + ",resultExposureRange=,resultEffectEstimate=,comment=Kommentar von Panagiotou AO, Wacholder S: How Big Is That Interaction (in My Community)-and I. Which Direction? Am. J. Epidemiol. 2014 180: 1150-1158."
            + ",intern=";
    // @formatter:on

    @Autowired
    private DSLContext dsl;

    @Autowired
    private JooqPaperRepo repo;

    @After
    public void teardown() {
        // Delete all books that were created in any test
        dsl.delete(PAPER).where(PAPER.ID.gt(MAX_ID_PREPOPULATED)).execute();
    }

    @Test
    public void findingAll() {
        List<Paper> papers = repo.findAll();
        assertThat(papers).hasSize(RECORD_COUNT_PREPOPULATED);
        assertThat(papers.get(0).getId()).isEqualTo(1);
        assertThat(papers.get(1).getId()).isEqualTo(2);
        assertThat(papers.get(2).getId()).isEqualTo(3);
        assertThat(papers.get(3).getId()).isEqualTo(4);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        Paper paper = repo.findById(1l);
        paper = repo.findById((long) RECORD_COUNT_PREPOPULATED);
        assertThat(paper.getId()).isEqualTo(MAX_ID_PREPOPULATED);
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1l)).isNull();
    }

    @Test
    public void addingRecord_savesRecordAndRefreshesId() {
        Paper p = makeMinimalPaper();
        assertThat(p.getId()).isNull();

        Paper saved = repo.add(p);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        assertThat(saved.getAuthors()).isEqualTo("a");
    }

    private Paper makeMinimalPaper() {
        Paper p = new Paper();
        p.setAuthors("a");
        p.setFirstAuthor("b");
        p.setFirstAuthorOverridden(true);
        p.setTitle("t");
        p.setLocation("l");
        p.setGoals("g");
        return p;
    }

    @Test
    public void updatingRecord() {
        Paper paper = repo.add(makeMinimalPaper());
        assertThat(paper).isNotNull();
        assertThat(paper.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        final long id = paper.getId();
        assertThat(paper.getAuthors()).isEqualTo("a");

        paper.setAuthors("b");
        repo.update(paper);
        assertThat(paper.getId()).isEqualTo(id);

        Paper newCopy = repo.findById(id);
        assertThat(newCopy).isNotEqualTo(paper);
        assertThat(newCopy.getId()).isEqualTo(id);
        assertThat(newCopy.getAuthors()).isEqualTo("b");
    }

    @Test
    public void deletingRecord() {
        Paper paper = repo.add(makeMinimalPaper());
        assertThat(paper).isNotNull();
        assertThat(paper.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        final long id = paper.getId();
        assertThat(paper.getAuthors()).isEqualTo("a");

        Paper deleted = repo.delete(id);
        assertThat(deleted.getId()).isEqualTo(id);

        assertThat(repo.findById(id)).isNull();
    }

    @Test
    public void findingById_forPaper1InGerman() {
        Paper paper = repo.findById(1l);
        assertThat(paper.toString()).isEqualTo(
        // @formatter:off
           PAPER1_WO_CODE_CLASSES
           +   ",codes=["
           +      "codesOfClass1=["
           +         "Code[code=1F,name=Feinstaub, Partikel,codeClass=CodeClass[id=1]]"
           +      "],"
           +      "codesOfClass2=["
           +         "Code[code=2N,name=Übrige Länder,codeClass=CodeClass[id=2]]"
           +      "],"
           +      "codesOfClass3=["
           +         "Code[code=3C,name=Erwachsene (alle),codeClass=CodeClass[id=3]]"
           +      "],"
           +      "codesOfClass4=["
           +         "Code[code=4G,name=Krebs,codeClass=CodeClass[id=4]]"
           +      "],"
           +      "codesOfClass5=["
           +         "Code[code=5H,name=Kohortenstudie,codeClass=CodeClass[id=5]], "
           +         "Code[code=5S,name=Statistik,codeClass=CodeClass[id=5]]"
           +      "],"
           +      "codesOfClass6=["
           +         "Code[code=6M,name=Mensch,codeClass=CodeClass[id=6]]"
           +      "],"
           +      "codesOfClass7="
           +         "[Code[code=7L,name=Langfristig,codeClass=CodeClass[id=7]]"
           +      "],"
           +      "codesOfClass8=["
           +         "Code[code=8O,name=Aussenluft,codeClass=CodeClass[id=8]]"
           +      "]"
           +    "]"
           + "]");
        // @formatter:on
    }

    // TODO reactivate those tests after implementing language specific querying
//    @Test
//    public void findingWithChildrenById_forPaper1InSwissGerman_findsGerman() {
//        Paper paper = repo.findCompleteById(1l, "de_CH");
//        assertThat(paper.toString()).isEqualTo(
//        // @formatter:off
//           PAPER1_WO_CODE_CLASSES
//           +   ",codes=["
//           +      "codesOfClass1=["
//           +         "Code[code=1F,name=Feinstaub, Partikel,codeClass=CodeClass[id=1]]"
//           +      "],"
//           +      "codesOfClass2=["
//           +         "Code[code=2N,name=Alle übrigen Länder,codeClass=CodeClass[id=2]]"
//           +      "],"
//           +      "codesOfClass3=["
//           +         "Code[code=3C,name=Erwachsene (alle),codeClass=CodeClass[id=3]]"
//           +      "],"
//           +      "codesOfClass4=["
//           +         "Code[code=4G,name=Krebs,codeClass=CodeClass[id=4]]"
//           +      "],"
//           +      "codesOfClass5=["
//           +         "Code[code=5H,name=Kohortenstudie,codeClass=CodeClass[id=5]], "
//           +         "Code[code=5S,name=Statistik,codeClass=CodeClass[id=5]]"
//           +      "],"
//           +      "codesOfClass6=["
//           +         "Code[code=6M,name=Mensch,codeClass=CodeClass[id=6]]"
//           +      "],"
//           +      "codesOfClass7="
//           +         "[Code[code=7L,name=Langfristig (1/2 Jahr – Jahre),codeClass=CodeClass[id=7]]"
//           +      "],"
//           +      "codesOfClass8=["
//           +         "Code[code=8O,name=Aussenluft,codeClass=CodeClass[id=8]]"
//           +      "]"
//           +    "]"
//           + "]");
//        // @formatter:on
//    }
//
//    @Test
//    public void findingWithChildrenById_forPaper1InEnglish() {
//        Paper paper = repo.findCompleteById(1l, "en");
//        assertThat(paper.toString()).isEqualTo(
//        // @formatter:off
//           PAPER1_WO_CODE_CLASSES
//           +   ",codes=["
//           +      "codesOfClass1=["
//           +         "Code[code=1F,name=Particles, Particulate Matter,codeClass=CodeClass[id=1]]"
//           +      "],"
//           +      "codesOfClass2=["
//           +         "Code[code=2N,name=not translated,codeClass=CodeClass[id=2]]"
//           +      "],"
//           +      "codesOfClass3=["
//           +         "Code[code=3C,name=not translated,codeClass=CodeClass[id=3]]"
//           +      "],"
//           +      "codesOfClass4=["
//           +         "Code[code=4G,name=not translated,codeClass=CodeClass[id=4]]"
//           +      "],"
//           +      "codesOfClass5=["
//           +         "Code[code=5H,name=not translated,codeClass=CodeClass[id=5]], "
//           +         "Code[code=5S,name=not translated,codeClass=CodeClass[id=5]]"
//           +      "],"
//           +      "codesOfClass6=["
//           +         "Code[code=6M,name=not translated,codeClass=CodeClass[id=6]]"
//           +      "],"
//           +      "codesOfClass7=["
//           +          "Code[code=7L,name=not translated,codeClass=CodeClass[id=7]]"
//           +      "],"
//           +      "codesOfClass8=["
//           +          "Code[code=8O,name=not translated,codeClass=CodeClass[id=8]]"
//           +      "]"
//           +    "]"
//           + "]");
//        // @formatter:on
//    }
//
//    @Test
//    public void findingWithChildrenById_forPaper1InNonExistingLanguage() {
//        Paper paper = repo.findCompleteById(1l, "xy");
//        assertThat(paper.toString()).isEqualTo(
//        // @formatter:off
//            PAPER1_WO_CODE_CLASSES
//            +   ",codes=["
//            +      "codesOfClass1=["
//            +        "Code[code=1F,name=not translated,codeClass=CodeClass[id=1]]"
//            +      "],"
//            +      "codesOfClass2=["
//            +         "Code[code=2N,name=not translated,codeClass=CodeClass[id=2]]"
//            +      "],"
//            +      "codesOfClass3=["
//            +         "Code[code=3C,name=not translated,codeClass=CodeClass[id=3]]"
//            +      "],"
//            +      "codesOfClass4=["
//            +         "Code[code=4G,name=not translated,codeClass=CodeClass[id=4]]"
//            +      "],"
//            +      "codesOfClass5=["
//            +        "Code[code=5H,name=not translated,codeClass=CodeClass[id=5]], "
//            +        "Code[code=5S,name=not translated,codeClass=CodeClass[id=5]]"
//            +      "],"
//            +      "codesOfClass6=["
//            +         "Code[code=6M,name=not translated,codeClass=CodeClass[id=6]]"
//            +      "],"
//            +      "codesOfClass7=["
//            +          "Code[code=7L,name=not translated,codeClass=CodeClass[id=7]]"
//            +      "],"
//            +      "codesOfClass8=["
//            +          "Code[code=8O,name=not translated,codeClass=CodeClass[id=8]]"
//            +      "]"
//            +    "]"
//            + "]");
//        // @formatter:on
//    }

    // TODO test findByExpression

}
