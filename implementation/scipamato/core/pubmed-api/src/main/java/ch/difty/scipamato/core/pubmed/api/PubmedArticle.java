
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
