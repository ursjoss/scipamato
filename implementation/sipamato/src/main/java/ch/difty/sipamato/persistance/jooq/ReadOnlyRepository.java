package ch.difty.sipamato.persistance.jooq;

import java.io.Serializable;
import java.util.List;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;
import ch.difty.sipamato.lib.NullArgumentException;

/**
 * The generic repository interface for reading-only entity repository methods.
 *
 * @author u.joss
 *
 * @param <R> the type of the record, extending {@link Record}
 * @param <T> the type of the entity, extending {@link SipamatoEntity}
 * @param <ID> the type of the ID of the entity
 * @param <M> the type of the record mapper mapping records of type <literal>R</literal> into the entity of type <literal>T</literal>
 * @param <F> the type of the filter extending {@link SipamatorFilter}
 */
public interface ReadOnlyRepository<R extends Record, T extends SipamatoEntity, ID, M extends RecordMapper<R, T>, F extends SipamatoFilter> extends Serializable {

    /**
     * Finds all persisted entities.
     *
     * @return list of all entities <code>T</code>
     */
    List<T> findAll();

    /**
     * Finds the persistent entities <code>T</code> with the provided id.
     *
     * @param id - must not be null
     * @return the persisted entity <code>T</code> or null if it can't be found.
     * @throws NullArgumentException if the id is null.
     */
    T findById(ID id);

    /**
     * Finds all persisted entities matching the provided filter, returned in pages.
     *
     * @param filter of type <code>F</code>
     * @param pageable {@link Pageable}
     * @return list of all matching entities <code>T</code>
     */
    Page<T> findByFilter(F filter, Pageable pageable);

    /**
     * Counts all persisted entities matching the provided filter. 
     *
     * @param filter of type <code>F</code>
     * @return list of all matching entities <code>T</code>
     */
    int countByFilter(F filter);
}
