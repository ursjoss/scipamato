package ch.difty.scipamato.core.web.resources.jasper;

public class PaperLiteratureReviewReportResourceReferenceTest
    extends JasperReportResourceReferenceTest<PaperLiteratureReviewReportResourceReference> {

    @Override
    protected PaperLiteratureReviewReportResourceReference getResourceReference() {
        return PaperLiteratureReviewReportResourceReference.get();
    }

    @Override
    protected String getReportBaseName() {
        return "paper_literature_review_A4";
    }

    @Override
    protected String getResourceReferencePath() {
        return "ch.difty.scipamato.core.web.resources.jasper.PaperLiteratureReviewReportResourceReference";
    }
}
