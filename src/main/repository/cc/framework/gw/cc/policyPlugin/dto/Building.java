
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Building complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Building">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BuildingNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Building", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "id",
        "notes",
        "buildingNumber",
        "publicID",
        "description"
})
public class Building {

    @XmlElement(name = "ID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String id;
    @XmlElement(name = "Notes", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String notes;
    @XmlElement(name = "BuildingNumber", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String buildingNumber;
    @XmlElement(name = "PublicID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String publicID;
    @XmlElement(name = "Description", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String description;

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
     * Gets the value of the notes property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    /**
     * Gets the value of the buildingNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBuildingNumber() {
        return buildingNumber;
    }

    /**
     * Sets the value of the buildingNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBuildingNumber(String value) {
        this.buildingNumber = value;
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

}
