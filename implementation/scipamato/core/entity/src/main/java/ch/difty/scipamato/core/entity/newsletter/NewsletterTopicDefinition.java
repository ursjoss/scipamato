package ch.difty.scipamato.core.entity.newsletter;

import java.util.Arrays;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.NullArgumentException;
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
     * @throws NullArgumentException
     *     if the mainLanguageCode is null
     */
    public NewsletterTopicDefinition(final Integer id, final String mainLanguageCode, final Integer version,
        final NewsletterTopicTranslation... translations) {
        super(AssertAs.INSTANCE.notNull(mainLanguageCode, "mainLanguageCode"), Arrays
            .stream(translations)
            .filter(tr -> mainLanguageCode.equals(tr.getLangCode()))
            .map(NewsletterTopicTranslation::getName)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("n.a."), version, translations);
        setId(id);
    }

    @Override
    public Integer getNullSafeId() {
        return getId() != null ? getId() : 0;
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    public String getTitle() {
        return getName();
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    public void setTitle(String title) {
        setName(title);
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    public String getTitleInLanguage(final String langCode) {
        return getNameInLanguage(langCode);
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    public void setTitleInLanguage(final String langCode, String title) {
        setNameInLanguage(langCode, title);
    }
}
