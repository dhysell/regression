
package repository.cc.framework.gw.cc.policyPlugin.dto;

import repository.gw.helpers.NumberUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RiskUnits complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RiskUnits">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RiskUnit" type="{http://www.idfbins.com/PolicyRetrieveResponse}RiskUnit" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiskUnits", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "riskUnit"
})
public class RiskUnits {

    @XmlElement(name = "RiskUnit", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected List<RiskUnit> riskUnit;

    /**
     * Gets the value of the riskUnit property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the riskUnit property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRiskUnit().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RiskUnit }
     */
    public List<RiskUnit> getRiskUnit() {
        if (riskUnit == null) {
            riskUnit = new ArrayList<RiskUnit>();
        }
        return this.riskUnit;
    }

    public List<String> getVehiclesAsStrings() {
        List<String> vehicleStrings = new ArrayList<>();

        for (RiskUnit riskUnit : this.riskUnit) {
            vehicleStrings.add(riskUnit.getVehicle().toString());
        }
        return vehicleStrings;
    }

    public List<String> getVehiclesWithCoverage(String coverage) {
        List<String> vehicleStrings = new ArrayList<>();

        for (RiskUnit riskUnit : this.riskUnit) {
            if (riskUnit.getCoverages().hasCoverage(coverage)) {
                vehicleStrings.add(riskUnit.getVehicle().toString());
            }
        }
        return vehicleStrings;
    }

    public String getRandomVehicleWithCoverage(String coverage) {
        List<String> vehicleStrings = new ArrayList<>();
        int count = 0;

        for (RiskUnit riskUnit : this.riskUnit) {
            if (riskUnit.getCoverages().hasCoverage(coverage)) {
                vehicleStrings.add(riskUnit.getVehicle().toString());
                count++;
            }
        }
        return vehicleStrings.get(NumberUtils.generateRandomNumberInt(0, count - 1));
    }
}
