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
