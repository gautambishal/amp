//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.06.19 at 04:51:40 GMT+04:00 
//


package org.digijava.module.ampharvester.jaxb10;


/**
 * Java content class for codeValueType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/java/PowerDot/source/aida/src/conf/org/digijava/module/ampharvester/jaxb10/AMP.1.0.xsd line 82)
 * <p>
 * <pre>
 * &lt;complexType name="codeValueType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="language" type="{http://www.w3.org/2001/XMLSchema}language" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CodeValueType {


    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCode();

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCode(java.lang.String value);

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getLanguage();

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setLanguage(java.lang.String value);

}
