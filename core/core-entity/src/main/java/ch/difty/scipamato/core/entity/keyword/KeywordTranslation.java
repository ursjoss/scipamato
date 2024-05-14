package ch.difty.scipamato.core.entity.keyword;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation;

/**
 * The individual translation in a particular language of a keyword.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordTranslation extends AbstractDefinitionTranslation {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public KeywordTranslation(@Nullable final Integer id, @NotNull final String langCode, @Nullable final String name,
        @Nullable final Integer version) {
        super(id, langCode, name, version);
    }
}
