@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.logic.exporting

import ch.difty.scipamato.core.entity.Paper
import com.gmail.gcolaianni5.jris.JRis
import com.gmail.gcolaianni5.jris.RisRecord
import com.gmail.gcolaianni5.jris.RisType
import java.io.Serializable


private val defaultSort: List<String> = listOf("AU", "PY", "TI", "JO", "SP", "EP", "VL", "IS", "ID", "DO", "M1", "M2", "M3", "AB", "DB", "L1", "L2")

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
sealed class JRisAdapter(protected val dbName: String, protected val internalUrl: String?, protected val publicUrl: String?) : RisAdapter {

    override fun build(papers: List<Paper>, sort: List<String>): String =
            JRis.build(
                    records = papers.map(::toRisRecords).toList(),
                    sort = sort
            )

    private fun toRisRecords(p: Paper): RisRecord {
        val (periodical, volume, issue, startPage, endPage) = p.locationComponents()
        return newRisRecord(p, startPage, endPage, periodical, volume, issue)
    }

    protected abstract fun newRisRecord(p: Paper, startPage: Int?, endPage: Int?, periodical: String, volume: String?, issue: String?): RisRecord

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
        matchResult?.let {
            val groups = it.groupValues
            check(groups.size == 6) { throw IllegalStateException("Unable to parse '$location' - should have been 5 parts") }
            return LocationComponents(
                    periodical = groups[1],
                    volume = groups[2],
                    issue = groups[3].takeUnless { it.trim().isBlank() },
                    startPage = groups[4].run { if (isNotEmpty()) toInt() else null },
                    endPage = groups[5].run { if (isNotEmpty()) toInt() else null }
            )
        }
        return LocationComponents(location)
    }

    private data class LocationComponents(
            val periodical: String,
            val volume: String? = null,
            val issue: String? = null,
            val startPage: Int? = null,
            val endPage: Int? = null
    )

    companion object {
        private const val AUTHOR_DELIM = ","
        private const val AUTHOR_SUFFIX = "."

        private const val RE_W = """\w\u00C0-\u024f"""
        private const val RW_WW = """[$RE_W-']+"""

        private val authorRegex = """^((?:$RW_WW ?)+) ([A-Z]+)(?: (Sr))?$""".toRegex()
        private val locationRegex = """^([^.]+)\. \d+; (\d+(?:-\d+)?)(?: \(((?:[\d-])+)\))?: (\d+)?(?:-(\d+))?(?:\.? ?e\d+)?\.$""".toRegex()
    }
}

class DefaultRisAdapter(dbName: String, internalUrl: String?, publicUrl: String?) : JRisAdapter(dbName, internalUrl, publicUrl) {
    override fun newRisRecord(p: Paper, startPage: Int?, endPage: Int?, periodical: String, volume: String?, issue: String?): RisRecord =
            RisRecord(
                    type = RisType.JOUR,
                    referenceId = p.pmId?.toString(),
                    title = p.title,
                    authors = p.formattedAuthors().toMutableList(),
                    publicationYear = p.publicationYear?.toString(),
                    startPage = startPage,
                    endPage = endPage,
                    periodicalNameFullFormatJO = periodical,
                    volumeNumber = volume,
                    issue = issue,
                    abstr = p.originalAbstract,
                    pdfLinks = publicUrl?.run { mutableListOf("${this}paper/number/${p.number}") } ?: mutableListOf(),
                    number = p.number?.toLong(),
                    fullTextLinks = internalUrl?.run { mutableListOf("$this${p.pmId}") } ?: mutableListOf(),
                    miscellaneous2 = p.goals?.takeUnless { it.trim().isBlank() },
                    doi = p.doi?.takeUnless { it.trim().isBlank() },
                    databaseName = dbName
            )
}

class DistillerSrRisAdapter(dbName: String, internalUrl: String?, publicUrl: String?) : JRisAdapter(dbName, internalUrl, publicUrl) {
    override fun newRisRecord(p: Paper, startPage: Int?, endPage: Int?, periodical: String, volume: String?, issue: String?): RisRecord =
            RisRecord(
                    type = RisType.JOUR,
                    referenceId = p.pmId?.toString(),
                    title = p.title,
                    authors = p.formattedAuthors().toMutableList(),
                    publicationYear = p.publicationYear?.toString(),
                    miscellaneous2 = getPages(startPage, endPage),
                    periodicalNameFullFormatJO = periodical,
                    volumeNumber = volume,
                    issue = issue,
                    abstr = p.originalAbstract,
                    pdfLinks = publicUrl?.run { mutableListOf("${this}paper/number/${p.number}") } ?: mutableListOf(),
                    number = p.number?.toLong(),
                    fullTextLinks = internalUrl?.run { mutableListOf("$this${p.pmId}") } ?: mutableListOf(),
                    typeOfWork = p.goals?.takeUnless { it.trim().isBlank() },
                    doi = p.doi?.takeUnless { it.trim().isBlank() },
                    databaseName = dbName
            )

    private fun getPages(sp: Int?, ep: Int?) =
            if (sp == null && ep == null)
                null
            else if (ep == null)
                sp.toString()
            else if (sp == null)
                ep.toString()
            else
                "$sp-$ep"
}
