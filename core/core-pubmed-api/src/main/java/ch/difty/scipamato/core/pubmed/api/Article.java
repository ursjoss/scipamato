
package ch.difty.scipamato.core.pubmed.api;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "journal",
    "articleTitle",
    "paginationOrELocationID",
    "_abstract",
    "authorList",
    "language",
    "dataBankList",
    "grantList",
    "publicationTypeList",
    "vernacularTitle",
    "articleDate"
})
@XmlRootElement(name = "Article")
public class Article {

    @XmlAttribute(name = "PubModel", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String pubModel;
    @XmlElement(name = "Journal", required = true)
    protected Journal journal;
    @XmlElement(name = "ArticleTitle", required = true)
    protected ArticleTitle articleTitle;
    @XmlElements({
        @XmlElement(name = "Pagination", required = true, type = Pagination.class),
        @XmlElement(name = "ELocationID", required = true, type = ELocationID.class)
    })
    protected List<java.lang.Object> paginationOrELocationID;
    @XmlElement(name = "Abstract")
    protected Abstract _abstract;
    @XmlElement(name = "AuthorList")
    protected AuthorList authorList;
    @XmlElement(name = "Language", required = true)
    protected List<Language> language;
    @XmlElement(name = "DataBankList")
    protected DataBankList dataBankList;
    @XmlElement(name = "GrantList")
    protected GrantList grantList;
    @XmlElement(name = "PublicationTypeList", required = true)
    protected PublicationTypeList publicationTypeList;
    @XmlElement(name = "VernacularTitle")
    protected String vernacularTitle;
    @XmlElement(name = "ArticleDate")
    protected List<ArticleDate> articleDate;

    /**
     * Ruft den Wert der pubModel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPubModel() {
        return pubModel;
    }

    /**
     * Legt den Wert der pubModel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPubModel(String value) {
        this.pubModel = value;
    }

    /**
     * Ruft den Wert der journal-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Journal }
     *     
     */
    public Journal getJournal() {
        return journal;
    }

    /**
     * Legt den Wert der journal-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Journal }
     *     
     */
    public void setJournal(Journal value) {
        this.journal = value;
    }

    /**
     * Ruft den Wert der articleTitle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArticleTitle }
     *     
     */
    public ArticleTitle getArticleTitle() {
        return articleTitle;
    }

    /**
     * Legt den Wert der articleTitle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArticleTitle }
     *     
     */
    public void setArticleTitle(ArticleTitle value) {
        this.articleTitle = value;
    }

    /**
     * Gets the value of the paginationOrELocationID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the paginationOrELocationID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPaginationOrELocationID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Pagination }
     * {@link ELocationID }
     * 
     * 
     */
    public List<java.lang.Object> getPaginationOrELocationID() {
        if (paginationOrELocationID == null) {
            paginationOrELocationID = new ArrayList<java.lang.Object>();
        }
        return this.paginationOrELocationID;
    }

    /**
     * Ruft den Wert der abstract-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Abstract }
     *     
     */
    public Abstract getAbstract() {
        return _abstract;
    }

    /**
     * Legt den Wert der abstract-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Abstract }
     *     
     */
    public void setAbstract(Abstract value) {
        this._abstract = value;
    }

    /**
     * Ruft den Wert der authorList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AuthorList }
     *     
     */
    public AuthorList getAuthorList() {
        return authorList;
    }

    /**
     * Legt den Wert der authorList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthorList }
     *     
     */
    public void setAuthorList(AuthorList value) {
        this.authorList = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the language property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanguage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Language }
     * 
     * 
     */
    public List<Language> getLanguage() {
        if (language == null) {
            language = new ArrayList<Language>();
        }
        return this.language;
    }

    /**
     * Ruft den Wert der dataBankList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DataBankList }
     *     
     */
    public DataBankList getDataBankList() {
        return dataBankList;
    }

    /**
     * Legt den Wert der dataBankList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DataBankList }
     *     
     */
    public void setDataBankList(DataBankList value) {
        this.dataBankList = value;
    }

    /**
     * Ruft den Wert der grantList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GrantList }
     *     
     */
    public GrantList getGrantList() {
        return grantList;
    }

    /**
     * Legt den Wert der grantList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GrantList }
     *     
     */
    public void setGrantList(GrantList value) {
        this.grantList = value;
    }

    /**
     * Ruft den Wert der publicationTypeList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PublicationTypeList }
     *     
     */
    public PublicationTypeList getPublicationTypeList() {
        return publicationTypeList;
    }

    /**
     * Legt den Wert der publicationTypeList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PublicationTypeList }
     *     
     */
    public void setPublicationTypeList(PublicationTypeList value) {
        this.publicationTypeList = value;
    }

    /**
     * Ruft den Wert der vernacularTitle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVernacularTitle() {
        return vernacularTitle;
    }

    /**
     * Legt den Wert der vernacularTitle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVernacularTitle(String value) {
        this.vernacularTitle = value;
    }

    /**
     * Gets the value of the articleDate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the articleDate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArticleDate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArticleDate }
     * 
     * 
     */
    public List<ArticleDate> getArticleDate() {
        if (articleDate == null) {
            articleDate = new ArrayList<ArticleDate>();
        }
        return this.articleDate;
    }

}
