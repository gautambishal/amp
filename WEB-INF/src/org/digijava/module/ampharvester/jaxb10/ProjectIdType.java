//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.06.19 at 04:51:40 GMT+04:00 
//


package org.digijava.module.ampharvester.jaxb10;


/**
 * Java content class for projectIdType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/java/PowerDot/source/aida/src/conf/org/digijava/module/ampharvester/jaxb10/AMP.1.0.xsd line 54)
 * <p>
 * <pre>
 * &lt;complexType name="projectIdType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="organization" type="{}codeValueType"/>
 *         &lt;element name="uniqID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProjectIdType {


    /**
     * Gets the value of the uniqID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUniqID();

    /**
     * Sets the value of the uniqID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUniqID(java.lang.String value);

    /**
     * Gets the value of the organization property.
     * 
     * @return
     *     possible object is
     *     {@link org.digijava.module.ampharvester.jaxb10.CodeValueType}
     */
    org.digijava.module.ampharvester.jaxb10.CodeValueType getOrganization();

    /**
     * Sets the value of the organization property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.digijava.module.ampharvester.jaxb10.CodeValueType}
     */
    void setOrganization(org.digijava.module.ampharvester.jaxb10.CodeValueType value);

}
