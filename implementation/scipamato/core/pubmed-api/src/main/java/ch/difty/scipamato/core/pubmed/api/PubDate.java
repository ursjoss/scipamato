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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "yearOrMonthOrDayOrSeasonOrMedlineDate"
})
@XmlRootElement(name = "PubDate")
public class PubDate {

    @XmlElements({
        @XmlElement(name = "Year", required = true, type = Year.class),
        @XmlElement(name = "Month", required = true, type = Month.class),
        @XmlElement(name = "Day", required = true, type = Day.class),
        @XmlElement(name = "Season", required = true, type = Season.class),
        @XmlElement(name = "MedlineDate", required = true, type = MedlineDate.class)
    })
    protected List<java.lang.Object> yearOrMonthOrDayOrSeasonOrMedlineDate;

    /**
     * Gets the value of the yearOrMonthOrDayOrSeasonOrMedlineDate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the yearOrMonthOrDayOrSeasonOrMedlineDate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getYearOrMonthOrDayOrSeasonOrMedlineDate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Year }
     * {@link Month }
     * {@link Day }
     * {@link Season }
     * {@link MedlineDate }
     * 
     * 
     */
    public List<java.lang.Object> getYearOrMonthOrDayOrSeasonOrMedlineDate() {
        if (yearOrMonthOrDayOrSeasonOrMedlineDate == null) {
            yearOrMonthOrDayOrSeasonOrMedlineDate = new ArrayList<java.lang.Object>();
        }
        return this.yearOrMonthOrDayOrSeasonOrMedlineDate;
    }

}
