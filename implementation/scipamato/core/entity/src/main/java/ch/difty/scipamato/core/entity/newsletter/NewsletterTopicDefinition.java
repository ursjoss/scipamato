package ch.difty.scipamato.core.entity.newsletter;

import java.util.Arrays;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.AbstractDefinitionEntity;

/**
 * Entity used for managing the newsletter topics in all defined languages.
 * This aggregate encapsulates the individual translations for all languages,
 * each captured in a {@link NewsletterTopicTranslation}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsletterTopicDefinition extends AbstractDefinitionEntity<NewsletterTopicTranslation, Integer> {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * Instantiate a new NewsletterTopicDefinition.
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
    public NewsletterTopicDefinition(@Nullable final Integer id, @NotNull final String mainLanguageCode,
        @Nullable final Integer version, @NotNull final NewsletterTopicTranslation... translations) {
        super(mainLanguageCode, Arrays
            .stream(translations)
            .filter(tr -> mainLanguageCode.equals(tr.getLangCode()))
            .map(NewsletterTopicTranslation::getName)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("n.a."), version, translations);
        setId(id);
    }

    @NotNull
    @Override
    public Integer getNullSafeId() {
        return getId() != null ? getId() : 0;
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    @NotNull
    public String getTitle() {
        return getName();
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    public void setTitle(@NotNull final String title) {
        setName(title);
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    @Nullable
    public String getTitleInLanguage(@NotNull final String langCode) {
        return getNameInLanguage(langCode);
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    public void setTitleInLanguage(@NotNull final String langCode, @NotNull String title) {
        setNameInLanguage(langCode, title);
    }
}
