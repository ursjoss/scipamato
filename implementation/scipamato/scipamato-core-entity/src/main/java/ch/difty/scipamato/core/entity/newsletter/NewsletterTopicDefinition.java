package ch.difty.scipamato.core.entity.newsletter;

import static java.util.stream.Collectors.toMap;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.NullArgumentException;

/**
 * Entity used for managing the newsletter topics in all defined languages.
 * This aggregate encapsulates the individual translations for all languages,
 * each captured in a {@link NewsletterTopicTranslation}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsletterTopicDefinition extends NewsletterTopic {
    private static final long serialVersionUID = 1L;

    private final String mainLanguageCode;

    private final Map<String, NewsletterTopicTranslation> translations;

    /**
     * Instantiate a new NewsletterTopicDefinition.
     *
     * @param id
     *     the aggregate id
     * @param mainLanguageCode
     *     the languageCode of the main language
     * @param translations
     *     translations for all relevant languages
     * @throws NullArgumentException
     *     if the mainLanguageCode is null
     */
    public NewsletterTopicDefinition(final Integer id, final String mainLanguageCode,
        final NewsletterTopicTranslation... translations) {
        super(id, Arrays
            .stream(translations)
            .filter(tr -> AssertAs
                .notNull(mainLanguageCode, "mainLanguageCode")
                .equals(tr.getLangCode()))
            .map(NewsletterTopicTranslation::getTitle)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("n.a."));
        this.mainLanguageCode = mainLanguageCode;
        this.translations = Arrays
            .stream(translations)
            .collect(toMap(NewsletterTopicTranslation::getLangCode, Function.identity()));
    }

    /**
     * Sets the title of the specified translation. If the language code matches the main language code set
     * during object construction, the aggregates title is set as well.
     * <p>
     * In case the specified language code does not exist in the predefined languages set during object construction,
     * the call to setTitleInLanguage will simply be ignored. It will not add additional languages.
     *
     * @param langCode
     *     the language code of the translation to be modified. Must not be null.
     * @param translatedTitle
     *     the translated topic description.
     * @throws NullArgumentException
     *     if the langCode is null
     */
    public void setTitleInLanguage(final String langCode, final String translatedTitle) {
        final NewsletterTopicTranslation tr = translations.get(AssertAs.notNull(langCode, "langCode"));
        if (tr != null) {
            tr.setTitle(translatedTitle);
            tr.setLastModified(LocalDateTime.now());
            if (mainLanguageCode.equals(langCode))
                setTitle(translatedTitle);
        }
    }

    /**
     * Get the topic title in the specified language.
     *
     * @param langCode
     *     the language to get the specified topic for
     * @return the topic title - or null if none is available.
     */
    public String getTitleInLanguage(final String langCode) {
        final NewsletterTopicTranslation tr = translations.get(AssertAs.notNull(langCode, "langCode"));
        return tr != null ? tr.getTitle() : null;
    }

    public String getTranslationsAsString() {
        final NewsletterTopicTranslation mainTitle = translations.get(mainLanguageCode);
        if (mainTitle != null) {
            final StringBuilder title = new StringBuilder();
            title.append(mainLanguageCode.toUpperCase());
            title.append(": '");
            title.append(mainTitle.getTitle());
            title.append("'");
            for (final Map.Entry<String, NewsletterTopicTranslation> entry : translations.entrySet()) {
                final String code = entry.getKey();
                if (!code.equals(mainLanguageCode)) {
                    if (title.length() > 0)
                        title.append("; ");
                    title
                        .append(code.toUpperCase())
                        .append(": ");
                    final String titleString = entry
                        .getValue()
                        .getTitle();
                    if (titleString != null)
                        title
                            .append("'")
                            .append(titleString)
                            .append("'");
                    else
                        title.append("n.a.");
                }
            }
            return title.toString();
        }
        return null;
    }
}
