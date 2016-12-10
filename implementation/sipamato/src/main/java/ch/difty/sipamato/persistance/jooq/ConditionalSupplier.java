package ch.difty.sipamato.persistance.jooq;

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
 */
public class ConditionalSupplier {
    final List<Supplier<Condition>> conditionSuppliers = new ArrayList<>();

    public ConditionalSupplier add(boolean isPresent, Supplier<Condition> conditionSupplier) {
        if (isPresent)
            conditionSuppliers.add(conditionSupplier);
        return this;
    }

    public ConditionalSupplier add(Supplier<Condition> conditionSupplier) {
        conditionSuppliers.add(conditionSupplier);
        return this;
    }

    public Condition combineWithAnd() {
        return conditionSuppliers.stream().map(Supplier::get).reduce(DSL.trueCondition(), Condition::and);
    }

    public Condition combineWithOr() {
        return conditionSuppliers.stream().map(Supplier::get).reduce(DSL.falseCondition(), Condition::or);
    }
}