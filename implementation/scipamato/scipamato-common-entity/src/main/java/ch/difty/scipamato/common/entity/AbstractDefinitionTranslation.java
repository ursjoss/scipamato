package ch.difty.scipamato.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.AssertAs;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractDefinitionTranslation extends ScipamatoEntity implements DefinitionTranslation {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String  langCode;
    private String  name;

    public AbstractDefinitionTranslation(final Integer id, final String langCode, final String name,
        final Integer version) {
        super();
        setId(id);
        setLangCode(AssertAs.notNull(langCode, "langCode"));
        setName(name);
        setVersion(version != null ? version : 0);
    }

    @Override
    public String getDisplayValue() {
        return getLangCode() + ": " + getName();
    }

    public enum DefinitionTranslationFields implements FieldEnumType {
        ID("id"),
        LANG_CODE("langCode"),
        NAME("name");

        private final String name;

        DefinitionTranslationFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
