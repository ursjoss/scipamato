package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link SearchOrder}s.
 * <br/><br/>
 * <b>Note:</b> the {@link SearchCondition}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class SearchOrderInsertSetStepSetter implements InsertSetStepSetter<SearchOrderRecord, SearchOrder> {

    @NotNull
    @Override
    public InsertSetMoreStep<SearchOrderRecord> setNonKeyFieldsFor(@NotNull final InsertSetStep<SearchOrderRecord> step,
        @NotNull final SearchOrder e) {
        return step
            .set(SEARCH_ORDER.NAME, e.getName())
            .set(SEARCH_ORDER.OWNER, e.getOwner())
            .set(SEARCH_ORDER.GLOBAL, e.isGlobal())

            .set(SEARCH_ORDER.CREATED_BY, e.getCreatedBy())
            .set(SEARCH_ORDER.LAST_MODIFIED_BY, e.getLastModifiedBy());
    }

    @Override
    public void considerSettingKeyOf(@NotNull final InsertSetMoreStep<SearchOrderRecord> step, @NotNull final SearchOrder entity) {
        final Long id = entity.getId();
        if (id != null) {
            //noinspection ResultOfMethodCallIgnored
            step.set(SEARCH_ORDER.ID, id);
        }
    }

    @Override
    public void resetIdToEntity(@NotNull final SearchOrder entity, @Nullable final SearchOrderRecord saved) {
        if (saved != null)
            entity.setId(saved.getId());
    }
}
