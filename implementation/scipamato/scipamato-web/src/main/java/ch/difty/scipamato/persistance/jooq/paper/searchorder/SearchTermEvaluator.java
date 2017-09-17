package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import org.jooq.Condition;

import ch.difty.scipamato.entity.filter.SearchTerm;

/**
 * Evaluates the {@link SearchTerm} implementation to build a concrete jOOQ Condition
 *
 * @author u.joss
 *
 * @param <T> the {@link SearchTerm} implementation to be evaluated
 */
@FunctionalInterface
public interface SearchTermEvaluator<T extends SearchTerm> {

    /**
     * Evaluates the searchTerm {@code T} to build the {@link Condition}
     * @param searchTerm
     * @return a jOOQ conditions
     */
    Condition evaluate(T searchTerm);

}
