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

    private static final String XML_2539026 = "xml/pubmed_result_25395026.xml";
    private static final String XML_27258721 = "xml/pubmed_result_27258721.xml";

    @Test
    public void feedIntoSipamatoArticle_25395026() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_2539026);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("25395026");
        assertThat(sa.getAuthors()).isEqualTo("Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Turner");
        assertThat(sa.getPublicationYear()).isEqualTo("2014");
        assertThat(sa.getLocation()).isEqualTo("Am J Epidemiol. 2014; 180 (12): 1145-1149.");
        assertThat(sa.getTitle()).isEqualTo("Interactions between cigarette smoking and fine particulate matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II.");
        assertThat(sa.getDoi()).isEqualTo("10.1093/aje/kwu275");
        assertThat(sa.getOriginalAbstract()).startsWith("The International Agency for Research on Cancer recently classified outdoor air pollution");
    }

    @Test
    public void feedIntoSipamatoArticle_27258721() throws XmlMappingException, IOException {
        List<PubmedArticleFacade> articles = getPubmedArticles(XML_27258721);
        assertThat(articles).hasSize(1);
        PubmedArticleFacade sa = articles.get(0);

        assertThat(sa.getPmId()).isEqualTo("27258721");
        assertThat(sa.getAuthors()).isEqualTo(
                "Aguilera I, Dratva J, Caviezel S, Burdet L, de Groot E, Ducret-Stich RE, Eeftens M, Keidel D, Meier R, Perez L, Rothe T, Schaffner E, Schmit-Trucksäss A, Tsai MY, Schindler C, Künzli N, Probst-Hensch N.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Aguilera");
        assertThat(sa.getPublicationYear()).isEqualTo("2016");
        assertThat(sa.getLocation()).isEqualTo("Environ Health Perspect. 2016; 124 (11): 1700-1706.");
        assertThat(sa.getTitle())
                .isEqualTo("Particulate Matter and Subclinical Atherosclerosis: Associations between Different Particle Sizes and Sources with Carotid Intima-Media Thickness in the SAPALDIA Study.");
        assertThat(sa.getDoi()).isEqualTo("10.1289/EHP161");
        assertThat(sa.getOriginalAbstract()).startsWith("Subclinical atherosclerosis has been associated with long-term exposure to particulate matter (PM)");
    }

    @Test
    public void manualExplorationOfFile_25395026() throws XmlMappingException, IOException {
        PubmedArticleSet articleSet = getPubmedArticleSet(XML_2539026);
        assertThat(articleSet.getPubmedArticleOrPubmedBookArticle()).hasSize(1);

        Object pubmedArticleObject = articleSet.getPubmedArticleOrPubmedBookArticle().get(0);
        assertThat(pubmedArticleObject).isInstanceOf(PubmedArticle.class);

        PubmedArticle pubmedArticle = (PubmedArticle) articleSet.getPubmedArticleOrPubmedBookArticle().get(0);

        MedlineCitation medlineCitation = pubmedArticle.getMedlineCitation();
        assertThat(medlineCitation.getPMID().getvalue()).isEqualTo("25395026");

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
        assertThat(year.getvalue()).isEqualTo("2014");

        assertThat(journal.getTitle()).isEqualTo("American journal of epidemiology");
        assertThat(journal.getISOAbbreviation()).isEqualTo("Am. J. Epidemiol.");
        assertThat(article.getArticleTitle().getvalue())
                .isEqualTo("Interactions between cigarette smoking and fine particulate matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II.");

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
        assertThat(elocationId.getvalue()).isEqualTo("10.1093/aje/kwu275");
        assertThat(elocationId.getEIdType()).isEqualTo("doi");

        assertThat(article.getAbstract().getAbstractText()).hasSize(1);
        assertThat(article.getAbstract().getAbstractText().get(0).getvalue()).startsWith("The International Agency for Research on Cancer recently classified outdoor air pollution");

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
        assertThat(articleDate.getYear().getvalue()).isEqualTo("2014");

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
