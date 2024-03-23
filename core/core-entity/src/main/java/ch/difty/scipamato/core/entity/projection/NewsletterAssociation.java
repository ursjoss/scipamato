package ch.difty.scipamato.core.entity.projection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewsletterAssociation extends IdScipamatoEntity<Integer> {

    private static final long serialVersionUID = 1L;

    @jakarta.validation.constraints.NotNull
    private String  issue;
    @jakarta.validation.constraints.NotNull
    private Integer publicationStatusId;
    private String  headline;

    public enum NewsletterAssociationFields implements FieldEnumType {
        ID("id"),
        ISSUE("issue"),
        STATUS("publicationStatusId"),
        HEADLINE("headline");

        private final String name;

        NewsletterAssociationFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    public NewsletterAssociation(@Nullable Integer id, @NotNull String issue, @NotNull Integer publicationStatusId,
        @Nullable String headline) {
        setId(id);
        this.issue = issue;
        this.publicationStatusId = publicationStatusId;
        this.headline = headline;
    }

    @NotNull
    @Override
    public String getDisplayValue() {
        return issue;
    }
}
