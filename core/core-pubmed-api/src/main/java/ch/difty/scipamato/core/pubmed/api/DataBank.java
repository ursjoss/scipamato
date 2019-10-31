
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
    "dataBankName",
    "accessionNumberList"
})
@XmlRootElement(name = "DataBank")
public class DataBank {

    @XmlElement(name = "DataBankName", required = true)
    protected String dataBankName;
    @XmlElement(name = "AccessionNumberList")
    protected AccessionNumberList accessionNumberList;

    /**
     * Ruft den Wert der dataBankName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataBankName() {
        return dataBankName;
    }

    /**
     * Legt den Wert der dataBankName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataBankName(String value) {
        this.dataBankName = value;
    }

    /**
     * Ruft den Wert der accessionNumberList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AccessionNumberList }
     *     
     */
    public AccessionNumberList getAccessionNumberList() {
        return accessionNumberList;
    }

    /**
     * Legt den Wert der accessionNumberList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessionNumberList }
     *     
     */
    public void setAccessionNumberList(AccessionNumberList value) {
        this.accessionNumberList = value;
    }

}
