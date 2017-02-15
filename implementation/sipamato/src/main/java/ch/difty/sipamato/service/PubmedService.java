package ch.difty.sipamato.service;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.pubmed.PubmedArticleSet;

@Service
public class PubmedService {

    private final Unmarshaller unmarshaller;

    @Autowired
    public PubmedService(final Unmarshaller unmarshaller) {
        this.unmarshaller = AssertAs.notNull(unmarshaller, "unmarshaller");
    }

    public PubmedArticleSet unmarshal(final String xmlString) throws XmlMappingException, IOException {
        final StringReader reader = new StringReader(AssertAs.notNull(xmlString, "xmlString"));
        return (PubmedArticleSet) unmarshaller.unmarshal(new StreamSource(reader));
    }
}
