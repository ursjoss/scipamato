package ch.difty.scipamato.web.resources.jasper;

public class PaperReviewReportResourceReferenceTest extends JasperReportResourceReferenceTest<PaperReviewReportResourceReference> {

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
        return "ch.difty.scipamato.web.resources.jasper.PaperReviewReportResourceReference";
    }

}
