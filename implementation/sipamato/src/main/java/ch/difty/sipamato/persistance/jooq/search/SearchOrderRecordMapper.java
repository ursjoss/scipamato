package ch.difty.sipamato.persistance.jooq.search;

import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Mapper mapping {@link SearchOrderRecord}s into entity {@link SearchOrder}.<p/>
 *
 * <b>Note:</b> the mapper leaves the nested list of {@link SearchCondition}s empty.
 *
 * @author u.joss
 */
@Component
public class SearchOrderRecordMapper implements RecordMapper<SearchOrderRecord, SearchOrder> {

    /** {@inheritDoc} */
    @Override
    public SearchOrder map(SearchOrderRecord from) {
        AssertAs.notNull(from, "from");
        SearchOrder to = new SearchOrder();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setOwner(from.getOwner());
        to.setGlobal(from.getGlobal());

        to.setCreated(from.getCreated() != null ? from.getCreated().toLocalDateTime() : null);
        to.setCreatedBy(from.getCreatedBy());
        to.setLastModified(from.getLastModified() != null ? from.getLastModified().toLocalDateTime() : null);
        to.setLastModifiedBy(from.getLastModifiedBy());
        to.setVersion(from.getVersion() != null ? from.getVersion() : 1);

        // invertExclusions is not persisted and therefore always false after mapping
        return to;
    }

}
