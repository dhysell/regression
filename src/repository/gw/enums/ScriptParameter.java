package repository.gw.enums;

public enum ScriptParameter {
	
	//Script Parameters for PC
	PredictiveAnalyticsEnabled ("PredictiveAnalyticsEnabled"),
	PredictiveAnalyticsUIDebugEnabled ("PredictiveAnalyticsUIDebugEnabled"),
	VeriskCreditScoreEnabled ("VeriskCreditScoreEnabled"),
	VeriskCVReportEnabled ("VeriskCVReportEnabled"),
	VeriskFeatureToggle ("VeriskFeatureToggle"),
	VeriskFeatureToggleProperty ("VeriskFeatureToggle_Property"),
	VeriskMVREnabled("VeriskMVREnabled"),
	
	//Script Parameters for BC

	
	//Script Parameters for AB
	
	
	//Script Parameters for CC
    EnableLexisNexisPoliceReports("EnableLexisNexisPoliceReports"),
    EnableCopart("EnableCopart");

    private String value;

    private ScriptParameter(String scriptParameter) {
    	value = scriptParameter;
    }

    public String getValue() {
        return this.value;
    }
}
