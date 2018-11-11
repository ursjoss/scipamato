package ch.difty.scipamato.core.entity.keyword;

import java.util.Arrays;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.entity.AbstractDefinitionEntity;

/**
 * Entity used for managing the keywords in all defined languages.
 * This aggregate encapsulates the individual translations for all languages,
 * each captured in a {@link KeywordTranslation}.
 * <p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordDefinition extends AbstractDefinitionEntity<KeywordDefinition, KeywordTranslation, Integer> {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String  searchOverride;

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
        this(id, mainLanguageCode, null, version, translations);
    }

    /**
     * Instantiate a new KeywordDefinition.
     *
     * @param id
     *     the aggregate id
     * @param mainLanguageCode
     *     the languageCode of the main language
     * @param searchOverride
     *     the search override
     * @param version
     *     audit field version
     * @param translations
     *     translations for all relevant languages
     * @throws NullArgumentException
     *     if the mainLanguageCode is null
     */
    public KeywordDefinition(final Integer id, final String mainLanguageCode, final String searchOverride,
        final Integer version, final KeywordTranslation... translations) {
        super(mainLanguageCode, Arrays
            .stream(translations)
            .filter(tr -> mainLanguageCode.equals(tr.getLangCode()))
            .map(KeywordTranslation::getName)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("n.a."), version, translations);
        setId(id);
        setSearchOverride(searchOverride);
    }

    @Override
    public Integer getNullSafeId() {
        return id != null ? id : 0;
    }

}
