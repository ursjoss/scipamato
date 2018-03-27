package ch.difty.scipamato.core.entity.newsletter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Newsletter extends IdScipamatoEntity<Integer> {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String                issue;
    private LocalDate             issueDate;
    @NotNull
    private PublicationStatus     publicationStatus;
    private List<NewsletterTopic> topics;

    public enum NewsletterFields implements FieldEnumType {
        ID("id"),
        ISSUE("issue"),
        ISSUE_DATE("issueDate"),
        PUBLICATION_STATUS("publicationStatus");

        private String name;

        NewsletterFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public String getDisplayValue() {
        return getIssue();
    }

}
