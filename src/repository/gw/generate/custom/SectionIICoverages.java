package repository.gw.generate.custom;

import java.util.ArrayList;

public class SectionIICoverages {
	
	private repository.gw.enums.Property.SectionIICoveragesEnum sectionIICoverage = null;
	private int quantity = 0;
	private int limit = 1001;
	private ArrayList<repository.gw.generate.custom.SquireLiablityCoverageIncidentalOccupancyItem> coverageIncidentalOccupancyItems = new ArrayList<repository.gw.generate.custom.SquireLiablityCoverageIncidentalOccupancyItem>();
	private repository.gw.enums.Property.SectionIICoverageNamedPersonsMedicalDeductible coverageNamedPersonsMedicalDeductible = null;
	private ArrayList<repository.gw.generate.custom.SquireLiablityCoverageNamedPersonsMedicalPerson> coverageNamedPersonsMedicalPersons = new ArrayList<repository.gw.generate.custom.SquireLiablityCoverageNamedPersonsMedicalPerson>();
	private ArrayList<repository.gw.generate.custom.SquireLiablityCoverageWatercraftLengthItem> coverageWatercraftLengthItems = new ArrayList<repository.gw.generate.custom.SquireLiablityCoverageWatercraftLengthItem>();
	private ArrayList<repository.gw.generate.custom.SquireLiablityCoverageLivestockItem> coverageLivestockItems = new ArrayList<repository.gw.generate.custom.SquireLiablityCoverageLivestockItem>();
	
	
	public SectionIICoverages(repository.gw.enums.Property.SectionIICoveragesEnum coverage, int limit, int quantity) {
		this.sectionIICoverage = coverage;
		this.quantity = quantity;
		this.limit = limit;
	}

	@SuppressWarnings("unchecked")
	public SectionIICoverages(repository.gw.enums.Property.SectionIICoveragesEnum coverage, ArrayList<?> sectionIIList) {
		this.sectionIICoverage = coverage;
		
		switch(coverage) {
		case IncidentalOccupancy:
			this.coverageIncidentalOccupancyItems = (ArrayList<repository.gw.generate.custom.SquireLiablityCoverageIncidentalOccupancyItem>) sectionIIList;
			break;
		case Livestock:
			this.coverageLivestockItems = (ArrayList<repository.gw.generate.custom.SquireLiablityCoverageLivestockItem>) sectionIIList;
			break;
		case NamedPersonsMedical:
			this.coverageNamedPersonsMedicalPersons = (ArrayList<repository.gw.generate.custom.SquireLiablityCoverageNamedPersonsMedicalPerson>) sectionIIList;
			break;
		case WatercraftLength:
			this.coverageWatercraftLengthItems = (ArrayList<repository.gw.generate.custom.SquireLiablityCoverageWatercraftLengthItem>) sectionIIList;
			break;
		default:
			break;
		}
	}

	
	
	
	
	
	
	
	
	
	public repository.gw.enums.Property.SectionIICoveragesEnum getSectionIICoverage() {
		return sectionIICoverage;
	}
	public void setSectionIICoverage(repository.gw.enums.Property.SectionIICoveragesEnum sectionIICoverage) {
		this.sectionIICoverage = sectionIICoverage;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public ArrayList<repository.gw.generate.custom.SquireLiablityCoverageIncidentalOccupancyItem> getCoverageIncidentalOccupancyItems() {
		return coverageIncidentalOccupancyItems;
	}
	public void setCoverageIncidentalOccupancyItems(
			ArrayList<SquireLiablityCoverageIncidentalOccupancyItem> coverageIncidentalOccupancyItems) {
		this.coverageIncidentalOccupancyItems = coverageIncidentalOccupancyItems;
	}
	public repository.gw.enums.Property.SectionIICoverageNamedPersonsMedicalDeductible getCoverageNamedPersonsMedicalDeductible() {
		return coverageNamedPersonsMedicalDeductible;
	}
	public void setCoverageNamedPersonsMedicalDeductible(
			repository.gw.enums.Property.SectionIICoverageNamedPersonsMedicalDeductible coverageNamedPersonsMedicalDeductible) {
		this.coverageNamedPersonsMedicalDeductible = coverageNamedPersonsMedicalDeductible;
	}
	public ArrayList<repository.gw.generate.custom.SquireLiablityCoverageNamedPersonsMedicalPerson> getCoverageNamedPersonsMedicalPersons() {
		return coverageNamedPersonsMedicalPersons;
	}
	public void setCoverageNamedPersonsMedicalPersons(
			ArrayList<SquireLiablityCoverageNamedPersonsMedicalPerson> coverageNamedPersonsMedicalPersons) {
		this.coverageNamedPersonsMedicalPersons = coverageNamedPersonsMedicalPersons;
	}
	public ArrayList<repository.gw.generate.custom.SquireLiablityCoverageWatercraftLengthItem> getCoverageWatercraftLengthItems() {
		return coverageWatercraftLengthItems;
	}
	public void setCoverageWatercraftLengthItems(
			ArrayList<SquireLiablityCoverageWatercraftLengthItem> coverageWatercraftLengthItems) {
		this.coverageWatercraftLengthItems = coverageWatercraftLengthItems;
	}
	public ArrayList<repository.gw.generate.custom.SquireLiablityCoverageLivestockItem> getCoverageLivestockItems() {
		return coverageLivestockItems;
	}
	public void setCoverageLivestockItems(ArrayList<SquireLiablityCoverageLivestockItem> coverageLivestockItems) {
		this.coverageLivestockItems = coverageLivestockItems;
	}
	
	
	

}
