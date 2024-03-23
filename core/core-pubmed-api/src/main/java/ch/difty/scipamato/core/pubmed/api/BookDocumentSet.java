
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
    "bookDocument",
    "deleteDocument"
})
@XmlRootElement(name = "BookDocumentSet")
public class BookDocumentSet {

    @XmlElement(name = "BookDocument")
    protected List<BookDocument> bookDocument;
    @XmlElement(name = "DeleteDocument")
    protected DeleteDocument deleteDocument;

    /**
     * Gets the value of the bookDocument property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bookDocument property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBookDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BookDocument }
     * 
     * 
     */
    public List<BookDocument> getBookDocument() {
        if (bookDocument == null) {
            bookDocument = new ArrayList<BookDocument>();
        }
        return this.bookDocument;
    }

    /**
     * Ruft den Wert der deleteDocument-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DeleteDocument }
     *     
     */
    public DeleteDocument getDeleteDocument() {
        return deleteDocument;
    }

    /**
     * Legt den Wert der deleteDocument-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteDocument }
     *     
     */
    public void setDeleteDocument(DeleteDocument value) {
        this.deleteDocument = value;
    }

}
