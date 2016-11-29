package ch.difty.sipamato.persistance.jooq.paper.slim;

import org.jooq.Condition;

import ch.difty.sipamato.entity.SearchTerm;

/**
 * Evaluates the {@link SearchTerm} implementation to build a concrete jOOQ Condition
 *
 * @author u.joss
 *
 * @param <T> the {@link SearchTerm} implementation to be evaluated
 */
public interface SearchTermEvaluator<T extends SearchTerm<T>> {

    /**
     * Evaluates the searchTerm <code>T</code> to build the {@link Condition}
     * @param searchTerm
     * @return a jOOQ conditions
     */
    Condition evaluate(T searchTerm);

}
