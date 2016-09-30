package ch.difty.sipamato.persistance.jooq;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jooq.Record;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Default implementation of the {@link JooqSortMapper} interface.
 *
 * @author u.joss
 *
 * @param <R> the type of the record, extending {@link Record}
 * @param <T> the type of the entity, extending {@link SipamatoEntity}
 * @param <TI> the type of the table implementation of record <literal>R</literal>
 */
@Component
public class SortMapper<R extends Record, T extends SipamatoEntity, TI extends TableImpl<R>> implements JooqSortMapper<R, T, TI> {

    /** {@inheritDoc} */
    @Override
    public Collection<SortField<T>> map(Sort sortSpecification, TI table) {
        Collection<SortField<T>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            return querySortFields;
        }

        Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();

        while (specifiedFields.hasNext()) {
            Sort.Order specifiedField = specifiedFields.next();

            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();

            TableField<R, T> tableField = getTableField(sortFieldName, table);
            SortField<T> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    /** protected for test purposes */
    @SuppressWarnings("unchecked")
    protected TableField<R, T> getTableField(String sortFieldName, TI table) {
        AssertAs.notNull(sortFieldName, "sortFieldName");
        AssertAs.notNull(table, "table");

        TableField<R, T> sortField = null;
        try {
            Field tableField = table.getClass().getField(sortFieldName.toUpperCase());
            sortField = (TableField<R, T>) tableField.get(table);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: {}", sortFieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }

        return sortField;
    }

    private SortField<T> convertTableFieldToSortField(TableField<R, T> tableField, Sort.Direction sortDirection) {
        return sortDirection == Sort.Direction.ASC ? tableField.asc() : tableField.desc();
    }

}
