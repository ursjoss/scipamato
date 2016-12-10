package ch.difty.sipamato.persistance.jooq.paper;

import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Mapper mapping {@link PaperRecord}s into entity {@link Paper}.<p/>
 *
 * <b>Note:</b> the mapper leaves the {@link Code}s empty.
 *
 * @author u.joss
 */
@Component
public class PaperRecordMapper implements RecordMapper<PaperRecord, Paper> {

    /** {@inheritDoc} */
    @Override
    public Paper map(PaperRecord from) {
        AssertAs.notNull(from, "from");
        Paper to = new Paper();
        to.setId(from.getId());
        to.setPmId(from.getPmId());
        to.setDoi(from.getDoi());
        to.setAuthors(from.getAuthors());
        to.setFirstAuthor(from.getFirstAuthor());
        to.setFirstAuthorOverridden(from.getFirstAuthorOverridden());
        to.setTitle(from.getTitle());
        to.setLocation(from.getLocation());
        to.setPublicationYear(from.getPublicationYear());

        to.setGoals(from.getGoals());
        to.setPopulation(from.getPopulation());
        to.setMethods(from.getMethods());

        to.setPopulationPlace(from.getPopulationPlace());
        to.setPopulationParticipants(from.getPopulationParticipants());
        to.setPopulationDuration(from.getPopulationDuration());
        to.setExposurePollutant(from.getExposurePollutant());
        to.setExposureAssessment(from.getExposureAssessment());
        to.setMethodStudyDesign(from.getMethodStudyDesign());
        to.setMethodOutcome(from.getMethodOutcome());
        to.setMethodStatistics(from.getMethodStatistics());
        to.setMethodConfounders(from.getMethodConfounders());

        to.setResult(from.getResult());
        to.setComment(from.getComment());
        to.setIntern(from.getIntern());

        to.setResultExposureRange(from.getResultExposureRange());
        to.setResultEffectEstimate(from.getResultEffectEstimate());

        to.setMainCodeOfCodeclass1(from.getMainCodeOfCodeclass1());

        return to;
    }

}
