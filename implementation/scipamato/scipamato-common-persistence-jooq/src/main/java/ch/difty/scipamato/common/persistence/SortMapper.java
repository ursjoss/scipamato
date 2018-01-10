package ch.difty.scipamato.common.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jooq.Record;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.entity.ScipamatoEntity;
import ch.difty.scipamato.common.persistence.paging.Sort;

/**
 * Default implementation of the {@link JooqSortMapper} interface.
 *
 * Sort properties are de-camel- and upper-cased; java property names are
 * therefore converted to table column names, e.g. {@code publicationYear} will
 * be translated to {@code PUBLICATION_YEAR}
 *
 * @author u.joss
 *
 * @param <R>
 *            the type of the record, extending {@link Record}
 * @param <T>
 *            the type of the entity, extending {@link ScipamatoEntity}
 * @param <TI>
 *            the type of the table implementation of record {@code R}
 */
@Component
public class SortMapper<R extends Record, T extends ScipamatoEntity, TI extends TableImpl<R>>
        implements JooqSortMapper<R, T, TI> {

    @Override
    public Collection<SortField<T>> map(final Sort sortSpecification, final TI table) {
        final Collection<SortField<T>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            return querySortFields;
        }

        final Iterator<Sort.SortProperty> sortProperties = sortSpecification.iterator();

        while (sortProperties.hasNext()) {
            final Sort.SortProperty sortProperty = sortProperties.next();

            final String propName = sortProperty.getName();
            final Sort.Direction sortDirection = sortProperty.getDirection();

            final TableField<R, T> tableField = getTableField(propName, table);
            final SortField<T> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    private TableField<R, T> getTableField(final String fieldName, final TI table) {
        AssertAs.notNull(table, "table");

        TableField<R, T> tableField = null;
        try {
            final String columnName = deCamelCase(fieldName);
            tableField = getTableFieldFor(table, columnName);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            final String errorMessage = String.format("Could not find table field: %s", fieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }

        return tableField;
    }

    /**
     * reflection based field extraction so we can stub it out in tests
     */
    @SuppressWarnings("unchecked")
    protected TableField<R, T> getTableFieldFor(final TI table, final String columnName)
            throws NoSuchFieldException, IllegalAccessException {
        return (TableField<R, T>) table.getClass()
            .getField(columnName)
            .get(table);
    }

    private String deCamelCase(final String sortFieldName) {
        return TranslationUtils.deCamelCase(sortFieldName)
            .toUpperCase();
    }

    private SortField<T> convertTableFieldToSortField(final TableField<R, T> tableField,
            final Sort.Direction sortDirection) {
        return sortDirection == Sort.Direction.ASC ? tableField.asc() : tableField.desc();
    }

}
