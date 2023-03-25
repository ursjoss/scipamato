package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.core.pubmed.api.Day
import ch.difty.scipamato.core.pubmed.api.ELocationID
import ch.difty.scipamato.core.pubmed.api.LastName
import ch.difty.scipamato.core.pubmed.api.MedlinePgn
import ch.difty.scipamato.core.pubmed.api.Month
import ch.difty.scipamato.core.pubmed.api.Pagination
import ch.difty.scipamato.core.pubmed.api.PubmedArticle
import ch.difty.scipamato.core.pubmed.api.Year
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldEndWith
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldStartWith
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

@Suppress("TooManyFunctions", "SpellCheckingInspection", "FunctionName", "MagicNumber")
internal class ScipamatoPubmedArticleIntegrationTest : PubmedIntegrationTest() {

    @Test
    fun feedIntoScipamatoArticle_25395026() {
        val articles = getPubmedArticles(XML_2539026)
        articles shouldHaveSize 1
        assertArticle239026(articles.first())
    }

    private fun assertArticle239026(sa: PubmedArticleFacade) {
        with(sa) {
            pmId shouldBeEqualTo "25395026"
            authors shouldBeEqualTo
                "Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM."
            firstAuthor shouldBeEqualTo "Turner"
            publicationYear shouldBeEqualTo "2014"
            location shouldBeEqualTo "Am J Epidemiol. 2014; 180 (12): 1145-1149."
            title shouldBeEqualTo
                "Interactions between cigarette smoking and fine particulate matter in the Risk " +
                "of Lung Cancer Mortality in Cancer Prevention Study II."
            doi shouldBeEqualTo "10.1093/aje/kwu275"
            originalAbstract shouldStartWith
                "The International Agency for Research on Cancer recently classified outdoor air pollution"
            originalAbstract.trim { it <= ' ' } shouldEndWith
                "based on reducing exposure to either risk factor alone."
        }
    }

    @Test
    fun feedIntoScipamatoArticle_23454700_withoutIssue_stillHasColon() {
        val articles = getPubmedArticles(XML_23454700)
        articles shouldHaveSize 1
        with(articles.first()) {
            pmId shouldBeEqualTo "23454700"
            authors shouldBeEqualTo
                "Pascal M, Corso M, Chanel O, Declercq C, Badaloni C, Cesaroni G, Henschel S, Meister " +
                "K, Haluza D, Martin-Olmedo P, Medina S; Aphekom group."
            firstAuthor shouldBeEqualTo "Pascal"
            publicationYear shouldBeEqualTo "2013"
            location shouldBeEqualTo "Sci Total Environ. 2013; 449: 390-400."
            title shouldBeEqualTo
                "Assessing the public health impacts of urban air pollution in 25 European cities: " +
                "results of the Aphekom project."
            doi shouldBeEqualTo "10.1016/j.scitotenv.2013.01.077"
            originalAbstract shouldStartWith
                "INTRODUCTION: The Aphekom project aimed to provide new, clear, " +
                "and meaningful information on the health effects of air pollution in Europe."
            originalAbstract shouldEndWith "EU legislation is being revised for an update in 2013."
        }
    }

    @Test
    fun feedIntoScipamatoArticle_26607712_notHavingPaginationButElocationIdOfTypePii_usesThat() {
        val articles = getPubmedArticles(XML_26607712)
        articles shouldHaveSize 1
        with(articles.first()) {
            pmId shouldBeEqualTo "26607712"
            authors shouldBeEqualTo "Hart JE, Puett RC, Rexrode KM, Albert CM, Laden F."
            firstAuthor shouldBeEqualTo "Hart"
            publicationYear shouldBeEqualTo "2015"
            location shouldBeEqualTo "J Am Heart Assoc. 2015; 4 (12). pii: e002301."
            title shouldBeEqualTo
                "Effect Modification of Long-Term Air Pollution Exposures and the Risk of " +
                "Incident Cardiovascular Disease in US Women."
            doi shouldBeEqualTo "10.1161/JAHA.115.002301"
            originalAbstract shouldStartWith
                "BACKGROUND: Ambient air pollution exposures have been frequently linked to " +
                "cardiovascular disease (CVD) morbidity and mortality. However, less is known " +
                "about the populations most susceptible to these adverse effects."
            originalAbstract shouldEndWith
                "women with diabetes were identified as the subpopulation most sensitive to the " +
                "adverse cardiovascular health effects of PM."
        }
    }

    @Test
    fun feedIntoScipamatoArticle_27214671_yearFromMedDate() {
        val articles = getPubmedArticles(XML_27214671)
        articles shouldHaveSize 1
        with(articles.first()) {
            pmId shouldBeEqualTo "27214671"
            authors shouldBeEqualTo "Clark-Reyna SE, Grineski SE, Collins TW."
            firstAuthor shouldBeEqualTo "Clark-Reyna"
            publicationYear shouldBeEqualTo "2016"
            location shouldBeEqualTo "Fam Community Health. 2016; 39 (3): 160-168."
            title shouldBeEqualTo
                "Health Status and Residential Exposure to Air Toxics: What Are " +
                "the Effects on Children's Academic Achievement?"
            doi shouldBeEqualTo "10.1097/FCH.0000000000000112"
            originalAbstract shouldStartWith
                "This article examines the effects of children's subjective health status and " +
                "exposure to residential environmental toxins"
            originalAbstract shouldEndWith
                "there is an independent effect of air pollution on children's academic achievement " +
                "that cannot be explained by poor health alone."
        }
    }

    @Test
    fun feedIntoScipamatoArticle_27224452_authorWithCollectiveName() {
        val articles = getPubmedArticles(XML_27224452)
        articles shouldHaveSize 1
        with(articles.first()) {
            pmId shouldBeEqualTo "27224452"
            authors shouldBeEqualTo
                "Lanzinger S, Schneider A, Breitner S, Stafoggia M, Erzen I, Dostal M, Pastorkova A, Bastian S, " +
                "Cyrys J, Zscheppang A, Kolodnitska T, Peters A; UFIREG study group."
            firstAuthor shouldBeEqualTo "Lanzinger"
            publicationYear shouldBeEqualTo "2016"
            location shouldBeEqualTo "Am J Respir Crit Care Med. 2016; 194 (10): 1233-1241."
            title shouldBeEqualTo
                "Ultrafine and Fine Particles and Hospital Admissions in Central Europe. Results from the UFIREG Study."
            doi shouldBeEqualTo "10.1164/rccm.201510-2042OC"
            originalAbstract shouldStartWith
                "RATIONALE: Evidence of short-term effects of ultrafine particles (UFP) on health is still " +
                "inconsistent and few multicenter studies have been conducted so far especially in Europe."
            originalAbstract shouldEndWith
                "harmonized UFP measurements to draw definite conclusions on health effects of UFP."
        }
    }

    @Test
    fun feedIntoScipamatoArticle_27258721() {
        val articles = getPubmedArticles(XML_27258721)
        articles shouldHaveSize 1
        with(articles.first()) {
            pmId shouldBeEqualTo "27258721"
            authors shouldBeEqualTo
                "Aguilera I, Dratva J, Caviezel S, Burdet L, de Groot E, Ducret-Stich RE, Eeftens M, Keidel D, " +
                "Meier R, Perez L, Rothe T, Schaffner E, Schmit-Trucksäss A, Tsai MY, Schindler C, Künzli N, " +
                "Probst-Hensch N."
            firstAuthor shouldBeEqualTo "Aguilera"
            publicationYear shouldBeEqualTo "2016"
            location shouldBeEqualTo "Environ Health Perspect. 2016; 124 (11): 1700-1706."
            title shouldBeEqualTo
                "Particulate Matter and Subclinical Atherosclerosis: Associations between Different Particle Sizes " +
                "and Sources with Carotid Intima-Media Thickness in the SAPALDIA Study."
            doi shouldBeEqualTo "10.1289/EHP161"
            originalAbstract shouldStartWith
                "BACKGROUND: Subclinical atherosclerosis has been associated with long-term exposure to particulate " +
                "matter (PM)"
            originalAbstract shouldEndWith
                "SAPALDIA study. Environ Health Perspect 124:1700-1706; http://dx.doi.org/10.1289/EHP161."
        }
    }

    @Suppress("LongMethod")
    @Test
    fun manualExplorationOfFile_25395026() {
        val articleSet = getPubmedArticleSet(XML_2539026)
        if (articleSet != null) {
            articleSet.pubmedArticleOrPubmedBookArticle shouldHaveSize 1

            val pubmedArticleObject = articleSet.pubmedArticleOrPubmedBookArticle.first()
            pubmedArticleObject shouldBeInstanceOf PubmedArticle::class

            val pubmedArticle = articleSet.pubmedArticleOrPubmedBookArticle.first() as PubmedArticle

            val medlineCitation = pubmedArticle.medlineCitation
            medlineCitation.pmid.getvalue() shouldBeEqualTo "25395026"

            val article = medlineCitation.article
            val journal = article.journal
            val journalIssue = journal.journalIssue
            journalIssue.volume shouldBeEqualTo "180"
            journalIssue.issue shouldBeEqualTo "12"

            journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate shouldHaveSize 3
            journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate[0] shouldBeInstanceOf Year::class
            journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate[1] shouldBeInstanceOf Month::class
            journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate[2] shouldBeInstanceOf Day::class
            val year = journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate[0] as Year
            year.getvalue() shouldBeEqualTo "2014"

            journal.title shouldBeEqualTo "American journal of epidemiology"
            journal.isoAbbreviation shouldBeEqualTo "Am. J. Epidemiol."
            article.articleTitle.getvalue() shouldBeEqualTo
                "Interactions between cigarette smoking and fine particulate matter in the Risk " +
                "of Lung Cancer Mortality in Cancer Prevention Study II."

            article.paginationOrELocationID shouldHaveSize 2
            article.paginationOrELocationID[0] shouldBeInstanceOf Pagination::class
            article.paginationOrELocationID[1] shouldBeInstanceOf ELocationID::class

            val pagination = article.paginationOrELocationID[0] as Pagination
            pagination.startPageOrEndPageOrMedlinePgn shouldHaveSize 1
            pagination.startPageOrEndPageOrMedlinePgn[0] shouldBeInstanceOf MedlinePgn::class
            val pgn = pagination.startPageOrEndPageOrMedlinePgn[0] as MedlinePgn
            pgn.getvalue() shouldBeEqualTo "1145-9"

            val elocationId = article.paginationOrELocationID[1] as ELocationID
            elocationId.validYN shouldBeEqualTo "Y"
            elocationId.getvalue() shouldBeEqualTo "10.1093/aje/kwu275"
            elocationId.eIdType shouldBeEqualTo "doi"

            article.abstract.abstractText shouldHaveSize 1
            article.abstract.abstractText[0].mixedContent[0].toString() shouldStartWith
                "The International Agency for Research on Cancer recently classified outdoor air pollution"

            val authorList = article.authorList
            authorList.completeYN shouldBeEqualTo "Y"
            authorList.type.shouldBeNull()
            authorList.author shouldHaveSize 9
            authorList.author.map { it.validYN } shouldContain "Y"
            authorList.author.map { it.validYN } shouldNotContain "N"

            val authorNames = authorList.author
                .flatMap { it.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName }
                .filterIsInstance<LastName>().map { it.getvalue() }
            authorNames shouldContainSame listOf(
                "Turner", "Cohen", "Jerrett", "Gapstur", "Diver", "Pope", "Krewski", "Beckerman", "Samet"
            )

            article.articleDate shouldHaveSize 1
            val articleDate = article.articleDate[0]
            articleDate.dateType shouldBeEqualTo "Electronic"
            articleDate.year.getvalue() shouldBeEqualTo "2014"

            val medlineJournalInfo = medlineCitation.medlineJournalInfo
            medlineJournalInfo.country shouldBeEqualTo "United States"
            medlineJournalInfo.medlineTA shouldBeEqualTo "Am J Epidemiol"
            medlineJournalInfo.nlmUniqueID shouldBeEqualTo "7910653"
            medlineJournalInfo.issnLinking shouldBeEqualTo "0002-9262"

            val chemicalList = medlineCitation.chemicalList
            chemicalList.chemical shouldHaveSize 3

            val meshHeadingList = medlineCitation.meshHeadingList
            meshHeadingList.meshHeading shouldHaveSize 20

            val keywordList = medlineCitation.keywordList
            keywordList shouldHaveSize 1
        } else {
            fail { "articleSet was null but was expected to be not" }
        }
    }

    @Test
    fun feedIntoScipamatoArticle_30124840() {
        val articles = getPubmedArticles(XML_30124840)
        articles shouldHaveSize 1
        with(articles.first()) {
            pmId shouldBeEqualTo "30124840"
            authors shouldBeEqualTo
                "Münzel T, Gori T, Al-Kindi S, Deanfield J, Lelieveld J, Daiber A, Rajagopalan S."
            firstAuthor shouldBeEqualTo "Münzel"
            publicationYear shouldBeEqualTo "2018"
            location shouldBeEqualTo
                "Eur Heart J. 2018 Aug 14. doi: 10.1093/eurheartj/ehy481. [Epub ahead of print]"
            title shouldBeEqualTo
                "Effects of gaseous and solid constituents of air pollution on endothelial function."
            doi shouldBeEqualTo "10.1093/eurheartj/ehy481"
        }
    }

    @Test
    fun feedIntoScipamatoArticle_29144419_withTagsInAbstract_canExtractAbstract() {
        val articles = getPubmedArticles(XML_29144419)
        articles shouldHaveSize 1
        with(articles.first()) {
            pmId shouldBeEqualTo "29144419"
            authors shouldBeEqualTo "Oudin A, Bråbäck L, Oudin Åström D, Forsberg B."
            firstAuthor shouldBeEqualTo "Oudin"
            publicationYear shouldBeEqualTo "2017"
            location shouldBeEqualTo "Int J Environ Res Public Health. 2017; 14 (11). pii: E1392."
            title shouldBeEqualTo
                "Air Pollution and Dispensed Medications for Asthma, and Possible Effect Modifiers Related to Mental " +
                "Health and Socio-Economy: A Longitudinal Cohort Study of Swedish Children and Adolescents."
            doi shouldBeEqualTo "10.3390/ijerph14111392"
            originalAbstract shouldStartWith
                "It has been suggested that children that are exposed to a stressful environment at home have " +
                "an increased susceptibility for air pollution-related asthma."
            originalAbstract shouldEndWith
                "We did not observe support for our hypothesis that stressors linked to socio-economy or " +
                "mental health problems would increase susceptibility to the effects of air pollution " +
                "on the development of asthma."
            originalAbstract.length shouldBeEqualTo 1844
        }
    }

    @Test
    fun feedIntoScipamatoArticle_30077140_withTagsInTitleAndAbstract_canExtractFullTitleAndAbstract() {
        val articles = getPubmedArticles(XML_30077140)
        articles shouldHaveSize 1
        with(articles.first()) {
            pmId shouldBeEqualTo "30077140"
            authors shouldBeEqualTo "Vodonos A, Awad YA, Schwartz J."
            firstAuthor shouldBeEqualTo "Vodonos"
            publicationYear shouldBeEqualTo "2018"
            location shouldBeEqualTo "Environ Res. 2018; 166: 677-689."
            title shouldBeEqualTo
                "The concentration-response between long-term PM2.5 exposure and mortality; A meta-regression approach."
            doi shouldBeEqualTo "10.1016/j.envres.2018.06.021"
            originalAbstract shouldStartWith
                "BACKGROUND: Long-term exposure to ambient fine particulate matter (≤ 2.5 μg/m3 in"
            originalAbstract shouldEndWith
                "The concentration -response function produced here can be further applied in the global " +
                "health risk assessment of air particulate matter."
            originalAbstract.length shouldBeEqualTo 2295
        }
    }

    companion object {
        private const val XML_2539026 = "xml/pubmed_result_25395026.xml"
        private const val XML_23454700 = "xml/pubmed_result_23454700.xml"
        private const val XML_26607712 = "xml/pubmed_result_26607712.xml"
        private const val XML_27214671 = "xml/pubmed_result_27214671.xml"
        private const val XML_27224452 = "xml/pubmed_result_27224452.xml"
        private const val XML_27258721 = "xml/pubmed_result_27258721.xml"
        private const val XML_30124840 = "xml/pubmed_result_30124840.xml"
        private const val XML_29144419 = "xml/pubmed_result_29144419.xml"
        private const val XML_30077140 = "xml/pubmed_result_30077140.xml"
    }
}
