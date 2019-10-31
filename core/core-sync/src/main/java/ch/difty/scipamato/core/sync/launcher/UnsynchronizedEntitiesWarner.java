package ch.difty.scipamato.core.sync.launcher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;

import ch.difty.scipamato.core.db.tables.Paper;
import ch.difty.scipamato.core.db.tables.PaperCode;

public class UnsynchronizedEntitiesWarner implements Warner {

    private final DSLContext jooqCore;

    public UnsynchronizedEntitiesWarner(@Qualifier("dslContext") @NotNull DSLContext jooqCore) {
        this.jooqCore = jooqCore;
    }

    @SuppressWarnings("unused")
    protected UnsynchronizedEntitiesWarner() {
        // for test purposes only
        jooqCore = null;
    }

    @NotNull
    @Override
    public Optional<String> findUnsynchronizedPapers() {
        final List<Long> numbers = retrieveRecords();
        if (numbers.isEmpty())
            return Optional.empty();
        else
            return Optional.of("Papers not synchronized due to missing codes: Number " + numbers
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")) + ".");
    }

    // protected for stubbing
    @NotNull
    List<Long> retrieveRecords() {
        //noinspection ConstantConditions
        return jooqCore
            .select(Paper.PAPER.NUMBER)
            .from(Paper.PAPER)
            .where(Paper.PAPER.ID.notIn(DSL
                .select(PaperCode.PAPER_CODE.PAPER_ID)
                .from(PaperCode.PAPER_CODE)))
            .fetchInto(Long.class);
    }
}

