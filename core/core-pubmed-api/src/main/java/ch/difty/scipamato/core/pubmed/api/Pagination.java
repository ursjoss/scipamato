
package ch.difty.scipamato.core.pubmed.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "startPageOrEndPageOrMedlinePgn"
})
@XmlRootElement(name = "Pagination")
public class Pagination {

    @XmlElements({
        @XmlElement(name = "StartPage", required = true, type = StartPage.class),
        @XmlElement(name = "EndPage", required = true, type = EndPage.class),
        @XmlElement(name = "MedlinePgn", required = true, type = MedlinePgn.class)
    })
    protected List<java.lang.Object> startPageOrEndPageOrMedlinePgn;

    /**
     * Gets the value of the startPageOrEndPageOrMedlinePgn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the startPageOrEndPageOrMedlinePgn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStartPageOrEndPageOrMedlinePgn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StartPage }
     * {@link EndPage }
     * {@link MedlinePgn }
     * 
     * 
     */
    public List<java.lang.Object> getStartPageOrEndPageOrMedlinePgn() {
        if (startPageOrEndPageOrMedlinePgn == null) {
            startPageOrEndPageOrMedlinePgn = new ArrayList<java.lang.Object>();
        }
        return this.startPageOrEndPageOrMedlinePgn;
    }

}
