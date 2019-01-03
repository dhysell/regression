
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PolicyCoverage complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PolicyCoverage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Coverages" type="{http://www.idfbins.com/PolicyRetrieveResponse}Coverages"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyCoverage", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "coverages"
})
public class PolicyCoverage {

    @XmlElement(name = "Coverages", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected Coverages coverages;

    /**
     * Gets the value of the coverages property.
     *
     * @return possible object is
     * {@link Coverages }
     */
    public Coverages getCoverages() {
        return coverages;
    }

    /**
     * Sets the value of the coverages property.
     *
     * @param value allowed object is
     *              {@link Coverages }
     */
    public void setCoverages(Coverages value) {
        this.coverages = value;
    }

}
