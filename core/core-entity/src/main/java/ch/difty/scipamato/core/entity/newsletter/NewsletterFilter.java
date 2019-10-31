package ch.difty.scipamato.core.entity.newsletter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewsletterFilter extends ScipamatoFilter {
    private static final long serialVersionUID = 1L;

    private String            issueMask;
    private PublicationStatus publicationStatus;
    private NewsletterTopic   newsletterTopic;

    public enum NewsletterFilterFields implements FieldEnumType {
        ISSUE_MASK("issueMask"),
        PUBLICATION_STATUS("publicationStatus"),
        NEWSLETTER_TOPIC_ID("newsletterTopic");

        private final String name;

        NewsletterFilterFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }
}
