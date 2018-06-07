package ch.difty.scipamato.core.entity.projection;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewsletterAssociation extends IdScipamatoEntity<Integer> {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String  issue;
    @NotNull
    private Integer publicationStatusId;
    private String  headline;

    public enum NewsletterSlimFields implements FieldEnumType {
        ID("id"),
        ISSUE("issue"),
        STATUS("publicationStatusId"),
        HEADLINE("headline");

        private final String name;

        NewsletterSlimFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public NewsletterAssociation(Integer id, String issue, Integer publicationStatusId, String headline) {
        setId(id);
        setIssue(issue);
        setPublicationStatusId(publicationStatusId);
        setHeadline(headline);
    }

    @Override
    public String getDisplayValue() {
        return issue;
    }

}
