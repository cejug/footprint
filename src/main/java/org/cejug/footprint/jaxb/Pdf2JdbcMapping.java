//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.02.18 at 09:58:49 PM BRT 
//

package org.cejug.footprint.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Pdf2JdbcMapping complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Pdf2JdbcMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="pdfField" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="databaseColumnName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="font" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pdf2JdbcMapping")
public class Pdf2JdbcMapping implements Serializable {

    private final static long serialVersionUID = -6908316602239211070L;
    @XmlAttribute(name = "pdfField")
    protected String pdfField;
    @XmlAttribute(name = "databaseColumnName")
    protected String databaseColumnName;
    @XmlAttribute(name = "font")
    protected String font;

    /**
     * Gets the value of the pdfField property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPdfField() {
        return pdfField;
    }

    /**
     * Sets the value of the pdfField property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPdfField(String value) {
        this.pdfField = value;
    }

    /**
     * Gets the value of the databaseColumnName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDatabaseColumnName() {
        return databaseColumnName;
    }

    /**
     * Sets the value of the databaseColumnName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDatabaseColumnName(String value) {
        this.databaseColumnName = value;
    }

    /**
     * Gets the value of the font property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFont() {
        return font;
    }

    /**
     * Sets the value of the font property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFont(String value) {
        this.font = value;
    }

}
