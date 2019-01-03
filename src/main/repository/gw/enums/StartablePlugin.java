package repository.gw.enums;

public enum StartablePlugin {
	//Plugins Shared Across More Than One Center
	IEdgeBootstrapperv700("IEdgeBootstrapperv700"),
	
	//AB Startable Plugins
	ISolrSystemPluginFBM("ISolrSystemPluginFBM"),
	
	//PC Startable Plugins
	CountyPlugin("CountyPlugin"),
	FormInferenceConfigurationPlugin("FormInferenceConfigurationPlugin"),
	IEdgeBootstrapper("IEdgeBootstrapper"),
	IPortalSingleSignOnPlugin("IPortalSingleSignOnPlugin"),
	ITestingClock("ITestingClock"),
	RuleEntityGraphStartablePlugin("RuleEntityGraphStartablePlugin");
	
	String value;
	
	private StartablePlugin(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}

}
