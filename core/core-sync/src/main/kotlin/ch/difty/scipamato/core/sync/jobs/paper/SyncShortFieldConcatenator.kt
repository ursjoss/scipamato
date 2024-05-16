package ch.difty.scipamato.core.sync.jobs.paper

import java.sql.ResultSet

/**
 * Gathers the content for the fields `methods`, `population` and
 * `result` based on a resultSet of papers. The content may either
 * be those fields themselves or some concatenated short fields.
 */
interface SyncShortFieldConcatenator {
    fun methodsFrom(rs: ResultSet): String?
    fun populationFrom(rs: ResultSet): String?
    fun resultFrom(rs: ResultSet): String?
}
