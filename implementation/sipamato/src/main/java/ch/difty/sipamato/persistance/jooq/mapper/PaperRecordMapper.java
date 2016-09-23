package ch.difty.sipamato.persistance.jooq.mapper;

import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.Asserts;

@Component
public class PaperRecordMapper implements RecordMapper<PaperRecord, Paper> {

    /** {@inheritDoc} */
    @Override
    public Paper map(PaperRecord from) {
        Asserts.notNull(from);
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
        to.setExposure(from.getExposure());
        to.setMethods(from.getMethods());

        to.setPopulationPlace(from.getPopulationPlace());
        to.setPopulationParticipants(from.getPopulationParticipants());
        to.setPopulationDuration(from.getPopulationDuration());
        to.setExposurePollutant(from.getExposurePollutant());
        to.setExposureAssessment(from.getExposureAssessment());
        to.setMethodOutcome(from.getMethodOutcome());
        to.setMethodStatistics(from.getMethodStatistics());
        to.setMethodConfounders(from.getMethodConfounders());

        to.setResult(from.getResult());
        to.setComment(from.getComment());
        to.setIntern(from.getIntern());

        to.setResultExposureRange(from.getResultExposureRange());
        to.setResultEffectEstimate(from.getResultEffectEstimate());

        return to;
    }

}
