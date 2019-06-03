
package ch.difty.scipamato.core.pubmed.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "year",
    "month",
    "day",
    "hour",
    "minute",
    "second"
})
@XmlRootElement(name = "PubMedPubDate")
public class PubMedPubDate {

    @XmlAttribute(name = "PubStatus", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String pubStatus;
    @XmlElement(name = "Year", required = true)
    protected Year year;
    @XmlElement(name = "Month", required = true)
    protected Month month;
    @XmlElement(name = "Day", required = true)
    protected Day day;
    @XmlElement(name = "Hour")
    protected String hour;
    @XmlElement(name = "Minute")
    protected String minute;
    @XmlElement(name = "Second")
    protected String second;

    /**
     * Ruft den Wert der pubStatus-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPubStatus() {
        return pubStatus;
    }

    /**
     * Legt den Wert der pubStatus-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPubStatus(String value) {
        this.pubStatus = value;
    }

    /**
     * Ruft den Wert der year-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Year }
     *     
     */
    public Year getYear() {
        return year;
    }

    /**
     * Legt den Wert der year-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Year }
     *     
     */
    public void setYear(Year value) {
        this.year = value;
    }

    /**
     * Ruft den Wert der month-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Month }
     *     
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Legt den Wert der month-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Month }
     *     
     */
    public void setMonth(Month value) {
        this.month = value;
    }

    /**
     * Ruft den Wert der day-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Day }
     *     
     */
    public Day getDay() {
        return day;
    }

    /**
     * Legt den Wert der day-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Day }
     *     
     */
    public void setDay(Day value) {
        this.day = value;
    }

    /**
     * Ruft den Wert der hour-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHour() {
        return hour;
    }

    /**
     * Legt den Wert der hour-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHour(String value) {
        this.hour = value;
    }

    /**
     * Ruft den Wert der minute-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinute() {
        return minute;
    }

    /**
     * Legt den Wert der minute-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinute(String value) {
        this.minute = value;
    }

    /**
     * Ruft den Wert der second-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecond() {
        return second;
    }

    /**
     * Legt den Wert der second-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecond(String value) {
        this.second = value;
    }

}
