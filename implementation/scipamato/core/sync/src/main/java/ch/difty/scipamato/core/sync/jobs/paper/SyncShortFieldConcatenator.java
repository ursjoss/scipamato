package ch.difty.scipamato.core.sync.jobs.paper;

import java.sql.ResultSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Gathers the content for the the fields methods, population and result based on a
 * resultSet of papers.
 * <p>
 * The content may either be those fields themselves or some concatenated short fields.
 */
interface SyncShortFieldConcatenator {

    /**
     * @param rs
     *     the paper recordset
     * @return the methods field content
     */
    @Nullable
    String methodsFrom(@NotNull ResultSet rs);

    /**
     * @param rs
     *     the paper recordset
     * @return the population field content
     */
    @Nullable
    String populationFrom(@NotNull ResultSet rs);

    /**
     * @param rs
     *     the paper recordset
     * @return the result field content
     */
    @Nullable
    String resultFrom(@NotNull ResultSet rs);
}
