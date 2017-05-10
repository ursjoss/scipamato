package ch.difty.sipamato.persistance.jooq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

/**
 * Conditional Supplier allowing to add {@link Condition}s and combine them with {@code OR} or {@code AND}.<p/>
 *
 * Original version by Lukas Saldanha. Optimized using jOOλ with some hints by Lukas Eder.<p/>
 * 
 * Example of usage:<p/>
 * 
 * @see https://gist.github.com/ggalmazor/93bdcf8b124304a19266#file-gistfile1-java
 *
 * @see http://www.programania.net/diseno-de-software/functional-trick-to-compose-conditions-in-jooq/
 * @see http://stackoverflow.com/questions/19803058/java-8-stream-getting-head-and-tail
 */
public class ConditionalSupplier {
    final List<Supplier<Condition>> conditionSuppliers = new ArrayList<>();

    /**
     * Adds the provided condition supplier.
     */
    public ConditionalSupplier add(Supplier<Condition> conditionSupplier) {
        conditionSuppliers.add(conditionSupplier);
        return this;
    }

    /**
     * Adds the provided condition supplier - if isPresent is true. Allowing to conditionally decide
     * whether to add or not from within the stream.
     *
     * @param isPresent filter allowing to add or not add 
     * @param conditionSupplier
     * @return
     */
    public ConditionalSupplier add(boolean isPresent, Supplier<Condition> conditionSupplier) {
        if (isPresent)
            conditionSuppliers.add(conditionSupplier);
        return this;
    }

    public Condition combineWithAnd() {
        final Tuple2<Optional<Supplier<Condition>>, Seq<Supplier<Condition>>> tuple = splitAtHead(conditionSuppliers.stream());
        final Condition head = tuple.v1.orElse(DSL::trueCondition).get();
        final Seq<Supplier<Condition>> tail = tuple.v2;
        return tail.stream().map(Supplier::get).reduce(head, Condition::and);
    }

    public Condition combineWithOr() {
        final Tuple2<Optional<Supplier<Condition>>, Seq<Supplier<Condition>>> t = splitAtHead(conditionSuppliers.stream());
        final Condition head = t.v1.orElse(DSL::falseCondition).get();
        final Seq<Supplier<Condition>> tail = t.v2;
        return tail.stream().map(Supplier::get).reduce(head, Condition::or);
    }

    private static <T> Tuple2<Optional<T>, Seq<T>> splitAtHead(Stream<T> stream) {
        Iterator<T> it = stream.iterator();
        return Tuple.tuple(it.hasNext() ? Optional.of(it.next()) : Optional.empty(), Seq.seq(it));
    }
}