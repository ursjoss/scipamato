package ch.difty.scipamato.core.entity.newsletter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewsletterFilter extends ScipamatoFilter {
    private static final long serialVersionUID = 1L;

    private String            issueMask;
    private PublicationStatus publicationStatus;

    public enum NewsletterFilterFields implements FieldEnumType {
        ISSUE_MASK("issueMask"),
        PUBLICATION_STATUS("publicationStatus");

        private final String name;

        NewsletterFilterFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
