package ch.difty.scipamato.core.persistence.paper.searchorder

const val NL = "\n"

internal fun concat(vararg strings: String): String {
    val sb = StringBuilder()
    for (s in strings)
        sb.append(s).append(NL)
    return if (sb.length > NL.length) {
        sb.substring(0, sb.length - NL.length)
    } else {
        sb.toString()
    }
}
