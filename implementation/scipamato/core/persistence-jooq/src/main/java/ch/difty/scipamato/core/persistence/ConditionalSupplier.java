package ch.difty.scipamato.core.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

/**
 * Conditional Supplier allowing to add {@link Condition}s and combine them with
 * {@code OR} or {@code AND}.
 * <p>
 * Original version by Lukas Saldanha. Optimized using jOOλ with some hints by
 * Lukas Eder.
 * <p>
 * Example of usage:
 * <p>
 *
 * @see <a href=
 *     "https://gist.github.com/ggalmazor/93bdcf8b124304a19266#file-gistfile1-java">https://gist.github.com/ggalmazor/93bdcf8b124304a19266#file-gistfile1-java</a>
 * @see <a href=
 *     "http://www.programania.net/diseno-de-software/functional-trick-to-compose-conditions-in-jooq/">http://www.programania.net/diseno-de-software/functional-trick-to-compose-conditions-in-jooq/</a>
 * @see <a href=
 *     "http://stackoverflow.com/questions/19803058/java-8-stream-getting-head-and-tail">http://stackoverflow.com/questions/19803058/java-8-stream-getting-head-and-tail</a>
 */
public class ConditionalSupplier {

    private final List<Supplier<Condition>> conditionSuppliers = new ArrayList<>();

    /**
     * Adds the provided condition supplier.
     *
     * @param conditionSupplier
     *     the supplier to add
     * @return the resulting conditional supplier
     */
    public ConditionalSupplier add(final Supplier<Condition> conditionSupplier) {
        conditionSuppliers.add(conditionSupplier);
        return this;
    }

    /**
     * Adds the provided condition supplier - if isPresent is true. Allowing to
     * conditionally decide whether to add or not from within the stream.
     *
     * @param isPresent
     *     filter allowing to add or not add
     * @param conditionSupplier
     *     the supplier to add
     * @return the resulting conditional supplier
     */
    public ConditionalSupplier add(final boolean isPresent, final Supplier<Condition> conditionSupplier) {
        if (isPresent)
            conditionSuppliers.add(conditionSupplier);
        return this;
    }

    public Condition combineWithAnd() {
        return combineWith(Condition::and, DSL::trueCondition);
    }

    private Condition combineWith(final BinaryOperator<Condition> binOp, final Supplier<Condition> defaultValue) {
        final Tuple2<Optional<Supplier<Condition>>, Seq<Supplier<Condition>>> tuple = splitAtHead(
            conditionSuppliers.stream());
        final Condition head = tuple.v1
            .orElse(defaultValue)
            .get();
        final Seq<Supplier<Condition>> tail = tuple.v2;
        return tail
            .stream()
            .map(Supplier::get)
            .reduce(head, binOp);
    }

    public Condition combineWithOr() {
        return combineWith(Condition::or, DSL::falseCondition);
    }

    private static <T> Tuple2<Optional<T>, Seq<T>> splitAtHead(final Stream<T> stream) {
        final Iterator<T> it = stream.iterator();
        return Tuple.tuple(it.hasNext() ? Optional.of(it.next()) : Optional.empty(), Seq.seq(it));
    }
}