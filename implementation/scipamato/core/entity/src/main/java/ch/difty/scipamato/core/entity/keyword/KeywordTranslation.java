package ch.difty.scipamato.core.entity.keyword;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation;

/**
 * The individual translation in a particular language of a keyword.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordTranslation extends AbstractDefinitionTranslation {
    private static final long serialVersionUID = 1L;

    public KeywordTranslation(final Integer id, final String langCode, final String name, final Integer version) {
        super(id, langCode, name, version);
    }
}
