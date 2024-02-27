
package ch.difty.scipamato.core.pubmed.api;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pubmedArticleOrPubmedBookArticle",
    "deleteCitation"
})
@XmlRootElement(name = "PubmedArticleSet")
public class PubmedArticleSet {

    @XmlElements({
        @XmlElement(name = "PubmedArticle", required = true, type = PubmedArticle.class),
        @XmlElement(name = "PubmedBookArticle", required = true, type = PubmedBookArticle.class)
    })
    protected List<java.lang.Object> pubmedArticleOrPubmedBookArticle;
    @XmlElement(name = "DeleteCitation")
    protected DeleteCitation deleteCitation;

    /**
     * Gets the value of the pubmedArticleOrPubmedBookArticle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pubmedArticleOrPubmedBookArticle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPubmedArticleOrPubmedBookArticle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PubmedArticle }
     * {@link PubmedBookArticle }
     * 
     * 
     */
    public List<java.lang.Object> getPubmedArticleOrPubmedBookArticle() {
        if (pubmedArticleOrPubmedBookArticle == null) {
            pubmedArticleOrPubmedBookArticle = new ArrayList<java.lang.Object>();
        }
        return this.pubmedArticleOrPubmedBookArticle;
    }

    /**
     * Ruft den Wert der deleteCitation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DeleteCitation }
     *     
     */
    public DeleteCitation getDeleteCitation() {
        return deleteCitation;
    }

    /**
     * Legt den Wert der deleteCitation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteCitation }
     *     
     */
    public void setDeleteCitation(DeleteCitation value) {
        this.deleteCitation = value;
    }

}
