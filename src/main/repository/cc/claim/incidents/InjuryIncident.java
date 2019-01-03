package repository.cc.claim.incidents;

public class InjuryIncident {


    private String injuredPerson = null;
    private String severity = null;
    private String injuryDescription = null;
    private String generalInjuryType = null;
    private String detailedInjuryType = null;
    private String areaOfBody = null;
    private String detailedBodyPart = null;

    public InjuryIncident() {
    }

    public String getInjuredPerson() {
        return injuredPerson;
    }

    public void setInjuredPerson(String injuredPerson) {
        this.injuredPerson = injuredPerson;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getInjuryDescription() {
        return injuryDescription;
    }

    public void setInjuryDescription(String injuryDescription) {
        this.injuryDescription = injuryDescription;
    }

    public String getGeneralInjuryType() {
        return generalInjuryType;
    }

    public void setGeneralInjuryType(String generalInjuryType) {
        this.generalInjuryType = generalInjuryType;
    }

    public String getDetailedInjuryType() {
        return detailedInjuryType;
    }

    public void setDetailedInjuryType(String detailedInjuryType) {
        this.detailedInjuryType = detailedInjuryType;
    }

    public String getAreaOfBody() {
        return areaOfBody;
    }

    public void setAreaOfBody(String areaOfBody) {
        this.areaOfBody = areaOfBody;
    }

    public String getDetailedBodyPart() {
        return detailedBodyPart;
    }

    public void setDetailedBodyPart(String detailedBodyPart) {
        this.detailedBodyPart = detailedBodyPart;
    }
}
