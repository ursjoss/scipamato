package ch.difty.scipamato.core.entity.keyword;

import java.util.Arrays;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.AbstractDefinitionEntity;

/**
 * Entity used for managing the keywords in all defined languages.
 * This aggregate encapsulates the individual translations for all languages,
 * each captured in a {@link KeywordTranslation}.
 * <p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordDefinition extends AbstractDefinitionEntity<KeywordTranslation, Integer> {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @Nullable
    private Integer id;
    @Nullable
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
     */
    public KeywordDefinition(@Nullable final Integer id, @NotNull final String mainLanguageCode,
        @Nullable final Integer version, @NotNull final KeywordTranslation... translations) {
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
     */
    public KeywordDefinition(@Nullable final Integer id, @NotNull final String mainLanguageCode,
        @Nullable final String searchOverride, @Nullable final Integer version,
        @NotNull final KeywordTranslation... translations) {
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

    @NotNull
    @Override
    public Integer getNullSafeId() {
        return id != null ? id : 0;
    }
}
