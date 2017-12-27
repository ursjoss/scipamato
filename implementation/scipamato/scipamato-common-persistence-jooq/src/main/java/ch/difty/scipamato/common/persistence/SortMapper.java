package ch.difty.scipamato.common.persistence;

import java.lang.reflect.Field;
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
 * Sort properties are de-camel-cased; java property names are therefore
 * converted to table column names, e.g. {@code publicationYear} will be
 * translated to {@code publication_year}
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

    /** {@inheritDoc} */
    @Override
    public Collection<SortField<T>> map(Sort sortSpecification, TI table) {
        Collection<SortField<T>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            return querySortFields;
        }

        Iterator<Sort.SortProperty> sortProperties = sortSpecification.iterator();

        while (sortProperties.hasNext()) {
            Sort.SortProperty sortProperty = sortProperties.next();

            String propName = sortProperty.getName();
            Sort.Direction sortDirection = sortProperty.getDirection();

            TableField<R, T> tableField = getTableField(propName, table);
            SortField<T> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    /**
     * public for test purposes (can't be tested in common due to missing generated
     * classes)
     */
    @SuppressWarnings("unchecked")
    public TableField<R, T> getTableField(String sortFieldName, TI table) {
        AssertAs.notNull(sortFieldName, "sortFieldName");
        AssertAs.notNull(table, "table");

        TableField<R, T> sortField = null;
        try {
            final String columnName = deCamelCase(sortFieldName);
            final Field tableField = table.getClass()
                .getField(columnName);
            sortField = (TableField<R, T>) tableField.get(table);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: %s", sortFieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }

        return sortField;
    }

    private String deCamelCase(String sortFieldName) {
        return TranslationUtils.deCamelCase(sortFieldName)
            .toUpperCase();
    }

    private SortField<T> convertTableFieldToSortField(TableField<R, T> tableField, Sort.Direction sortDirection) {
        return sortDirection == Sort.Direction.ASC ? tableField.asc() : tableField.desc();
    }

}
