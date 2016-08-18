//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.18 at 11:07:33 AM EEST 
//


package org.digijava.module.xmlpatcher.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for patch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="patch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="jira" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyword" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="author" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deprecate" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="trigger" type="{http://docs.ampdev.net/schemas/xmlpatcher}trigger" minOccurs="0"/>
 *         &lt;element name="apply" type="{http://docs.ampdev.net/schemas/xmlpatcher}scriptGroup"/>
 *         &lt;element name="rollback" type="{http://docs.ampdev.net/schemas/xmlpatcher}rollback" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="retryOnFail" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="closeOnSuccess" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "patch", propOrder = {
    "jira",
    "keyword",
    "author",
    "description",
    "deprecate",
    "trigger",
    "apply",
    "rollback"
})
public class Patch {

    protected String jira;
    protected List<String> keyword;
    @XmlElement(required = true)
    protected List<String> author;
    protected String description;
    protected List<String> deprecate;
    protected Trigger trigger;
    @XmlElement(required = true)
    protected ScriptGroup apply;
    protected Rollback rollback;
    @XmlAttribute(name = "retryOnFail")
    protected Boolean retryOnFail;
    @XmlAttribute(name = "closeOnSuccess")
    protected Boolean closeOnSuccess;

    /**
     * Gets the value of the jira property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJira() {
        return jira;
    }

    /**
     * Sets the value of the jira property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJira(String value) {
        this.jira = value;
    }

    /**
     * Gets the value of the keyword property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyword property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyword().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getKeyword() {
        if (keyword == null) {
            keyword = new ArrayList<String>();
        }
        return this.keyword;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAuthor() {
        if (author == null) {
            author = new ArrayList<String>();
        }
        return this.author;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the deprecate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deprecate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeprecate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDeprecate() {
        if (deprecate == null) {
            deprecate = new ArrayList<String>();
        }
        return this.deprecate;
    }

    /**
     * Gets the value of the trigger property.
     * 
     * @return
     *     possible object is
     *     {@link Trigger }
     *     
     */
    public Trigger getTrigger() {
        return trigger;
    }

    /**
     * Sets the value of the trigger property.
     * 
     * @param value
     *     allowed object is
     *     {@link Trigger }
     *     
     */
    public void setTrigger(Trigger value) {
        this.trigger = value;
    }

    /**
     * Gets the value of the apply property.
     * 
     * @return
     *     possible object is
     *     {@link ScriptGroup }
     *     
     */
    public ScriptGroup getApply() {
        return apply;
    }

    /**
     * Sets the value of the apply property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScriptGroup }
     *     
     */
    public void setApply(ScriptGroup value) {
        this.apply = value;
    }

    /**
     * Gets the value of the rollback property.
     * 
     * @return
     *     possible object is
     *     {@link Rollback }
     *     
     */
    public Rollback getRollback() {
        return rollback;
    }

    /**
     * Sets the value of the rollback property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rollback }
     *     
     */
    public void setRollback(Rollback value) {
        this.rollback = value;
    }

    /**
     * Gets the value of the retryOnFail property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRetryOnFail() {
        if (retryOnFail == null) {
            return false;
        } else {
            return retryOnFail;
        }
    }

    /**
     * Sets the value of the retryOnFail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRetryOnFail(Boolean value) {
        this.retryOnFail = value;
    }

    /**
     * Gets the value of the closeOnSuccess property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCloseOnSuccess() {
        if (closeOnSuccess == null) {
            return true;
        } else {
            return closeOnSuccess;
        }
    }

    /**
     * Sets the value of the closeOnSuccess property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCloseOnSuccess(Boolean value) {
        this.closeOnSuccess = value;
    }

}
