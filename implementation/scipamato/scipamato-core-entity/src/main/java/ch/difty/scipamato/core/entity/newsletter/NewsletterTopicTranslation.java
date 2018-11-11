package ch.difty.scipamato.core.entity.newsletter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation;

/**
 * The individual translation in a particular language of a newsletter topic.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsletterTopicTranslation extends AbstractDefinitionTranslation {
    private static final long serialVersionUID = 1L;

    public NewsletterTopicTranslation(final Integer id, final String langCode, final String name,
        final Integer version) {
        super(id, langCode, name, version);
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

}
