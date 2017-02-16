package ch.difty.sipamato.entity.xml;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.oxm.XmlMappingException;

import ch.difty.sipamato.pubmed.Article;
import ch.difty.sipamato.pubmed.ArticleDate;
import ch.difty.sipamato.pubmed.Author;
import ch.difty.sipamato.pubmed.AuthorList;
import ch.difty.sipamato.pubmed.ChemicalList;
import ch.difty.sipamato.pubmed.Day;
import ch.difty.sipamato.pubmed.ELocationID;
import ch.difty.sipamato.pubmed.Journal;
import ch.difty.sipamato.pubmed.JournalIssue;
import ch.difty.sipamato.pubmed.KeywordList;
import ch.difty.sipamato.pubmed.LastName;
import ch.difty.sipamato.pubmed.MedlineCitation;
import ch.difty.sipamato.pubmed.MedlineJournalInfo;
import ch.difty.sipamato.pubmed.MedlinePgn;
import ch.difty.sipamato.pubmed.MeshHeadingList;
import ch.difty.sipamato.pubmed.Month;
import ch.difty.sipamato.pubmed.Pagination;
import ch.difty.sipamato.pubmed.PubmedArticle;
import ch.difty.sipamato.pubmed.PubmedArticleSet;
import ch.difty.sipamato.pubmed.Year;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;

public class SipamatoPubmedArticleIntegrationTest extends PubmedIntegrationTest {

    private static final String XML_PUBMED_RESULT_XML = "xml/pubmed_result.xml";

    private static final String PM_ID = "25395026";
    private static final String PUBLICATION_YEAR = "2014";
    private static final String TITLE = "Interactions between cigarette smoking and fine particulate matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II.";
    private static final String DOI = "10.1093/aje/kwu275";
    private static final String ABSTRACT_START = "The International Agency for Research on Cancer recently classified outdoor air pollution";

    @Test
    public void feedIntoSipamatoArticle() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_PUBMED_RESULT_XML);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo(PM_ID);
        assertThat(sa.getAuthors()).isEqualTo("Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Turner");
        assertThat(sa.getPublicationYear()).isEqualTo(PUBLICATION_YEAR);
        assertThat(sa.getLocation()).isEqualTo("Am J Epidemiol. 2014; 180 (12): 1145-1149.");
        assertThat(sa.getTitle()).isEqualTo(TITLE);
        assertThat(sa.getDoi()).isEqualTo(DOI);
        assertThat(sa.getOriginalAbstract()).startsWith(ABSTRACT_START);
    }

    @Test
    public void manualExplorationOfFile() throws XmlMappingException, IOException {
        PubmedArticleSet articleSet = getPubmedArticleSet(XML_PUBMED_RESULT_XML);
        assertThat(articleSet.getPubmedArticleOrPubmedBookArticle()).hasSize(1);

        Object pubmedArticleObject = articleSet.getPubmedArticleOrPubmedBookArticle().get(0);
        assertThat(pubmedArticleObject).isInstanceOf(PubmedArticle.class);

        PubmedArticle pubmedArticle = (PubmedArticle) articleSet.getPubmedArticleOrPubmedBookArticle().get(0);

        MedlineCitation medlineCitation = pubmedArticle.getMedlineCitation();
        assertThat(medlineCitation.getPMID().getvalue()).isEqualTo(PM_ID);

        Article article = medlineCitation.getArticle();
        Journal journal = article.getJournal();
        JournalIssue journalIssue = journal.getJournalIssue();
        assertThat(journalIssue.getVolume()).isEqualTo("180");
        assertThat(journalIssue.getIssue()).isEqualTo("12");

        assertThat(journalIssue.getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate()).hasSize(3);
        assertThat(journalIssue.getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().get(0)).isInstanceOf(Year.class);
        assertThat(journalIssue.getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().get(1)).isInstanceOf(Month.class);
        assertThat(journalIssue.getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().get(2)).isInstanceOf(Day.class);
        Year year = (Year) journalIssue.getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().get(0);
        assertThat(year.getvalue()).isEqualTo(PUBLICATION_YEAR);

        assertThat(journal.getTitle()).isEqualTo("American journal of epidemiology");
        assertThat(journal.getISOAbbreviation()).isEqualTo("Am. J. Epidemiol.");
        assertThat(article.getArticleTitle().getvalue()).isEqualTo(TITLE);

        assertThat(article.getPaginationOrELocationID()).hasSize(2);
        assertThat(article.getPaginationOrELocationID().get(0)).isInstanceOf(Pagination.class);
        assertThat(article.getPaginationOrELocationID().get(1)).isInstanceOf(ELocationID.class);

        Pagination pagination = (Pagination) article.getPaginationOrELocationID().get(0);
        assertThat(pagination.getStartPageOrEndPageOrMedlinePgn()).hasSize(1);
        assertThat(pagination.getStartPageOrEndPageOrMedlinePgn().get(0)).isInstanceOf(MedlinePgn.class);
        MedlinePgn pgn = (MedlinePgn) pagination.getStartPageOrEndPageOrMedlinePgn().get(0);
        assertThat(pgn.getvalue()).isEqualTo("1145-9");

        ELocationID elocationId = (ELocationID) article.getPaginationOrELocationID().get(1);
        assertThat(elocationId.getValidYN()).isEqualTo("Y");
        assertThat(elocationId.getvalue()).isEqualTo(DOI);
        assertThat(elocationId.getEIdType()).isEqualTo("doi");

        assertThat(article.getAbstract().getAbstractText()).hasSize(1);
        assertThat(article.getAbstract().getAbstractText().get(0).getvalue()).startsWith(ABSTRACT_START);

        AuthorList authorList = article.getAuthorList();
        assertThat(authorList.getCompleteYN()).isEqualTo("Y");
        assertThat(authorList.getType()).isNull();
        assertThat(authorList.getAuthor()).hasSize(9);
        assertThat(authorList.getAuthor()).extracting("validYN").containsOnly("Y");

        List<String> authorNames = authorList.getAuthor()
                .stream()
                .map(Author::getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName)
                .flatMap(n -> n.stream())
                .filter((o) -> o instanceof LastName)
                .map(lm -> ((LastName) lm).getvalue())
                .collect(Collectors.toList());
        assertThat(authorNames).contains("Turner", "Cohen", "Jerrett", "Gapstur", "Diver", "Pope", "Krewski", "Beckerman", "Samet");

        assertThat(article.getArticleDate()).hasSize(1);
        ArticleDate articleDate = article.getArticleDate().get(0);
        assertThat(articleDate.getDateType()).isEqualTo("Electronic");
        assertThat(articleDate.getYear().getvalue()).isEqualTo(PUBLICATION_YEAR);

        MedlineJournalInfo medlineJournalInfo = medlineCitation.getMedlineJournalInfo();
        assertThat(medlineJournalInfo.getCountry()).isEqualTo("United States");
        assertThat(medlineJournalInfo.getMedlineTA()).isEqualTo("Am J Epidemiol");
        assertThat(medlineJournalInfo.getNlmUniqueID()).isEqualTo("7910653");
        assertThat(medlineJournalInfo.getISSNLinking()).isEqualTo("0002-9262");

        ChemicalList chemicalList = medlineCitation.getChemicalList();
        assertThat(chemicalList.getChemical()).hasSize(3);

        MeshHeadingList meshHeadingList = medlineCitation.getMeshHeadingList();
        assertThat(meshHeadingList.getMeshHeading()).hasSize(20);

        List<KeywordList> keywordList = medlineCitation.getKeywordList();
        assertThat(keywordList).hasSize(1);
    }

}
