package ch.difty.scipamato.core.pubmed

/**
 * Takes care of removing capital greek letters that visually look similar or even identical
 * to regular latin capital characters and replacing them with the regular ones.
 *
 * Needed for processing Author strings from Pubmed that would otherwise not pass the
 * validation constraint implemented in the Paper class.
 *
 * Identified in PM ID 35469927 where one of the authors contains a greek capital alpha.
 */
object GreekLetterTranslator {

    @Suppress("MagicNumber")
    private val greekLetterReplacement = mapOf(
        913 to 65,
        914 to 66,
        917 to 69,
        918 to 90,
        919 to 72,
        921 to 73,
        922 to 75,
        924 to 77,
        925 to 78,
        927 to 79,
        929 to 80,
        932 to 84,
        933 to 89,
        935 to 88,
    ).map { (key, value) -> Char(key) to Char(value) }

    fun replaceGreekLetters(original: String): String {
        var tmp = original
        greekLetterReplacement.forEach { (greek, replacement) ->
            tmp = tmp.replace(greek, replacement)
        }
        return tmp
    }
}
