package repository.gw.generate.cc;

public class ReserveLine {

    private String incidentName  = null;
    private String costCategory  = null;
    private String reserveAmount = null;
    private String comment       = null;
    private String exposureName  = null;
    private String exposureNumber = "1";
    private String exposureCoverage = null;
   
    public String getIncidentName() {
        return incidentName;
    }

    public String getCostCategory() {
        return costCategory;
    }

    public String getReserveAmount() {
        return reserveAmount;
    }

    public String getComment() {
        return comment;
    }

    public String getExposureName() {
        return exposureName;
    }

    public void setIncidentName(String incidentName) {
        this.incidentName = incidentName;
    }

    public void setCostCategory(String costCategory) {
        this.costCategory = costCategory;
    }

    public void setReserveAmount(String reserveAmount) {
        this.reserveAmount = reserveAmount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setExposureName(String exposureName) {
        this.exposureName = exposureName;
    }

    
    /**
     * @param incidentName - This is the incident that got picked at the fnol. 
     * @param costCategory - name of cost category you want. 
     * @param reserveAmount - reserve amount you want. 
     */
    public ReserveLine(String incidentName, String costCategory, String reserveAmount) {
        this.incidentName = incidentName;
        this.costCategory = costCategory;
        this.reserveAmount = reserveAmount;
        this.exposureNumber = "1";
    }

    public ReserveLine(String incidentName, String costCategory) {
        this.incidentName = incidentName;
        this.costCategory = costCategory;
        this.reserveAmount = "Random";
    }

    public ReserveLine(String incidentName) {
        this.incidentName = incidentName;
        this.costCategory = "Random";
        this.reserveAmount = "Random";
    }
    
    public ReserveLine(){
        this.incidentName = "Random";
        this.costCategory = "Random";
        this.reserveAmount = "Random";
    }

    public String getExposureNumber() {
        return exposureNumber;
    }

    public String getExposureCoverage() {
        return exposureCoverage;
    }

    public void setExposureNumber(String exposureNumber) {
        this.exposureNumber = exposureNumber;
    }

    public void setExposureCoverage(String exposureCoverage) {
        this.exposureCoverage = exposureCoverage;
    }

}
