@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.pubmed

import org.springframework.http.HttpStatus

private val errorRegex =
    """.+\{"error":"([^"]*)",.+}""".toRegex(RegexOption.DOT_MATCHES_ALL)
private val reasonRegex =
    """.+Reason: <strong>([^<]+)</strong>+.+""".toRegex(RegexOption.DOT_MATCHES_ALL)

/**
 * Data Class providing the [PubmedArticleFacade] or an error specific message
 * providing information about the problem that prevented the retrieval of the article.
 */
class PubmedArticleResult(
    val pubmedArticleFacade: PubmedArticleFacade?,
    httpStatus: HttpStatus?,
    rawMessage: String?
) {
    val errorMessage: String = evaluateMessageFrom(httpStatus, rawMessage ?: "")

    private fun evaluateMessageFrom(httpStatus: HttpStatus?, rawMessage: String): String =
        when {
            pubmedArticleFacade != null -> ""
            httpStatus == null -> rawMessage
            else -> when (httpStatus) {
                HttpStatus.OK -> rawMessage
                HttpStatus.BAD_REQUEST -> wrap(httpStatus, errorRegex.extractFrom(rawMessage))
                HttpStatus.BAD_GATEWAY -> wrap(httpStatus, reasonRegex.extractFrom(rawMessage))
                else -> wrap(httpStatus, prependColumn(rawMessage))
            }
        }

    private fun wrap(httpStatus: HttpStatus, msg: String): String = "Status ${httpStatus}$msg"
    private fun prependColumn(msg: String): String = if (msg.isBlank()) "" else ": $msg"

    private fun Regex.extractFrom(rawMessage: String): String =
        when (val result = matchEntire(rawMessage)) {
            null -> prependColumn(rawMessage)
            else -> prependColumn(result.groups[1]?.value ?: "")
        }
}
