//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.18 at 11:07:32 AM EEST 
//


package org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
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
 *         &lt;element ref="{}iati-activity"/>
 *         &lt;any processContents='lax' namespace='##other'/>
 *       &lt;/choice>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="generated-datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="linked-data-default" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    "iatiActivityOrAny"
})
@XmlRootElement(name = "iati-activities")
public class IatiActivities {

    @XmlElementRef(name = "iati-activity", type = IatiActivity.class)
    @XmlAnyElement(lax = true)
    protected List<Object> iatiActivityOrAny;
    @XmlAttribute(name = "version")
    protected BigDecimal version;
    @XmlAttribute(name = "generated-datetime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar generatedDatetime;
    @XmlAttribute(name = "linked-data-default")
    protected String linkedDataDefault;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the iatiActivityOrAny property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the iatiActivityOrAny property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIatiActivityOrAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link Element }
     * {@link IatiActivity }
     * 
     * 
     */
    public List<Object> getIatiActivityOrAny() {
        if (iatiActivityOrAny == null) {
            iatiActivityOrAny = new ArrayList<Object>();
        }
        return this.iatiActivityOrAny;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVersion(BigDecimal value) {
        this.version = value;
    }

    /**
     * Gets the value of the generatedDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGeneratedDatetime() {
        return generatedDatetime;
    }

    /**
     * Sets the value of the generatedDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGeneratedDatetime(XMLGregorianCalendar value) {
        this.generatedDatetime = value;
    }

    /**
     * Gets the value of the linkedDataDefault property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkedDataDefault() {
        return linkedDataDefault;
    }

    /**
     * Sets the value of the linkedDataDefault property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkedDataDefault(String value) {
        this.linkedDataDefault = value;
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

}
