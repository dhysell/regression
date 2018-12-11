
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Owner complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Owner">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Lienholder" type="{http://www.idfbins.com/PolicyRetrieveResponse}Contact"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OwnerType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Owner", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "id",
        "lienholder",
        "displayName",
        "ownerType",
        "publicID"
})
public class Owner {

    @XmlElement(name = "ID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String id;
    @XmlElement(name = "Lienholder", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected Contact lienholder;
    @XmlElement(name = "DisplayName", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String displayName;
    @XmlElement(name = "OwnerType", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String ownerType;
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
     * Gets the value of the lienholder property.
     *
     * @return possible object is
     * {@link Contact }
     */
    public Contact getLienholder() {
        return lienholder;
    }

    /**
     * Sets the value of the lienholder property.
     *
     * @param value allowed object is
     *              {@link Contact }
     */
    public void setLienholder(Contact value) {
        this.lienholder = value;
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
     * Gets the value of the ownerType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOwnerType() {
        return ownerType;
    }

    /**
     * Sets the value of the ownerType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOwnerType(String value) {
        this.ownerType = value;
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
