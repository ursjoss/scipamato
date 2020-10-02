package ch.difty.scipamato.common.persistence

const val NOT_TRANSL = "not translated"

private const val LANG_CODE_BASE_LENGTH = 2
private val DE_CAMEL_REGEX = "(.)(\\p{Upper})".toRegex()

/**
 * Converts an extended language code (e.g. de_CH) into the main one (de)
 */
fun trimLanguageCode(languageCode: String): String =
    if (languageCode.length <= LANG_CODE_BASE_LENGTH) languageCode
    else languageCode.substring(0, LANG_CODE_BASE_LENGTH)

/**
 * Converts a camel cased string [original] into an underscored one,
 * e.g. `fooBar` -&gt; `foo_bar`
 */
fun deCamelCase(original: String): String = when {
    original.isEmpty() -> original
    else -> original.replace(DE_CAMEL_REGEX, "$1_$2").toLowerCase()
}
