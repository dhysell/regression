package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineMiscellaneousArticles {

	private InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Deductible deductible = InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Deductible.FiveHundred;
	private InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Coinsurance coinsurance = InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Coinsurance.EightlyPercent;
	
	private boolean miscellaneousArticlesBlanketCoverage = true;
	private int blanketCoveragePerOccurrance = 1000;
	private String blanketCoverageDescription = "This is a miscellaneous articles blanket coverage description";
	
	private List<CPPInlandMarineMiscellaneousArticles_ScheduledItem> scheduledItems = new ArrayList<CPPInlandMarineMiscellaneousArticles_ScheduledItem>();
	
	private boolean waterExclusion = false;
	
	public CPPInlandMarineMiscellaneousArticles(List<CPPInlandMarineMiscellaneousArticles_ScheduledItem> scheduledItems) {
		this.scheduledItems = scheduledItems;
	}

	public InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Deductible deductible) {
		this.deductible = deductible;
	}

	public InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Coinsurance getCoinsurance() {
		return coinsurance;
	}

	public void setCoinsurance(InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Coinsurance coinsurance) {
		this.coinsurance = coinsurance;
	}

	public boolean isMiscellaneousArticlesBlanketCoverage() {
		return miscellaneousArticlesBlanketCoverage;
	}

	public void setMiscellaneousArticlesBlanketCoverage(boolean miscellaneousArticlesBlanketCoverage) {
		this.miscellaneousArticlesBlanketCoverage = miscellaneousArticlesBlanketCoverage;
	}

	public int getBlanketCoveragePerOccurrance() {
		return blanketCoveragePerOccurrance;
	}

	public void setBlanketCoveragePerOccurrance(int blanketCoveragePerOccurrance) {
		this.blanketCoveragePerOccurrance = blanketCoveragePerOccurrance;
	}

	public String getBlanketCoverageDescription() {
		return blanketCoverageDescription;
	}

	public void setBlanketCoverageDescription(String blanketCoverageDescription) {
		this.blanketCoverageDescription = blanketCoverageDescription;
	}

	public List<CPPInlandMarineMiscellaneousArticles_ScheduledItem> getScheduledItems() {
		return scheduledItems;
	}

	public void setScheduledItems(List<CPPInlandMarineMiscellaneousArticles_ScheduledItem> scheduledItems) {
		this.scheduledItems = scheduledItems;
	}

	public boolean isWaterExclusion() {
		return waterExclusion;
	}

	public void setWaterExclusion(boolean waterExclusion) {
		this.waterExclusion = waterExclusion;
	}

}
