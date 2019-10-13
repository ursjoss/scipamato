package ch.difty.scipamato.core.entity.newsletter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

@Data
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("SameParameterValue")
public class NewsletterTopicFilter extends ScipamatoFilter {
    private static final long serialVersionUID = 1L;

    private String titleMask;

    public enum NewsletterTopicFilterFields implements FieldEnumType {
        TITLE_MASK("titleMask");

        private final String name;

        NewsletterTopicFilterFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }
}
