package ch.difty.scipamato.common.entity

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

/**
 * Interface for entities that have translations in multiple languages.
 * This interface is used in the scenario where we want to actually maintain
 * those entities in all defined languages. This is to distinguish from the
 * other scenario where we only want to display such data in one of the
 * languages, e.g. specified by the locale of the browser.
 *
 * The name "Definition" was not very considerate and I'm planning to refactor
 * this one day. In fact those Definition* classes should rather be some kind
 * of Entity class while the normal entities are closer to projections or DTOs.
 * But that's something to consider for the future when the important functionality
 * is implemented.
 *
 * The DefinitionEntity has an id of type [ID] and contains a set of DefinitionTranslations of type [T].
 */
interface DefinitionEntity<ID, T> : Serializable {
    val nullSafeId: ID?
    val displayValue: String
    val translationsAsString: String?

    fun getTranslations(langCode: String? = null): List<T>
    fun addTranslation(langCode: String, translation: T)
    fun removeTranslation(translation: T)
}

/**
 * Translation of the to be translated parts of a [DefinitionEntity] (see class javadoc there).
 * The DefinitionEntity contains multiple of those translations, one for each language.
 */
interface DefinitionTranslation : Serializable {
    val langCode: String
    var name: String?
    val displayValue: String
    var lastModified: LocalDateTime?
}

/**
 * Abstract base class comprising of the state and behavior common to all [DefinitionEntity] implementations.
 * It contains definition translations of type  [T] and an id of type [ID].
 */
// TODO directly implement as data classes
abstract class AbstractDefinitionEntity<T : DefinitionTranslation, ID>(
    val mainLanguageCode: String,
    mainName: String,
    version: Int? = 0, // TODO make non-nullable
    translationArray: Array<out T>,
) : ScipamatoEntity(version = version ?: 0), DefinitionEntity<ID, T> {

    private val translations: MutableMap<String, List<T>> = translationArray.groupBy { it.langCode }.toMutableMap()

    var name = mainName

    /**
     * Returns the translations in the following scenarios:
     * - for a given language if [langCode] is provided as a parameter and the language is defined.
     * - emptyList if [langCode] is provided but not defined.
     * - all translations if [langCode] is `null`
     */
    override fun getTranslations(langCode: String?): List<T> =
        if (langCode == null) translations.flatMap { it.value }
        else translations[langCode] ?: emptyList()

    override fun addTranslation(langCode: String, translation: T) {
        translations[langCode] = translations.getOrDefault(langCode, emptyList()) + listOf(translation)
    }

    override fun removeTranslation(translation: T) {
        translations[translation.langCode] =
            translations.getOrDefault(translation.langCode, emptyList())
                .filterNot { it.name == translation.name }
    }

    override val displayValue: String
        get() = name

    override val translationsAsString: String?
        get() {
            val trs = getTranslations(mainLanguageCode)
            if (trs.isEmpty()) return null

            val mainNames = trs.mapNotNull { it.name }.joinToString("','")
            val sb = StringBuilder()
            sb.append("${mainLanguageCode.uppercase(Locale.getDefault())}: ")
            sb.append(if (mainNames.isEmpty()) "n.a." else "'$mainNames'")
            translations.filterNot { it.key == mainLanguageCode }.forEach { (kw, value) ->
                sb.append("; ")
                sb.append(kw.uppercase(Locale.getDefault())).append(": ")
                val nameString = value.mapNotNull { it.name }.joinToString("','")
                if (nameString.isNotBlank()) sb.append("'$nameString'") else sb.append("n.a.")
            }
            return sb.toString()
        }

    fun setNameInLanguage(langCode: String, translatedName: String) {
        getTranslations(langCode).firstOrNull()?.let { tr ->
            tr.name = translatedName
            tr.lastModified = LocalDateTime.now()
            if (mainLanguageCode == langCode) name = translatedName
        }
    }

    /**
     * Get the **first** code name in the specified language.
     */
    fun getNameInLanguage(langCode: String): String? {
        val trs = translations[langCode]
        return if (trs?.isEmpty() != false) null else trs[0].name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as AbstractDefinitionEntity<*, *>

        if (mainLanguageCode != other.mainLanguageCode) return false
        if (translations != other.translations) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + mainLanguageCode.hashCode()
        result = 31 * result + translations.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString() =
        "AbstractDefinitionEntity[translations=$translationsAsString, mainLanguageCode=$mainLanguageCode, name=$name]"

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

// TODO directly implement as data classes
abstract class AbstractDefinitionTranslation(
    var id: Int?,
    override val langCode: String,
    override var name: String?,
    version: Int? = 0 // TODO make non-nullable
) : ScipamatoEntity(version = version ?: 0), DefinitionTranslation {

    override val displayValue: String
        get() = "$langCode: $name"

    override fun toString(): String = "AbstractDefinitionTranslation(id=$id, langCode=$langCode, name=$name)"

    enum class DefinitionTranslationFields(override val fieldName: String) : FieldEnumType {
        ID("id"),
        LANG_CODE("langCode"),
        NAME("name")
    }

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
