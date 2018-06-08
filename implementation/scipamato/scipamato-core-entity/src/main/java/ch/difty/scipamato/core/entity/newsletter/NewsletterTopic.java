package ch.difty.scipamato.core.entity.newsletter;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewsletterTopic extends IdScipamatoEntity<Integer> {
    private static final long serialVersionUID = 1L;

    public NewsletterTopic(final Integer id, final String title) {
        setId(id);
        setTitle(title);
    }

    public enum NewsletterTopicFields implements FieldEnumType {
        ID("id"),
        TITLE("title");

        private String name;

        NewsletterTopicFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    @NotNull
    private String title;

    @Override
    public String getDisplayValue() {
        return getTitle();
    }

}
