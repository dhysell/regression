
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CoverageTerm complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CoverageTerm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoverageTerm", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "id",
        "code",
        "displayName",
        "description",
        "value",
        "publicID"
})
public class CoverageTerm {

    @XmlElement(name = "ID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String id;
    @XmlElement(name = "Code", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String code;
    @XmlElement(name = "DisplayName", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String displayName;
    @XmlElement(name = "Description", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String description;
    @XmlElement(name = "Value", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected float value;
    @XmlElement(name = "PublicID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String publicID;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the code property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the displayName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the value property.
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Gets the value of the publicID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPublicID() {
        return publicID;
    }

    /**
     * Sets the value of the publicID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublicID(String value) {
        this.publicID = value;
    }

}
