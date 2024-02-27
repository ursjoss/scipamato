
package ch.difty.scipamato.core.pubmed.api;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.NormalizedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "mixedContent"
})
@XmlRootElement(name = "ArticleTitle")
public class ArticleTitle {

    @XmlAttribute(name = "book")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String book;
    @XmlAttribute(name = "part")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String part;
    @XmlAttribute(name = "sec")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String sec;
    @XmlMixed
    @XmlElementRefs({ @XmlElementRef(name = "b", type = B.class), @XmlElementRef(name = "i", type = I.class),
        @XmlElementRef(name = "u", type = U.class), @XmlElementRef(name = "sub", type = Sub.class),
        @XmlElementRef(name = "sup", type = Sup.class) })
    protected List<java.lang.Object> mixedContent;

    /**
     * Ruft den Wert der book-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBook() {
        return book;
    }

    /**
     * Legt den Wert der book-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBook(String value) {
        this.book = value;
    }

    /**
     * Ruft den Wert der part-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPart() {
        return part;
    }

    /**
     * Legt den Wert der part-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPart(String value) {
        this.part = value;
    }

    /**
     * Ruft den Wert der sec-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSec() {
        return sec;
    }

    /**
     * Legt den Wert der sec-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSec(String value) {
        this.sec = value;
    }

    public List<java.lang.Object> getMixedContent() {
        if (mixedContent == null)
            mixedContent = new ArrayList<>();
        return mixedContent;
    }

    public void setMixedContent(List<java.lang.Object> mixedContent) {
        this.mixedContent = mixedContent;
    }

    public String getvalue() {
        if (mixedContent == null)
            return null;
        return getMixedContent()
            .stream()
            .map(Objects::toString)
            .collect(Collectors.joining(""));
    }

}
