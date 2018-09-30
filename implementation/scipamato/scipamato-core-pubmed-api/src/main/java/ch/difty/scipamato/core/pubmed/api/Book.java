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
    "publisher",
    "bookTitle",
    "pubDate",
    "beginningDate",
    "endingDate",
    "authorList",
    "investigatorList",
    "volume",
    "volumeTitle",
    "edition",
    "collectionTitle",
    "isbn",
    "eLocationID",
    "medium",
    "reportNumber"
})
@XmlRootElement(name = "Book")
public class Book {

    @XmlElement(name = "Publisher", required = true)
    protected Publisher publisher;
    @XmlElement(name = "BookTitle", required = true)
    protected BookTitle bookTitle;
    @XmlElement(name = "PubDate", required = true)
    protected PubDate pubDate;
    @XmlElement(name = "BeginningDate")
    protected BeginningDate beginningDate;
    @XmlElement(name = "EndingDate")
    protected EndingDate endingDate;
    @XmlElement(name = "AuthorList")
    protected List<AuthorList> authorList;
    @XmlElement(name = "InvestigatorList")
    protected InvestigatorList investigatorList;
    @XmlElement(name = "Volume")
    protected String volume;
    @XmlElement(name = "VolumeTitle")
    protected String volumeTitle;
    @XmlElement(name = "Edition")
    protected String edition;
    @XmlElement(name = "CollectionTitle")
    protected CollectionTitle collectionTitle;
    @XmlElement(name = "Isbn")
    protected List<Isbn> isbn;
    @XmlElement(name = "ELocationID")
    protected List<ELocationID> eLocationID;
    @XmlElement(name = "Medium")
    protected String medium;
    @XmlElement(name = "ReportNumber")
    protected String reportNumber;

    /**
     * Ruft den Wert der publisher-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Publisher }
     *     
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Legt den Wert der publisher-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Publisher }
     *     
     */
    public void setPublisher(Publisher value) {
        this.publisher = value;
    }

    /**
     * Ruft den Wert der bookTitle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BookTitle }
     *     
     */
    public BookTitle getBookTitle() {
        return bookTitle;
    }

    /**
     * Legt den Wert der bookTitle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BookTitle }
     *     
     */
    public void setBookTitle(BookTitle value) {
        this.bookTitle = value;
    }

    /**
     * Ruft den Wert der pubDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PubDate }
     *     
     */
    public PubDate getPubDate() {
        return pubDate;
    }

    /**
     * Legt den Wert der pubDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PubDate }
     *     
     */
    public void setPubDate(PubDate value) {
        this.pubDate = value;
    }

    /**
     * Ruft den Wert der beginningDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeginningDate }
     *     
     */
    public BeginningDate getBeginningDate() {
        return beginningDate;
    }

    /**
     * Legt den Wert der beginningDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeginningDate }
     *     
     */
    public void setBeginningDate(BeginningDate value) {
        this.beginningDate = value;
    }

    /**
     * Ruft den Wert der endingDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EndingDate }
     *     
     */
    public EndingDate getEndingDate() {
        return endingDate;
    }

    /**
     * Legt den Wert der endingDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EndingDate }
     *     
     */
    public void setEndingDate(EndingDate value) {
        this.endingDate = value;
    }

    /**
     * Gets the value of the authorList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authorList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthorList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuthorList }
     * 
     * 
     */
    public List<AuthorList> getAuthorList() {
        if (authorList == null) {
            authorList = new ArrayList<AuthorList>();
        }
        return this.authorList;
    }

    /**
     * Ruft den Wert der investigatorList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link InvestigatorList }
     *     
     */
    public InvestigatorList getInvestigatorList() {
        return investigatorList;
    }

    /**
     * Legt den Wert der investigatorList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link InvestigatorList }
     *     
     */
    public void setInvestigatorList(InvestigatorList value) {
        this.investigatorList = value;
    }

    /**
     * Ruft den Wert der volume-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolume() {
        return volume;
    }

    /**
     * Legt den Wert der volume-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolume(String value) {
        this.volume = value;
    }

    /**
     * Ruft den Wert der volumeTitle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolumeTitle() {
        return volumeTitle;
    }

    /**
     * Legt den Wert der volumeTitle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolumeTitle(String value) {
        this.volumeTitle = value;
    }

    /**
     * Ruft den Wert der edition-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEdition() {
        return edition;
    }

    /**
     * Legt den Wert der edition-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEdition(String value) {
        this.edition = value;
    }

    /**
     * Ruft den Wert der collectionTitle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CollectionTitle }
     *     
     */
    public CollectionTitle getCollectionTitle() {
        return collectionTitle;
    }

    /**
     * Legt den Wert der collectionTitle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CollectionTitle }
     *     
     */
    public void setCollectionTitle(CollectionTitle value) {
        this.collectionTitle = value;
    }

    /**
     * Gets the value of the isbn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the isbn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIsbn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Isbn }
     * 
     * 
     */
    public List<Isbn> getIsbn() {
        if (isbn == null) {
            isbn = new ArrayList<Isbn>();
        }
        return this.isbn;
    }

    /**
     * Gets the value of the eLocationID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eLocationID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getELocationID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ELocationID }
     * 
     * 
     */
    public List<ELocationID> getELocationID() {
        if (eLocationID == null) {
            eLocationID = new ArrayList<ELocationID>();
        }
        return this.eLocationID;
    }

    /**
     * Ruft den Wert der medium-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMedium() {
        return medium;
    }

    /**
     * Legt den Wert der medium-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMedium(String value) {
        this.medium = value;
    }

    /**
     * Ruft den Wert der reportNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportNumber() {
        return reportNumber;
    }

    /**
     * Legt den Wert der reportNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportNumber(String value) {
        this.reportNumber = value;
    }

}
