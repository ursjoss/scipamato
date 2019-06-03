
package ch.difty.scipamato.core.pubmed.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "grant"
})
@XmlRootElement(name = "GrantList")
public class GrantList {

    @XmlAttribute(name = "CompleteYN")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String completeYN;
    @XmlElement(name = "Grant", required = true)
    protected List<Grant> grant;

    /**
     * Ruft den Wert der completeYN-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompleteYN() {
        if (completeYN == null) {
            return "Y";
        } else {
            return completeYN;
        }
    }

    /**
     * Legt den Wert der completeYN-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompleteYN(String value) {
        this.completeYN = value;
    }

    /**
     * Gets the value of the grant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the grant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGrant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Grant }
     * 
     * 
     */
    public List<Grant> getGrant() {
        if (grant == null) {
            grant = new ArrayList<Grant>();
        }
        return this.grant;
    }

}
