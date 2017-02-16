package ch.difty.sipamato.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.xml.PubmedArticleFacade;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.pubmed.PubmedArticleSet;

@Service
public class PubmedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubmedService.class);

    private final Unmarshaller unmarshaller;

    @Autowired
    public PubmedService(final Unmarshaller unmarshaller) {
        this.unmarshaller = AssertAs.notNull(unmarshaller, "unmarshaller");
    }

    public PubmedArticleSet unmarshal(final String xmlString) throws XmlMappingException, IOException {
        final StringReader reader = new StringReader(AssertAs.notNull(xmlString, "xmlString"));
        return (PubmedArticleSet) unmarshaller.unmarshal(new StreamSource(reader));
    }

    public List<PubmedArticleFacade> getArticlesFrom(final String xmlString) {
        List<PubmedArticleFacade> articles = new ArrayList<>();
        try {
            PubmedArticleSet set = unmarshal(xmlString);
            final List<Object> aoba = set.getPubmedArticleOrPubmedBookArticle();
            articles.addAll(aoba.stream().map(PubmedArticleFacade::of).collect(Collectors.toList()));
        } catch (Exception e) {
            LOGGER.info("Unable to parse xmlString '{}': {}", xmlString, e.getMessage());
        }
        return articles;
    }
}
