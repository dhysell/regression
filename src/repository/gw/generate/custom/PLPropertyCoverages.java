package repository.gw.generate.custom;

public class PLPropertyCoverages {

	// Section I Coverages
	private repository.gw.generate.custom.Coverage_A coverageA = new repository.gw.generate.custom.Coverage_A();
	private repository.gw.generate.custom.Coverage_C coverageC = new repository.gw.generate.custom.Coverage_C();
	private repository.gw.generate.custom.Coverage_E coverageE = new repository.gw.generate.custom.Coverage_E();
	
	private boolean hasCoverageC = true;
	
	private boolean earthquake = false;
	private boolean includeMasonry = false;
	
	private boolean guns = false;
	private int guns_Limit = 4000;
	private int guns_IncreaseTheftLimit = 0;
	
	private boolean silverware = false;
	private int silverware_Limit = 3500;
	private int silverware_IncreasedTheftLimit = 0;
	
	private boolean tools = false;
	private int tools_limit = 8000;
	private int tools_increasedLimit = 0;
	
	private boolean saddlesAndtack = false;
	private int saddlesAndTack_Limit = 3000;
	private int saddlesAndTack_IncreasedLimit = 0;
	

	public boolean isGuns() {
		return guns;
	}

	public void setGuns(boolean guns) {
		this.guns = guns;
	}

	public int getGuns_Limit() {
		return guns_Limit;
	}

	public void setGuns_Limit(int guns_Limit) {
		this.guns_Limit = guns_Limit;
	}

	public int getGuns_IncreaseTheftLimit() {
		return guns_IncreaseTheftLimit;
	}

	public void setGuns_IncreaseTheftLimit(int guns_IncreaseTheftLimit) {
		this.guns_IncreaseTheftLimit = guns_IncreaseTheftLimit;
	}

	public boolean isSilverware() {
		return silverware;
	}

	public void setSilverware(boolean silverware) {
		this.silverware = silverware;
	}

	public int getSilverware_Limit() {
		return silverware_Limit;
	}

	public void setSilverware_Limit(int silverware_Limit) {
		this.silverware_Limit = silverware_Limit;
	}

	public int getSilverware_IncreasedTheftLimit() {
		return silverware_IncreasedTheftLimit;
	}

	public void setSilverware_IncreasedTheftLimit(int silverware_IncreasedTheftLimit) {
		this.silverware_IncreasedTheftLimit = silverware_IncreasedTheftLimit;
	}

	public boolean isTools() {
		return tools;
	}

	public void setTools(boolean tools) {
		this.tools = tools;
	}

	public int getTools_limit() {
		return tools_limit;
	}

	public void setTools_limit(int tools_limit) {
		this.tools_limit = tools_limit;
	}

	public int getTools_increasedLimit() {
		return tools_increasedLimit;
	}

	public void setTools_increasedLimit(int tools_increasedLimit) {
		this.tools_increasedLimit = tools_increasedLimit;
	}

	public boolean isSaddlesAndtack() {
		return saddlesAndtack;
	}

	public void setSaddlesAndtack(boolean saddlesAndtack) {
		this.saddlesAndtack = saddlesAndtack;
	}

	public int getSaddlesAndTack_Limit() {
		return saddlesAndTack_Limit;
	}

	public void setSaddlesAndTack_Limit(int saddlesAndTack_Limit) {
		this.saddlesAndTack_Limit = saddlesAndTack_Limit;
	}

	public int getSaddlesAndTack_IncreasedLimit() {
		return saddlesAndTack_IncreasedLimit;
	}

	public void setSaddlesAndTack_IncreasedLimit(int saddlesAndTack_IncreasedLimit) {
		this.saddlesAndTack_IncreasedLimit = saddlesAndTack_IncreasedLimit;
	}

	public repository.gw.generate.custom.Coverage_E getCoverageE() {
		return coverageE;
	}

	public void setCoverageE(Coverage_E coverageE) {
		this.coverageE = coverageE;
	}

	public repository.gw.generate.custom.Coverage_C getCoverageC() {
		return coverageC;
	}

	public void setCoverageC(Coverage_C coverageC) {
		this.coverageC = coverageC;
	}

	public repository.gw.generate.custom.Coverage_A getCoverageA() {
		return coverageA;
	}

	public void setCoverageA(Coverage_A coverageA) {
		this.coverageA = coverageA;
	}


	public boolean isEarthquake() {
		return earthquake;
	}

	public void setEarthquake(boolean earthquake) {
		this.earthquake = earthquake;
	}

	public boolean isIncludeMasonry() {
		return includeMasonry;
	}

	public void setIncludeMasonry(boolean includeMasonry) {
		this.includeMasonry = includeMasonry;
	}

	public boolean hasCoverageC() {
		return hasCoverageC;
	}

	public void setHasCoverageC(boolean hasCoverageC) {
		this.hasCoverageC = hasCoverageC;
	}

	
	
	
	
}
