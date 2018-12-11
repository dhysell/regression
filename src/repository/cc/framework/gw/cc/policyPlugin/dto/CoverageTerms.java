
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for CoverageTerms complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CoverageTerms">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CoverageTerm" type="{http://www.idfbins.com/PolicyRetrieveResponse}CoverageTerm" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoverageTerms", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "coverageTerm"
})
public class CoverageTerms {

    @XmlElement(name = "CoverageTerm", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected List<CoverageTerm> coverageTerm;

    /**
     * Gets the value of the coverageTerm property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coverageTerm property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoverageTerm().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CoverageTerm }
     */
    public List<CoverageTerm> getCoverageTerm() {
        if (coverageTerm == null) {
            coverageTerm = new ArrayList<CoverageTerm>();
        }
        return this.coverageTerm;
    }

}
