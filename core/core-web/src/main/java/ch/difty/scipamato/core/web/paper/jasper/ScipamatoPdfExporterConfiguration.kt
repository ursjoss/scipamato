package ch.difty.scipamato.core.web.paper.jasper

import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.User
import net.sf.jasperreports.pdf.SimplePdfExporterConfiguration

/**
 * Custom implementation of the [SimplePdfExporterConfiguration] which is clusterable
 * and uses the builder pattern to set the parameters relevant to scipamato.
 *
 * @author u.joss
 */
class ScipamatoPdfExporterConfiguration private constructor(builder: Builder) :
    SimplePdfExporterConfiguration(), ClusterablePdfExporterConfiguration {

    private fun makeTitle(builder: Builder): String {
        val sb = StringBuilder()
        builder.title?.let { sb.append(it) }
        if (sb.isNotEmpty() && (builder.paperAuthor != null || builder.paperTitle != null)) sb.append(" - ")
        builder.paperAuthor?.let { sb.append(it).append(" et al.") }
        if (builder.paperAuthor != null && builder.paperTitle != null) sb.append(": ")
        builder.paperTitle?.let { sb.append(it) }
        return sb.toString()
    }

    class Builder {
        val title: String?
        var author: String? = null
        var paperTitle: String? = null
        var paperAuthor: String? = null
        var subject: String? = null
        var creator: String? = null
        var keywords: String? = null
        var compression = false

        /**
         * Derive the metadata title from [headerPart] and the paper [number] (if you also
         * set the paperTitle, it will be appended as well...)
         */
        constructor(headerPart: String?, number: Long?) {
            val sb = StringBuilder()
            headerPart?.let { sb.append(headerPart) }
            number?.let {
                if (sb.isNotEmpty()) sb.append(" ")
                sb.append(it)
            }
            title = if (sb.isNotEmpty()) sb.toString() else null
        }

        /**
         * Set the metadata [title] directly. (if you also set the paperTitle, it will be
         * appended as well...)
         */
        constructor(title: String?) {
            this.title = title
        }

        fun withAuthor(author: String?): Builder {
            this.author = author
            return this
        }

        fun withAuthor(user: User?): Builder {
            user?.let { author = "${it.firstName} ${it.lastName}" }
            return this
        }

        fun withPaperTitle(paperTitle: String?): Builder {
            this.paperTitle = paperTitle
            return this
        }

        fun withPaperAuthor(paperAuthor: String?): Builder {
            this.paperAuthor = paperAuthor
            return this
        }

        fun withSubject(subject: String?): Builder {
            this.subject = subject
            return this
        }

        fun withCreator(creator: String?): Builder {
            this.creator = creator
            return this
        }

        fun withCompression(): Builder {
            compression = true
            return this
        }

        fun withCodes(codes: List<Code?>?): Builder {
            codes?.let {
                val sb = StringBuilder()
                it.filterNotNull().forEach { c ->
                    if (sb.isNotEmpty()) sb.append(",")
                    val codeName = c.name
                    val hasSpaces = codeName.hasSpaces()
                    if (hasSpaces) sb.append(QUOTE)
                    sb.append(codeName)
                    if (hasSpaces) sb.append(QUOTE)
                }
                keywords = sb.toString()
            }
            return this
        }

        private fun String.hasSpaces(): Boolean = contains(" ")

        fun build(): ScipamatoPdfExporterConfiguration = ScipamatoPdfExporterConfiguration(this)

        companion object {
            private const val QUOTE = "\""
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }

    init {
        builder.author?.let { metadataAuthor = it }
        if (builder.title != null || builder.paperTitle != null) metadataTitle = makeTitle(builder)
        metadataTitle?.let { isDisplayMetadataTitle = true }
        builder.subject?.let { metadataSubject = it }
        builder.creator?.let { metadataCreator = it }
        builder.keywords?.let {
            metadataKeywords = it
            isTagged = true
        }
        isCompressed = builder.compression
    }
}
