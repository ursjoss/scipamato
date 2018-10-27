package ch.difty.scipamato.core.entity.keyword;

import static java.util.stream.Collectors.joining;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.NullArgumentException;

/**
 * Entity used for managing the keywords in all defined languages.
 * This aggregate encapsulates the individual translations for all languages,
 * each captured in a {@link KeywordTranslation}.
 * <p>
 * TODO add method to modify other tranlsations within one language
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordDefinition extends Keyword {
    private static final long serialVersionUID = 1L;

    private final String mainLanguageCode;

    private final ListValuedMap<String, KeywordTranslation> translations;

    /**
     * Instantiate a new KeywordDefinition.
     *
     * @param id
     *     the aggregate id
     * @param mainLanguageCode
     *     the languageCode of the main language
     * @param version
     *     audit field version
     * @param translations
     *     translations for all relevant languages
     * @throws NullArgumentException
     *     if the mainLanguageCode is null
     */
    public KeywordDefinition(final Integer id, final String mainLanguageCode, final Integer version,
        final KeywordTranslation... translations) {
        super(id, Arrays
            .stream(translations)
            .filter(tr -> AssertAs
                .notNull(mainLanguageCode, "mainLanguageCode")
                .equals(tr.getLangCode()))
            .map(KeywordTranslation::getName)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("n.a."), Arrays
            .stream(translations)
            .filter(tr -> AssertAs
                .notNull(mainLanguageCode, "mainLanguageCode")
                .equals(tr.getLangCode()))
            .map(KeywordTranslation::getSearchOverride)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null));
        this.mainLanguageCode = AssertAs.notNull(mainLanguageCode, "mainLanguageCode");
        this.translations = new ArrayListValuedHashMap<>();
        for (final KeywordTranslation kt : translations) {
            final String langCode = kt.getLangCode();
            this.translations.put(langCode, kt);
        }
        setVersion(version != null ? version : 0);
    }

    /**
     * Sets the name of the <b>first </b> item (if there are multiple) of the specified translation.
     * If the language code matches the main language code set during object construction, the aggregates name is set as well.
     * <p>
     * In case the specified language code does not exist in the predefined languages set during object construction,
     * the call to setNameInLanguage will simply be ignored. It will not add additional languages.
     *
     * @param langCode
     *     the language code of the translation to be modified. Must not be null.
     * @param translatedName
     *     the translated keyword name.
     * @throws NullArgumentException
     *     if the langCode is null
     */
    public void setNameInLanguage(final String langCode, final String translatedName) {
        final Collection<KeywordTranslation> trs = translations.get(AssertAs.notNull(langCode, "langCode"));
        if (CollectionUtils.isNotEmpty(trs)) {
            final KeywordTranslation tr = trs
                .iterator()
                .next();
            tr.setName(translatedName);
            tr.setLastModified(LocalDateTime.now());
            if (mainLanguageCode.equals(langCode))
                setName(translatedName);
        }
    }

    /**
     * Get the <b>first</b> keyword name in the specified language.
     *
     * @param langCode
     *     the language to get the specified name for
     * @return the keyword - or null if none is available.
     */
    public String getNameInLanguage(final String langCode) {
        final Collection<KeywordTranslation> trs = translations.get(AssertAs.notNull(langCode, "langCode"));
        if (CollectionUtils.isEmpty(trs))
            return null;
        final KeywordTranslation tr = trs
            .iterator()
            .next();
        return tr != null ? tr.getName() : null;
    }

    public String getTranslationsAsString() {
        final Collection<KeywordTranslation> trs = translations.get(mainLanguageCode);
        if (CollectionUtils.isEmpty(trs))
            return null;
        final StringBuilder name = new StringBuilder();
        name.append(mainLanguageCode.toUpperCase());
        name.append(": '");
        name.append(trs
            .stream()
            .map(KeywordTranslation::getName)
            .collect(joining("','")));
        name.append("'");
        for (final Map.Entry<String, Collection<KeywordTranslation>> entry : translations
            .asMap()
            .entrySet()) {
            final String kw = entry.getKey();
            if (!kw.equals(mainLanguageCode)) {
                if (name.length() > 0)
                    name.append("; ");
                name
                    .append(kw.toUpperCase())
                    .append(": ");
                final String nameString = entry
                    .getValue()
                    .stream()
                    .map(KeywordTranslation::getName)
                    .filter(Objects::nonNull)
                    .collect(joining("','"));
                if (StringUtils.isNotBlank(nameString))
                    name
                        .append("'")
                        .append(nameString)
                        .append("'");
                else
                    name.append("n.a.");
            }
        }
        return name.toString();
    }
}
