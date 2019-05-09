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
    "lastName",
    "foreName",
    "initials",
    "suffix"
})
@XmlRootElement(name = "PersonalNameSubject")
public class PersonalNameSubject {

    @XmlElement(name = "LastName", required = true)
    protected LastName lastName;
    @XmlElement(name = "ForeName")
    protected ForeName foreName;
    @XmlElement(name = "Initials")
    protected Initials initials;
    @XmlElement(name = "Suffix")
    protected Suffix suffix;

    /**
     * Ruft den Wert der lastName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LastName }
     *     
     */
    public LastName getLastName() {
        return lastName;
    }

    /**
     * Legt den Wert der lastName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LastName }
     *     
     */
    public void setLastName(LastName value) {
        this.lastName = value;
    }

    /**
     * Ruft den Wert der foreName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ForeName }
     *     
     */
    public ForeName getForeName() {
        return foreName;
    }

    /**
     * Legt den Wert der foreName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ForeName }
     *     
     */
    public void setForeName(ForeName value) {
        this.foreName = value;
    }

    /**
     * Ruft den Wert der initials-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Initials }
     *     
     */
    public Initials getInitials() {
        return initials;
    }

    /**
     * Legt den Wert der initials-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Initials }
     *     
     */
    public void setInitials(Initials value) {
        this.initials = value;
    }

    /**
     * Ruft den Wert der suffix-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Suffix }
     *     
     */
    public Suffix getSuffix() {
        return suffix;
    }

    /**
     * Legt den Wert der suffix-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Suffix }
     *     
     */
    public void setSuffix(Suffix value) {
        this.suffix = value;
    }

}
