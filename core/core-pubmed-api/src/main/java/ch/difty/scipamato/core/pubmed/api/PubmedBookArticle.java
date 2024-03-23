
package ch.difty.scipamato.core.pubmed.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bookDocument",
    "pubmedBookData"
})
@XmlRootElement(name = "PubmedBookArticle")
public class PubmedBookArticle {

    @XmlElement(name = "BookDocument", required = true)
    protected BookDocument bookDocument;
    @XmlElement(name = "PubmedBookData")
    protected PubmedBookData pubmedBookData;

    /**
     * Ruft den Wert der bookDocument-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BookDocument }
     *     
     */
    public BookDocument getBookDocument() {
        return bookDocument;
    }

    /**
     * Legt den Wert der bookDocument-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BookDocument }
     *     
     */
    public void setBookDocument(BookDocument value) {
        this.bookDocument = value;
    }

    /**
     * Ruft den Wert der pubmedBookData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PubmedBookData }
     *     
     */
    public PubmedBookData getPubmedBookData() {
        return pubmedBookData;
    }

    /**
     * Legt den Wert der pubmedBookData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PubmedBookData }
     *     
     */
    public void setPubmedBookData(PubmedBookData value) {
        this.pubmedBookData = value;
    }

}
