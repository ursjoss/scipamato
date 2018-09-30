//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 generiert 
// Siehe <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.09.30 um 04:04:35 PM CEST 
//

package ch.difty.scipamato.core.pubmed.api;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
@XmlRootElement(name = "AbstractText")
public class AbstractText {

    @XmlAttribute(name = "Label")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String label;
    @XmlAttribute(name = "NlmCategory")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String nlmCategory;
    @XmlMixed
    @XmlElementRefs({ @XmlElementRef(name = "b", type = B.class), @XmlElementRef(name = "i", type = I.class),
        @XmlElementRef(name = "u", type = U.class), @XmlElementRef(name = "sub", type = Sub.class),
        @XmlElementRef(name = "sup", type = Sup.class) })
    protected List<java.lang.Object> mixedContent;

    /**
     * Ruft den Wert der label-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getLabel() {
        return label;
    }

    /**
     * Legt den Wert der label-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Ruft den Wert der nlmCategory-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getNlmCategory() {
        return nlmCategory;
    }

    /**
     * Legt den Wert der nlmCategory-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setNlmCategory(String value) {
        this.nlmCategory = value;
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
        return mixedContent
            .stream()
            .map(Objects::toString)
            .collect(Collectors.joining(""));
    }
}
