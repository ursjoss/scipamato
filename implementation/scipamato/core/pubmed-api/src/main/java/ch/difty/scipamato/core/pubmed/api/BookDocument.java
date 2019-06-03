
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
    "pmid",
    "articleIdList",
    "book",
    "locationLabel",
    "articleTitle",
    "vernacularTitle",
    "pagination",
    "language",
    "authorList",
    "investigatorList",
    "publicationType",
    "_abstract",
    "sections",
    "keywordList",
    "contributionDate",
    "dateRevised",
    "citationString",
    "grantList",
    "itemList"
})
@XmlRootElement(name = "BookDocument")
public class BookDocument {

    @XmlElement(name = "PMID", required = true)
    protected PMID pmid;
    @XmlElement(name = "ArticleIdList", required = true)
    protected ArticleIdList articleIdList;
    @XmlElement(name = "Book", required = true)
    protected Book book;
    @XmlElement(name = "LocationLabel")
    protected List<LocationLabel> locationLabel;
    @XmlElement(name = "ArticleTitle")
    protected ArticleTitle articleTitle;
    @XmlElement(name = "VernacularTitle")
    protected String vernacularTitle;
    @XmlElement(name = "Pagination")
    protected Pagination pagination;
    @XmlElement(name = "Language")
    protected List<Language> language;
    @XmlElement(name = "AuthorList")
    protected List<AuthorList> authorList;
    @XmlElement(name = "InvestigatorList")
    protected InvestigatorList investigatorList;
    @XmlElement(name = "PublicationType")
    protected List<PublicationType> publicationType;
    @XmlElement(name = "Abstract")
    protected Abstract _abstract;
    @XmlElement(name = "Sections")
    protected Sections sections;
    @XmlElement(name = "KeywordList")
    protected List<KeywordList> keywordList;
    @XmlElement(name = "ContributionDate")
    protected ContributionDate contributionDate;
    @XmlElement(name = "DateRevised")
    protected DateRevised dateRevised;
    @XmlElement(name = "CitationString")
    protected String citationString;
    @XmlElement(name = "GrantList")
    protected GrantList grantList;
    @XmlElement(name = "ItemList")
    protected List<ItemList> itemList;

    /**
     * Ruft den Wert der pmid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PMID }
     *     
     */
    public PMID getPMID() {
        return pmid;
    }

    /**
     * Legt den Wert der pmid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PMID }
     *     
     */
    public void setPMID(PMID value) {
        this.pmid = value;
    }

    /**
     * Ruft den Wert der articleIdList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArticleIdList }
     *     
     */
    public ArticleIdList getArticleIdList() {
        return articleIdList;
    }

    /**
     * Legt den Wert der articleIdList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArticleIdList }
     *     
     */
    public void setArticleIdList(ArticleIdList value) {
        this.articleIdList = value;
    }

    /**
     * Ruft den Wert der book-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Book }
     *     
     */
    public Book getBook() {
        return book;
    }

    /**
     * Legt den Wert der book-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Book }
     *     
     */
    public void setBook(Book value) {
        this.book = value;
    }

    /**
     * Gets the value of the locationLabel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locationLabel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocationLabel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocationLabel }
     * 
     * 
     */
    public List<LocationLabel> getLocationLabel() {
        if (locationLabel == null) {
            locationLabel = new ArrayList<LocationLabel>();
        }
        return this.locationLabel;
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
     * Ruft den Wert der pagination-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Pagination }
     *     
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Legt den Wert der pagination-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Pagination }
     *     
     */
    public void setPagination(Pagination value) {
        this.pagination = value;
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
     * Gets the value of the publicationType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the publicationType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPublicationType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PublicationType }
     * 
     * 
     */
    public List<PublicationType> getPublicationType() {
        if (publicationType == null) {
            publicationType = new ArrayList<PublicationType>();
        }
        return this.publicationType;
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
     * Ruft den Wert der sections-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Sections }
     *     
     */
    public Sections getSections() {
        return sections;
    }

    /**
     * Legt den Wert der sections-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Sections }
     *     
     */
    public void setSections(Sections value) {
        this.sections = value;
    }

    /**
     * Gets the value of the keywordList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keywordList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeywordList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeywordList }
     * 
     * 
     */
    public List<KeywordList> getKeywordList() {
        if (keywordList == null) {
            keywordList = new ArrayList<KeywordList>();
        }
        return this.keywordList;
    }

    /**
     * Ruft den Wert der contributionDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ContributionDate }
     *     
     */
    public ContributionDate getContributionDate() {
        return contributionDate;
    }

    /**
     * Legt den Wert der contributionDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ContributionDate }
     *     
     */
    public void setContributionDate(ContributionDate value) {
        this.contributionDate = value;
    }

    /**
     * Ruft den Wert der dateRevised-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateRevised }
     *     
     */
    public DateRevised getDateRevised() {
        return dateRevised;
    }

    /**
     * Legt den Wert der dateRevised-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateRevised }
     *     
     */
    public void setDateRevised(DateRevised value) {
        this.dateRevised = value;
    }

    /**
     * Ruft den Wert der citationString-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitationString() {
        return citationString;
    }

    /**
     * Legt den Wert der citationString-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitationString(String value) {
        this.citationString = value;
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
     * Gets the value of the itemList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItemList }
     * 
     * 
     */
    public List<ItemList> getItemList() {
        if (itemList == null) {
            itemList = new ArrayList<ItemList>();
        }
        return this.itemList;
    }

}
