package ch.difty.scipamato.publ.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import ch.difty.scipamato.common.entity.FieldEnumType;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewStudyPageLink extends PublicEntity {

    private static final long serialVersionUID = 1L;

    private final String  langCode;
    private final Integer sort;
    private       String  title;
    private       String  url;

    public enum NewProjectPageLinkFields implements FieldEnumType {
        LANG_CODE("langCode"),
        SORT("sort"),
        TITLE("title"),
        URL("url");

        private final String name;

        NewProjectPageLinkFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
