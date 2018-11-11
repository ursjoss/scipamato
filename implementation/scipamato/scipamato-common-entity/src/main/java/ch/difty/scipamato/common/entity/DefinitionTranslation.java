package ch.difty.scipamato.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Translation of the to be translated parts of a {@link DefinitionEntity} (see class javadoc there).
 * The DefinitionEntity contains multiple of those translations, one for each language.
 */
public interface DefinitionTranslation extends Serializable {

    /**
     * @return the two digit language code the current DefinitionTranslation is for
     */
    String getLangCode();

    /**
     * @return the translated name in the language defined by getLangCode().
     */
    String getName();

    /**
     * Sets the name in the language defined by getLangCode().
     *
     * @param name
     *     the translated name
     */
    void setName(String name);

    /**
     * Sets the last modified timestamp
     *
     * @param lastModified
     *     the timestamp
     */
    void setLastModified(LocalDateTime lastModified);

    /**
     * @return display value representing the translation in the given language.
     */
    String getDisplayValue();
}
