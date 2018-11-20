package ch.difty.scipamato.common.entity;

import java.io.Serializable;

import org.apache.commons.collections4.ListValuedMap;

/**
 * Interface for entities that have translations in multiple languages.
 * this interface is used in the scenario where we want to actually maintain
 * those entities in all defined languages. This is to distinguish from the
 * other scenario where we only want to display such data in one of the
 * languages, e.g. specified by the locale of the browser.
 * <p>
 * The name "Definition" was not very considerate and I'm planning to refactor
 * this one day. In fact those Definition* classes should rather be some kind
 * of Entity class while the normal entities are closer to projections or DTOs.
 * But that's something to consider for the future when the important functionality
 * is implemented.
 * <p>
 * The DefinitionEntity contains a set of DefinitionTranslations.
 *
 * @param <ID>
 *     the type of the id property
 * @param <T>
 *     the type of the translations
 */
public interface DefinitionEntity<ID, T> extends Serializable {

    /**
     * Id value of type {@code T}, must never be null.
     */
    ID getNullSafeId();

    /**
     * A display value for the definition entity. Conveys more about the entity
     * than the infoIdentifier.
     */
    String getDisplayValue();

    /**
     * A concatenated string representing the entity in ALL the languages in one string.
     */
    String getTranslationsAsString();

    /**
     * @return a list valued map of translations by language
     */
    ListValuedMap<String, T> getTranslations();

}
