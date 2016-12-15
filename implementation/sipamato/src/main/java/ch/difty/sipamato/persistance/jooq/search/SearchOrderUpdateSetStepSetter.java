package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link SearchOrder}s.<p/>
 *
 * <b>Note:</b> the {@link SearchCondition}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class SearchOrderUpdateSetStepSetter implements UpdateSetStepSetter<SearchOrderRecord, SearchOrder> {

    /** {@inheritDoc} */
    @Override
    public UpdateSetMoreStep<SearchOrderRecord> setFieldsFor(UpdateSetFirstStep<SearchOrderRecord> step, SearchOrder e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");
        // @formatter:off
        return step
            .set(SEARCH_ORDER.NAME, e.getName())
            .set(SEARCH_ORDER.OWNER, e.getOwner())
            .set(SEARCH_ORDER.GLOBAL, e.isGlobal());
         // @formatter:on
    }

}
