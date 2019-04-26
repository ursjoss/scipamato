package ch.difty.scipamato.core.web.resources.jasper;

class PaperReviewReportResourceReferenceTest
    extends JasperReportResourceReferenceTest<PaperReviewReportResourceReference> {

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
        return "ch.difty.scipamato.core.web.resources.jasper.PaperReviewReportResourceReference";
    }

}
