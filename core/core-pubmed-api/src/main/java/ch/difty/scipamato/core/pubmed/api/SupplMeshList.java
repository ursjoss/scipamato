
package ch.difty.scipamato.core.pubmed.api;

import java.util.ArrayList;
import java.util.List;
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
    "supplMeshName"
})
@XmlRootElement(name = "SupplMeshList")
public class SupplMeshList {

    @XmlElement(name = "SupplMeshName", required = true)
    protected List<SupplMeshName> supplMeshName;

    /**
     * Gets the value of the supplMeshName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supplMeshName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupplMeshName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SupplMeshName }
     * 
     * 
     */
    public List<SupplMeshName> getSupplMeshName() {
        if (supplMeshName == null) {
            supplMeshName = new ArrayList<SupplMeshName>();
        }
        return this.supplMeshName;
    }

}
