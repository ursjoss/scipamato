package ch.difty.scipamato.core.entity.newsletter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

/**
 * The NewsletterTopic class represents the newsletter topic in one
 * particular language only.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsletterTopic extends IdScipamatoEntity<Integer> {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @jakarta.validation.constraints.NotNull
    private String title;

    public NewsletterTopic(@Nullable final Integer id, @NotNull final String title) {
        setId(id);
        this.title = title;
    }

    public enum NewsletterTopicFields implements FieldEnumType {
        ID("id"),
        TITLE("title");

        private final String name;

        NewsletterTopicFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    @NotNull
    @Override
    public String getDisplayValue() {
        return getTitle();
    }
}
