/*
 * Record.java
 * 
 * 22 apr 2017
 */
package com.gmail.gcolaianni5.jris.bean;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A single RIS record. It contains all the allowed tag from RIS format.
 *
 * @author Gianluca Colaianni -- g.colaianni5@gmail.com
 * @version 1.0
 * @since 22 apr 2017
 */
public class Record {
	
	/**
	 * TY.
	 */
	private Type type;
	
	/**
	 * A1.
	 */
	private List<String> firstAuthors;
	
	/**
	 * A2.
	 */
	private List<String> secondaryAuthors;
	
	/**
	 * A3.
	 */
	private List<String> tertiaryAuthors;
	
	/**
	 * A4.
	 */
	private List<String> subsidiaryAuthors;
	
	/**
	 * AB.
	 */
	private String abstr;
	
	/**
	 * AD.
	 */
	private String authorAddress;
	
	/**
	 * AN.
	 */
	private String accessionNumber;
	
	/**
	 * AU.
	 */
	private List<String> authors;
	
	/**
	 * AV.
	 */
	private String archivesLocation;
	
	/**
	 * BT.
	 */
	private String bt;
	
	/**
	 * C1.
	 */
	private String custom1;
	
	/**
	 * C2.
	 */
	private String custom2;
	
	/**
	 * C3.
	 */
	private String custom3;
	
	/**
	 * C4.
	 */
	private String custom4;
	
	/**
	 * C5.
	 */
	private String custom5;
	
	/**
	 * C6.
	 */
	private String custom6;
	
	/**
	 * C7.
	 */
	private String custom7;
	
	/**
	 * C8.
	 */
	private String custom8;
	
	/**
	 * CA.
	 */
	private String caption;
	
	/**
	 * CN.
	 */
	private String callNumber;
	
	/**
	 * CP.
	 */
	private String cp;
	
	/**
	 * CT.
	 */
	private String unpublishedReferenceTitle;
	
	/**
	 * CY.
	 */
	private String placePublished;
	
	/**
	 * DA.
	 */
	private String date;
	
	/**
	 * DB.
	 */
	private String databaseName;
	
	/**
	 * DO.
	 */
	private String doi;
	
	/**
	 * DP.
	 */
	private String databaseProvider;
	
	/**
	 * ED.
	 */
	private String editor;
	
	/**
	 * EP.
	 */
	private Integer endPage;
	
	/**
	 * ED.
	 */
	private String edition;
	
	/**
	 * ID.
	 */
	private String referenceId;
	
	/**
	 * IS.
	 */
	private Integer issueNumber;
	
	/**
	 * J1. Max 255 characters.
	 */
	private String periodicalNameUserAbbrevation;
	
	/**
	 * J2. This field is used for the abbreviated title of a book or journal name, the latter mapped to T2.
	 */
	private String alternativeTitle;
	
	/**
	 * JA. This is the periodical in which the article was (or is to be, in the case of in-press references) published. This is an alphanumeric field of up to 255 characters.
	 */
	private String periodicalNameStandardAbbrevation;
	
	/**
	 * JF. Journal/Periodical name: full format. This is an alphanumeric field of up to 255 characters.
	 */
	private String periodicalNameFullFormatJF;
	
	/**
	 * JO. Journal/Periodical name: full format. This is an alphanumeric field of up to 255 characters.
	 */
	private String periodicalNameFullFormatJO;
	
	/**
	 * KW.
	 */
	private List<String> keywords;
	
	/**
	 * L1. There is no practical limit to the length of this field. URL addresses can be entered individually, one per tag or multiple addresses can be entered on one line using a semi-colon as a separator.
	 */
	private List<String> pdfLinks;
	
	/**
	 * L2. Link to Full-text. There is no practical limit to the length of this field. URL addresses can be entered individually, one per tag or multiple addresses can be entered on one line using a semi-colon as a separator.
	 */
	private List<String> fullTextLinks;
	
	/**
	 * L3. Related Records. There is no practical limit to the length of this field.
	 */
	private List<String> relatedRecords;
	
	/**
	 * L4. Image(s). There is no practical limit to the length of this field.
	 */
	private List<String> images;
	
	/**
	 * LA.
	 */
	private String language;
	
	/**
	 * LB.
	 */
	private String label;
	
	/**
	 * LK.
	 */
	private String websiteLink;
	
	/**
	 * M1.
	 */
	private Integer number;
	
	/**
	 * M2. This is an alphanumeric field and there is no practical limit to the length of this field.
	 */
	private String miscellaneous2;
	
	/**
	 * M3.
	 */
	private String typeOfWork;
	
	/**
	 * N1.
	 */
	private String notes;
	
	/**
	 * N2. Abstract. This is a free text field and can contain alphanumeric characters. There is no practical length limit to this field.
	 */
	private String abstr2;
	
	/**
	 * OP.
	 */
	private String originalPublication;
	
	/**
	 * PB.
	 */
	private String publisher;
	
	/**
	 * PP.
	 */
	private String publishingPlace;
	
	/**
	 * PY. Publication year (YYYY/MM/DD).
	 */
	private String publicationYear;
	
	/**
	 * RI.
	 */
	private String reviewedItem;
	
	/**
	 * RN.
	 */
	private String researchNotes;
	
	/**
	 * RP.
	 */
	private String reprintEdition;
	
	/**
	 * SE.
	 */
	private String section;
	
	/**
	 * SN.
	 */
	private String isbnIssn;
	
	/**
	 * SP.
	 */
	private Integer startPage;
	
	/**
	 * ST.
	 */
	private String shortTitle;
	
	/**
	 * T1.
	 */
	private String primaryTitle;
	
	/**
	 * T2.
	 */
	private String secondaryTitle;
	
	/**
	 * T3.
	 */
	private String tertiaryTitle;
	
	/**
	 * TA.
	 */
	private String translatedAuthor;
	
	/**
	 * TI.
	 */
	private String title;
	
	/**
	 * TT.
	 */
	private String transaltedTitle;
	
	/**
	 * U1. User definable 1. This is an alphanumeric field and there is no practical limit to the length of this field.
	 */
	private String userDefinable1;
	
	/**
	 * U. User definable 2. This is an alphanumeric field and there is no practical limit to the length of this field.
	 */
	private String userDefinable2;
	
	/**
	 * U3. User definable 3. This is an alphanumeric field and there is no practical limit to the length of this field.
	 */
	private String userDefinable3;
	
	/**
	 * U4. User definable 4. This is an alphanumeric field and there is no practical limit to the length of this field.
	 */
	private String userDefinable4;
	
	/**
	 * U5. User definable 5. This is an alphanumeric field and there is no practical limit to the length of this field.
	 */
	private String userDefinable5;
	
	/**
	 * UR.
	 */
	private String url;
	
	/**
	 * VL.
	 */
	private String volumeNumber;
	
	/**
	 * VO.
	 */
	private String publisherStandardNumber;
	
	/**
	 * Y1.
	 */
	private String primaryDate;
	
	/**
	 * Y2.
	 */
	private String accessDate;
	
	/**
	 * 
	 */
	public Record() {
	}

	/**
	 * Return type value or reference.
	 * @return type value or reference.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Set type value or reference.
	 * @param type Value to set.
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Return firstAuthors value or reference.
	 * @return firstAuthors value or reference.
	 */
	public List<String> getFirstAuthors() {
		return firstAuthors;
	}

	/**
	 * Add a secondary author to the list. If the list is null, it is created.
	 * @param firstAuthor the value to add.
	 */
	public void addFirstAuthor(String firstAuthor) {
		if (this.firstAuthors == null) {
			this.firstAuthors = new LinkedList<String>();
		}
		this.firstAuthors.add(firstAuthor);
	}

	/**
	 * Return secondaryAuthors value or reference.
	 * @return secondaryAuthors value or reference.
	 */
	public List<String> getSecondaryAuthors() {
		return secondaryAuthors;
	}
	
	/**
	 * Add a secondary author to the list. If the list is null, it is created.
	 * @param secondaryAuthor the value to add.
	 */
	public void addSecondaryAuthor(String secondaryAuthor) {
		if (this.secondaryAuthors == null) {
			this.secondaryAuthors = new LinkedList<String>();
		}
		this.secondaryAuthors.add(secondaryAuthor);
	}

	/**
	 * Return tertiaryAuthors value or reference.
	 * @return tertiaryAuthors value or reference.
	 */
	public List<String> getTertiaryAuthors() {
		return tertiaryAuthors;
	}
	
	/**
	 * Add a tertiary author to the list. If the list is null, it is created.
	 * @param tertiaryAuthor the value to add.
	 */
	public void addTertiaryAuthor(String tertiaryAuthor) {
		if (this.tertiaryAuthors == null) {
			this.tertiaryAuthors = new LinkedList<String>();
		}
		this.tertiaryAuthors.add(tertiaryAuthor);
	}
	
	/**
	 * Return subsidiaryAuthors value or reference.
	 * @return subsidiaryAuthors value or reference.
	 */
	public List<String> getSubsidiaryAuthors() {
		return subsidiaryAuthors;
	}

	/**
	 * Add a subsidiary author to the list. If the list is null, it is created.
	 * @param subsidiaryAuthor the value to add.
	 */
	public void addSubsidiaryAuthor(String subsidiaryAuthor) {
		if (this.subsidiaryAuthors == null) {
			this.subsidiaryAuthors = new LinkedList<String>();
		}
		this.subsidiaryAuthors.add(subsidiaryAuthor);
	}

	/**
	 * Return abstr value or reference.
	 * @return abstr value or reference.
	 */
	public String getAbstr() {
		return abstr;
	}

	/**
	 * Set abstr value or reference.
	 * @param abstr Value to set.
	 */
	public void setAbstr(String abstr) {
		this.abstr = abstr;
	}

	/**
	 * Return authorAddress value or reference.
	 * @return authorAddress value or reference.
	 */
	public String getAuthorAddress() {
		return authorAddress;
	}

	/**
	 * Set authorAddress value or reference.
	 * @param authorAddress Value to set.
	 */
	public void setAuthorAddress(String authorAddress) {
		this.authorAddress = authorAddress;
	}

	/**
	 * Return accessionNumber value or reference.
	 * @return accessionNumber value or reference.
	 */
	public String getAccessionNumber() {
		return accessionNumber;
	}

	/**
	 * Set accessionNumber value or reference.
	 * @param accessionNumber Value to set.
	 */
	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	/**
	 * Return authors value or reference.
	 * @return authors value or reference.
	 */
	public List<String> getAuthors() {
		return authors;
	}

	/**
	 * Add an author to the list. If the list is null, it is created.
	 * @param author the value to add.
	 */
	public void addAuthor(String author) {
		if (this.authors == null) {
			this.authors = new LinkedList<String>();
		}
		this.authors.add(author);
	}

	/**
	 * Return archivesLocation value or reference.
	 * @return archivesLocation value or reference.
	 */
	public String getArchivesLocation() {
		return archivesLocation;
	}

	/**
	 * Set archivesLocation value or reference.
	 * @param archivesLocation Value to set.
	 */
	public void setArchivesLocation(String archivesLocation) {
		this.archivesLocation = archivesLocation;
	}

	/**
	 * Return bt value or reference.
	 * @return bt value or reference.
	 */
	public String getBt() {
		return bt;
	}

	/**
	 * Set bt value or reference.
	 * @param bt Value to set.
	 */
	public void setBt(String bt) {
		this.bt = bt;
	}

	/**
	 * Return custom1 value or reference.
	 * @return custom1 value or reference.
	 */
	public String getCustom1() {
		return custom1;
	}

	/**
	 * Set custom1 value or reference.
	 * @param custom1 Value to set.
	 */
	public void setCustom1(String custom1) {
		this.custom1 = custom1;
	}

	/**
	 * Return custom2 value or reference.
	 * @return custom2 value or reference.
	 */
	public String getCustom2() {
		return custom2;
	}

	/**
	 * Set custom2 value or reference.
	 * @param custom2 Value to set.
	 */
	public void setCustom2(String custom2) {
		this.custom2 = custom2;
	}

	/**
	 * Return custom3 value or reference.
	 * @return custom3 value or reference.
	 */
	public String getCustom3() {
		return custom3;
	}

	/**
	 * Set custom3 value or reference.
	 * @param custom3 Value to set.
	 */
	public void setCustom3(String custom3) {
		this.custom3 = custom3;
	}

	/**
	 * Return custom4 value or reference.
	 * @return custom4 value or reference.
	 */
	public String getCustom4() {
		return custom4;
	}

	/**
	 * Set custom4 value or reference.
	 * @param custom4 Value to set.
	 */
	public void setCustom4(String custom4) {
		this.custom4 = custom4;
	}

	/**
	 * Return custom5 value or reference.
	 * @return custom5 value or reference.
	 */
	public String getCustom5() {
		return custom5;
	}

	/**
	 * Set custom5 value or reference.
	 * @param custom5 Value to set.
	 */
	public void setCustom5(String custom5) {
		this.custom5 = custom5;
	}

	/**
	 * Return custom6 value or reference.
	 * @return custom6 value or reference.
	 */
	public String getCustom6() {
		return custom6;
	}

	/**
	 * Set custom6 value or reference.
	 * @param custom6 Value to set.
	 */
	public void setCustom6(String custom6) {
		this.custom6 = custom6;
	}

	/**
	 * Return custom7 value or reference.
	 * @return custom7 value or reference.
	 */
	public String getCustom7() {
		return custom7;
	}

	/**
	 * Set custom7 value or reference.
	 * @param custom7 Value to set.
	 */
	public void setCustom7(String custom7) {
		this.custom7 = custom7;
	}

	/**
	 * Return custom8 value or reference.
	 * @return custom8 value or reference.
	 */
	public String getCustom8() {
		return custom8;
	}

	/**
	 * Set custom8 value or reference.
	 * @param custom8 Value to set.
	 */
	public void setCustom8(String custom8) {
		this.custom8 = custom8;
	}

	/**
	 * Return caption value or reference.
	 * @return caption value or reference.
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Set caption value or reference.
	 * @param caption Value to set.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * Return callNumber value or reference.
	 * @return callNumber value or reference.
	 */
	public String getCallNumber() {
		return callNumber;
	}

	/**
	 * Set callNumber value or reference.
	 * @param callNumber Value to set.
	 */
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	/**
	 * Return cp value or reference.
	 * @return cp value or reference.
	 */
	public String getCp() {
		return cp;
	}

	/**
	 * Set cp value or reference.
	 * @param cp Value to set.
	 */
	public void setCp(String cp) {
		this.cp = cp;
	}

	/**
	 * Return unpublishedReferenceTitle value or reference.
	 * @return unpublishedReferenceTitle value or reference.
	 */
	public String getUnpublishedReferenceTitle() {
		return unpublishedReferenceTitle;
	}

	/**
	 * Set unpublishedReferenceTitle value or reference.
	 * @param unpublishedReferenceTitle Value to set.
	 */
	public void setUnpublishedReferenceTitle(String unpublishedReferenceTitle) {
		this.unpublishedReferenceTitle = unpublishedReferenceTitle;
	}

	/**
	 * Return placePublished value or reference.
	 * @return placePublished value or reference.
	 */
	public String getPlacePublished() {
		return placePublished;
	}

	/**
	 * Set placePublished value or reference.
	 * @param placePublished Value to set.
	 */
	public void setPlacePublished(String placePublished) {
		this.placePublished = placePublished;
	}

	/**
	 * Return date value or reference.
	 * @return date value or reference.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Set date value or reference.
	 * @param date Value to set.
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Return databaseName value or reference.
	 * @return databaseName value or reference.
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * Set databaseName value or reference.
	 * @param databaseName Value to set.
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * Return doi value or reference.
	 * @return doi value or reference.
	 */
	public String getDoi() {
		return doi;
	}

	/**
	 * Set doi value or reference.
	 * @param doi Value to set.
	 */
	public void setDoi(String doi) {
		this.doi = doi;
	}

	/**
	 * Return databaseProvider value or reference.
	 * @return databaseProvider value or reference.
	 */
	public String getDatabaseProvider() {
		return databaseProvider;
	}

	/**
	 * Set databaseProvider value or reference.
	 * @param databaseProvider Value to set.
	 */
	public void setDatabaseProvider(String databaseProvider) {
		this.databaseProvider = databaseProvider;
	}

	/**
	 * Return editor value or reference.
	 * @return editor value or reference.
	 */
	public String getEditor() {
		return editor;
	}

	/**
	 * Set editor value or reference.
	 * @param editor Value to set.
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}

	/**
	 * Return endPage value or reference.
	 * @return endPage value or reference.
	 */
	public Integer getEndPage() {
		return endPage;
	}

	/**
	 * Set endPage value or reference.
	 * @param endPage Value to set.
	 */
	public void setEndPage(Integer endPage) {
		this.endPage = endPage;
	}

	/**
	 * Return edition value or reference.
	 * @return edition value or reference.
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * Set edition value or reference.
	 * @param edition Value to set.
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

	/**
	 * Return referenceId value or reference.
	 * @return referenceId value or reference.
	 */
	public String getReferenceId() {
		return referenceId;
	}

	/**
	 * Set referenceId value or reference.
	 * @param referenceId Value to set.
	 */
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	/**
	 * Return issueNumber value or reference.
	 * @return issueNumber value or reference.
	 */
	public Integer getIssueNumber() {
		return issueNumber;
	}

	/**
	 * Set issueNumber value or reference.
	 * @param issueNumber Value to set.
	 */
	public void setIssueNumber(Integer issueNumber) {
		this.issueNumber = issueNumber;
	}

	/**
	 * Return periodicalNameUserAbbrevation value or reference.
	 * @return periodicalNameUserAbbrevation value or reference.
	 */
	public String getPeriodicalNameUserAbbrevation() {
		return periodicalNameUserAbbrevation;
	}

	/**
	 * Set periodicalNameUserAbbrevation value or reference.
	 * @param periodicalNameUserAbbrevation Value to set.
	 */
	public void setPeriodicalNameUserAbbrevation(String periodicalNameUserAbbrevation) {
		this.periodicalNameUserAbbrevation = periodicalNameUserAbbrevation;
	}

	/**
	 * Return alternativeTitle value or reference.
	 * @return alternativeTitle value or reference.
	 */
	public String getAlternativeTitle() {
		return alternativeTitle;
	}

	/**
	 * Set alternativeTitle value or reference.
	 * @param alternativeTitle Value to set.
	 */
	public void setAlternativeTitle(String alternativeTitle) {
		this.alternativeTitle = alternativeTitle;
	}

	/**
	 * Return periodicalNameStandardAbbrevation value or reference.
	 * @return periodicalNameStandardAbbrevation value or reference.
	 */
	public String getPeriodicalNameStandardAbbrevation() {
		return periodicalNameStandardAbbrevation;
	}

	/**
	 * Set periodicalNameStandardAbbrevation value or reference.
	 * @param periodicalNameStandardAbbrevation Value to set.
	 */
	public void setPeriodicalNameStandardAbbrevation(String periodicalNameStandardAbbrevation) {
		this.periodicalNameStandardAbbrevation = periodicalNameStandardAbbrevation;
	}

	/**
	 * Return periodicalNameFullFormatJF value or reference.
	 * @return periodicalNameFullFormatJF value or reference.
	 */
	public String getPeriodicalNameFullFormatJF() {
		return periodicalNameFullFormatJF;
	}

	/**
	 * Set periodicalNameFullFormatJF value or reference.
	 * @param periodicalNameFullFormatJF Value to set.
	 */
	public void setPeriodicalNameFullFormatJF(String periodicalNameFullFormatJF) {
		this.periodicalNameFullFormatJF = periodicalNameFullFormatJF;
	}

	/**
	 * Return periodicalNameFullFormatJO value or reference.
	 * @return periodicalNameFullFormatJO value or reference.
	 */
	public String getPeriodicalNameFullFormatJO() {
		return periodicalNameFullFormatJO;
	}

	/**
	 * Set periodicalNameFullFormatJO value or reference.
	 * @param periodicalNameFullFormatJO Value to set.
	 */
	public void setPeriodicalNameFullFormatJO(String periodicalNameFullFormatJO) {
		this.periodicalNameFullFormatJO = periodicalNameFullFormatJO;
	}

	/**
	 * Return keywords value or reference.
	 * @return keywords value or reference.
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
	 * Add a keyword to the list. If the list is empty, it is created.
	 * @param keyword
	 */
	public void addKeyword(String keyword) {
		if (this.keywords == null) {
			this.keywords = new LinkedList<String>();
		}
		this.keywords.add(keyword);
	}

	/**
	 * Return pdfLinks value or reference.
	 * @return pdfLinks value or reference.
	 */
	public List<String> getPdfLinks() {
		return pdfLinks;
	}

	/**
	 * Add a pdf link to the list. If the list is empty, it is created.
	 * @param pdfLink value to add.
	 */
	public void addPdfLink(String pdfLink) {
		if (this.pdfLinks == null) {
			this.pdfLinks = new LinkedList<String>();
		}
		this.pdfLinks.add(pdfLink);
	}

	/**
	 * Return fullTextLinks value or reference.
	 * @return fullTextLinks value or reference.
	 */
	public List<String> getFullTextLinks() {
		return fullTextLinks;
	}

	/**
	 * Add a full text link to the list. If the list is empty, it is created.
	 * @param fullTextLink value to add.
	 */
	public void addFullTextLink(String fullTextLink) {
		if (this.fullTextLinks == null) {
			this.fullTextLinks = new LinkedList<String>();
		}
		this.fullTextLinks.add(fullTextLink);
	}

	/**
	 * Return relatedRecords value or reference.
	 * @return relatedRecords value or reference.
	 */
	public List<String> getRelatedRecords() {
		return relatedRecords;
	}

	/**
	 * Add a related record to the list. If the list is null, it s created.
	 * @param relatedRecord value to add.
	 */
	public void addRelatedRecord(String relatedRecord) {
		if (this.relatedRecords == null) {
			this.relatedRecords = new LinkedList<String>();
		}
		this.relatedRecords.add(relatedRecord);
	}

	/**
	 * Return images value or reference.
	 * @return images value or reference.
	 */
	public List<String> getImages() {
		return images;
	}

	/**
	 * Add an image link to the list. If the list is null, it is created.
	 * @param image value to add.
	 */
	public void addImage(String image) {
		if (this.images == null) {
			this.images = new LinkedList<String>();
		}
		this.images.add(image);
	}

	/**
	 * Return language value or reference.
	 * @return language value or reference.
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Set language value or reference.
	 * @param language Value to set.
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Return label value or reference.
	 * @return label value or reference.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set label value or reference.
	 * @param label Value to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Return websiteLink value or reference.
	 * @return websiteLink value or reference.
	 */
	public String getWebsiteLink() {
		return websiteLink;
	}

	/**
	 * Set websiteLink value or reference.
	 * @param websiteLink Value to set.
	 */
	public void setWebsiteLink(String websiteLink) {
		this.websiteLink = websiteLink;
	}

	/**
	 * Return number value or reference.
	 * @return number value or reference.
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * Set number value or reference.
	 * @param number Value to set.
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * Return miscellaneous2 value or reference.
	 * @return miscellaneous2 value or reference.
	 */
	public String getMiscellaneous2() {
		return miscellaneous2;
	}

	/**
	 * Set miscellaneous2 value or reference.
	 * @param miscellaneous2 Value to set.
	 */
	public void setMiscellaneous2(String miscellaneous2) {
		this.miscellaneous2 = miscellaneous2;
	}

	/**
	 * Return typeOfWork value or reference.
	 * @return typeOfWork value or reference.
	 */
	public String getTypeOfWork() {
		return typeOfWork;
	}

	/**
	 * Set typeOfWork value or reference.
	 * @param typeOfWork Value to set.
	 */
	public void setTypeOfWork(String typeOfWork) {
		this.typeOfWork = typeOfWork;
	}

	/**
	 * Return notes value or reference.
	 * @return notes value or reference.
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Set notes value or reference.
	 * @param notes Value to set.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Return abstr2 value or reference.
	 * @return abstr2 value or reference.
	 */
	public String getAbstr2() {
		return abstr2;
	}

	/**
	 * Set abstr2 value or reference.
	 * @param abstr2 Value to set.
	 */
	public void setAbstr2(String abstr2) {
		this.abstr2 = abstr2;
	}

	/**
	 * Return originalPublication value or reference.
	 * @return originalPublication value or reference.
	 */
	public String getOriginalPublication() {
		return originalPublication;
	}

	/**
	 * Set originalPublication value or reference.
	 * @param originalPublication Value to set.
	 */
	public void setOriginalPublication(String originalPublication) {
		this.originalPublication = originalPublication;
	}

	/**
	 * Return publisher value or reference.
	 * @return publisher value or reference.
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * Set publisher value or reference.
	 * @param publisher Value to set.
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * Return publishingPlace value or reference.
	 * @return publishingPlace value or reference.
	 */
	public String getPublishingPlace() {
		return publishingPlace;
	}

	/**
	 * Set publishingPlace value or reference.
	 * @param publishingPlace Value to set.
	 */
	public void setPublishingPlace(String publishingPlace) {
		this.publishingPlace = publishingPlace;
	}

	/**
	 * Return publicationYear value or reference.
	 * @return publicationYear value or reference.
	 */
	public String getPublicationYear() {
		return publicationYear;
	}

	/**
	 * Set publicationYear value or reference.
	 * @param publicationYear Value to set.
	 */
	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * Return reviewedItem value or reference.
	 * @return reviewedItem value or reference.
	 */
	public String getReviewedItem() {
		return reviewedItem;
	}

	/**
	 * Set reviewedItem value or reference.
	 * @param reviewedItem Value to set.
	 */
	public void setReviewedItem(String reviewedItem) {
		this.reviewedItem = reviewedItem;
	}

	/**
	 * Return researchNotes value or reference.
	 * @return researchNotes value or reference.
	 */
	public String getResearchNotes() {
		return researchNotes;
	}

	/**
	 * Set researchNotes value or reference.
	 * @param researchNotes Value to set.
	 */
	public void setResearchNotes(String researchNotes) {
		this.researchNotes = researchNotes;
	}

	/**
	 * Return reprintEdition value or reference.
	 * @return reprintEdition value or reference.
	 */
	public String getReprintEdition() {
		return reprintEdition;
	}

	/**
	 * Set reprintEdition value or reference.
	 * @param reprintEdition Value to set.
	 */
	public void setReprintEdition(String reprintEdition) {
		this.reprintEdition = reprintEdition;
	}

	/**
	 * Return section value or reference.
	 * @return section value or reference.
	 */
	public String getSection() {
		return section;
	}

	/**
	 * Set section value or reference.
	 * @param section Value to set.
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * Return isbnIssn value or reference.
	 * @return isbnIssn value or reference.
	 */
	public String getIsbnIssn() {
		return isbnIssn;
	}

	/**
	 * Set isbnIssn value or reference.
	 * @param isbnIssn Value to set.
	 */
	public void setIsbnIssn(String isbnIssn) {
		this.isbnIssn = isbnIssn;
	}

	/**
	 * Return startPage value or reference.
	 * @return startPage value or reference.
	 */
	public Integer getStartPage() {
		return startPage;
	}

	/**
	 * Set startPage value or reference.
	 * @param startPage Value to set.
	 */
	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	/**
	 * Return shortTitle value or reference.
	 * @return shortTitle value or reference.
	 */
	public String getShortTitle() {
		return shortTitle;
	}

	/**
	 * Set shortTitle value or reference.
	 * @param shortTitle Value to set.
	 */
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	/**
	 * Return primaryTitle value or reference.
	 * @return primaryTitle value or reference.
	 */
	public String getPrimaryTitle() {
		return primaryTitle;
	}

	/**
	 * Set primaryTitle value or reference.
	 * @param primaryTitle Value to set.
	 */
	public void setPrimaryTitle(String primaryTitle) {
		this.primaryTitle = primaryTitle;
	}

	/**
	 * Return secondaryTitle value or reference.
	 * @return secondaryTitle value or reference.
	 */
	public String getSecondaryTitle() {
		return secondaryTitle;
	}

	/**
	 * Set secondaryTitle value or reference.
	 * @param secondaryTitle Value to set.
	 */
	public void setSecondaryTitle(String secondaryTitle) {
		this.secondaryTitle = secondaryTitle;
	}

	/**
	 * Return tertiaryTitle value or reference.
	 * @return tertiaryTitle value or reference.
	 */
	public String getTertiaryTitle() {
		return tertiaryTitle;
	}

	/**
	 * Set tertiaryTitle value or reference.
	 * @param tertiaryTitle Value to set.
	 */
	public void setTertiaryTitle(String tertiaryTitle) {
		this.tertiaryTitle = tertiaryTitle;
	}

	/**
	 * Return translatedAuthor value or reference.
	 * @return translatedAuthor value or reference.
	 */
	public String getTranslatedAuthor() {
		return translatedAuthor;
	}

	/**
	 * Set translatedAuthor value or reference.
	 * @param translatedAuthor Value to set.
	 */
	public void setTranslatedAuthor(String translatedAuthor) {
		this.translatedAuthor = translatedAuthor;
	}

	/**
	 * Return title value or reference.
	 * @return title value or reference.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set title value or reference.
	 * @param title Value to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Return transaltedTitle value or reference.
	 * @return transaltedTitle value or reference.
	 */
	public String getTransaltedTitle() {
		return transaltedTitle;
	}

	/**
	 * Set transaltedTitle value or reference.
	 * @param transaltedTitle Value to set.
	 */
	public void setTransaltedTitle(String transaltedTitle) {
		this.transaltedTitle = transaltedTitle;
	}

	/**
	 * Return userDefinable1 value or reference.
	 * @return userDefinable1 value or reference.
	 */
	public String getUserDefinable1() {
		return userDefinable1;
	}

	/**
	 * Set userDefinable1 value or reference.
	 * @param userDefinable1 Value to set.
	 */
	public void setUserDefinable1(String userDefinable1) {
		this.userDefinable1 = userDefinable1;
	}

	/**
	 * Return userDefinable2 value or reference.
	 * @return userDefinable2 value or reference.
	 */
	public String getUserDefinable2() {
		return userDefinable2;
	}

	/**
	 * Set userDefinable2 value or reference.
	 * @param userDefinable2 Value to set.
	 */
	public void setUserDefinable2(String userDefinable2) {
		this.userDefinable2 = userDefinable2;
	}

	/**
	 * Return userDefinable3 value or reference.
	 * @return userDefinable3 value or reference.
	 */
	public String getUserDefinable3() {
		return userDefinable3;
	}

	/**
	 * Set userDefinable3 value or reference.
	 * @param userDefinable3 Value to set.
	 */
	public void setUserDefinable3(String userDefinable3) {
		this.userDefinable3 = userDefinable3;
	}

	/**
	 * Return userDefinable4 value or reference.
	 * @return userDefinable4 value or reference.
	 */
	public String getUserDefinable4() {
		return userDefinable4;
	}

	/**
	 * Set userDefinable4 value or reference.
	 * @param userDefinable4 Value to set.
	 */
	public void setUserDefinable4(String userDefinable4) {
		this.userDefinable4 = userDefinable4;
	}

	/**
	 * Return userDefinable5 value or reference.
	 * @return userDefinable5 value or reference.
	 */
	public String getUserDefinable5() {
		return userDefinable5;
	}

	/**
	 * Set userDefinable5 value or reference.
	 * @param userDefinable5 Value to set.
	 */
	public void setUserDefinable5(String userDefinable5) {
		this.userDefinable5 = userDefinable5;
	}

	/**
	 * Return url value or reference.
	 * @return url value or reference.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set url value or reference.
	 * @param url Value to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Return volumeNumber value or reference.
	 * @return volumeNumber value or reference.
	 */
	public String getVolumeNumber() {
		return volumeNumber;
	}

	/**
	 * Set volumeNumber value or reference.
	 * @param volumeNumber Value to set.
	 */
	public void setVolumeNumber(String volumeNumber) {
		this.volumeNumber = volumeNumber;
	}

	/**
	 * Return publisherStandardNumber value or reference.
	 * @return publisherStandardNumber value or reference.
	 */
	public String getPublisherStandardNumber() {
		return publisherStandardNumber;
	}

	/**
	 * Set publisherStandardNumber value or reference.
	 * @param publisherStandardNumber Value to set.
	 */
	public void setPublisherStandardNumber(String publisherStandardNumber) {
		this.publisherStandardNumber = publisherStandardNumber;
	}

	/**
	 * Return primaryDate value or reference.
	 * @return primaryDate value or reference.
	 */
	public String getPrimaryDate() {
		return primaryDate;
	}

	/**
	 * Set primaryDate value or reference.
	 * @param primaryDate Value to set.
	 */
	public void setPrimaryDate(String primaryDate) {
		this.primaryDate = primaryDate;
	}

	/**
	 * Return accessDate value or reference.
	 * @return accessDate value or reference.
	 */
	public String getAccessDate() {
		return accessDate;
	}

	/**
	 * Set accessDate value or reference.
	 * @param accessDate Value to set.
	 */
	public void setAccessDate(String accessDate) {
		this.accessDate = accessDate;
	}

	/* (non-Jsdoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Record [type=");
		builder.append(type);
		builder.append(", firstAuthors=");
		builder.append(firstAuthors);
		builder.append(", secondaryAuthors=");
		builder.append(secondaryAuthors);
		builder.append(", tertiaryAuthors=");
		builder.append(tertiaryAuthors);
		builder.append(", subsidiaryAuthors=");
		builder.append(subsidiaryAuthors);
		builder.append(", abstr=");
		builder.append(abstr);
		builder.append(", authorAddress=");
		builder.append(authorAddress);
		builder.append(", accessionNumber=");
		builder.append(accessionNumber);
		builder.append(", authors=");
		builder.append(authors);
		builder.append(", archivesLocation=");
		builder.append(archivesLocation);
		builder.append(", bt=");
		builder.append(bt);
		builder.append(", custom1=");
		builder.append(custom1);
		builder.append(", custom2=");
		builder.append(custom2);
		builder.append(", custom3=");
		builder.append(custom3);
		builder.append(", custom4=");
		builder.append(custom4);
		builder.append(", custom5=");
		builder.append(custom5);
		builder.append(", custom6=");
		builder.append(custom6);
		builder.append(", custom7=");
		builder.append(custom7);
		builder.append(", custom8=");
		builder.append(custom8);
		builder.append(", caption=");
		builder.append(caption);
		builder.append(", callNumber=");
		builder.append(callNumber);
		builder.append(", cp=");
		builder.append(cp);
		builder.append(", unpublishedReferenceTitle=");
		builder.append(unpublishedReferenceTitle);
		builder.append(", placePublished=");
		builder.append(placePublished);
		builder.append(", date=");
		builder.append(date);
		builder.append(", databaseName=");
		builder.append(databaseName);
		builder.append(", doi=");
		builder.append(doi);
		builder.append(", databaseProvider=");
		builder.append(databaseProvider);
		builder.append(", editor=");
		builder.append(editor);
		builder.append(", endPage=");
		builder.append(endPage);
		builder.append(", edition=");
		builder.append(edition);
		builder.append(", referenceId=");
		builder.append(referenceId);
		builder.append(", issueNumber=");
		builder.append(issueNumber);
		builder.append(", periodicalNameUserAbbrevation=");
		builder.append(periodicalNameUserAbbrevation);
		builder.append(", alternativeTitle=");
		builder.append(alternativeTitle);
		builder.append(", periodicalNameStandardAbbrevation=");
		builder.append(periodicalNameStandardAbbrevation);
		builder.append(", periodicalNameFullFormatJF=");
		builder.append(periodicalNameFullFormatJF);
		builder.append(", periodicalNameFullFormatJO=");
		builder.append(periodicalNameFullFormatJO);
		builder.append(", keywords=");
		builder.append(keywords);
		builder.append(", pdfLinks=");
		builder.append(pdfLinks);
		builder.append(", fullTextLinks=");
		builder.append(fullTextLinks);
		builder.append(", relatedRecords=");
		builder.append(relatedRecords);
		builder.append(", images=");
		builder.append(images);
		builder.append(", language=");
		builder.append(language);
		builder.append(", label=");
		builder.append(label);
		builder.append(", websiteLink=");
		builder.append(websiteLink);
		builder.append(", number=");
		builder.append(number);
		builder.append(", miscellaneous2=");
		builder.append(miscellaneous2);
		builder.append(", typeOfWork=");
		builder.append(typeOfWork);
		builder.append(", notes=");
		builder.append(notes);
		builder.append(", abstr2=");
		builder.append(abstr2);
		builder.append(", originalPublication=");
		builder.append(originalPublication);
		builder.append(", publisher=");
		builder.append(publisher);
		builder.append(", publishingPlace=");
		builder.append(publishingPlace);
		builder.append(", publicationYear=");
		builder.append(publicationYear);
		builder.append(", reviewedItem=");
		builder.append(reviewedItem);
		builder.append(", researchNotes=");
		builder.append(researchNotes);
		builder.append(", reprintEdition=");
		builder.append(reprintEdition);
		builder.append(", section=");
		builder.append(section);
		builder.append(", isbnIssn=");
		builder.append(isbnIssn);
		builder.append(", startPage=");
		builder.append(startPage);
		builder.append(", shortTitle=");
		builder.append(shortTitle);
		builder.append(", primaryTitle=");
		builder.append(primaryTitle);
		builder.append(", secondaryTitle=");
		builder.append(secondaryTitle);
		builder.append(", tertiaryTitle=");
		builder.append(tertiaryTitle);
		builder.append(", translatedAuthor=");
		builder.append(translatedAuthor);
		builder.append(", title=");
		builder.append(title);
		builder.append(", transaltedTitle=");
		builder.append(transaltedTitle);
		builder.append(", userDefinable1=");
		builder.append(userDefinable1);
		builder.append(", userDefinable2=");
		builder.append(userDefinable2);
		builder.append(", userDefinable3=");
		builder.append(userDefinable3);
		builder.append(", userDefinable4=");
		builder.append(userDefinable4);
		builder.append(", userDefinable5=");
		builder.append(userDefinable5);
		builder.append(", url=");
		builder.append(url);
		builder.append(", volumeNumber=");
		builder.append(volumeNumber);
		builder.append(", publisherStandardNumber=");
		builder.append(publisherStandardNumber);
		builder.append(", primaryDate=");
		builder.append(primaryDate);
		builder.append(", accessDate=");
		builder.append(accessDate);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Record record = (Record) o;
		return type == record.type &&
				Objects.equals(firstAuthors, record.firstAuthors) &&
				Objects.equals(secondaryAuthors, record.secondaryAuthors) &&
				Objects.equals(tertiaryAuthors, record.tertiaryAuthors) &&
				Objects.equals(subsidiaryAuthors, record.subsidiaryAuthors) &&
				Objects.equals(abstr, record.abstr) &&
				Objects.equals(authorAddress, record.authorAddress) &&
				Objects.equals(accessionNumber, record.accessionNumber) &&
				Objects.equals(authors, record.authors) &&
				Objects.equals(archivesLocation, record.archivesLocation) &&
				Objects.equals(bt, record.bt) &&
				Objects.equals(custom1, record.custom1) &&
				Objects.equals(custom2, record.custom2) &&
				Objects.equals(custom3, record.custom3) &&
				Objects.equals(custom4, record.custom4) &&
				Objects.equals(custom5, record.custom5) &&
				Objects.equals(custom6, record.custom6) &&
				Objects.equals(custom7, record.custom7) &&
				Objects.equals(custom8, record.custom8) &&
				Objects.equals(caption, record.caption) &&
				Objects.equals(callNumber, record.callNumber) &&
				Objects.equals(cp, record.cp) &&
				Objects.equals(unpublishedReferenceTitle, record.unpublishedReferenceTitle) &&
				Objects.equals(placePublished, record.placePublished) &&
				Objects.equals(date, record.date) &&
				Objects.equals(databaseName, record.databaseName) &&
				Objects.equals(doi, record.doi) &&
				Objects.equals(databaseProvider, record.databaseProvider) &&
				Objects.equals(editor, record.editor) &&
				Objects.equals(endPage, record.endPage) &&
				Objects.equals(edition, record.edition) &&
				Objects.equals(referenceId, record.referenceId) &&
				Objects.equals(issueNumber, record.issueNumber) &&
				Objects.equals(periodicalNameUserAbbrevation, record.periodicalNameUserAbbrevation) &&
				Objects.equals(alternativeTitle, record.alternativeTitle) &&
				Objects.equals(periodicalNameStandardAbbrevation, record.periodicalNameStandardAbbrevation) &&
				Objects.equals(periodicalNameFullFormatJF, record.periodicalNameFullFormatJF) &&
				Objects.equals(periodicalNameFullFormatJO, record.periodicalNameFullFormatJO) &&
				Objects.equals(keywords, record.keywords) &&
				Objects.equals(pdfLinks, record.pdfLinks) &&
				Objects.equals(fullTextLinks, record.fullTextLinks) &&
				Objects.equals(relatedRecords, record.relatedRecords) &&
				Objects.equals(images, record.images) &&
				Objects.equals(language, record.language) &&
				Objects.equals(label, record.label) &&
				Objects.equals(websiteLink, record.websiteLink) &&
				Objects.equals(number, record.number) &&
				Objects.equals(miscellaneous2, record.miscellaneous2) &&
				Objects.equals(typeOfWork, record.typeOfWork) &&
				Objects.equals(notes, record.notes) &&
				Objects.equals(abstr2, record.abstr2) &&
				Objects.equals(originalPublication, record.originalPublication) &&
				Objects.equals(publisher, record.publisher) &&
				Objects.equals(publishingPlace, record.publishingPlace) &&
				Objects.equals(publicationYear, record.publicationYear) &&
				Objects.equals(reviewedItem, record.reviewedItem) &&
				Objects.equals(researchNotes, record.researchNotes) &&
				Objects.equals(reprintEdition, record.reprintEdition) &&
				Objects.equals(section, record.section) &&
				Objects.equals(isbnIssn, record.isbnIssn) &&
				Objects.equals(startPage, record.startPage) &&
				Objects.equals(shortTitle, record.shortTitle) &&
				Objects.equals(primaryTitle, record.primaryTitle) &&
				Objects.equals(secondaryTitle, record.secondaryTitle) &&
				Objects.equals(tertiaryTitle, record.tertiaryTitle) &&
				Objects.equals(translatedAuthor, record.translatedAuthor) &&
				Objects.equals(title, record.title) &&
				Objects.equals(transaltedTitle, record.transaltedTitle) &&
				Objects.equals(userDefinable1, record.userDefinable1) &&
				Objects.equals(userDefinable2, record.userDefinable2) &&
				Objects.equals(userDefinable3, record.userDefinable3) &&
				Objects.equals(userDefinable4, record.userDefinable4) &&
				Objects.equals(userDefinable5, record.userDefinable5) &&
				Objects.equals(url, record.url) &&
				Objects.equals(volumeNumber, record.volumeNumber) &&
				Objects.equals(publisherStandardNumber, record.publisherStandardNumber) &&
				Objects.equals(primaryDate, record.primaryDate) &&
				Objects.equals(accessDate, record.accessDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, firstAuthors, secondaryAuthors, tertiaryAuthors, subsidiaryAuthors, abstr,
				authorAddress, accessionNumber, authors, archivesLocation, bt, custom1, custom2, custom3, custom4,
				custom5, custom6, custom7, custom8, caption, callNumber, cp, unpublishedReferenceTitle, placePublished,
				date, databaseName, doi, databaseProvider, editor, endPage, edition, referenceId, issueNumber,
				periodicalNameUserAbbrevation, alternativeTitle, periodicalNameStandardAbbrevation,
				periodicalNameFullFormatJF, periodicalNameFullFormatJO, keywords, pdfLinks, fullTextLinks,
				relatedRecords, images, language, label, websiteLink, number, miscellaneous2, typeOfWork, notes, abstr2,
				originalPublication, publisher, publishingPlace, publicationYear, reviewedItem, researchNotes,
				reprintEdition, section, isbnIssn, startPage, shortTitle, primaryTitle, secondaryTitle, tertiaryTitle,
				translatedAuthor, title, transaltedTitle, userDefinable1, userDefinable2, userDefinable3,
				userDefinable4, userDefinable5, url, volumeNumber, publisherStandardNumber, primaryDate, accessDate);
	}
}
