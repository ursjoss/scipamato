package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;

import java.sql.Timestamp;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.UtilsKt;
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link SearchOrder}s.
 *
 *
 * <b>Note:</b> the {@link SearchCondition}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class SearchOrderUpdateSetStepSetter implements UpdateSetStepSetter<SearchOrderRecord, SearchOrder> {

    @Override
    public UpdateSetMoreStep<SearchOrderRecord> setFieldsFor(UpdateSetFirstStep<SearchOrderRecord> step,
        SearchOrder e) {
        AssertAs.INSTANCE.notNull(step, "step");
        AssertAs.INSTANCE.notNull(e, "entity");
        final Timestamp created = e.getCreated() == null ? null : UtilsKt.toTimestamp(e.getCreated());
        final Timestamp lastMod = e.getLastModified() == null ? null : UtilsKt.toTimestamp(e.getLastModified());
        return step
            .set(SEARCH_ORDER.NAME, e.getName())
            .set(SEARCH_ORDER.OWNER, e.getOwner())
            .set(SEARCH_ORDER.GLOBAL, e.isGlobal())

            .set(SEARCH_ORDER.CREATED, created)
            .set(SEARCH_ORDER.CREATED_BY, e.getCreatedBy())
            .set(SEARCH_ORDER.LAST_MODIFIED, lastMod)
            .set(SEARCH_ORDER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(SEARCH_ORDER.VERSION, e.getVersion() + 1);
    }

}
