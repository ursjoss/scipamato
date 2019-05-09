//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 generiert 
// Siehe <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.09.30 um 04:04:35 PM CEST 
//


package ch.difty.scipamato.core.pubmed.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "issn",
    "journalIssue",
    "title",
    "isoAbbreviation"
})
@XmlRootElement(name = "Journal")
public class Journal {

    @XmlElement(name = "ISSN")
    protected ISSN issn;
    @XmlElement(name = "JournalIssue", required = true)
    protected JournalIssue journalIssue;
    @XmlElement(name = "Title")
    protected String title;
    @XmlElement(name = "ISOAbbreviation")
    protected String isoAbbreviation;

    /**
     * Ruft den Wert der issn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ISSN }
     *     
     */
    public ISSN getISSN() {
        return issn;
    }

    /**
     * Legt den Wert der issn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ISSN }
     *     
     */
    public void setISSN(ISSN value) {
        this.issn = value;
    }

    /**
     * Ruft den Wert der journalIssue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JournalIssue }
     *     
     */
    public JournalIssue getJournalIssue() {
        return journalIssue;
    }

    /**
     * Legt den Wert der journalIssue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JournalIssue }
     *     
     */
    public void setJournalIssue(JournalIssue value) {
        this.journalIssue = value;
    }

    /**
     * Ruft den Wert der title-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Legt den Wert der title-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Ruft den Wert der isoAbbreviation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISOAbbreviation() {
        return isoAbbreviation;
    }

    /**
     * Legt den Wert der isoAbbreviation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISOAbbreviation(String value) {
        this.isoAbbreviation = value;
    }

}
