@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.logic.exporting

import ch.difty.scipamato.common.asProperty

/**
 * Parses a property value to define the [RisExporterStrategy] to be used.
 */
enum class RisExporterStrategy {

    /**
     * The [RisExporterStrategy] exporting the default RIS format
     */
    DEFAULT,

    /**
     * The [RisExportStrategy] targeting https://www.evidencepartners.com/products/distillersr-systematic-review-software/
     */
    DISTILLERSR;

    companion object {

        private val STRATEGIES = values()

        /**
         * Converts the string based [propertyValue] into the proper [RisExporterStrategy] enum value.
         * Accepts a [propertyKey] for logging purposes
         */
        fun fromProperty(propertyValue: String, propertyKey: String): RisExporterStrategy =
                propertyValue.asProperty(STRATEGIES, DEFAULT, propertyKey)
    }
}
