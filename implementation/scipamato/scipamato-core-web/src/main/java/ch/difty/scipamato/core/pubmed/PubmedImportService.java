package ch.difty.scipamato.core.pubmed;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.config.core.ApplicationProperties;
import ch.difty.scipamato.core.persistence.DefaultServiceResult;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.persistence.ServiceResult;

@Service
public class PubmedImportService implements PubmedImporter {

    private final PubmedArticleService pubmedArticleService;
    private final PaperService         paperService;
    private final long                 minimumNumber;

    public PubmedImportService(final PubmedArticleService pubmedArticleService, final PaperService paperService,
            final ApplicationProperties applicationProperties) {
        this.pubmedArticleService = AssertAs.notNull(pubmedArticleService, "pubmedArticleService");
        this.paperService = AssertAs.notNull(paperService, "paperService");
        this.minimumNumber = AssertAs.notNull(applicationProperties, "applicationProperties")
            .getMinimumPaperNumberToBeRecycled();
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public ServiceResult persistPubmedArticlesFromXml(final String xml) {
        if (xml != null) {
            final List<PubmedArticleFacade> articles = pubmedArticleService.extractArticlesFrom(xml);
            return paperService.dumpPubmedArticlesToDb(articles, minimumNumber);
        } else {
            final ServiceResult sr = new DefaultServiceResult();
            sr.addErrorMessage("xml must not be null.");
            return sr;
        }
    }

}
