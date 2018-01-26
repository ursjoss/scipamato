package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateUtils;
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link SearchOrder}s.
 * <p>
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
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");
        return step.set(SEARCH_ORDER.NAME, e.getName())
            .set(SEARCH_ORDER.OWNER, e.getOwner())
            .set(SEARCH_ORDER.GLOBAL, e.isGlobal())

            .set(SEARCH_ORDER.CREATED, DateUtils.tsOf(e.getCreated()))
            .set(SEARCH_ORDER.CREATED_BY, e.getCreatedBy())
            .set(SEARCH_ORDER.LAST_MODIFIED, DateUtils.tsOf(e.getLastModified()))
            .set(SEARCH_ORDER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(SEARCH_ORDER.VERSION, e.getVersion() + 1);
    }

}
