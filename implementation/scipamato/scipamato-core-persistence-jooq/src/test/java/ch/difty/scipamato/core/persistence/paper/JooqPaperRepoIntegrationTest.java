package ch.difty.scipamato.core.persistence.paper;

import static ch.difty.scipamato.core.db.Tables.PAPER_ATTACHMENT;
import static ch.difty.scipamato.core.persistence.TestDbConstants.MAX_ID_PREPOPULATED;
import static ch.difty.scipamato.core.persistence.TestDbConstants.RECORD_COUNT_PREPOPULATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.core.db.tables.Code;
import ch.difty.scipamato.core.db.tables.CodeClass;
import ch.difty.scipamato.core.db.tables.SearchExclusion;
import ch.difty.scipamato.core.db.tables.records.CodeClassRecord;
import ch.difty.scipamato.core.db.tables.records.CodeRecord;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.filter.PaperFilter;
import ch.difty.scipamato.core.entity.filter.SearchCondition;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;

public class JooqPaperRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    private static final long   TEST_PAPER_ID = 1l;
    private static final String TEST_FILE_1   = "test file";
    private static final String TEST_FILE_2   = "test file 2";
    private static final String LC            = "en_us";

    private static final String ID_PART = ",id=1,createdBy=1,lastModifiedBy=1,created=2016-12-14T14:47:29.431,lastModified=2016-12-14T14:47:29.431,version=1";
    // @formatter:off
    private static final String PAPER1_WO_CODE_CLASSES =
            "Paper[number=1,doi=10.1093/aje/kwu275,pmId=25395026"
            + ",authors=Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.,firstAuthor=Turner,firstAuthorOverridden=false"
            + ",title=Interactions Between Cigarette Smoking and Fine Particulate Matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II."
            + ",location=Am J Epidemiol. 2014; 180 (12): 1145-1149."
            + ",publicationYear=2014,goals=Neue Analyse der Daten der amerikanischen Krebspräventions-Kohertenstudie zur Untersuchung, wie gross das kombinierte Krebsrisiko durch Feinstaub ist."
            + ",population=429'406 Teilnehmer, Frauen und Männer aus 50 Staaten der USA, welche in den Jahren 1982/1983 im Alter von mindestens 30 Jahren für die Krebsvorsorgestudie der amerikanischen Kriegsgesellschaft (ACS) rekrutiert worden waren, in den Jahren 1984, 1986 und 1988 wieder kontaktiert worden waren, und seit 1989 mit dem nationalen Sterberegister auf ihr Überleben kontaktiert wurden. Nicht in diese Analyse einbezogen wurden Exrauchende und Pfeifen- oder Zigarrenraucher. USA.,populationPlace=,populationParticipants=,populationDuration=,exposurePollutant=,exposureAssessment=,methods=Da nur bis 1998 individuelle Informationen über das Rauchverhalten vorlagen, wurden nur die ersten 6 Studienjahre in diese Analyse einbzeogen. Die Abschätzung der Belastung mit Feinstaub wurde mit Landnutzungsmodellen für die geocodierten Adressen bei Studieneintritt vorgenommen, welche sich auf Monatsmittelwerte von PM2.5 der Jahre 1999-2004 von 1464 Messstationen abstützten, unter der Annahme, dass die Feinstaubbelastungen über die Jahre eng korreliert seien. Mit proportionalen Hazard-Modellen nach Cox, stratifiziert für Alter, Geschlecht und Rasse wurde das Überleben bzw. die Sterblichkeit an Lungenkrebs in den ersten 6 Jahren in Abhängigkeit der PM2.5-Belastung in verschiedenen Kategorien (über/unter der 50 Perzentile, über der 66. vs. unter der 33. Perzentile, sowie über der 75. vs. unter der 25. Perzentile) und in Abhängigkeit von Rauchen/nicht Rauchen modelliert. Einbezogen wurden folgende invidivuellen Faktoren: Schulbildung, Zivilstand, BMI, Passivrauchen, Ernährung, Alkoholkonsum und berufliche Belastung. Die Effektmodifikation bezüglich Lungenkrebsterblichkeit wurde mit drei Grössen untersucht: das relative zusätzliche Risiko durch die Interatkion (RERI), der der Interaktion anrechenbare Teil des Risikos (AP) und der Synergie-Index (SI). Lungenkrebs, Kohortenstudie, Statistik, epidemiologische Methoden. ACS-Studie. USA.,methodStudyDesign=,methodOutcome=,methodStatistics=,methodConfounders=,result=In 2'509'717 Personen-Jahren der Nachkontrolle ereigneten sich 1921 Todesfälle an Lungenkrebs. Die geschätzte Feinstaubbelastung lag im Durchschnitt bei 12.6 SD 2.85 µg PM2.5/m3, mit der 25. und 75. Perzentile bei 10.59 und 14.44 µg PM2.5/m3. Raucher hatten im Vergleich zu Nichtrauchern ein 13.5 fach erhöhtes Risiko (95%CI 10.2-17.9), an Lungenkrebs zu sterben, wenn ihre PM2.5-Belastung gering war, d.h. unter der 25. Perzentile lag. Nichtraucher hatten ein 1.28 faches Risiko (0.92-1.78), wenn ihre Belastung über der 75. Perzentile der PM2.5-Belastung lag, im Vergleich zu Nichtrauchern mit geringer Belastung. Raucher hatten ein 16 faches Risiko (12.1-21.1), an Lungenkrebs zu sterben, wenn ihre Feinstaubbelastung über der 75. Perzentile lag. Das zusätzliche relative Risiko durch die Interaktion (RERI) für die Kombination von Rauchen und schlechter Luft betrug 2.19 (-0.10;+4.83). Der Risikoanteil, der dem Kombinationseffekt angerechnet werden kann, betrug 14%, der Synergie-Index 1.17. Die Autoren schliessen daraus, dass die Folgen von Rauchen und Luftverschmutzung stärker zusammenwiren als nur additiv. Auch wenn die Lungenkrebfälle am stärksten durch einen Rückgang des Rauchens abnehmen, kann ein solcher Rückgang mit einer Verbesserung der Luftqualität stärker ausfallen als mit einer der beiden Massnahmen allein."
            + ",resultExposureRange=,resultEffectEstimate=,resultMeasuredOutcome=,comment=Kommentar von Panagiotou AO, Wacholder S: How Big Is That Interaction (in My Community)-and I. Which Direction? Am. J. Epidemiol. 2014 180: 1150-1158."
            + ",intern=,originalAbstract=<null>,mainCodeOfCodeclass1=1F,attachments=[]";
    // @formatter:on

    @Autowired
    private DSLContext dsl;

    @Autowired
    private JooqPaperRepo repo;

    @Test
    public void findingAll() {
        List<Paper> papers = repo.findAll();
        papers.sort((p1, p2) -> p1.getId()
            .compareTo(p2.getId()));

        assertThat(papers).hasSize(RECORD_COUNT_PREPOPULATED);
        assertThat(papers.get(0)
            .getId()).isEqualTo(1);
        assertThat(papers.get(1)
            .getId()).isEqualTo(2);
        assertThat(papers.get(2)
            .getId()).isEqualTo(3);
        assertThat(papers.get(3)
            .getId()).isEqualTo(4);
        assertThat(papers.get(4)
            .getId()).isEqualTo(10);
        assertThat(papers.get(13)
            .getId()).isEqualTo(19);
        assertThat(papers.get(22)
            .getId()).isEqualTo(28);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        long id = 4;
        Paper paper = repo.findById(id);
        assertThat(paper.getId()).isEqualTo(id);
        assertThat(paper.getAuthors()).isEqualTo("Kutlar Joss M, Joss U.");
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
        assertThat(saved.getId()).isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        assertThat(saved.getAuthors()).isEqualTo("a");
    }

    private Paper makeMinimalPaper() {
        Paper p = new Paper();
        p.setNumber(100l);
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
        assertThat(paper.getId()).isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
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
    public void savingAssociatedEntitiesOf_withCodes() {
        Paper paper = repo.add(makeMinimalPaper());
        assertThat(paper).isNotNull();
        assertThat(paper.getId()).isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        final long id = paper.getId();
        assertThat(paper.getAuthors()).isEqualTo("a");

        CodeRecord cr = dsl.selectFrom(Code.CODE)
            .limit(1)
            .fetchOne();
        CodeClassRecord ccr = dsl.selectFrom(CodeClass.CODE_CLASS)
            .where(CodeClass.CODE_CLASS.ID.eq(cr.getCodeClassId()))
            .fetchOne();
        ch.difty.scipamato.core.entity.CodeClass codeClass = new ch.difty.scipamato.core.entity.CodeClass(ccr.getId(),
                "", "");
        ch.difty.scipamato.core.entity.Code code = new ch.difty.scipamato.core.entity.Code(cr.getCode(), "", "", true,
                codeClass.getId(), codeClass.getName(), codeClass.getDescription(), cr.getSort(), null, null, null,
                null, null);
        paper.addCode(code);

        repo.update(paper);
        assertThat(paper.getId()).isEqualTo(id);

        Paper newCopy = repo.findById(id);
        assertThat(newCopy).isNotEqualTo(paper);
        assertThat(newCopy.getId()).isEqualTo(id);
        assertThat(newCopy.getCodes()).extracting(Code.CODE.CODE_.getName())
            .containsExactly(code.getCode());
    }

    @Test
    public void deletingRecord() {
        Paper paper = repo.add(makeMinimalPaper());
        assertThat(paper).isNotNull();
        assertThat(paper.getId()).isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        final long id = paper.getId();
        assertThat(paper.getAuthors()).isEqualTo("a");

        Paper deleted = repo.delete(id, paper.getVersion());
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
           +         "Code[code=1F,name=Feinstaub, Partikel,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
           +      "],"
           +      "codesOfClass2=["
           +         "Code[code=2N,name=Übrige Länder,comment=<null>,internal=false,codeClass=CodeClass[id=2],sort=2,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
           +      "],"
           +      "codesOfClass3=["
           +         "Code[code=3C,name=Erwachsene (alle),comment=<null>,internal=false,codeClass=CodeClass[id=3],sort=3,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
           +      "],"
           +      "codesOfClass4=["
           +         "Code[code=4G,name=Krebs,comment=<null>,internal=false,codeClass=CodeClass[id=4],sort=7,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
           +      "],"
           +      "codesOfClass5=["
           +         "Code[code=5H,name=Kohortenstudie,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=7,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1], "
           +         "Code[code=5S,name=Statistik,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=10,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
           +      "],"
           +      "codesOfClass6=["
           +         "Code[code=6M,name=Mensch,comment=<null>,internal=false,codeClass=CodeClass[id=6],sort=1,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
           +      "],"
           +      "codesOfClass7="
           +         "[Code[code=7L,name=Langfristig,comment=<null>,internal=false,codeClass=CodeClass[id=7],sort=2,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
           +      "],"
           +      "codesOfClass8=["
           +         "Code[code=8O,name=Aussenluft,comment=<null>,internal=false,codeClass=CodeClass[id=8],sort=2,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
           +      "]"
           +    "]"
           + ID_PART
           + "]");
        // @formatter:on
    }

    @Test
    public void gettingByIds_returnsRecordForEveryIdExisting() {
        List<Paper> papers = repo.findByIds(Arrays.asList(1l, 2l, 3l, 10l, -17l));
        assertThat(papers).hasSize(4);
        assertThat(papers).extracting(Paper.ID)
            .containsExactly(1l, 2l, 3l, 10l);

        // codes not enriched
        assertThat(papers.get(0)
            .getCodes()).isEmpty();
    }

    @Test
    public void gettingByIds_returnsEmptyListForEmptyIdList() {
        assertThat(repo.findByIds(Arrays.asList())).isEmpty();
    }

    @Test
    public void gettingWithCodesByIds_returnsRecordForEveryIdExisting() {
        List<Paper> papers = repo.findWithCodesByIds(Arrays.asList(1l, 2l, 3l, 10l, -17l), LC);
        assertThat(papers).hasSize(4);
        assertThat(papers).extracting(Paper.ID)
            .containsExactly(1l, 2l, 3l, 10l);

        // codes are present
        assertThat(papers.get(0)
            .getCodes()).isNotEmpty();
    }

    @Test
    public void findingPapersByPmIds_withThreeValidPmIds_returnsThreePapers() {
        List<Paper> papers = repo.findByPmIds(Arrays.asList(20335815, 27128166, 25104428), LC);
        assertThat(papers).hasSize(3);
        assertThat(papers).extracting(Paper.PMID)
            .containsOnly(20335815, 27128166, 25104428);
    }

    @Test
    public void findingPapersByPmIds_withInvalidPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(Arrays.asList(-20335815), LC)).isEmpty();
    }

    @Test
    public void findingPapersByPmIds_hasCodesEnriched() {
        List<Paper> papers = repo.findByPmIds(Arrays.asList(20335815), LC);
        assertThat(papers.get(0)
            .getCodes()).isNotEmpty();
    }

    @Test
    public void findingExistingPmIdsOutOf_withThreeValidPmIds_returnsThreePMIDs() {
        List<Integer> pmids = repo.findExistingPmIdsOutOf(Arrays.asList(20335815, 27128166, 25104428));
        assertThat(pmids).hasSize(3);
        assertThat(pmids).containsOnly(20335815, 27128166, 25104428);
    }

    @Test
    public void findingExistingPmIdsOutOf_withInvalidPmIds_returnsEmptyList() {
        assertThat(repo.findExistingPmIdsOutOf(Arrays.asList(-20335815))).isEmpty();
    }

    @Test
    public void findingPapersByNumbers_withThreeValidNumbers_returnsThreePapers() {
        List<Paper> papers = repo.findByNumbers(Arrays.asList(1l, 2l, 3l), LC);
        assertThat(papers).hasSize(3);
        assertThat(papers).extracting(Paper.NUMBER)
            .containsOnly(1l, 2l, 3l);
    }

    @Test
    public void findingPapersByNumbers_withInvalidNumbers_returnsEmptyList() {
        assertThat(repo.findByNumbers(Arrays.asList(-1l), LC)).isEmpty();
    }

    @Test
    public void findingPapersByNumber_hasCodesEnriched() {
        List<Paper> papers = repo.findByNumbers(Arrays.asList(1l), LC);
        assertThat(papers.get(0)
            .getCodes()).isNotEmpty();
    }

    @Test
    public void findingBySearchOrder() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc = new SearchCondition();
        sc.setAuthors("kutlar");
        searchOrder.add(sc);
        List<Paper> papers = repo.findBySearchOrder(searchOrder, LC);
        assertThat(papers).isNotEmpty();
    }

    @Test
    public void findingPageBySearchOrder() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc = new SearchCondition();
        sc.setAuthors("kutlar");
        searchOrder.add(sc);
        assertThat(repo.findPageBySearchOrder(searchOrder, new PaginationRequest(Direction.ASC, "authors"), LC))
            .isNotEmpty();
    }

    @Test
    public void findingLowestFreeNumberStartingFrom_findsFirstGapStartingAboveMinimumValue() {
        long number = repo.findLowestFreeNumberStartingFrom(0l);
        assertThat(number).isEqualTo(5l);
    }

    @Test
    public void findingLowestFreeNumberStartingFrom_withMinimumInMultiNumberGap_ignoresRemainingNumbersOfSameGap() {
        long number = repo.findLowestFreeNumberStartingFrom(5l);
        assertThat(number).isGreaterThanOrEqualTo(42l);
    }

    @Test
    public void findingLowestFreeNumberStartingFrom_withMinimumBeyondLastGap_findsNextFreeNumber() {
        long number = repo.findLowestFreeNumberStartingFrom(30);
        assertThat(number).isGreaterThanOrEqualTo(42l);
    }

    @Test
    public void findingLowestFreeNumberStartingFrom_withMinimumBeyondNextFreeNumber_findsMiniumLeavingGap() {
        long number = repo.findLowestFreeNumberStartingFrom(100l);
        assertThat(number).isGreaterThanOrEqualTo(100l);
    }

    @Test
    public void findingPageOfIdsByFilter() {
        PaperFilter filter = new PaperFilter();
        filter.setAuthorMask("Kutlar");
        assertThat(repo.findPageOfIdsByFilter(filter, new PaginationRequest(Direction.ASC, "authors"))).isNotEmpty()
            .containsExactly(4l);
    }

    @Test
    public void findingPageOfIdsBySearchOrder() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc = new SearchCondition();
        sc.setAuthors("kutlar");
        searchOrder.add(sc);
        assertThat(repo.findPageOfIdsBySearchOrder(searchOrder, new PaginationRequest(Direction.ASC, "authors")))
            .isNotEmpty()
            .containsExactly(4l);
    }

    @Test
    public void exludingPaperFromSearch_addsOneRecord_reincluding_removesItAgain() {
        final long searchOrderId = 1;
        final long paperId = 1;
        ensureRecordNotPresent(searchOrderId, paperId);

        repo.excludePaperFromSearchOrderResults(searchOrderId, paperId);
        assertExclusionCount(searchOrderId, paperId, 1);

        repo.reincludePaperIntoSearchOrderResults(searchOrderId, paperId);
        ensureRecordNotPresent(searchOrderId, paperId);
    }

    private void ensureRecordNotPresent(final long searchOrderId, final long paperId) {
        assertExclusionCount(searchOrderId, paperId, 0);
    }

    private void assertExclusionCount(final long searchOrderId, final long paperId, final int count) {
        assertThat(dsl.selectCount()
            .from(SearchExclusion.SEARCH_EXCLUSION)
            .where(SearchExclusion.SEARCH_EXCLUSION.SEARCH_ORDER_ID.eq(searchOrderId))
            .and(SearchExclusion.SEARCH_EXCLUSION.PAPER_ID.eq(paperId))
            .fetchOne(0, int.class)).isEqualTo(count);
    }

    @Test
    public void exludingPaperFromSearch_whenAddingMultipleTimes_ignoresAllButFirst() {
        final long searchOrderId = 1;
        final long paperId = 1;
        ensureRecordNotPresent(searchOrderId, paperId);

        repo.excludePaperFromSearchOrderResults(searchOrderId, paperId);
        repo.excludePaperFromSearchOrderResults(searchOrderId, paperId);
        repo.excludePaperFromSearchOrderResults(searchOrderId, paperId);
        assertExclusionCount(searchOrderId, paperId, 1);
    }

    @Test
    public void loadingSlimAttachment_loadsEverythingExceptContent() {
        final String content1 = "baz";
        PaperAttachment pa1 = newPaperAttachment(TEST_FILE_1, content1);
        repo.saveAttachment(pa1);

        final String content2 = "blup";
        PaperAttachment pa2 = newPaperAttachment(TEST_FILE_2, content2);
        repo.saveAttachment(pa2);

        List<PaperAttachment> results = repo.loadSlimAttachment(TEST_PAPER_ID);

        assertThat(results).hasSize(2);
        PaperAttachment saved = results.get(0);

        assertThat(saved.getName()).isEqualTo(pa1.getName());
        assertThat(saved.getContent()).isNull();
        assertThat(saved.getSize()).isEqualTo(content1.length());
        assertThat(saved.getContentType()).isEqualTo("application/pdf");
        assertThat(saved.getCreated()
            .toString()).isEqualTo("2016-12-09T06:02:13");
        assertThat(saved.getLastModified()
            .toString()).isEqualTo("2016-12-09T06:02:13");
    }

    private PaperAttachment newPaperAttachment(String name, final String content) {
        return new PaperAttachment(null, TEST_PAPER_ID, name, content.getBytes(), "application/pdf",
                (long) content.length());
    }

    @Test
    public void savingAttachment_whenNotExisting_insertsIntoDb() {
        final String content = "foo";
        PaperAttachment pa = newPaperAttachment(TEST_FILE_1, content);

        Paper p = repo.saveAttachment(pa);
        PaperAttachment saved = dsl.select()
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
            .fetchOneInto(PaperAttachment.class);

        assertThat(p.getAttachments()).extracting("id")
            .contains(saved.getId());

        assertThat(saved.getName()).isEqualTo(pa.getName());
        assertThat(new String(saved.getContent())).isEqualTo(content);
        assertThat(saved.getSize()).isEqualTo(content.length());
        assertThat(saved.getContentType()).isEqualTo("application/pdf");
        assertThat(saved.getCreated()
            .toString()).isEqualTo("2016-12-09T06:02:13");
        assertThat(saved.getLastModified()
            .toString()).isEqualTo("2016-12-09T06:02:13");
    }

    @Test
    public void savingAttachment_whenExisted_performsUpdate() {
        final String content2 = "bar";
        PaperAttachment pa1 = newPaperAttachment(TEST_FILE_1, "foo");
        PaperAttachment pa2 = newPaperAttachment(TEST_FILE_1, content2);
        assertThat(pa1.getPaperId()).isEqualTo(pa2.getPaperId());
        assertThat(pa1.getName()).isEqualTo(pa2.getName());

        repo.saveAttachment(pa1);
        repo.saveAttachment(pa2);

        PaperAttachment saved2 = dsl.select()
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
            .fetchOneInto(PaperAttachment.class);

        assertThat(saved2.getName()).isEqualTo(pa1.getName());
        assertThat(saved2.getVersion()).isEqualTo(2);
        assertThat(new String(saved2.getContent())).isEqualTo(content2);
    }

    @Test
    public void loadingAttachmentWithContentById() {
        final String content1 = "baz";
        PaperAttachment pa1 = newPaperAttachment(TEST_FILE_1, content1);
        repo.saveAttachment(pa1);

        Integer id = dsl.select(PAPER_ATTACHMENT.ID)
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
            .fetchOneInto(Integer.class);
        PaperAttachment attachment = repo.loadAttachmentWithContentBy(id);
        assertThat(attachment.getContent()).isNotNull();
        assertThat(new String(attachment.getContent())).isEqualTo(content1);
    }

    @Test
    public void deletingAttachment_deletes() {
        repo.saveAttachment(newPaperAttachment(TEST_FILE_1, "foo"));
        Integer id = dsl.select(PAPER_ATTACHMENT.ID)
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
            .fetchOneInto(Integer.class);
        assertThat(id).isNotNull();
        Paper p = repo.deleteAttachment(id);
        assertThat(p.getAttachments()).extracting("id")
            .doesNotContain(id);
        assertThat(dsl.select(PAPER_ATTACHMENT.ID)
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
            .fetchOneInto(Integer.class)).isNull();
    }

    /**
     * Verify the rollback is occurring, also that the JooqExceptionTranslator is
     * doing it's job to translate jooq specific exceptions into spring exceptions.
     */
    @Test
    public void testDeclarativeTransaction() {
        boolean rollback = false;
        Paper paper = repo.findById(1l);
        assertThat(paper).isNotNull();
        try {
            paper.setNumber(null);
            repo.update(paper);
            fail("Should have thrown exception due to null value on non-null column");
        } catch (org.jooq.exception.DataAccessException dae) {
            fail("JooqExceptionTranslator did not translate the jooqException into a spring exception");
        } catch (DataAccessException dae) {
            rollback = true;
            assertThat(dae).isInstanceOf(DataIntegrityViolationException.class);
            dae.getMessage()
                .startsWith("jOOQ; SQL [update \"public\".\"paper\" set \"number\" = ?");
        }
        assertThat(rollback).isTrue();
    }
}
