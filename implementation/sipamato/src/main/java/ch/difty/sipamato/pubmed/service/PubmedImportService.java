package ch.difty.sipamato.pubmed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.sipamato.config.ApplicationProperties;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.sipamato.service.DefaultServiceResult;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.service.PubmedArticleService;
import ch.difty.sipamato.service.PubmedImporter;
import ch.difty.sipamato.service.ServiceResult;

@Service
public class PubmedImportService implements PubmedImporter {

    private final PubmedArticleService pubmedArticleService;
    private final PaperService paperService;
    private final long minimumNumber;

    @Autowired
    public PubmedImportService(final PubmedArticleService pubmedArticleService, final PaperService paperService, final ApplicationProperties applicationProperties) {
        this.pubmedArticleService = AssertAs.notNull(pubmedArticleService, "pubmedArticleService");
        this.paperService = AssertAs.notNull(paperService, "paperService");
        this.minimumNumber = AssertAs.notNull(applicationProperties, "applicationProperties").getMinimumPaperNumberToBeRecycled();
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
