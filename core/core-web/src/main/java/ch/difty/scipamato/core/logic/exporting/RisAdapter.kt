@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.logic.exporting

import ch.difty.kris.KRis
import ch.difty.kris.domain.RisRecord
import ch.difty.kris.domain.RisType
import ch.difty.scipamato.core.entity.Paper
import java.io.Serializable

private val defaultSort: List<String> = listOf(
    "AU", "PY", "TI", "JO", "SP", "EP", "VL", "IS", "ID", "DO", "M1", "M2", "AB", "DB", "L1", "L2"
)
private val defaultDistillerSort: List<String> = listOf(
    "AU", "PY", "TI", "JO", "SP", "EP", "M2", "VL", "IS", "ID", "DO", "M1", "C1", "AB", "DB", "L1", "L2"
)

private const val LOCATION_PARTS = 6

/**
 * The implementation of the [RisAdapterFactory] provides a configured [RisAdapter] able export to
 * different flavours of RIS files.
 */
interface RisAdapterFactory {

    /**
     * Creates an implementation of a [RisAdapter] depending on the provided `risExporter string`.
     */
    fun createRisAdapter(brand: String, internalUrl: String?, publicUrl: String?): RisAdapter

    companion object {
        fun create(risExporterStrategy: RisExporterStrategy) = object : RisAdapterFactory {
            override fun createRisAdapter(brand: String, internalUrl: String?, publicUrl: String?): RisAdapter =
                when (risExporterStrategy) {
                    RisExporterStrategy.DEFAULT -> DefaultRisAdapter(brand, internalUrl, publicUrl)
                    RisExporterStrategy.DISTILLERSR -> DistillerSrRisAdapter(brand, internalUrl, publicUrl)
                }
        }
    }
}

interface RisAdapter : Serializable {
    fun build(papers: List<Paper>) = build(papers, defaultSort)
    fun build(papers: List<Paper>, sort: List<String>): String
}

/**
 * Adapter working as a kind of bridge between [Paper]s and the JRis world.
 */
sealed class JRisAdapter(
    protected val dbName: String,
    protected val internalUrl: String?,
    protected val publicUrl: String?
) : RisAdapter {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun build(papers: List<Paper>, sort: List<String>): String =
        KRis.buildFromList(
            risRecords = papers.map(::toRisRecords).toList(),
            sort = sort
        ).joinToString(separator = "")

    @Suppress("DestructuringDeclarationWithTooManyEntries")
    private fun toRisRecords(p: Paper): RisRecord {
        val (periodical, volume, issue, startPage, endPage) = p.locationComponents()
        return newRisRecord(p, startPage, endPage, periodical, volume, issue)
    }

    @Suppress("LongParameterList")
    protected abstract fun newRisRecord(
        p: Paper,
        startPage: Long?,
        endPage: Long?,
        periodical: String,
        volume: String?,
        issue: String?
    ): RisRecord

    protected fun Paper.formattedAuthors(): List<String> {
        val formattedAuthors = authors
            .removeSuffix(AUTHOR_SUFFIX)
            .split(AUTHOR_DELIM)
            .map { it.trim() }
            .map(::toRisAuthor)
            .toList()
        check(formattedAuthors.isNotEmpty()) { throw IllegalStateException("paper must have at least one author") }
        return formattedAuthors
    }

    private fun toRisAuthor(scipamatoAuthor: String): String {
        val matchResult = authorRegex.matchEntire(scipamatoAuthor)
        matchResult?.let {
            val groups = matchResult.groupValues.drop(1).map { it.trim() }.filter { it.isNotEmpty() }
            val lastNames = groups.first()
            val firstAndStuff = groups.drop(1).joinToString("$AUTHOR_SUFFIX$AUTHOR_DELIM").plus(AUTHOR_SUFFIX)
            return "$lastNames,$firstAndStuff"
        }
        return "$scipamatoAuthor$AUTHOR_SUFFIX"
    }

    private fun Paper.locationComponents(): LocationComponents {
        val matchResult = locationRegex.matchEntire(location)
        matchResult?.let { result ->
            val groups = result.groupValues
            check(groups.size == LOCATION_PARTS) {
                throw IllegalStateException("Unable to parse '$location' - should have been LOCATION_PARTS parts")
            }
            var i = 1
            return LocationComponents(
                periodical = groups[i++],
                volume = groups[i++],
                issue = groups[i++].takeUnless { it.trim().isBlank() },
                startPage = groups[i++].run { if (isNotEmpty()) toLongOrNull() else null },
                endPage = groups[i].run { if (isNotEmpty()) toLongOrNull() else null }
            )
        }
        return LocationComponents(location)
    }

    private data class LocationComponents(
        val periodical: String,
        val volume: String? = null,
        val issue: String? = null,
        val startPage: Long? = null,
        val endPage: Long? = null
    )

    companion object {
        private const val serialVersionUID: Long = 1L
        private const val AUTHOR_DELIM = ","
        private const val AUTHOR_SUFFIX = "."

        private const val RE_W =
            """\w\u00C0-\u024f"""
        private const val RW_WW =
            """[$RE_W-']+"""

        private val authorRegex =
            """^((?:$RW_WW ?)+) ([A-Z]+)(?: (Sr))?$""".toRegex()
        private val locationRegex =
            """^([^.]+)\. \d+; (\d+(?:-\d+)?)(?: \(([\d-]+)\))?: (\d+)?(?:-(\d+))?(?:\.? ?e\d+)?\.$""".toRegex()
    }
}

/**
 * Default mapping of SciPaMaTo fields to the RIS tag that seem most appropriate.
 */
class DefaultRisAdapter(
    dbName: String,
    internalUrl: String?,
    publicUrl: String?
) : JRisAdapter(dbName, internalUrl, publicUrl) {
    override fun newRisRecord(
        p: Paper,
        startPage: Long?,
        endPage: Long?,
        periodical: String,
        volume: String?,
        issue: String?
    ): RisRecord =
        RisRecord(
            type = RisType.JOUR,
            referenceId = p.pmId?.toString(),
            title = p.title,
            authors = p.formattedAuthors().toMutableList(),
            publicationYear = p.publicationYear?.toString(),
            startPage = startPage?.toString(),
            endPage = endPage?.toString(),
            periodicalNameFullFormatJO = periodical,
            volumeNumber = volume,
            issue = issue,
            abstr = p.originalAbstract,
            pdfLinks = publicUrl?.run { mutableListOf("${this}paper/number/${p.number}") } ?: mutableListOf(),
            miscellaneous1 = p.number?.toString(),
            fullTextLinks = internalUrl?.run { mutableListOf("$this${p.pmId}") } ?: mutableListOf(),
            miscellaneous2 = p.goals?.takeUnless { it.trim().isBlank() },
            doi = p.doi?.takeUnless { it.trim().isBlank() },
            databaseName = dbName
        )

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

/**
 * Mapping of SciPaMaTo-fields to the RIS tags expected to be imported into DistillerSR. Apparently
 * the folks at Evidence Partners had to adapt the import into their tool to be able to handle
 * importing from some other exporters:
 *
 * * `M2` is mapped to `Start Page` (instead of `SP`)
 * * `SP` is mapped to `Pages`
 */
class DistillerSrRisAdapter(
    dbName: String,
    internalUrl: String?,
    publicUrl: String?
) : JRisAdapter(dbName, internalUrl, publicUrl) {

    override fun build(papers: List<Paper>) = build(papers, defaultDistillerSort)
    override fun newRisRecord(
        p: Paper,
        startPage: Long?,
        endPage: Long?,
        periodical: String,
        volume: String?,
        issue: String?
    ): RisRecord =
        RisRecord(
            type = RisType.JOUR,
            referenceId = p.pmId?.toString(),
            title = p.title,
            authors = p.formattedAuthors().toMutableList(),
            publicationYear = p.publicationYear?.toString(),
            miscellaneous2 = startPage?.toString(),
            startPage = getPages(startPage, endPage),
            endPage = endPage?.toString(),
            periodicalNameFullFormatJO = periodical,
            volumeNumber = volume,
            issue = issue,
            abstr = p.originalAbstract,
            pdfLinks = publicUrl?.run { mutableListOf("${this}paper/number/${p.number}") } ?: mutableListOf(),
            miscellaneous1 = p.number?.toString(),
            fullTextLinks = internalUrl?.run { mutableListOf("$this${p.pmId}") } ?: mutableListOf(),
            custom1 = p.goals?.takeUnless { it.trim().isBlank() },
            doi = p.doi?.takeUnless { it.trim().isBlank() },
            databaseName = dbName
        )

    private fun getPages(sp: Long?, ep: Long?) = when {
        sp == null && ep == null -> null
        ep == null -> sp.toString()
        sp == null -> ep.toString()
        else -> "$sp-$ep"
    }

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
