package ch.difty.scipamato.publ.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewStudy extends PublicEntity {

    private static final long serialVersionUID = 1L;

    private final int     sort;
    private final long    number;
    private final Integer year;
    private final String  authors;
    private final String  headline;
    private final String  description;

    public enum NewStudyFields implements FieldEnumType {
        SORT("sort"),
        NUMBER("number"),
        YEAR("year"),
        AUTHORS("authors"),
        REFERENCE("reference"),
        HEADLINE("headline"),
        DESCRIPTION("description");

        private final String name;

        NewStudyFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    public String getReference() {
        return "(" + authors + "; " + year + ")";
    }

}
