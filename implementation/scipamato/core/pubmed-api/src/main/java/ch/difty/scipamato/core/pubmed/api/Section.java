//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 generiert 
// Siehe <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.09.30 um 04:04:35 PM CEST 
//


package ch.difty.scipamato.core.pubmed.api;

import java.util.ArrayList;
import java.util.List;
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
    "locationLabel",
    "sectionTitle",
    "section"
})
@XmlRootElement(name = "Section")
public class Section {

    @XmlElement(name = "LocationLabel")
    protected LocationLabel locationLabel;
    @XmlElement(name = "SectionTitle", required = true)
    protected SectionTitle sectionTitle;
    @XmlElement(name = "Section")
    protected List<Section> section;

    /**
     * Ruft den Wert der locationLabel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LocationLabel }
     *     
     */
    public LocationLabel getLocationLabel() {
        return locationLabel;
    }

    /**
     * Legt den Wert der locationLabel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LocationLabel }
     *     
     */
    public void setLocationLabel(LocationLabel value) {
        this.locationLabel = value;
    }

    /**
     * Ruft den Wert der sectionTitle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SectionTitle }
     *     
     */
    public SectionTitle getSectionTitle() {
        return sectionTitle;
    }

    /**
     * Legt den Wert der sectionTitle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SectionTitle }
     *     
     */
    public void setSectionTitle(SectionTitle value) {
        this.sectionTitle = value;
    }

    /**
     * Gets the value of the section property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the section property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Section }
     * 
     * 
     */
    public List<Section> getSection() {
        if (section == null) {
            section = new ArrayList<Section>();
        }
        return this.section;
    }

}
