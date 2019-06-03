
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
    "registryNumber",
    "nameOfSubstance"
})
@XmlRootElement(name = "Chemical")
public class Chemical {

    @XmlElement(name = "RegistryNumber", required = true)
    protected String registryNumber;
    @XmlElement(name = "NameOfSubstance", required = true)
    protected NameOfSubstance nameOfSubstance;

    /**
     * Ruft den Wert der registryNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistryNumber() {
        return registryNumber;
    }

    /**
     * Legt den Wert der registryNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistryNumber(String value) {
        this.registryNumber = value;
    }

    /**
     * Ruft den Wert der nameOfSubstance-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NameOfSubstance }
     *     
     */
    public NameOfSubstance getNameOfSubstance() {
        return nameOfSubstance;
    }

    /**
     * Legt den Wert der nameOfSubstance-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NameOfSubstance }
     *     
     */
    public void setNameOfSubstance(NameOfSubstance value) {
        this.nameOfSubstance = value;
    }

}
