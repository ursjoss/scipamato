package ch.difty.scipamato.common.persistence;

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.entity.ScipamatoEntity;
import ch.difty.scipamato.common.persistence.paging.Sort;

/**
 * Default implementation of the {@link JooqSortMapper} interface.
 * <p>
 * Sort properties are de-camel- and upper-cased; java property names are
 * therefore converted to table column names, e.g. {@code publicationYear} will
 * be translated to {@code PUBLICATION_YEAR}
 *
 * @param <R>
 *     the type of the record, extending {@link Record}
 * @param <T>
 *     the type of the entity, extending {@link ScipamatoEntity}
 * @param <TI>
 *     the type of the table implementation of record {@code R}
 * @author u.joss
 */
@Component
public class SortMapper<R extends Record, T extends ScipamatoEntity, TI extends TableImpl<R>>
    implements JooqSortMapper<R, T, TI> {

    @NotNull
    @Override
    public Collection<SortField<T>> map(@Nullable final Sort sortSpecification, @NotNull final TI table) {
        final Collection<SortField<T>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            return querySortFields;
        }

        for (final Sort.SortProperty sortProperty : sortSpecification) {
            final String propName = sortProperty.getName();
            final Sort.Direction sortDirection = sortProperty.getDirection();

            final TableField<R, T> tableField = getTableField(propName, table);
            final SortField<T> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    private TableField<R, T> getTableField(final @NotNull String fieldName, final @NotNull TI table) {
        try {
            return getTableFieldFor(table, deCamelCase(fieldName));
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            final String errorMessage = String.format("Could not find table field: %s", fieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }
    }

    /**
     * reflection based field extraction so we can stub it out in tests
     */
    @SuppressWarnings("unchecked")
    TableField<R, T> getTableFieldFor(final TI table, final String columnName)
        throws NoSuchFieldException, IllegalAccessException {
        return (TableField<R, T>) table
            .getClass()
            .getField(columnName)
            .get(table);
    }

    @SuppressWarnings("ConstantConditions")
    private String deCamelCase(final @NotNull String sortFieldName) {
        return TranslationUtils.INSTANCE
            .deCamelCase(sortFieldName)
            .toUpperCase();
    }

    private SortField<T> convertTableFieldToSortField(final TableField<R, T> tableField,
        final Sort.Direction sortDirection) {
        return sortDirection == Sort.Direction.ASC ? tableField.asc() : tableField.desc();
    }

}
