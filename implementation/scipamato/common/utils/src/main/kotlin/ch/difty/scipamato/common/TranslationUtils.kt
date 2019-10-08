package ch.difty.scipamato.common

private const val LANG_CODE_BASE_LENGTH = 2

// TODO move into common-persistence-jooq
object TranslationUtils {

    const val NOT_TRANSL = "not translated"

    private val DE_CAMEL_REGEX = "(.)(\\p{Upper})".toRegex()

    /**
     * Converts an extended language code (e.g. de_CH) into the main one (de)
     */
    fun trimLanguageCode(languageCode: String): String =
            if (languageCode.length <= LANG_CODE_BASE_LENGTH) languageCode else languageCode.substring(0, LANG_CODE_BASE_LENGTH)

    /**
     * Converts a camel cased string [original] into an underscored one, e.g. `fooBar` -&gt; `foo_bar` (or null if original is null)
     */
    fun deCamelCase(original: String?): String? {
        return when {
            original == null -> null
            original.isEmpty() -> original
            else -> original.replace(DE_CAMEL_REGEX, "$1_$2").toLowerCase()
        }
    }


}
