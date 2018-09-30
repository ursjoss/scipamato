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
    "medlineCitation",
    "pubmedData"
})
@XmlRootElement(name = "PubmedArticle")
public class PubmedArticle {

    @XmlElement(name = "MedlineCitation", required = true)
    protected MedlineCitation medlineCitation;
    @XmlElement(name = "PubmedData")
    protected PubmedData pubmedData;

    /**
     * Ruft den Wert der medlineCitation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MedlineCitation }
     *     
     */
    public MedlineCitation getMedlineCitation() {
        return medlineCitation;
    }

    /**
     * Legt den Wert der medlineCitation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MedlineCitation }
     *     
     */
    public void setMedlineCitation(MedlineCitation value) {
        this.medlineCitation = value;
    }

    /**
     * Ruft den Wert der pubmedData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PubmedData }
     *     
     */
    public PubmedData getPubmedData() {
        return pubmedData;
    }

    /**
     * Legt den Wert der pubmedData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PubmedData }
     *     
     */
    public void setPubmedData(PubmedData value) {
        this.pubmedData = value;
    }

}
