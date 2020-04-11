package ch.difty.scipamato.core.persistence

import org.jooq.Condition
import org.jooq.impl.DSL
import java.util.ArrayList

/**
 * Conditional Supplier allowing to add [Condition]s and combine them with `OR` or `AND`.
 *
 * Original version by Lukas Saldanha. Optimized using jOOλ with some hints by Lukas Eder.
 * Refactored to only depend on kotlin.
 */
class ConditionalSupplier {

    private val conditionSuppliers: MutableList<() -> Condition> = ArrayList()

    /**
     * Adds the provided condition supplier.
     *
     * @param conditionSupplier the supplier to add
     * @return the resulting conditional supplier
     */
    fun add(conditionSupplier: () -> Condition): ConditionalSupplier {
        conditionSuppliers.add(conditionSupplier)
        return this
    }

    /**
     * Adds the provided condition supplier - if isPresent is true. Allowing to
     * conditionally decide whether to add or not from within the stream.
     *
     * @param isPresent filter allowing to add or not add the condition
     * @param conditionSupplier the supplier to add
     * @return the resulting conditional supplier
     */
    fun add(isPresent: Boolean, conditionSupplier: () -> Condition): ConditionalSupplier {
        if (isPresent) conditionSuppliers.add(conditionSupplier)
        return this
    }

    fun combineWithAnd(): Condition = combineWith(
        { first: Condition, second: Condition? -> first.and(second) },
        { DSL.trueCondition() }
    )

    fun combineWithOr(): Condition = combineWith(
        { first: Condition, second: Condition? -> first.or(second) },
        { DSL.falseCondition() }
    )

    private fun combineWith(
        binOp: (Condition, Condition?) -> Condition,
        defaultValueProvider: () -> Condition
    ): Condition {
        fun <T> Sequence<T>.splitAtHead(): Pair<T?, Sequence<T>> = Pair(firstOrNull(), drop(1))
        val pair = conditionSuppliers.asSequence().splitAtHead()
        val head = pair.first ?: defaultValueProvider
        val tail = pair.second
        return (listOf(head) + tail)
            .map { conditionProvider: () -> Condition -> conditionProvider() }
            .reduce { acc, cond -> binOp(acc, cond) }
    }
}
