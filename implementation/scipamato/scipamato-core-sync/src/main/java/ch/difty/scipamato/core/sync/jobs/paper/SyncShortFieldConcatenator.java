package ch.difty.scipamato.core.sync.jobs.paper;

import java.sql.ResultSet;

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
    String methodsFrom(ResultSet rs);

    /**
     * @param rs
     *     the paper recordset
     * @return the population field content
     */
    String populationFrom(ResultSet rs);

    /**
     * @param rs
     *     the paper recordset
     * @return the result field content
     */
    String resultFrom(ResultSet rs);
}
