
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
    "geneSymbol"
})
@XmlRootElement(name = "GeneSymbolList")
public class GeneSymbolList {

    @XmlElement(name = "GeneSymbol", required = true)
    protected List<GeneSymbol> geneSymbol;

    /**
     * Gets the value of the geneSymbol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geneSymbol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGeneSymbol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GeneSymbol }
     * 
     * 
     */
    public List<GeneSymbol> getGeneSymbol() {
        if (geneSymbol == null) {
            geneSymbol = new ArrayList<GeneSymbol>();
        }
        return this.geneSymbol;
    }

}
