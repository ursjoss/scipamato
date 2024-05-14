package ch.difty.scipamato.core.entity.newsletter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation;

/**
 * The individual translation in a particular language of a newsletter topic.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsletterTopicTranslation extends AbstractDefinitionTranslation {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public NewsletterTopicTranslation(@Nullable final Integer id, @NotNull final String langCode,
        @Nullable final String name, @Nullable final Integer version) {
        super(id, langCode, name, version);
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    @Nullable
    public String getTitle() {
        return getName();
    }

    /**
     * Title as Alias for name, as it's used in the database
     */
    public void setTitle(@Nullable final String title) {
        setName(title);
    }

}
