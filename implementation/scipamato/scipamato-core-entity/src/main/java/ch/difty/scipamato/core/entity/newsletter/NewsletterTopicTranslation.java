package ch.difty.scipamato.core.entity.newsletter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.FieldEnumType;

/**
 * The individual translation in a particular language of a newsletter topic.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsletterTopicTranslation extends NewsletterTopic {
    private static final long serialVersionUID = 1L;

    private final String langCode;

    public NewsletterTopicTranslation(final Integer id, final String langCode, final String title) {
        super(id, title);
        this.langCode = langCode;
    }

    public enum NewsletterTopicTranslationFields implements FieldEnumType {
        ID("id"),
        LANG_CODE("langCode"),
        TITLE("title");

        private String name;

        NewsletterTopicTranslationFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    @Override
    public String getDisplayValue() {
        return getLangCode() + ": " + getTitle();
    }

}
