package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jooq.Condition;
import org.jooq.impl.DSL;

/**
 * 
 * @author Lukas Saldanha
 * 
 * @see http://www.programania.net/diseno-de-software/functional-trick-to-compose-conditions-in-jooq/
 *
 */
class ConditionalSupplier {
    final List<Supplier<Condition>> conditionSuppliers = new ArrayList<>();

    ConditionalSupplier add(boolean isPresent, Supplier<Condition> conditionSupplier) {
        if (isPresent)
            conditionSuppliers.add(conditionSupplier);
        return this;
    }

    ConditionalSupplier add(Supplier<Condition> conditionSupplier) {
        conditionSuppliers.add(conditionSupplier);
        return this;
    }

    Condition combineWithAnd() {
        return conditionSuppliers.stream().map(Supplier::get).reduce(DSL.trueCondition(), Condition::and);
    }

    Condition combineWithOr() {
        return conditionSuppliers.stream().map(Supplier::get).reduce(DSL.falseCondition(), Condition::or);
    }
}