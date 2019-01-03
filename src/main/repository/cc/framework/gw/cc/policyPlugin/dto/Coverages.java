
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for Coverages complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Coverages">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Coverage" type="{http://www.idfbins.com/PolicyRetrieveResponse}Coverage" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Coverages", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "coverage"
})
public class Coverages {

    @XmlElement(name = "Coverage", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected List<Coverage> coverage;

    /**
     * Gets the value of the coverage property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coverage property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoverage().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Coverage }
     */
    public List<Coverage> getCoverage() {
        if (coverage == null) {
            coverage = new ArrayList<Coverage>();
        }
        return this.coverage;
    }

    public boolean hasCoverage(String requestedCoverage) {
        for (Coverage coverageItem : this.coverage) {
            if (coverageItem.getCoverageType().description.equalsIgnoreCase(requestedCoverage)) {
                return true;
            }
        }
        return false;
    }

}
