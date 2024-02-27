
package ch.difty.scipamato.core.pubmed.api;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.NormalizedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pmid",
    "dateCompleted",
    "dateRevised",
    "article",
    "medlineJournalInfo",
    "chemicalList",
    "supplMeshList",
    "citationSubset",
    "commentsCorrectionsList",
    "geneSymbolList",
    "meshHeadingList",
    "numberOfReferences",
    "personalNameSubjectList",
    "otherID",
    "otherAbstract",
    "keywordList",
    "coiStatement",
    "spaceFlightMission",
    "investigatorList",
    "generalNote"
})
@XmlRootElement(name = "MedlineCitation")
public class MedlineCitation {

    @XmlAttribute(name = "Owner")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String owner;
    @XmlAttribute(name = "Status", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String status;
    @XmlAttribute(name = "VersionID")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String versionID;
    @XmlAttribute(name = "VersionDate")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String versionDate;
    @XmlAttribute(name = "IndexingMethod")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String indexingMethod;
    @XmlElement(name = "PMID", required = true)
    protected PMID pmid;
    @XmlElement(name = "DateCompleted")
    protected DateCompleted dateCompleted;
    @XmlElement(name = "DateRevised")
    protected DateRevised dateRevised;
    @XmlElement(name = "Article", required = true)
    protected Article article;
    @XmlElement(name = "MedlineJournalInfo", required = true)
    protected MedlineJournalInfo medlineJournalInfo;
    @XmlElement(name = "ChemicalList")
    protected ChemicalList chemicalList;
    @XmlElement(name = "SupplMeshList")
    protected SupplMeshList supplMeshList;
    @XmlElement(name = "CitationSubset")
    protected List<CitationSubset> citationSubset;
    @XmlElement(name = "CommentsCorrectionsList")
    protected CommentsCorrectionsList commentsCorrectionsList;
    @XmlElement(name = "GeneSymbolList")
    protected GeneSymbolList geneSymbolList;
    @XmlElement(name = "MeshHeadingList")
    protected MeshHeadingList meshHeadingList;
    @XmlElement(name = "NumberOfReferences")
    protected String numberOfReferences;
    @XmlElement(name = "PersonalNameSubjectList")
    protected PersonalNameSubjectList personalNameSubjectList;
    @XmlElement(name = "OtherID")
    protected List<OtherID> otherID;
    @XmlElement(name = "OtherAbstract")
    protected List<OtherAbstract> otherAbstract;
    @XmlElement(name = "KeywordList")
    protected List<KeywordList> keywordList;
    @XmlElement(name = "CoiStatement")
    protected String coiStatement;
    @XmlElement(name = "SpaceFlightMission")
    protected List<SpaceFlightMission> spaceFlightMission;
    @XmlElement(name = "InvestigatorList")
    protected InvestigatorList investigatorList;
    @XmlElement(name = "GeneralNote")
    protected List<GeneralNote> generalNote;

    /**
     * Ruft den Wert der owner-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        if (owner == null) {
            return "NLM";
        } else {
            return owner;
        }
    }

    /**
     * Legt den Wert der owner-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Ruft den Wert der status-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Legt den Wert der status-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Ruft den Wert der versionID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionID() {
        return versionID;
    }

    /**
     * Legt den Wert der versionID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionID(String value) {
        this.versionID = value;
    }

    /**
     * Ruft den Wert der versionDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionDate() {
        return versionDate;
    }

    /**
     * Legt den Wert der versionDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionDate(String value) {
        this.versionDate = value;
    }

    /**
     * Ruft den Wert der indexingMethod-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndexingMethod() {
        return indexingMethod;
    }

    /**
     * Legt den Wert der indexingMethod-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndexingMethod(String value) {
        this.indexingMethod = value;
    }

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
     * Ruft den Wert der dateCompleted-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateCompleted }
     *     
     */
    public DateCompleted getDateCompleted() {
        return dateCompleted;
    }

    /**
     * Legt den Wert der dateCompleted-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateCompleted }
     *     
     */
    public void setDateCompleted(DateCompleted value) {
        this.dateCompleted = value;
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
     * Ruft den Wert der article-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Article }
     *     
     */
    public Article getArticle() {
        return article;
    }

    /**
     * Legt den Wert der article-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Article }
     *     
     */
    public void setArticle(Article value) {
        this.article = value;
    }

    /**
     * Ruft den Wert der medlineJournalInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MedlineJournalInfo }
     *     
     */
    public MedlineJournalInfo getMedlineJournalInfo() {
        return medlineJournalInfo;
    }

    /**
     * Legt den Wert der medlineJournalInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MedlineJournalInfo }
     *     
     */
    public void setMedlineJournalInfo(MedlineJournalInfo value) {
        this.medlineJournalInfo = value;
    }

    /**
     * Ruft den Wert der chemicalList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ChemicalList }
     *     
     */
    public ChemicalList getChemicalList() {
        return chemicalList;
    }

    /**
     * Legt den Wert der chemicalList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ChemicalList }
     *     
     */
    public void setChemicalList(ChemicalList value) {
        this.chemicalList = value;
    }

    /**
     * Ruft den Wert der supplMeshList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SupplMeshList }
     *     
     */
    public SupplMeshList getSupplMeshList() {
        return supplMeshList;
    }

    /**
     * Legt den Wert der supplMeshList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplMeshList }
     *     
     */
    public void setSupplMeshList(SupplMeshList value) {
        this.supplMeshList = value;
    }

    /**
     * Gets the value of the citationSubset property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the citationSubset property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCitationSubset().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CitationSubset }
     * 
     * 
     */
    public List<CitationSubset> getCitationSubset() {
        if (citationSubset == null) {
            citationSubset = new ArrayList<CitationSubset>();
        }
        return this.citationSubset;
    }

    /**
     * Ruft den Wert der commentsCorrectionsList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CommentsCorrectionsList }
     *     
     */
    public CommentsCorrectionsList getCommentsCorrectionsList() {
        return commentsCorrectionsList;
    }

    /**
     * Legt den Wert der commentsCorrectionsList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CommentsCorrectionsList }
     *     
     */
    public void setCommentsCorrectionsList(CommentsCorrectionsList value) {
        this.commentsCorrectionsList = value;
    }

    /**
     * Ruft den Wert der geneSymbolList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GeneSymbolList }
     *     
     */
    public GeneSymbolList getGeneSymbolList() {
        return geneSymbolList;
    }

    /**
     * Legt den Wert der geneSymbolList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GeneSymbolList }
     *     
     */
    public void setGeneSymbolList(GeneSymbolList value) {
        this.geneSymbolList = value;
    }

    /**
     * Ruft den Wert der meshHeadingList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MeshHeadingList }
     *     
     */
    public MeshHeadingList getMeshHeadingList() {
        return meshHeadingList;
    }

    /**
     * Legt den Wert der meshHeadingList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MeshHeadingList }
     *     
     */
    public void setMeshHeadingList(MeshHeadingList value) {
        this.meshHeadingList = value;
    }

    /**
     * Ruft den Wert der numberOfReferences-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfReferences() {
        return numberOfReferences;
    }

    /**
     * Legt den Wert der numberOfReferences-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfReferences(String value) {
        this.numberOfReferences = value;
    }

    /**
     * Ruft den Wert der personalNameSubjectList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PersonalNameSubjectList }
     *     
     */
    public PersonalNameSubjectList getPersonalNameSubjectList() {
        return personalNameSubjectList;
    }

    /**
     * Legt den Wert der personalNameSubjectList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonalNameSubjectList }
     *     
     */
    public void setPersonalNameSubjectList(PersonalNameSubjectList value) {
        this.personalNameSubjectList = value;
    }

    /**
     * Gets the value of the otherID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherID }
     * 
     * 
     */
    public List<OtherID> getOtherID() {
        if (otherID == null) {
            otherID = new ArrayList<OtherID>();
        }
        return this.otherID;
    }

    /**
     * Gets the value of the otherAbstract property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherAbstract property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherAbstract().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherAbstract }
     * 
     * 
     */
    public List<OtherAbstract> getOtherAbstract() {
        if (otherAbstract == null) {
            otherAbstract = new ArrayList<OtherAbstract>();
        }
        return this.otherAbstract;
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
     * Ruft den Wert der coiStatement-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoiStatement() {
        return coiStatement;
    }

    /**
     * Legt den Wert der coiStatement-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoiStatement(String value) {
        this.coiStatement = value;
    }

    /**
     * Gets the value of the spaceFlightMission property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spaceFlightMission property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpaceFlightMission().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SpaceFlightMission }
     * 
     * 
     */
    public List<SpaceFlightMission> getSpaceFlightMission() {
        if (spaceFlightMission == null) {
            spaceFlightMission = new ArrayList<SpaceFlightMission>();
        }
        return this.spaceFlightMission;
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
     * Gets the value of the generalNote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the generalNote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGeneralNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GeneralNote }
     * 
     * 
     */
    public List<GeneralNote> getGeneralNote() {
        if (generalNote == null) {
            generalNote = new ArrayList<GeneralNote>();
        }
        return this.generalNote;
    }

}
