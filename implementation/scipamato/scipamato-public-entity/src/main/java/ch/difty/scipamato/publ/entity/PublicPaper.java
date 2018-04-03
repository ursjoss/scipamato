package ch.difty.scipamato.publ.entity;

import ch.difty.scipamato.common.entity.FieldEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PublicPaper extends PublicEntity {

    private static final long serialVersionUID = 1L;

    private Long    id;
    private Long    number;
    private Integer pmId;
    private String  authors;
    private String  authorsAbbreviated;
    private String  title;
    private String  location;
    private String  journal;
    private Integer publicationYear;

    private String goals;
    private String methods;
    private String population;
    private String result;
    private String comment;

    public enum PublicPaperFields implements FieldEnumType {
        ID("id"),
        NUMBER("number"),
        PMID("pmId"),
        AUTHORS("authors"),
        AUTHORS_ABBREVIATED("authorsAbbreviated"),
        TITLE("title"),
        LOCATION("location"),
        JOURNAL("journal"),
        PUBL_YEAR("publicationYear"),
        GOALS("goals"),
        METHODS("methods"),
        POPULATION("population"),
        RESULT("result"),
        COMMENT("comment");

        private String name;

        PublicPaperFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
