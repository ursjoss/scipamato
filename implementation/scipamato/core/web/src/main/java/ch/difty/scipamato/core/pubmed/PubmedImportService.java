package ch.difty.scipamato.core.pubmed;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.persistence.DefaultServiceResult;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.persistence.ServiceResult;

@Service
public class PubmedImportService implements PubmedImporter {

    private final PubmedArticleService pubmedArticleService;
    private final PaperService         paperService;
    private final long                 minimumNumber;

    public PubmedImportService(@NotNull final PubmedArticleService pubmedArticleService,
        @NotNull final PaperService paperService, @NotNull final ApplicationCoreProperties applicationProperties) {
        this.pubmedArticleService = pubmedArticleService;
        this.paperService = paperService;
        this.minimumNumber = applicationProperties.getMinimumPaperNumberToBeRecycled();
    }

    @NotNull
    @Transactional
    @Override
    public ServiceResult persistPubmedArticlesFromXml(@Nullable final String xml) {
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
