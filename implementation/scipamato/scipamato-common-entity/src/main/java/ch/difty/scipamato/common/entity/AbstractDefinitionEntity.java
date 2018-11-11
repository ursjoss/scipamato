package ch.difty.scipamato.common.entity;

import static java.util.stream.Collectors.joining;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;

import ch.difty.scipamato.common.AssertAs;

/**
 * Abstract base class comprising of the state and behavior common to all {@link DefinitionEntity} implementations.
 *
 * @param <T>
 *     the concrete type implementing the {@link DefinitionTranslation} for the given entity
 * @param <ID>
 *     the type of the ID
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractDefinitionEntity<T extends DefinitionTranslation, ID> extends ScipamatoEntity
    implements DefinitionEntity<ID, T> {

    private final ListValuedMap<String, T> translations;
    private final String                   mainLanguageCode;

    private String name;

    /**
     * @param mainLanguageCode
     *     two digit language code, cannot be null. Immutable
     * @param mainName
     *     the name of the DefinitionEntity in the main language. Mutable.
     * @param version
     *     the version of the record
     * @param translations
     *     an array of {@link DefinitionTranslation}s
     */
    public AbstractDefinitionEntity(final String mainLanguageCode, final String mainName, final Integer version,
        final T[] translations) {
        super();
        this.mainLanguageCode = AssertAs.notNull(mainLanguageCode, "mainLanguageCode");
        this.name = mainName;
        this.translations = new ArrayListValuedHashMap<>();
        for (final T tr : AssertAs.notNull(translations, "translations")) {
            final String langCode = tr.getLangCode();
            this.translations.put(langCode, tr);
        }
        setVersion(version != null ? version : 0);
    }

    @Override
    public ListValuedMap<String, T> getTranslations() {
        return translations;
    }

    public String getMainLanguageCode() {
        return mainLanguageCode;
    }

    @Override
    public String getTranslationsAsString() {
        final Collection<T> trs = translations.get(mainLanguageCode);
        if (CollectionUtils.isEmpty(trs))
            return null;
        final StringBuilder sb = new StringBuilder();
        sb.append(mainLanguageCode.toUpperCase());
        sb.append(": ");
        String mainNames = trs
            .stream()
            .map(DefinitionTranslation::getName)
            .filter(Objects::nonNull)
            .collect(joining("','"));
        sb.append(mainNames.isEmpty() ? "n.a." : ("'" + mainNames + "'"));
        for (final Map.Entry<String, Collection<T>> entry : translations
            .asMap()
            .entrySet()) {
            final String kw = entry.getKey();
            if (!kw.equals(mainLanguageCode)) {
                if (sb.length() > 0)
                    sb.append("; ");
                sb
                    .append(kw.toUpperCase())
                    .append(": ");
                final String nameString = entry
                    .getValue()
                    .stream()
                    .map(DefinitionTranslation::getName)
                    .filter(Objects::nonNull)
                    .collect(joining("','"));
                if (StringUtils.isNotBlank(nameString))
                    sb
                        .append("'")
                        .append(nameString)
                        .append("'");
                else
                    sb.append("n.a.");
            }
        }
        return sb.toString();
    }

    public void setNameInLanguage(final String langCode, final String translatedName) {
        final Collection<T> trs = translations.get(AssertAs.notNull(langCode, "langCode"));
        if (CollectionUtils.isNotEmpty(trs)) {
            final T tr = trs
                .iterator()
                .next();
            tr.setName(translatedName);
            tr.setLastModified(LocalDateTime.now());
            if (mainLanguageCode.equals(langCode))
                setMainName(translatedName);
        }
    }

    private void setMainName(String translatedName) {
        this.name = translatedName;
    }

    /**
     * Get the <b>first</b> code name in the specified language.
     *
     * @param langCode
     *     the language to get the specified name for
     * @return the code - or null if none is available.
     */
    public String getNameInLanguage(final String langCode) {
        final Collection<T> trs = translations.get(AssertAs.notNull(langCode, "langCode"));
        if (CollectionUtils.isEmpty(trs))
            return null;
        final T tr = trs
            .iterator()
            .next();
        return tr != null ? tr.getName() : null;
    }

    @Override
    public String getDisplayValue() {
        return name;
    }

}
