//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for materialChoiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="materialChoiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://cp.com/rules/client}climate_control_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}walls_and_ceilings_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}flooring_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}appliances_builtin_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}exterior_finish_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}roofing_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}foundation_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}fireplace_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}balcony_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}decks_and_patios_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}porch_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}parking_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}basement_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}kitchen_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}bathroom_material_type"/>
 *         &lt;element ref="{http://cp.com/rules/client}unknown_material_type"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "materialChoiceType", propOrder = {
    "climateControlMaterialType",
    "wallsAndCeilingsMaterialType",
    "flooringMaterialType",
    "appliancesBuiltinMaterialType",
    "exteriorFinishMaterialType",
    "roofingMaterialType",
    "foundationMaterialType",
    "fireplaceMaterialType",
    "balconyMaterialType",
    "decksAndPatiosMaterialType",
    "porchMaterialType",
    "parkingMaterialType",
    "basementMaterialType",
    "kitchenMaterialType",
    "bathroomMaterialType",
    "unknownMaterialType"
})
public class MaterialChoiceType {

    @XmlElement(name = "climate_control_material_type")
    protected ClimateControlMaterialTypeEnum climateControlMaterialType;
    @XmlElement(name = "walls_and_ceilings_material_type")
    protected WallsAndCeilingsMaterialTypeEnum wallsAndCeilingsMaterialType;
    @XmlElement(name = "flooring_material_type")
    protected FlooringMaterialTypeEnum flooringMaterialType;
    @XmlElement(name = "appliances_builtin_material_type")
    protected AppliancesBuiltinMaterialTypeEnum appliancesBuiltinMaterialType;
    @XmlElement(name = "exterior_finish_material_type")
    protected ExteriorFinishMaterialTypeEnum exteriorFinishMaterialType;
    @XmlElement(name = "roofing_material_type")
    protected RoofingMaterialTypeEnum roofingMaterialType;
    @XmlElement(name = "foundation_material_type")
    protected FoundationMaterialTypeEnum foundationMaterialType;
    @XmlElement(name = "fireplace_material_type")
    protected FireplaceMaterialTypeEnum fireplaceMaterialType;
    @XmlElement(name = "balcony_material_type")
    protected BalconyMaterialTypeEnum balconyMaterialType;
    @XmlElement(name = "decks_and_patios_material_type")
    protected DecksAndPatiosMaterialTypeEnum decksAndPatiosMaterialType;
    @XmlElement(name = "porch_material_type")
    protected PorchMaterialTypeEnum porchMaterialType;
    @XmlElement(name = "parking_material_type")
    protected ParkingMaterialTypeEnum parkingMaterialType;
    @XmlElement(name = "basement_material_type")
    protected BasementMaterialTypeEnum basementMaterialType;
    @XmlElement(name = "kitchen_material_type")
    protected KitchenMaterialTypeEnum kitchenMaterialType;
    @XmlElement(name = "bathroom_material_type")
    protected BathroomMaterialTypeEnum bathroomMaterialType;
    @XmlElement(name = "unknown_material_type")
    protected String unknownMaterialType;

    /**
     * Gets the value of the climateControlMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.ClimateControlMaterialTypeEnum }
     *     
     */
    public ClimateControlMaterialTypeEnum getClimateControlMaterialType() {
        return climateControlMaterialType;
    }

    /**
     * Sets the value of the climateControlMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.ClimateControlMaterialTypeEnum }
     *     
     */
    public void setClimateControlMaterialType(ClimateControlMaterialTypeEnum value) {
        this.climateControlMaterialType = value;
    }

    /**
     * Gets the value of the wallsAndCeilingsMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link WallsAndCeilingsMaterialTypeEnum }
     *     
     */
    public WallsAndCeilingsMaterialTypeEnum getWallsAndCeilingsMaterialType() {
        return wallsAndCeilingsMaterialType;
    }

    /**
     * Sets the value of the wallsAndCeilingsMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link WallsAndCeilingsMaterialTypeEnum }
     *     
     */
    public void setWallsAndCeilingsMaterialType(WallsAndCeilingsMaterialTypeEnum value) {
        this.wallsAndCeilingsMaterialType = value;
    }

    /**
     * Gets the value of the flooringMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link FlooringMaterialTypeEnum }
     *     
     */
    public FlooringMaterialTypeEnum getFlooringMaterialType() {
        return flooringMaterialType;
    }

    /**
     * Sets the value of the flooringMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlooringMaterialTypeEnum }
     *     
     */
    public void setFlooringMaterialType(FlooringMaterialTypeEnum value) {
        this.flooringMaterialType = value;
    }

    /**
     * Gets the value of the appliancesBuiltinMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.AppliancesBuiltinMaterialTypeEnum }
     *     
     */
    public AppliancesBuiltinMaterialTypeEnum getAppliancesBuiltinMaterialType() {
        return appliancesBuiltinMaterialType;
    }

    /**
     * Sets the value of the appliancesBuiltinMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.AppliancesBuiltinMaterialTypeEnum }
     *     
     */
    public void setAppliancesBuiltinMaterialType(AppliancesBuiltinMaterialTypeEnum value) {
        this.appliancesBuiltinMaterialType = value;
    }

    /**
     * Gets the value of the exteriorFinishMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.ExteriorFinishMaterialTypeEnum }
     *     
     */
    public ExteriorFinishMaterialTypeEnum getExteriorFinishMaterialType() {
        return exteriorFinishMaterialType;
    }

    /**
     * Sets the value of the exteriorFinishMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.ExteriorFinishMaterialTypeEnum }
     *     
     */
    public void setExteriorFinishMaterialType(ExteriorFinishMaterialTypeEnum value) {
        this.exteriorFinishMaterialType = value;
    }

    /**
     * Gets the value of the roofingMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.RoofingMaterialTypeEnum }
     *     
     */
    public RoofingMaterialTypeEnum getRoofingMaterialType() {
        return roofingMaterialType;
    }

    /**
     * Sets the value of the roofingMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.RoofingMaterialTypeEnum }
     *     
     */
    public void setRoofingMaterialType(RoofingMaterialTypeEnum value) {
        this.roofingMaterialType = value;
    }

    /**
     * Gets the value of the foundationMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link FoundationMaterialTypeEnum }
     *     
     */
    public FoundationMaterialTypeEnum getFoundationMaterialType() {
        return foundationMaterialType;
    }

    /**
     * Sets the value of the foundationMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FoundationMaterialTypeEnum }
     *     
     */
    public void setFoundationMaterialType(FoundationMaterialTypeEnum value) {
        this.foundationMaterialType = value;
    }

    /**
     * Gets the value of the fireplaceMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link FireplaceMaterialTypeEnum }
     *     
     */
    public FireplaceMaterialTypeEnum getFireplaceMaterialType() {
        return fireplaceMaterialType;
    }

    /**
     * Sets the value of the fireplaceMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FireplaceMaterialTypeEnum }
     *     
     */
    public void setFireplaceMaterialType(FireplaceMaterialTypeEnum value) {
        this.fireplaceMaterialType = value;
    }

    /**
     * Gets the value of the balconyMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link BalconyMaterialTypeEnum }
     *     
     */
    public BalconyMaterialTypeEnum getBalconyMaterialType() {
        return balconyMaterialType;
    }

    /**
     * Sets the value of the balconyMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BalconyMaterialTypeEnum }
     *     
     */
    public void setBalconyMaterialType(BalconyMaterialTypeEnum value) {
        this.balconyMaterialType = value;
    }

    /**
     * Gets the value of the decksAndPatiosMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.DecksAndPatiosMaterialTypeEnum }
     *     
     */
    public DecksAndPatiosMaterialTypeEnum getDecksAndPatiosMaterialType() {
        return decksAndPatiosMaterialType;
    }

    /**
     * Sets the value of the decksAndPatiosMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.DecksAndPatiosMaterialTypeEnum }
     *     
     */
    public void setDecksAndPatiosMaterialType(DecksAndPatiosMaterialTypeEnum value) {
        this.decksAndPatiosMaterialType = value;
    }

    /**
     * Gets the value of the porchMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.PorchMaterialTypeEnum }
     *     
     */
    public PorchMaterialTypeEnum getPorchMaterialType() {
        return porchMaterialType;
    }

    /**
     * Sets the value of the porchMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.PorchMaterialTypeEnum }
     *     
     */
    public void setPorchMaterialType(PorchMaterialTypeEnum value) {
        this.porchMaterialType = value;
    }

    /**
     * Gets the value of the parkingMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.ParkingMaterialTypeEnum }
     *     
     */
    public ParkingMaterialTypeEnum getParkingMaterialType() {
        return parkingMaterialType;
    }

    /**
     * Sets the value of the parkingMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.ParkingMaterialTypeEnum }
     *     
     */
    public void setParkingMaterialType(ParkingMaterialTypeEnum value) {
        this.parkingMaterialType = value;
    }

    /**
     * Gets the value of the basementMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link BasementMaterialTypeEnum }
     *     
     */
    public BasementMaterialTypeEnum getBasementMaterialType() {
        return basementMaterialType;
    }

    /**
     * Sets the value of the basementMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BasementMaterialTypeEnum }
     *     
     */
    public void setBasementMaterialType(BasementMaterialTypeEnum value) {
        this.basementMaterialType = value;
    }

    /**
     * Gets the value of the kitchenMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link KitchenMaterialTypeEnum }
     *     
     */
    public KitchenMaterialTypeEnum getKitchenMaterialType() {
        return kitchenMaterialType;
    }

    /**
     * Sets the value of the kitchenMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link KitchenMaterialTypeEnum }
     *     
     */
    public void setKitchenMaterialType(KitchenMaterialTypeEnum value) {
        this.kitchenMaterialType = value;
    }

    /**
     * Gets the value of the bathroomMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.BathroomMaterialTypeEnum }
     *     
     */
    public BathroomMaterialTypeEnum getBathroomMaterialType() {
        return bathroomMaterialType;
    }

    /**
     * Sets the value of the bathroomMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.BathroomMaterialTypeEnum }
     *     
     */
    public void setBathroomMaterialType(BathroomMaterialTypeEnum value) {
        this.bathroomMaterialType = value;
    }

    /**
     * Gets the value of the unknownMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnknownMaterialType() {
        return unknownMaterialType;
    }

    /**
     * Sets the value of the unknownMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnknownMaterialType(String value) {
        this.unknownMaterialType = value;
    }

}
