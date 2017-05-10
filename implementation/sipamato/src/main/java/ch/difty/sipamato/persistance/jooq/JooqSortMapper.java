package ch.difty.sipamato.persistance.jooq;

import java.util.Collection;

import org.jooq.Record;
import org.jooq.SortField;
import org.jooq.impl.TableImpl;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.paging.Sort;

/**
 * Implementations of this interface map sorting specifications into the jOOQ specific {@link SortField}s.
 *
 * @author u.joss
 *
 * @param <R> the type of the record, extending {@link Record}
 * @param <T> the type of the entity, extending {@link SipamatoEntity}
 * @param <TI> the type of the table implementation of record {@code R}
 */
@FunctionalInterface
public interface JooqSortMapper<R extends Record, T extends SipamatoEntity, TI extends TableImpl<R>> {

    /**
     * Maps spring data {@link Sort} specifications of table {@code TI} into
     * the jOOQ specific {@link SortField}s.
     *
     * @param sortSpecification the spring data {@link Sort}
     * @param table {@code TI}
     * @return collection of {@link SortField}s
     */
    Collection<SortField<T>> map(Sort sortSpecification, TI table);

}
