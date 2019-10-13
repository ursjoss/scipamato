package ch.difty.scipamato.core.persistence.paper.searchorder;

import org.jetbrains.annotations.NotNull;
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
interface SearchTermEvaluator<T extends SearchTerm> {

    /**
     * Evaluates the searchTerm {@code T} to build the {@link Condition}
     *
     * @param searchTerm
     *     the search term to evaluate
     * @return a jOOQ conditions
     */
    @NotNull
    Condition evaluate(@NotNull T searchTerm);
}
