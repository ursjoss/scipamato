package ch.difty.scipamato.core.web.paper.jasper

import java.io.Serializable

interface JasperEntity : Serializable

fun na(s: String?): String = s ?: ""

/**
 * Provides the (non-null) label, but only if the associated value is neither null nor blank.
 *
 * @param label the label to display (if it is non-null)
 * @param value the value to test against if it is null or blank to not show the label
 * @return label
 */
fun na(label: String?, value: String?): String = if (value == null || value.isEmpty()) "" else label ?: ""

/**
 * Provides the label, but only if the associated value is neither null nor blank.
 *
 * @param label the label to display
 * @param value the value to test against if it is null or blank to not show the label
 * @return label
 */
fun na2(label: String, value: String?): String = if (value == null || value.isEmpty()) "" else label
