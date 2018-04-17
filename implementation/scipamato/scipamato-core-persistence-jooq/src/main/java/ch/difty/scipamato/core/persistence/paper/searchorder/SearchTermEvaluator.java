package ch.difty.scipamato.core.persistence.paper.searchorder;

import org.jooq.Condition;

import ch.difty.scipamato.core.entity.search.SearchTerm;

/**
 * Evaluates the {@link SearchTerm} implementation to build a concrete jOOQ
 * Condition
 *
 * @param <T>
 *     the {@link SearchTerm} implementation to be evaluated
 * @author u.joss
 */
@FunctionalInterface
public interface SearchTermEvaluator<T extends SearchTerm> {

    /**
     * Evaluates the searchTerm {@code T} to build the {@link Condition}
     *
     * @param searchTerm
     * @return a jOOQ conditions
     */
    Condition evaluate(T searchTerm);

}
