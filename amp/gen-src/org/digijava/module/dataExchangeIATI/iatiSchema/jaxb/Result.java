//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.18 at 11:07:31 AM EEST 
//


package org.digijava.module.dataExchangeIATI.iatiSchema.jaxb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}title"/>
 *         &lt;element ref="{}description"/>
 *         &lt;element name="indicator">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{}title"/>
 *                   &lt;element ref="{}description"/>
 *                   &lt;element name="baseline" type="{}indicatorOutcomeType"/>
 *                   &lt;element name="target" type="{}indicatorOutcomeType"/>
 *                   &lt;element name="actual" type="{}indicatorOutcomeType"/>
 *                   &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/choice>
 *                 &lt;attribute name="measure" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;any processContents='lax' namespace='##other'/>
 *       &lt;/choice>
 *       &lt;attribute ref="{}type use="required""/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "titleOrDescriptionOrIndicator"
})
@XmlRootElement(name = "result")
public class Result {

    @XmlElementRefs({
        @XmlElementRef(name = "description", type = Description.class),
        @XmlElementRef(name = "indicator", type = JAXBElement.class),
        @XmlElementRef(name = "title", type = JAXBElement.class)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> titleOrDescriptionOrIndicator;
    @XmlAttribute(name = "type", required = true)
    protected String type;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the titleOrDescriptionOrIndicator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the titleOrDescriptionOrIndicator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTitleOrDescriptionOrIndicator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Description }
     * {@link JAXBElement }{@code <}{@link Result.Indicator }{@code >}
     * {@link JAXBElement }{@code <}{@link TextType }{@code >}
     * {@link Object }
     * {@link Element }
     * 
     * 
     */
    public List<Object> getTitleOrDescriptionOrIndicator() {
        if (titleOrDescriptionOrIndicator == null) {
            titleOrDescriptionOrIndicator = new ArrayList<Object>();
        }
        return this.titleOrDescriptionOrIndicator;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice maxOccurs="unbounded" minOccurs="0">
     *         &lt;element ref="{}title"/>
     *         &lt;element ref="{}description"/>
     *         &lt;element name="baseline" type="{}indicatorOutcomeType"/>
     *         &lt;element name="target" type="{}indicatorOutcomeType"/>
     *         &lt;element name="actual" type="{}indicatorOutcomeType"/>
     *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/choice>
     *       &lt;attribute name="measure" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "titleOrDescriptionOrBaseline"
    })
    public static class Indicator {

        @XmlElementRefs({
            @XmlElementRef(name = "baseline", type = JAXBElement.class),
            @XmlElementRef(name = "actual", type = JAXBElement.class),
            @XmlElementRef(name = "description", type = Description.class),
            @XmlElementRef(name = "title", type = JAXBElement.class),
            @XmlElementRef(name = "target", type = JAXBElement.class)
        })
        @XmlAnyElement(lax = true)
        protected List<Object> titleOrDescriptionOrBaseline;
        @XmlAttribute(name = "measure", required = true)
        @XmlSchemaType(name = "anySimpleType")
        protected String measure;

        /**
         * Gets the value of the titleOrDescriptionOrBaseline property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the titleOrDescriptionOrBaseline property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTitleOrDescriptionOrBaseline().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Object }
         * {@link JAXBElement }{@code <}{@link IndicatorOutcomeType }{@code >}
         * {@link Element }
         * {@link JAXBElement }{@code <}{@link IndicatorOutcomeType }{@code >}
         * {@link Description }
         * {@link JAXBElement }{@code <}{@link TextType }{@code >}
         * {@link JAXBElement }{@code <}{@link IndicatorOutcomeType }{@code >}
         * 
         * 
         */
        public List<Object> getTitleOrDescriptionOrBaseline() {
            if (titleOrDescriptionOrBaseline == null) {
                titleOrDescriptionOrBaseline = new ArrayList<Object>();
            }
            return this.titleOrDescriptionOrBaseline;
        }

        /**
         * Gets the value of the measure property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMeasure() {
            return measure;
        }

        /**
         * Sets the value of the measure property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMeasure(String value) {
            this.measure = value;
        }

    }

}
