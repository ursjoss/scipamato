package ch.difty.sipamato.web.resources.jasper;

public class PaperReviewReportResourceReferenceTest extends ReportResourceReferenceTest<PaperReviewReportResourceReference> {

    @Override
    protected PaperReviewReportResourceReference getResourceReference() {
        return PaperReviewReportResourceReference.get();
    }

    @Override
    protected String getReportBaseName() {
        return "paper_review_A4";
    }

    @Override
    protected String getResourceReferencePath() {
        return "ch.difty.sipamato.web.resources.jasper.PaperReviewReportResourceReference";
    }

}
