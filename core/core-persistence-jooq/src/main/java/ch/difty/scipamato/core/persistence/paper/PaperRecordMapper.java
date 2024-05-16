package ch.difty.scipamato.core.persistence.paper;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.persistence.AuditFields;
import ch.difty.scipamato.core.persistence.EntityRecordMapper;

/**
 * Mapper mapping {@link PaperRecord}s into entity {@link Paper}.
 * <br/><br/>
 * <b>Note:</b> the mapper leaves the {@link Code}s empty.
 *
 * @author u.joss
 */
@Component
public class PaperRecordMapper extends EntityRecordMapper<PaperRecord, Paper> {

    @NotNull
    @Override
    protected Paper makeEntity() {
        return new Paper();
    }

    @NotNull
    @Override
    protected AuditFields getAuditFieldsOf(@NotNull final PaperRecord r) {
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(), r.getVersion());
    }

    @Override
    protected void mapFields(@NotNull final PaperRecord from, @NotNull final Paper to) {
        to.setId(from.getId());
        to.setNumber(from.getNumber());
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
        to.setResultMeasuredOutcome(from.getResultMeasuredOutcome());
        to.setConclusion(from.getConclusion());

        to.setOriginalAbstract(from.getOriginalAbstract());

        to.setMainCodeOfCodeclass1(from.getMainCodeOfCodeclass1());
    }
}
